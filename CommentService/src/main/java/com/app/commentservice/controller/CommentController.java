package com.app.commentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.commentservice.entity.Comment;
import com.app.commentservice.service.CommentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Mono<ResponseEntity<Comment>> addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment)
                .map(savedComment -> ResponseEntity.status(HttpStatus.CREATED).body(savedComment));
    }

    @GetMapping("/photo/{photoId}")
    public Flux<Comment> getCommentsByPhoto(@PathVariable String photoId) {
        return commentService.getCommentsByPhotoId(photoId);
    }

    @GetMapping("/replies/{commentId}")
    public Flux<Comment> getReplies(@PathVariable String commentId) {
        return commentService.getReplies(commentId);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable String id) {
        return commentService.deleteComment(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
