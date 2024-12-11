// import { fetchWithAuth } from './Interceptor';

// const API_BASE_URL = 'http://localhost:8080/api'; // Base URL del tuo backend

// const apiMiddleware = {
//   get: async (endpoint) =>
//     fetchWithAuth(`${API_BASE_URL}${endpoint}`, { method: 'GET' }),

//   post: async (endpoint, data) =>
//     fetchWithAuth(`${API_BASE_URL}${endpoint}`, {
//       method: 'POST',
//       headers: { 'Content-Type': 'application/json' },
//       body: JSON.stringify(data),
//     }),

//   put: async (endpoint, data) =>
//     fetchWithAuth(`${API_BASE_URL}${endpoint}`, {
//       method: 'PUT',
//       headers: { 'Content-Type': 'application/json' },
//       body: JSON.stringify(data),
//     }),

//   delete: async (endpoint) =>
//     fetchWithAuth(`${API_BASE_URL}${endpoint}`, { method: 'DELETE' }),
// };

// export default apiMiddleware;





import { useFetchWithAuth } from './UseFetchWithAuth';

const API_BASE_URL = 'http://localhost:8080/api'; // Base URL del tuo backend

export const ApiMiddleware = () => {
  const fetchWithAuth = useFetchWithAuth();

  return {
    get: async (endpoint) =>
      fetchWithAuth(`${API_BASE_URL}${endpoint}`, { method: 'GET' }),

    post: async (endpoint, data) =>
      fetchWithAuth(`${API_BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
        
      }),

    put: async (endpoint, data) =>
      fetchWithAuth(`${API_BASE_URL}${endpoint}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      }),

    delete: async (endpoint) =>
      fetchWithAuth(`${API_BASE_URL}${endpoint}`, { method: 'DELETE' }),
  };

  
};


