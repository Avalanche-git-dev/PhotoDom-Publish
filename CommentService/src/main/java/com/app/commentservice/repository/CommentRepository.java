package com.app.commentservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.app.commentservice.entity.Comment;

import reactor.core.publisher.Flux;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findByPhotoId(String photoId);
    Flux<Comment> findByUserId(String userId);
    Flux<Comment> findByParentCommentId(String parentCommentId);
}
