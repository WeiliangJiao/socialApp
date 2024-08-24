import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const GroupManagement = () => {
  const { groupId } = useParams();
  const navigate = useNavigate();
  const [requests, setRequests] = useState([]);

  useEffect(() => {
    const canAccessManage = sessionStorage.getItem("canAccessManage");

    if (canAccessManage !== "true") {
      navigate("/home");
      return;
    }

    const fetchRequests = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/memberships/group/${groupId}`,
          {
            params: { isApproved: false },
          }
        );
        setRequests(response.data);
        sessionStorage.removeItem("canAccessManage"); // Remove access flag after fetching data
      } catch (error) {
        console.error("Error fetching requests", error);
      }
    };

    fetchRequests();
  }, [groupId, navigate]);

  const handleApprove = async (membershipId) => {
    try {
      await axios.put(`http://localhost:8080/memberships/approve`, {
        id: membershipId,
      });
      setRequests(requests.filter((request) => request.id !== membershipId));
    } catch (error) {
      console.error("Error approving request", error);
    }
  };

  const handleDeny = async (membershipId) => {
    try {
      await axios.delete(`http://localhost:8080/memberships/${membershipId}`);
      setRequests(requests.filter((request) => request.id !== membershipId));
    } catch (error) {
      console.error("Error denying request", error);
    }
  };

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/";
  } else {
    return (
      <div>
        <h2>Group Management</h2>
        {requests.length === 0 ? (
          <p>No requests available</p>
        ) : (
          <ul>
            {requests.map((request) => (
              <li key={request.id}>
                {request.user.username} - {request.user.name}
                <button onClick={() => handleApprove(request.id)}>
                  Approve
                </button>
                <button onClick={() => handleDeny(request.id)}>Deny</button>
              </li>
            ))}
          </ul>
        )}
      </div>
    );
  }
};

export default GroupManagement;
