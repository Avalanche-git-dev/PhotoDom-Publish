import React, { useState } from "react";
import "./comment.css";

const Comment = ({ comment, onReply, onExpand, onDelete }) => {
  const [replyContent, setReplyContent] = useState("");
  const [showReplies, setShowReplies] = useState(false);

  const toggleReplies = () => {
    if (!showReplies) {
      onExpand();
    }
    setShowReplies(!showReplies);
  };

  return (
    <div className="comment-container">
      <div className="comment-header">
        <p className="comment-content">{comment.content}</p>
        <button className="delete-button" onClick={() => onDelete(comment.id)}>
          🗑
        </button>
      </div>
      <div className="comment-actions">
        <textarea
          placeholder="Rispondi..."
          value={replyContent}
          onChange={(e) => setReplyContent(e.target.value)}
        />
        <button onClick={() => onReply(replyContent)}>Rispondi</button>
        {comment.repliesIds && comment.repliesIds.length > 0 && (
          <button onClick={toggleReplies}>
            {showReplies ? "Nascondi Risposte" : `Mostra Risposte (${comment.repliesIds.length})`}
          </button>
        )}
      </div>
      {showReplies && comment.replies && (
        <div className="replies-list">
          {comment.replies.map((reply) => (
            <Comment
              key={reply.id}
              comment={reply}
              onReply={onReply}
              onExpand={onExpand}
              onDelete={onDelete}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default Comment;
