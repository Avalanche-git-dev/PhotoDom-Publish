package com.app.commentservice.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.app.commentservice.entity.Comment;
import com.app.commentservice.exception.CommentNotFoundException;
import com.app.commentservice.repository.CommentRepository;
import com.app.commentservice.socket.CommentWebSocketHandler;
import com.app.commentservice.utils.UserContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentService {

	private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private UserContext userContext;

	@Autowired
	private CommentWebSocketHandler commentWebSocketHandler;

	@CacheEvict(value = { "photo:comments", "replies", "user:comments" }, allEntries = true)
	public Mono<Comment> addComment(Comment comment) {
		comment.setCreatedDate(new Date());

		return userContext.getCurrentUserId().flatMap(userId -> {
			comment.setUserId(String.valueOf(userId));
			logger.info("Tentativo di aggiungere un commento da parte dell'utente: {}", userId);

			if (comment.getParentCommentId() != null) {
				// È una risposta
				return commentRepository.findById(comment.getParentCommentId()).flatMap(parentComment -> {
					parentComment.addReply(comment);
					return commentRepository.save(comment) // Salva la risposta
							.flatMap(savedComment -> commentRepository.save(parentComment) // Aggiorna il padre
									.thenReturn(savedComment))
							.doOnSuccess(savedComment -> {
								commentWebSocketHandler.notifyCommentUpdate("Reply added: " + savedComment.getId());
								logger.info("Risposta salvata con successo con ID: {}", savedComment.getId());
							});
				});
			}

			// È un commento principale
			return commentRepository.save(comment).doOnSuccess(savedComment -> {
				commentWebSocketHandler.notifyCommentUpdate("Comment added: " + savedComment.getId());
				logger.info("Commento principale salvato con successo con ID: {}", savedComment.getId());
			});
		}).doOnError(error -> logger.error("Errore durante l'aggiunta del commento: {}", error.getMessage()));
	}

	@Cacheable(value = "comments", key = "'comments:' + #commentId")
	public Mono<Comment> getCommentById(String commentId) {
		logger.info("Recupero commento con ID: {}", commentId);
		return commentRepository.findById(commentId)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Commento non trovato")));
	}

	@Cacheable(value = "photo:comments", key = "'photo:comments:' + #photoId")
	public Flux<Comment> getCommentsByPhotoId(String photoId) {
		logger.info("Recupero commenti per photoId: {}", photoId);
		return commentRepository.findByPhotoId(photoId).filter(comment -> comment.getParentCommentId() == null)
				.switchIfEmpty(Flux.error(new IllegalArgumentException("Nessun commento trovato per questa foto")));
	}

	@Cacheable(value = "user:comments", key = "'user:comments:' + #userId")
	public Flux<Comment> getCommentsByUserId(String userId) {
		logger.info("Recupero commenti per userId: {}", userId);
		return commentRepository.findByUserId(userId)
				.switchIfEmpty(Flux.error(new IllegalArgumentException("Nessun commento trovato per questo utente")));
	}

	@CacheEvict(value = { "comments", "photo:comments", "replies", "user:comments" }, allEntries = true)
	public Mono<Void> deleteComment(String commentId) {
		logger.info("Eliminazione commento con ID: {}", commentId);

		return commentRepository.findById(commentId)
				.switchIfEmpty(
						Mono.error(new CommentNotFoundException("Commento con ID " + commentId + " non trovato.")))
				.flatMap(comment -> {
					// Rimuovi il commento dalla lista di risposte del genitore, se esiste
					if (comment.getParentCommentId() != null) {
						return commentRepository.findById(comment.getParentCommentId()).flatMap(parentComment -> {
							parentComment.getReplyIds().remove(commentId);
							return commentRepository.save(parentComment).then(deleteCommentRecursively(commentId));
						});
					}
					return deleteCommentRecursively(commentId);
				}).doOnSuccess(unused -> logger.info("Commento eliminato con successo: {}", commentId))
				.doOnTerminate(() -> {
					commentWebSocketHandler.notifyCommentUpdate("Comment deleted: " + commentId);
					logger.info("Notifica WebSocket inviata per commento eliminato: {}", commentId);
				})
				.doOnError(error -> logger.error("Errore durante l'eliminazione del commento: {}", error.getMessage()));
	}

	// Metodo ricorsivo per cancellare il commento e le sue risposte
	private Mono<Void> deleteCommentRecursively(String commentId) {
		return commentRepository.findById(commentId).flatMap(comment -> {
			Flux<Void> deleteReplies = Flux.fromIterable(comment.getReplyIds()).flatMap(this::deleteCommentRecursively);
			return deleteReplies.then(commentRepository.deleteById(commentId)).doOnTerminate(() -> {
				commentWebSocketHandler.notifyCommentUpdate("Comment deleted: " + commentId);
				logger.info("Notifica WebSocket inviata per eliminazione ricorsiva di commento: {}", commentId);
			});
		});
	}

	@Cacheable(value = "replies", key = "'replies:' + #commentId")
	public Flux<Comment> getReplies(String commentId) {
		logger.info("Recupero risposte per commentId: {}", commentId);

		return commentRepository.findById(commentId).flatMapMany(parentComment -> {
			if (parentComment.getReplyIds() == null || parentComment.getReplyIds().isEmpty()) {
				return Flux.error(new IllegalArgumentException("Nessuna risposta trovata per questo commento"));
			}
			return Flux.fromIterable(parentComment.getReplyIds())
					.flatMap(replyId -> commentRepository.findById(replyId))
					.switchIfEmpty(Flux.error(new IllegalArgumentException("Risposte non trovate per alcuni ID")));
		}).switchIfEmpty(Flux.error(new IllegalArgumentException("Commento padre non trovato con ID: " + commentId)));
	}

	public Mono<Long> countMainComments(String photoId) {
		return commentRepository.countByPhotoIdAndParentCommentIdIsNull(photoId)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Photo ID not found.")));
	}

	public Mono<Long> countReplies(String parentCommentId) {
		return commentRepository.countByParentCommentId(parentCommentId)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Parent comment ID not found.")));
	}

}
