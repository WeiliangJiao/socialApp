import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const ChangeRole = () => {
    const [userId, setUserId] = useState("");
    const [isAdmin, setIsAdmin] = useState(true);
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const currentUserId = sessionStorage.getItem("userId");


    useEffect(() => {
      const checkAdmin = async () => {
        try {
          const response = await axios.get(`http://localhost:8080/api/users/${currentUserId}/isAdmin`);
          if (response.data) {
            setIsAdmin(true);
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

    const submitAdmin = async () => {
      try {
        const response = await axios.put(
          "http://localhost:8080/api/users/${userId}/isAdmin",
          {
            isAdmin,
          }
        );
        if (response.status === 200) {
          alert("User status changed successfully");
        }
      } catch (error) {
        setError("Error during status change: " + error.message);
      }
    };

    if (!sessionStorage.getItem("userId")) {
      window.location.href = "http://localhost:3000/";
      return null;
    } else {
      return (
          <div>
              <h2>Admin - Change Role</h2>
              {error ? <p style={{ color: "red" }}>{error}</p> :  
              <>
                <label>User ID: </label>
                    <input
                        type="userId"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                    Enter the ID of the user that will be given administrative privileges.
                    <br/> <button onClick={submitAdmin}>Submit</button>
              </>}
          </div>
      );
    }
};

export default ChangeRole;