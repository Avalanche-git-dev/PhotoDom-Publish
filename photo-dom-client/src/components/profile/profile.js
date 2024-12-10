
import React, { useState, useEffect } from "react";
import { ApiMiddleware } from "../../utils/ApiMiddleware";
import { useAuth } from "../../utils/AuthContext";
import "./profile.css";

const Profile = () => {
  const api = ApiMiddleware();
  const { getUserId, logout } = useAuth();

  const [profileData, setProfileData] = useState(null); // Stato per i dati del profilo
  const [editInfo, setEditInfo] = useState({
    firstName: "",
    lastName: "",
    email: "",
    nickname: "",
    telephone: "",
  }); // Stato per i dati modificabili
  const [passwords, setPasswords] = useState({
    oldPassword: "",
    newPassword: "",
  });

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  // Funzione per caricare i dati del profilo
  const loadProfileData = async () => {
    const userId = getUserId();
    if (!userId) {
      setError("User ID is not available.");
      return;
    }

    try {
      const response = await api.get(`/users/profile?id=${userId}`);
      const data = response;

      setProfileData(data);
      setEditInfo({
        firstName:  "",
        lastName:  "",
        email:  "",
        nickname:  "",
        telephone:  "",
      });
      setError("");
    } catch (err) {
      setError("Failed to load profile data.");
    }
  };

  // Carica i dati iniziali al montaggio
  useEffect(() => {
    loadProfileData();
  }, []);

  // Gestione modifica del modulo
  const handleProfileChange = (e) => {
    const { name, value } = e.target;
    setEditInfo((prev) => ({ ...prev, [name]: value }));
  };

  const handleProfileUpdate = async () => {
       setMessage("");
    setError("");
    try {
      await api.put("/users/profile/update", editInfo);
      setMessage("Profile updated successfully.");
      setError("");
      await loadProfileData(); // Ricarica i dati del profilo dopo l'aggiornamento
    } catch (err) {
      if (err.response?.status === 409) {
        console.log(err.response.status==409);
        setError("Conflict: Some data might already exist or be invalid.");
      } else {
        setError("An error occurred while updating the profile.");
      }
    }
  };


  // const handleProfileUpdate = async () => {
  //   setMessage("");
  //   setError("");
  //   try {
  //     // if (!editInfo.firstName.trim()) throw new Error("First name is required.");
  //     if (!editInfo.email.includes("@")) throw new Error("Invalid email address.");
  //     if (editInfo.telephone && editInfo.telephone.length !== 10) {
  //       throw new Error("Telephone must be exactly 10 digits.");
  //     }

  //     await api.put("/users/profile/update", editInfo);
  //     setMessage("Profile updated successfully.");
  //     await loadProfileData();
  //   } catch (err) {
  //     setError(err.response?.data?.message || err.message || "An error occurred.");
  //   }
  // };

  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPasswords((prev) => ({ ...prev, [name]: value }));
  };


  const handlePasswordUpdate = async () => {
    setMessage("");
    setError("");
    try {
      if (!passwords.oldPassword || passwords.oldPassword.length < 6) {
        throw new Error("Old password must be at least 6 characters.");
      }
      if (!passwords.newPassword.match(/^(?=.*[0-9]).{6,}$/)) {
        throw new Error("New password must contain at least 6 characters, including a number.");
      }

      await api.put("/users/credentials/update", passwords);
      setMessage("Password updated successfully.");
      setPasswords({ oldPassword: "", newPassword: "" });
    } catch (err) {
      setError(err.response?.data?.message || err.message || "An error occurred.");
    }
  };



  // const handlePasswordUpdate = async () => {
  //   setMessage("");
  //   setError("");
  //   try {
  //     if (!passwords.oldPassword || passwords.oldPassword.length < 6) {
  //       throw new Error("Old password must be at least 6 characters.");
  //     }
  //     if (!passwords.newPassword.match(/^(?=.*[0-9]).{6,}$/)) {
  //       throw new Error("New password must contain at least 6 characters, including a number.");
  //     }

  //     await api.put("/users/credentials/update", passwords);
  //     setMessage("Password updated successfully.");
  //     setPasswords({ oldPassword: "", newPassword: "" });
  //   } catch (err) {
  //     setError(err.response?.data?.message || err.message || "An error occurred.");
  //   }
  // };

  // const handleDeleteAccount = async () => {
  //   try {
  //     await api.delete(`/users/delete?id=${profileData.id}`);
  //     logout();
  //   } catch (err) {
  //     setError("Failed to delete account.");
  //   }
  // };

  const handleDeleteAccount = async () => {
    setMessage("");
    setError("");
    try {
      await api.delete(`/users/delete?id=${profileData.id}`);
     
      setMessage("Account deleted successfully.");
    } catch (err) {
      setError("Failed to delete account.");
    } finally{
      logout();
    }
  };

  if (!profileData) {
    return <p>Loading profile data...</p>;
  }

  return (
    <div className="profile-container ">
      <div className="profile-menu card">
        <h3>Your Private Info</h3>
        <ul>
          <li>Nickname: {profileData.nickname}</li>
          <li>Username: {profileData.username}</li>
          <li>Email: {profileData.email}</li>
          <li>First Name: {profileData.firstName}</li>
          <li>Last Name: {profileData.lastName}</li>
          <li>Birthday: {profileData.birthday}</li>
          <li>Age: {profileData.age}</li>
          <li>Telephone: {profileData.telephone}</li>
          <li>Role: {profileData.role}</li>
        </ul>
      </div>

      <div className="profile-content">
        <h2>Update Profile</h2>
        <div className="form-group">
          <label>First Name:</label>
          <input
            type="text"
            name="firstName"
            value={editInfo.firstName}
            onChange={handleProfileChange}
          />
        </div>
        <div className="form-group">
          <label>Last Name:</label>
          <input
            type="text"
            name="lastName"
            value={editInfo.lastName}
            onChange={handleProfileChange}
          />
        </div>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={editInfo.email}
            onChange={handleProfileChange}
          />
        </div>
        <div className="form-group">
          <label>Nickname:</label>
          <input
            type="text"
            name="nickname"
            value={editInfo.nickname}
            onChange={handleProfileChange}
          />
        </div>
        <div className="form-group">
          <label>Telephone:</label>
          <input
            type="text"
            name="telephone"
            value={editInfo.telephone}
            onChange={handleProfileChange}
          />
        </div>
        <button className="btn btn-primary" onClick={handleProfileUpdate}>Save Changes</button>

        <h2>Change Password</h2>
        <div className="form-group">
          <label>Old Password:</label>
          <input
            type="password"
            name="oldPassword"
            value={passwords.oldPassword}
            onChange={handlePasswordChange}
          />
        </div>
        <div className="form-group">
          <label>New Password:</label>
          <input
            type="password"
            name="newPassword"
            value={passwords.newPassword}
            onChange={handlePasswordChange}
          />
        </div>
        <button className = "btn btn-warning"onClick={handlePasswordUpdate}>Change Password</button>

        <h2>Delete Account</h2>
        <button className="btn btn-danger" onClick={handleDeleteAccount}>
          Delete Account
        </button>
        {message && !error && <p className="alert text-success">{message}</p>}
        {error && <p className="alert text-danger">{error}</p>}


        {/* {message && <p className="success-message">{message}</p>}
        {error && <p className="error-message">{error}</p>} */}
      </div>
    </div>
  );
};

export default Profile;






























// import React, { useState, useEffect } from "react";
// import { ApiMiddleware } from "../../utils/ApiMiddleware";
// import { useAuth } from "../../utils/AuthContext"; // Importa useAuth
// import "./profile.css";

// const Profile = () => {
//   const api = ApiMiddleware();
//   const { getUserId, logout } = useAuth(); // Usa getUserId e logout

//   // Stati principali
//   const [profileData, setProfileData] = useState(); // Stato per i dati del profilo
//   const [editInfo, setEditInfo] = useState({
//     firstName: "",
//     lastName: "",
//     email: "",
//     nickname: "",
//     telephone: "",
//   }); // Stato per i dati modificabili
//   const [passwords, setPasswords] = useState({
//     oldPassword: "",
//     newPassword: "",
//   }); // Stato per la modifica della password

//   const [message, setMessage] = useState(""); // Messaggi di successo
//   const [error, setError] = useState(""); // Messaggi di errore

//   // Funzione per caricare i dati del profilo
//   const loadProfileData = async () => {
//     const userId = getUserId();
//     if (!userId) {
//       setError("User ID is not available.");
//       return;
//     }

//     try {
//       const response = await api.get(`/users/profile?id=${userId}`);
//       const data = response;

//       setProfileData(data);
//       setError("");
//     } catch (err) {
//       setError("Failed to load profile data.");
//     }
//   };

//   // Carica i dati al montaggio del componente
//   useEffect(() => {
//     if (!profileData) {
//       loadProfileData();
//     }
//   }, [profileData]);

//   // Gestione modifica del modulo
//   const handleProfileChange = (e) => {
//     const { name, value } = e.target;
//     setEditInfo((prev) => ({ ...prev, [name]: value }));
//   };

//   const handleProfileUpdate = async () => {
//     // Validazione dei campi
//     if (!editInfo.email.includes("@")) {
//       setError("Invalid email address.");
//       return;
//     }
//     if (editInfo.telephone.length !== 10 || isNaN(editInfo.telephone)) {
//       setError("Telephone must be a 10-digit number.");
//       return;
//     }

//     try {
//       await api.put("/users/profile/update", editInfo);
//       setMessage("Profile updated successfully.");
//       setError("");
//     } catch (err) {
//       setError(err.response?.data?.message || "An error occurred.");
//       setMessage("");
//     }
//   };

//   const handlePasswordChange = (e) => {
//     const { name, value } = e.target;
//     setPasswords((prev) => ({ ...prev, [name]: value }));
//   };

//   const handlePasswordUpdate = async () => {
//     // Validazione della nuova password
//     const passwordRegex = /^(?=.*\d).{6,}$/; // Almeno 6 caratteri e 1 numero
//     if (!passwordRegex.test(passwords.newPassword)) {
//       setError("Password must be at least 6 characters long and include a number.");
//       return;
//     }

//     try {
//       await api.put("/credentials/update", passwords);
//       setMessage("Password updated successfully.");
//       setError("");
//       setPasswords({ oldPassword: "", newPassword: "" });
//     } catch (err) {
//       setError(err.response?.data?.message || "An error occurred.");
//       setMessage("");
//     }
//   };

//   const handleDeleteAccount = async () => {
//     try {
//       await api.delete(`/delete?id=${profileData.id}`);
//       logout();
//     } catch (err) {
//       setError("Failed to delete account.");
//     }
//   };

//   // Se i dati del profilo non sono disponibili, restituisci un messaggio
//   if (!profileData) {
//     return <p>No profile data available.</p>;
//   }

//   return (
//     <div className="profile-container">
//       <div className="profile-menu card">
//         <h3>Your Private Info</h3>
//         <ul>
//           <li>Nickname: {profileData.nickname}</li>
//           <li>Username: {profileData.username}</li>
//           <li>Email: {profileData.email}</li>
//           <li>First Name: {profileData.firstName}</li>
//           <li>Last Name: {profileData.lastName}</li>
//           <li>Birthday: {profileData.birthday}</li>
//           <li>Age: {profileData.age}</li>
//           <li>Telephone: {profileData.telephone}</li>
//           <li>Role: {profileData.role}</li>
//         </ul>
//       </div>

//       {/* Form principale */}
//       <div className="profile-content">
//         <h2>Update Profile</h2>
//         <div className="form-group">
//           <label>First Name:</label>
//           <input type="text" name="firstName" onChange={handleProfileChange} />
//         </div>
//         <div className="form-group">
//           <label>Last Name:</label>
//           <input type="text" name="lastName" onChange={handleProfileChange} />
//         </div>
//         <div className="form-group">
//           <label>Email:</label>
//           <input type="email" name="email" onChange={handleProfileChange} />
//         </div>
//         <div className="form-group">
//           <label>Nickname:</label>
//           <input type="text" name="nickname" onChange={handleProfileChange} />
//         </div>
//         <div className="form-group">
//           <label>Telephone:</label>
//           <input type="text" name="telephone" onChange={handleProfileChange} />
//         </div>
//         <button onClick={handleProfileUpdate}>Save Changes</button>

//         <h2>Change Password</h2>
//         <div className="form-group">
//           <label>Old Password:</label>
//           <input type="password" name="oldPassword" onChange={handlePasswordChange} />
//         </div>
//         <div className="form-group">
//           <label>New Password:</label>
//           <input type="password" name="newPassword" onChange={handlePasswordChange} />
//         </div>
//         <button onClick={handlePasswordUpdate}>Change Password</button>

//         <h2>Delete Account</h2>
//         <button className="delete-button" onClick={handleDeleteAccount}>
//           Delete Account
//         </button>

//         {/* Messaggi di feedback */}
//         {message && <p className="success-message">{message}</p>}
//         {error && <p className="error-message">{error}</p>}
//       </div>
//     </div>
//   );
// };

// export default Profile;
