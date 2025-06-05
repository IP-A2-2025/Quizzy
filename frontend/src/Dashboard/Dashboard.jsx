import React, { useEffect, useState } from 'react';
import './Dashboard.css';
import { useNavigate } from 'react-router-dom';
import { api } from '../utils/api';
import BurgerMenu from '../components/BurgerMenu/BurgerMenu';
import { SidebarLeft, SidebarRight } from '../components/Sidebar';

const Dashboard = () => {
    const navigate = useNavigate();
    const [streak, setStreak] = useState(null);

    const username = localStorage.getItem('user') || '';
    // Extract first name from email address or username
    const extractFirstName = (userStr) => {
        if (!userStr) return '';
        
        // If it's an email, get the part before @
        const emailPart = userStr.includes('@') ? userStr.split('@')[0] : userStr;
        
        // Get the first part before any dot or underscore
        const firstName = emailPart.split(/[._]/)[0];
        
        // Capitalize first letter and make rest lowercase
        return firstName.charAt(0).toUpperCase() + firstName.slice(1).toLowerCase();
    };
    
    const prenume = extractFirstName(username);
    const userId = localStorage.getItem('userId');

    useEffect(() => {
        const fetchStreak = async () => {
            try {
                if (userId) {
                    const response = await api.get(`/streaks/${userId}`);
                    setStreak(response.data.currentStreak || 0);
                }
            } catch (error) {
                console.error('Error fetching streak:', error);
                setStreak(0);
            }
        };

        fetchStreak();
    }, [userId]);

    const currentDate = new Date();
    const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    const daysOfWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

    const day = currentDate.getDate();
    const month = months[currentDate.getMonth()];
    const dayName = daysOfWeek[currentDate.getDay()];

    const handleClick = (label) => {
        navigate(`/${label.toLowerCase()}`);
    };    return (
        <div className="dashboard-wrapper">
            {/* Burger Menu for tablet and mobile */}
            <BurgerMenu currentPage="dashboard" />
            
            {/* Desktop Sidebars */}
            <SidebarLeft activeRoute="/dashboard" />
            <SidebarRight />
            
            <header className="dashboard-header">
                {/* Header can be used for other elements if needed */}
            </header>

            <main className="dashboard-main">
                <section className="dashboard-content">
                    <div className="welcome">
                        <img src="/bufnita.svg" alt="Bufnita" className="owl" />
                        <h1>Hello, {prenume}</h1>
                    </div>

                    <div className="top-bar">
                        <div className="date-info">
                            <span>🗓️ {dayName}, {day} {month}</span>
                            <span className="streak">🔥 {streak}</span>
                        </div>
                    </div>

                    <hr />

                    <div className="week-days">
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
                                    <div className="day-body">
                                        <h3>{weekDay}</h3>
                                        <p>{weekDayDate} {weekDayMonth}</p>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </section>
            </main>
        </div>
    );
};

export default Dashboard;
