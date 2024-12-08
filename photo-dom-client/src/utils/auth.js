// import jwtDecode from 'jwt-decode';

// // Decodifica il token JWT
// export const decodeToken = (token) => {
//   return jwtDecode(token);
// };

// // Controlla se il token è scaduto
// export const isTokenExpired = (token) => {
//   const decoded = decodeToken(token);
//   const currentTime = Date.now() / 1000; // Tempo in secondi
//   return decoded.exp < currentTime;
// };

// // Logout: Rimuovi i token
// export const logout = () => {
//   localStorage.removeItem('access_token');
//   localStorage.removeItem('refresh_token');
//   window.location.href = '/login';
// };


import { jwtDecode } from 'jwt-decode';

export const decodeToken = (token) => {
  try {
    return jwtDecode(token);
  } catch (error) {
    console.error('Errore durante la decodifica del token:', error);
    return null;
  }
};

// Controlla se il token è scaduto
export const isTokenExpired = (token) => {
  const decoded = decodeToken(token);
  if (!decoded) return true;

  const currentTime = Date.now() / 1000; // Tempo in secondi
  return decoded.exp < currentTime;
};

// Logout: Rimuove i token e reindirizza alla pagina di login
export const logout = () => {
  localStorage.removeItem('access_token');
  localStorage.removeItem('refresh_token');
  window.location.href = '/login';
};