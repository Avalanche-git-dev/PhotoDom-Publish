// import React from "react";
// import { NavLink, useNavigate } from "react-router-dom";
// import { useAuth } from "../../utils/AuthContext";

// const Navbar = () => {
//   const { logout } = useAuth();
//   const navigate = useNavigate();

//   const handleLogout = async () => {
//     const success = await logout();
//     if (success) {
//       navigate('/login', { state: { message: 'Logout eseguito con successo!' } });
//     }
//   };

//   return (
//     <nav className="navbar">
//       <ul>
//         <li>
//           <NavLink to="/dashboard">Dashboard</NavLink>
//         </li>
//         <li>
//           <NavLink to="/profile">Profile</NavLink>
//         </li>
//         <li>
//           <NavLink to="/gallery">Gallery</NavLink>
//         </li>
//         <li>
//           <NavLink to="/explore">Explore</NavLink>
//         </li>
//         <li>
//           <button onClick={handleLogout}>Logout</button>
//         </li>
//       </ul>
//     </nav>
//   );
// };

// export default Navbar;



// import React from "react";
// import { Link } from "react-router-dom";
// import { useAuth } from '../../utils/AuthContext'; // Usa il tuo AuthProvider

// const Navbar = () => {
//   const { isAuthenticated } = useAuth();

//   if (!isAuthenticated) {
//     return null; // Nasconde la navbar se l'utente non è autenticato
//   }

//   return (
//     <nav className="navbar">
//       <ul className="navbar-list">
//         <li className="navbar-item">
//           <Link to="/dashboard">Dashboard</Link>
//         </li>
//         <li className="navbar-item">
//           <Link to="/profile">Profile</Link>
//         </li>
//         <li className="navbar-item">
//           <Link to="/gallery">Gallery</Link>
//         </li>
//         <li className="navbar-item">
//           <Link to="/explore">Explore</Link>
//         </li>
//       </ul>
//     </nav>
//   );
// };

// export default Navbar;




import React from "react";
import { NavLink,useNavigate } from "react-router-dom";
import { useAuth } from "../../utils/AuthContext";
import './navbar.css'
import icon from '../../images/icon.webp';

// const Navbar = () => {
//   const { logout } = useAuth();
//   const navigate = useNavigate();

//   const handleLogout = async () => {
//     const result = await logout(); // Chiamata alla funzione di logout
//     if (result) {
//       navigate("/login", { state: { message: "Logout eseguito con successo!" } });
//     }
//   };

const Navbar = () => {
  const { userInfo } = useAuth(); // Verifica se l'utente è loggato

 
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    
   

    const success = await logout();
    if (success) {
      // navigate("/login", { state: { message: "Logout eseguito con successo!" } });
       navigate("/login");
    }
  };

  if (!userInfo) {
    return null; // Non mostrare nulla se l'utente non è autenticato
  }

  return (
    // <nav className="navbar">
    //   <ul>
    //     <li>
    //       <NavLink to="/dashboard">Dashboard</NavLink>
    //     </li>
    //     <li>
    //       <NavLink to="/profile">Profile</NavLink>
    //     </li>
    //     <li>
    //       <NavLink to="/gallery">Gallery</NavLink>
    //     </li>
    //     <li>
    //       <NavLink to="/explore">Explore</NavLink>
    //     </li>
    //     <li>
    //        <button onClick={handleLogout}>Logout</button>
    //     </li>
    //   </ul>
    // </nav>



    <nav className="navbar navbar-expand-lg navbar-secondary bg-primary custom-navbar">
    {/* <div className="container-fluid"> */}
      {/* Logo e Brand */}
      <NavLink className="navbar-brand" to="/dashboard">
        <img
          src={icon} // Modifica con il percorso reale del tuo logo
          alt="logo"
          className="logo"
        />
      </NavLink>

      {/* Toggler per dispositivi piccoli */}
      <button
        className="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </button>

      {/* Links della Navbar */}
      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav me-auto">
          {/* <li className="nav-item">
            <NavLink className="nav-link text-white" to="/dashboard">
              Dashboard
            </NavLink>
          </li> */}
          <li className="nav-item">
            <NavLink className="nav-link text-white" to="/profile">
              Profile
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink className="nav-link text-white" to="/gallery">
              Gallery
            </NavLink>
          </li>
          <li className="nav-item">
            <NavLink className="nav-link text-white" to="/explore">
              Explore
            </NavLink>
          </li>
        </ul>

        {/* Bottone Logout */}
        {/* <form className="logout-btn"> */}
          <button
            type="button"
            className="btn btn-secondary logout-btn"
            onClick={handleLogout}
          >
            Logout
          </button>
          <i className="fas fa-cog settings-icon text-white" title="Impostazioni"></i>
        {/* </form> */}
      </div>
    {/* </div> */}
  </nav>
  );
};

export default Navbar;
