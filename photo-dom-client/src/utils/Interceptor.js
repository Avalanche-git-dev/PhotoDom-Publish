// import  { useAuth }  from './AuthContext';
// import { handleError } from './ErrorHandler';

// export const fetchWithAuth = async (url, options = {}) => {
//   const { tokens, refreshAccessToken,isTokenExpired } = useAuth();

//   try {
//     // Controlla e aggiorna il token se necessario
//     if (tokens?.accessToken && isTokenExpired(tokens.accessToken)) {
//       await refreshAccessToken();
//     }

//     // Aggiungi l'intestazione Authorization
//     const headers = {
//       ...options.headers,
//       Authorization: `Bearer ${tokens.accessToken}`,
//     };

//     const response = await fetch(url, { ...options, headers });

//     if (!response.ok) {
//       throw new Error(`Errore ${response.status}: ${response.statusText}`);
//     }

//     return response.json();
//   } catch (error) {
//     handleError(error); // Gestisce gli errori con il tuo handler globale
//     throw error; // Continua a propagare l'errore
//   }
// };


import { handleError } from './ErrorHandler';

export const fetchWithAuth = async (url, options = {}, tokens, refreshAccessToken, isTokenExpired) => {
  try {
    // Controlla e aggiorna il token se necessario
    if (tokens?.accessToken && isTokenExpired(tokens.accessToken)) {
      await refreshAccessToken();
    }

    // Aggiungi l'intestazione Authorization
    const headers = {
      ...options.headers,
      Authorization: `Bearer ${tokens.accessToken}`,
    };

    const response = await fetch(url, { ...options, headers });

    if (!response.ok) {
      throw new Error(`Errore ${response.status}: ${response.statusText}`);
    }

    return response.json();
  } catch (error) {
    handleError(error); // Gestisce gli errori con il tuo handler globale
    throw error; // Continua a propagare l'errore
  }
};
