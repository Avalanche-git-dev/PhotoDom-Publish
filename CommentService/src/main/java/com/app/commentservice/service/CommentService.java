package com.app.commentservice.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.app.commentservice.entity.Comment;
import com.app.commentservice.entity.CommentComponent;
import com.app.commentservice.repository.CommentRepository;
import com.app.commentservice.utils.UserContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//
//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private UserContext userContext;
//
//    public Mono<Comment> addComment(Comment comment) {
//        comment.setCreatedDate(new Date());
//
//        // Recupera l'ID utente e assegna dinamicamente al commento
//        return userContext.getCurrentUserId()
//            .flatMap(userId -> {
//                comment.setUserId(String.valueOf(userId)); // Assegna l'utente corrente al commento
//                return commentRepository.save(comment)
//                    .flatMap(savedComment -> {
//                        if (savedComment.getParentCommentId() != null) {
//                            // Gestisce i commenti come risposte
//                            return commentRepository.findById(savedComment.getParentCommentId())
//                                .flatMap(parentComment -> {
//                                    parentComment.addReply(savedComment);
//                                    return commentRepository.save(parentComment).thenReturn(savedComment);
//                                });
//                        }
//                        return Mono.just(savedComment);
//                    });
//            });
//    }
//
//    public Mono<Comment> getCommentById(String commentId) {
//        return commentRepository.findById(commentId)
//                .switchIfEmpty(Mono.error(new IllegalArgumentException("Commento non trovato")));
//    }
//
//    public Flux<Comment> getCommentsByPhotoId(String photoId) {
//        return commentRepository.findByPhotoId(photoId)
//                .switchIfEmpty(Flux.error(new IllegalArgumentException("Nessun commento trovato per questa foto")));
//    }
//
//    public Flux<Comment> getReplies(String commentId) {
//        return commentRepository.findByParentCommentId(commentId)
//                .switchIfEmpty(Flux.error(new IllegalArgumentException("Nessuna risposta trovata per questo commento")));
//    }
//
//    public Mono<Void> deleteComment(String commentId) {
//        return commentRepository.findById(commentId)
//            .switchIfEmpty(Mono.error(new IllegalArgumentException("Commento non trovato")))
//            .flatMap(existingComment -> commentRepository.deleteById(commentId));
//    }
//
//    public Flux<Comment> getCommentsByIds(List<String> ids) {
//        return Flux.fromIterable(ids)
//            .flatMap(commentRepository::findById)
//            .switchIfEmpty(Flux.error(new IllegalArgumentException("Alcuni commenti non sono stati trovati")));
//    }
//}
//
//



@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserContext userContext;

//    
//    @CacheEvict(value = {"photoComments", "replies", "userComments"}, allEntries = true) 
//    public Mono<Comment> addComment(Comment comment) {
//        comment.setCreatedDate(new Date());
//
//        return userContext.getCurrentUserId()
//            .flatMap(userId -> {
//                comment.setUserId(String.valueOf(userId));
//                
//                // Se il commento ha un parentCommentId, aggiorna il padre
//                if (comment.getParentCommentId() != null) {
//                    return commentRepository.findById(comment.getParentCommentId())
//                        .flatMap(parentComment -> {
//                            parentComment.addReply(comment); // Aggiungi la risposta al padre
//                            return commentRepository.save(parentComment) // Salva il padre aggiornato
//                                    .thenReturn(comment);
//                        });
//                }
//
//                // Se non ha un parentCommentId, salva come un nuovo commento
//                return commentRepository.save(comment);
//            });
//    }
    
    
    
    @CacheEvict(value = {"photoComments", "replies", "userComments"}, allEntries = true) 
    public Mono<Comment> addComment(Comment comment) {
        comment.setCreatedDate(new Date());

        return userContext.getCurrentUserId()
            .flatMap(userId -> {
                comment.setUserId(String.valueOf(userId));

                if (comment.getParentCommentId() != null) {
                    return commentRepository.findById(comment.getParentCommentId())
                        .flatMap(parentComment -> {
                            long currentSize = calculateDocumentSize(parentComment);

                            if (currentSize + calculateDocumentSize(comment) > 15.5 * 1024 * 1024) {
                                // Se il limite viene superato, crea un nuovo documento per la risposta
                                return commentRepository.save(comment);
                            } else {
                                // Aggiungi la risposta al commento padre
                                parentComment.addReply(comment);
                                return commentRepository.save(parentComment).thenReturn(comment);
                            }
                        });
                }
                

                // Se è un nuovo commento, salvalo
                return commentRepository.save(comment);
            });
    }



    @Cacheable(value = "comments", key = "#commentId")
    public Mono<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Commento non trovato")));
    }

    @Cacheable(value = "photoComments", key = "#photoId")
    public Flux<Comment> getCommentsByPhotoId(String photoId) {
        return commentRepository.findByPhotoId(photoId)
                .switchIfEmpty(Flux.error(new IllegalArgumentException("Nessun commento trovato per questa foto")));
    }

    @Cacheable(value = "replies", key = "#commentId")
    public Flux<Comment> getReplies(String commentId) {
        return commentRepository.findByParentCommentId(commentId)
                .switchIfEmpty(Flux.error(new IllegalArgumentException("Nessuna risposta trovata per questo commento")));
    }

    @CacheEvict(value = {"comments", "photoComments", "replies", "userComments"}, allEntries = true)
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

    @Cacheable(value = "userComments", key = "#userId")
    public Flux<Comment> getCommentsByUserId(String userId) {
        return commentRepository.findByUserId(userId)
                .switchIfEmpty(Flux.error(new IllegalArgumentException("Nessun commento trovato per questo utente")));
    }
    
    
    
    
    public long calculateDocumentSize(Comment comment) {
        long size = 0;

        // Calcola la lunghezza dei campi stringa
        size += comment.getId() != null ? comment.getId().getBytes().length : 0;
        size += comment.getContent() != null ? comment.getContent().getBytes().length : 0;
        size += comment.getPhotoId() != null ? comment.getPhotoId().getBytes().length : 0;
        size += comment.getUserId() != null ? comment.getUserId().getBytes().length : 0;
        size += comment.getParentCommentId() != null ? comment.getParentCommentId().getBytes().length : 0;

        // Calcola la lunghezza delle risposte ricorsivamente
        if (comment.getReplies() != null) {
            for (CommentComponent reply : comment.getReplies()) {
                size += calculateDocumentSize((Comment) reply); // Richiama ricorsivamente per ogni risposta
            }
        }

        // Calcola la lunghezza della data
        size += comment.getCreatedDate() != null ? 8 : 0; // Una data occupa 8 byte in BSON

        return size;
    }


}
