package com.app.photoservice.dto;

import com.app.photoservice.entity.PhotoMetadata;

public class PhotoMapper {

    public static CommentDto toPhotoDto(PhotoMetadata photoMetadata, byte[] imageBytes, int likeCount) {
        CommentDto photoDto = new CommentDto();
        photoDto.setId(photoMetadata.getId());
        photoDto.setUserId(photoMetadata.getUserId());
        photoDto.setFilename(photoMetadata.getFilename());
        photoDto.setContentType(photoMetadata.getContentType());
        photoDto.setSize(photoMetadata.getSize());
        photoDto.setImageBytes(imageBytes);
        photoDto.setLikeCount(likeCount);
        return photoDto;
    }

    public static PhotoMetadata toPhotoMetadata(CommentDto photoDto, String fileId) {
        PhotoMetadata photoMetadata = new PhotoMetadata();
        photoMetadata.setUserId(photoDto.getUserId());
        photoMetadata.setFilename(photoDto.getFilename());
        photoMetadata.setContentType(photoDto.getContentType());
        photoMetadata.setSize(photoDto.getSize());
        photoMetadata.setFileId(fileId);
        photoMetadata.setLikeCount(photoDto.getLikeCount());
        return photoMetadata;
    }
    
    
    
    public static CommentDto toPhotoDto(PhotoMetadata photoMetadata, byte[] imageBytes) {
        return toPhotoDto(photoMetadata, imageBytes, photoMetadata.getLikeCount());
    }
}
