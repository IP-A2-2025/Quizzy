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
import { SidebarLeft, SidebarRight } from '../components/Sidebar';

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
  const sliderRef = useRef(null)
  const [currentSlide, setCurrentSlide] = useState(0);

  useEffect(() => {
    // Get user role from localStorage when component mounts
    const role = localStorage.getItem('userRole')
    setUserRole(role)
  }, [])

  useEffect(() => {
    if (notification) {
      // Use longer timeout for enrollment errors 
      const timeout = notification.includes('already have 4 courses') ? 10000 : 5000;
      const timer = setTimeout(() => {
        setNotification(null)
      }, timeout)
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

  // Add function to fetch questions for the course
  const fetchQuestions = useCallback(async () => {
    if (!id) return;
    try {
      const response = await api.get(`/questions/course/${id}`);
      setTests(response.data || []);
    } catch (err) {
      console.error("Error fetching questions:", err);
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
          await fetchQuestions();
        } catch (err) {
          console.error("Error fetching data:", err);
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
        setNotification("Successfully enrolled in course!")
        fetchCourse() // Refresh data after enrollment
      }
    } catch (err) {
      console.error("Enrollment error:", err);
      if (err.response?.data === "You have reached the maximum limit of 4 enrolled courses.") {
        setNotification("You already have 4 courses enrolled. Please unenroll from a course before enrolling in a new one.")
      } else {
        setNotification(err.response?.data || "Failed to enroll in course")
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

  const handleStartLearning = () => {
    if (materials.length > 0 && materials[0].id) {
      navigate(`/flashcards/${materials[0].id}`, { 
        state: { 
          courseId: id,
          courseTitle: course?.title || 'Course'
        } 
      })
    } else {
      setNotification("Nu există materiale cu flashcard-uri disponibile.")
    }
  }

  // eslint-disable-next-line no-unused-vars
  const goToNextSlide = () => {
    if (sliderRef.current) {
      sliderRef.current.slickNext()
    }
  }

  // eslint-disable-next-line no-unused-vars
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
    afterChange: (index) => setCurrentSlide(index),
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
          setNotification('File replaced successfully!');
          // Refresh the course data to get updated materials
          fetchCourse();
        } else {
          setNotification(response.data.message || 'Failed to replace file');
        }
        
      } catch (error) {
        console.error('Error replacing file:', error);
        setNotification(error.response?.data?.message || 'Failed to replace file');
      }
    };
    
    // Trigger file picker
    document.body.appendChild(fileInput);
    fileInput.click();
    document.body.removeChild(fileInput);
  };

  // eslint-disable-next-line no-unused-vars
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
      {notification && <div className="graph-notification">{notification}</div>}
      
      {/* Burger Menu Component */}
      <BurgerMenu currentPage="Library" />
      
      {/* Desktop Sidebars */}
      <SidebarLeft activeRoute="/library" />
      <SidebarRight />

      {/* Main Content Box */}
      <div className="graph-box">
        <div className="graph-content-box">
          {/* Course Header */}
          <div className="graph-header">
            <h1 className="graph-title">
              {course?.title || "Loading..."}
            </h1>
            <p className="graph-description">
              {course?.description || "Course description"}
            </p>
            {userRole === 'student' && course && (
              <div className="enrollment-section">
                {enrolled ? (
                  <button 
                    className="graph-unenroll-button"
                    onClick={handleUnenroll}
                    disabled={unenrolling}
                  >
                    {unenrolling ? "Unenrolling..." : "Unenroll"}
                  </button>
                ) : (
                  <button 
                    className="graph-enroll-button"
                    onClick={handleEnroll}
                    disabled={enrolling}
                  >
                    {enrolling ? "Enrolling..." : "Enroll"}
                  </button>
                )}
              </div>
            )}
            {enrolled && (
              <button className="graph-start-button" onClick={handleStartLearning}>
                Start learning ►
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
            {allFlashcards.length > 0 ? (
              <div className="flashcard-section">
                <div className="slider-container">
                  <button 
                    className="slider-nav-button prev" 
                    onClick={goToPrevSlide}
                    aria-label="Previous slide"
                  >
                    &lt;
                  </button>
                  
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
                  
                  <button 
                    className="slider-nav-button next" 
                    onClick={goToNextSlide}
                    aria-label="Next slide"
                  >
                    &gt;
                  </button>
                </div>
                
                <div className="dots-only-container">
                  {allFlashcards.map((_, index) => (
                    <button 
                      key={index} 
                      className={`custom-dot ${index === currentSlide ? "active" : ""}`}
                      onClick={() => {
                        if (sliderRef.current) {
                          sliderRef.current.slickGoTo(index);
                        }
                      }}
                      aria-label={`Go to slide ${index + 1}`}
                    />
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
            {tests.length > 0 ? (
              <div className="flashcard-section">
                <div className="slider-container">
                  <button 
                    className="slider-nav-button prev" 
                    onClick={goToPrevSlide}
                    aria-label="Previous slide"
                  >
                    &lt;
                  </button>
                  
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
                  
                  <button 
                    className="slider-nav-button next" 
                    onClick={goToNextSlide}
                    aria-label="Next slide"
                  >
                    &gt;
                  </button>
                </div>
                
                <div className="dots-only-container">
                  {tests.map((_, index) => (
                    <button 
                      key={index} 
                      className={`custom-dot ${index === currentSlide ? "active" : ""}`}
                      onClick={() => {
                        if (sliderRef.current) {
                          sliderRef.current.slickGoTo(index);
                        }
                      }}
                      aria-label={`Go to slide ${index + 1}`}
                    />
                  ))}
                </div>
              </div>
            ) : (
              <div className="graph-no-flashcards">
                <p>No questions available for this course yet.</p>
                <p className="graph-no-flashcards-sub">Click the + button to create your first question!</p>
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
            )}
          </div>
        </div>
      </div>
      {error && <div className="library-error">{error}</div>}
    </div>
  )
}

export default CoursePage;