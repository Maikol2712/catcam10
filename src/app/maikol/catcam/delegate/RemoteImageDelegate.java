package app.maikol.catcam.delegate;

import app.maikol.catcam.model.Comment;
import app.maikol.catcam.model.PublicPhoto;

public interface RemoteImageDelegate {
	
	public void didGetImage(PublicPhoto photo);

	public void didGetImage(Integer index, PublicPhoto photo);
	
	public void didFailGetImage(int i);

	public void didGetComment(int i, Comment comment);

	public void didFailGetComment(int i);
	

}
