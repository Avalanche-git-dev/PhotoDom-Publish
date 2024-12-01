package com.app.photoservice.service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.app.photoservice.configuration.JwtUtil;
import com.app.photoservice.dto.PhotoDto;
import com.app.photoservice.dto.PhotoMapper;
import com.app.photoservice.entity.Like;
import com.app.photoservice.entity.PhotoMetadata;
import com.app.photoservice.repository.LikeRepository;
import com.app.photoservice.repository.PhotoMetadataRepository;

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
    private JwtUtil jwt;


    
    
    
    public Mono<PhotoDto> savePhoto(FilePart file, String token) {
        Long userId = jwt.extractUserId(token);

        return file.content() // Ottiene il contenuto del FilePart come un Flux di DataBuffer
                .map(dataBuffer -> dataBuffer.asInputStream(true)) // Converte DataBuffer in InputStream
                .next() // Prende solo il primo elemento, dato che vogliamo un unico InputStream
                .flatMap(inputStream -> {
                    // Salva la foto in MongoDB e ottieni l'ID del file
                    String fileId = photoStorageService.savePhoto(inputStream, file.filename(), file.headers().getContentType().toString());

                    // Crea e salva i metadati della foto in PostgreSQL
                    PhotoMetadata metadata = new PhotoMetadata();
                    metadata.setUserId(userId);
                    metadata.setFilename(file.filename());
                    metadata.setContentType(file.headers().getContentType().toString());
                    metadata.setSize(file.headers().getContentLength());
                    metadata.setFileId(fileId);
                    metadata.setUploadDate(new Date());

                    // Salvataggio reattivo dei metadati
                    return Mono.fromCallable(() -> photoMetadataRepository.save(metadata))
                            .map(savedMetadata -> PhotoMapper.toPhotoDto(savedMetadata, null, 0));
                });
    }

//    public PhotoDto getPhotoDto(Long photoId) throws IllegalStateException, IOException {
//        PhotoMetadata metadata = photoMetadataRepository.findById(photoId)
//            .orElseThrow(() -> new PhotoNotFoundException("Photo not found"));
//
//        int likeCount = likeRepository.countByPhotoId(photoId);
//        GridFsResource photoStream = photoStorageService.getPhoto(metadata.getFileId());
//
//        return PhotoMapper.toPhotoDto(metadata, photoStream.getInputStream(), likeCount);
//    }
    
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



//
//    public boolean addLike(Long userId, Long photoId) {
//        Optional<Like> existingLike = likeRepository.findByUserIdAndPhotoId(userId, photoId);
//        if (existingLike.isPresent()) {
//            return false; // L'utente ha già messo un "like"
//        }
//
//        PhotoMetadata photo = photoMetadataRepository.findById(photoId)
//            .orElseThrow(() -> new RuntimeException("Photo not found"));
//
//        Like like = new Like();
//        like.setUserId(userId);
//        like.setPhoto(photo);
//        likeRepository.save(like);
//        return true;
//    }
//
//    public boolean removeLike(Long userId, Long photoId) {
//        Optional<Like> like = likeRepository.findByUserIdAndPhotoId(userId, photoId);
//        if (like.isPresent()) {
//            likeRepository.delete(like.get());
//            return true;
//        }
//        return false; // L'utente non ha messo un "like"
//    }
    
    public boolean addLike(String token, Long photoId) {
        Long userId = jwt.extractUserId(token); // Estrai l'userId dal token

        Optional<Like> existingLike = likeRepository.findByUserIdAndPhotoId(userId, photoId);
        if (existingLike.isPresent()) {
            return false; // L'utente ha già messo un "like"
        }

        PhotoMetadata photo = photoMetadataRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found"));

        Like like = new Like();
        like.setUserId(userId);
        like.setPhoto(photo);
        likeRepository.save(like);
        return true;
    }

    public boolean removeLike(String token, Long photoId) {
        Long userId = jwt.extractUserId(token); // Estrai l'userId dal token

        Optional<Like> like = likeRepository.findByUserIdAndPhotoId(userId, photoId);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return true;
        }
        return false; // L'utente non ha messo un "like"
    }
    
    
    
    
}

