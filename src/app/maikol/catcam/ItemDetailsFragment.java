package app.maikol.catcam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import app.maikol.catcam.components.ViewPagerDelegate;
import app.maikol.catcam.model.PublicPhoto;

public class ItemDetailsFragment extends Fragment {

	ViewPagerDelegate mViewPagerDelegate;

	ImageButton mtoLeftButton;
	ImageButton mtoRightButton;
	Button mNewImagesButton;
	Button mMyCloudImagesButton;
	TextView mTxtUserName, mTxtDescription;

	ImageView image;

	String mSavedUsername;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final SharedPreferences options = getActivity().getPreferences(
				Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = options.edit();

		ScrollView imageDetailsLayout = (ScrollView) inflater.inflate(
				R.layout.imagedetails, container, false);
		mViewPagerDelegate = (CustomPagerActivity) this.getActivity();
		mTxtUserName = (TextView) imageDetailsLayout
				.findViewById(R.id.imagedetails_txt_username);
		mTxtDescription = (TextView) imageDetailsLayout
				.findViewById(R.id.imagedetails_txt_description);

		image = (ImageView) imageDetailsLayout
				.findViewById(R.id.imagedetails_image);
		return imageDetailsLayout;
	}

	public void setPhoto(PublicPhoto photo) {

		image.setImageBitmap(photo.getBitmap());

		mTxtUserName.setText(photo.getUser());
		mTxtDescription.setText(photo.getDescription());
	}
}
