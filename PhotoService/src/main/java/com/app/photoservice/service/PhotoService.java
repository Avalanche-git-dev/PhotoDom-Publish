package com.app.photoservice.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.app.photoservice.dto.PhotoDto;
import com.app.photoservice.dto.PhotoMapper;
import com.app.photoservice.entity.Like;
import com.app.photoservice.entity.PhotoMetadata;
import com.app.photoservice.exception.PhotoNotFoundException;
import com.app.photoservice.repository.LikeRepository;
import com.app.photoservice.repository.PhotoMetadataRepository;
import com.app.photoservice.utils.UserContext;

import reactor.core.publisher.Mono;


@Service
public class PhotoService {

    @Autowired
    private PhotoMetadataRepository photoMetadataRepository;

    @Autowired
    private PhotoStorageService photoStorageService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserContext userContext;

    // Salva una foto nel sistema
//    public Mono<PhotoDto> savePhoto(FilePart file) {
//        Long userId = userContext.getCurrentUserId().; // Estrai userId centralizzato
//
//        return file.content()
//                .map(dataBuffer -> dataBuffer.asInputStream(true))
//                .next()
//                .flatMap(inputStream -> {
//                    try {
//                        // Calcola la dimensione del file
//                        long size = calculateStreamSize(inputStream);
//
//                        // Salva la foto in MongoDB e ottieni l'ID del file
//                        String fileId = photoStorageService.savePhoto(inputStream, file.filename(), file.headers().getContentType().toString());
//
//                        // Crea e salva i metadati della foto
//                        PhotoMetadata metadata = new PhotoMetadata();
//                        metadata.setUserId(userId);
//                        metadata.setFilename(file.filename());
//                        metadata.setContentType(file.headers().getContentType().toString());
//                        metadata.setSize(size);
//                        metadata.setFileId(fileId);
//                        metadata.setUploadDate(new Date());
//
//                        return Mono.fromCallable(() -> photoMetadataRepository.save(metadata))
//                                .map(savedMetadata -> PhotoMapper.toPhotoDto(savedMetadata, null, 0));
//                    } catch (IOException e) {
//                        return Mono.error(new RuntimeException("Errore durante il salvataggio del file", e));
//                    }
//                });
//    }
    
    
    

    
    
    public Mono<PhotoDto> savePhoto(FilePart file) {
        return userContext.getCurrentUserId()
                .flatMap(userId ->
                    file.content()
                        .map(dataBuffer -> dataBuffer.asInputStream(true))
                        .next()
                        .flatMap(inputStream -> {
                            try {
                                // Duplica il flusso in memoria (per piccole immagini)
                                byte[] imageBytes = inputStream.readAllBytes();

                                // Calcola la dimensione del file
                                long size = imageBytes.length;

                                // Salva la foto in MongoDB e ottieni l'ID del file
                                String fileId = photoStorageService.savePhoto(
                                        new ByteArrayInputStream(imageBytes), // Flusso rigenerato
                                        file.filename(),
                                        file.headers().getContentType().toString()
                                );

                                // Crea e salva i metadati della foto
                                PhotoMetadata metadata = new PhotoMetadata();
                                metadata.setUserId(userId);
                                metadata.setFilename(file.filename());
                                metadata.setContentType(file.headers().getContentType().toString());
                                metadata.setSize(size);
                                metadata.setFileId(fileId);
                                metadata.setUploadDate(new Date());

                                return Mono.fromCallable(() -> photoMetadataRepository.save(metadata))
                                        .map(savedMetadata -> PhotoMapper.toPhotoDto(savedMetadata, null, 0));
                            } catch (IOException e) {
                                return Mono.error(new RuntimeException("Errore durante il salvataggio del file", e));
                            }
                        })
                );
    }

    
    



  public Mono<PhotoDto> getPhotoById(Long photoId) {
  return Mono.defer(() -> {
      Optional<PhotoMetadata> optionalMetadata = photoMetadataRepository.findById(photoId);
      
      if (optionalMetadata.isEmpty()) {
          return Mono.error(new RuntimeException("Photo not found"));
      }

      PhotoMetadata metadata = optionalMetadata.get();
      GridFsResource photoResource = photoStorageService.getPhoto(metadata.getFileId());

      try {
          // Usa la classe mapper per creare il PhotoDto
      	int likeCount = likeRepository.countByPhotoId(photoId);
          PhotoDto photoDto = PhotoMapper.toPhotoDto(metadata, photoResource.getInputStream(), likeCount);
          return Mono.just(photoDto);
      } catch (IOException e) {
          return Mono.error(new RuntimeException("Error reading photo stream", e));
      }
  });
}


  
  
  public Mono<Boolean> addLike(Long photoId) {
	    return userContext.getCurrentUserId()
	            .flatMap(userId -> {
	                Optional<Like> existingLike = likeRepository.findByUserIdAndPhotoId(userId, photoId);
	                if (existingLike.isPresent()) {
	                    return Mono.just(false); // L'utente ha già messo un "like"
	                }

	                PhotoMetadata photo = photoMetadataRepository.findById(photoId)
	                        .orElseThrow(() -> new PhotoNotFoundException("Photo not found"));

	                Like like = new Like();
	                like.setUserId(userId);
	                like.setPhoto(photo);
	                likeRepository.save(like);
	                return Mono.just(true);
	            });
	}

	public Mono<Boolean> removeLike(Long photoId) {
	    return userContext.getCurrentUserId()
	            .flatMap(userId -> {
	                Optional<Like> like = likeRepository.findByUserIdAndPhotoId(userId, photoId);
	                if (like.isPresent()) {
	                    likeRepository.delete(like.get());
	                    return Mono.just(true);
	                }
	                return Mono.just(false); // L'utente non ha messo un "like"
	            });
	}
	
public PhotoDto getPhotoDto(Long photoId) throws IllegalStateException, IOException {
PhotoMetadata metadata = photoMetadataRepository.findById(photoId)
    .orElseThrow(() -> new PhotoNotFoundException("Photo not found"));

int likeCount = likeRepository.countByPhotoId(photoId);
GridFsResource photoStream = photoStorageService.getPhoto(metadata.getFileId());

return PhotoMapper.toPhotoDto(metadata, photoStream.getInputStream(), likeCount);
}







    // Metodo di utilità per calcolare la dimensione di un InputStream
//    private long calculateStreamSize(InputStream inputStream) throws IOException {
//        long size = 0;
//        byte[] buffer = new byte[8192];
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            size += bytesRead;
//        }
//        return size;
//    }
}


