package com.example.mydemos_len.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		outRect.set(30, 12, 30, 12);
	}
}
