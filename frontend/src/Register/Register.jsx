import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../utils/api';
import './Register.css';

function Register() {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: 'student' // Default role
    });
    const [showPassword, setShowPassword] = useState(false);
    const [mesaj, setMesaj] = useState('');
    const [limba, setLimba] = useState(() => localStorage.getItem('limba') || 'ro');
    const navigate = useNavigate();

    const texte = {
        ro: {
            titlu: 'Creează cont nou',
            firstName: 'Prenume',
            lastName: 'Nume',
            email: 'Email',
            password: 'Parolă',
            confirmPassword: 'Confirmă parola',
            butonRegister: 'Înregistrare',
            butonLogin: 'Am deja cont',
            succes: 'Cont creat cu succes! 🎓',
            eroareParola: 'Parolele nu coincid.',
            eroareCampuri: 'Te rugăm să completezi toate câmpurile.',
            eroareEmail: 'Adresa de email este deja folosită.',
            eroareParolaScurta: 'Parola trebuie să aibă minim 6 caractere.',
        },
        en: {
            titlu: 'Create new account',
            firstName: 'First Name',
            lastName: 'Last Name',
            email: 'Email',
            password: 'Password',
            confirmPassword: 'Confirm Password',
            butonRegister: 'Register',
            butonLogin: 'I already have an account',
            succes: 'Account created successfully! 🎓',
            eroareParola: 'Passwords do not match.',
            eroareCampuri: 'Please fill in all fields.',
            eroareEmail: 'Email address is already in use.',
            eroareParolaScurta: 'Password must be at least 6 characters long.',
        }
    };

    const t = texte[limba];

    const schimbaLimba = (nouaLimba) => {
        setLimba(nouaLimba);
        localStorage.setItem('limba', nouaLimba);
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        // Validări
        if (!formData.firstName || !formData.lastName || !formData.email || !formData.password || !formData.confirmPassword) {
            setMesaj(t.eroareCampuri);
            return;
        }

        if (formData.password !== formData.confirmPassword) {
            setMesaj(t.eroareParola);
            return;
        }

        if (formData.password.length < 6) {
            setMesaj(t.eroareParolaScurta);
            return;
        }

        try {
            console.log('Attempting registration for:', formData.email);
            const response = await api.post("/users/register", {
                firstName: formData.firstName,
                lastName: formData.lastName,
                email: formData.email,
                password: formData.password,
                role: formData.role
            });

            if (!response) {
                console.error('No response received from server');
                setMesaj("Eroare de conexiune cu serverul.");
                return;
            }

            const data = response.data;
            console.log('Registration response:', { status: response.status, success: data.success });

            if (data.success) {
                console.log('Registration successful');
                setMesaj(t.succes);
                setTimeout(() => {
                    console.log('Redirecting to login');
                    navigate("/login");
                }, 2000);
            } else {
                console.warn('Registration failed:', data.message);
                if (data.message.includes("exists")) {
                    setMesaj(t.eroareEmail);
                } else {
                    setMesaj(data.message);
                }
            }
        } catch (error) {
            console.error("Eroare la înregistrare:", error);
            setMesaj("Eroare de conexiune cu serverul.");
        }
    };

    return (
        <div className="register-page">
            <div className="register-header">
                <img src="/logo-facultate.svg" alt="FII Logo" className="logo-facultate" />
                <img src="/quizzy-logo.svg" alt="Quizzy Logo" className="quizzy-logo" />
            </div>

            <div className="register-container">
                <h2>{t.titlu}</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        name="firstName"
                        placeholder={t.firstName}
                        value={formData.firstName}
                        onChange={handleInputChange}
                    />
                    <input
                        type="text"
                        name="lastName"
                        placeholder={t.lastName}
                        value={formData.lastName}
                        onChange={handleInputChange}
                    />
                    <input
                        type="email"
                        name="email"
                        placeholder={t.email}
                        value={formData.email}
                        onChange={handleInputChange}
                    />
                    <div className="password-wrapper">
                        <input
                            type={showPassword ? 'text' : 'password'}
                            name="password"
                            placeholder={t.password}
                            value={formData.password}
                            onChange={handleInputChange}
                        />
                        <button
                            type="button"
                            className="toggle-password"
                            onClick={() => setShowPassword(!showPassword)}
                            aria-label="Toggle password visibility"
                        >
                            {showPassword ? (
                                <svg width="24" height="18" viewBox="0 0 24 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M1.29485 9.81509C1.14269 9.56857 1.06659 9.44537 1.02399 9.25531C0.992002 9.11257 0.992002 8.88743 1.02399 8.74469C1.06659 8.55463 1.14269 8.43143 1.29485 8.18491C2.5524 6.14839 6.2956 1 12 1C17.7044 1 21.4476 6.14839 22.7052 8.18491C22.8574 8.43143 22.9335 8.55463 22.976 8.74469C23.008 8.88743 23.008 9.11257 22.976 9.25531C22.9335 9.44537 22.8574 9.56857 22.7052 9.81509C21.4476 11.8517 17.7044 17 12 17C6.2956 17 2.5524 11.8517 1.29485 9.81509Z" stroke="#173B61" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                    <path d="M12 12.4286C13.8514 12.4286 15.3522 10.8936 15.3522 9C15.3522 7.1064 13.8514 5.57143 12 5.57143C10.1486 5.57143 8.64776 7.1064 8.64776 9C8.64776 10.8936 10.1486 12.4286 12 12.4286Z" stroke="#173B61" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                </svg>
                            ) : (
                                <svg width="20" height="18" viewBox="0 0 20 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M8.85032 2.85984C9.22196 2.80643 9.60521 2.77778 9.99998 2.77778C14.6672 2.77778 17.7299 6.78208 18.7587 8.36604C18.8833 8.55778 18.9455 8.6536 18.9804 8.80151C19.0066 8.91253 19.0065 9.08773 18.9804 9.19876C18.9454 9.34658 18.8828 9.44311 18.7574 9.63609C18.4832 10.0579 18.0653 10.6508 17.5115 11.2938M5.17634 4.30226C3.19968 5.60596 1.85774 7.41724 1.24215 8.36471C1.11705 8.55724 1.05451 8.65351 1.01964 8.80133C0.993461 8.91236 0.993452 9.08747 1.01962 9.19858C1.05447 9.3464 1.11673 9.44222 1.24125 9.63396C2.27015 11.218 5.33276 15.2222 9.99998 15.2222C11.8819 15.2222 13.5029 14.5712 14.8349 13.6903M1.77178 1L18.2282 17M8.06059 7.11438C7.56425 7.59698 7.25726 8.26364 7.25726 9C7.25726 10.4728 8.48526 11.6667 9.99998 11.6667C10.7573 11.6667 11.443 11.3682 11.9394 10.8856" stroke="#173B61" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                </svg>
                            )}
                        </button>
                    </div>
                    <input
                        type="password"
                        name="confirmPassword"
                        placeholder={t.confirmPassword}
                        value={formData.confirmPassword}
                        onChange={handleInputChange}
                    />

                    <div className="register-buttons">
                        <button
                            type="button"
                            className="login-button"
                            onClick={() => navigate('/login')}
                        >
                            {t.butonLogin}
                        </button>
                        <button type="submit" className="register-button">
                            {t.butonRegister}
                        </button>
                    </div>
                </form>

                {mesaj && (
                    <p className={`mesaj ${mesaj === t.succes ? 'mesaj-succes' : 'mesaj-eroare'}`}>
                        {mesaj}
                    </p>
                )}
            </div>

            <div className="language-switch">
                <span
                    onClick={() => schimbaLimba('en')}
                    className={limba === 'en' ? 'active-language' : ''}
                >
                    <img src="/flags-of-england_2.png" alt="EN" className="flag-icon" /> English
                </span>
                <span className="separator"> | </span>
                <span
                    onClick={() => schimbaLimba('ro')}
                    className={limba === 'ro' ? 'active-language' : ''}
                >
                    <img src="/romanian-flag-icon.jpg" alt="RO" className="flag-icon" /> Română
                </span>
            </div>
        </div>
    );
}

export default Register;
