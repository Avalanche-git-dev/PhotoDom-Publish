package com.app.photoservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.photoservice.entity.PhotoMetadata;

public interface PhotoMetadataRepository extends JpaRepository<PhotoMetadata, Long> {

	List<PhotoMetadata> findAllByOrderByLikesDesc();
	Page<PhotoMetadata> findByUserId(Long userId, Pageable pageable);
	
	
}

