package com.example.mydemos_len;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import com.baidu.mapapi.SDKInitializer;
import com.example.mydemos_len.service.LocationService;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class APP extends Application {
	public LocationService locationService;
	public Vibrator mVibrator;
	@Override
	public void onCreate() {
		super.onCreate();
		/***
		 * 初始化定位sdk，建议在Application中创建
		 */
		locationService = new LocationService(getApplicationContext());
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		// SDKInitializer.initialize(getApplicationContext());
		
	}
}
