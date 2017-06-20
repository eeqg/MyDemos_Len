package com.example.mydemos_len.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.BackgroundToForegroundTransformer;
import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.ToxicBakery.viewpager.transforms.FlipVerticalTransformer;
import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.mydemos_len.R;
import com.example.mydemos_len.adapter.ImagePagerAdapter;
import com.example.mydemos_len.utils.LocalImageHolderView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BannerViewActivity extends Activity {
	private ArrayList<Integer> localImages = new ArrayList<Integer>();
	private ArrayList<String> transformerList = new ArrayList<String>();
	private ConvenientBanner convenientBanner;
	private ViewPager viewpager;
	private int transformerIndex = 0;
	private Subscription subscription;
	private boolean autoLoop = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banner_view);
		
		initData();
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//开始自动翻页
		convenientBanner.startTurning(3000);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		//停止翻页
		convenientBanner.stopTurning();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(subscription != null)subscription.unsubscribe();
	}
	
	private void initData(){
		loadTestData();
	}
	
	private void initView(){
		viewpager = (ViewPager) findViewById(R.id.viewpager_banner);
		viewpager.setAdapter(new ImagePagerAdapter<Integer>(localImages));
		viewpager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()){
					case MotionEvent.ACTION_DOWN:
						autoLoop = false;
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_OUTSIDE:
						autoLoop = true;
						break;
				}
				return false;
			}
		});
		
		//startLoop();
		
		int itemMod = (Integer.MAX_VALUE / 2) % localImages.size();
		Log.d("test_wp","itemMod=="+itemMod);
		viewpager.setCurrentItem(Integer.MAX_VALUE / 2 - itemMod);//初始化显示第一页
		
		viewpager.setOffscreenPageLimit(5);
		viewpager.setPageTransformer(false, new ViewPager.PageTransformer() {
			@Override
			public void transformPage(View view, float position) {
				float maxXScale = 0.5F;
				float minXScale = 0.3F;
				float maxYScale = 0.76F;
				float minYScale = 0.6F;
				
				android.util.Log.d("test_wp", "BannerViewActivity--transformPager()--position="+position);
				if (position == 0) {
					ViewCompat.setScaleX(view, maxXScale);
					ViewCompat.setScaleY(view, maxYScale);
					ViewCompat.setTranslationX(view, 0);
				} else if (position < 0) {//滑出的页
					ViewCompat.setScaleX(view, maxXScale + (maxXScale - minXScale) * position);
					ViewCompat.setScaleY(view, maxYScale + (maxYScale - minYScale) * position);
					ViewCompat.setTranslationX(view, view.getWidth() * -position * 0.55F);
				} else if (position > 0) {//滑进的页
					ViewCompat.setScaleX(view, maxXScale - (maxXScale - minXScale) * position);
					ViewCompat.setScaleY(view, maxYScale - (maxYScale - minYScale) * position);
					ViewCompat.setTranslationX(view, view.getWidth() * -position * 0.55F);
				}
				
				// float alpha = 0.0f;
				// if (0.0f <= position && position <= 1.0f) {
				// 	alpha = 1.0f - position + 0.35f;
				// } else if (-1.0f <= position && position < 0.0f) {
				// 	alpha = position + 1.0f + 0.35f;
				// }
				// view.setAlpha(alpha);
			}
		});
		
		//- - - - - - - - - - - - - - -- - - -
		
		
		
		//- - - -- - - - - - - - - - - - - - -- - - - - -
		
		convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner1);
		convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
										@Override
										public LocalImageHolderView createHolder() {
											return new LocalImageHolderView();
										}
									}, localImages)
						.setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(int position) {
								Toast.makeText(BannerViewActivity.this,"点击了第"+position+"个",Toast.LENGTH_SHORT).show();
							}
						});
		findViewById(R.id.changeTransformer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(transformerList.size() > 0){
					if(transformerIndex == transformerList.size() - 1){
						transformerIndex = 0;
					}else {
						transformerIndex ++;
					}
					//切换翻页效果
					String transforemerName = transformerList.get(transformerIndex);
					try {
						Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + transforemerName);
						ABaseTransformer transforemer= (ABaseTransformer)cls.newInstance();
						convenientBanner.getViewPager().setPageTransformer(true,transforemer);
						
						//部分3D特效需要调整滑动速度
						if(transforemerName.equals("StackTransformer")){
							convenientBanner.setScrollDuration(1200);
						}
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void startLoop(){
		subscription = Observable.interval(3000, TimeUnit.MILLISECONDS)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Action1<Long>() {
					@Override
					public void call(Long aLong) {
						if(!autoLoop) return;
						viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
					}
				});
	}
	
	private void loadTestData() {
		//本地图片集合
		for (int position = 0; position < 7; position++)
			localImages.add(getResId("ic_test_" + position, R.drawable.class));
		
		
		//各种翻页效果
		transformerList.add(DefaultTransformer.class.getSimpleName());
		transformerList.add(AccordionTransformer.class.getSimpleName());
		transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
		transformerList.add(CubeInTransformer.class.getSimpleName());
		transformerList.add(CubeOutTransformer.class.getSimpleName());
		transformerList.add(DepthPageTransformer.class.getSimpleName());
		transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
		transformerList.add(FlipVerticalTransformer.class.getSimpleName());
		transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
		transformerList.add(RotateDownTransformer.class.getSimpleName());
		transformerList.add(RotateUpTransformer.class.getSimpleName());
		transformerList.add(StackTransformer.class.getSimpleName());
		transformerList.add(ZoomInTransformer.class.getSimpleName());
		transformerList.add(ZoomOutTranformer.class.getSimpleName());
		//
		// transformerArrayAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
	 *
	 * @param variableName
	 * @param c
	 * @return
	 */
	public static int getResId(String variableName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(variableName);
			return idField.getInt(idField);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
