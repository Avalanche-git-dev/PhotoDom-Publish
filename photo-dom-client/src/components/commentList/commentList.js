import React, { useState } from "react";
import Comment from "../comment/comment";
import "./commentList.css";

const CommentList = ({ comments, api, parentCommentId, onAddReply }) => {
  const [expandedReplies, setExpandedReplies] = useState([]);

  // Gestisce il caricamento delle risposte
  const toggleReplies = async (commentId) => {
    if (expandedReplies.includes(commentId)) {
      setExpandedReplies((prev) => prev.filter((id) => id !== commentId));
    } else {
      try {
        const response = await api.get(`/comments/comment/replies?id=${commentId}`);
        onAddReply({ id: commentId, replies: response });
        setExpandedReplies((prev) => [...prev, commentId]);
      } catch (error) {
        console.error("Errore nel caricamento delle risposte:", error);
      }
    }
  };

  return (
    <div className="comment-list">
      {comments.map((comment) => (
        <Comment
          key={comment.id}
          comment={comment}
          api={api}
          onReply={(reply) => onAddReply(reply)}
          onToggleReplies={() => toggleReplies(comment.id)}
        />
      ))}
    </div>
  );
};

export default CommentList;
