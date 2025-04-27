package com.app.photoservice.entity;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "likes", schema = "photo_service_db")
public class Like {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId; 

	@ManyToOne
	@JoinColumn(name = "photo_id", nullable = false)
	private PhotoMetadata photo;

	public Like() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Like(Long id, Long userId, PhotoMetadata photo) {
		super();
		this.id = id;
		this.userId = userId;
		this.photo = photo;
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

	public PhotoMetadata getPhoto() {
		return photo;
	}

	public void setPhoto(PhotoMetadata photo) {
		this.photo = photo;
	}

}
