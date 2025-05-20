import React, {useEffect, useState} from 'react';
import './Explore.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { api } from '../utils/api';

const Explore = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const currentPath = location.pathname;

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
    /*
    const courses = [
        { title: 'Graph Algorithms', flashcards: 24, files: 6, path: '/graph-algorithms' },
        { title: 'Software Engineering', flashcards: 37, files: 12 },
        { title: 'English', flashcards: 7, files: 2 },
        { title: 'Databases', flashcards: 95, files: 16 },
        { title: 'Databases', flashcards: 95, files: 16 },
        { title: 'Databases', flashcards: 95, files: 16 },
        { title: 'Databases', flashcards: 95, files: 16 },
    ];
    */
    const [courses, setCourses] = useState([]);

useEffect(() => {
    api.get('/courses')
        .then((res) => {
            console.log('Răspuns backend:', res.data);
            setCourses(res.data);
        })
        .catch((err) => {
            console.error('Failed to fetch courses:', err);
        });
}, []);



    const getActiveClass = (route) => {
        return currentPath === route ? 'active' : '';
    };

    return (
        <div className="explore-container">
            <div className="explore-logo">
                <img src="/quizzy-logo-homepage.svg" alt="Logo" style={{ width: '100%', height: '100%' }} />
            </div>

            <div className="explore-inner-box" />

            {/* Sidebar Buttons */}
            <button
                className={`explore-icon-wrapper explore-icon-home ${getActiveClass('/dashboard')}`}
                onClick={() => handleClick("Home")}
            >
                {getActiveClass('/dashboard') && <div className="explore-rectangle-home"></div>}
                <img src="/home-logo.svg" alt="Home" className="explore-icon-image" />
                <div className="explore-icon-text">Home</div>
            </button>

            <button
                className={`explore-icon-wrapper explore-icon-library ${getActiveClass('/library')}`}
                onClick={() => handleClick("Library")}
            >
                {getActiveClass('/library') && <div className="explore-rectangle-home"></div>}
                <img src="/library-logo.svg" alt="Library" className="explore-icon-image" />
                <span className="explore-icon-text">Library</span>
            </button>

            <button
                className={`explore-icon-wrapper explore-icon-explore ${getActiveClass('/explore')}`}
                onClick={() => handleClick("Explore")}
            >
                {getActiveClass('/explore') && <div className="explore-rectangle-home"></div>}
                <img src="/explore-logo.svg" alt="Explore" className="explore-icon-image" />
                <span className="explore-icon-text">Explore</span>
            </button>

            <button
                className={`explore-icon-wrapper explore-icon-profile ${getActiveClass('/profile')}`}
                onClick={() => handleClick("Profile")}
            >
                {getActiveClass('/profile') && <div className="explore-rectangle-home"></div>}
                <img src="/profile-logo.svg" alt="Profile" className="explore-icon-image" />
                <span className="explore-icon-text">Profile</span>
            </button>

            <div className="explore-logo-fii">
                <img src="/logo-fac-homepage.svg" alt="FII" style={{ width: '100%', height: '100%' }} />
            </div>

            {/* White Box */}
            <div className="explore-white-box">
                {/* Dashboard-ul este gol momentan */}
            </div>

            {/* Cards Section */}
            <div className="library-cards-container">
    {courses.map((course, index) => (
        <div
            key={index}
            className="library-card"
            // Dacă vrei navigare, poți adăuga path mai târziu
            style={{ cursor: 'default' }}
        >
            <div className="library-card-header" />
            <div className="library-card-header-text">
                <div className="library-course-title">{course.title}</div>
                <div className="library-course-info">
                </div>
            </div>
        </div>
    ))}
</div>

        </div>

    );
};

export default Explore;