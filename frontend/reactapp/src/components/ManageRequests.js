import React, { useState, useEffect } from "react";
import axios from "axios";

const ManageRequests = ({ groupId }) => {
  const [requests, setRequests] = useState([]);

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/memberships/group/${groupId}?isApproved=false`
        );
        setRequests(response.data);
      } catch (error) {
        console.error("Error fetching requests", error);
      }
    };

    fetchRequests();
  }, [groupId]);

  const handleApprove = async (requestId) => {
    try {
      await axios.put(`http://localhost:8080/memberships/approve`, {
        id: requestId,
      });
      setRequests(requests.filter((request) => request.id !== requestId));
    } catch (error) {
      console.error("Error approving request", error);
    }
  };

  const handleDeny = async (requestId) => {
    try {
      await axios.delete(`http://localhost:8080/memberships/${requestId}`);
      setRequests(requests.filter((request) => request.id !== requestId));
    } catch (error) {
      console.error("Error denying request", error);
    }
  };

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/";
  } else {
    return (
      <div>
        <h2>Manage Requests</h2>
        {requests.length === 0 ? (
          <p>No requests to manage</p>
        ) : (
          <ul>
            {requests.map((request) => (
              <li key={request.id}>
                {request.user.username}
                <button onClick={() => handleApprove(request.id)}>Approve</button>
                <button onClick={() => handleDeny(request.id)}>Deny</button>
              </li>
            ))}
          </ul>
        )}
      </div>
    );
  }
};

export default ManageRequests;
