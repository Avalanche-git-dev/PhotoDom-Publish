package com.app.photoservice.controller;


import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.app.photoservice.dto.PhotoDto;
import com.app.photoservice.service.PhotoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    
    
    
    @PostMapping("/upload")
    public Mono<ResponseEntity<String>> uploadPhoto(@RequestPart("file") FilePart file) {
        return photoService.savePhoto(file)
            .map(response -> ResponseEntity.ok(response))
            .onErrorResume(e -> Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore: " + e.getMessage())
            ));
    }



    @GetMapping("/photo")
    public Mono<ResponseEntity<byte[]>> getPhotoById(@RequestParam Long photoId) {
        return photoService.getPhotoById(photoId)
                .map(photoDto -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(photoDto.getContentType()))
                        .body(photoDto.getImageBytes()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    

    @GetMapping("/data/photo")
    public Mono<ResponseEntity<PhotoDto>> getPhotoMetadata(@RequestParam Long photoId) {
        return photoService.getPhotoMetadata(photoId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    
    
    @PostMapping("/like/add")
    public Mono<ResponseEntity<Map<String, String>>> addLike(@RequestParam Long photoId) {
        return photoService.addLike(photoId)
                .map(result -> {
                    Map<String, String> response = new HashMap<>();
                    if (result) {
                        response.put("message", "Like aggiunto!");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("message", "Like già esistente.");
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    
    @DeleteMapping("/like/remove")
    public Mono<ResponseEntity<Map<String, String>>> removeLike(@RequestParam Long photoId) {
        return photoService.removeLike(photoId)
                .map(result -> {
                    Map<String, String> response = new HashMap<>();
                    if (result) {
                        response.put("message", "Like rimosso!");
                        return ResponseEntity.ok(response);
                    } else {
                        response.put("message", "Like non esistente.");
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    
    

    @GetMapping("/full/batch")
    public Flux<PhotoDto> getPhotosBatch(
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "15") int size) {
        return photoService.getPhotosBatch(page, size).map(photoDto -> {
           
            String imageBase64 = Base64.getEncoder().encodeToString(photoDto.getImageBytes());
            photoDto.setImageBase64(imageBase64); 
            photoDto.setImageBytes(null); 
            return photoDto;
        });
    }
    
    
    
    
    @GetMapping("/full/user/batch")
    public Flux<PhotoDto> getPhotosBatchByUser(
    		@RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "15") int size) {
        return photoService.getPhotosBatchByUser(page, size)
                .map(photoDto -> {
                    // Codifica l'immagine in Base64
                    String imageBase64 = Base64.getEncoder().encodeToString(photoDto.getImageBytes());
                    photoDto.setImageBase64(imageBase64); 
                    photoDto.setImageBytes(null); 
                    return photoDto;
                });
    }
    

  
    
    
    
    @GetMapping("/metadata/user/batch")
    public Flux<PhotoDto> getPhotoMetadataBatchByUser(
    		@RequestParam(defaultValue = "0")int page,
    		@RequestParam(defaultValue = "15") int size) {
        return photoService.getPhotoMetadataBatchByUser(page, size);
    }
    
    @GetMapping("/metadata/batch")
    public Flux<PhotoDto> getPhotoMetadataBatch(
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "15") int size) {
        return photoService.getPhotoMetadataBatch(page, size);
    }

    
}
