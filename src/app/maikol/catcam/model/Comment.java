package app.maikol.catcam.model;

public class Comment {

	String imageId;
	String username;
	String comment;
	
	public Comment(){
		super();
	}


	public Comment(String imageId, String username, String comment) {
		super();
		this.imageId = imageId;
		this.username = username;
		this.comment = comment;
	}


	public String getImageId() {
		return imageId;
	}


	public void setImageId(String imageId) {
		this.imageId = imageId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
