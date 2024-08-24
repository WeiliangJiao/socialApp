// src/components/Logout.js
import React from 'react';
import { useNavigate } from 'react-router-dom';

const Logout = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    sessionStorage.removeItem('userId'); 
    navigate('/');
  };

  if (!sessionStorage.getItem("userId")) {
    window.location.href = "http://localhost:3000/"
  } else {
    return <button onClick={handleLogout}>Logout</button>;
  }
};

export default Logout;
