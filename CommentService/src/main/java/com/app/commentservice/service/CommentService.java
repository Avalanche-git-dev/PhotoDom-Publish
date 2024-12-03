package com.app.commentservice.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.commentservice.entity.Comment;
import com.app.commentservice.repository.CommentRepository;
import com.app.commentservice.utils.UserContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//    
//    
//    @Autowired
//    private UserContext userContext;
//
//
//    
//    public Mono<Comment> addComment(Comment comment) {
//        comment.setCreatedDate(new Date());
//
//        return commentRepository.save(comment)
//                .flatMap(savedComment -> {
//                    if (savedComment.getParentCommentId() != null) {
//                        // Trova il commento padre e aggiungi la risposta
//                        return commentRepository.findById(savedComment.getParentCommentId())
//                                .flatMap(parentComment -> {
//                                    parentComment.addReply(savedComment); // Aggiungi la risposta con l'ID univoco
//                                    return commentRepository.save(parentComment).thenReturn(savedComment);
//                                });
//                    }
//                    return Mono.just(savedComment);
//                });
//    }
//
//
//
//    
//    public Mono<Comment> getCommentById(String commentId) {
//        return commentRepository.findById(commentId);
//    }
//
//    public Flux<Comment> getCommentsByPhotoId(String photoId) {
//        return commentRepository.findByPhotoId(photoId);
//    }
//
//    public Flux<Comment> getReplies(String commentId) {
//        return commentRepository.findByParentCommentId(commentId);
//    }
//
//    public Mono<Void> deleteComment(String commentId) {
//        return commentRepository.deleteById(commentId);
//    }
//    
//    
//    
//    public Flux<Comment> getCommentsByIds(List<String> ids) {
//        return Flux.fromIterable(ids)
//                   .flatMap(commentRepository::findById);
//    }
//
//}


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserContext userContext;

    public Mono<Comment> addComment(Comment comment) {
        comment.setCreatedDate(new Date());

        // Recupera l'ID utente e assegna dinamicamente al commento
        return userContext.getCurrentUserId()
            .flatMap(userId -> {
                comment.setUserId(String.valueOf(userId)); // Assegna l'utente corrente al commento
                return commentRepository.save(comment)
                    .flatMap(savedComment -> {
                        if (savedComment.getParentCommentId() != null) {
                            // Gestisce i commenti come risposte
                            return commentRepository.findById(savedComment.getParentCommentId())
                                .flatMap(parentComment -> {
                                    parentComment.addReply(savedComment);
                                    return commentRepository.save(parentComment).thenReturn(savedComment);
                                });
                        }
                        return Mono.just(savedComment);
                    });
            });
    }

    public Mono<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Commento non trovato")));
    }

    public Flux<Comment> getCommentsByPhotoId(String photoId) {
        return commentRepository.findByPhotoId(photoId)
                .switchIfEmpty(Flux.error(new IllegalArgumentException("Nessun commento trovato per questa foto")));
    }

    public Flux<Comment> getReplies(String commentId) {
        return commentRepository.findByParentCommentId(commentId)
                .switchIfEmpty(Flux.error(new IllegalArgumentException("Nessuna risposta trovata per questo commento")));
    }

    public Mono<Void> deleteComment(String commentId) {
        return commentRepository.findById(commentId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Commento non trovato")))
            .flatMap(existingComment -> commentRepository.deleteById(commentId));
    }

    public Flux<Comment> getCommentsByIds(List<String> ids) {
        return Flux.fromIterable(ids)
            .flatMap(commentRepository::findById)
            .switchIfEmpty(Flux.error(new IllegalArgumentException("Alcuni commenti non sono stati trovati")));
    }
}


