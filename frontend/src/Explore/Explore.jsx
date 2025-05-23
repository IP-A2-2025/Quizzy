import React, { useState, useEffect } from 'react';
import './Explore.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { api } from '../utils/api';

const Explore = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const currentPath = location.pathname;

    useEffect(() => {
        const fetchCourses = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await api.get('/courses');
                setCourses(response.data);
            } catch (err) {
                setError(err.response?.data?.message || err.message);
            } finally {
                setLoading(false);
            }
        };
        fetchCourses();
    }, []);

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
                {loading ? (
                    <div className="library-loading">Loading courses...</div>
                ) : error ? (
                    <div className="library-error">Error: {error}</div>
                ) : courses.length === 0 ? (
                    <div className="library-empty">No courses available</div>
                ) : (
                    courses.map((course, index) => (
                        <div
                            key={course.id || index}
                            className="library-card"
                            style={{ cursor: 'pointer' }}
                            onClick={() => navigate(`/course/${course.id}`, { state: { course } })}
                        >
                            <div className="library-card-header" />
                            <div className="library-card-header-text">
                                <div className="library-course-title">{course.title}</div>
                                <div className="library-course-info">
                                    <span className="library-number">{course.flashcardCount || 0}</span> Flashcards |
                                    <span className="library-number">{course.materials?.length || 0}</span> Files
                                </div>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default Explore;