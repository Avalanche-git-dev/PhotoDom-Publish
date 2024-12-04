package com.app.commentservice.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class Comment implements CommentComponent, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    private String id;

    private String content;

    private String photoId; // Usando l'ID come stringa per riferimenti a Photo

    private String userId;  // Usando l'ID come stringa per riferimenti a User

    private String parentCommentId; // ID del commento genitore
   
    private List<Comment> replies = new ArrayList<>();

    private Date createdDate;

    // Implementazione dei metodi di CommentComponent
//    @Override
//    public void addReply(CommentComponent comment) {
//        replies.add((Comment) comment);
//        ((Comment) comment).setParentCommentId(this.id);
//    }
    
    @Override
    public void addReply(CommentComponent comment) {
        Comment reply = (Comment) comment;
        if (reply.getId() == null || reply.getId().isEmpty()) {
            reply.setId(new ObjectId().toString()); // Genera un ObjectId unico per il commento figlio
        }
        replies.add(reply);
        reply.setParentCommentId(this.id);
    }


    @Override
    public void removeReply(CommentComponent comment) {
        replies.remove(comment);
        ((Comment) comment).setParentCommentId(null);
    }

    @Override
    public List<CommentComponent> getReplies() {
        return new ArrayList<>(replies);
    }

    @Override
    public String getContent() {
        return content;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}

	public Comment(String id, String content, String photoId, String userId, String parentCommentId,
			List<Comment> replies, Date createdDate) {
		super();
		this.id = id;
		this.content = content;
		this.photoId = photoId;
		this.userId = userId;
		this.parentCommentId = parentCommentId;
		this.replies = replies;
		this.createdDate = createdDate;
	}

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    

}
