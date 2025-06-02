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
        role: 'student',
        professorSecret: ''
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
            succes: 'Cont creat cu succes!',
            eroareParola: 'Parolele nu coincid.',
            eroareCampuri: 'Te rugăm să completezi toate câmpurile.',
            eroareEmail: 'Adresa de email este deja folosită.',
            eroareParolaScurta: 'Parola trebuie să aibă minim 6 caractere.',
            professorSecret: 'Cod Secret Profesor'
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
            succes: 'Account created successfully!',
            eroareParola: 'Passwords do not match.',
            eroareCampuri: 'Please fill in all fields.',
            eroareEmail: 'Email address is already in use.',
            eroareParolaScurta: 'Password must be at least 6 characters long.',
            professorSecret: 'Professor Secret Code'
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
            const payload = {
                firstName: formData.firstName,
                lastName: formData.lastName,
                email: formData.email,
                password: formData.password,
                role: formData.role
            };

            if (formData.role === 'profesor') {
                payload.professorSecret = formData.professorSecret;
            }

            const response = await api.post("/users/register", payload);

            if (!response) {
                setMesaj("Eroare de conexiune cu serverul.");
                return;
            }

            const data = response.data;

            if (data.success) {
                setMesaj(t.succes);
                setTimeout(() => {
                    navigate("/login");
                }, 2000);
            } else {
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
                                <svg width="24" height="18" viewBox="0 0 24 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M1.29485 9.81509C1.14269 9.56857 1.06659 9.44537 1.02399 9.25531C0.992002 9.11257 0.992002 8.88743 1.02399 8.74469C1.06659 8.55463 1.14269 8.43143 1.29485 8.18491C2.5524 6.14839 6.2956 1 12 1C17.7044 1 21.4476 6.14839 22.7052 8.18491C22.8574 8.43143 22.9335 8.55463 22.976 8.74469C23.008 8.88743 23.008 9.11257 22.976 9.25531C22.9335 9.44537 22.8574 9.56857 22.7052 9.81509C21.4476 11.8517 17.7044 17 12 17C6.2956 17 2.5524 11.8517 1.29485 9.81509Z" stroke="#173B61" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                                    <path d="M12 12.4286C13.8514 12.4286 15.3522 10.8936 15.3522 9C15.3522 7.1064 13.8514 5.57143 12 5.57143C10.1486 5.57143 8.64776 7.1064 8.64776 9C8.64776 10.8936 10.1486 12.4286 12 12.4286Z" stroke="#173B61" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
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

                    <select
                        name="role"
                        value={formData.role}
                        onChange={handleInputChange}
                        className="dropdown-role"
                    >
                        <option value="student">Student</option>
                        <option value="profesor">Professor</option>
                    </select>

                    {formData.role === 'profesor' && (
                        <input
                            type="text"
                            name="professorSecret"
                            placeholder={t.professorSecret}
                            value={formData.professorSecret}
                            onChange={handleInputChange}
                        />
                    )}

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
