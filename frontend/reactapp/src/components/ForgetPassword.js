import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const ForgetPassword = () => {
  const [email, setEmail] = useState("");
  const [securityQuestion, setSecurityQuestion] = useState("");
  const [securityAnswer, setSecurityAnswer] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [showSecurityQuestion, setShowSecurityQuestion] = useState(false);
  const [showResetPassword, setShowResetPassword] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const fetchSecurityQuestion = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8080/api/users/get-security-question",
        {
          params: { email },
        }
      );
      setSecurityQuestion(response.data);
      setShowSecurityQuestion(true);
      setError("");
    } catch (error) {
      setError("Email not found");
    }
  };

  const checkSecurityAnswer = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/users/check-security-answer",
        {
          email,
          securityAnswer,
        }
      );
      if (response.status === 200) {
        setShowResetPassword(true);
        setError("");
      }
    } catch (error) {
      setError("Incorrect security answer");
    }
  };

  const resetPassword = async () => {
    try {
      const response = await axios.put(
        "http://localhost:8080/api/users/reset-password",
        {
          email,
          password: newPassword,
        }
      );
      if (response.status === 200) {
        alert("Password reset successfully");
        navigate("/");
      }
    } catch (error) {
      setError("Failed to reset password");
    }
  };

  return (
    <div>
      <h2>Forgot Password</h2>
      {!showSecurityQuestion && !showResetPassword && (
        <div>
          <label>Email: </label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <button onClick={fetchSecurityQuestion}>Get Security Question</button>
        </div>
      )}
      {showSecurityQuestion && !showResetPassword && (
        <div>
          <p>{securityQuestion}</p>
          <label>Security Answer:</label>
          <input
            type="text"
            value={securityAnswer}
            onChange={(e) => setSecurityAnswer(e.target.value)}
            required
          />
          <button onClick={checkSecurityAnswer}>Submit</button>
        </div>
      )}
      {showResetPassword && (
        <div>
          <label className = "description">
            Password must be at least 8 characters with at least one lowercase,
            uppercase, number, and special character
          </label>
          <br/>
          <label>New Password:</label>
          <input
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
          <button onClick={resetPassword}>Reset Password</button>
        </div>
      )}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
};

export default ForgetPassword;
