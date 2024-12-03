package com.app.commentservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.commentservice.entity.Comment;
import com.app.commentservice.model.CommentDto;
import com.app.commentservice.model.CommentMapper;
import com.app.commentservice.service.CommentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment/add")
    public Mono<ResponseEntity<Comment>> addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment)
                .map(savedComment -> ResponseEntity.status(HttpStatus.CREATED).body(savedComment));
    }

    @GetMapping("/comment")
    public Mono<CommentDto> getCommentById(@RequestParam String id) {
        return commentService.getCommentById(id)
                             .map(CommentMapper::toDto);
    }

    @GetMapping("/comment/photo")
    public Flux<CommentDto> getCommentsByPhoto(@RequestParam String photoId) {
        return commentService.getCommentsByPhotoId(photoId)
                             .map(CommentMapper::toDto);
    }

    @GetMapping("/comment/replies")
    public Flux<CommentDto> getReplies(@RequestParam String id) {
        return commentService.getReplies(id)
                             .map(CommentMapper::toDto);
    }

    @GetMapping("/batch")
    public Flux<CommentDto> getCommentsByIds(@RequestParam List<String> ids) {
        return commentService.getCommentsByIds(ids)
                             .map(CommentMapper::toDto);
    }

    @DeleteMapping("/comment/delete")
    public Mono<ResponseEntity<Void>> deleteComment(@RequestParam String id) {
        return commentService.deleteComment(id)
                             .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
