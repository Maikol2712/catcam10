package app.maikol.catcam.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class PublicPhoto extends GenericPhoto {
	String id;
	String user;
	String description;
	List<Comment> commentList = new ArrayList<Comment>();
	boolean favorite;
	
	public PublicPhoto(String imageId, String image64, String user, String description) {
		super();
		this.id = imageId;
		
		this.bitmap = decodeString(image64);
		this.user = user;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
}
