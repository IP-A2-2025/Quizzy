import './Profile.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { api } from '../utils/api.js';

function Profile() {
    const navigate = useNavigate();
    const location = useLocation();
    const [userData, setUserData] = useState({
        id: '',
        firstName: '',
        lastName: '',
        email: '',
        role: ''
    });
    const [passwordData, setPasswordData] = useState({
        newPassword: '',
        confirmPassword: ''
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [updateStatus, setUpdateStatus] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: ''
    });
    const [showPassword, setShowPassword] = useState(false);

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                setLoading(true);
                // Get email directly from localStorage
                const userEmail = localStorage.getItem('user');

                if (!userEmail) {
                    setError('User not authenticated');
                    setLoading(false);
                    return;
                }

                // Fetch user profile data by email
                const response = await api.get(`/users/profile?email=${encodeURIComponent(userEmail)}`);
                setUserData({
                    id: response.data.id || '',
                    firstName: response.data.firstName || '',
                    lastName: response.data.lastName || '',
                    email: response.data.email || '',
                    role: response.data.role || ''
                });
                setLoading(false);
            } catch (err) {
                console.error('Error fetching user profile:', err);
                setError('Failed to load profile data');
                setLoading(false);
            }
        };

        fetchUserProfile();
    }, []);

    const handleInputChange = (field, value) => {
        setUserData(prev => ({
            ...prev,
            [field]: value
        }));
    };

    const handlePasswordChange = (field, value) => {
        setPasswordData(prev => ({
            ...prev,
            [field]: value
        }));
    };

    const handleUpdate = async (field) => {
        try {
            setUpdateStatus(prev => ({
                ...prev,
                [field]: 'updating...'
            }));

            const endpoint = `/users/update/${field.toLowerCase()}`;
            const response = await api.post(endpoint, {
                id: userData.id,
                value: userData[field]
            });

            // Check if response is successful
            if (response.status === 200) {
                // If email was updated successfully, update localStorage
                if (field === 'email') {
                    localStorage.setItem('user', userData.email);
                    console.log('Updated email in localStorage to:', userData.email);
                }

                setUpdateStatus(prev => ({
                    ...prev,
                    [field]: 'Updated successfully'
                }));

                // Clear success message after 3 seconds
                setTimeout(() => {
                    setUpdateStatus(prev => ({
                        ...prev,
                        [field]: ''
                    }));
                }, 3000);
            }
        } catch (error) {
            console.error(`Error updating ${field}:`, error);
            setUpdateStatus(prev => ({
                ...prev,
                [field]: error.response?.data || 'Update failed'
            }));
        }
    };

    const handleUpdatePassword = async () => {
        try {
            // Validate passwords
            if (passwordData.newPassword.length < 6) {
                setUpdateStatus(prev => ({
                    ...prev,
                    password: 'Password must be at least 6 characters long'
                }));
                return;
            }

            if (passwordData.newPassword !== passwordData.confirmPassword) {
                setUpdateStatus(prev => ({
                    ...prev,
                    password: 'Passwords do not match'
                }));
                return;
            }

            setUpdateStatus(prev => ({
                ...prev,
                password: 'updating...'
            }));

            const endpoint = '/users/update/password';
            const response = await api.post(endpoint, {
                id: userData.id,
                value: passwordData.newPassword
            });

            // Check if response is successful
            if (response.status === 200) {
                setUpdateStatus(prev => ({
                    ...prev,
                    password: 'Password updated successfully'
                }));

                // Clear password fields
                setPasswordData({
                    newPassword: '',
                    confirmPassword: ''
                });

                // Clear success message after 3 seconds
                setTimeout(() => {
                    setUpdateStatus(prev => ({
                        ...prev,
                        password: ''
                    }));
                }, 3000);
            }
        } catch (error) {
            console.error('Error updating password:', error);
            setUpdateStatus(prev => ({
                ...prev,
                password: error.response?.data || 'Password update failed'
            }));
        }
    };

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
                {loading ? (
                    <div className="loading">Loading profile data...</div>
                ) : error ? (
                    <div className="error">{error}</div>
                ) : (
                    <form className="profile-form">
                        <div className="form-group">
                            <label>First name</label>
                            <div className="input-with-button">
                                <input
                                    type="text"
                                    value={userData.firstName}
                                    onChange={(e) => handleInputChange('firstName', e.target.value)}
                                />
                                <button
                                    type="button"
                                    onClick={() => handleUpdate('firstName')}
                                    className="update-button"
                                >
                                    Update
                                </button>
                                {updateStatus.firstName && (
                                    <span className="update-status">{updateStatus.firstName}</span>
                                )}
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Last name</label>
                            <div className="input-with-button">
                                <input
                                    type="text"
                                    value={userData.lastName}
                                    onChange={(e) => handleInputChange('lastName', e.target.value)}
                                />
                                <button
                                    type="button"
                                    onClick={() => handleUpdate('lastName')}
                                    className="update-button"
                                >
                                    Update
                                </button>
                                {updateStatus.lastName && (
                                    <span className="update-status">{updateStatus.lastName}</span>
                                )}
                            </div>
                        </div><br/>
                        <div className="form-group">
                            <label>Email</label>
                            <div className="input-with-button">
                                <input
                                    type="email"
                                    value={userData.email}
                                    onChange={(e) => handleInputChange('email', e.target.value)}
                                />
                                <button
                                    type="button"
                                    onClick={() => handleUpdate('email')}
                                    className="update-button"
                                >
                                    Update
                                </button>
                                {updateStatus.email && (
                                    <span className="update-status">{updateStatus.email}</span>
                                )}
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Role</label>
                            <input
                                type="text"
                                value={userData.role}
                                readOnly
                            />
                            <span className="role-note">(Role cannot be updated)</span>
                        </div>

                        <h3 className="password-section-title">Change Password</h3>
                        <div className="form-group">
                            <label>New Password</label>
                            <div className="password-wrapper">
                                <input
                                    type={showPassword ? 'text' : 'password'}
                                    value={passwordData.newPassword}
                                    onChange={(e) => handlePasswordChange('newPassword', e.target.value)}
                                    placeholder="Enter new password"
                                />
                                <button
                                    type="button"
                                    className="toggle-password"
                                    onClick={() => setShowPassword(!showPassword)}
                                    aria-label="Toggle password visibility"
                                >
                                    {showPassword ? 'Hide' : 'Show'}
                                </button>
                            </div>
                        </div>
                        <div className="form-group">
                            <label>Confirm Password</label>
                            <div className="password-wrapper">
                                <input
                                    type="password"
                                    value={passwordData.confirmPassword}
                                    onChange={(e) => handlePasswordChange('confirmPassword', e.target.value)}
                                    placeholder="Confirm new password"
                                />
                            </div>
                            <div className="button-container">
                                <button
                                    type="button"
                                    onClick={handleUpdatePassword}
                                    className="update-button"
                                >
                                    Update Password
                                </button>
                                {updateStatus.password && (
                                    <span className="update-status">{updateStatus.password}</span>
                                )}
                            </div>
                        </div>
                    </form>
                )}
            </div>
        </div>
    );
}

export default Profile;
