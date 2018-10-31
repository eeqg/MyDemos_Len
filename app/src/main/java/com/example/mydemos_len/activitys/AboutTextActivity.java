package com.example.mydemos_len.activitys;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.example.mydemos_len.R;
import com.example.mydemos_len.widget.WithTagTextView;

import java.util.ArrayList;
import java.util.List;

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
		
		TextView tvTag = (TextView) findViewById(R.id.tvTag);
		Spanned spanned = Html.fromHtml(
				"<div style=\"display: block;background: #ff4f30;padding:1px;float: left;border-radius:4px;color:#ffffff\">包邮</div>");
		tvTag.setText(spanned);
		
		String pre = "pm";
		String time = "18:00";
		((TextView) findViewById(R.id.tvTag2)).setText(Html.fromHtml("<b><small><font color=#ff0000>"
				+ pre + "</b><small/><font/>" +
				"<big>" + time + "</big>"));
		
		//
		TextView tvAA = (TextView) findViewById(R.id.tvAA);
		TextView tvVVV = (TextView) findViewById(R.id.tvVVV);
		calculateTag2(tvAA, tvVVV, "加大的开发");
		
		// SpannableStringBuilder span = new SpannableStringBuilder("缩进" + data.getMainTitle());
		// span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
		// 		Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		// itemTitleTv.setText(span);
		
		List<String> tagList = new ArrayList<>();
		tagList.add("新品");
		tagList.add("包邮");
		tagList.add("包邮rr");
		WithTagTextView wtvTag = (WithTagTextView) findViewById(R.id.wtvTag);
		wtvTag.setDataInfo("加大的开发加大的开发加大的开发加大的开发加大的开发加大的开发加大的开蛋黄酥 芋泥味 莲蓉味 128g/盒", tagList);
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
	
	//方案一:将文字查分为两个两个TextView 显示
	public static void calculateTag1(final TextView first, final TextView second, final String text) {
		ViewTreeObserver observer = first.getViewTreeObserver();
		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				Layout layout = first.getLayout();
				int lineEnd = layout.getLineEnd(0);
				String substring = text.substring(0, lineEnd);
				String substring1 = text.substring(lineEnd, text.length());
				Log.i("TAG", "onGlobalLayout:" + "+end:" + lineEnd);
				Log.i("TAG", "onGlobalLayout: 第一行的内容::" + substring);
				Log.i("TAG", "onGlobalLayout: 第二行的内容::" + substring1);
				if (TextUtils.isEmpty(substring1)) {
					second.setVisibility(View.GONE);
					second.setText(null);
				} else {
					second.setVisibility(View.VISIBLE);
					second.setText(substring1);
				}
				first.getViewTreeObserver().removeOnPreDrawListener(
						this);
				return false;
			}
		});
	}
	
	//方案二:动态设置缩进距离的方式
	public void calculateTag2(final TextView tag, final TextView title, final String text) {
		ViewTreeObserver observer = tag.getViewTreeObserver();
		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				SpannableString spannableString = new SpannableString(text);
				//这里没有获取margin的值,而是直接写死的
				LeadingMarginSpan.Standard what = new LeadingMarginSpan.Standard(tag.getWidth() + dip2px(tag.getContext(), 0), 0);
				spannableString.setSpan(what, 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
				title.setText(spannableString);
				tag.getViewTreeObserver().removeOnPreDrawListener(this);
				return false;
			}
		});
	}
	
	public static int dip2px(Context context, double dpValue) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5);
	}
}
