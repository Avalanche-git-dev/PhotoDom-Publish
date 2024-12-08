import React, { useEffect, useState } from 'react';
import api from '../utils/api';

function Profile() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const data = await api.get('/users/me'); // Endpoint per ottenere i dati dell'utente
        setUser(data);
      } catch (error) {
        console.error('Errore nel recupero dei dati:', error);
      }
    };

    fetchUserData();
  }, []);

  if (!user) {
    return <div>Caricamento...</div>;
  }

  return (
    <div>
      <h1>Profilo Utente</h1>
      <p>Username: {user.username}</p>
      <p>Email: {user.email}</p>
    </div>
  );
}

export default Profile;
