// src/App.js
import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import ForgetPassword from "./components/ForgetPassword";
import SearchAndFollow from "./components/SearchAndFollow";
import UserProfile from "./components/UserProfile";
import Logout from "./components/Logout";
import Signup from "./components/Signup";
import HomePage from "./components/HomePage";
import AdminApproveReject from "./components/Admin-approve-reject";
import MyGroups from "./components/MyGroups";
import SearchGroups from "./components/SearchGroups";
import GroupManagement from "./components/GroupManagement";
import CreateGroup from "./components/CreateGroup";
import ChangeRole from "./components/Change-role";
import AdminPage from "./components/AdminPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/logout" element={<Logout />} />
        <Route path="/search-follow" element={<SearchAndFollow />} />
        <Route path="/profile" element={<UserProfile />} />
        <Route path="/forgot-password" element={<ForgetPassword />} />
        <Route path="/sign-up" element={<Signup />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/admin-approve-reject" element={<AdminApproveReject />} />
        <Route path="/myGroups" element={<MyGroups />} />
        <Route path="/searchGroups" element={<SearchGroups />} />
        <Route path="/manageGroup/:groupId" element={<GroupManagement />} />
        <Route path="/createGroup" element={<CreateGroup />} />
        <Route path="/change-role" element={<ChangeRole />} />
        <Route path="/admin" element={<AdminPage/>} />
      </Routes>
    </Router>
  );
}

export default App;
