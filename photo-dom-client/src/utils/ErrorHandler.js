export const handleError = (error) => {
    console.error('Errore globale:', error.message || 'Errore sconosciuto');
    if (error.message === 'Sessione scaduta. Effettua nuovamente il login.') {
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
      window.location.href = '/login'; // Reindirizza al login
    }


    if (error.message === 'Refresh token non disponibile.') {
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
      window.location.href = '/login'; // Reindirizza al login
      }
  };
  