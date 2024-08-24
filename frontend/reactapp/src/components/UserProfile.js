import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ChangeStatus from "./ChangeStatus"

const UserProfile = () => {
  const [userId, setUserId] = useState(null);
  const [userProfile, setUserProfile] = useState({
    name: '',
    username: '',
    interests: ''
  });
  const [isEditing, setIsEditing] = useState(false);
  const [error, setError] = useState('');


  useEffect(() => {
    const fetchUserProfile = async () => {
      const id = sessionStorage.getItem('userId');
      if (id) {
        try {
          const response = await axios.get(`http://localhost:8080/api/users/${id}/profile`);
          setUserProfile({
            name: response.data.name,
            username: response.data.username,
            interests: response.data.interests
          });
        } catch (err) {
          setError('Failed to fetch user data');
        }
      }
      setUserId(id);
    };
    fetchUserProfile();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserProfile(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.put(`http://localhost:8080/api/users/${userId}/profile`, userProfile)
      .then(() => {
        setIsEditing(false);
        alert('Profile updated successfully');
      })
      .catch(() => setError('Failed to update profile'));
  };

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/";
    return null; 
  }

  return (
    <div>
      <h2>User Profile</h2>
      
      {isEditing ? (
        <form onSubmit={handleSubmit}>
          <div>
            <label>Name:</label>
            <input type="text" name="name" value={userProfile.name} onChange={handleInputChange} />
          </div>
          <div>
            <label>Username:</label>
            <input type="text" name="username" value={userProfile.username} onChange={handleInputChange} />
          </div>
          <div>
            <label>Interests:</label>
            <textarea name="interests" value={userProfile.interests} onChange={handleInputChange} />
          </div>
          <button type="submit">Save Changes</button>
          <button type="button" onClick={() => setIsEditing(false)}>Cancel</button>
        </form>
      ) : (
        <div>
          <p>Name: {userProfile.name}</p>
          <p>Username: {userProfile.username}</p>
          <p>Interests: {userProfile.interests}</p>
          <button onClick={() => setIsEditing(true)}>Edit Profile</button>
          {/* Pass userId to the ChangeStatus component */}
          <ChangeStatus userId={userId} />
        </div>
      )}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <a href="http://localhost:3000/home" style={{ display: 'block', marginTop: '20px' }}>Return to Homepage</a>
    </div>
  );
};

export default UserProfile;
