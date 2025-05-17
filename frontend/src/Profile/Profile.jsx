import './Profile.css';
import { useNavigate, useLocation } from 'react-router-dom';

function Profile() {
    const navigate = useNavigate();
    const location = useLocation();

    const handleClick = (label) => {
        if (label === "Home") navigate('/dashboard');
        else if (label === "Library") navigate('/library');
        else if (label === "Explore") navigate('/explore');
        else if (label === "Profile") navigate('/profile');
    };

    return (
        <div className="profile-container">
            {/* Logo Quizzy */}
            <div className="profile-logo">
                <img src="/quizzy-logo-homepage.svg" alt="Quizzy Logo" style={{ width: '100%', height: '100%' }} />
            </div>

            {/* Logo FII */}
            <div className="profile-logo-fii">
                <img src="/logo-fac-homepage.svg" alt="FII Logo" style={{ width: '100%', height: '100%' }} />
            </div>

            {/* Sidebar buttons */}
            <button
                className={`profile-icon-wrapper profile-icon-home ${location.pathname === '/dashboard' ? 'active' : ''}`}
                onClick={() => handleClick("Home")}
            >
                <img src="/home-logo.svg" alt="Home" className="profile-icon-image" />
                <span className="profile-icon-text">Home</span>
            </button>

            <button
                className={`profile-icon-wrapper profile-icon-library ${location.pathname === '/library' ? 'active' : ''}`}
                onClick={() => handleClick("Library")}
            >
                <img src="/library-logo.svg" alt="Library" className="profile-icon-image" />
                <span className="profile-icon-text">Library</span>
            </button>

            <button
                className={`profile-icon-wrapper profile-icon-explore ${location.pathname === '/explore' ? 'active' : ''}`}
                onClick={() => handleClick("Explore")}
            >
                <img src="/explore-logo.svg" alt="Explore" className="profile-icon-image" />
                <span className="profile-icon-text">Explore</span>
            </button>

            <button
                className={`profile-icon-wrapper profile-icon-profile ${location.pathname === '/profile' ? 'active' : ''}`}
                onClick={() => handleClick("Profile")}
            >
                <img src="/profile-logo.svg" alt="Profile" className="profile-icon-image" />
                <span className="profile-icon-text">Profile</span>
            </button>

            {/* Form */}
            <div className="profile-white-box">
                <form className="profile-form">
                    <div className="form-group">
                        <label>First name</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>Last name</label>
                        <input type="text" />
                    </div><br/>
                    <div className="form-group">
                        <label>Email</label>
                        <input type="email" />
                    </div>
                    <div className="form-group">
                        <label>Phone number</label>
                        <input type="text" />
                    </div>
                    <div className="form-group">
                        <label>Role</label>
                        <input type="text" />
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Profile;
