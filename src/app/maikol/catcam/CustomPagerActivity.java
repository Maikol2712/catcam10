package app.maikol.catcam;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import app.maikol.catcam.components.ViewPagerDelegate;
import app.maikol.catcam.model.PublicPhoto;
import app.maikol.catcam.util.HttpConnectionManager;

public class CustomPagerActivity extends FragmentActivity implements
		ViewPagerDelegate {

	FragmentPagerAdapter mPagerAdapter;
	ViewPager mViewPager;
	public List<Fragment> fragments;

	CustomGallery galleryFragment;
	UserFragment userFragment;
	ImagesGrid imagesFragment;
	ItemDetailsFragment imageFragment;

	SharedPreferences options;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainpager);

		options = this.getPreferences(Context.MODE_PRIVATE);

		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		fragments = new ArrayList<Fragment>();

		Bundle args = new Bundle();
		args.putSerializable("delegate", this);
		if (savedInstanceState != null) {
			galleryFragment = (CustomGallery) getSupportFragmentManager()
					.findFragmentByTag("USER_GALLERY");
			userFragment = (UserFragment) getSupportFragmentManager()
					.findFragmentByTag("USER");
			imagesFragment = (ImagesGrid) getSupportFragmentManager()
					.findFragmentByTag("IMAGE_GRID");
			imageFragment = (ItemDetailsFragment) getSupportFragmentManager()
					.findFragmentByTag("IMAGE_DETAILS");
		} else {

			galleryFragment = new CustomGallery();
			userFragment = new UserFragment();
			imagesFragment = new ImagesGrid();
			// imageFragment = new ItemDetailsFragment();

			galleryFragment.setArguments(args);
			userFragment.setArguments(args);
			imagesFragment.setArguments(args);
			fragments.add(galleryFragment);
			fragments.add(userFragment);
			fragments.add(imagesFragment);
			// fragments.add(imageFragment);

		}
		mPagerAdapter = new FragmentPagerAdapter(
				this.getSupportFragmentManager()) {

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return CustomPagerActivity.this.fragments.get(arg0);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return CustomPagerActivity.this.fragments.size();
			}

		};
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(1);

		mViewPager.setCurrentItem(1, Boolean.FALSE);
	}

	@Override
	public void didReceiveFragmentRequest(int fragmentId, int operationCode) {
		int index = 0;
		if (fragmentId == R.layout.gallery)
			index = 0;
		if (fragmentId == R.layout.userdetails)
			index = 1;
		if (fragmentId == R.layout.image_gallery_grid) {
			index = 2;
			if (operationCode == HttpConnectionManager.CODE_GET_IMAGES_BY_USER) {
				String user = options.getString("username", "");
				imagesFragment.getImagesByUser(user);
			}
			if (operationCode == HttpConnectionManager.CODE_GET_ALL_IMAGES) {
				imagesFragment.getAllImages();
			}
		}

		mViewPager.setCurrentItem(index, Boolean.TRUE);
	}

	@Override
	public void didReceiveImageDetailsRequest(PublicPhoto publicPhoto) {

		if (imageFragment == null) {
			imageFragment = new ItemDetailsFragment();
		}
		if (!fragments.contains(imageFragment)){
			fragments.add(imageFragment);
			mPagerAdapter.notifyDataSetChanged();
		}
		mViewPager.setCurrentItem(fragments.size() - 1, Boolean.FALSE);

		imageFragment.setPhoto(publicPhoto);

	}

	@Override
	public void removeImageDetailsFragment() {
		if (imageFragment != null){
			fragments.remove(imageFragment);
			mPagerAdapter.notifyDataSetChanged();
		}
	}
}
