import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const SearchGroups = () => {
  const [query, setQuery] = useState("");
  const [selectedMajor, setSelectedMajor] = useState("All Majors");
  const [groups, setGroups] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const userId = sessionStorage.getItem("userId");

  const handleSearch = async () => {
    try {
      let response;
      let params = {};
  
      // Set params based on filter selection
      if (selectedMajor && selectedMajor !== "All Majors") {
        params.major = selectedMajor;
      }
  
      if (query) {
        params.keyword = query.trim(); // Ensure query is trimmed
      }
  
      // If no params are set, fetch all groups
      if (Object.keys(params).length === 0) {
        params = {}; // No params will trigger the backend to return all groups
      }
  
      response = await axios.get("http://localhost:8080/groups/search", {
        params: params,
      });

      console.log("Response data:", response.data); // Debugging output

      if (response.data.length === 0) {
        setGroups([]); // Explicitly handle empty results
        setError("No groups found"); // Show error message if no results
      } else {
        setGroups(response.data);
        setError(null); // Clear error if results are found
      }
    } catch (error) {
      console.error("Error searching groups:", error);
      setError("Error searching groups");
    }
  };

  const handleGroupClick = (groupId) => {
    navigate(`/manageGroup/${groupId}`);
  };

  const handleRequestToJoin = async (groupId) => {
    try {
      await axios.post(
        "http://localhost:8080/memberships/requestToJoin",
        null,
        {
          params: { groupId: groupId, userId: userId },
        }
      );
      alert("Request to join sent!");
    } catch (error) {
      console.error("Error requesting to join group:", error);
      alert("Error requesting to join group");
    }
  };

  if (!userId) {
    window.location.href = "http://localhost:3000/";
    return null;
  }

  return (
    <div>
      <h2>Search Groups</h2>
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Search by Group Name or Keyword"
      />
      <select
        value={selectedMajor}
        onChange={(e) => setSelectedMajor(e.target.value)}
      >
        <option value="All Majors">All Majors</option>
        <option value="Computer Science">Computer Science</option>
        <option value="Literature">Literature</option>
        <option value="Engineering">Engineering</option>
      </select>
      <button onClick={handleSearch}>Search</button>

      {error && <p style={{ color: "red" }}>{error}</p>}
      {groups.length === 0 ? (
        <br/>
      ) : (
        <ul>
          {groups.map((group) => (
            <li key={group.id}>
              <span onClick={() => handleGroupClick(group.id)}>
                {group.name} - {group.major}
              </span>
              <button onClick={() => handleRequestToJoin(group.id)}>
                Request to Join
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default SearchGroups;
