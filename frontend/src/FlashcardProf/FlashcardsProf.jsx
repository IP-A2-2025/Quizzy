import React, { useState, useEffect } from 'react';
import './FlashcardsProf.css';
import { useLocation, useNavigate } from 'react-router-dom';
import { api } from '../utils/api';

const FlashcardsProf = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { courseId, courseTitle } = location.state || {};
    
    const [showAddBox, setShowAddBox] = useState(false);
    const [showQuestionInput, setShowQuestionInput] = useState(false);
    const [showAnswerInput, setShowAnswerInput] = useState(false);
    const [question, setQuestion] = useState('');
    const [answers, setAnswers] = useState([]);
    const [savedQuestions, setSavedQuestions] = useState([]);
    const [saving, setSaving] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [error, setError] = useState(null);
    const [errorDetails, setErrorDetails] = useState(null);
    const [currentTestId, setCurrentTestId] = useState(null);
    const [currentQuestionId, setCurrentQuestionId] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [originalAnswers, setOriginalAnswers] = useState([]);

    // Debug the API endpoints and structure
    useEffect(() => {
        const checkApiEndpoints = async () => {
            if (!courseId) return;
            
            try {
                // Check TestQuestionController endpoints (/questions)
                try {
                    const questionsResponse = await api.get('/questions');
                    console.log("TestQuestionController - Questions API info:", questionsResponse.data);
                } catch (err) {
                    console.log("Couldn't access TestQuestionController questions endpoint");
                }
                
                // Check course-specific questions from TestQuestionController
                try {
                    const courseQuestionsResponse = await api.get(`/questions/course/${courseId}`);
                    console.log("TestQuestionController - Course questions:", courseQuestionsResponse.data);
                } catch (err) {
                    console.log("Couldn't get course questions from TestQuestionController");
                }
                
                // Check TestController endpoints (/tests)
                try {
                    const testsResponse = await api.get(`/tests/course/${courseId}`);
                    console.log("TestController - Course tests:", testsResponse.data);
                } catch (err) {
                    console.log("Couldn't get course tests from TestController");
                }
            } catch (err) {
                console.error("API debugging failed:", err);
            }
        };
        
        checkApiEndpoints();
    }, [courseId]);

    // Find or create a test for this course using TestController
    useEffect(() => {
        const findOrCreateTest = async () => {
            if (!courseId) return;
            
            try {
                // First, check if there's an existing test for this course using TestController
                const testsResponse = await api.get(`/tests/course/${courseId}`);
                
                if (testsResponse.data && Array.isArray(testsResponse.data) && testsResponse.data.length > 0) {
                    // Use the first test we find
                    setCurrentTestId(testsResponse.data[0].id);
                    console.log(`Found existing test with ID: ${testsResponse.data[0].id} via TestController`);
                } else {
                    // No test found, create a new one using TestController
                    const testData = {
                        title: `Test for ${courseTitle || 'course'}`,
                        description: 'Created from flashcard interface',
                        courseId: courseId,
                        professorId: localStorage.getItem('userId') || null
                    };
                    
                    const newTestResponse = await api.post('/tests', testData);
                    if (newTestResponse.data && newTestResponse.data.id) {
                        setCurrentTestId(newTestResponse.data.id);
                        console.log(`Created new test with ID: ${newTestResponse.data.id} via TestController`);
                    }
                }
            } catch (err) {
                console.error("Error finding/creating test via TestController:", err);
            }
        };
        
        findOrCreateTest();
    }, [courseId, courseTitle]);

    // Fetch existing questions for this course using TestQuestionController
    const fetchQuestions = async () => {
        if (!courseId) return;
        
        try {
            // Try to get questions by course ID using TestQuestionController
            const response = await api.get(`/questions/course/${courseId}`);
            if (response.data && Array.isArray(response.data)) {
                console.log("TestQuestionController - Fetched questions:", response.data);
                setSavedQuestions(response.data);
            }
        } catch (err) {
            console.error("Error fetching questions from TestQuestionController:", err);
        }
    };

    useEffect(() => {
        fetchQuestions();
    }, [courseId]);

    const handleNewFlashcardClick = () => {
        setCurrentQuestionId(null);
        setIsEditing(false);
        setShowAddBox(true);
        setShowQuestionInput(false);
        setShowAnswerInput(false);
        setQuestion('');
        setAnswers([]);
        setOriginalAnswers([]);
    };

    const handleAddQuestionClick = () => {
        setShowQuestionInput(true);
    };

    const handleAddAnswerClick = () => {
        setShowAnswerInput(true);
        setAnswers([...answers, { text: '', isCorrect: false }]);
    };

    const handleAnswerChange = (index, newText) => {
        const newAnswers = [...answers];
        newAnswers[index].text = newText;
        setAnswers(newAnswers);
    };

    const toggleCorrect = (index) => {
        const newAnswers = [...answers];
        newAnswers[index].isCorrect = !newAnswers[index].isCorrect;
        console.log(`Toggled answer ${index} to isCorrect:`, newAnswers[index].isCorrect);
        setAnswers(newAnswers);
    };

    const deleteAnswer = (index) => {
        const newAnswers = [...answers];
        newAnswers.splice(index, 1);
        setAnswers(newAnswers);
    };

    const handleQuestionClick = (question) => {
        setIsEditing(true);
        setCurrentQuestionId(question.id);
        setQuestion(question.questionText);
        
        // Load answers for this question using TestAnswerController
        const loadAnswersForQuestion = async () => {
            try {
                // Use TestAnswerController to get answers
                const answersResponse = await api.get(`/answers/question/${question.id}`);
                console.log("TestAnswerController - Raw answers from API:", answersResponse.data);
                
                if (answersResponse.data && Array.isArray(answersResponse.data)) {
                    // Convert to our format
                    const formattedAnswers = answersResponse.data.map(ans => ({
                        id: ans.id,
                        text: ans.optionText,
                        isCorrect: ans.isCorrect
                    }));
                    setAnswers(formattedAnswers);
                    setOriginalAnswers(formattedAnswers);
                    console.log("TestAnswerController - Loaded answers with isCorrect values:", formattedAnswers.map(a => a.isCorrect));
                } else {
                    // If no answers in response, use the ones from the question object if available
                    if (question.answers && Array.isArray(question.answers)) {
                        const existingAnswers = question.answers.map(ans => ({
                            id: ans.id,
                            text: ans.optionText || ans.text,
                            isCorrect: ans.isCorrect
                        }));
                        setAnswers(existingAnswers);
                        setOriginalAnswers(existingAnswers);
                        console.log("Using question answers from TestQuestionController response:", existingAnswers);
                    } else {
                        setAnswers([]);
                        setOriginalAnswers([]);
                    }
                }
            } catch (err) {
                console.error("Error loading answers from TestAnswerController:", err);
                if (question.answers && Array.isArray(question.answers)) {
                    const existingAnswers = question.answers.map(ans => ({
                        id: ans.id,
                        text: ans.optionText || ans.text,
                        isCorrect: ans.isCorrect
                    }));
                    setAnswers(existingAnswers);
                    setOriginalAnswers(existingAnswers);
                } else {
                    setAnswers([]);
                    setOriginalAnswers([]);
                }
            }
        };
        
        loadAnswersForQuestion();
        setShowAddBox(true);
        setShowQuestionInput(true);
    };

    const handleDeleteQuestion = async () => {
        if (!currentQuestionId) {
            setShowAddBox(false);
            return;
        }
        
        setDeleting(true);
        setError(null);
        setErrorDetails(null);
        
        try {
            // Delete the question using TestQuestionController
            await api.delete(`/questions/${currentQuestionId}`);
            console.log(`Question with ID ${currentQuestionId} deleted via TestQuestionController`);
            
            // Update the local state
            setSavedQuestions(prev => 
                prev.filter(q => q.id !== currentQuestionId)
            );
            
            // Clear form and close editor
            setCurrentQuestionId(null);
            setIsEditing(false);
            setQuestion('');
            setAnswers([]);
            setOriginalAnswers([]);
            setShowAddBox(false);
        } catch (err) {
            console.error("Error deleting question via TestQuestionController:", err);
            let errorMessage = "Failed to delete question. Please try again.";
            let errorDetailsMessage = null;
            
            if (err.response) {
                console.error("Response data:", err.response.data);
                console.error("Response status:", err.response.status);
                errorDetailsMessage = `Server error ${err.response.status}`;
                
                if (err.response.data && typeof err.response.data === 'string') {
                    errorDetailsMessage += ` - ${err.response.data}`;
                } else if (err.response.data && err.response.data.message) {
                    errorDetailsMessage += ` - ${err.response.data.message}`;
                }
            }
            
            setError(errorMessage);
            setErrorDetails(errorDetailsMessage);
        } finally {
            setDeleting(false);
        }
    };

    const handleSaveFlashcard = async () => {
        if (!question.trim()) {
            alert("Please enter a question.");
            return;
        }

        const validAnswers = answers.filter(a => a.text.trim() !== '');
        if (validAnswers.length === 0) {
            alert("Please add at least one answer.");
            return;
        }

        if (!currentTestId) {
            setError("No test available. Please try again later.");
            return;
        }

        setSaving(true);
        setError(null);
        setErrorDetails(null);
        
        try {
            if (isEditing && currentQuestionId) {
                // Update existing question using TestQuestionController
                const questionData = {
                    id: currentQuestionId,
                    questionText: question.trim(),
                    pointValue: 1.0,
                    testId: currentTestId
                };
                
                console.log("TestQuestionController - Updating question:", questionData);
                const questionResponse = await api.put(`/questions/${currentQuestionId}`, questionData);
                console.log("TestQuestionController - Question updated:", questionResponse.data);
                
                // Find answers to delete (those in originalAnswers but not in validAnswers)
                const answersToDelete = originalAnswers.filter(
                    original => !validAnswers.some(valid => valid.id === original.id)
                );
                
                // Delete answers that are no longer present using TestAnswerController
                for (const answer of answersToDelete) {
                    if (answer.id) {
                        console.log(`TestAnswerController - Deleting answer ID: ${answer.id}`);
                        await api.delete(`/answers/${answer.id}`);
                    }
                }
                
                // Log all answers being updated
                console.log("TestAnswerController - Valid answers to update:", validAnswers.map(a => ({
                    id: a.id,
                    text: a.text,
                    isCorrect: a.isCorrect
                })));
                
                // Update existing answers and add new ones using TestAnswerController
                for (const answer of validAnswers) {
                    if (answer.id) {
                        // Update existing answer using TestAnswerController
                        const answerData = {
                            id: answer.id,
                            optionText: answer.text,
                            correct: answer.isCorrect,
                            questionId: currentQuestionId
                        };
                        console.log(`TestAnswerController - Updating answer ID: ${answer.id}`, answerData);
                        
                        try {
                            const updateResponse = await api.put(`/answers/${answer.id}`, answerData);
                            console.log("TestAnswerController - Answer update response:", updateResponse.data);
                        } catch (updateErr) {
                            console.error(`Failed to update answer ${answer.id} via TestAnswerController:`, updateErr);
                            throw updateErr;
                        }
                    } else {
                        // Add new answer using TestAnswerController
                        const answerData = {
                            optionText: answer.text,
                            correct: answer.isCorrect,
                            questionId: currentQuestionId
                        };
                        console.log("TestAnswerController - Creating new answer", answerData);
                        
                        try {
                            const createResponse = await api.post('/answers', answerData);
                            console.log("TestAnswerController - Answer create response:", createResponse.data);
                        } catch (createErr) {
                            console.error("Failed to create answer via TestAnswerController:", createErr);
                            throw createErr;
                        }
                    }
                }
                
                // Refresh questions list using TestQuestionController
                await fetchQuestions();
                
                // Reset form
                setCurrentQuestionId(null);
                setIsEditing(false);
                setQuestion('');
                setAnswers([]);
                setOriginalAnswers([]);
                setShowAddBox(false);
            } else {
                // Create new question using TestQuestionController
                const questionData = {
                    questionText: question.trim(),
                    pointValue: 1.0,
                    testId: currentTestId
                };
                
                console.log("TestQuestionController - Creating question:", questionData);
                const questionResponse = await api.post('/questions', questionData);
                console.log("TestQuestionController - Question created:", questionResponse.data);
                
                // Log all answers being created
                console.log("TestAnswerController - Answers to create:", validAnswers.map(a => ({
                    text: a.text,
                    isCorrect: a.isCorrect
                })));
                
                // Create answers using TestAnswerController
                if (questionResponse.data && questionResponse.data.id) {
                    const questionId = questionResponse.data.id;
                    
                    // Create all answers using TestAnswerController
                    for (const answer of validAnswers) {
                        const answerData = {
                            optionText: answer.text,
                            correct: answer.isCorrect,
                            questionId: questionId
                        };
                        
                        try {
                            console.log("TestAnswerController - Creating answer:", answerData);
                            const answerResponse = await api.post('/answers', answerData);
                            console.log("TestAnswerController - Answer created:", answerResponse.data);
                        } catch (ansErr) {
                            console.error("Failed to create answer via TestAnswerController:", ansErr);
                            throw ansErr;
                        }
                    }
                    
                    // Refresh questions list using TestQuestionController
                    await fetchQuestions();
                    
                    // Reset form
                    setQuestion('');
                    setAnswers([]);
                    setOriginalAnswers([]);
                    setShowAddBox(false);
                }
            }
        } catch (err) {
            console.error("Error saving question:", err);
            
            let errorMessage = "Failed to save question. Please try again.";
            let errorDetailsMessage = null;
            
            if (err.response) {
                console.error("Response data:", err.response.data);
                console.error("Response status:", err.response.status);
                errorDetailsMessage = `Server error ${err.response.status}`;
                
                if (err.response.data && typeof err.response.data === 'string') {
                    errorDetailsMessage += ` - ${err.response.data}`;
                } else if (err.response.data && err.response.data.message) {
                    errorDetailsMessage += ` - ${err.response.data.message}`;
                }
            }
            
            setError(errorMessage);
            setErrorDetails(errorDetailsMessage);
        } finally {
            setSaving(false);
        }
    };

    const handleBackToCourse = () => {
        if (courseId) {
            navigate(`/course/${courseId}`);
        } else {
            navigate('/dashboard');
        }
    };

    return (
        <div className="flashcardsProf-container">
            {error && (
                <div className="flashcardsProf-error-notification">
                    <p>{error}</p>
                    {errorDetails && <p className="flashcardsProf-error-details">{errorDetails}</p>}
                </div>
            )}
            
            <div className="flashcardsProf-blue-box">
                <div className="flashcardsProf-white-header">
                    <div className="flashcardsProf-white-header-text">
                        Create test questions for "{courseTitle || 'Software Engineering'}"
                    </div>
                </div>

                <div
                    className="flashcardsProf-new-flashcard"
                    onClick={handleNewFlashcardClick}
                    style={{ cursor: 'pointer' }}
                >
                    <div className="flashcardsProf-new-flashcard-text">+ New test question</div>
                </div>

                <div className="flashcardsProf-saved-cards">
                    {savedQuestions.map((question, index) => (
                        <div 
                            key={index} 
                            className="flashcardsProf-card"
                            onClick={() => handleQuestionClick(question)}
                            style={{ cursor: 'pointer' }}
                        >
                            <div className="flashcardsProf-card-question">
                                <strong>Q:</strong> {question.questionText}
                            </div>
                            <div className="flashcardsProf-card-answers">
                                {question.answers && question.answers.map((ans, i) => (
                                    <div key={i} className={`flashcardsProf-card-answer ${ans.isCorrect ? 'correct' : 'wrong'}`}>
                                        {ans.optionText || ans.text}
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>

                <div className="flashcardsProf-delimitation"></div>

                {showAddBox && (
                    <div className="flashcardsProf-add-box expanded">
                        <div
                            className={`flashcardsProf-add-box-question ${showQuestionInput ? 'flashcardsProf-add-box-question-expanded' : ''}`}
                            onClick={handleAddQuestionClick}
                            style={{ cursor: showQuestionInput ? 'default' : 'pointer' }}
                        >
                            {showQuestionInput ? (
                                <input
                                    type="text"
                                    placeholder="Enter your question"
                                    className="flashcardsProf-input"
                                    value={question}
                                    onChange={(e) => setQuestion(e.target.value)}
                                />
                            ) : (
                                `+ ${isEditing ? 'Edit' : 'Add'} question`
                            )}
                        </div>

                        <div className="flashcardsProf-add-delimitation"></div>

                        <div className="flashcardsProf-answer-section">
                            <div className="flashcardsProf-answer-header">
                                Answers
                                <button className="flashcardsProf-add-answer-btn" onClick={handleAddAnswerClick}>
                                    + Add answer
                                </button>
                            </div>

                            {answers.map((answer, index) => (
                                <div
                                    key={index}
                                    className={`flashcardsProf-answer-row ${answer.isCorrect ? 'correct' : 'wrong'}`}
                                >
                                    <input
                                        type="text"
                                        className="flashcardsProf-input"
                                        value={answer.text}
                                        onChange={(e) => handleAnswerChange(index, e.target.value)}
                                        placeholder="Enter an answer"
                                    />
                                    <button
                                        className={`flashcardsProf-correct-toggle ${answer.isCorrect ? 'correct' : 'wrong'}`}
                                        onClick={() => toggleCorrect(index)}
                                    >
                                        {answer.isCorrect ? 'Correct' : 'Wrong'}
                                    </button>
                                    <button
                                        className="flashcardsProf-delete-answer-btn"
                                        onClick={() => deleteAnswer(index)}
                                    >
                                        Delete
                                    </button>
                                </div>
                            ))}
                        </div>

                        <div className="flashcardsProf-add-box-buttons">
                            <button className="flashcardsProf-save-btn" onClick={handleSaveFlashcard} disabled={saving}>
                                {saving ? "Saving..." : isEditing ? "Update question" : "Save question"}
                            </button>
                            <button 
                                className="flashcardsProf-delete-btn" 
                                onClick={handleDeleteQuestion}
                                disabled={deleting || (!isEditing)}
                            >
                                {deleting ? "Deleting..." : "Delete question"}
                            </button>
                            <button className="flashcardsProf-cancel-btn" onClick={() => setShowAddBox(false)}>
                                Cancel
                            </button>
                            <button className="flashcardsProf-back-btn" onClick={handleBackToCourse}>
                                Back to Course
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default FlashcardsProf;
