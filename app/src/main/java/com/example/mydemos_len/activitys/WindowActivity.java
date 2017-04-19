package com.example.mydemos_len.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;

import com.example.mydemos_len.R;

public class WindowActivity extends Activity {

	private WindowManager winManager;
	private LayoutParams mParams;
	private View parentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_window);
		
		findViewById(R.id.window_share).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showShareView();
			}
		});
	}

	protected void showShareView() {
		// TODO Auto-generated method stub
//		winManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		mParams = new WindowManager.LayoutParams();
//		mParams.type = LayoutParams.TYPE_PHONE;
//		mParams.format = PixelFormat.RGBA_8888;
//		mParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//		mParams.width = LayoutParams.MATCH_PARENT;
//		mParams.height = LayoutParams.MATCH_PARENT ;
//		
//		parentView = getLayoutInflater().inflate(R.layout.layout_window_share, null);
//		parentView.setBackgroundColor(0x30000000);
//		
//		winManager.addView(parentView , mParams);
		
		Intent intent = new Intent(this, ShareActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.window, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
