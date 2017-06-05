package com.example.mydemos_len.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mydemos_len.R;
import com.example.mydemos_len.basic.BasicFragment;
import com.example.mydemos_len.databinding.FragmentTestBinding;

import wp.allutils.common.ColorUtils;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class TestFragment extends BasicFragment {
	private static final String PAGE_TYPE = "page_type";
	private FragmentTestBinding dataBinding;
	private int pageType;
	
	public static TestFragment getInstance(int pageType){
		Bundle args = new Bundle();
		args.putInt(PAGE_TYPE, pageType);
		TestFragment fragment = new TestFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageType = getArguments().getInt(PAGE_TYPE);
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
		return dataBinding.getRoot();
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//this.dataBinding.rootView.setBackgroundColor(ColorUtils.getRandomColor());
		this.dataBinding.tvTips.setBackgroundColor(ColorUtils.getRandomColor());
		this.dataBinding.setTips("TestFragment #"+pageType);
	}
}
