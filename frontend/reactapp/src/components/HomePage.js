import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import SearchAndFollow from "./SearchAndFollow";
import Logout from "./Logout";
import MyGroups from "./MyGroups";

const HomePage = () => {
  const [posts, setPosts] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const [newPostContent, setNewPostContent] = useState("");
  const userId = sessionStorage.getItem("userId");
  const navigate = useNavigate();

  const fetchFeed = useCallback(async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/posts/feed/${userId}`
      );
      setPosts(response.data);
    } catch (error) {
      console.error("Error fetching feed", error);
    }
  }, [userId]);

  const fetchRecommendations = useCallback(async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/users/recommendations/${userId}`
      );
      if (Array.isArray(response.data)) {
        setRecommendations(response.data);
      } else {
        console.error(
          "Recommendations API did not return an array",
          response.data
        );
        setRecommendations([]);
      }
    } catch (error) {
      console.error("Error fetching recommendations", error);
      setRecommendations([]);
    }
  }, [userId]);

  useEffect(() => {
    fetchFeed();
    fetchRecommendations();
  }, [fetchFeed, fetchRecommendations]);

  const handleCreatePost = async () => {
    if (!newPostContent.trim()) return;

    try {
      await axios.post(`http://localhost:8080/posts/addpost/${userId}`, {
        user: { id: userId },
        content: newPostContent,
      });
      setNewPostContent("");
      await fetchFeed(); // Ensure fetchFeed completes before refreshing
    } catch (error) {
      console.error("Error creating post", error);
    }
  };

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/";
  } else {
    return (
      <div>
        <nav className="nav-bar">
          <button onClick={() => navigate("/profile")}>My Profile</button>
          <Logout />
        </nav>
        <SearchAndFollow fetchFeed={fetchFeed} />
        <h1>Home Page</h1>
        <div>
          <h2>Create Post</h2>
          <textarea
            value={newPostContent}
            onChange={(e) => setNewPostContent(e.target.value)}
            placeholder="What's on your mind?"
          ></textarea>
          <button onClick={handleCreatePost}>Post</button>
        </div>
        <div>
          <h2>Feed</h2>
          {posts.length === 0 ? (
            <p>No posts available</p>
          ) : (
            <ul>
              {posts.map((post) => (
                <li key={post.id}>
                  <strong>{post.user.username}</strong>: {post.content}
                  <br />
                  <small>{new Date(post.timePosted).toLocaleString()}</small>
                </li>
              ))}
            </ul>
          )}
        </div>
        <MyGroups />
        <div>
          <h2>Recommendations</h2>
          {recommendations.length === 0 ? (
            <p>No recommendations available</p>
          ) : (
            <ul>
              {recommendations.map((user) => (
                <li key={user.id}>{user.username}</li>
              ))}
            </ul>
          )}
        </div>
      </div>
    );
  }
};

export default HomePage;
