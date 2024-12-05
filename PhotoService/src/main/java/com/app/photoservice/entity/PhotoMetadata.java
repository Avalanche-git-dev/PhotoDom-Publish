package com.app.photoservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "photo_metadata", schema = "photo_service_db")
public class PhotoMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false, unique = true)
    private String fileId; 

    @Column(nullable = false)
    private Long userId; 
    
    @Column(nullable = false)
    private Date uploadDate;
    
    
    @Column(nullable = false,columnDefinition = "integer default 0")
    private int likeCount = 0; 

    public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	@OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();


	public PhotoMetadata(Long id, String filename, String contentType, Long size, String fileId, Long userId,
			Date uploadDate, int likeCount, List<Like> likes) {
		super();
		this.id = id;
		this.filename = filename;
		this.contentType = contentType;
		this.size = size;
		this.fileId = fileId;
		this.userId = userId;
		this.uploadDate = uploadDate;
		this.likeCount = likeCount;
		this.likes = likes;
	}

	public PhotoMetadata() {
		super();
	}
	
	

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Like> getLikes() {
		return likes;
	}

	public void setLikes(List<Like> likes) {
		this.likes = likes;
	}

	@Override
	public String toString() {
		return "PhotoMetadata [id=" + id + ", filename=" + filename + ", contentType=" + contentType + ", size=" + size
				+ ", fileId=" + fileId + ", userId=" + userId + ", uploadDate=" + uploadDate + ", likeCount="
				+ likeCount + "]";
	}

    
	
	
    
    
}
