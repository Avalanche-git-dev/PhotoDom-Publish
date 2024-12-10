
// import React, { useState, useEffect } from "react";
// import { ApiMiddleware } from "../../utils/ApiMiddleware";
// import "./feed.css";

// const Feed = () => {
//   const api = ApiMiddleware();

//   // Stati principali
//   const [photos, setPhotos] = useState([]); // Contiene tutte le foto caricate finora
//   const [page, setPage] = useState(1); // Pagina corrente
//   const [loading, setLoading] = useState(false); // Stato di caricamento
//   const [hasMore, setHasMore] = useState(true); // Indica se ci sono altre pagine da caricare

//   const pageSize = 10; // Numero di foto per pagina

//   // Funzione per caricare le foto di una pagina specifica
//   const loadPhotos = async (pageNumber) => {
//     if (loading || !hasMore) return; // Evita richieste multiple o inutili
//     setLoading(true);

//     try {
//       const response = await api.get(
//         `/photos/full/batch?page=${pageNumber}&size=${pageSize}`
//       );
//       const data = response;
//       console.log(data); // Supponendo che l'API restituisca un array di foto
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
//           <div className="photo-card" key={index}>
//             <img
//               src={`data:${photo.contentType};base64,${photo.imageBytes}`}
//               alt={photo.filename}
//               className="photo-image"
//             />
//             <p className="photo-title">{photo.filename}</p>
//             <p>Likes: {photo.likeCount}</p>
//           </div>
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




import React, { useState, useEffect } from "react";
import { ApiMiddleware } from "../../utils/ApiMiddleware";
import Card from "../card/card"; // Importa il componente Card
import "./feed.css";

const Feed = () => {
  const api = ApiMiddleware();

  // Stati principali
  const [photos, setPhotos] = useState([]); // Contiene tutte le foto caricate finora
  const [page, setPage] = useState(1); // Pagina corrente
  const [loading, setLoading] = useState(false); // Stato di caricamento
  const [hasMore, setHasMore] = useState(true); // Indica se ci sono altre pagine da caricare

  const pageSize = 10; // Numero di foto per pagina

  // Funzione per caricare le foto di una pagina specifica
  const loadPhotos = async (pageNumber) => {
    if (loading || !hasMore) return; // Evita richieste multiple o inutili
    setLoading(true);

    try {
      const response = await api.get(
        `/photos/full/batch?page=${pageNumber}&size=${pageSize}`
      );
      const data = response;
      console.log(data); // Supponendo che l'API restituisca un array di foto
      if (data.length === 0) {
        setHasMore(false); // Non ci sono altre foto da caricare
      } else {
        setPhotos((prevPhotos) => [...prevPhotos, ...data]); // Aggiunge nuove foto alle esistenti
      }
    } catch (error) {
      console.error("Errore nel caricamento delle foto:", error);
    } finally {
      setLoading(false);
    }
  };

  // Effettua il caricamento iniziale
  useEffect(() => {
    loadPhotos(page);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  // Gestione della paginazione: Carica la pagina successiva
  const loadNextPage = () => {
    if (hasMore) {
      setPage((prevPage) => prevPage + 1); // Incrementa la pagina
    }
  };

  return (
    <div className="feed-container">
      <div className="photo-grid">
        {photos.map((photo, index) => (
          <Card
            key={index}
            photoUrl={`data:${photo.contentType};base64,${photo.imageBase64}`}
            likesCount={photo.likeCount}
            
            onLike={(liked) => {
              console.log(`Photo ${photo.filename} liked:`, liked);
              // Qui puoi implementare una chiamata API per aggiornare il like
            }}
            onOpenModal={() => {
              console.log("Open modal for:", photo);
              // Qui puoi implementare il modal per visualizzare la foto in grande e interagire
            }}
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
    </div>
  );
};

export default Feed;
