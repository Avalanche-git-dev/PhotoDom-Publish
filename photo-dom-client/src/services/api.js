const KEYCLOAK_TOKEN_URL = 'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/token';
const KEYCLOAK_USERINFO_URL = 'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/userinfo';
const KEYCLOAK_LOGOUT_URL = 'http://localhost:8180/realms/PhotoDom/protocol/openid-connect/logout';



const api = {
  // Login dell'utente
  login: async (username, password) => {
    const body = new URLSearchParams({
      client_id: 'front-end-client',
      grant_type: 'password',
      username,
      password,
      scope: 'openid', 
    });

    const response = await fetch(KEYCLOAK_TOKEN_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: body.toString(),
    });

    if (!response.ok) {
      throw new Error('Errore durante il login. Controlla le credenziali.');
    }

    // const data = await response.json();
    return response.json();
  },

  register: async (formData) => {
    const response = await fetch('http://localhost:8080/api/users/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData),
    });
  
    if (!response.ok) {
      const errorResponse = await response.json();
      throw new Error(errorResponse.message || 'Errore durante la registrazione.');
    }
  
    return response.json();
  },
  

  // Refresh del token
  refreshToken: async (refreshToken) => {
    const body = new URLSearchParams({
      client_id: 'front-end-client',
      grant_type: 'refresh_token',
      refresh_token: refreshToken,
    });

    const response = await fetch(KEYCLOAK_TOKEN_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: body.toString(),
    });

    if (!response.ok) {
      throw new Error('Errore durante il refresh del token.');
    }

    return response.json();
  },

    // Recupera informazioni sull'utente
    getUserInfo: async (accessToken) => {
      const response = await fetch(KEYCLOAK_USERINFO_URL, {
        method: 'GET',
        headers: { Authorization: `Bearer ${accessToken}` },
      });
  
      if (!response.ok) {
        throw new Error('Errore nel recupero delle informazioni utente.');
      }
  
      return response.json();
    },
  
    // Effettua il logout
    logout: async (refreshToken) => {
      const body = new URLSearchParams({
        client_id: 'front-end-client',
        refresh_token: refreshToken,
      });
  
      const response = await fetch(KEYCLOAK_LOGOUT_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: body.toString(),
      });
  
      if (!response.ok) {
        throw new Error('Errore durante il logout.');
      }
  
      return response.text('Log-out eseguito con successo'); // Restituisce un messaggio di successo
    },
};

export default api;
