package com.example.mydemos_len.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mydemos_len.R;

public class AnimationActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);
		
		findViewById(R.id.button_animation_property).setOnClickListener(this);
		findViewById(R.id.button_objectAnimator).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_animation_property:
			startActivity(new Intent(this, PropertyAnimationActivity.class));
			break;
		case R.id.button_objectAnimator:
			startActivity(new Intent(this, ObjectAnimatorActivity.class));
			break;

		default:
			break;
		}
	}

	
}
