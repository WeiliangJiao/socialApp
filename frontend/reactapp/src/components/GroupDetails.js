import React, { useState, useEffect } from "react";
import axios from "axios";

const GroupDetails = ({ group, onBack }) => {
  const [members, setMembers] = useState([]);

  useEffect(() => {
    const fetchGroupMembers = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/memberships/group/${group.id}`,
          {
            params: { isApproved: true },
          }
        );
        setMembers(response.data);
      } catch (error) {
        console.error("Error fetching group members", error);
      }
    };

    fetchGroupMembers();
  }, [group.id]);

  return (
    <div>
      <h2>Group Details</h2>
      <button onClick={onBack}>Back</button>
      <p>
        <strong>Name:</strong> {group.name}
      </p>
      <p>
        <strong>Major:</strong> {group.major}
      </p>
      <p>
        <strong>Admin:</strong> {group.admin.username}
      </p>
      <h3>Members</h3>
      {members.length === 0 ? (
        <p>No members available</p>
      ) : (
        <ul>
          {members.map((member) => (
            <li key={member.user.userId}>
              {member.user.username} - {member.user.name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default GroupDetails;
