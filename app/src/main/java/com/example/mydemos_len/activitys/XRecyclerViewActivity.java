package com.example.mydemos_len.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mydemos_len.R;
import com.example.mydemos_len.adapter.MyXRecyclerViewAdapter;
import com.example.mydemos_len.databinding.ActivityXrecyclerViewBinding;

import java.util.ArrayList;

import wp.allutils.common.ColorUtils;
import wp.allutils.widget.x_recycler.ProgressStyle;
import wp.allutils.widget.x_recycler.XRecyclerView;

public class XRecyclerViewActivity extends AppCompatActivity {
	private ActivityXrecyclerViewBinding dataBinding;
	private MyXRecyclerViewAdapter adapter;
	private ArrayList<String> listData;
	private int refreshTime = 0;
	private int times = 0;
	
	private CollapsingToolbarLayoutState state;
	
	private enum CollapsingToolbarLayoutState {
		EXPANDED, //展开
		COLLAPSED, //折叠
		INTERNEDIATE //折叠中
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_xrecycler_view);
		
		initData();
		observeContent();
	}
	
	private void initData(){
		listData = new ArrayList<String>();
		for(int i = 0; i < 15 ;i++){
			listData.add("item" + i);
		}
	}
	
	private void observeContent(){
		this.dataBinding.toolbar.setTitle("XRecyclerView");
		this.dataBinding.toolbar.setContentInsetStartWithNavigation(0); //title left margin
		setSupportActionBar(this.dataBinding.toolbar);//将toolbar设置为activity的操作栏了
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);//back
		
		//this.dataBinding.collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER);
		//监听appBar折叠状态
		this.dataBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				if (verticalOffset == 0) {
					if (state != CollapsingToolbarLayoutState.EXPANDED) {
						state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
						dataBinding.setCustomViewVisible(true);
					}
				} else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
					if (state != CollapsingToolbarLayoutState.COLLAPSED) {
						state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
						dataBinding.setCustomViewVisible(false);
					}
				} else {
					if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
						state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
						if(state == CollapsingToolbarLayoutState.COLLAPSED){
							//playButton.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
						}
						//collapsingToolbarLayout.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
					}
				}
			}
		});
		
		this.dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		this.dataBinding.recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		this.dataBinding.recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
		this.dataBinding.recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
		
		final TextView headerView = new TextView(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		headerView.setLayoutParams(params);
		headerView.setPadding(36, 36, 36, 36);
		headerView.setGravity(Gravity.CENTER_HORIZONTAL);
		headerView.setText("this is a header view...");
		headerView.setBackgroundColor(ColorUtils.getRandomColor(20));
		this.dataBinding.recyclerView.addHeaderView(headerView);
		
		this.dataBinding.recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
			@Override
			public void onRefresh() {
				refreshData();
			}
			
			@Override
			public void onLoadMore() {
				loadMore();
			}
		});
		
		adapter = new MyXRecyclerViewAdapter(listData);
		this.dataBinding.recyclerView.setAdapter(adapter);
		this.dataBinding.recyclerView.refresh();//首次自动刷新
	}
	
	private void refreshData(){
		refreshTime ++;
		times = 0;
		new Handler().postDelayed(new Runnable(){
			public void run() {
				
				listData.clear();
				for(int i = 0; i < 15 ;i++){
					listData.add("item" + i + "after " + refreshTime + " times of refresh");
				}
				adapter.notifyDataSetChanged();
				dataBinding.recyclerView.refreshComplete();
			}
			
		}, 1000);
	}
	
	private void loadMore(){
		if(times < 2){
			new Handler().postDelayed(new Runnable(){
				public void run() {
					dataBinding.recyclerView.loadMoreComplete();
					for(int i = 0; i < 15 ;i++){
						listData.add("item" + (i + listData.size()) );
					}
					dataBinding.recyclerView.loadMoreComplete();
					adapter.notifyDataSetChanged();
				}
			}, 1000);
		} else {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					for(int i = 0; i < 9 ;i++){
						listData.add("item" + (1 + listData.size() ) );
					}
					dataBinding.recyclerView.setNoMore(true);
					adapter.notifyDataSetChanged();
				}
			}, 1000);
		}
		times ++;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				onBackPressed();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
