import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../App.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // check database to see if email and password match
      const response = await axios.post("http://localhost:8080/api/login", {
        email,
        password,
      });

      // save the returned userID to check for authorization and starting a session. 
      const userId = response.data;

      // check for authorization
      const activeResponse = await axios.get(`http://localhost:8080/api/users/${userId}/is_active`);
      const isActive = activeResponse.data;

      if (isActive) {
        // if authorized, start a session with the userID
        sessionStorage.setItem('userId', userId);
        navigate('/home');
      } else {
        setError("Waiting for admin's approval");
      }
    } catch (error) {
      if (error.response && error.response.status === 403) {
        setError("Waiting for admin's approval");
      } else {
        setError("Invalid email or password");
      }
    }
  };

  return (
    <div className="test">
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label>Email:</label>
          <br/>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password: </label>
          <br/>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}

      <div>
        <Link to="/forgot-password">Forgot Password?</Link>
      </div>
      <div>
        <Link to="/sign-up">Sign Up!</Link>
      </div>
    </div>
  );
};

export default Login;
