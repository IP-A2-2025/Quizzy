import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'; // adăugat Navigate
import Login from './Login.jsx';


ReactDOM.createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} /> {/* redirect root */}
            <Route path="/login" element={<Login />} />

        </Routes>
    </BrowserRouter>
);
