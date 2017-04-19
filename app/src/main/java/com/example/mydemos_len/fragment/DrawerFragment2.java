package com.example.mydemos_len.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mydemos_len.R.layout;

public class DrawerFragment2 extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(layout.layout_pager03, null);
		
		Log.d("test", "CcFragment--onCreateView()");
		return rootView;
	}
	
	@Override
	public void onStop() {
		Log.d("test", "CcFragment--onStop()");
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		Log.d("test", "CcFragment--onDestroy()");
		super.onDestroy();
	}
}
