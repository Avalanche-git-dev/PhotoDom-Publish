import React, { useState } from 'react';
import api from '../../services/api';
import { decodeToken } from '../../utils/auth';
import './login.css';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [tokenInfo, setTokenInfo] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const data = await api.login(username, password);
      console.log('Token ottenuto:', data);

      // Decodifica il token e stampa i dettagli
      const decoded = decodeToken(data.access_token);
      console.log('Dati decodificati:', decoded);
      setTokenInfo(decoded);

      alert('Login effettuato con successo!');
    } catch (err) {
      console.error(err);
      setError('Credenziali errate o problema durante il login.');
    }
  };

  return (
    // <div className="container">
    <div className="login-container login-body">
  <div className="login-form">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
          <label className="form-label">Username</label>
          <input
            type="text"
            className="form-control"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <label className="form-label">Password</label>
          <input
            type="password"
            className="form-control"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        {error && <div className="text-danger">{error}</div>}
        <button type="submit" className="btn btn-primary login-btn">
          Login
        </button>
      </form>
      </div>
    </div>
    // </div>
  );
}

export default Login;
