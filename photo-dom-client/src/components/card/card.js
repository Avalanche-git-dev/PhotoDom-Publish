// import React, { useState } from "react";
// import { ApiMiddleware } from "../../utils/ApiMiddleware";
// const Card = ({ photoUrl, likesCount, onLike, onOpenModal,photoId }) => {
//   // const [liked, setLiked] = useState(false); // Stato per il like locale
//   // const [likes, setLikes] = useState(likesCount); // Contatore dei like

//   // const handleLike = () => {
//   //   setLiked(!liked);
//   //   setLikes(likes + (liked ? -1 : 1));
//   //   onLike(!liked); // Notifica al genitore del cambio di stato
//   // };
//   const [liked, setLiked] = useState(false);
//   const [likes, setLikes] = useState(likesCount);
//   const api = ApiMiddleware();

 

//   return (
//     <div style={styles.card}>
//       {/* Foto */}
//       <img src={photoUrl} alt="Foto" style={styles.photo} />
      
//       {/* Sezione interattiva */}
//       <div style={styles.actions}>
//         {/* Tasto Like */}
//         <button onClick={handleLike} style={styles.likeButton}>
//           {liked ? "❤️" : "🤍"} {likes}
//         </button>

//         {/* Tasto Apri Modal */}
//         <button onClick={onOpenModal} style={styles.modalButton}>
//           🔍
//         </button>
//       </div>
//     </div>
//   );
// };

// // Stili inline di base
// const styles = {
//   card: {
//     border: "1px solid #ddd",
//     borderRadius: "8px",
//     padding: "10px",
//     maxWidth: "300px",
//     boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
//     textAlign: "center",
//   },
//   photo: {
//     width: "100%",
//     borderRadius: "8px",
//   },
//   actions: {
//     display: "flex",
//     justifyContent: "space-around",
//     marginTop: "10px",
//   },
//   likeButton: {
//     background: "none",
//     border: "none",
//     cursor: "pointer",
//     fontSize: "16px",
//   },
//   modalButton: {
//     background: "none",
//     border: "none",
//     cursor: "pointer",
//     fontSize: "16px",
//   },
// };

// export default Card;


// import React, { useState } from "react";
// import { ApiMiddleware } from "../../utils/ApiMiddleware";
// import "./card.css"; // Importa il file CSS

// const Card = ({ photoUrl, likesCount, onLike, onOpenModal, photoId }) => {
//   const [liked, setLiked] = useState(false);
//   const [likes, setLikes] = useState(likesCount);
//   const api = ApiMiddleware();

//    // Funzione per controllare se l'utente ha già messo like
//   //  const checkLikeStatus = async () => {
//   //   try {
//   //     const response = await api.get(`/photos/like/status?photoId=${photoId}`);
//   //     setLiked(response.liked); // Imposta lo stato iniziale del like
//   //   } catch (error) {
//   //     console.error("Errore nel recupero dello stato del like:", error);
//   //   }
//   // };

//   // useEffect(() => {
//   //   checkLikeStatus();
//   //   // eslint-disable-next-line react-hooks/exhaustive-deps
//   // }, []);

//   const handleLike = async () => {
//     try {
//       if (!liked) {
//         const response = await api.post(`/photos/like/add?photoId=${photoId}`);
//         console.log(response.message); // Mostra il messaggio "Like aggiunto!"
//         if (response.success) {
//           setLiked(true);
//           setLikes((prevLikes) => prevLikes + 1);
//         } else {
//           console.error(response.message);
//         }
//       } else {
//         const response = await api.post(`/photos/like/remove?photoId=${photoId}`);
//         console.log(response.message); // Mostra il messaggio "Like rimosso!"
//         if (response.success) {
//           setLiked(false);
//           setLikes((prevLikes) => prevLikes - 1);
//         } else {
//           console.error(response.message);
//         }
//       }
//       onLike(!liked); // Notifica al componente padre
//     } catch (error) {
//       console.error("Errore nel gestire il like:", error);
//     }
//   };

//   return (
//     <div className="card">
//       {/* Foto */}
//       <img src={photoUrl} alt="Foto" className="photo" />

//       {/* Sezione interattiva */}
//       <div className="actions">
//         {/* Tasto Like */}
//         <button onClick={handleLike} className="like-button">
//           {liked ? "❤️" : "🤍"} {likes}
//         </button>

//         {/* Tasto Apri Modal */}
//         <button onClick={onOpenModal} className="modal-button">
//           🔍
//         </button>
//       </div>
//     </div>
//   );
// };

// export default Card;





import React, { useState, useEffect } from "react";
import { ApiMiddleware } from "../../utils/ApiMiddleware";
import "./card.css"; // Importa il file CSS

const Card = ({ photoUrl, likesCount, onLike, onOpenModal, photoId }) => {
  const [liked, setLiked] = useState(false); // Stato iniziale per il like
  const [likes, setLikes] = useState(likesCount); // Contatore dei like
  const api = ApiMiddleware();

  // Funzione per controllare se l'utente ha già messo like
  const checkLikeStatus = async () => {
    try {
      const response = await api.get(`/photos/like/status?photoId=${photoId}`);
      setLiked(response.liked); // Imposta lo stato iniziale del like
    } catch (error) {
      console.error("Errore nel recupero dello stato del like:", error);
    }
  };

  // Effettua il check dello stato del like al montaggio del componente
  useEffect(() => {
    checkLikeStatus();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Funzione per gestire il like/dislike
  const handleLike = async () => {
    try {
      if (!liked) {
        const response = await api.post(`/photos/like/add?photoId=${photoId}`);
        console.log(response.message); // Mostra il messaggio "Like aggiunto!"
        if (response.success) {
          setLiked(true);
          setLikes((prevLikes) => prevLikes + 1);
        } else {
          console.error(response.message);
        }
      } else {
        const response = await api.delete(`/photos/like/remove?photoId=${photoId}`);
        console.log(response.message); // Mostra il messaggio "Like rimosso!"
        if (response.success) {
          setLiked(false);
          setLikes((prevLikes) => prevLikes - 1);
        } else {
          console.error(response.message);
        }
      }
      onLike(!liked); // Notifica al componente padre
    } catch (error) {
      console.error("Errore nel gestire il like:", error);
    }
  };

  return (
    <div className="card">
      {/* Foto */}
      <img src={photoUrl} alt="Foto" className="photo" />

      {/* Sezione interattiva */}
      <div className="actions">
        {/* Tasto Like */}
        <button onClick={handleLike} className="like-button">
          {liked ? "❤️" : "🤍"} {likes}
        </button>

        {/* Tasto Apri Modal */}
        <button onClick={onOpenModal} className="modal-button">
          🔍
        </button>
      </div>
    </div>
  );
};

export default Card;

