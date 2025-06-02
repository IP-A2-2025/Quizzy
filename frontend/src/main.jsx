import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'; // adăugat Navigate
import Login from './Login/Login.jsx';
import Dashboard from './Dashboard/Dashboard.jsx';
import Profile from './Profile/Profile.jsx';
import Library from './Library/Library.jsx';
import Register from './Register/Register.jsx';
import Explore from './Explore/Explore.jsx';
import Flashcards from "./Flashcards/Flashcards.jsx";
import PDFViewer from './PDFViewer/PDFViewer.jsx';
import CoursePage from './CoursePage/CoursePage.jsx';



ReactDOM.createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} /> {/* redirect root */}
            <Route path="/login" element={<Login />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/library" element={<Library />} />
            <Route path="/register" element={<Register />} />
            <Route path="/explore" element={<Explore />} />
            <Route path="/flashcards" element={<Flashcards />} />
            <Route path="/course/:id" element={<CoursePage />} />
            <Route path="/Material/path/*" element={<PDFViewer />} />
            <Route path="/flashcards/:materialId" element={<Flashcards />} />

        </Routes>
    </BrowserRouter>
);

