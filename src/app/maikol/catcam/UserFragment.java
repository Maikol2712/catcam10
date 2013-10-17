package app.maikol.catcam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import app.maikol.catcam.components.ViewPagerDelegate;
import app.maikol.catcam.util.HttpConnectionManager;

public class UserFragment extends Fragment {

	ViewPagerDelegate mViewPagerDelegate;
	
	ImageButton mtoLeftButton;
	ImageButton mtoRightButton;
	Button mNewImagesButton;
	Button mMyCloudImagesButton;
	TextView mTxtUserName;

	String mSavedUsername;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		

		final SharedPreferences options = getActivity().getPreferences(
				Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = options.edit();

		RelativeLayout usermenuView = (RelativeLayout) inflater.inflate(
				R.layout.userdetails, container, false);
		
//		mViewPagerDelegate = (ViewPagerDelegate) savedInstanceState.getSerializable("delegate");
		mViewPagerDelegate = (CustomPagerActivity) this.getActivity();
		mTxtUserName = (TextView) usermenuView.findViewById(R.id.txtUsername);
		
		mtoLeftButton = (ImageButton) usermenuView
				.findViewById(R.id.usermenu_to_left);
		mtoRightButton = (ImageButton) usermenuView
				.findViewById(R.id.usermenu_to_right);
		mNewImagesButton = (Button) usermenuView
				.findViewById(R.id.usermenu_new_photos_button);
		mMyCloudImagesButton = (Button) usermenuView
				.findViewById(R.id.usermenu_my_images);

		mNewImagesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mViewPagerDelegate.didReceiveFragmentRequest(R.layout.image_gallery_grid,0);
			}
		});
		
		mMyCloudImagesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPagerDelegate.didReceiveFragmentRequest(R.layout.image_gallery_grid,HttpConnectionManager.CODE_GET_IMAGES_BY_USER );
			}
		});

		mtoLeftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPagerDelegate.didReceiveFragmentRequest(R.layout.gallery, 0);
			}
		});

		mtoRightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mViewPagerDelegate.didReceiveFragmentRequest(R.layout.image_gallery_grid,0);

			}
		});
		
		
		mTxtUserName.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				editor.putString("username", mTxtUserName.getText().toString());
				editor.commit();
				return true;
			}
		});
		mSavedUsername = options.getString("username", "");

		if (mSavedUsername.length() > 0) {
			mTxtUserName.setText(mSavedUsername);
		}

		return usermenuView;
	}
}
