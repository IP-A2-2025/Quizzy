import React, { useState, useEffect } from 'react';
import './Flashcards.css';
import { useNavigate, useParams } from 'react-router-dom';
import { api } from '../utils/api';

const Flashcards = () => {
    const navigate = useNavigate();
    const { materialId } = useParams(); // dacă dorești să preiei flashcards după materialId din URL

    // State pentru flashcards
    const [flashcards, setFlashcards] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Statele existente
    const [index, setIndex] = useState(0);
    const [showAnswer, setShowAnswer] = useState(false);
    const [selectedOption, setSelectedOption] = useState(null);
    const [feedbackMessage, setFeedbackMessage] = useState(null);
    const [ratings, setRatings] = useState([]);
    const [showKeyboardInput, setShowKeyboardInput] = useState(false);
    const [inputText, setInputText] = useState('');
    const [isMobile, setIsMobile] = useState(false);
    const [score, setScore] = useState(null);

    const ratingColors = {
        0: "#2D852D",  // Good (😊) - Green
        1: "#E2A54D",  // Neutral (😐) - Yellow/Orange
        2: "#940A00",  // Bad (😡) - Red
        "-1": "#CCCCCC" // Default - Gray (not rated)
    };

    // Preluăm flashcards de la API
    useEffect(() => {
        const fetchFlashcards = async () => {
            try {
                setLoading(true);
                let response;

                // Verificăm dacă avem materialId pentru a face cererea corespunzătoare
                if (materialId) {
                    response = await api.get(`/Flashcard/material/${materialId}`);
                } else {
                    // Dacă nu avem materialId, luăm toate flashcards-urile
                    // Sau poți înlocui cu userId dacă vrei să le filtrezi după user
                    response = await api.get('/Flashcard');
                }

                // Procesăm datele primite pentru a le face compatibile cu componenta
                const processedFlashcards = response.data.map(card => {
                    // Procesăm răspunsurile
                    const processedCard = {
                        id: card.id,
                        question: card.question,
                        level: card.level,
                        lastStudiedAt: card.lastStudiedAt,
                        questionType: card.questionType
                    };

                    // Verificăm dacă este o întrebare cu opțiuni multiple
                    if (card.questionType === 'Multiple') {
                        // Extragem opțiunile și răspunsul corect
                        const options = card.answers.map(answer => answer.optionText);
                        const correctAnswer = card.answers.find(answer => answer.correct)?.optionText;
                        processedCard.options = options;
                        processedCard.correctAnswer = correctAnswer;
                    } else {
                        // Pentru întrebări cu răspuns simplu
                        processedCard.answer = card.answers.find(answer => answer.correct)?.text ||
                            card.answers[0]?.text ||
                            "No answer provided";
                    }

                    return processedCard;
                });

                setFlashcards(processedFlashcards);
                setRatings(Array(processedFlashcards.length).fill(-1));
                setLoading(false);
            } catch (err) {
                console.error('Error fetching flashcards:', err);
                setError('Failed to load flashcards. Please try again later.');
                setLoading(false);
            }
        };

        fetchFlashcards();
    }, [materialId]);

    // Detectăm dispozitivul mobil (cod existent)
    useEffect(() => {
        const checkScreenSize = () => {
            setIsMobile(window.innerWidth <= 480);
        };

        checkScreenSize();
        window.addEventListener('resize', checkScreenSize);

        return () => {
            window.removeEventListener('resize', checkScreenSize);
        };
    }, []);

    const current = flashcards[index];

    const nextCard = () => {
        if (index < flashcards.length - 1) {
            setIndex(index + 1);
            setShowAnswer(false);
            setSelectedOption(null);
            setFeedbackMessage(null);
            setShowKeyboardInput(false);
            setScore(null);
            setInputText('');
        }
    };

    const prevCard = () => {
        if (index > 0) {
            setIndex(index - 1);
            setShowAnswer(false);
            setSelectedOption(null);
            setFeedbackMessage(null);
            setShowKeyboardInput(false);
            setScore(null);
            setInputText('');
        }
    };

    const handleOptionSelect = (option) => {
        if (showAnswer) return;

        setSelectedOption(option);
        setShowAnswer(true);

        if (option === current.correctAnswer) {
            setFeedbackMessage("Correct!");
        } else {
            setFeedbackMessage("Wrong!");
        }
    };

    const handleFeedback = (type, ratingIndex) => {
        const newRatings = [...ratings];
        newRatings[index] = ratingIndex;
        setRatings(newRatings);
    };

    const resetRatings = () => {
        setRatings(Array(flashcards.length).fill(-1));
        setIndex(0);
        setShowAnswer(false);
        setSelectedOption(null);
        setFeedbackMessage(null);
        setShowKeyboardInput(false);
        setScore(null);
        setInputText('');
    };

    const toggleKeyboardInput = () => {
        setShowKeyboardInput(!showKeyboardInput);
        if (showKeyboardInput) {
            setInputText('');
        }
    };

    const handleInputChange = (e) => {
        setInputText(e.target.value);
    };

    const handleSubmitInput = async () => {
        if (!inputText.trim()) return;

        setShowAnswer(true);
        console.log('Sending:', {
            question: current.question,
            //officialAnswer: "Coada functioneaza pe principiul first-in-first-out, pe cand stiva merge pe principiul last-in-first-out",
            officialAnswer: current.answer,
            usersAnswer: inputText
        });
        try {
            const res = await api.post('/api/gemini/compare-users-answer-to-the-official-answer',  {

                    question: current.question,
                    //officialAnswer: "Coada functioneaza pe principiul first-in-first-out, pe cand stiva merge pe principiul last-in-first-out",
                    officialAnswer: current.answer,
                    usersAnswer: inputText

            });
            setScore(res.data);
        } catch (err) {
            console.error('Comparison error:', err);
            setFeedbackMessage("Error comparing your answer");
        }
    };

    const navigateBack = () => {
        navigate('/graph-algorithms');
    };

    if (loading) return <div className="flashcard-app"><div className="loading">Loading flashcards...</div></div>;
    if (error) return <div className="flashcard-app"><div className="error">{error}</div></div>;
    if (flashcards.length === 0) return <div className="flashcard-app"><div className="no-flashcards">No flashcards available.</div></div>;

    return (
        <div className="flashcard-app">
            <div className="flashcard-container">
                <div className="top-bar">
                    <div className="top-bar-inner">
                        <button className="back-icon" onClick={navigateBack}>&lt;</button>
                    </div>
                </div>

                <div className="flashcard">
                    <div className="progress-bar-container">
                        <div className="progress-bar">
                            {flashcards.map((_, i) => (
                                <div
                                    key={i}
                                    className="progress-segment"
                                    style={{
                                        backgroundColor: ratingColors[ratings[i]],
                                        opacity: i === index ? 1 : 0.8
                                    }}
                                />
                            ))}
                        </div>
                    </div>

                    <div className="flashcard-question">{current.question}</div>

                    {current.options ? (
                        <div className="flashcard-options-container">
                            <div className="instruction-text">Select 1 correct answer</div>

                            <div className="flashcard-options">
                                {current.options.map((option, i) => {
                                    const isCorrect = showAnswer && option === current.correctAnswer;
                                    const isWrong = showAnswer && option !== current.correctAnswer;

                                    return (
                                        <div
                                            key={i}
                                            onClick={() => handleOptionSelect(option)}
                                            className={`option-card ${isCorrect ? 'correct' : ''} ${isWrong && showAnswer ? 'incorrect' : ''} ${selectedOption === option && !showAnswer ? 'selected' : ''}`}
                                        >
                                            {option}
                                        </div>
                                    );
                                })}
                            </div>
                        </div>
                    ) : (
                        <>
                            <div className="flashcard-content">
                                {showAnswer && <hr className={`answer-divider ${isMobile ? 'mobile-divider' : ''}`} />}

                                {showAnswer ? (
                                    <div className={`flashcard-answer centered ${isMobile ? 'mobile-answer' : ''}`}>{current.answer}</div>
                                ) : (
                                    <div className="answer-button-container">
                                        <button
                                            className="show-answer-btn"
                                            onClick={() => setShowAnswer(true)}
                                        >
                                            Show Answer
                                        </button>
                                    </div>
                                )}
                            </div>

                            <div className="keyboard-icon-container" onClick={toggleKeyboardInput}>
                                <span className="keyboard-icon">⌨️</span>
                            </div>

                            {showKeyboardInput && (
                                <div className="keyboard-input-container">
                                    <input
                                        type="text"
                                        className="keyboard-input"
                                        value={inputText}
                                        onChange={handleInputChange}
                                        placeholder="Type the answer"
                                        autoFocus
                                        style={{ marginLeft: isMobile ? '40px' : '60px', width: isMobile ? 'calc(100% - 50px)' : 'calc(100% - 75px)' }}
                                    />
                                    <button className="submit-answer-btn" onClick={handleSubmitInput}>
                                        Submit
                                    </button>
                                </div>
                            )}

                            {score !== null && (
                                <div className="feedback-message">
                                    Your answer is {score}% correct
                                </div>
                            )}
                        </>
                    )}

                    {showAnswer && feedbackMessage && (
                        <div className={`feedback-message ${feedbackMessage === "Correct!" ? "correct-feedback" : "incorrect-feedback"}`}>
                            {feedbackMessage}
                        </div>
                    )}
                </div>

                <div className="rating-container">
                    <button className="nav-btn-circle" onClick={prevCard}>&lt;</button>
                    <button className="rating-btn" onClick={() => handleFeedback('bad', 2)}>😡</button>
                    <button className="rating-btn" onClick={() => handleFeedback('neutral', 1)}>😐</button>
                    <button className="rating-btn" onClick={() => handleFeedback('good', 0)}>😊</button>
                    <button className="nav-btn-circle" onClick={nextCard}>&gt;</button>
                </div>
            </div>
        </div>
    );
};

export default Flashcards;
