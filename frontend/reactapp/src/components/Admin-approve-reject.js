import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Admin = () => {
  const [inactiveUsers, setInactiveUsers] = useState([]);
  const [error, setError] = useState("");
  const [notification, setNotification] = useState("");
  const [isAdmin, setIsAdmin] = useState(false);
  const navigate = useNavigate();
  const userId = sessionStorage.getItem("userId");

  useEffect(() => {
    const checkAdmin = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/users/${userId}/isAdmin`);
        if (response.data) {
          setIsAdmin(true);
          fetchInactiveUsers();
        } else {
          setError("You are not authorized to access this page.");
          setTimeout(() => {
            navigate("/home");
          }, 3000);
        }
      } catch (error) {
        setError("Error checking admin status");
        console.error("Error checking admin status:", error);
      }
    };

    checkAdmin();
  }, [userId, navigate]);

  const fetchInactiveUsers = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/users/inactive");
      setInactiveUsers(response.data);
    } catch (error) {
      setError("Error fetching inactive users");
      console.error("Error fetching inactive users:", error);
    }
  };

  const approveUser = async (userId) => {
    if (window.confirm(`Are you sure you want to approve the user with User ID of ${userId}?`)) {
      try {
        await axios.put(`http://localhost:8080/api/users/${userId}/active-status`, { is_active: true });
        setInactiveUsers(inactiveUsers.filter(user => user.user_id !== userId));
        setNotification("User approved successfully");
      } catch (error) {
        setError("Error approving user");
        console.error("Error approving user:", error);
      }
    }
  };

  const rejectUser = async (userId) => {
    if (window.confirm(`Are you sure you want to reject the user with the user ID of ${userId}? This action cannot be undone.`)) {
      try {
        await axios.delete(`http://localhost:8080/api/users/${userId}`);
        setInactiveUsers(inactiveUsers.filter(user => user.user_id !== userId));
        setNotification("User rejected successfully");
      } catch (error) {
        setError("Error rejecting user");
        console.error("Error rejecting user:", error);
      }
    }
  };

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/";
    return null;
  } else {
    return (
      <div>
        <h2>Admin - Approve Or Reject Potential Users</h2>
        {error && <p style={{ color: "red" }}>{error}</p>}
        {notification && <p style={{ color: "green" }}>{notification}</p>}
        {isAdmin && (
          <table>
            <thead>
              <tr>
                <th>User ID</th>
                <th>Username</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {inactiveUsers.map(user => (
                <tr key={user.user_id}>
                  <td>{user.user_id}</td>
                  <td>{user.username}</td>
                  <td>
                    <button onClick={() => approveUser(user.user_id)}>Approve</button>
                    <button onClick={() => rejectUser(user.user_id)}>Reject</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    );
  }
};

export default Admin;
