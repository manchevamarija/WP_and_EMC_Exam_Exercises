import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BASE_API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

const PUBLIC_ENDPOINTS = ['/user/login', '/user/register'];

const isPublic = (url: string | undefined): boolean => {
  if (!url) {
    return false;
  }
  return PUBLIC_ENDPOINTS.some((endpoint) => url.includes(endpoint));
};

axiosInstance.interceptors.request.use(
  (config) => {
    const jwtToken = localStorage.getItem('token');
    if (jwtToken && !isPublic(config.url)) {
      config.headers.Authorization = `Bearer ${jwtToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if ((error.response?.status === 401 || error.response?.status === 403) && !isPublic(error.config?.url)) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;