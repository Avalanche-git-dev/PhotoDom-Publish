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

    @PostMapping
    public Mono<ResponseEntity<Comment>> addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment)
                .map(savedComment -> ResponseEntity.status(HttpStatus.CREATED).body(savedComment));
    }

    @GetMapping("/{id}")
    public Mono<CommentDto> getCommentById(@PathVariable String id) {
        return commentService.getCommentById(id)
                             .map(CommentMapper::toDto);
    }

    @GetMapping("/photo/{id}")
    public Flux<CommentDto> getCommentsByPhoto(@PathVariable String id) {
        return commentService.getCommentsByPhotoId(id)
                             .map(CommentMapper::toDto);
    }

    @GetMapping("/{id}/replies")
    public Flux<CommentDto> getReplies(@PathVariable String id) {
        return commentService.getReplies(id)
                             .map(CommentMapper::toDto);
    }

    @GetMapping("/batch")
    public Flux<CommentDto> getCommentsByIds(@RequestParam List<String> ids) {
        return commentService.getCommentsByIds(ids)
                             .map(CommentMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable String id) {
        return commentService.deleteComment(id)
                             .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
