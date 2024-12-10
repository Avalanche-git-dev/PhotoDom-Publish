// import React, { useEffect, useState } from "react";
// import { useAuth } from "../AuthProvider"; // Usa il tuo AuthProvider
// import apiMiddleware from "../apiMiddleware";

// const Dashboard = () => {
//   const { isAuthenticated } = useAuth();
//   const [nickname, setNickname] = useState("");

//   useEffect(() => {
//     const fetchUserInfo = async () => {
//       try {
//        /* const response = await apiMiddleware.get("/user/info"); // Endpoint per ottenere le info utente*/
//         const userInfo = await response.json();

//         setNickname(userInfo.Nickname || "Guest"); // Usa il nickname o "Guest" come fallback
//       } catch (error) {
//         console.error("Errore nel recuperare le informazioni dell'utente:", error);
//       }
//     };

//     if (isAuthenticated) {
//       fetchUserInfo();
//     }
//   }, [isAuthenticated]);

//   if (!isAuthenticated) {
//     return <p>Caricamento in corso...</p>;
//   }

//   return (
//     <div className="dashboard">
//       <h1>Benvenuto {nickname}!</h1>
//       <p>Questa è la tua dashboard. Puoi esplorare le tue foto o quelle di altri utenti.</p>
//     </div>
//   );
// };

// export default Dashboard;



import React from 'react';
import { useAuth } from '../../utils/AuthContext';
import Feed from '../feed/feed.js'

const Dashboard = () => {
  const { userInfo } = useAuth();

  if (!userInfo) {
    return <p>Caricamento in corso...</p>;
  }

  return (
    // <div className="dashboard">
    //   <h1>Benvenuto {userInfo.Nickname || 'Guest'}!</h1>
    //   <p>Questa è la tua dashboard. Puoi esplorare le tue foto o quelle di altri utenti.</p>
    // </div>



    <div className="dashboard">
      <h1>Benvenuto {userInfo.Nickname || 'Guest'}!</h1>
      <p>Esplora il feed delle foto!</p>
      <Feed />
    </div>
  );
};

export default Dashboard;
