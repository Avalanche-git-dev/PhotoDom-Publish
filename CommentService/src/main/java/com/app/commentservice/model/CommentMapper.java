package com.app.commentservice.model;

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
                comment.getReplyIds(),
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
        comment.setReplyIds(commentDto.getRepliesIds());
        comment.setCreatedDate(commentDto.getCreatedDate());
        return comment;
    }
}
