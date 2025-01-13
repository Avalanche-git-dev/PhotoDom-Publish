package com.app.photoservice.controller;

import java.util.Base64;

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
import com.app.photoservice.dto.PhotoResponse;
import com.app.photoservice.service.PhotoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

	@Autowired
	private PhotoService photoService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<ResponseEntity<PhotoResponse<String>>> uploadPhoto(@RequestPart("file") FilePart file) {
		System.out.println("file" + file.toString());
		System.out.println("fileContent" + file.content().toString());
		System.out.println("fileheader" + file.headers());
		return photoService.savePhoto(file).map(response -> ResponseEntity
				.ok(PhotoResponse.success("Foto caricata con successo!", HttpStatus.OK, response)));
	}

	@GetMapping("/photo")
	public Mono<ResponseEntity<PhotoResponse<PhotoDto>>> getPhotoById(@RequestParam Long photoId) {
		return photoService.getPhotoById(photoId).map(photoDto -> {
			String imageBase64 = Base64.getEncoder().encodeToString(photoDto.getImageBytes());
			photoDto.setImageBase64(imageBase64);
			photoDto.setImageBytes(null);
			return ResponseEntity.ok(PhotoResponse.success("Foto trovata", HttpStatus.OK, photoDto));
		});
	}

	@GetMapping("/data/photo")
	public Mono<ResponseEntity<PhotoResponse<PhotoDto>>> getPhotoMetadata(@RequestParam Long photoId) {
		return photoService.getPhotoMetadata(photoId).map(
				photoDto -> ResponseEntity.ok(PhotoResponse.success("Metadata recuperati", HttpStatus.OK, photoDto)));
	}

	@PostMapping("/like/add")
	public Mono<PhotoResponse<Void>> addLike(@RequestParam Long photoId) {
		return photoService.addLike(photoId).map(result -> {
			if (result) {
				return PhotoResponse.success("Like aggiunto!", HttpStatus.OK);
			} else {
				return PhotoResponse.failure("Like già esistente.", HttpStatus.BAD_REQUEST);
			}
		});
	}

	@DeleteMapping("/like/remove")
	public Mono<PhotoResponse<Void>> removeLike(@RequestParam Long photoId) {
		return photoService.removeLike(photoId).map(result -> {
			if (result) {
				return PhotoResponse.success("Like rimosso!", HttpStatus.OK);

			} else {
				return PhotoResponse.failure("Like non esistente.", HttpStatus.BAD_REQUEST);

			}
		});
	}

	@GetMapping("/like/status")
	public Mono<PhotoResponse<Void>> getLikeStatus(@RequestParam Long photoId) {
		return photoService.getLikeStatus(photoId).map(isLiked -> {
			if (isLiked) {
				return PhotoResponse.success("Like exists", HttpStatus.OK);
			} else {
				return PhotoResponse.failure("Like does not exist", HttpStatus.NOT_FOUND);
			}
		});

	}

	@GetMapping("/full/batch")
	public Flux<PhotoDto> getPhotosBatch(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size) {
		return photoService.getPhotosBatch(page, size).map(photoDto -> {
			String imageBase64 = Base64.getEncoder().encodeToString(photoDto.getImageBytes());
			photoDto.setImageBase64(imageBase64);
			photoDto.setImageBytes(null);
			return photoDto;
		});
	}

	@GetMapping("/full/user/batch")
	public Flux<PhotoDto> getPhotosBatchByUser(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size) {
		return photoService.getPhotosBatchByUser(page, size).map(photoDto -> {
			String imageBase64 = Base64.getEncoder().encodeToString(photoDto.getImageBytes());
			photoDto.setImageBase64(imageBase64);
			photoDto.setImageBytes(null);
			return photoDto;
		});
	}
	
	@GetMapping("/full/user/photos")
	public Flux<PhotoDto> getPhotosByUser(
	        @RequestParam Long userId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "15") int size) {
	    return photoService.getPhotosByUser(userId, page, size).map(photoDto -> {
	        String imageBase64 = Base64.getEncoder().encodeToString(photoDto.getImageBytes());
	        photoDto.setImageBase64(imageBase64);
	        photoDto.setImageBytes(null);
	        return photoDto;
	    });
	}


	@GetMapping("/metadata/user/batch")
	public Flux<PhotoDto> getPhotoMetadataBatchByUser(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size) {
		return photoService.getPhotoMetadataBatchByUser(page, size);
	}

	@GetMapping("/metadata/batch")
	public Flux<PhotoDto> getPhotoMetadataBatch(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size) {
		return photoService.getPhotoMetadataBatch(page, size);
	}
	
	
	@DeleteMapping("/delete")
	public Mono<PhotoResponse<Void>> deletePhoto(@RequestParam Long photoId) {
	    return photoService.deletePhoto(photoId)
	            .map(success -> PhotoResponse.success("Foto eliminata con successo!", HttpStatus.OK));
	}

	
	

}
