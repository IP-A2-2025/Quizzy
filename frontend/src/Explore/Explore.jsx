import React, { useState, useEffect } from 'react';
import './Explore.css';
import BurgerMenu from '../components/BurgerMenu/BurgerMenu';
import { SidebarLeft, SidebarRight } from '../components/Sidebar';
import { useNavigate, useLocation } from 'react-router-dom';
import { api } from '../utils/api';

const Explore = () => {
    const navigate = useNavigate();
    const location = useLocation(); 
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

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

    return (
        <>
            <BurgerMenu currentPage={location.pathname} />
            
            {/* Desktop Sidebars */}
            <SidebarLeft activeRoute="/explore" />
            <SidebarRight />

            <div className="explore-container">
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
                                        <span className="library-number">{course.materialCount || 0}</span> Files
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </>
    );
};

export default Explore;
