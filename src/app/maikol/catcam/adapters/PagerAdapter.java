package app.maikol.catcam.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
	
	List<Fragment> fragmentList;
	public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragmentList = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return this.fragmentList.size();
	}
	
}
