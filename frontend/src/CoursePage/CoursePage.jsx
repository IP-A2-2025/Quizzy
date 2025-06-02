"use client"

import { FaFilePdf } from "react-icons/fa"
import "./CoursePage.css"
import { useNavigate, useParams, useLocation } from "react-router-dom"
import Slider from "react-slick"
import "slick-carousel/slick/slick.css"
import "slick-carousel/slick/slick-theme.css"
import { useEffect, useState, useCallback, useRef } from "react"
import { api } from "../utils/api"

function CoursePage() {
  const navigate = useNavigate()
  const { id } = useParams()
  const location = useLocation()
  const courseFromState = location.state?.course

  const [course, setCourse] = useState(courseFromState || null)
  const [loading, setLoading] = useState(!courseFromState)
  const [error, setError] = useState(null)
  const [enrolled, setEnrolled] = useState(false)
  const [enrolling, setEnrolling] = useState(false)
  const [unenrolling, setUnenrolling] = useState(false)
  const [notification, setNotification] = useState(null)
  const [materials, setMaterials] = useState([])
  const sliderRef = useRef(null)

  useEffect(() => {
    if (notification) {
      const timer = setTimeout(() => {
        setNotification(null)
      }, 3000)
      return () => clearTimeout(timer)
    }
  }, [notification])

  // Fetch course data
  const fetchCourse = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await api.get(`/courses/${id}`)
      setCourse(response.data)

      // Get materials for this course
      try {
        const materialsResponse = await api.get(`/Material/course/${id}`)
        setMaterials(materialsResponse.data || [])
      } catch (materialErr) {
        console.error("Error fetching materials:", materialErr)
        setMaterials([])
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message)
    } finally {
      setLoading(false)
    }
  }, [id])

  // Check enrollment status
  const checkEnrollment = useCallback(async () => {
    const userId = localStorage.getItem("userId")
    if (!userId) return setEnrolled(false)
    try {
      const response = await api.get(`/enrollments/student/${userId}`)
      const enrolledCourses = response.data
      setEnrolled(enrolledCourses.some((c) => String(c.id) === String(id)))
    } catch {
      setEnrolled(false)
    }
  }, [id])

  // Initial data loading
  useEffect(() => {
    if (courseFromState) {
      // If course is from state, still check enrollment
      checkEnrollment()

      // Try to fetch materials
      const fetchMaterials = async () => {
        try {
          const materialsResponse = await api.get(`/Material/course/${id}`)
          setMaterials(materialsResponse.data || [])
        } catch (err) {
          console.error("Error fetching materials:", err)
        }
      }
      fetchMaterials()
    } else {
      fetchCourse()
    }
  }, [id, courseFromState, fetchCourse, checkEnrollment])

  // Check enrollment whenever course changes or enrollment actions happen
  useEffect(() => {
    if (course) {
      checkEnrollment()
    }
  }, [course, enrolling, unenrolling, checkEnrollment])

  const handleEnroll = async () => {
    setEnrolling(true)
    setError(null)
    const userId = localStorage.getItem("userId")
    try {
      const response = await api.post(`/enrollments?userId=${userId}&courseId=${id}`)
      if (response.status === 200) {
        setEnrolled(true)
        setNotification("Successfully enrolled in course!")
        fetchCourse() // Refresh data after enrollment
      }
    } catch (err) {
      setNotification(err.response?.data?.message || "Failed to enroll in course")
    } finally {
      setEnrolling(false)
    }
  }

  const handleUnenroll = async () => {
    setUnenrolling(true)
    setError(null)
    const userId = localStorage.getItem("userId")
    try {
      await api.delete(`/enrollments/${userId}/course/${id}`)
      setEnrolled(false)
      setNotification("Successfully unenrolled from course")
      fetchCourse() // Refresh data after unenrollment
    } catch (err) {
      setNotification(err.response?.data?.message || err.message)
    } finally {
      setUnenrolling(false)
    }
  }

  const handleMaterialClick = (material) => {
    console.log("Navigating to PDF path:", material) // Debug log
    navigate(`/Material/path/${material}`, {
      state: {
        title: material.split("/").pop().replace(".pdf", "") || "Document",
      },
    })
  }

  const handleNavClick = (label) => {
    if (label === "Home") navigate("/dashboard")
    else if (label === "Library") navigate("/library")
    else if (label === "Explore") navigate("/explore")
    else if (label === "Profile") navigate("/profile")
  }
  const handleStartLearning = () => {
    if (materials.length > 0 && materials[0].id) {
      navigate(`/flashcards/${materials[0].id}`)
    } else {
      setNotification("Nu există materiale cu flashcard-uri disponibile.")
    }
  }
  const goToNextSlide = () => {
    if (sliderRef.current) {
      sliderRef.current.slickNext()
    }
  }

  const goToPrevSlide = () => {
    if (sliderRef.current) {
      sliderRef.current.slickPrev()
    }
  }

  const sliderSettings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 3,
    slidesToScroll: 1,
    centerMode: true,
    centerPadding: "40px",
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 2,
          centerPadding: "30px",
        },
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 1,
          centerPadding: "20px",
        },
      },
    ],
  }

  if (loading)
    return (
      <div className="graph-container">
        <div className="library-loading">Loading course...</div>
      </div>
    )
  if (error)
    return (
      <div className="graph-container">
        <div className="library-loading">Error loading course. Please try again.</div>
      </div>
    )
  if (!course)
    return (
      <div className="graph-container">
        <div className="library-loading">No course found.</div>
      </div>
    )

  // Debug logs to verify course data
  console.log("Course data:", course)
  console.log("Materials:", materials)

  // Get flashcards from materials
  const allFlashcards = materials.flatMap((mat) => (mat.flashcards ? mat.flashcards : []))

  return (
    <div className="graph-container">
      {notification && <div className="graph-notification">{notification}</div>}

      <div className="graph-logo">
        <img src="/quizzy-logo-homepage.svg" alt="Logo" />
      </div>

      <div className="graph-box" />

      {/* Sidebar buttons */}
      <button className="graph-icon-wrapper graph-icon-home" onClick={() => handleNavClick("Home")}>
        <img src="/home-logo.svg" alt="Home" className="graph-icon-image" />
        <span className="graph-icon-text">Home</span>
      </button>

      <button className="graph-icon-wrapper graph-icon-library graph-icon-active">
        <div className="graph-rectangle-active"></div>
        <img src="/library-logo.svg" alt="Library" className="graph-icon-image" />
        <div className="graph-icon-text">Library</div>
      </button>

      <button className="graph-icon-wrapper graph-icon-explore" onClick={() => handleNavClick("Explore")}>
        <img src="/explore-logo.svg" alt="Explore" className="graph-icon-image" />
        <span className="graph-icon-text">Explore</span>
      </button>

      <button className="graph-icon-wrapper graph-icon-profile" onClick={() => handleNavClick("Profile")}>
        <img src="/profile-logo.svg" alt="Profile" className="graph-icon-image" />
        <span className="graph-icon-text">Profile</span>
      </button>

      <div className="graph-logo-fii">
        <img src="/logo-fac-homepage.svg" alt="FII Logo" />
      </div>

      <div className="graph-content-box">
        <div className="graph-header">
          <h1 className="graph-title">{course.title}</h1>
          {enrolled ? (
            <>
              <button className="graph-unenroll-button" onClick={handleUnenroll} disabled={unenrolling}>
                {unenrolling ? "Unenrolling..." : "Unenroll ►"}
              </button>
              <button className="graph-enrolled-button" disabled>
                Enrolled
              </button>
            </>
          ) : (
            <button className="graph-enroll-button" onClick={handleEnroll} disabled={enrolling}>
              {enrolling ? "Enrolling..." : "Enroll"}
            </button>
          )}
          <button className="graph-start-button" onClick={handleStartLearning}>
            Start learning ►
          </button>
        </div>

        <div className="graph-divider"></div>

        <div className="graph-flashcards">
          <div className="flashcards-header">
            <h2 className="graph-section-title">Flashcards</h2>
            <div className="flashcards-buttons">
              <button className="flashcard-add-button">Add</button>
              <button className="flashcard-remove-button">Remove</button>
            </div>
          </div>
          {allFlashcards.length > 0 ? (
            <div className="flashcard-section">
              <Slider ref={sliderRef} {...sliderSettings}>
                {allFlashcards.map((fc, idx) => (
                  <div className="graph-flashcard" key={fc.id || idx}>
                    <h3>{fc.question || "No question available"}</h3>
                    {fc.answer && <p>{fc.answer}</p>}
                  </div>
                ))}
              </Slider>
              <div className="dots-only-container">
                {allFlashcards.map((_, index) => (
                  <div key={index} className={`custom-dot ${index === 0 ? "active" : ""}`} />
                ))}
              </div>
            </div>
          ) : (
            <div className="graph-no-flashcards">
              <p>No flashcards available for this course yet.</p>
              <p className="graph-no-flashcards-sub">Check back later for study materials!</p>
            </div>
          )}
        </div>

        <div className="graph-divider"></div>

        <div className="graph-files">
          <div className="graph-file-header">
            <h2 className="graph-section-title">Files</h2>
            <h2 className="graph-file-count">{materials.length}</h2>
          </div>
          {materials.length > 0 ? (
            materials.map((mat, i) => (
              <div key={mat.id || i}>
                <div className="graph-file-entry clickable" onClick={() => handleMaterialClick(mat.path)}>
                  <FaFilePdf size={40} color="#E74C3C" />
                  <div className="graph-file-text">
                    <p className="graph-file-name">{mat.name || "Unnamed Material"}</p>
                    <p className="graph-file-details">
                      {mat.pages ? `${mat.pages} pages` : "Unknown pages"} |
                      {mat.flashcards ? ` ${mat.flashcards.length} flashcards` : " No flashcards"}
                    </p>
                  </div>
                </div>
                {i < materials.length - 1 && <div className="graph-divider-small"></div>}
              </div>
            ))
          ) : (
            <div className="graph-file-entry">No files available.</div>
          )}
        </div>
      </div>
      {error && <div className="library-error">{error}</div>}
    </div>
  )
}

export default CoursePage
