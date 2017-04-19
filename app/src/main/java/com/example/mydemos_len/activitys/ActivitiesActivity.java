package com.example.mydemos_len.activitys;

import com.example.mydemos_len.R;
import com.example.mydemos_len.R.id;
import com.example.mydemos_len.R.layout;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

public class ActivitiesActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activities);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		findViewById(R.id.btn_activities_1).setOnClickListener(this);
		findViewById(R.id.button_attribute).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_activities_1:
			this.finish();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(ActivitiesActivity.this, ActivitiesActivity.class);
					//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
					boolean showingLocked = km.inKeyguardRestrictedInputMode(); 
					Log.d("test_wp", "showingLocked="+showingLocked);
				}
			}, 5000);
			break;
		case R.id.button_attribute:
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
			break;

		default:
			break;
		}
	}

}
