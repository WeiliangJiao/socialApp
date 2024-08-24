import React, { useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import '../App.css'

const Signup = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");
  const [securityQuestion, setSecurityQuestion] = useState("");
  const [securityAnswer, setSecurityAnswer] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8080/api/users/signup",
        null,
        {
          params: {
            username,
            email,
            password,
            securityQuestion,
            securityAnswer,
          },
        }
      );
      alert(response.data);
      navigate("/");
    } catch (error) {
      setError("Error during registration: " + error.message);
    }
  };

  return (
    <div>
      <h2>Sign-up</h2>
      <form onSubmit={handleSignup}>
        <div>
          <label>Email:</label>
          <br/>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <br/>
          <label className = "description">Email must be in the form: user@dal.ca</label>
        </div>
        <div>
          <label>Password:</label>
          <br/>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <br/>
          <label className = "description">
            Password must be at least 8 characters with at least one lowercase,<br/>
            uppercase, number, and special character
          </label>
        </div>
        <div>
          <label>Username:</label>
          <br/>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Security Question:</label>
          <br/>
          <input
            type="text"
            value={securityQuestion}
            onChange={(e) => setSecurityQuestion(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Security Answer:</label>
          <br/>
          <input
            type="text"
            value={securityAnswer}
            onChange={(e) => setSecurityAnswer(e.target.value)}
            required
          />
        </div>
        <button type="submit">Sign-up</button>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}

      <div>
        <Link to="/">Already have an account?</Link>
      </div>
    </div>
  );
};

export default Signup;
