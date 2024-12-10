import React, { useState } from "react";

const Card = ({ photoUrl, likesCount, onLike, onOpenModal }) => {
  const [liked, setLiked] = useState(false); // Stato per il like locale
  const [likes, setLikes] = useState(likesCount); // Contatore dei like

  const handleLike = () => {
    setLiked(!liked);
    setLikes(likes + (liked ? -1 : 1));
    onLike(!liked); // Notifica al genitore del cambio di stato
  };

  return (
    <div style={styles.card}>
      {/* Foto */}
      <img src={photoUrl} alt="Foto" style={styles.photo} />
      
      {/* Sezione interattiva */}
      <div style={styles.actions}>
        {/* Tasto Like */}
        <button onClick={handleLike} style={styles.likeButton}>
          {liked ? "❤️" : "🤍"} {likes}
        </button>

        {/* Tasto Apri Modal */}
        <button onClick={onOpenModal} style={styles.modalButton}>
          🔍
        </button>
      </div>
    </div>
  );
};

// Stili inline di base
const styles = {
  card: {
    border: "1px solid #ddd",
    borderRadius: "8px",
    padding: "10px",
    maxWidth: "300px",
    boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
    textAlign: "center",
  },
  photo: {
    width: "100%",
    borderRadius: "8px",
  },
  actions: {
    display: "flex",
    justifyContent: "space-around",
    marginTop: "10px",
  },
  likeButton: {
    background: "none",
    border: "none",
    cursor: "pointer",
    fontSize: "16px",
  },
  modalButton: {
    background: "none",
    border: "none",
    cursor: "pointer",
    fontSize: "16px",
  },
};

export default Card;
