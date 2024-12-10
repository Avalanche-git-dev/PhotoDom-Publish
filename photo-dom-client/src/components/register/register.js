
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../utils/AuthContext';
import api from '../../services/Api';
import './register.css';

function Register() {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    birthday: '',
    email: '',
    telephone: '',
    nickname: '',
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();
  const { saveTokens } = useAuth();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      // Effettua la registrazione
      await api.register(formData);

      // Effettua il login automaticamente
      const tokenData = await api.login(formData.username, formData.password);

      // Salva i token nel contesto
      saveTokens(tokenData.access_token, tokenData.refresh_token);

      // Recupera informazioni sull'utente
      // await fetchUserInfo();

      // Mostra messaggio di successo e reindirizza
      setSuccess('Registrazione completata con successo!');
      setTimeout(() => navigate('/dashboard'), 250); // Aspetta 2 secondi prima di reindirizzare
    } catch (err) {
      console.error('Errore durante la registrazione:', err);
      setError('Si è verificato un problema durante la registrazione.');
    }
  };

  return (
    // <div className="register-body">
    //   <div className="register-container">
    //     <form className="register-form" onSubmit={handleSubmit}>
    //       <h2>Registrazione</h2>
    //       <input
    //         type="text"
    //         name="username"
    //         placeholder="Username"
    //         value={formData.username}
    //         onChange={handleChange}
    //         required
    //       />
    //       <input
    //         type="password"
    //         name="password"
    //         placeholder="Password"
    //         value={formData.password}
    //         onChange={handleChange}
    //         required
    //       />
    //       <input
    //         type="text"
    //         name="firstName"
    //         placeholder="Nome"
    //         value={formData.firstName}
    //         onChange={handleChange}
    //         required
    //       />
    //       <input
    //         type="text"
    //         name="lastName"
    //         placeholder="Cognome"
    //         value={formData.lastName}
    //         onChange={handleChange}
    //         required
    //       />
    //       <input
    //         type="date"
    //         name="birthday"
    //         placeholder="Data di nascita"
    //         value={formData.birthday}
    //         onChange={handleChange}
    //         required
    //       />
    //       <input
    //         type="email"
    //         name="email"
    //         placeholder="Email"
    //         value={formData.email}
    //         onChange={handleChange}
    //         required
    //       />
    //       <input
    //         type="text"
    //         name="telephone"
    //         placeholder="Telefono"
    //         value={formData.telephone}
    //         onChange={handleChange}
    //         required
    //       />
    //       <input
    //         type="text"
    //         name="nickname"
    //         placeholder="Nickname"
    //         value={formData.nickname}
    //         onChange={handleChange}
    //         required
    //       />
    //       {error && <div className="text-danger">{error}</div>}
    //       {success && <div className="text-success">{success}</div>}
    //       <button type="submit" className="btn btn-primary">
    //         Registrati
    //       </button>
    //     </form>
    //   </div>
    // </div>



    <div className="register-body">
    <div className="register-container">
      {/* Controlla se la registrazione è avvenuta con successo */}
      {!success ? (
        <form className="register-form" onSubmit={handleSubmit}>
          <h2>Registrazione</h2>
          <input
            type="text"
            name="username"
            placeholder="Username"
            value={formData.username}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          <input
            type="text"
            name="firstName"
            placeholder="Nome"
            value={formData.firstName}
            onChange={handleChange}
            required
          />
          <input
            type="text"
            name="lastName"
            placeholder="Cognome"
            value={formData.lastName}
            onChange={handleChange}
            required
          />
          <input
            type="date"
            name="birthday"
            placeholder="Data di nascita"
            value={formData.birthday}
            onChange={handleChange}
            required
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            required
          />
          <input
            type="text"
            name="telephone"
            placeholder="Telefono"
            value={formData.telephone}
            onChange={handleChange}
            required
          />
          <input
            type="text"
            name="nickname"
            placeholder="Nickname"
            value={formData.nickname}
            onChange={handleChange}
            required
          />
          {error && <div className="text-danger">{error}</div>}
          <button type="submit" className="btn btn-primary">
            Registrati
          </button>
        </form>
      ) : (
        <div className="success-message">
          <h2>{success}</h2>
          <p>Reindirizzamento in corso...</p>
        </div>
      )}
    </div>
  </div>
  );
}

export default Register;
