import { decodeToken, isTokenExpired } from '../utils/auth';


const KEYCLOAK_TOKEN_URL = 'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/token';
const API_BASE_URL = 'http://localhost:8080/api';



const api = {
  // Funzione per il login
  login: async (username, password) => {
    const body = new URLSearchParams({
      client_id: 'front-end-client', // Cambia con il tuo client ID
      grant_type: 'password',
      username: username,
      password: password,
      scope: 'access', // Aggiunto parametro scope
    });

    const response = await fetch(KEYCLOAK_TOKEN_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: body.toString(),
    });

    if (!response.ok) {
      console.error('Errore durante il login:', response.status, response.statusText);
      throw new Error('Errore durante il login. Controlla le credenziali.');
    }

    const data = await response.json();
    localStorage.setItem('access_token', data.access_token);
    localStorage.setItem('refresh_token', data.refresh_token);

    // Decodifica e stampa i dati del token per debug
    console.log('Token decodificato:', decodeToken(data.access_token));
    return data;
  },

  // Funzione generica per richieste GET
  get: async (endpoint) => {
    const token = localStorage.getItem('access_token');
    if (!token) {
      throw new Error('Token non disponibile. Effettua il login.');
    }

    if (isTokenExpired(token)) {
      throw new Error('Il token è scaduto. Effettua nuovamente il login.');
    }

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error('Errore nella richiesta.');
    }

    return response.json();
  },
};

export default api;





// const KEYCLOAK_TOKEN_URL = 'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/token';
// const API_BASE_URL = 'http://localhost:8080/api';

// const api = {
//   // Funzione per il login
//   login: async (username, password) => {
//     const body = new URLSearchParams({
//       client_id: 'front-end-client', // Cambia con il tuo client ID
//       grant_type: 'password',
//       username: username,
//       password: password,  
//     });

//     const response = await fetch(KEYCLOAK_TOKEN_URL, {
//       method: 'POST',
//       headers: {
//         'Content-Type': 'application/x-www-form-urlencoded',
//       },
//       body: body.toString(),
//     });

//     if (!response.ok) {
//       throw new Error('Errore durante il login. Controlla le credenziali.');
//     }

//     const data = await response.json();
//     localStorage.setItem('access_token', data.access_token);
//     localStorage.setItem('refresh_token', data.refresh_token);
//     return data;
//   },

//   // Funzione generica per GET
//   get: async (endpoint) => {
//     const token = localStorage.getItem('access_token');
//     if (!token) {
//       throw new Error('Token non disponibile. Effettua il login.');
//     }

//     const response = await fetch(`${API_BASE_URL}${endpoint}`, {
//       method: 'GET',
//       headers: {
//         'Authorization': `Bearer ${token}`,
//       },
//     });

//     if (!response.ok) {
//       throw new Error('Errore nella richiesta.');
//     }

//     return response.json();
//   },
// };

// export default api;
