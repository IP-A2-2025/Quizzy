import axios from 'axios';

const BASE_URL = process.env.BACKEND_URL || 'http://localhost:3000';

const axiosInstance = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
});

// Request interceptor to add JWT token and log details
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        //DEBUG REMOVE LATER
        console.log(`Making ${config.method.toUpperCase()} request to: ${config.baseURL}${config.url}`);
        console.log('Headers:', config.headers);
        if (config.data) {
            console.log('Body:', config.data);
        }

        
        return config;
    },
    (error) => Promise.reject(error)
);

// Response interceptor for logging and handling 401
axiosInstance.interceptors.response.use(
    (response) => {
        console.log(`Response status: ${response.status}`);
        console.log('Response data:', response.data);
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            console.log('Unauthorized response, clearing auth data');
            localStorage.removeItem('authToken');
            localStorage.removeItem('user');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

// Export helper functions for API calls
export const api = {
    get: (url) => axiosInstance.get(url),
    post: (url, data) => axiosInstance.post(url, data),
    put: (url, data) => axiosInstance.put(url, data),
    delete: (url) => axiosInstance.delete(url),
};