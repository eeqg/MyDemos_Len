package com.example.mydemos_len.activitys;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydemos_len.R;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class ClipDrawableActivity extends RxAppCompatActivity implements View.OnClickListener {
	
	ClipDrawable upgradeViewBackground;
	ClipDrawable avatarDrawable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip_drawable);
		
		observeContent();
	}
	
	private void observeContent() {
		TextView upgradeView = (TextView) findViewById(R.id.tvUpgrade);
		upgradeViewBackground = (ClipDrawable) upgradeView.getBackground();
		
		ImageView avatar = (ImageView) findViewById(R.id.avatar);
		avatarDrawable = (ClipDrawable) avatar.getDrawable();
		
		findViewById(R.id.restart).setOnClickListener(this);
		
		//data
		start();
	}
	
	private void start(){
		Observable.interval(500, 100, TimeUnit.MILLISECONDS)
				.take(20)
				.compose(this.<Long>bindToLifecycle())//当前组件生命周期结束时，自动取消对Observable订阅,
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Long>() {
					@Override
					public void onCompleted() {
						
					}
					
					@Override
					public void onError(Throwable e) {
						
					}
					
					@Override
					public void onNext(Long aLong) {
						android.util.Log.d("test_wp", "ClipDrawableActivity--onNext()--along= "+aLong);
						avatarDrawable.setLevel((int)(aLong+1)*500);
						upgradeViewBackground.setLevel((int)(aLong+1)*500);
					}
				});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.restart:
				avatarDrawable.setLevel(0);
				upgradeViewBackground.setLevel(0);
				start();
				break;
		}
	}
}
