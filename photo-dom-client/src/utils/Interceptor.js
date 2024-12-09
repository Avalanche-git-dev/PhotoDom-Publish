import  { useAuth }  from './utils/AuthContext';
import { handleError } from './utils/ErrorHandler';

export const fetchWithAuth = async (url, options = {}) => {
  const { tokens, refreshAccessToken } = useAuth();

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



// import { useAuth } from './authcontext';

// export const fetchWithAuth = async (url, options = {}) => {
//   const { tokens, refreshAccessToken, clearTokens } = useAuth();

//   // Verifica se il token è scaduto
//   if (tokens?.accessToken && isTokenExpired(tokens.accessToken)) {
//     try {
//       await refreshAccessToken();
//     } catch (error) {
//       console.error('Sessione scaduta. Effettua nuovamente il login.');
//       clearTokens();
//       throw error;
//     }
//   }

//   // Aggiungi il token aggiornato alle intestazioni
//   const headers = {
//     ...options.headers,
//     Authorization: `Bearer ${tokens?.accessToken}`,
//   };

//   const response = await fetch(url, { ...options, headers });

//   if (!response.ok) {
//     console.error('Errore nella richiesta:', response.status, response.statusText);
//     throw new Error(`Errore: ${response.statusText}`);
//   }

//   return response.json();
// };
