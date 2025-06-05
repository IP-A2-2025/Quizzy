import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { api } from '../utils/api';
import './Library.css';
import BurgerMenu from '../components/BurgerMenu/BurgerMenu.jsx';
import { SidebarLeft, SidebarRight } from '../components/Sidebar';

function Library() {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const role = localStorage.getItem('userRole');
    setUserRole(role);
    fetchCourses(role);
  }, []);

  const fetchCourses = async (role) => {
    try {
      setLoading(true);
      const userId = localStorage.getItem('userId');
      
      let response;
      if (role === 'student') {
        response = await api.get(`/enrollments/student/${userId}`);
        // If the response contains enrollment data, extract courses
        if (response.data && Array.isArray(response.data)) {
          const coursesData = response.data.map(enrollment => 
            enrollment.course || enrollment
          );
          setCourses(coursesData);
        } else {
          setCourses([]);
        }
      } else if (role === 'profesor') {
        response = await api.get(`/courses/professor/${userId}`);
        setCourses(response.data || []);
      } else {
        response = await api.get('/courses');
        setCourses(response.data || []);
      }
      
    } catch (err) {
      console.error('Error fetching courses:', err);
      setError(err.response?.data?.message || err.message);
      setCourses([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <BurgerMenu currentPage={location.pathname} />
      
      {/* Desktop Sidebars */}
      <SidebarLeft activeRoute="/library" />
      <SidebarRight />

      <div className="explore-container">
        {/* Cards Section */}
        <div className="library-cards-container">
          {loading ? (
            <div className="library-loading">Loading courses...</div>
          ) : error ? (
            <div className="library-error">Error: {error}</div>
          ) : courses.length === 0 ? (
            <div className="library-empty">
              {userRole === 'student'
                ? "You are not enrolled in any courses yet"
                : userRole === 'profesor'
                ? "You are not teaching any courses yet"
                : "No courses available"
              }
            </div>
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
                    <span className="library-number">{course.flashcardCount || 0}</span> Flashcards |{" "}
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
}

export default Library;