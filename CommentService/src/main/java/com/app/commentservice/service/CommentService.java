package com.app.commentservice.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.commentservice.entity.Comment;
import com.app.commentservice.repository.CommentRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Mono<Comment> addComment(Comment comment) {
        comment.setCreatedDate(new Date());
        return commentRepository.save(comment);
    }

    public Flux<Comment> getCommentsByPhotoId(String photoId) {
        return commentRepository.findByPhotoId(photoId);
    }

    public Flux<Comment> getReplies(String commentId) {
        return commentRepository.findByParentCommentId(commentId);
    }

    public Mono<Void> deleteComment(String commentId) {
        return commentRepository.deleteById(commentId);
    }
    
    

}

