"use client"

import { FaFilePdf } from "react-icons/fa"
import "./CoursePage.css"
import { useNavigate, useParams, useLocation } from "react-router-dom"
import Slider from "react-slick"
import "slick-carousel/slick/slick.css"
import "slick-carousel/slick/slick-theme.css"
import { useEffect, useState, useCallback, useRef } from "react"
import { api } from "../utils/api"
import BurgerMenu from "../components/BurgerMenu/BurgerMenu.jsx"
import LoadingSpinner from "../components/LoadingSpinner/LoadingSpinner.jsx"

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
  const [viewMode, setViewMode] = useState('flashcards') // 'flashcards' or 'tests'
  const [tests, setTests] = useState([])
  const [userRole, setUserRole] = useState(null)
  const [flashcardsLoading, setFlashcardsLoading] = useState(true)
  const [filesLoading, setFilesLoading] = useState(true)
  const sliderRef = useRef(null)

  useEffect(() => {
    // Get user role from localStorage when component mounts
    const role = localStorage.getItem('userRole')
    setUserRole(role)
    
    // Set initial loading states
    setFlashcardsLoading(true)
    setFilesLoading(true)
  }, [])
  useEffect(() => {
    if (notification) {
      // Use longer timeout for file replacement operations
      const timeout = notification.includes('Replacing') ? 0 : 5000; // Don't auto-hide "Replacing..." messages
      if (timeout > 0) {
        const timer = setTimeout(() => {
          setNotification(null)
        }, timeout)
        return () => clearTimeout(timer)
      }
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
        // Disable loading when materials are loaded
        setFilesLoading(false)
      } catch (materialErr) {
        console.error("Error fetching materials:", materialErr)
        setMaterials([])
        setFilesLoading(false)
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

  // Add function to fetch questions for the course
  const fetchQuestions = useCallback(async () => {
    if (!id) return;
    try {
      const response = await api.get(`/questions/course/${id}`);
      setTests(response.data || []);
      // Disable loading when questions are loaded
      setFlashcardsLoading(false);
    } catch (err) {
      console.error("Error fetching questions:", err);
      setFlashcardsLoading(false);
    }
  }, [id]);

  // Modify useEffect to fetch questions
  useEffect(() => {
    if (courseFromState) {
      checkEnrollment();
      const fetchData = async () => {
        try {
          const materialsResponse = await api.get(`/Material/course/${id}`);
          setMaterials(materialsResponse.data || []);
          // Disable files loading when materials are loaded
          setFilesLoading(false);
          await fetchQuestions();
        } catch (err) {
          console.error("Error fetching data:", err);
          // Disable loading on error
          setFilesLoading(false);
          setFlashcardsLoading(false);
        }
      };
      fetchData();
    } else {
      fetchCourse();
      fetchQuestions();
    }
  }, [id, courseFromState, fetchCourse, checkEnrollment, fetchQuestions]);

  const handleEnroll = async () => {
    setEnrolling(true)
    setError(null)
    const userId = localStorage.getItem("userId")
    try {
      const response = await api.post(`/enrollments?userId=${userId}&courseId=${id}`)
      if (response.status === 200) {
        setEnrolled(true)
        // Don't show success notification
        fetchCourse() // Refresh data after enrollment
      }
    } catch (err) {
      // Check if response has data (string) or a nested message object
      const errorMessage = err.response?.data || err.response?.data?.message || "Failed to enroll in course";
      // Replace the backend message with the requested format
      if (errorMessage.includes("maximum limit of 4 enrolled courses")) {
        setNotification("you allready have enrolled in 4 courses");
      } else {
        setNotification(errorMessage);
      }
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
      // Don't show success notification
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
        courseId: id,
        courseTitle: course?.title || 'Course'
      },
    })
  }

  const handleFlashcardClick = (flashcard, materialId) => {
    // Navigate to the flashcards page with the specific material
    navigate(`/flashcards/${materialId}`, {
      state: {
        courseId: id,
        courseTitle: course?.title || 'Course',
        startingFlashcardId: flashcard.id // Optional: if you want to start at a specific flashcard
      }
    });
  };

  const handleNavClick = (label) => {
    if (label === "Home") navigate("/dashboard")
    else if (label === "Library") navigate("/library")
    else if (label === "Explore") navigate("/explore")
    else if (label === "Profile") navigate("/profile")
  }

  const handleStartLearning = () => {
    if (materials.length > 0 && materials[0].id) {
      navigate(`/flashcards/${materials[0].id}`, { 
        state: { 
          courseId: id,
          courseTitle: course?.title || 'Course'
        } 
      })
    } else {
      // Don't show notification
      console.log("No materials with flashcards available");
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

  // Add function to handle creating a new test
  const handleAddTest = () => {
    if (!id) {
      console.error("Course ID is missing");
      return;
    }
    
    navigate('/flashcardsProf', {
      state: {
        courseId: id,
        courseTitle: course?.title || 'Unknown Course',
        createNewTest: true
      }
    });
  };

  // Add function to handle viewing/editing a test
  const handleViewTest = (question) => {
    if (!question?.id) {
      console.error("Question ID is missing");
      return;
    }
    
    navigate('/flashcardsProf', {
      state: {
        courseId: id,
        courseTitle: course?.title || 'Unknown Course',
        questionId: question.id,
        question: question
      }
    });
  };

  // Add function to handle replacing a file
  const handleReplaceFile = async (materialId, materialName) => {
    if (!materialId) {
      console.error("Material ID is missing");
      return;
    }
    
    // Create a file input element
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = '.pdf';
    fileInput.style.display = 'none';
    
    // Handle file selection
    fileInput.onchange = async (event) => {
      const file = event.target.files[0];
      if (!file) {
        return;
      }
      
      // Validate file type
      if (!file.name.toLowerCase().endsWith('.pdf')) {
        setNotification('Please select a PDF file');
        return;
      }
      
      try {
        setNotification(`Replacing "${materialName}"...`);
        
        // Find the material to get its path
        const material = materials.find(mat => mat.id === materialId);
        if (!material) {
          setNotification('Material not found');
          return;
        }
        
        // Get the current user ID
        const userId = localStorage.getItem('userId') || '1';
        
        // Create FormData for file upload
        const formData = new FormData();
        formData.append('oldCoursePath', material.path);
        formData.append('newCourseFile', file);
        formData.append('userId', userId);
        
        console.log('Replacing file with data:', {
          oldCoursePath: material.path,
          newFileName: file.name,
          userId: userId
        });
        
        // Call the replace course API
        const response = await api.postFile('/Material/replace-course', formData);
        
        if (response.data.status === 'success') {
          setNotification(`Successfully replaced "${materialName}" with "${file.name}"`);
          
          // Refresh materials to show the updated file
          try {
            const materialsResponse = await api.get(`/Material/course/${id}`);
            setMaterials(materialsResponse.data || []);
          } catch (refreshErr) {
            console.error("Error refreshing materials:", refreshErr);
          }
          
          // Show flashcard generation status
          if (response.data.flashcardStatus === 'success') {
            setNotification(`File replaced successfully! Generated ${response.data.importedFlashcards} new flashcards.`);
          } else if (response.data.flashcardStatus === 'failed') {
            setNotification(`File replaced successfully, but flashcard generation failed: ${response.data.flashcardError}`);
          }
          
        } else {
          setNotification(`Failed to replace file: ${response.data.message}`);
        }
        
      } catch (error) {
        console.error('Error replacing file:', error);
        const errorMessage = error.response?.data?.message || error.message || 'Failed to replace file';
        setNotification(`Error: ${errorMessage}`);
      }
    };
    
    // Trigger file picker
    document.body.appendChild(fileInput);
    fileInput.click();
    document.body.removeChild(fileInput);
  };

  // Simple navigation function for desktop buttons
  const handleDesktopNavClick = (label) => {
    if (label === "Home") {
      navigate("/dashboard")
      window.location.reload()
    }
    else if (label === "Library") navigate("/library")
    else if (label === "Explore") navigate("/explore")
    else if (label === "Profile") navigate("/profile")
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
    <div className={`graph-container ${userRole === 'student' ? 'student-view' : 'professor-view'}`}>
      {/* Removed general notification display */}
      
      {/* Burger Menu Component */}
      <BurgerMenu currentPage="Library" />      {/* Quizzy Logo positioned in top left */}
      <div className="graph-logo">
        <img src="/quizzy-logo-homepage.svg" alt="Logo" />
      </div>

      {/* Sidebar Container */}
      <div className="graph-sidebar">        
        {/* Navigation Container */}
        <div className="graph-nav">
          <button className="graph-icon-wrapper" onClick={() => handleDesktopNavClick("Home")}>
            <img src="/home-logo.svg" alt="Home" className="graph-icon-image" />
            <span className="graph-icon-text">Home</span>
          </button>

          <button className="graph-icon-wrapper graph-icon-active">
            <div className="graph-rectangle-active"></div>
            <img src="/library-logo.svg" alt="Library" className="graph-icon-image" />
            <div className="graph-icon-text">Library</div>
          </button>

          <button className="graph-icon-wrapper" onClick={() => handleDesktopNavClick("Explore")}>
            <img src="/explore-logo.svg" alt="Explore" className="graph-icon-image" />
            <span className="graph-icon-text">Explore</span>
          </button>

          <button className="graph-icon-wrapper" onClick={() => handleDesktopNavClick("Profile")}>
            <img src="/profile-logo.svg" alt="Profile" className="graph-icon-image" />
            <span className="graph-icon-text">Profile</span>
          </button>
        </div>
      </div>
      
      {/* FII Logo positioned on right border */}
      <div className="graph-logo-fii">
        <img src="/logo-fac-homepage.svg" alt="FII Logo" />
      </div>

      {/* Main Content Box */}
      <div className="graph-box">
        <div className="graph-content-box">
        <div className="graph-header">
          <h1 className="graph-title">{course.title}</h1>
          <div className="graph-buttons-container">
          {enrolled ? (
            <>
              <button className="graph-unenroll-button" onClick={handleUnenroll} disabled={unenrolling}>
                {unenrolling ? "Unenrolling..." : "Unenroll ►"}
              </button>
              <button className="graph-enrolled-button" disabled>
                Enrolled
              </button>
              <button className="graph-start-button" onClick={handleStartLearning}>
                Start learning ►
              </button>
            </>
          ) : (
            <button className="graph-enroll-button" onClick={handleEnroll} disabled={enrolling}>
              {enrolling ? "Enrolling..." : "Enroll"}
            </button>
          )}
          </div>
        </div>

        <div className="graph-divider"></div>

        <div className="view-toggle-container">
          <button 
            className={`toggle-button ${viewMode === 'flashcards' ? 'active' : ''}`}
            onClick={() => setViewMode('flashcards')}
          >
            Flashcards
          </button>
          <button 
            className={`toggle-button ${viewMode === 'tests' ? 'active' : ''}`}
            onClick={() => setViewMode('tests')}
          >
            Tests
          </button>
        </div>

        {viewMode === 'flashcards' ? (
          <div className="graph-flashcards">
            <div className="flashcards-header">
              <h2 className="graph-section-title">Flashcards</h2>
            </div>
            {flashcardsLoading ? (
              <LoadingSpinner />
            ) : allFlashcards.length > 0 ? (
              <div className="flashcard-section">
                <Slider ref={sliderRef} {...sliderSettings}>
                  {materials.map((material) =>
                    material.flashcards?.map((fc, idx) => (
                      <div
                        className="graph-flashcard clickable-flashcard"
                        key={fc.id || `${material.id}-${idx}`}
                        onClick={() => handleFlashcardClick(fc, material.id)}
                        style={{ cursor: 'pointer' }}
                      >
                        <h3>{fc.question || "No question available"}</h3>
                        {fc.questionType === 'Multiple' || fc.questionType === 'Teorie' ? (
                          <p className="flashcard-preview-type">Multiple Choice Question</p>
                        ) : (
                          fc.answer && <p className="flashcard-preview-answer">{fc.answer.substring(0, 100)}...</p>
                        )}
                        <div className="flashcard-preview-footer">
                          <span className="material-name">{material.name}</span>
                          <span className="click-hint">Click to study →</span>
                        </div>
                      </div>
                    ))
                  ).flat()}
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
        ) : (
          <div className="graph-flashcards">
            <div className="flashcards-header">
              <h2 className="graph-section-title">Questions</h2>
              {userRole !== 'student' && (
                <button className="graph-add-button" onClick={handleAddTest}>+</button>
              )}
            </div>
            {flashcardsLoading ? (
              <LoadingSpinner />
            ) : tests.length > 0 ? (
              <div className="flashcard-section">
                <Slider ref={sliderRef} {...sliderSettings}>
                  {tests.map((question) => (
                    <div 
                      className={`graph-flashcard ${userRole !== 'student' ? 'clickable' : ''}`} 
                      key={question.id} 
                      onClick={() => userRole !== 'student' ? handleViewTest(question) : null}
                      style={userRole === 'student' ? { cursor: 'default' } : {}}
                    >
                      <h3>{question.questionText}</h3>
                      {question.answers && question.answers.length > 0 && (
                        <div className="answers-preview">
                          {question.answers.map((answer, index) => (
                            <p key={index} className={answer.correct ? 'correct-answer' : ''}>
                              {answer.optionText}
                            </p>
                          ))}
                        </div>
                      )}
                    </div>
                  ))}
                </Slider>
                <div className="dots-only-container">
                  {tests.map((_, index) => (
                    <div key={index} className={`custom-dot ${index === 0 ? "active" : ""}`} />
                  ))}
                </div>
              </div>
            ) : (
              <div className="graph-no-flashcards">
                <p>No questions available for this course yet.</p>
                {userRole !== 'student' && (
                  <p className="graph-no-flashcards-sub">Click the + button to create a test!</p>
                )}
              </div>
            )}
          </div>
        )}

        <div className="graph-divider"></div>

        <div className="graph-files">
          <div className="graph-file-header">
            <h2 className="graph-section-title">Files</h2>
            <h2 className="graph-file-count">{materials.length}</h2>
          </div>
          <div className="graph-files-container">
          {filesLoading ? (
            <LoadingSpinner />
          ) : materials.length > 0 ? (
            materials.map((mat, i) => (              <div key={mat.id || i}>
                <div className="graph-file-entry clickable" onClick={() => handleMaterialClick(mat.path)}>
                  <FaFilePdf size={40} color="#E74C3C" />
                  <div className="graph-file-text">
                    <p className="graph-file-name">{mat.name || "Unnamed Material"}</p>
                    <p className="graph-file-details">
                      {mat.pages ? `${mat.pages} pages` : "Unknown pages"} |
                      {mat.flashcards ? ` ${mat.flashcards.length} flashcards` : " No flashcards"}
                    </p>
                  </div>
                  {userRole !== 'student' && (
                    <button 
                      className="graph-replace-button" 
                      onClick={(e) => {
                        e.stopPropagation();
                        handleReplaceFile(mat.id, mat.name);
                      }}
                      title="Replace this file"
                    >
                      ↻
                    </button>
                  )}
                </div>
                {i < materials.length - 1 && <div className="graph-divider-small"></div>}
              </div>
            ))
          ) : (
            <div className="graph-file-entry">No files available.</div>
          )}          </div>
        </div>
      </div>
      </div>
      {error && <div className="library-error">{error}</div>}
              {notification && notification.includes('allready') && (
          <div className="notification error">
            {notification}
          </div>
        )}
      {/* Remove any other notification displays */}
    </div>
  )
}

export default CoursePage;