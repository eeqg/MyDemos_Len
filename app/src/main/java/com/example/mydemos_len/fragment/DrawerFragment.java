package com.example.mydemos_len.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mydemos_len.R;
import com.example.mydemos_len.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class DrawerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
	private final int LOAD_TYPE_REFRESH = 0;
	private final int LOAD_TYPE_LOAD = 1;
	private final int TYPE_ITEM = 0;
	private final int TYPE_FOOTER = 1;
	private final int TOTAL = 50;
	private List<String> mDatas = new ArrayList<String>();
	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView recyclerView;
	private LinearLayoutManager linearLayoutManager;
	MyRecyclerViewAdapter myRecyclerViewAdapter;
	private int lastVisibleItemPosition;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_drawerfragment, container, false);
		
		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlDrawer);
		swipeRefreshLayout.setColorSchemeColors(
				ContextCompat.getColor(getContext(), R.color.colorSwipeRefreshProgressInner1),
				ContextCompat.getColor(getContext(), R.color.colorSwipeRefreshProgressInner2),
				ContextCompat.getColor(getContext(), R.color.colorSwipeRefreshProgressInner3));
		swipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getContext(),
				R.color.colorSwipeRefreshProgressOuter));
		swipeRefreshLayout.setOnRefreshListener(this);
		
		//iniData();
		
		recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDrawer);
		linearLayoutManager = new LinearLayoutManager(this.getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.addItemDecoration(new SpaceItemDecoration());
		myRecyclerViewAdapter = new MyRecyclerViewAdapter(mDatas);
		recyclerView.setAdapter(myRecyclerViewAdapter);
		recyclerView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
			}
			
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				Log.d("test_wp","--onScrollStateChanged()--lastVisibleItemPosition="+lastVisibleItemPosition);
				Log.d("test_wp","--onScrollStateChanged()--getItemCount="+myRecyclerViewAdapter.getItemCount());
				if (newState == RecyclerView.SCROLL_STATE_IDLE
						&& lastVisibleItemPosition == myRecyclerViewAdapter.getItemCount() - 1) {
					Log.d("test_wp","--onScrollStateChanged()--mDatas.size()="+mDatas.size());
					if (mDatas.size() < TOTAL) {
						loadingData(LOAD_TYPE_LOAD);
					}
				}
			}
		});
		
		loadingData(LOAD_TYPE_REFRESH);
		
		return rootView;
	}
	
	private void iniData() {
		for (int i = 1; i <= 20; i++) {
			mDatas.add("item " + i);
		}
	}
	
	private void addData() {
		int size = mDatas.size();
		for (int i = size + 1; i <= size + 20; i++) {
			if(mDatas.size() >= TOTAL){
				return;
			}
			mDatas.add("item " + i);
		}
	}
	
	@Override
	public void onRefresh() {
		loadingData(LOAD_TYPE_REFRESH);
	}
	
	private void loadingData(int type){
		Log.d("test_wp","--loadingData()--type="+type);
		if(type == LOAD_TYPE_REFRESH){
			swipeRefreshLayout.setRefreshing(true);
			if(mDatas!=null)mDatas.clear();
			iniData();
		}else if(type == LOAD_TYPE_LOAD){
			addData();
		}
		Observable.timer(2000, TimeUnit.MILLISECONDS)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Long>() {
					@Override
					public void call(Long aLong) {
						myRecyclerViewAdapter.notifyDataSetChanged();
						swipeRefreshLayout.setRefreshing(false);
					}
				});
		/**
		 * RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
		 * RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
		 */
	}
	
	public static boolean isVisBottom(RecyclerView recyclerView){
		LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
		//屏幕中最后一个可见子项的position
		int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
		//当前屏幕所看到的子项个数
		int visibleItemCount = layoutManager.getChildCount();
		//当前RecyclerView的所有子项个数
		int totalItemCount = layoutManager.getItemCount();
		//RecyclerView的滑动状态
		int state = recyclerView.getScrollState();
		if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE){
			return true;
		}else {
			return false;
		}
	}
	
	private class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
		private List<String> data;
		
		public MyRecyclerViewAdapter(List<String> data) {
			this.data = data;
			Log.d("test_wp","--MyRecyclerViewAdapter()--data.size="+data.size());
		}
		
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if (viewType == TYPE_ITEM) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
				return new ItemViewHolder(view);
			} else if (viewType == TYPE_FOOTER) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_footer, parent, false);
				return new FooterViewHolder(view);
			}
			return null;
		}
		
		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			Log.d("test_wp","--onBindViewHolder()--position="+position);
			Log.d("test_wp","--onBindViewHolder()--getItemCount()="+getItemCount());
			if (holder instanceof ItemViewHolder) {
				((ItemViewHolder) holder).tvName.setText(data.get(position));
			}else{
				if(getItemCount() == TOTAL + 1){
					((FooterViewHolder) holder).progressBar.setVisibility(View.GONE);
					((FooterViewHolder) holder).tvLabel.setText("completed!");
				}else{
					((FooterViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
					((FooterViewHolder) holder).tvLabel.setText("loading...");
				}
			}
		}
		
		@Override
		public int getItemCount() {
			return data.size() + 1;
		}
		
		@Override
		public int getItemViewType(int position) {
			if (position == getItemCount() - 1) {
				return TYPE_FOOTER;
			} else {
				return TYPE_ITEM;
			}
		}
		
		private class ItemViewHolder extends RecyclerView.ViewHolder {
			public TextView tvName;
			
			public ItemViewHolder(View view) {
				super(view);
				tvName = (TextView) view.findViewById(R.id.item_name);
			}
		}
		
		private class FooterViewHolder extends RecyclerView.ViewHolder {
			public TextView tvLabel;
			public View progressBar;
			public FooterViewHolder(View view) {
				super(view);
				tvLabel = (TextView) view.findViewById(R.id.tvFooterLabel);
				progressBar = view.findViewById(R.id.pbIndicator);
			}
		}
	}
	
}
