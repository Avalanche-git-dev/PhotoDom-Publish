package com.app.photoservice.kafka.event;

public class PhotoProcessedEvent {
    private String photoId;
    private String userId;
    private String status; // "ok" o "error"
    private String reason; // Opzionale, presente solo se status è "error"

    // Getter e Setter
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
