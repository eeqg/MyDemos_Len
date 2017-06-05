package com.example.mydemos_len.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mydemos_len.fragment.TestFragment;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class TabFragmentAdapter extends FragmentStatePagerAdapter {
	public TabFragmentAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int position) {
		return TestFragment.getInstance(position);
	}
	
	@Override
	public int getCount() {
		return 4;
	}
}
