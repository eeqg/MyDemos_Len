package com.example.mydemos_len.activitys;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.example.mydemos_len.R;

public class AboutTextActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_text);
		ActionBar actionBar = getActionBar();
        actionBar.setTitle("AboutText");
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
		
		TextView text1 = (TextView) findViewById(R.id.about_textView1);
		TextView text2 = (TextView) findViewById(R.id.about_textView2);
		text1.setText(getString(R.string.test_text1, 10, 3));
		text2.setText(String.format(getString(R.string.test_text2), 12, 5));
		
		findViewById(R.id.button_test1).setOnClickListener(new OnClickListener() {

			private AlertDialog mDialog;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View dialogView = LayoutInflater.from(AboutTextActivity.this).inflate(R.layout.layout_pswd_dialog, null);
		    	
		        mDialog = new AlertDialog.Builder(AboutTextActivity.this)
		        .setView(dialogView)
		        .create();
		        mDialog.setCanceledOnTouchOutside(false);
		        mDialog.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_text, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
