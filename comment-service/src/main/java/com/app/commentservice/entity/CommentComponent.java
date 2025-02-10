package com.app.commentservice.entity;

import java.util.List;

public interface CommentComponent {
    void addReply(CommentComponent comment);
    void removeReply(CommentComponent comment);
    List<CommentComponent> getReplies();
    String getContent();
}

