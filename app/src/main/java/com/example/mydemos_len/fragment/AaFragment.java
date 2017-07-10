package com.example.mydemos_len.fragment;


import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mydemos_len.R;
import com.example.mydemos_len.activitys.AboutTextActivity;
import com.example.mydemos_len.activitys.ActivitiesActivity;
import com.example.mydemos_len.activitys.AnimationActivity;
import com.example.mydemos_len.activitys.PasswordRetrievalActivity;
import com.example.mydemos_len.activitys.ScrollActivity;
import com.example.mydemos_len.activitys.SmsActivity;
import com.example.mydemos_len.activitys.WebViewActivity;
import com.example.mydemos_len.activitys.WidgetActivity;
import com.example.mydemos_len.activitys.WindowActivity;

public class AaFragment extends ListFragment{

	private List<String> items;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(getContext());
		        progressDialog.setTitle("test.....");
		        progressDialog.show();
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				progressDialog.dismiss();
			}
		}.execute();*/
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.layout_pager01, null);
		
		List<String> items = fillList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
		setListAdapter(adapter);
		
		
		
		
		Log.d("test", "AaFragment--onCreateView()");
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		//getListView().setFadingEdgeLength(0);
		getListView().setOverScrollMode(View.OVER_SCROLL_NEVER);
	}
	
	private List<String> fillList() {
		items = new ArrayList<String>();
		items.add("Animation");
		items.add("Activities");
		items.add("SMS");
		items.add("Widget");
		items.add("WebView");
		items.add("AboutText");
		items.add("About window");
		items.add("About scroll");
		items.add("");
		items.add("");
		items.add("");
		items.add("");
		items.add("");
		items.add("");
		items.add("");
		items.add("");
		items.add("");
		items.add("");
		items.add("");
		
		//...
		items.add("PasswordRetrieval");
		items.add("others");
		
		return items;
	}
	
	private void forTest() {
		android.util.Log.d("test_wp", "-----2-----");
//		try {
//			//Thread.sleep(13000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		int i = 0;
//		while (true) {
//			android.util.Log.d("test_wp", "-----3-----"+i++);
//		}
		android.util.Log.d("test_wp", "-----3-----");
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch (position) {
		case 0:
			//for test
			android.util.Log.d("test_wp", "-----1-----");
			synchronized (this) {
				forTest();
			}
			//
			android.util.Log.d("test_wp", "-----4-----");
			
			startActivity(new Intent(getActivity(), AnimationActivity.class));
			break;
		case 1:
			startActivity(new Intent(getActivity(), ActivitiesActivity.class));
			break;
		case 2:
			startActivity(new Intent(getActivity(), SmsActivity.class));
			break;
		case 3:
			startActivity(new Intent(getActivity(), WidgetActivity.class));
			break;
		case 4:
			startActivity(new Intent(getActivity(), WebViewActivity.class));
			break;
		case 5:
			startActivity(new Intent(getActivity(), AboutTextActivity.class));
			break;
		case 6:
			startActivity(new Intent(getActivity(), WindowActivity.class));
			break;
		case 7:
			startActivity(new Intent(getActivity(), ScrollActivity.class));
			break;
		case 8:
			//startActivity(new Intent(getActivity(), NetworkActivity.class));
			break;
		case 9:
			//startActivity(new Intent(getActivity(), DrawerLayoutActivity.class));
			break;
		case 10:
			//startActivity(new Intent(getActivity(), ImageViewsActivity.class));
			break;
		case 11:
			//startActivity(new Intent(getActivity(), ImageUtilsActivity.class));
			break;
		case 12:
			//startActivity(new Intent(getActivity(), ImageBlurring.class));
			break;
		case 13:
			//startActivity(new Intent(getActivity(), SlidingPanelActivity.class));
			break;
		case 14:
			//startActivity(new Intent(getActivity(), SlidingUpPanelActivity.class));
			break;
		case 15:
			//startActivity(new Intent(getActivity(), WaterDropActivity.class));
			break;
		case 16:
			//startActivity(new Intent(getActivity(), LocationActivity.class));
			break;
		case 17:
			//startActivity(new Intent(getActivity(), WindowsActivity.class));
			break;
		case 18:
			//startActivity(new Intent(getActivity(), RippleEffectActivity.class));
			break;
		}
		
		if(position == items.size() -2){
			startActivity(new Intent(getActivity(), PasswordRetrievalActivity.class));
		}
		if(position == items.size() -1){
			//startActivity(new Intent(getActivity(), OthersActivity2.class));
			
			//System.exit(0);
		}
	}
	

	@Override
	public void onStop() {
		Log.d("test", "AaFragment--onStop()");
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		Log.d("test", "AaFragment--onDestroy()");
		super.onDestroy();
	}
}
