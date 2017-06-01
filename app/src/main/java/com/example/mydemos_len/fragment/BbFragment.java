package com.example.mydemos_len.fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mydemos_len.R;
import com.example.mydemos_len.activitys.BannerViewActivity;
import com.example.mydemos_len.activitys.DrawerLayoutActivity;
import com.example.mydemos_len.activitys.FloatingActionActivity;
import com.example.mydemos_len.activitys.LocationActivity;
import com.example.mydemos_len.activitys.OkhttpUtilsTestActivity;
import com.example.mydemos_len.activitys.PicturePickActivity;
import com.example.mydemos_len.activitys.SearchViewActivity;
import com.example.mydemos_len.activitys.TableHostActivity;
import com.example.mydemos_len.activitys.TagViewActivity;
import com.example.mydemos_len.bean.TestTitleBean;
import com.example.mydemos_len.utils.DividerItemDecoration;
import com.example.mydemos_len.utils.MyAdapter;

import java.util.ArrayList;

public class BbFragment extends Fragment implements View.OnClickListener{

	private ArrayList<TestTitleBean> originData = new ArrayList<TestTitleBean>();
	private ArrayList<TestTitleBean> attempData = new ArrayList<TestTitleBean>();
	private ArrayList<TestTitleBean> searchData = new ArrayList<TestTitleBean>();
	private MyAdapter adapter;
	EditText searchView;
	View searchLayout;
	View searchDel;
	TextView searchIcon;
	ObjectAnimator animatorMoveLeft;
	ObjectAnimator animatorMoveRight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_pager02, null);
		RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
		searchLayout = rootView.findViewById(R.id.searchLayout);
		searchView = (EditText) rootView.findViewById(R.id.editor_search);
		searchView.addTextChangedListener(new EditTextWatcher());
		searchView.setOnFocusChangeListener(new SearchFocusChangeListener());
		searchView.clearFocus();
		searchDel = rootView.findViewById(R.id.search_del);
		searchDel.setOnClickListener(this);
		searchIcon = (TextView) rootView.findViewById(R.id.tv_search_icon);
		searchIcon.setSelected(false);

		initAnimation();

		initData();

		adapter = new MyAdapter(getActivity(), attempData);
		recyclerView.setAdapter(adapter);
		recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

		adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position, String title) {
				//Log.d("test_wp", BbFragment.this.getClass().getSimpleName()+"--"+position);
				//Toast.makeText(BbFragment.this.getActivity(), ""+position, Toast.LENGTH_SHORT).show();
				switch (title) {
					case "TabHost":
						startActivity(new Intent(BbFragment.this.getActivity(), TableHostActivity.class));
						break;
					case "SearchView":
						startActivity(new Intent(BbFragment.this.getActivity(), SearchViewActivity.class));
						break;
					case "DrawerLayout":
						startActivity(new Intent(BbFragment.this.getActivity(), DrawerLayoutActivity.class));
						break;
					case "PicturePicker":
						startActivity(new Intent(BbFragment.this.getActivity(), PicturePickActivity.class));
						break;
					case "OkHttpUtils":
						startActivity(new Intent(BbFragment.this.getActivity(), OkhttpUtilsTestActivity.class));
						break;
					case "TagView":
						startActivity(new Intent(BbFragment.this.getActivity(), TagViewActivity.class));
						break;
					case "BannerView":
						startActivity(new Intent(BbFragment.this.getActivity(), BannerViewActivity.class));
						break;
					case "Location":
						startActivity(new Intent(BbFragment.this.getActivity(), LocationActivity.class));
						break;
					case "FloatingActionButton":
						startActivity(new Intent(BbFragment.this.getActivity(), FloatingActionActivity.class));
						break;
				}
			}

			@Override
			public void onItemLongClick(View view, int position) {

			}
		});
		
		Log.d("test", "BbFragment--onCreateView()");
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		//initAnimation();
	}

	private void initData(){
		originData.add(new TestTitleBean("TabHost","TabLayout + ViewPager + Fragment"));
		originData.add(new TestTitleBean("SearchView","aaa"));
		originData.add(new TestTitleBean("DrawerLayout","侧滑菜单, 下拉刷新, 上拉加载..."));
		originData.add(new TestTitleBean("PicturePicker","图片选择器"));
		originData.add(new TestTitleBean("OkHttpUtils","test the OkHttp"));
		originData.add(new TestTitleBean("TagView","标签layout"));
		originData.add(new TestTitleBean("BannerView","循环 轮播 viewpager"));
		originData.add(new TestTitleBean("Location","定位"));
		originData.add(new TestTitleBean("FloatingActionButton","FAB"));
		originData.add(new TestTitleBean("",""));

		attempData.addAll(originData);
	}

	private void initAnimation(){
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		searchIcon.measure(w, h);
		searchLayout.measure(w, h);
		float dest = (searchIcon.getMeasuredWidth() - searchLayout.getMeasuredWidth())/2;
		Log.d("test_wp","--------------dest="+dest);
		animatorMoveLeft = ObjectAnimator.ofFloat(searchIcon, "translationX", 0f, -580f);
		animatorMoveLeft.setDuration(300);
		animatorMoveLeft.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				searchIcon.setText("");
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		animatorMoveRight = ObjectAnimator.ofFloat(searchIcon, "translationX", -580f, 0f);
		animatorMoveRight.setDuration(300);

	}

	class EditTextWatcher implements TextWatcher{
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			Log.d("test_wp", BbFragment.this.getClass().getSimpleName()+"--onTextChanged--s="+s);
			searchData.clear();
			for (TestTitleBean testBean: originData) {
				Log.d("test_wp", BbFragment.this.getClass().getSimpleName()+"--onTextChanged--title="+testBean.title);
				if(testBean.title.toUpperCase().contains(s.toString().toUpperCase())){
					searchData.add(testBean);
				}
			}
			Log.d("test_wp", BbFragment.this.getClass().getSimpleName()+"--onTextChanged---searchData.size="+searchData.size());
			attempData.clear();
			if(!searchData.isEmpty()){
				attempData.addAll(searchData);
			}
			adapter.notifyDataSetChanged();
		}
	}

	class SearchFocusChangeListener implements View.OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			Log.d("test_wp", BbFragment.this.getClass().getSimpleName()+"--hasFocus="+hasFocus);
			if(hasFocus){
				animatorMoveLeft.start();
				searchDel.setVisibility(View.VISIBLE);
				//searchIcon.setText("");
			}else{
				animatorMoveRight.start();
				searchDel.setVisibility(View.GONE);
				searchIcon.setText("Search");
				//隐藏软键盘
				InputMethodManager imm = (InputMethodManager) BbFragment.this.getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v == searchDel){
			searchView.setText("");
			searchView.clearFocus();
			attempData.clear();
			attempData.addAll(originData);
		}
	}

	@Override
	public void onStop() {
		Log.d("test", "BbFragment--onStop()");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Log.d("test", "AaFragment--onDestroy()");
		super.onDestroy();
	}

	

}
