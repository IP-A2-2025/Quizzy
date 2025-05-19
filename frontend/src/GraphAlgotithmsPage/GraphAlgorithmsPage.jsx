import { FaFilePdf } from 'react-icons/fa';
import './GraphAlgorithmsPage.css';
import { useNavigate } from 'react-router-dom';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

function GraphAlgorithmPage() {
    const navigate = useNavigate();

    const handleClick = (label) => {
        if (label === "Home") navigate('/dashboard');
        else if (label === "Library") navigate('/library');
        else if (label === "Explore") navigate('/explore');
        else if (label === "Profile") navigate('/profile');
    };

    const sliderSettings = {
        dots: true,
        infinite: true,
        speed: 500,
        slidesToShow: 3,
        slidesToScroll: 1,
        centerMode: true,
        centerPadding: '40px',
        responsive: [
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 2,
                    centerPadding: '30px'
                }
            },
            {
                breakpoint: 600,
                settings: {
                    slidesToShow: 1,
                    centerPadding: '20px'
                }
            }
        ]
    };

    return (
        <div className="graph-container">
            <div className="graph-logo">
                <img src="/quizzy-logo-homepage.svg" alt="Logo" />
            </div>

            <div className="graph-box" />

            {/* Sidebar buttons */}
            <button className="graph-icon-wrapper graph-icon-home" onClick={() => handleClick("Home")}>
                <img src="/home-logo.svg" alt="Home" className="graph-icon-image" />
                <span className="graph-icon-text">Home</span>
            </button>

            <button className="graph-icon-wrapper graph-icon-library graph-icon-active">
                <div className="graph-rectangle-active"></div>
                <img src="/library-logo.svg" alt="Library" className="graph-icon-image" />
                <div className="graph-icon-text">Library</div>
            </button>

            <button className="graph-icon-wrapper graph-icon-explore" onClick={() => handleClick("Explore")}>
                <img src="/explore-logo.svg" alt="Explore" className="graph-icon-image" />
                <span className="graph-icon-text">Explore</span>
            </button>

            <button className="graph-icon-wrapper graph-icon-profile" onClick={() => handleClick("Profile")}>
                <img src="/profile-logo.svg" alt="Profile" className="graph-icon-image" />
                <span className="graph-icon-text">Profile</span>
            </button>

            <div className="graph-logo-fii">
                <img src="/logo-fac-homepage.svg" alt="FII Logo" />
            </div>

            <div className="graph-content-box">
                <div className="graph-header">
                    <h1 className="graph-title">Graph Algorithms</h1>
                    <button className="graph-unenroll-button" onClick={() => alert('Unenrolled!')}>
                        Unenroll ►
                    </button>
                    <button className="graph-enrolled-button" onClick={() => alert('Enrolled!')}>
                        Enrolled
                    </button>
                    <button className="graph-start-button" onClick={() => alert('Start learning!')}>
                        Start learning ►
                    </button>
                </div>

                <div className="graph-divider"></div>

                <div className="graph-flashcards">
                    <h2 className="graph-section-title">Flashcards</h2>
                    <Slider {...sliderSettings}>
                        <div className="graph-flashcard"><h3>What is a minimum spanning tree?</h3></div>
                        <div className="graph-flashcard"><h3>Define a complete bipartite graph.</h3></div>
                        <div className="graph-flashcard"><h3>What is an adjacency matrix?</h3></div>
                        <div className="graph-flashcard"><h3>Difference between Dijkstra and Bellman-Ford?</h3></div>
                    </Slider>
                </div>

                <div className="graph-divider"></div>

                <div className="graph-files">
                    <div className="graph-file-header">
                        <h2 className="graph-section-title">Files</h2>
                        <h2 className="graph-file-count">6</h2>
                    </div>

                    {[1, 2, 3].map((i) => (
                        <div key={i}>
                            <div className="graph-file-entry">
                                <FaFilePdf size={40} color="#E74C3C" />
                                <div className="graph-file-text">
                                    <p className="graph-file-name">{`Agr${i}`}</p>
                                    <p className="graph-file-details">{`${30 + i * 2} pages | ${3 + i} flashcards`}</p>
                                </div>
                            </div>
                            {i < 3 && <div className="graph-divider-small"></div>}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default GraphAlgorithmPage;