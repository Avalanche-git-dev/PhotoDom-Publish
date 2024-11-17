package com.app.photoservice.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.photoservice.dto.PhotoDto;
import com.app.photoservice.service.PhotoService;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;
    @PostMapping("/upload")
    public ResponseEntity<PhotoDto> uploadPhoto(
            @RequestParam MultipartFile file,
            @RequestHeader("Authorization") String token) {
    	
    	
        try {
            PhotoDto savedPhoto = photoService.savePhoto(file, token);
            return ResponseEntity.ok(savedPhoto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDto> getPhotoMetadata(@PathVariable Long id) throws IllegalStateException, IOException {
        PhotoDto photoDto = photoService.getPhotoDto(id);
        return ResponseEntity.ok(photoDto);
    }

    @PostMapping("/like")
    public ResponseEntity<String> likePhoto(@RequestParam Long userId, @RequestParam Long photoId) {
        if (photoService.addLike(userId, photoId)) {
            return ResponseEntity.ok("Like added successfully");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Like already exists");
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<String> unlikePhoto(@RequestParam Long userId, @RequestParam Long photoId) {
        if (photoService.removeLike(userId, photoId)) {
            return ResponseEntity.ok("Like removed successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Like not found");
    }
}

