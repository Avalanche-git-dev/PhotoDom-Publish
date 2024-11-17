package com.app.photoservice.service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.photoservice.configuration.JwtUtil;
import com.app.photoservice.dto.PhotoDto;
import com.app.photoservice.dto.PhotoMapper;
import com.app.photoservice.entity.Like;
import com.app.photoservice.entity.PhotoMetadata;
import com.app.photoservice.exception.PhotoNotFoundException;
import com.app.photoservice.repository.LikeRepository;
import com.app.photoservice.repository.PhotoMetadataRepository;

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

//    public PhotoDto savePhoto(PhotoDto photoDto) {
//        // Salva la foto in MongoDB
//        String fileId = photoStorageService.savePhoto(photoDto.getPhotoStream(), photoDto.getFilename(), photoDto.getContentType());
//
//        // Salva i metadati della foto in PostgreSQL
//        PhotoMetadata metadata = PhotoMapper.toPhotoMetadata(photoDto, fileId);
//        PhotoMetadata savedMetadata = photoMetadataRepository.save(metadata);
//
//        return PhotoMapper.toPhotoDto(savedMetadata, null, 0);
//    }
    
    
    public PhotoDto savePhoto(MultipartFile file, String token) throws IOException {
        // Salva la foto in MongoDB e ottieni l'ID del file
        String fileId = photoStorageService.savePhoto(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        Long userId = jwt.extractUserId(token);

        // Crea e salva i metadati della foto in PostgreSQL
        PhotoMetadata metadata = new PhotoMetadata();
        metadata.setUserId(userId);
        metadata.setFilename(file.getOriginalFilename());
        metadata.setContentType(file.getContentType());
        metadata.setSize(file.getSize());
        metadata.setFileId(fileId);
        metadata.setUploadDate(new Date());
        
        PhotoMetadata savedMetadata = photoMetadataRepository.save(metadata);

        // Restituisci un PhotoDto con i dettagli della foto salvata
        return PhotoMapper.toPhotoDto(savedMetadata, null, 0);
    }

    public PhotoDto getPhotoDto(Long photoId) throws IllegalStateException, IOException {
        PhotoMetadata metadata = photoMetadataRepository.findById(photoId)
            .orElseThrow(() -> new PhotoNotFoundException("Photo not found"));

        int likeCount = likeRepository.countByPhotoId(photoId);
        GridFsResource photoStream = photoStorageService.getPhoto(metadata.getFileId());

        return PhotoMapper.toPhotoDto(metadata, photoStream.getInputStream(), likeCount);
    }

    public boolean addLike(Long userId, Long photoId) {
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

    public boolean removeLike(Long userId, Long photoId) {
        Optional<Like> like = likeRepository.findByUserIdAndPhotoId(userId, photoId);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return true;
        }
        return false; // L'utente non ha messo un "like"
    }
}

