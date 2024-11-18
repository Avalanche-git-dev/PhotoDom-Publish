package com.app.commentservice.model;

import java.util.stream.Collectors;

import com.app.commentservice.entity.Comment;

public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getPhotoId(),
                comment.getUserId(),
                comment.getParentCommentId(),
                comment.getReplies().stream().map(reply -> ((Comment) reply).getId()).collect(Collectors.toList()),
                comment.getCreatedDate()
        );
    }

    public static Comment toEntity(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setContent(commentDto.getContent());
        comment.setPhotoId(commentDto.getPhotoId());
        comment.setUserId(commentDto.getUserId());
        comment.setParentCommentId(commentDto.getParentCommentId());
        comment.setCreatedDate(commentDto.getCreatedDate());
        // Le risposte non vengono convertite direttamente per evitare dipendenze circolari
        return comment;
    }
}
