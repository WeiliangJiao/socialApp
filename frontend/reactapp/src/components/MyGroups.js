import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import GroupDetails from "./GroupDetails";
import SearchGroups from "./SearchGroups";
import CreateGroup from "./CreateGroup";

const MyGroups = () => {
  const [groups, setGroups] = useState([]);
  const [selectedGroup, setSelectedGroup] = useState(null);
  const userId = parseInt(sessionStorage.getItem("userId"), 10);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/memberships/user/${userId}`
        );
        const groupIds = response.data.map((membership) => membership.group.id);
        const groupResponses = await Promise.all(
          groupIds.map((id) => axios.get(`http://localhost:8080/groups/${id}`))
        );
        const groupData = groupResponses.map((res) => res.data);
        setGroups(groupData);
      } catch (error) {
        console.error("Error fetching groups", error);
        setGroups([]);
      }
    };

    fetchGroups();
  }, [userId]);

  const handleGroupClick = (group) => {
    setSelectedGroup(group);
  };

  const handleManageClick = (groupId) => {
    sessionStorage.setItem("canAccessManage", "true");
    navigate(`/manageGroup/${groupId}`);
  };

  const handleBack = () => {
    setSelectedGroup(null);
  };

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/";
  } else {
    return (
      <div>
        {selectedGroup ? (
          <GroupDetails group={selectedGroup} onBack={handleBack} />
        ) : (
          <div>
            <SearchGroups />
            <h2>My Groups</h2>
            {groups.length === 0 ? (
              <p>No groups available</p>
            ) : (
              <ul>
                {groups.map((group) => (
                  <li key={group.id}>
                    <span
                      onClick={() => handleGroupClick(group)}
                      style={{
                        cursor: "pointer",
                        color: "blue",
                        textDecoration: "underline",
                      }}
                    >
                      {group.name} - {group.major}
                    </span>
                    {group.admin.user_id === userId && (
                      <button onClick={() => handleManageClick(group.id)}>
                        Manage
                      </button>
                    )}
                  </li>
                ))}
              </ul>
            )}
            <CreateGroup />
          </div>
        )}
      </div>
    );
  }
};

export default MyGroups;
