package com.example.mydemos_len.activitys;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mydemos_len.R;
import com.example.mydemos_len.bean.TestTitleBean;
import com.example.mydemos_len.utils.ItemDecoration;
import com.example.mydemos_len.utils.LogUtils;
import com.example.mydemos_len.utils.MyAdapter;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import java.util.ArrayList;

public class FloatingActionActivity extends Activity {
	
	FABToolbarLayout fabtoolbar;
	private ArrayList<TestTitleBean> originData = new ArrayList<TestTitleBean>();
	private MyAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_floating_action);
		
		
		findViewById(R.id.fabtoolbar_fab);
		fabtoolbar = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
		
		initData();
		
		LogUtils.d(originData.size());
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new MyAdapter(this, originData);
		recyclerView.setAdapter(adapter);
		//recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		recyclerView.addItemDecoration(new ItemDecoration(this));
	}
	
	private void initData() {
		for (int i = 'A'; i < 'z'; i++){
			originData.add(new TestTitleBean("Item "+(char)i, "fgurhgskdg;lsdkjjg,,......"));
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
