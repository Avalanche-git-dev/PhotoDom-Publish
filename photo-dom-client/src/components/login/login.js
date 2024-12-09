// import React, { useState } from 'react';
// import { useAuth } from '../../utils/AuthContext'; // Usa il contesto per l'autenticazione
// import { useNavigate } from 'react-router-dom'; // Per il reindirizzamento
// import './login.css';

// function Login() {
//   const [username, setUsername] = useState('');
//   const [password, setPassword] = useState('');
//   const [error, setError] = useState('');
//   const [tokenInfo, setTokenInfo] = useState(null);

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     setError('');

//     try {
//       const data = await api.login(username, password);
//       console.log('Token ottenuto:', data);

//       // Decodifica il token e stampa i dettagli
//       const decoded = decodeToken(data.access_token);
//       console.log('Dati decodificati:', decoded);
//       setTokenInfo(decoded);
//       useAuth.

//       alert('Login effettuato con successo!');
//     } catch (err) {
//       console.error(err);
//       setError('Credenziali errate o problema durante il login.');
//     }
//   };

//   return (
//     // <div className="container">
//     <div className="login-container login-body">
//   <div className="login-form">
//       <h2>Login</h2>
//       <form onSubmit={handleSubmit}>
//           <label className="form-label">Username</label>
//           <input
//             type="text"
//             className="form-control"
//             value={username}
//             onChange={(e) => setUsername(e.target.value)}
//             required
//           />
//           <label className="form-label">Password</label>
//           <input
//             type="password"
//             className="form-control"
//             value={password}
//             onChange={(e) => setPassword(e.target.value)}
//             required
//           />
//         {error && <div className="text-danger">{error}</div>}
//         <button type="submit" className="btn btn-primary login-btn">
//           Login
//         </button>
//       </form>
//       </div>
//     </div>
//     // </div>
//   );
// }

// export default Login;


// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// // import { useLocation } from "react-router-dom";

// import { useAuth } from '../../utils/AuthContext';
// import api from '../../services/Api';
// import { handleError } from '../../utils/ErrorHandler.js'; // Gestione globale degli errori
// import './login.css';

// function Login() {
//   // const location = useLocation();
//   // const { login } = useAuth();
//   const [username, setUsername] = useState('');
//   const [password, setPassword] = useState('');
//   // const [message, setMessage] = useState(location.state?.message || "");
//   const [error, setError] = useState('');
//   const navigate = useNavigate();
//   const { saveTokens, fetchUserInfo } = useAuth();

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     setError('');

//     try {
    
//       const tokenData = await api.login(username, password);

      
//       await saveTokens(tokenData.access_token, tokenData.refresh_token);
//       // console.log(tokenData.access_token);
      
//       // await fetchUserInfo();
//       await fetchUserInfo();
//       console.log(await fetchUserInfo());
//       // Passo 4: Naviga alla dashboard
//       navigate('/dashboard');
//     } catch (err) {
//       console.error('Errore durante il login:', err);
//       setError('Credenziali errate o problema durante il login.');
//       handleError(err); // Gestione globale degli errori
//     }
//   };

//   return (
//     <div className="login-container login-body">
//       <div className="login-form">
//         <h2>Login</h2>
//         <form onSubmit={handleSubmit}>
//           <label className="form-label">Username</label>
//           <input
//             type="text"
//             className="form-control"
//             value={username}
//             onChange={(e) => setUsername(e.target.value)}
//             required
//           />
//           <label className="form-label">Password</label>
//           <input
//             type="password"
//             className="form-control"
//             value={password}
//             onChange={(e) => setPassword(e.target.value)}
//             required
//           />
//           {error && <div className="alert alert-danger">{error}</div>}
//           {/* {message && <p className="alert alert-success">{message}</p>} */}
//           <button type="submit" className="btn btn-primary login-btn">
//             Login
//           </button>
//         </form>
//       </div>
//     </div>
//   );
// }

// export default Login;




// import React, { useState } from 'react';
// import { useNavigate, useLocation } from 'react-router-dom';
// import { useAuth } from '../../utils/AuthContext';
// import api from '../../services/Api';
// import { handleError } from '../../utils/ErrorHandler.js'; // Gestione globale degli errori
// import './login.css';

// function Login() {
//   const location = useLocation(); // Per leggere lo stato passato dal logout
//   const navigate = useNavigate();

//   const { saveTokens, fetchUserInfo } = useAuth();
//   const [username, setUsername] = useState('');
//   const [password, setPassword] = useState('');
//   const [error, setError] = useState('');
//   const [message] = useState(location.state?.message || ''); // Messaggio passato dal logout

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     setError('');

//     try {
//       const tokenData = await api.login(username, password);

//       await saveTokens(tokenData.access_token, tokenData.refresh_token);
//       await fetchUserInfo();

//       navigate('/dashboard');
//     } catch (err) {
//       console.error('Errore durante il login:', err);
//       setError('Credenziali errate o problema durante il login.');
//       handleError(err); // Gestione globale degli errori
//     }
//   };



import React, { useState,useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../utils/AuthContext';
import api from '../../services/Api';
import './login.css';

function Login() {
  

  
 
  



  
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { saveTokens,message,clearMessage } = useAuth();
  

  // Resetta il messaggio una volta mostrato
  useEffect(() => {
    if (message) {
      const timer = setTimeout(() => clearMessage(), 3000); // Rimuove il messaggio dopo 3 secondi
      return () => clearTimeout(timer); // Pulisce il timer se il componente si smonta
    }
  }, [message, clearMessage]);

       


  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const tokenData = await api.login(username, password);
      saveTokens(tokenData.access_token, tokenData.refresh_token);
      // console.log(tokenData.access_token);
      // console.log(tokenData.refresh_token);
      // await fetchUserInfo();
      // console.log(fetchUserInfo());
      navigate('/dashboard');
    } catch (err) {
      console.error('Errore durante il login:', err);
      setError('Credenziali errate o problema durante il login.');
    }
  };
 

  return (
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
          {error && <div className="alert text-danger">{error}</div>}
          {message && <div className="alert text-success">{message}</div>} {/*Messaggio di successo*/}
          <button type="submit" className="btn btn-primary login-btn">
            Login
          </button>
        </form>
      </div>
    </div>
  );
  
}




export default Login;
