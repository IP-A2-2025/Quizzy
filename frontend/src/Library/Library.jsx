import React from 'react';
import './Library.css';
import { useNavigate } from 'react-router-dom';

const Library = () => {
    const navigate = useNavigate();

    const handleClick = (label) => {
        if (label === "Home") {
            navigate('/dashboard');
        } else if (label === "Profile") {
            navigate('/profile');
        } else if (label === "Explore") {
            navigate('/explore');
        }
    };

    const courses = [
        { title: 'Graph Algorithms', flashcards: 24, files: 6, path: '/graph-algorithms' },
        { title: 'Software Engineering', flashcards: 37, files: 12 },
        { title: 'English', flashcards: 7, files: 2 },
        { title: 'Databases', flashcards: 95, files: 16 },
    ];

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
                {courses.map((course, index) => (
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
                ))}
            </div>
        </div>
    );
};

export default Library;