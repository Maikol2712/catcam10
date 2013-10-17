package app.maikol.catcam.components;

import java.io.Serializable;

import app.maikol.catcam.model.PublicPhoto;

public interface ViewPagerDelegate extends Serializable{
	public void didReceiveFragmentRequest(int fragmentId, int operationCode);
	public void didReceiveImageDetailsRequest(PublicPhoto publicPhoto);
}
