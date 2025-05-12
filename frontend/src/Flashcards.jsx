import React, { useState } from 'react';
import './Flashcards.css';

const Flashcards = () => {
    const flashcards = [
        {
            question: "What is a minimum spanning tree?",
            options: [
                "A) A flying donkey",
                "B) 24",
                "C) A spanning tree with the minimum possible total edge weight.",
                "D) Belgium"
            ],
            correctAnswer: "C) A spanning tree with the minimum possible total edge weight."
        },
        {
            question: "What is Big O notation?",
            answer: "Big O describes the upper bound of an algorithm's time or space complexity as input size grows."
        },
        {
            question: "What is recursion in programming?",
            answer: "Recursion is when a function calls itself to solve smaller instances of the same problem."
        },
        {
            question: "What is the difference between stack and queue?",
            answer: "Stack is LIFO (last in, first out); Queue is FIFO (first in, first out)."
        },
        {
            question: "What is a hash table?",
            answer: "A hash table maps keys to values using a hash function to compute an index into an array."
        }
    ];

    const [index, setIndex] = useState(0);
    const [showAnswer, setShowAnswer] = useState(false);
    const [selectedOption, setSelectedOption] = useState(null);

    const current = flashcards[index];

    const nextCard = () => {
        if (index < flashcards.length - 1) {
            setIndex(index + 1);
            setShowAnswer(false);
            setSelectedOption(null);
        }
    };

    const prevCard = () => {
        if (index > 0) {
            setIndex(index - 1);
            setShowAnswer(false);
            setSelectedOption(null);
        }
    };

    const handleFeedback = (type) => {
        const messages = {
            bad: 'You disliked this card.',
            neutral: 'You felt neutral.',
            good: 'You liked this card.'
        };
        alert(messages[type]);
    };

    return (
        <div className="flashcard-container">
            <div className="top-bar">
                <div className="top-bar-inner">
                    <button className="back-icon">&lt;</button>
                </div>
            </div>

            <div className="flashcard">
                {/* Question always at the top */}
                <div className="flashcard-question">{current.question}</div>

                {/* Answer options or text */}
                {current.options ? (
                    <div className="flashcard-answer">
                        {current.options.map((option, i) => {
                            const isCorrect = showAnswer && option === current.correctAnswer;
                            const isWrongSelection = showAnswer && option === selectedOption && option !== current.correctAnswer;

                            return (
                                <div
                                    key={i}
                                    onClick={() => !showAnswer && setSelectedOption(option)}
                                    style={{
                                        padding: '0.7rem 1.2rem',
                                        margin: '0.4rem 0',
                                        borderRadius: '12px',
                                        cursor: showAnswer ? 'default' : 'pointer',
                                        backgroundColor: isCorrect
                                            ? '#2D852D'
                                            : isWrongSelection
                                                ? '#B92C2C'
                                                : '#EFEAE6',
                                        color: '#173B61',
                                        border: '2px solid #173B61',
                                        fontWeight: isCorrect || isWrongSelection ? 'bold' : 'normal'
                                    }}
                                >
                                    {option}
                                </div>
                            );
                        })}
                    </div>
                ) : (
                    showAnswer && <div className="flashcard-answer">{current.answer}</div>
                )}

                {/* Show/Hide Button at the bottom */}
                <div style={{ marginTop: 'auto' }}>
                    <button
                        className="show-answer-btn"
                        onClick={() => setShowAnswer(!showAnswer)}
                    >
                        {showAnswer ? 'Hide Answer' : 'Show Answer'}
                    </button>
                </div>
            </div>

            <div className="rating-container">
                <button className="nav-btn-circle" onClick={prevCard}>&lt;</button>
                <button className="rating-btn" onClick={() => handleFeedback('bad')}>😡</button>
                <button className="rating-btn" onClick={() => handleFeedback('neutral')}>😐</button>
                <button className="rating-btn" onClick={() => handleFeedback('good')}>😊</button>
                <button className="nav-btn-circle" onClick={nextCard}>&gt;</button>
            </div>
        </div>
    );
};

export default Flashcards;
