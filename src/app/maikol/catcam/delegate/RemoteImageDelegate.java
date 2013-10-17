package app.maikol.catcam.delegate;

import app.maikol.catcam.model.PublicPhoto;

public interface RemoteImageDelegate {
	
	public void didGetImage(PublicPhoto photo);

	public void didGetImage(Integer index, PublicPhoto photo);
	
	public void didFailGetImage(int i);
	

}
