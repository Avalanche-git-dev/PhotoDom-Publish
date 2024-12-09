// import React from "react";
// import { Navigate } from "react-router-dom";
// import { useAuth } from '../utils/AuthContext'; // Usa il tuo AuthProvider

// const ProtectedRoute = ({ children }) => {
//   const { isAuthenticated } = useAuth();

//   return isAuthenticated ? children : <Navigate to="/login" />;
// };

// export default ProtectedRoute;




import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../utils/AuthContext';

const ProtectedRoute = ({ children }) => {
  const { tokens } = useAuth();

  // Controlla se l'utente è autenticato
  const isAuthenticated = !!tokens.accessToken;

  if (!isAuthenticated) {
    // Reindirizza al login se non autenticato
    return <Navigate to="/login" replace />;
  }

  // Renderizza i figli se autenticato
  return children;
};

export default ProtectedRoute;
