package com.example.mydemos_len.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class CustomDragView extends LinearLayout{

	private final int HEIGHT = 800;
	private OnParentViewDragListener mDragListener;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	public static int  SNAP_VELOCITY = 600 ;
	private float mLastMotionY;
	private float mDownY;
	private float mUpY;
	private int mOffset = 100;
	private boolean mIsOpened = false;
	private boolean mIsMoveing = false;
	private int mScrollY;

	public CustomDragView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mScroller = new Scroller(context);
	}
	
	public interface OnParentViewDragListener{
		void onDragStart();
		void onDraging(float rawY);
		void onDragEnd();
		void onClosed(int dy);
		void onOpened();
	}
	
	public void setOnViewDragListener(OnParentViewDragListener listener){
		this.mDragListener = listener;
	}
	
	private void onOpen(){
		//Toast.makeText(getContext(), "on done!!", 0).show();
		int dy = (int) (HEIGHT - getScrollY());
		//Log.d("test","onDone()--dy="+dy);
		
		mScroller.startScroll(0, getScrollY(), 0,  dy, Math.abs(dy) * 1);
		invalidate(); // Redraw the layout
		
		mIsOpened = true;
		if(mDragListener != null)mDragListener.onOpened();
	}
	
	public void onClose(){
		//Toast.makeText(getContext(), "on reset!!", 0).show();
		int dy = (int) getScrollY();
		//Log.d("test","onReset()--dy="+dy);
		
		mScroller.startScroll(0, getScrollY(), 0, -dy, Math.abs(dy) * 1);
		invalidate(); // Redraw the layout
		
		mIsOpened = false;
		if(mDragListener != null)mDragListener.onClosed(dy);
	}

	private void returnToOpen() {
		int dy = (int) (HEIGHT - getScrollY());
		mScroller.startScroll(0, getScrollY(), 0,  dy, Math.abs(dy) * 1);
		invalidate(); 
	}
	
	private void returnToClose() {
		int dy = (int) getScrollY();
		mScroller.startScroll(0, getScrollY(), 0, -dy, Math.abs(dy) * 1);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	} 
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {  
            mVelocityTracker = VelocityTracker.obtain();  
        }  
        mVelocityTracker.addMovement(event);  
		
		final float rawY = event.getRawY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = rawY;
			mDownY = rawY;
			mIsMoveing = true;
			if(mDragListener != null)mDragListener.onDragStart();
			//Log.d("test","action_down--mLastMotionY="+mLastMotionY);
			break;
		case MotionEvent.ACTION_MOVE:
			int y_offset = (int) (mLastMotionY - rawY);
			//Log.d("test","action_move--y_offset="+y_offset);
			mScrollY = getScrollY();
			//Log.d("test","action_move--scrollY="+mScrollY);
			if((mScrollY <= HEIGHT || y_offset < 0) && mScrollY >= 0){
				scrollBy(0, y_offset);
				mLastMotionY = rawY;
				if(mDragListener != null)mDragListener.onDraging(rawY);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			int dy = (int) (mDownY - rawY);
			//Log.d("test","action_up--dy="+dy);
			
			final VelocityTracker velocityTracker = mVelocityTracker  ;  
            velocityTracker.computeCurrentVelocity(1000);  
            int velocityY = (int) velocityTracker.getYVelocity() ;  
            //Log.d("test","action_up--velocityY="+velocityY);
            if(dy > 0){
            	if(Math.abs(velocityY) > SNAP_VELOCITY || dy > 300){
            		if(!mIsOpened){
            			onOpen();
            		}
            	}else{
            		if(!mIsOpened){
            			onClose();
            		}
            	}
            }else if(dy == 0){
            	if(!mIsOpened){
            		onClose();
            	}
            }else{
            	if(Math.abs(velocityY) > SNAP_VELOCITY || dy < -200){
            		if(mIsOpened){
            			onClose();
            		}
            	}else{
            		if(mIsOpened){
            			returnToOpen();
            		}
            	}
            }
            
            if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
            //Log.d("test","action_up--scrollY="+mScrollY+", getScrollY()="+getScrollY());
            if(mScrollY > HEIGHT){
            	mScrollY = HEIGHT;
            	int overedY = (int) (HEIGHT - getScrollY());
        		mScroller.startScroll(0, getScrollY(), 0,  overedY, Math.abs(overedY) * 1);
        		invalidate();
            }
            
            mIsMoveing = false;
            if(mDragListener != null)mDragListener.onDragEnd();
			
			break;

		default:
			break;
		}
		
		return true;
	}
}
