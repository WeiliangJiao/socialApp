import React, { useState } from "react";
import axios from "axios";

const CreateGroup = () => {
  const [name, setName] = useState("");
  const [major, setMajor] = useState("");
  const [isPrivate, setIsPrivate] = useState(false);
  const userId = parseInt(sessionStorage.getItem("userId"), 10);

  const handleCreateGroup = async () => {
    if (!name.trim() || !major.trim()) {
      alert("Group name and major are required");
      return;
    }

    try {
      const response = await axios.post(`http://localhost:8080/groups`, null, {
        params: {
          name,
          major,
          isPrivate,
          adminId: userId,
        },
      });
      setName("");
      setMajor("");
      setIsPrivate(false);
      alert("Group created successfully");
    } catch (error) {
      console.error("Error creating group", error);
      alert("Failed to create group. Please try again.");
    }
  };

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/";
  } else {
    return (
      <div>
        <h2>Create Group</h2>
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Group Name"
        />
        <input
          type="text"
          value={major}
          onChange={(e) => setMajor(e.target.value)}
          placeholder="Group Major"
        />
        <label>
          Private:
          <input
            type="checkbox"
            checked={isPrivate}
            onChange={(e) => setIsPrivate(e.target.checked)}
          />
        </label>
        <button onClick={handleCreateGroup}>Create</button>
      </div>
    );
  }
};

export default CreateGroup;
