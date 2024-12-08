import React, { useState } from 'react';
// import api from '../../services/api';
// import { decodeToken } from '../../utils/auth';
import './register.css';



function Register(){

    return (


        
        <>
  {/* Register form */}
  <div className='register-body'>
  <div className="register-container">
    <form
      className="register-form"
      action="<c:url value= '/user/registrati' />"
      method="post"
    >
      <h2>Registrazione</h2>
      <input type="text" name="username" placeholder="Username" required="" />
      <input
        type="password"
        name="password"
        placeholder="Password"
        required=""
      />
      <input type="text" name="nome" placeholder="Nome" required="" />
      <input type="text" name="cognome" placeholder="Cognome" required="" />
      <input
        type="text"
        name="telefono"
        placeholder="Numero di Telefono"
        required=""
      />
      <input type="email" name="email" placeholder="Email" required="" />
      <button type="submit" className="btn btn-primary">
        Registrati
      </button>
    </form>
  </div>
  </div>
  
</>




    );

        
    


   
}



export default Register;