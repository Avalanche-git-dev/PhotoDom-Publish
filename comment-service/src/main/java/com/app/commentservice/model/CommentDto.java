package com.app.commentservice.model;

import java.util.Date;
import java.util.List;

public class CommentDto {

    private String id;
    private String content;
    private String photoId;  
    private String userId;   
    private String parentCommentId; 
    private List<String> repliesIds; 
    private Date createdDate;

    public CommentDto(String id, String content, String photoId, String userId, String parentCommentId, List<String> repliesIds, Date createdDate) {
        this.id = id;
        this.content = content;
        this.photoId = photoId;
        this.userId = userId;
        this.parentCommentId = parentCommentId;
        this.repliesIds = repliesIds;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public List<String> getRepliesIds() {
        return repliesIds;
    }

    public void setRepliesIds(List<String> repliesIds) {
        this.repliesIds = repliesIds;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

	public CommentDto() {
		super();
	}
    
    
}
