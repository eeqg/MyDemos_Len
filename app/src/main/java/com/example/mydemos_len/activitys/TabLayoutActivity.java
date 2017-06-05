package com.example.mydemos_len.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.example.mydemos_len.R;
import com.example.mydemos_len.adapter.TabFragmentAdapter;
import com.example.mydemos_len.databinding.ActivityTablayoutBinding;

public class TabLayoutActivity extends AppCompatActivity {
	
	private ActivityTablayoutBinding dataBinding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_tablayout);
		
		observeContent();
	}
	
	private void observeContent(){
		TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
		this.dataBinding.viewPager.setAdapter(tabFragmentAdapter);
		
		//this.dataBinding.tabLayout.setupWithViewPager(this.dataBinding.viewPager);
		
		this.dataBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				dataBinding.viewPager.setCurrentItem(tab.getPosition());
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				
			}
		});
	}
}
