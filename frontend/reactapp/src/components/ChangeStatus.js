import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ChangeStatus = ({ userId }) => {
  const [status, setStatus] = useState('');
  const [error, setError] = useState('');
  const [currentStatus, setCurrentStatus] = useState('');

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/users/${userId}/status`)
      .then((response) => {
        setCurrentStatus(response.data);
        setStatus(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the status!", error);
      });
  }, [userId]);

  const handleStatusChange = (e) => {
    setStatus(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.put(`http://localhost:8080/api/users/${userId}/status`, { status })
      .then(() => {
        setCurrentStatus(status);
        alert('Status updated successfully');
      })
      .catch(() => {
        
        setError('Failed to update status');
      });
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div><label>Status: {currentStatus}</label></div>
        <select value={status} onChange={handleStatusChange}>
          <option value="Available">Available</option>
          <option value="Busy">Busy</option>
          <option value="Away">Away</option>
        </select>
        <button type="submit">Update Status</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
};

export default ChangeStatus;
