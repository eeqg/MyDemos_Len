package com.example.mydemos_len.activitys;

import com.example.mydemos_len.R;
import com.example.mydemos_len.R.id;
import com.example.mydemos_len.R.layout;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class ObjectAnimatorActivity extends Activity implements OnClickListener {

	private View firstPageView;
	private View nextPageView;
	private View imageContainView;
	private int distance;
	private int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object_animator);
		
		Point outSize = new Point();
		getWindowManager().getDefaultDisplay().getSize(outSize);
		screenWidth = outSize.x;
		Log.d("test_wp", "------------------screenWidth="+screenWidth);
		
		firstPageView = findViewById(R.id.firstPage);
		nextPageView = findViewById(R.id.nextPage);
		nextPageView.setTranslationX(screenWidth);
		findViewById(R.id.button_up).setOnClickListener(this);
		findViewById(R.id.button_next).setOnClickListener(this);
		imageContainView = findViewById(R.id.imageContainView);
		distance = imageContainView.getWidth();
		Log.d("test_wp", "------------------"+distance);
		LayoutParams params = imageContainView.getLayoutParams();
		params.width = screenWidth * 2;
		imageContainView.setLayoutParams(params);
		Log.d("test_wp", "-----------------getWidth="+imageContainView.getWidth());
		
		View view111 = findViewById(R.id.imageView1111);
		LayoutParams params111 = view111.getLayoutParams();
		params111.width = screenWidth;
		view111.setLayoutParams(params111);
		View view222 = findViewById(R.id.imageView2222);
		LayoutParams params222 = view222.getLayoutParams();
		params222.width = screenWidth;
		view222.setLayoutParams(params222);
		view222.setTranslationX(screenWidth);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_next:
			ObjectAnimator animatorLeftOut = ObjectAnimator.ofFloat(firstPageView, "translationX", 0f, -screenWidth);
			animatorLeftOut.setDuration(400);
			ObjectAnimator animatorRightIn = ObjectAnimator.ofFloat(nextPageView, "translationX", screenWidth, 0f);
			animatorRightIn.setDuration(400);
			animatorLeftOut.start();
			animatorRightIn.start();
			nextPageView.setVisibility(View.VISIBLE);
			
			//
			ObjectAnimator animatorMoveLeft = ObjectAnimator.ofFloat(imageContainView, "translationX", 0f, -screenWidth);
			animatorMoveLeft.setDuration(400);
			animatorMoveLeft.start();
			
			break;
		case R.id.button_up:
			ObjectAnimator animatorLeftIn = ObjectAnimator.ofFloat(firstPageView, "translationX", -screenWidth, 0f);
			animatorLeftIn.setDuration(400);
			ObjectAnimator animatorRightOut = ObjectAnimator.ofFloat(nextPageView, "translationX", 0f, screenWidth);
			animatorRightOut.setDuration(400);
			animatorLeftIn.start();
			animatorRightOut.start();
			
			//
			ObjectAnimator animatorMoveRight = ObjectAnimator.ofFloat(imageContainView, "translationX", -screenWidth, 0f);
			animatorMoveRight.setDuration(400);
			animatorMoveRight.start();
			break;

		default:
			break;
		}
	}

}
