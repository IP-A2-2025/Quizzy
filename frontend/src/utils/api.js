import axios from 'axios';

const envUrl = import.meta.env.VITE_BACKEND_URL;
const BASE_URL =
    typeof envUrl === 'string' && envUrl.trim() !== ''
        ? envUrl + '/api'
        : 'http://localhost:3000';

const axiosInstance = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
});

// Request interceptor to add JWT token
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
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

// Create a separate instance for binary file downloads
const binaryAxiosInstance = axios.create({
    baseURL: BASE_URL,
    responseType: 'blob',
});

// Copy the auth interceptor to the binary instance
binaryAxiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        console.log(`Making binary ${config.method.toUpperCase()} request to: ${config.baseURL}${config.url}`);
        return config;
    },
    (error) => Promise.reject(error)
);

// Export helper functions for API calls
export const api = {
    get: (url) => axiosInstance.get(url),
    post: (url, data) => axiosInstance.post(url, data),
    put: (url, data) => axiosInstance.put(url, data),
    delete: (url) => axiosInstance.delete(url),
    getBinaryFile: (url, headers = {}) => binaryAxiosInstance.get(url, { headers })
};