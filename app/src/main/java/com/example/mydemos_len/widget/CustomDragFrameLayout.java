package com.example.mydemos_len.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class CustomDragFrameLayout extends FrameLayout {
	private String TAG = "CustomDragFrameLayout---";

	public CustomDragFrameLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomDragFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomDragFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	int mLastX = 0, mLastY = 0, mDownX = 0, mDownY = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int currentX = (int) event.getX();
		//int currentY = (int) event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d("test_wp", TAG+"ACTION_DOWN");
			mDownX = mLastX = (int) currentX;
			//mDownY = mLastY = (int) currentY;
			
			break;
		case MotionEvent.ACTION_MOVE:
			int dx = (int) (currentX - mDownX);
			//int dy = (int) (currentY - mDownY);
			Log.d("test_wp", "ACTION_MOVE---dx= "+dx);
			//Log.d("test_wp", "ACTION_MOVE---dy= "+dy);
			setTranslationX(dx);
			//setTranslationY(dy);
			
			mLastX = currentX;
			//mLastY = currentY;
			//invalidate();
			
			break;
		case MotionEvent.ACTION_UP:
			Log.d("test_wp", TAG+"ACTION_UP");
			
			break;
		}
		
		
		//return super.onTouchEvent(event);
		return true;
	}
}
