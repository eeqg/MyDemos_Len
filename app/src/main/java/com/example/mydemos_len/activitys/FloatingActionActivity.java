package com.example.mydemos_len.activitys;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mydemos_len.R;
import com.example.mydemos_len.bean.TestTitleBean;
import com.example.mydemos_len.utils.FloatingActionAdapter;
import com.example.mydemos_len.utils.ItemDecoration;
import com.example.mydemos_len.utils.LogUtils;
import com.example.mydemos_len.utils.MyAdapter;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import java.util.ArrayList;

public class FloatingActionActivity extends Activity {
	
	FABToolbarLayout fabtoolbar;
	private ArrayList<TestTitleBean> originData = new ArrayList<TestTitleBean>();
	private FloatingActionAdapter adapter;
	private View stickyView;
	private TextView stickyContent;
	private RecyclerView recyclerView;
	private int totalDy = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_floating_action);
		
		
		findViewById(R.id.fabtoolbar_fab);
		fabtoolbar = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
		
		initData();
		
		LogUtils.d(originData.size());
		
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new FloatingActionAdapter(this, originData);
		recyclerView.setAdapter(adapter);
		//recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		recyclerView.addItemDecoration(new ItemDecoration(this));
		
		//sticky
		stickyView = findViewById(R.id.stickyView);
		stickyView.setVisibility(View.GONE);
		stickyContent = (TextView) findViewById(R.id.stickyContent);
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}
			
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				
				RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
				if (layoutManager instanceof LinearLayoutManager) {
					LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
					//int height = linearLayoutManager.getChildAt(0).getHeight();
					int height = (int) getResources().getDisplayMetrics().density * 200;
					//android.util.Log.d("test_wp", "-----onScrolled()---height= " + height);
					int scrollOffset = recyclerView.computeVerticalScrollOffset();
					totalDy += dy;
					android.util.Log.d("test_wp", "-----onScrolled()---scrollOffset= " + scrollOffset);
					android.util.Log.d("test_wp", "-----onScrolled()---totalDy= " + totalDy);
					if (totalDy > height) {
						stickyView.setVisibility(View.VISIBLE);
					} else {
						stickyView.setVisibility(View.GONE);
					}
					int position = linearLayoutManager.findFirstVisibleItemPosition();
					if(position > 0)stickyContent.setText(originData.get(position - 1).title);
				}
			}
		});
	}
	
	private void initData() {
		for (int i = 'A'; i < 'z'; i++) {
			originData.add(new TestTitleBean("Item " + (char) i, "fgurhgskdg;lsdkjjg,,......"));
		}
	}
	
	public void onFabClick(View view) {
		fabtoolbar.show();
	}
	
	@Override
	public void onBackPressed() {
		if (fabtoolbar.isToolbar()) {
			fabtoolbar.hide();
			return;
		}
		super.onBackPressed();
	}
}
