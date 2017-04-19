package com.example.mydemos_len.activitys;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mydemos_len.R;

public class PasswordRetrievalActivity extends Activity {
	private static final String REQUEST_UNLOCK_ACTION =
	        "com.motorola.internal.policy.impl.REQUEST_UNLOCK";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_retrieval);
		
		ActionBar actionBar = getActionBar();
		//actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendBroadcast(new Intent(REQUEST_UNLOCK_ACTION));
			}
		}, 5000);
		
		findViewById(R.id.button_test1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendBroadcast(new Intent(REQUEST_UNLOCK_ACTION));
			}
		});
	}

	
}
