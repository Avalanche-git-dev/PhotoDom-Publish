package com.app.commentservice.service;

import java.util.Date;
import java.util.List;

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

//    public Mono<Comment> addComment(Comment comment) {
//        comment.setCreatedDate(new Date());
//        
//        // Salva il commento nel database e aggiorna il commento padre se necessario
//        return commentRepository.save(comment)
//                .flatMap(savedComment -> {
//                    if (savedComment.getParentCommentId() != null) {
//                        // Trova il commento padre e aggiungi la risposta
//                        return commentRepository.findById(savedComment.getParentCommentId())
//                                .flatMap(parentComment -> {
//                                    parentComment.addReply(savedComment);
//                                    return commentRepository.save(parentComment).thenReturn(savedComment);
//                                });
//                    }
//                    return Mono.just(savedComment);
//                });
//    }
    
    
//    public Mono<Comment> addComment(Comment comment) {
//        comment.setCreatedDate(new Date());
//        
//        // Controlla se il commento è una risposta
//        if (comment.getParentCommentId() != null) {
//            // Trova il commento padre e aggiungi la risposta
//            return commentRepository.findById(comment.getParentCommentId())
//                    .flatMap(parentComment -> {
//                        parentComment.addReply(comment); // Aggiungi il commento come risposta annidata
//                        return commentRepository.save(parentComment).then(Mono.just(comment));
//                    });
//        } else {
//            // Se non è una risposta, salva come commento principale
//            return commentRepository.save(comment);
//        }
//    }
    
    public Mono<Comment> addComment(Comment comment) {
        comment.setCreatedDate(new Date());

        return commentRepository.save(comment)
                .flatMap(savedComment -> {
                    if (savedComment.getParentCommentId() != null) {
                        // Trova il commento padre e aggiungi la risposta
                        return commentRepository.findById(savedComment.getParentCommentId())
                                .flatMap(parentComment -> {
                                    parentComment.addReply(savedComment); // Aggiungi la risposta con l'ID univoco
                                    return commentRepository.save(parentComment).thenReturn(savedComment);
                                });
                    }
                    return Mono.just(savedComment);
                });
    }



    
    public Mono<Comment> getCommentById(String commentId) {
        return commentRepository.findById(commentId);
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
    
    
    
    public Flux<Comment> getCommentsByIds(List<String> ids) {
        return Flux.fromIterable(ids)
                   .flatMap(commentRepository::findById);
    }

}

