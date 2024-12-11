// import React from "react";
// import "./modal.css";

// const Modal = ({ photo, isOpen, onClose, onLikeToggle }) => {
//   if (!isOpen) return null;

//   return (
//     <div className="modal-overlay">
//       <div className="modal-content">
//         <button className="close-button" onClick={onClose}>
//           ✖
//         </button>
//         <img
//           src={`data:${photo.contentType};base64,${photo.imageBytes}`}
//           alt={photo.filename}
//           className="modal-image"
//         />
//         <p className="modal-title">{photo.filename}</p>
//         <p>Likes: {photo.likeCount}</p>
//         <button onClick={() => onLikeToggle(photo.id)}>
//           {photo.isLiked ? "❤️ Unlike" : "🤍 Like"}
//         </button>
//       </div>
//     </div>
//   );
// };

// export default Modal;



// import React from "react";
// import "./modal.css";

// const Modal = ({ photo, isOpen, onClose }) => {
//   if (!isOpen || !photo) return null;

//   return (
//     <div className="modal-overlay">
//       <div className="modal-content">
//         {/* Chiudi il modal */}
//         <button className="close-button" onClick={onClose}>
//           ✖
//         </button>

//         {/* Visualizza la foto */}
//         <img
//           src={`data:${photo.contentType};base64,${photo.imageBase64}`}
//           alt={photo.filename}
//           className="modal-photo"
//         />

//         {/* Titolo della foto */}
//         <p className="modal-title">{photo.filename}</p>

//         {/* Sezione commenti */}
//         <div className="comments-section">
//           <h3>Commenti</h3>
//           <ul className="comments-list">
//             {/* Lista mock dei commenti */}
//             <li className="comment-item">
//               <p>Commento 1</p>
//               <button className="reply-button">Rispondi</button>
//             </li>
//             <li className="comment-item">
//               <p>Commento 2</p>
//               <button className="reply-button">Rispondi</button>
//             </li>
//             <li className="comment-item">
//               <p>Commento 3</p>
//               <button className="reply-button">Rispondi</button>
//             </li>
//           </ul>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Modal;



// import React, { useState } from "react";
// import Comment from "../comment/comment";
// import {useAuth} from "../../utils/AuthContext"
// import "./modal.css";

// const Modal = ({ photo, isOpen, onClose, api }) => {
//   const [comments, setComments] = useState([]); // Commenti principali
//   const [newComment, setNewComment] = useState("");
//   const { getUserId } = useAuth();
//    // Testo del nuovo commento

//   if (!isOpen || !photo) return null;

//   // Carica i commenti principali all'apertura
//   const loadComments = async () => {
//     try {
//       const response = await api.get(`/api/comments/comment/photo?photoId=${photo.id}`);
//       setComments(response);
//     } catch (error) {
//       console.error("Errore nel caricamento dei commenti:", error);
//     }
//   };

//   const addComment = async (content, parentId = null) => {
//     try {
//       // Ottieni l'ID utente corrente
//       const userId = getUserId(); // Funzione per estrarre l'ID
  
//       const comment = {
//         content,
//         photoId: photo.id,
//         userId, // Associa il commento all'utente corrente
//         parentCommentId: parentId,
//       };
//       const response = await api.post("/api/comments/comment/add", comment);
//       if (parentId) {
//         // Aggiorna le risposte del commento genitore
//         setComments((prevComments) =>
//           prevComments.map((c) =>
//             c.id === parentId
//               ? { ...c, replies: [...(c.replies || []), response] }
//               : c
//           )
//         );
//       } else {
//         // Aggiungi il nuovo commento principale
//         setComments((prevComments) => [...prevComments, response]);
//       }
//       setNewComment(""); // Resetta il campo input
//     } catch (error) {
//       console.error("Errore nell'aggiunta del commento:", error);
//     }
//   };
  

//   // Elimina un commento
//   const deleteComment = async (commentId) => {
//     try {
//       await api.delete(`/api/comments/comment/delete?id=${commentId}`);
//       setComments((prevComments) =>
//         prevComments.filter((comment) => comment.id !== commentId)
//       );
//     } catch (error) {
//       console.error("Errore nell'eliminazione del commento:", error);
//     }
//   };

//   // Carica le risposte di un commento
//   const loadReplies = async (commentId) => {
//     try {
//       const response = await api.get(`/api/comments/comment/replies?id=${commentId}`);
//       setComments((prevComments) =>
//         prevComments.map((comment) =>
//           comment.id === commentId ? { ...comment, replies: response } : comment
//         )
//       );
//     } catch (error) {
//       console.error("Errore nel caricamento delle risposte:", error);
//     }
//   };

//   return (
//     <div className="modal-overlay">
//       <div className="modal-content">
//         <button className="close-button" onClick={onClose}>
//           ✖
//         </button>
//         <img
//           src={`data:${photo.contentType};base64,${photo.imageBase64}`}
//           alt={photo.filename}
//           className="modal-photo"
//         />
//         <p className="modal-title">{photo.filename}</p>
//         <div className="comments-section">
//           <h3>Commenti</h3>
//           {comments.length === 0 ? (
//             <p>Nessun commento. Aggiungi il primo!</p>
//           ) : (
//             comments.map((comment) => (
//               <Comment
//                 key={comment.id}
//                 comment={comment}
//                 onReply={(content) => addComment(content, comment.id)}
//                 onExpand={() => loadReplies(comment.id)}
//                 onDelete={() => deleteComment(comment.id)}
//               />
//             ))
//           )}
//           <div className="add-comment">
//             <textarea
//               placeholder="Scrivi un commento..."
//               value={newComment}
//               onChange={(e) => setNewComment(e.target.value)}
//             />
//             <button onClick={() => addComment(newComment)}>Invia</button>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Modal;





// import React, { useState, useEffect } from "react";
// import Comment from "../comment/comment";
// import { useAuth } from "../../utils/AuthContext"; // Importa il contesto Auth
// import "./modal.css";

// const Modal = ({ photo, isOpen, onClose, api }) => {
//   const [comments, setComments] = useState([]); // Commenti principali
//   const [newComment, setNewComment] = useState(""); // Testo del nuovo commento
//   const { getUserId } = useAuth(); // Ottieni `getUserId` dal contesto Auth

//   // if (!isOpen || !photo) return null;

//   // Carica i commenti principali all'apertura
//   useEffect(() => {
//     const loadComments = async () => {
//       try {
//         if (isOpen && photo) { // Condizione verificata all'interno
//           const response = await api.get(`/comments/comment/photo?photoId=${photo.id}`);
//           setComments(response);
//         }
//       } catch (error) {
//         console.error("Errore nel caricamento dei commenti:", error);
//       }
//     };
  
//     loadComments();
//   }, [isOpen, photo, api]); // Dipendenze dichiarate correttamente
  

//   // Aggiungi un nuovo commento principale o risposta
//   const addComment = async (content, parentId = null) => {
//     try {
//       const userId = getUserId(); // Ottieni l'ID utente corrente
//       const comment = {
//         content,
//         photoId: photo.id,
//         userId, // Associa il commento all'utente corrente
//         parentCommentId: parentId,
//       };
//       const response = await api.post("/comments/comment/add", comment);

//       if (parentId) {
//         // Aggiorna le risposte del commento genitore
//         setComments((prevComments) =>
//           prevComments.map((c) =>
//             c.id === parentId
//               ? { ...c, replies: [...(c.replies || []), response] }
//               : c
//           )
//         );
//       } else {
//         // Aggiungi il nuovo commento principale
//         setComments((prevComments) => [...prevComments, response]);
//       }
//       setNewComment(""); // Resetta il campo input
//     } catch (error) {
//       console.error("Errore nell'aggiunta del commento:", error);
//     }
//   };

//   // Elimina un commento
//   const deleteComment = async (commentId) => {
//     try {
//       await api.delete(`/comments/comment/delete?id=${commentId}`);
//       setComments((prevComments) =>
//         prevComments.filter((comment) => comment.id !== commentId)
//       );
//     } catch (error) {
//       console.error("Errore nell'eliminazione del commento:", error);
//     }
//   };

//   // Carica le risposte di un commento
//   const loadReplies = async (commentId) => {
//     try {
//       const response = await api.get(`/comments/comment/replies?id=${commentId}`);
//       setComments((prevComments) =>
//         prevComments.map((comment) =>
//           comment.id === commentId ? { ...comment, replies: response } : comment
//         )
//       );
//     } catch (error) {
//       console.error("Errore nel caricamento delle risposte:", error);
//     }
//   };
//   if (!isOpen || !photo) return null; 

//   return (
//     <div className="modal-overlay">
//       <div className="modal-content">
//         <button className="close-button" onClick={onClose}>
//           ✖
//         </button>
//         <img
//           src={`data:${photo.contentType};base64,${photo.imageBase64}`}
//           alt={photo.filename}
//           className="modal-photo"
//         />
//         <p className="modal-title">{photo.filename}</p>
//         <div className="comments-section">
//           <h3>Commenti</h3>
//           {comments.length === 0 ? (
//             <p>Nessun commento. Aggiungi il primo!</p>
//           ) : (
//             comments.map((comment) => (
//               <Comment
//                 key={comment.id}
//                 comment={comment}
//                 onReply={(content) => addComment(content, comment.id)}
//                 onExpand={() => loadReplies(comment.id)}
//                 onDelete={() => deleteComment(comment.id)}
//               />
//             ))
//           )}
//           <div className="add-comment">
//             <textarea
//               placeholder="Scrivi un commento..."
//               value={newComment}
//               onChange={(e) => setNewComment(e.target.value)}
//             />
//             <button onClick={() => addComment(newComment)}>Invia</button>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Modal;




// import React, { useState, useEffect } from "react";
// import Comment from "../comment/comment";
// import { useAuth } from "../../utils/AuthContext";
// import "./modal.css";

// const Modal = ({ photo, isOpen, onClose, api }) => {
//   const [comments, setComments] = useState([]);
//   const [newComment, setNewComment] = useState("");
//   const { getUserId } = useAuth();

//   // Carica i commenti principali all'apertura
//   useEffect(() => {
//     const loadComments = async () => {
//       try {
//         if (isOpen && photo) {
//           const response = await api.get(`/comments/comment/photo?photoId=${photo.id}`);
//           setComments(response);
//         }
//       } catch (error) {
//         console.error("Errore nel caricamento dei commenti:", error);
//       }
//     };

//     loadComments();
//   }, [isOpen, photo, api]);

//   // Aggiungi un nuovo commento
//   const addComment = async (content, parentId = null) => {
//     try {
//       const userId = getUserId();
//       const comment = {
//         content,
//         photoId: photo.id,
//         userId,
//         parentCommentId: parentId,
//       };
//       const response = await api.post("/comments/comment/add", comment);

//       if (parentId) {
//         setComments((prevComments) =>
//           prevComments.map((c) =>
//             c.id === parentId
//               ? { ...c, replies: [...(c.replies || []), response] }
//               : c
//           )
//         );
//       } else {
//         setComments((prevComments) => [...prevComments, response]);
//       }
//       setNewComment("");
//     } catch (error) {
//       console.error("Errore nell'aggiunta del commento:", error);
//     }
//   };

//   // Elimina un commento
//   const deleteComment = async (commentId) => {
//     try {
//       await api.delete(`/comments/comment/delete?id=${commentId}`);
//       setComments((prevComments) =>
//         prevComments.filter((comment) => comment.id !== commentId)
//       );
//     } catch (error) {
//       console.error("Errore nell'eliminazione del commento:", error);
//     }
//   };

//   // Carica le risposte di un commento
//   const loadReplies = async (commentId) => {
//     try {
//       const response = await api.get(`/comments/comment/replies?id=${commentId}`);
//       setComments((prevComments) =>
//         prevComments.map((comment) =>
//           comment.id === commentId ? { ...comment, replies: response } : comment
//         )
//       );
//     } catch (error) {
//       console.error("Errore nel caricamento delle risposte:", error);
//     }
//   };

//   if (!isOpen || !photo) return null; // Aggiunto controllo per prevenire errori

//   return (
//     <div className="modal-overlay">
//       <div className="modal-content">
//         <button className="close-button" onClick={onClose}>
//           ✖
//         </button>
//         {photo && (
//           <>
//             <img
//               src={`data:${photo.contentType};base64,${photo.imageBase64}`}
//               alt={photo.filename || "Foto"}
//               className="modal-photo"
//             />
//             <p className="modal-title">{photo.filename || "Titolo non disponibile"}</p>
//           </>
//         )}
//         <div className="comments-section">
//           <h3>Commenti</h3>
//           {comments.length === 0 ? (
//             <p>Nessun commento. Aggiungi il primo!</p>
//           ) : (
//             comments.map((comment) => (
//               <Comment
//                 key={comment.id}
//                 comment={comment}
//                 onReply={(content) => addComment(content, comment.id)}
//                 onExpand={() => loadReplies(comment.id)}
//                 onDelete={() => deleteComment(comment.id)}
//               />
//             ))
//           )}
//           <div className="add-comment">
//             <textarea
//               placeholder="Scrivi un commento..."
//               value={newComment}
//               onChange={(e) => setNewComment(e.target.value)}
//             />
//             <button onClick={() => addComment(newComment)}>Invia</button>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Modal;



import React, { useState, useEffect } from "react";
import Comment from "../comment/comment";
import { useAuth } from "../../utils/AuthContext";
import "./modal.css";

const Modal = ({ photo, isOpen, onClose, api }) => {
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const { getUserId } = useAuth();

  // Carica i commenti principali all'apertura
  useEffect(() => {
    const loadComments = async () => {
      try {
        if (isOpen && photo) {
          const response = await api.get(`/comments/comment/photo?photoId=${photo.id}`);
          setComments(response);
        } else {
          setComments([]);
        }
      } catch (error) {
        console.error("Errore nel caricamento dei commenti:", error);
      }
    };

    loadComments();
  }, [isOpen, photo, api]);

  // Aggiungi un nuovo commento o risposta
  const addComment = async (content, parentId = null) => {
    try {
      const userId = getUserId();
      const comment = {
        content,
        photoId: photo.id,
        userId,
        parentCommentId: parentId,
      };
      const response = await api.post("/comments/comment/add", comment);

      if (parentId) {
        setComments((prevComments) =>
          prevComments.map((c) =>
            c.id === parentId
              ? { ...c, replies: [...(c.replies || []), response] }
              : c
          )
        );
      } else {
        setComments((prevComments) => [...prevComments, response]);
      }
      setNewComment("");
    } catch (error) {
      console.error("Errore nell'aggiunta del commento:", error);
    }
  };

  // Elimina un commento
  const deleteComment = async (commentId) => {
    try {
      await api.delete(`/comments/comment/delete?id=${commentId}`);
      const loadComments = async () => {
        const response = await api.get(`/comments/comment/photo?photoId=${photo.id}`);
        setComments(response); // Ricarica i commenti principali
      };
      loadComments();
    } catch (error) {
      console.error("Errore nell'eliminazione del commento:", error);
    }
  };

  // Carica le risposte di un commento
  const loadReplies = async (commentId) => {
    try {
      const response = await api.get(`/comments/comment/replies?id=${commentId}`);
      setComments((prevComments) =>
        prevComments.map((comment) =>
          comment.id === commentId ? { ...comment, replies: response } : comment
        )
      );
    } catch (error) {
      console.error("Errore nel caricamento delle risposte:", error);
    }
  };

  if (!isOpen || !photo) return null;

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <button className="close-button" onClick={onClose}>
          ✖
        </button>
        <div className="modal-header">
          {photo && (
            <>
              <img
                src={`data:${photo.contentType};base64,${photo.imageBase64}`}
                alt={photo.filename || "Foto"}
                className="modal-photo"
              />
              <p className="modal-title">{photo.filename || "Titolo non disponibile"}</p>
            </>
          )}
        </div>
        <div className="comments-section">
          <h3>Commenti</h3>
          <div className="comments-list">
            {comments.map((comment) => (
              <Comment
                key={comment.id}
                comment={comment}
                onReply={(content) => addComment(content, comment.id)}
                onExpand={() => loadReplies(comment.id)}
                onDelete={() => deleteComment(comment.id)}
              />
            ))}
          </div>
          <div className="add-comment">
            <textarea
              placeholder="Scrivi un nuovo commento..."
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
            />
            <button onClick={() => addComment(newComment)}>Invia</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Modal;
// import React, { useState, useEffect } from "react";
// import CommentList from "../commentList/commentList";
// import { useAuth } from "../../utils/AuthContext";
// import "./modal.css";

// const Modal = ({ photo, isOpen, onClose, api }) => {
//   const [parentComments, setParentComments] = useState([]);
//   const [newComment, setNewComment] = useState("");
//   const { getUserId } = useAuth();

//   // Carica i commenti principali all'apertura
//   useEffect(() => {
//     const loadParentComments = async () => {
//       try {
//         if (isOpen && photo) {
//           const response = await api.get(`/comments/comment/photo?photoId=${photo.id}`);
//           setParentComments(response);
//         } else {
//           setParentComments([]);
//         }
//       } catch (error) {
//         console.error("Errore nel caricamento dei commenti:", error);
//       }
//     };

//     loadParentComments();
//   }, [isOpen, photo, api]);

//   // Aggiungi un nuovo commento principale
//   const addParentComment = async () => {
//     if (!newComment.trim()) return;

//     try {
//       const userId = getUserId();
//       const comment = {
//         content: newComment,
//         photoId: photo.id,
//         userId,
//         parentCommentId: null,
//       };

//       const response = await api.post("/comments/comment/add", comment);
//       setParentComments((prev) => [...prev, response]);
//       setNewComment("");
//     } catch (error) {
//       console.error("Errore nell'aggiunta del commento:", error);
//     }
//   };

//   if (!isOpen || !photo) return null;

//   return (
//     <div className="modal-overlay">
//       <div className="modal-content">
//         <button className="close-button" onClick={onClose}>
//           ✖
//         </button>
//         <div className="modal-header">
//           <img
//             src={`data:${photo.contentType};base64,${photo.imageBase64}`}
//             alt={photo.filename || "Foto"}
//             className="modal-photo"
//           />
//           <p className="modal-title">{photo.filename || "Titolo non disponibile"}</p>
//         </div>
//         <div className="comments-section">
//           <h3>Commenti</h3>
//           <CommentList
//             comments={parentComments}
//             api={api}
//             parentCommentId={null}
//             onAddReply={(reply) =>
//               setParentComments((prev) =>
//                 prev.map((c) =>
//                   c.id === reply.parentCommentId ? { ...c, replies: [...(c.replies || []), reply] } : c
//                 )
//               )
//             }
//           />
//           <div className="add-comment">
//             <textarea
//               placeholder="Scrivi un commento..."
//               value={newComment}
//               onChange={(e) => setNewComment(e.target.value)}
//             />
//             <button onClick={addParentComment}>Invia</button>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Modal;

