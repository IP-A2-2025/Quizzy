import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'; // adăugat Navigate
import Login from './Login/Login.jsx';
import Dashboard from './Dashboard/Dashboard.jsx';
import Profile from './Profile/Profile.jsx';
import Library from './Library/Library.jsx';
import GraphAlgorithmsPage from "./GraphAlgotithmsPage/GraphAlgorithmsPage.jsx";
import Register from './Register/Register.jsx';
import Explore from './Explore/Explore.jsx';



ReactDOM.createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} /> {/* redirect root */}
            <Route path="/login" element={<Login />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/library" element={<Library />} />
            <Route path="/graph-algorithms" element={<GraphAlgorithmsPage />} />
            <Route path="/register" element={<Register />} />
            <Route path="/explore" element={<Explore />} />



        </Routes>
    </BrowserRouter>
);
