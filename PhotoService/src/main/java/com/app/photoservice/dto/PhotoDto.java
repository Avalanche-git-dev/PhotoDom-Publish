package com.app.photoservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoDto {
    private Long id;
    private Long userId;
    private String filename;
    private String contentType;
    private Long size;

    private byte[] imageBytes;
    private int likeCount; 
 
   private String imageBase64;

    public String getImageBase64() {
	return imageBase64;
}


public void setImageBase64(String imageBase64) {
	this.imageBase64 = imageBase64;
}


	public PhotoDto() {
		super();
	}
    
    
	public PhotoDto(Long id, Long userId, String filename, String contentType, Long size,byte[] imageBytes,
			int likeCount) {
		super();
		this.id = id;
		this.userId = userId;
		this.filename = filename;
		this.contentType = contentType;
		this.size = size;
		this.imageBytes=imageBytes;
		this.likeCount = likeCount;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public byte[] getImageBytes() {
		return imageBytes;
	}
	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}


    

}

