package com.app.photoservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
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

import com.app.photoservice.dto.CommentDto;
import com.app.photoservice.service.PhotoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


//
//@RestController
//@RequestMapping("/api/photos")
//public class PhotoController {
//
//    @Autowired
//    private PhotoService photoService;
//
//    @PostMapping("/upload")
//    public Mono<ResponseEntity<PhotoDto>> uploadPhoto(@RequestPart("file") FilePart file) {
//        return photoService.savePhoto(file)
//                .map(ResponseEntity::ok)
//                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
//    }
//
////    @GetMapping("/photo")
////    public Mono<ResponseEntity<PhotoDto>> getPhotoById(@RequestParam Long photoId) {
////        return photoService.getPhotoById(photoId)
////                .map(ResponseEntity::ok)
////                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
////    }
//    
//    
//    @GetMapping("/photo")
//    public Mono<ResponseEntity<byte[]>> getPhotoById(@RequestParam Long photoId) {
//        return photoService.getPhotoById(photoId)
//                .map(photoDto -> {
//                    try {
//                        byte[] imageBytes = photoDto.getImageBytes();
//                        return ResponseEntity.ok()
//                                .contentType(MediaType.parseMediaType(photoDto.getContentType()))
//                                .header("Filename", photoDto.getFilename())
//                                .header("Size", String.valueOf(photoDto.getSize()))
//                                .header("UserId", String.valueOf(photoDto.getUserId()))
//                                .header("LikeCount", String.valueOf(photoDto.getLikeCount()))
//                                .body(imageBytes);
//                    } catch (Exception e) {
//                        throw new RuntimeException("Errore durante la lettura dei dati della foto", e);
//                    }
//                });
//    }
//
//
//
//    
//@GetMapping("/data/photo")
//public ResponseEntity<PhotoDto> getPhotoMetadata(@RequestParam Long photoId) throws IllegalStateException, IOException {
//    PhotoDto photoDto = photoService.getPhotoDto(photoId);
//    return ResponseEntity.ok(photoDto);
//}
//    
//    
//    
//    
//
//
//    
//    
//    
//    @PostMapping("/like/add")
//    public Mono<ResponseEntity<String>> addLike(@RequestParam Long photoId) {
//        return photoService.addLike(photoId)
//                .map(result -> result
//                        ? ResponseEntity.ok("Like aggiunto!")
//                        : ResponseEntity.badRequest().body("Like già esistente."));
//    }
//    
//    
//    
//    @DeleteMapping("/like/remove")
//    public Mono<ResponseEntity<String>> removeLike(@RequestParam Long photoId) {
//        return photoService.removeLike(photoId)
//                .map(result -> result
//                        ? ResponseEntity.ok("Like rimosso!")
//                        : ResponseEntity.badRequest().body("Like non esistente."));
//    }
//
//
//}
//








@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/upload")
    public Mono<ResponseEntity<CommentDto>> uploadPhoto(@RequestPart("file") FilePart file) {
        return photoService.savePhoto(file)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
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
    public Mono<ResponseEntity<CommentDto>> getPhotoMetadata(@RequestParam Long photoId) {
        return photoService.getPhotoMetadata(photoId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/like/add")
    public Mono<ResponseEntity<String>> addLike(@RequestParam Long photoId) {
        return photoService.addLike(photoId)
                .map(result -> result
                        ? ResponseEntity.ok("Like aggiunto!")
                        : ResponseEntity.badRequest().body("Like già esistente."));
    }

    @DeleteMapping("/like/remove")
    public Mono<ResponseEntity<String>> removeLike(@RequestParam Long photoId) {
        return photoService.removeLike(photoId)
                .map(result -> result
                        ? ResponseEntity.ok("Like rimosso!")
                        : ResponseEntity.badRequest().body("Like non esistente."));
    }
    
    
    
    @GetMapping("/metadata/batch")
    public Flux<CommentDto> getPhotoMetadataBatch(
            @RequestParam int page,
            @RequestParam int size) {
        return photoService.getPhotoMetadataBatch(page, size);
    }

    // Nuovo metodo: Ottieni batch completo di foto con immagini
    @GetMapping("/full/batch")
    public Flux<CommentDto> getPhotosBatch(
            @RequestParam int page,
            @RequestParam int size) {
        return photoService.getPhotosBatch(page, size);
    }

    // Nuovo metodo: Ottieni batch di metadati di foto per utente
    @GetMapping("/metadata/user/batch")
    public Flux<CommentDto> getPhotoMetadataBatchByUser(
            @RequestParam int page,
            @RequestParam int size) {
        return photoService.getPhotoMetadataBatchByUser(page, size);
    }

    // Nuovo metodo: Ottieni batch completo di foto per utente
    @GetMapping("/full/user/batch")
    public Flux<CommentDto> getPhotosBatchByUser(
            @RequestParam int page,
            @RequestParam int size) {
        return photoService.getPhotosBatchByUser(page, size);
    }
    
    
}
