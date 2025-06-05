import './Profile.css';
import { useNavigate, useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { api } from '../utils/api.js';
import BurgerMenu from '../components/BurgerMenu/BurgerMenu.jsx';
import { SidebarLeft, SidebarRight } from '../components/Sidebar';

function Profile() {
    const navigate = useNavigate();
    const location = useLocation();
    const [profileData, setProfileData] = useState({
        nume: '',
        prenume: '',
        email: '',
        facultate: '',
        an_studiu: '',
        grupa: ''
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                const userId = localStorage.getItem('userId');
                if (!userId) {
                    throw new Error('User ID not found');
                }

                const response = await api.get(`/users/${userId}`);
                setProfileData(response.data);
            } catch (err) {
                setError(err.message || 'Failed to load profile data');
            } finally {
                setLoading(false);
            }
        };

        fetchProfileData();
    }, []);

    return (
        <div className="profile-container">
            {/* Burger Menu Component for mobile/tablet */}
            <BurgerMenu currentPage="Profile" />

            {/* Desktop Sidebars */}
            <SidebarLeft activeRoute="/profile" />
            <SidebarRight />

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
                            <input
                                type="text"
                                name="prenume"
                                value={profileData.prenume}
                                readOnly
                            />
                        </div>
                        <div className="form-group">
                            <label>Last name</label>
                            <input
                                type="text"
                                name="nume"
                                value={profileData.nume}
                                readOnly
                            />
                        </div>
                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                name="email"
                                value={profileData.email}
                                readOnly
                            />
                        </div>
                        <div className="form-group">
                            <label>Faculty</label>
                            <input
                                type="text"
                                name="facultate"
                                value={profileData.facultate}
                                readOnly
                            />
                        </div>
                        <div className="form-group">
                            <label>Study year</label>
                            <input
                                type="text"
                                name="an_studiu"
                                value={profileData.an_studiu}
                                readOnly
                            />
                        </div>
                        <div className="form-group">
                            <label>Group</label>
                            <input
                                type="text"
                                name="grupa"
                                value={profileData.grupa}
                                readOnly
                            />
                        </div>
                    </form>
                )}
            </div>
        </div>
    );
}

export default Profile;
