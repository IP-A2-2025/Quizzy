import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import './BurgerMenu.css'

const BurgerMenu = ({ currentPage = "" }) => {
  const [isBurgerMenuOpen, setIsBurgerMenuOpen] = useState(false)
  const navigate = useNavigate()

  const toggleBurgerMenu = () => {
    setIsBurgerMenuOpen(!isBurgerMenuOpen)
  }

  const closeBurgerMenu = () => {
    setIsBurgerMenuOpen(false)
  }

  const handleNavClick = (label) => {
    closeBurgerMenu()
    if (label === "Home") {
      navigate("/dashboard")
      window.location.reload()
    }
    else if (label === "Library") navigate("/library")
    else if (label === "Explore") navigate("/explore")
    else if (label === "Profile") navigate("/profile")
  }

  // Map page names to check which should be active
  const getActiveClass = (itemName) => {
    const pageMapping = {
      "Home": ["dashboard", "home"],
      "Library": ["library", "course"],
      "Explore": ["explore"],
      "Profile": ["profile"]
    }
    
    const currentPageLower = currentPage.toLowerCase()
    return pageMapping[itemName]?.some(page => 
      currentPageLower.includes(page) || window.location.pathname.includes(page)
    ) ? "burger-menu-item-active" : ""
  }

  return (
    <>      {/* Burger Menu Button */}
      <button 
        className={`burger-menu-button ${isBurgerMenuOpen ? 'active' : ''}`}
        onClick={(e) => {
          e.stopPropagation()
          toggleBurgerMenu()
        }}
      >
        <div className="burger-line"></div>
        <div className="burger-line"></div>
        <div className="burger-line"></div>
      </button>

      {/* Burger Menu Overlay */}
      {isBurgerMenuOpen && (
        <div className="burger-menu-overlay" onClick={(e) => e.stopPropagation()}>
          <div className="burger-menu-content">
            {/* Logos in burger menu */}
            <div className="burger-menu-logos">
              <div className="burger-menu-logo">
                <img src="/quizzy-logo-homepage.svg" alt="Quizzy Logo" />
              </div>
              <div className="burger-menu-logo-fii">
                <img src="/logo-fac-homepage.svg" alt="FII Logo" />
              </div>
            </div>
            
            {/* Navigation items in burger menu */}
            <div className="burger-menu-nav">
              <button 
                className={`burger-menu-item ${getActiveClass("Home")}`}
                onClick={() => handleNavClick("Home")}
              >
                <img src="/home-logo.svg" alt="Home" className="burger-menu-icon" />
                <span>Home</span>
              </button>
              
              <button 
                className={`burger-menu-item ${getActiveClass("Library")}`}
                onClick={() => handleNavClick("Library")}
              >
                <img src="/library-logo.svg" alt="Library" className="burger-menu-icon" />
                <span>Library</span>
              </button>
              
              <button 
                className={`burger-menu-item ${getActiveClass("Explore")}`}
                onClick={() => handleNavClick("Explore")}
              >
                <img src="/explore-logo.svg" alt="Explore" className="burger-menu-icon" />
                <span>Explore</span>
              </button>
              
              <button 
                className={`burger-menu-item ${getActiveClass("Profile")}`}
                onClick={() => handleNavClick("Profile")}
              >
                <img src="/profile-logo.svg" alt="Profile" className="burger-menu-icon" />
                <span>Profile</span>
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  )
}

export default BurgerMenu
