package com.app.commentservice.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "comments")
public class Comment implements CommentComponent, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String content;

    private String photoId; 

    private String userId;  

    private String parentCommentId; 

    private List<String> replyIds = new ArrayList<>(); 

    private Date createdDate;

    @Override
    public void addReply(CommentComponent comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Il commento non può essere null");
        }
        Comment reply = (Comment) comment;

        if (reply.getId() == null || reply.getId().isEmpty()) {
            reply.setId(new ObjectId().toString());
        }
        reply.setParentCommentId(this.id);

        if (!replyIds.contains(reply.getId())) {
            replyIds.add(reply.getId());
        }
    }

    @Override
    public void removeReply(CommentComponent comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Il commento non può essere null");
        }
        replyIds.remove(((Comment) comment).getId());
    }

    @Override
    @JsonIgnore
    public List<CommentComponent> getReplies() {
        throw new UnsupportedOperationException("I reply devono essere caricati dal database tramite gli ID.");
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

	public List<String> getReplyIds() {
		return replyIds;
	}

	public void setReplyIds(List<String> replyIds) {
		this.replyIds = replyIds;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    // Getter e setter
    // ...
}

