package com.app.photoservice.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.app.photoservice.dto.PhotoDto;
import com.app.photoservice.service.PhotoService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;
    
    
  
    
    
//    @PostMapping("/upload")
//    public Mono<ResponseEntity<PhotoDto>> uploadPhoto(
//            @RequestPart("file") Mono<Part> filePart,
//            @RequestHeader("Authorization") String token) {
//
//        return filePart
//                .cast(FilePart.class) // Cast to FilePart to handle file operations
//                .flatMap(file -> {
//                    try {
//                        return photoService.savePhoto(file, token)
//                                           .map(savedPhoto -> ResponseEntity.ok(savedPhoto));
//                    } catch (IOException e) {
//                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
//                    }
//                });
//    }
    
    
    
    @PostMapping("/upload")
    public Mono<ResponseEntity<PhotoDto>> uploadPhoto(
            @RequestPart("file") Mono<Part> filePart,
            @RequestHeader("Authorization") String tokenID) {
    	String token = tokenID.replace("Bearer ", "").trim();

        return filePart
                .cast(FilePart.class) // Cast a FilePart per gestire le operazioni sul file
                .flatMap(file -> photoService.savePhoto(file, token)
                        .map(savedPhoto -> ResponseEntity.ok(savedPhoto)));
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<PhotoDto> getPhotoMetadata(@PathVariable Long id) throws IllegalStateException, IOException {
//        PhotoDto photoDto = photoService.getPhotoDto(id);
//        return ResponseEntity.ok(photoDto);
//    }

    
    
    @GetMapping("/{id}")
    public Mono<ResponseEntity<byte[]>> getPhoto(@PathVariable Long id) {
        return photoService.getPhotoById(id)
                .flatMap(photoDto -> {
                    try {
                        byte[] imageBytes = photoDto.getPhotoStream().readAllBytes();
                        return Mono.just(
                                ResponseEntity.ok()
                                        .contentType(MediaType.parseMediaType(photoDto.getContentType()))
                                        .header("Filename", photoDto.getFilename())
                                        .header("Size", String.valueOf(photoDto.getSize()))
                                        .header("UserId", String.valueOf(photoDto.getUserId()))
                                        .header("LikeCount", String.valueOf(photoDto.getLikeCount()))
                                        .body(imageBytes)
                        );
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Error reading photo data", e));
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }



//
//    @PostMapping("/like")
//    public ResponseEntity<String> likePhoto(@RequestParam Long userId, @RequestParam Long photoId) {
//        if (photoService.addLike(userId, photoId)) {
//            return ResponseEntity.ok("Like added successfully");
//        }
//        return ResponseEntity.status(HttpStatus.CONFLICT).body("Like already exists");
//    }
//
//    @DeleteMapping("/unlike")
//    public ResponseEntity<String> unlikePhoto(@RequestParam Long userId, @RequestParam Long photoId) {
//        if (photoService.removeLike(userId, photoId)) {
//            return ResponseEntity.ok("Like removed successfully");
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Like not found");
//    }
    
    
    
    @PostMapping("/{id}/like/a")
    public ResponseEntity<String> addLike(
            @PathVariable Long id,
            @RequestHeader("Authorization") String tokenID) {
        String token = tokenID.replace("Bearer ", "").trim();

        boolean likeAdded = photoService.addLike(token, id);
        if (likeAdded) {
            return ResponseEntity.ok("Like added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User has already liked this photo");
        }
    }

    @DeleteMapping("/{id}/like/r")
    public ResponseEntity<String> removeLike(
            @PathVariable Long id,
            @RequestHeader("Authorization") String tokenID) {
        String token = tokenID.replace("Bearer ", "").trim();

        boolean likeRemoved = photoService.removeLike(token, id);
        if (likeRemoved) {
            return ResponseEntity.ok("Like removed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Like not found");
        }
    }
}

