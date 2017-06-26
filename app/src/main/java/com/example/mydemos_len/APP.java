package com.example.mydemos_len;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.example.mydemos_len.service.LocationService;

import crash.CrashHandler2;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class APP extends Application {
	
	/** 屏幕宽度 */
	public static int SCREEN_WIDTH;
	/** 屏幕高度 */
	public static int SCREEN_HEIGHT;
	
	public LocationService locationService;
	public Vibrator mVibrator;
	private static APP INSTANCE;
	
	@Override
	public void onCreate() {
		super.onCreate();
		INSTANCE = this;
		
		/***
		 * 初始化定位sdk，建议在Application中创建
		 */
		locationService = new LocationService(getApplicationContext());
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		// SDKInitializer.initialize(getApplicationContext());
		
		
		initScreenSize();
		
		//bug
		//CrashHandler2.getInstance().init(getApplicationContext());
	}
	
	public static APP getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new APP();
		}
		return INSTANCE;
	}
	
	/**
	 * 获取屏幕大小
	 */
	private void initScreenSize() {
		final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		boolean isPortrait = displayMetrics.widthPixels < displayMetrics.heightPixels;
		SCREEN_WIDTH = isPortrait ? displayMetrics.widthPixels : displayMetrics.heightPixels;
		SCREEN_HEIGHT = isPortrait ? displayMetrics.heightPixels : displayMetrics.widthPixels;
	}
}
