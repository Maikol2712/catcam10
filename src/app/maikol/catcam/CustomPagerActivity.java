package app.maikol.catcam;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

public class CustomPagerActivity extends FragmentActivity {

	FragmentPagerAdapter mPagerAdapter;
	ViewPager mViewPager;
	public List<Fragment> fragments;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainpager);
		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		fragments = new ArrayList<Fragment>();

		fragments.add(new CustomGallery());
		fragments.add(new UserFragment());
		mPagerAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {			

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
	}
}
