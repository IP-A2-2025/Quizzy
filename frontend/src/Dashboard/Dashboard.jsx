import React, { useEffect, useState } from 'react';
import './Dashboard.css';
import { useNavigate } from 'react-router-dom';
import { api } from '../utils/api';

const Dashboard = () => {
    const navigate = useNavigate();
    const [streak, setStreak] = useState(null);

    // Preluăm username-ul și extragem prenumele
    const username = localStorage.getItem('user') || '';
    const prenume = username.split('.')[0]?.charAt(0).toUpperCase() + username.split('.')[0]?.slice(1).toLowerCase();

    // Preluăm userId-ul salvat în localStorage
    const userId = localStorage.getItem('userId');

    // Obținem data curentă
    const currentDate = new Date();
    const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const months = [
        'January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'
    ];
    const dayName = daysOfWeek[currentDate.getDay()];
    const day = currentDate.getDate();
    const month = months[currentDate.getMonth()];

    useEffect(() => {
        if (userId) {
            api.get(`/users/streak/latest?userId=${userId}`)
                .then((res) => {
                    if (res.data && typeof res.data.currentStreak === 'number') {
                        setStreak(res.data.currentStreak);
                    } else {
                        setStreak(1);
                    }
                })
                .catch((err) => {
                    console.error('Eroare la preluarea streak-ului:', err);
                    setStreak(1);
                });
        } else {
            setStreak(1);
        }
    }, [userId]);

    const handleClick = (label) => {
        if (label === "Home") {
            navigate('/dashboard');
        } else if (label === "Profile") {
            navigate('/profile');
        } else if (label === "Explore") {
            navigate('/explore');
        } else if (label === "Library") {
            navigate('/library');
        }
    };

    return (
        <div className="dashboard-container">
            <div className="dashboard-logo">
                <img src="/quizzy-logo-homepage.svg" alt="Logo" style={{ width: '100%', height: '100%' }} />
            </div>

            <div className="dashboard-inner-box" />

            {/* Sidebar Buttons */}
            <div className="dashboard-icon-wrapper dashboard-icon-home active">
                <div className="dashboard-rectangle-home"></div>
                <img src="/home-logo.svg" alt="Home" className="dashboard-icon-image" />
                <div className="dashboard-icon-text">Home</div>
            </div>

            <button className="dashboard-icon-wrapper dashboard-icon-library" onClick={() => handleClick("Library")}>
                <img src="/library-logo.svg" alt="Library" className="dashboard-icon-image" />
                <span className="dashboard-icon-text">Library</span>
            </button>

            <button className="dashboard-icon-wrapper dashboard-icon-explore" onClick={() => handleClick("Explore")}>
                <img src="/explore-logo.svg" alt="Explore" className="dashboard-icon-image" />
                <span className="dashboard-icon-text">Explore</span>
            </button>

            <button className="dashboard-icon-wrapper dashboard-icon-profile" onClick={() => handleClick("Profile")}>
                <img src="/profile-logo.svg" alt="Profile" className="dashboard-icon-image" />
                <span className="dashboard-icon-text">Profile</span>
            </button>

            <div className="dashboard-logo-fii">
                <img src="/logo-fac-homepage.svg" alt="FII" style={{ width: '100%', height: '100%' }} />
            </div>

            {/* White Box */}
            <div className="dashboard-white-box">
                <div className="welcome-content">
                    <h1 className="welcome-text">Hello, {prenume}</h1>
                    <img src="/bufnita.svg" alt="Bufnita" className="welcome-owl-image" />
                </div>

                <div className="day-and-buttons">
                    <div className="date-indicator">
                        <span>🗓️ {dayName}, {day} {month}</span>
                        <span className="indicator">🔥 {streak}</span>
                    </div>

                    <div className="navigation-buttons">
                        <button className="previous-day-button">&lt; Previous day</button>
                        <button className="next-day-button">Next day &gt;</button>
                    </div>
                </div>

                <div className="graph-divider"></div>

                <div className="week-days-container">
                    {Array.from({ length: 5 }).map((_, index) => {
                        const newDate = new Date();
                        newDate.setDate(currentDate.getDate() + index);
                        const weekDay = daysOfWeek[newDate.getDay()];
                        const weekDayDate = newDate.getDate();
                        const weekDayMonth = months[newDate.getMonth()];
                        const isToday = newDate.toDateString() === currentDate.toDateString();

                        return (
                            <div className={`day-card ${isToday ? 'active' : ''}`} key={index}>
                                <div className="day-top"></div>
                                <div className="day-content">
                                    <h3 className="day-name">{weekDay}</h3>
                                    <h5 className="day-date">{weekDayDate} {weekDayMonth}</h5>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
