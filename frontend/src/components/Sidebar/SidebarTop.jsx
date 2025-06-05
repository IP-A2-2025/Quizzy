import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './Sidebar.css';

const SidebarTop = ({ activeRoute = '' }) => {
  const navigate = useNavigate();
  const location = useLocation();
  
  const currentPath = location.pathname;
  
  const handleClick = (label) => {
    if (label === "Home") {
      navigate('/dashboard');
    } else if (label === "Library") {
      navigate('/library');
    } else if (label === "Explore") {
      navigate('/explore');
    } else if (label === "Profile") {
      navigate('/profile');
    }
  };

  const getActiveClass = (route) => {
    if (activeRoute && activeRoute === route) return 'active';
    return currentPath === route ? 'active' : '';
  };

  return (
    <div className="sidebar-top">
      <div className="sidebar-top-left">
        <div className="sidebar-logo">
          <img src="/quizzy-logo-homepage.svg" alt="Logo" />
        </div>
      </div>
      
      <div className="sidebar-top-navigation">
        <button
          className={`sidebar-icon-wrapper sidebar-icon-home ${getActiveClass('/dashboard')}`}
          onClick={() => handleClick("Home")}
        >
          {getActiveClass('/dashboard') && <div className="sidebar-rectangle-active"></div>}
          <img src="/home-logo.svg" alt="Home" className="sidebar-icon-image" />
          <span className="sidebar-icon-text">Home</span>
        </button>
        
        <button
          className={`sidebar-icon-wrapper sidebar-icon-library ${getActiveClass('/library')}`}
          onClick={() => handleClick("Library")}
        >
          {getActiveClass('/library') && <div className="sidebar-rectangle-active"></div>}
          <img src="/library-logo.svg" alt="Library" className="sidebar-icon-image" />
          <span className="sidebar-icon-text">Library</span>
        </button>
        
        <button
          className={`sidebar-icon-wrapper sidebar-icon-explore ${getActiveClass('/explore')}`}
          onClick={() => handleClick("Explore")}
        >
          {getActiveClass('/explore') && <div className="sidebar-rectangle-active"></div>}
          <img src="/explore-logo.svg" alt="Explore" className="sidebar-icon-image" />
          <span className="sidebar-icon-text">Explore</span>
        </button>
        
        <button
          className={`sidebar-icon-wrapper sidebar-icon-profile ${getActiveClass('/profile')}`}
          onClick={() => handleClick("Profile")}
        >
          {getActiveClass('/profile') && <div className="sidebar-rectangle-active"></div>}
          <img src="/profile-logo.svg" alt="Profile" className="sidebar-icon-image" />
          <span className="sidebar-icon-text">Profile</span>
        </button>
      </div>
      
      <div className="sidebar-top-right">
        <div className="sidebar-logo-fii">
          <img src="/logo-fac-homepage.svg" alt="FII Logo" />
        </div>
      </div>
    </div>
  );
};

export default SidebarTop;