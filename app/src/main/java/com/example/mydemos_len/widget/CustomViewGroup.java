package com.example.mydemos_len.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Scroller;
import android.widget.TextView;

public class CustomViewGroup extends ViewGroup {
	//private final int WIDTH = 990;
	//private final int HEIGHT = 680;
	private final int SNAP_NEXT = 1;
	private final int SNAP_PRE = 2;
	private TextView view1;
	private TextView view2;
	private TextView view3;
	private Scroller mScroller;
	private float lastMotionX;
	private float downX;
	private int curScreen = 1;
	private VelocityTracker mVelocityTracker;
	private boolean bFlag;
	private boolean mStartSwap;
	private boolean isScrolling;
	private int h = 900;
	private int w = 990;
	private int availableWidth;
	private int availableHeight;
	
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case SNAP_NEXT:
					snapToScreen(curScreen + 1);
					break;
				
				case SNAP_PRE:
					snapToScreen(curScreen - 1);
					break;
				
				default:
					break;
			}
		}
	};
	
	public CustomViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mScroller = new Scroller(context);
		
		setBackgroundColor(Color.parseColor("#308f8080"));
		
		initViewSize();
		
		view1 = new TextView(context);
		view1.setText("AAAaaa");
		view1.setBackgroundColor(Color.parseColor("#80ff0000"));
		view2 = new TextView(context);
		view2.setText("BBBbbb");
		view2.setBackgroundColor(Color.parseColor("#8000ff00"));
		view3 = new TextView(context);
		view3.setText("CCCccc");
		view3.setBackgroundColor(Color.parseColor("#800000ff"));
		
		addView(view1);
		addView(view2);
		addView(view3);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		//initViewSize();
	}
	
	private void initViewSize() {
		//获取View宽高的方法
		//OnGlobalLayoutListener 是ViewTreeObserver的内部类，当一个视图树的布局发生改变时，可以被ViewTreeObserver监听到.
		this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				w = CustomViewGroup.this.getWidth();
				h = CustomViewGroup.this.getHeight();
				Log.d("test_wp", String.format("--onGlobalLayout()--w = %s, h = %s", w, h));
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					CustomViewGroup.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				} else {
					CustomViewGroup.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		});
	}
	
	@Override
	public void computeScroll() {
		Log.d("test_wp", "computeScroll()----computeScrollOffset=" + mScroller.computeScrollOffset());
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		} else {
			Log.d("test_wp", "computeScroll()----mStartSwap=" + mStartSwap);
			isScrolling = false;
			if (mStartSwap) {
				swapScreen();
				mStartSwap = false;
			}
		}
		
	}
	
	private void swapScreen() {
		// TODO Auto-generated method stub
		Log.d("test_wp", "swapScreen()--");
		TextView tempView = null;
		if (curScreen < 1) {
			tempView = view3;
			view3 = view2;
			view2 = view1;
			view1 = tempView;
		} else {
			tempView = view1;
			view1 = view2;
			view2 = view3;
			view3 = tempView;
		}
		reLayout();
		curScreen = 1;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		
		float currentX = event.getX();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = currentX;
				lastMotionX = currentX;
				bFlag = false;
				
				break;
			
			case MotionEvent.ACTION_MOVE:
				int x_offset = (int) (lastMotionX - currentX);
				//Log.d("test_wp", "action_move---- x_offset= "+x_offset);
				//Log.d("test_wp", "action_move---- getScrollX= "+getScrollX());
				//			if((curScreen == 0 && x_offset < 0)
				//					|| (curScreen == 2 && x_offset > 0)){
				//				bFlag = true;
				//				return false;
				//			}
				scrollBy(x_offset, 0);
				lastMotionX = currentX;
				break;
			
			case MotionEvent.ACTION_UP:
				if (bFlag) return false;
				//Log.d("test_wp", "action_up---- getScrollX= "+getScrollX());
				
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				//计算速率
				int velocityX = (int) velocityTracker.getXVelocity();
				
				int dx = (int) (currentX - downX);
				Log.d("test_wp", "action_up---- dx= " + dx);
				if (velocityX > 600 || dx > 500) {
					//>>>>>>>>> -->
					snapToPre();
				} else if (velocityX < -600 || dx < -500) {
					//<<<<<<<<<< <--
					snapToNext();
				} else {
					snapToScreen(curScreen);
				}
				
				//回收VelocityTracker对象
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				
				break;
			
			default:
				break;
		}
		
		return true;
	}
	
	private void snapToNext() {
		if (!mScroller.isFinished()) {
			Log.d("test_wp", "snapToNext()--isScrolling--foceFinished.");
			mScroller.forceFinished(true);
			mHandler.sendEmptyMessageDelayed(SNAP_NEXT, 20);
		} else {
			snapToScreen(curScreen + 1);
		}
	}
	
	private void snapToPre() {
		if (!mScroller.isFinished()) {
			Log.d("test_wp", "snapToPre()--isScrolling--foceFinished.");
			mScroller.forceFinished(true);
			mHandler.sendEmptyMessageDelayed(SNAP_PRE, 20);
		} else {
			snapToScreen(curScreen - 1);
		}
	}
	
	private void snapToScreen(int screen) {
		// TODO Auto-generated method stub
		Log.d("test_wp", "snapToScreen()--screen=" + screen);
		if (curScreen != screen) {
			mStartSwap = true;
		}
		curScreen = screen;
		
		int dx = curScreen * getMeasuredWidth() - getScrollX();
		mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(dx) * 1);
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int childCount = getChildCount();
		if (childCount == 0) {
			setMeasuredDimension(0, 0);
		} else {
			setMeasuredDimension(width, height);
		}
		
		//		for (int i = 0; i < childCount; i++) {
		//			View child = getChildAt(i);
		//			child.measure(60, 80);
		//		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		//		for (int i = 0; i < getChildCount(); i++) {
		//			View child = getChildAt(i);
		//			//child.layout(l, t, l+child.getMeasuredWidth(), t+child.getMeasuredHeight());
		//			child.layout(l, t, l+WIDTH, t+WIDTH);
		//			l += WIDTH +  20;
		//		}
		
		reLayout();
		
	}
	
	private void reLayout() {
		availableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		availableHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
		Log.d("test_wp", "reLayout()--scrollX = " + getScrollX());
		Log.d("test_wp", String.format("reLayout()--with=%s, height=%s", availableWidth, availableHeight));
		setScrollX(getMeasuredWidth());
		if (view1 != null) {
			view1.layout(getPaddingLeft(), getPaddingTop(),
					getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
		}
		if (view2 != null) {
			view2.layout(getMeasuredWidth() + getPaddingLeft(), getPaddingTop(),
					2 * getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
		}
		if (view3 != null) {
			view3.layout(2 * getMeasuredWidth() + getPaddingLeft(), getPaddingTop(),
					3 * getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
		}
	}
	
}
