package com.example.mydemos_len.activitys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mydemos_len.R;
import com.example.mydemos_len.fragment.DrawerFragment;
import com.example.mydemos_len.fragment.DrawerFragment2;

public class DrawerLayoutActivity extends AppCompatActivity {
	private DrawerLayout mDrawerLayout;
	private DrawerFragment fragment1;
	private DrawerFragment2 fragment2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer_layout);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(getDrawable(R.drawable.bg_actionbar));
		actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		//mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDarkTest);
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		if (navigationView != null) {
			setupDrawerContent(navigationView);
		}
		
		// DrawerFragment drawerFragment = (DrawerFragment)
		// 		getSupportFragmentManager().findFragmentById(R.id.contentFrame);
		// if (drawerFragment == null) {
		// 	drawerFragment = new DrawerFragment();
		// 	getSupportFragmentManager().beginTransaction()
		// 			.add(R.id.contentFrame, drawerFragment)
		// 			.commit();
		// }
		selectFragment(1);
	}
	
	private void selectFragment(int item) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (fragment1 != null) {
			transaction.hide(fragment1);
		}
		if (fragment2 != null) {
			transaction.hide(fragment2);
		}
		switch (item) {
			case 1:
				if (fragment1 == null) {
					fragment1 = new DrawerFragment();
					transaction.add(R.id.contentFrame, fragment1);
				} else {
					transaction.show(fragment1);
				}
				break;
			case 2:
				if (fragment2 == null) {
					fragment2 = new DrawerFragment2();
					transaction.add(R.id.contentFrame, fragment2);
				} else {
					transaction.show(fragment2);
				}
				break;
		}
		transaction.commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				mDrawerLayout.openDrawer(Gravity.START);
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void setupDrawerContent(NavigationView navigationView) {
		//get headerView
		View headerView = navigationView.getHeaderView(0);
		headerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(DrawerLayoutActivity.this, "header view clicked!!", Toast.LENGTH_LONG).show();
			}
		});
		//item click
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_navigationview_item_1:
						selectFragment(1);
						break;
					case R.id.menu_navigationview_item_2:
						selectFragment(2);
						break;
				}
				// Close the navigation drawer when an item is selected.
				// item.setChecked(true);
				mDrawerLayout.closeDrawers();
				return true;
			}
		});
	}
}
