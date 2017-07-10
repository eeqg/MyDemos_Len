package com.example.mydemos_len.activitys;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.example.mydemos_len.R;

public class ScrollActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll);
		
		observeContent();
	}
	
	private void observeContent() {
		final int offset = 10;
		final View viewIndicator = findViewById(R.id.viewIndicator);
		findViewById(R.id.offsetLeft).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewIndicator.offsetLeftAndRight(-offset);
			}
		});
		findViewById(R.id.offsetRight).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewIndicator.offsetLeftAndRight(offset);
			}
		});
		findViewById(R.id.offsetUp).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewIndicator.offsetTopAndBottom(-offset);
			}
		});
		findViewById(R.id.offsetDown).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewIndicator.offsetTopAndBottom(offset);
			}
		});
	}
}
