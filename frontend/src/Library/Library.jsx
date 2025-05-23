import React, { useState, useEffect } from 'react';
import './Library.css';
import { useNavigate } from 'react-router-dom';
import { api } from '../utils/api';

const Library = () => {
    const navigate = useNavigate();
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Funcție pentru a obține userId pe baza email-ului
    const fetchUserIdByEmail = async (email) => {
        try {
            const response = await api.get(`/users/profile?email=${encodeURIComponent(email)}`);
            const user = response.data;
            if (!user.id) {
                throw new Error('User ID not found in response');
            }
            localStorage.setItem('userId', user.id);
            return user.id;
        } catch (err) {
            setError(err.response?.data?.message || err.message);
            setLoading(false);
            return null;
        }
    };

    // Funcție pentru a obține cursurile la care utilizatorul este înscris
    const fetchEnrolledCourses = async (userId) => {
        try {
            const response = await api.get(`/enrollments/student/${userId}`);
            const data = response.data;
            setCourses(data.map(course => ({
                title: course.title,
                flashcards: course.flashcardCount || 0,
                files: course.materials?.length || 0,
                path: `/course/${course.id}`
            })));
        } catch (err) {
            setError(err.response?.data?.message || err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const init = async () => {
            let userId = localStorage.getItem('userId');
            if (!userId) {
                const email = localStorage.getItem('user');
                if (!email) {
                    setError('User email not found');
                    setLoading(false);
                    return;
                }
                userId = await fetchUserIdByEmail(email);
                if (!userId) return;
            }
            fetchEnrolledCourses(userId);
        };
        init();
        // eslint-disable-next-line
    }, []);

    const handleClick = (label) => {
        if (label === "Home") {
            navigate('/dashboard');
        } else if (label === "Profile") {
            navigate('/profile');
        } else if (label === "Explore") {
            navigate('/explore');
        }
    };

    return (
        <div className="library-container">
            <div className="library-logo">
                <img src="/quizzy-logo-homepage.svg" alt="Logo" style={{ width: '100%', height: '100%' }} />
            </div>

            <div className="library-inner-box" />

            {/* Sidebar Buttons */}
            <button className="library-icon-wrapper library-icon-home" onClick={() => handleClick("Home")}>
                <img src="/home-logo.svg" alt="Home" className="library-icon-image" />
                <span className="library-icon-text">Home</span>
            </button>

            <div className="library-icon-wrapper library-icon-library active">
                <div className="library-rectangle-library"></div>
                <img src="/library-logo.svg" alt="Library" className="library-icon-image" />
                <div className="library-icon-text">Library</div>
            </div>

            <button className="library-icon-wrapper library-icon-explore" onClick={() => handleClick("Explore")}>
                <img src="/explore-logo.svg" alt="Explore" className="library-icon-image" />
                <span className="library-icon-text">Explore</span>
            </button>

            <button className="library-icon-wrapper library-icon-profile" onClick={() => handleClick("Profile")}>
                <img src="/profile-logo.svg" alt="Profile" className="library-icon-image" />
                <span className="library-icon-text">Profile</span>
            </button>

            <div className="library-logo-fii-library">
                <img src="/logo-fac-homepage.svg" alt="FII" style={{ width: '100%', height: '100%' }} />
            </div>

            {/* Cards Section */}
            <div className="library-cards-container">
                {loading ? (
                    <div className="library-loading">Loading your courses...</div>
                ) : error ? (
                    <div className="library-error">Error: {error}</div>
                ) : courses.length === 0 ? (
                    <div className="library-empty">
                        You are not enrolled in any courses yet
                        <button
                            className="library-explore-button"
                            onClick={() => navigate('/explore')}
                        >
                            Explore Courses
                        </button>
                    </div>
                ) : (
                    courses.map((course, index) => (
                        <div
                            key={index}
                            className="library-card"
                            onClick={() => course.path && navigate(course.path)}
                            style={{ cursor: course.path ? 'pointer' : 'default' }}
                        >
                            <div className="library-card-header" />
                            <div className="library-card-header-text">
                                <div className="library-course-title">{course.title}</div>
                                <div className="library-course-info">
                                    <span className="library-number">{course.flashcards}</span> Flashcards |
                                    <span className="library-number">{course.files}</span> Files
                                </div>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default Library;