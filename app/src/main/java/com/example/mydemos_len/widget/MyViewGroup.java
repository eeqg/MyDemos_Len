package com.example.mydemos_len.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyViewGroup extends ViewGroup {
 
    // 子View的水平间隔
    private final static int padding = 20;
     
    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setBackgroundColor(Color.parseColor("#308f8080"));
        
        TextView view1 = new TextView(context);
		view1.setText("AAA");
		view1.setBackgroundColor(Color.parseColor("#80ff0000"));
		TextView view2 = new TextView(context);
		view2.setText("BBB");
		view2.setBackgroundColor(Color.parseColor("#8000ff00"));
		TextView view3 = new TextView(context);
		view3.setText("CCC");
		view3.setBackgroundColor(Color.parseColor("#800000ff"));
		
		addView(view1);
		addView(view2);
		addView(view3);
    }
 
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
         
        // 动态获取子View实例
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View view = getChildAt(i);
            // 放置子View，宽高都是100
            view.layout(l, t, l + 100, t + 100);
            l += 100 + padding;
        }
         
    }
     
}