import React, { useState, useEffect } from "react";
import axios from "axios";

const SearchAndFollow = ({ fetchFeed }) => {
  const [searchQuery, setSearchQuery] = useState("");
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);
  const userId = sessionStorage.getItem("userId");
  const [filterType, setFilterType] = useState("public"); // New state for filter type

  const handleSearch = async () => {
    if (!searchQuery) return;
    try {
      const endpoint = filterType === "public"
          ? "http://localhost:8080/api/users/public/search"
          : `http://localhost:8080/api/users/${userId}/followers/search`;

      const response = await axios.get(endpoint, {
        params: { query: searchQuery },
      });

      console.log("Response data:", response.data);
      if (Array.isArray(response.data)) {
        // Check if response data is an array
        const usersWithFollowStatus = await Promise.all(
            response.data.map(async (user) => {
              const isFollowing = filterType === "friends" ? await checkFollowingStatus(user.user_id) : false;
              return { ...user, isFollowing };
            })
        );
        setUsers(usersWithFollowStatus);
      } else {
        throw new Error("Unexpected response format");
      }
    } catch (error) {
      console.error("Error searching users:", error);
    }
  };

  const checkFollowingStatus = async (userIdToCheck) => {
    try {
      const response = await axios.get(
          "http://localhost:8080/api/followers/isFollowing",
          {
            params: {
              userId: userIdToCheck,
              followerId: userId,
            },
          }
      );
      return response.data;
    } catch (error) {
      console.error("Error checking following status:", error);
      return false;
    }
  };

  const handleFollow = async (userIdToFollow) => {
    try {
      await axios.post("http://localhost:8080/api/followers/follow", null, {
        params: {
          userId: userIdToFollow,
          followerId: userId,
        },
      });
      setUsers(
          users.map((user) =>
              user.user_id === userIdToFollow
                  ? { ...user, isFollowing: true }
                  : user
          )
      );
      fetchFeed();
    } catch (error) {
      console.error("Error following user:", error);
      setError("Error following user");
    }
  };

  const handleUnfollow = async (userIdToUnfollow) => {
    try {
      await axios.post("http://localhost:8080/api/followers/unfollow", null, {
        params: {
          userId: userIdToUnfollow,
          followerId: userId,
        },
      });
      setUsers(
          users.map((user) =>
              user.user_id === userIdToUnfollow
                  ? { ...user, isFollowing: false }
                  : user
          )
      );
      fetchFeed();
    } catch (error) {
      console.error("Error unfollowing user:", error);
      setError("Error unfollowing user");
    }
  };

  useEffect(() => {
    handleSearch();
  }, [filterType]); // Added filterType as dependency

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/";
    return null;
  } else {
    return (
        <div>
          <select onChange={(e) => setFilterType(e.target.value)} value={filterType}>
            <option value="public">Public</option>
            <option value="friends">Friends</option>
          </select>
          <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search users..."
          />
          <button onClick={handleSearch}>Search</button>
          {error && <p style={{ color: "red" }}>{error}</p>}
          <ul>
            {users.map((user) => (
                <li key={user.user_id}>
                  {user.username}
                  {user.isFollowing ? (
                      <button onClick={() => handleUnfollow(user.user_id)}>
                        Unfollow
                      </button>
                  ) : (
                      <button onClick={() => handleFollow(user.user_id)}>
                        Follow
                      </button>
                  )}
                </li>
            ))}
          </ul>
        </div>
    );
  }
};

export default SearchAndFollow;
