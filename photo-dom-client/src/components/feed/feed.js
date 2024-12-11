// import React, { useState, useEffect } from "react";
// import { ApiMiddleware } from "../../utils/ApiMiddleware";
// import Card from "../card/card"; // Importa il componente Card
// import "./feed.css";

// const Feed = () => {
//   const api = ApiMiddleware();

//   // Stati principali
//   const [photos, setPhotos] = useState([]); // Contiene tutte le foto caricate finora
//   const [page, setPage] = useState(0); // Pagina corrente
//   const [loading, setLoading] = useState(false); // Stato di caricamento
//   const [hasMore, setHasMore] = useState(true); // Indica se ci sono altre pagine da caricare

//   const pageSize = 15; // Numero di foto per pagina

//   // Funzione per caricare le foto di una pagina specifica
//   const loadPhotos = async (pageNumber) => {
//     if (loading || !hasMore) return; // Evita richieste multiple o inutili
//     setLoading(true);

//     try {
//       const response = await api.get(
//         `/photos/full/batch?page=${pageNumber}&size=${pageSize}`
//       );
//       const data = response;
//       // console.log(...data);
//       // console.log(data.id); // Supponendo che l'API restituisca un array di foto
//       if (data.length === 0) {
//         setHasMore(false); // Non ci sono altre foto da caricare
//       } else {
//         setPhotos((prevPhotos) => [...prevPhotos, ...data]); // Aggiunge nuove foto alle esistenti
//       }
//     } catch (error) {
//       console.error("Errore nel caricamento delle foto:", error);
//     } finally {
//       setLoading(false);
//     }
//   };

//   // Effettua il caricamento iniziale
//   useEffect(() => {
//     loadPhotos(page);
//     // eslint-disable-next-line react-hooks/exhaustive-deps
//   }, [page]);

//   // Gestione della paginazione: Carica la pagina successiva
//   const loadNextPage = () => {
//     if (hasMore) {
//       setPage((prevPage) => prevPage + 1); // Incrementa la pagina
//     }
//   };



  

//   return (
//     <div className="feed-container">
//       <div className="photo-grid">
//         {photos.map((photo, index) => (
//           <Card
//             key={index}
//             photoUrl={`data:${photo.contentType};base64,${photo.imageBase64}`}
//             likesCount={photo.likeCount}
//             photoId={photo.id} 
            
         
            
//             onLike={(liked) => {
//               console.log(`Photo ${photo.filename} liked:`, liked);
//               console.log(photo.id);
//               // Qui puoi implementare una chiamata API per aggiornare il like
//             }}
//             onOpenModal={() => {
//               console.log("Open modal for:", photo);
//               // Qui puoi implementare il modal per visualizzare la foto in grande e interagire
//             }}
//           />
//         ))}
//         {loading && <p>Caricamento in corso...</p>}
//       </div>
//       {hasMore && !loading && (
//         <div className="pagination">
//           <button onClick={loadNextPage}>Carica Altre Foto</button>
//         </div>
//       )}
//       {!hasMore && <p>Non ci sono più foto da caricare.</p>}
//     </div>
//   );
// };

// export default Feed;



// import React, { useState, useEffect } from "react";
// import { ApiMiddleware } from "../../utils/ApiMiddleware";
// import Card from "../card/card";
// import Modal from "../modal/modal";
// import "./feed.css";

// const Feed = () => {
//   const api = ApiMiddleware();

//   // Stati principali
//   const [photos, setPhotos] = useState([]);
//   const [page, setPage] = useState(0);
//   const [loading, setLoading] = useState(false);
//   const [hasMore, setHasMore] = useState(true);
//   const [selectedPhoto, setSelectedPhoto] = useState(null); // Foto selezionata per il modal

//   const pageSize = 15;

//   const loadPhotos = async (pageNumber) => {
//     if (loading || !hasMore) return;
//     setLoading(true);

//     try {
//       const response = await api.get(
//         `/photos/full/batch?page=${pageNumber}&size=${pageSize}`
//       );
//       const data = response;
//       if (data.length === 0) {
//         setHasMore(false);
//       } else {
//         setPhotos((prevPhotos) => [...prevPhotos, ...data]);
//       }
//     } catch (error) {
//       console.error("Errore nel caricamento delle foto:", error);
//     } finally {
//       setLoading(false);
//     }
//   };

//   useEffect(() => {
//     loadPhotos(page);
//   }, [page]);

//   const loadNextPage = () => {
//     if (hasMore) {
//       setPage((prevPage) => prevPage + 1);
//     }
//   };

//   const openModal = (photo) => {
//     setSelectedPhoto(photo); // Apri il modal con i dettagli della foto
//   };

//   const closeModal = () => {
//     setSelectedPhoto(null); // Chiudi il modal
//   };

//   return (
//     <div className="feed-container">
//       <div className="photo-grid">
//         {photos.map((photo, index) => (
//           <Card
//             key={index}
//             photoUrl={`data:${photo.contentType};base64,${photo.imageBase64}`}
//             likesCount={photo.likeCount}
//             photoId={photo.id}
//             onLike={(liked) => {
//               console.log(`Photo ${photo.filename} liked:`, liked);
//             }}
//             onOpenModal={() => openModal(photo)}
//           />
//         ))}
//         {loading && <p>Caricamento in corso...</p>}
//       </div>
//       {hasMore && !loading && (
//         <div className="pagination">
//           <button onClick={loadNextPage}>Carica Altre Foto</button>
//         </div>
//       )}
//       {!hasMore && <p>Non ci sono più foto da caricare.</p>}

//       {/* Modal per la foto selezionata */}
//       <Modal
//         photo={selectedPhoto}
//         isOpen={!!selectedPhoto}
//         onClose={closeModal}
//       />
//     </div>
//   );
// };

// export default Feed;




import React, { useState, useEffect } from "react";
import { ApiMiddleware } from "../../utils/ApiMiddleware";
import Card from "../card/card";
import Modal from "../modal/modal";
import "./feed.css";

const Feed = () => {
  const api = ApiMiddleware();

  // Stati principali
  const [photos, setPhotos] = useState([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [selectedPhoto, setSelectedPhoto] = useState(null); // Foto selezionata per il modal
  const [comments, setComments] = useState([]); // Commenti della foto selezionata

  const pageSize = 15;

  const loadPhotos = async (pageNumber) => {
    if (loading || !hasMore) return;
    setLoading(true);

    try {
      const response = await api.get(
        `/photos/full/batch?page=${pageNumber}&size=${pageSize}`
      );
      const data = response;
      if (data.length === 0) {
        setHasMore(false);
      } else {
        setPhotos((prevPhotos) => [...prevPhotos, ...data]);
      }
    } catch (error) {
      console.error("Errore nel caricamento delle foto:", error);
    } finally {
      setLoading(false);
    }
  };

  const loadComments = async (photoId) => {
    try {
      const response = await api.get(`/comments/comment/photo?photoId=${photoId}`);
      setComments(response); // Imposta i commenti per il modal
    } catch (error) {
      console.error("Errore nel caricamento dei commenti:", error);
    }
  };

  useEffect(() => {
    loadPhotos(page);
  }, [page]);

  const loadNextPage = () => {
    if (hasMore) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  const openModal = async (photo) => {
    setSelectedPhoto(photo);
    await loadComments(photo.id); // Carica i commenti per la foto selezionata
  };

  const closeModal = () => {
    setSelectedPhoto(null);
    setComments([]); // Pulisci i commenti
  };

  return (
    <div className="feed-container">
      <div className="photo-grid">
        {photos.map((photo, index) => (
          <Card
            key={index}
            photoUrl={`data:${photo.contentType};base64,${photo.imageBase64}`}
            likesCount={photo.likeCount}
            photoId={photo.id}
            onLike={(liked) => {
              console.log(`Photo ${photo.filename} liked:`, liked);
            }}
            onOpenModal={() => openModal(photo)}
          />
        ))}
        {loading && <p>Caricamento in corso...</p>}
      </div>
      {hasMore && !loading && (
        <div className="pagination">
          <button onClick={loadNextPage}>Carica Altre Foto</button>
        </div>
      )}
      {!hasMore && <p>Non ci sono più foto da caricare.</p>}

      {/* Modal per la foto selezionata */}
      <Modal
  photo={selectedPhoto}
  comments={comments}
  isOpen={!!selectedPhoto}
  onClose={closeModal}
  api={api} // Passa l'istanza API
/>
    </div>
  );
};

export default Feed;
