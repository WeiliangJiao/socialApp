import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const AdminPage = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState('');
    const [notification, setNotification] = useState('');
    const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();
    const userId = sessionStorage.getItem("userId");

    useEffect(() => {
        const checkAdmin = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/users/${userId}/isAdmin`);
                if (response.data) {
                    setIsAdmin(true);
                    fetchUsers();
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

    const fetchUsers = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/users');
            setUsers(response.data);
        } catch (err) {
            setError('Failed to fetch users');
        }
    };

    const deleteUser = async (userId) => {
        if (window.confirm(`Are you sure you want to delete the user with User ID of ${userId}?`)) {
            try {
                await axios.delete(`http://localhost:8080/api/users/${userId}`);
                setUsers(users.filter(user => user.user_id !== userId));
                setNotification("User deleted successfully");
            } catch (err) {
                setError('Failed to delete user');
                console.error('Failed to delete user:', err);
            }
        }
    };

    if (!sessionStorage.getItem("userId")) {
        window.location.href = "http://localhost:3000/";
        return null;
    } else {
        return (
            <div>
                <h2>Admin - Remove Users</h2>
                {error && <p style={{ color: "red" }}>{error}</p>}
                {notification && <p style={{ color: "green" }}>{notification}</p>}
                {isAdmin && (
                    <ul>
                        {users.map(user => (
                            <li key={user.user_id}>
                                {user.username} <button onClick={() => deleteUser(user.user_id)}>Remove</button>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        );
    }
};

export default AdminPage;
