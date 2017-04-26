package com.example.mydemos_len.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yancy.imageselector.ImageConfig;

import java.util.List;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class ImagePagerAdapter<T> extends PagerAdapter {
	private List<Integer> dataList;
	private ImageView imageView;
	
	public ImagePagerAdapter(List<Integer> data){
		dataList = data;
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}
	
	public int getRealCount(){
		return dataList == null ? 0 : dataList.size();
	}
	
	private int toRealPosition(int position){
		int realCount = getRealCount();
		if(realCount == 0){
			return 0;
		}
		
		return position % realCount;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.d("test_wp", "position="+position);
		int realPosition = toRealPosition(position);
		Log.d("test_wp", "realPosition="+realPosition);
		
		imageView = new ImageView(container.getContext());
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setImageResource(dataList.get(realPosition));
		
		container.addView(imageView);
		return imageView;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
}
