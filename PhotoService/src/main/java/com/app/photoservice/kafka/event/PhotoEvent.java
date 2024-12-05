package com.app.photoservice.kafka.event;

public class PhotoEvent {
    private String photoId;
    private String userId;
    private String timestamp;

    
    
    
    

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






	public String getTimestamp() {
		return timestamp;
	}






	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}






	public PhotoEvent(String photoId, String userId, String timestamp) {
		super();
		this.photoId = photoId;
		this.userId = userId;
		this.timestamp = timestamp;
	}






	public PhotoEvent() {
		super();
	}






	@Override
    public String toString() {
        return "PhotoEvent{" +
                "photoId='" + photoId + '\'' +
                ", userId='" + userId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
