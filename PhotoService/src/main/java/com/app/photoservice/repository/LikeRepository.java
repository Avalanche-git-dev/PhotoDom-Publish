package com.app.photoservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.photoservice.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPhotoId(Long userId, Long photoId);
    int countByPhotoId(Long photoId);
}

