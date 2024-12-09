// import React, { useState } from 'react';
// import api from '../../services/api';
// import { useNavigate } from 'react-router-dom';
// import './register.css';


// function Register(){

//   const [formData, setFormData] = useState({
//     username: '',
//     password: '',
//     firstName: '',
//     lastName: '',
//     birthday: '',
//     email: '',
//     telephone: '',
//     nickname: '',
//   });


//   const [error, setError] = useState('');
//   const navigate = useNavigate();

//   const handleChange = (e) => {
//     const { name, value } = e.target;
//     setFormData({ ...formData, [name]: value });
//   };

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     setError('');

//     try {
//       await api.register(formData);

//       // Dopo la registrazione, effettua il login automaticamente
//       const tokenData = await api.login(formData.username, formData.password);
//       console.log('Token ricevuto:', tokenData);

//       alert('Registrazione completata con successo!');
//       navigate('/dashboard'); // Reindirizza alla dashboard
//     } catch (err) {
//       console.error(err);
//       setError(err.message);
//     }
//   };

//     return (


// <>

//       <div className="register-body">
//       <div className="register-container">
//         <form className="register-form" onSubmit={handleSubmit}>
//           <h2>Registrazione</h2>
//           <input type="text" name="username" placeholder="Username" onChange={handleChange} required />
//           <input type="password" name="password" placeholder="Password" onChange={handleChange} required />
//           <input type="text" name="firstName" placeholder="Nome" onChange={handleChange} required />
//           <input type="text" name="lastName" placeholder="Cognome" onChange={handleChange} required />
//           <input type="date" name="birthday" placeholder="Data di nascita" onChange={handleChange} required />
//           <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
//           <input type="text" name="telephone" placeholder="Telefono" onChange={handleChange} required />
//           <input type="text" name="nickname" placeholder="Nickname" onChange={handleChange} required />
//           {error && <div className="text-danger">{error}</div>}
//           <button type="submit" className="btn btn-primary">
//             Registrati
//           </button>
//         </form>
//       </div>
//     </div>

      
        
       
//   {/* Register form */}
//   {/* <div className='register-body'>
//   <div className="register-container">
//     <form
//       className="register-form"
//       action="<c:url value= '/user/registrati' />"
//       method="post"
//     >
//       <h2>Registrazione</h2>
//       <input type="text" name="username" placeholder="Username" required="" />
//       <input
//         type="password"
//         name="password"
//         placeholder="Password"
//         required=""
//       />
//       <input type="text" name="nome" placeholder="Nome" required="" />
//       <input type="text" name="cognome" placeholder="Cognome" required="" />
//       <input
//         type="text"
//         name="telefono"
//         placeholder="Numero di Telefono"
//         required=""
//       />
//       <input type="email" name="email" placeholder="Email" required="" />
//       <button type="submit" className="btn btn-primary">
//         Registrati
//       </button>
//     </form>
//   </div>
//   </div> */}

// </>



//     );

        
    


   
// }



// export default Register;





import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../utils/AuthContext';
import api from '../../services/Api';
import { handleError } from '../../utils/ErrorHandler'; // Gestione degli errori
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
  const navigate = useNavigate();
  const { saveTokens, fetchUserInfo } = useAuth();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      // Passo 1: Effettua la registrazione
      await api.register(formData);

      // Passo 2: Effettua il login automaticamente
      const tokenData = await api.login(formData.username, formData.password);

      // Passo 3: Salva i token nel contesto
      saveTokens(tokenData.access_token, tokenData.refresh_token);

      // Passo 4: Recupera informazioni sull'utente
      await fetchUserInfo();

      // Passo 5: Naviga alla dashboard
      alert('Registrazione completata con successo!');
      navigate('/dashboard');
    } catch (err) {
      console.error('Errore durante la registrazione:', err);
      setError('Si è verificato un problema durante la registrazione.');
      handleError(err); // Gestione globale degli errori
    }
  };

  return (
    <div className="register-body">
      <div className="register-container">
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
      </div>
    </div>
  );
}

export default Register;
