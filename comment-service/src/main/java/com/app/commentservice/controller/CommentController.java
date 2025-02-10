package com.app.commentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.commentservice.entity.Comment;
import com.app.commentservice.model.CommentDto;
import com.app.commentservice.model.CommentMapper;
import com.app.commentservice.model.CommentResponse;
import com.app.commentservice.service.CommentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@GetMapping("/comment")
	public Mono<CommentDto> getCommentById(@RequestParam String id) {
		return commentService.getCommentById(id).map(CommentMapper::toDto);
	}

	@GetMapping("/comment/photo")
	public Flux<CommentDto> getCommentsByPhoto(@RequestParam String photoId) {
		return commentService.getCommentsByPhotoId(photoId).map(CommentMapper::toDto);
	}

	@GetMapping("/comment/replies")
	public Flux<CommentDto> getReplies(@RequestParam String id) {
		return commentService.getReplies(id).map(CommentMapper::toDto);
	}

	@GetMapping("/comment/user")
	public Flux<CommentDto> getCommentsByUser(@RequestParam String userId) {
		return commentService.getCommentsByUserId(userId).map(CommentMapper::toDto);
	}

	@PostMapping("/comment/add")
	public Mono<CommentResponse<String>> addComment(@RequestBody Comment comment) {
		return commentService.addComment(comment).map(savedComment -> CommentResponse
				.success("Comment added successfully", HttpStatus.CREATED, savedComment.getId()));
	}

	@DeleteMapping("/comment/delete")
	public Mono<CommentResponse<Void>> deleteComment(@RequestParam String id) {
		return commentService.deleteComment(id)
				.then(Mono.just(CommentResponse.success("Comment deleted successfully", HttpStatus.OK)));
	}

	@GetMapping("/count/comments")
	public Mono<CommentResponse<Long>> countMainComments(@RequestParam String photoId) {
		return commentService.countMainComments(photoId).map(count -> CommentResponse
				.success("Count of main comments retrieved successfully", HttpStatus.OK, count));
	}

	@GetMapping("/count/replies")
	public Mono<CommentResponse<Long>> countReplies(@RequestParam String parentCommentId) {
		return commentService.countReplies(parentCommentId)
				.map(count -> CommentResponse.success("Count of replies retrieved successfully", HttpStatus.OK, count));
	}

}
