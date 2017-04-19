package com.example.mydemos_len;

import java.util.ArrayList;
import java.util.List;

import com.example.mydemos_len.R;
import com.example.mydemos_len.fragment.AaFragment;
import com.example.mydemos_len.fragment.BbFragment;
import com.example.mydemos_len.fragment.CcFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout.LayoutParams;


public class MainActivity extends FragmentActivity {

	private final static int TAB_COUNT = 3;
    private View mIndicatorView;
	private int mTabBarWidth;
	private int mTabWidth;
	private int mLeftMargin;
	private View view1, view2, view3;//需要滑动的页卡
	private List<View> viewList;//把需要滑动的页卡添加到这个list中 
	private int mIndicatorViewWidth;
	private LayoutParams mIndicatorParams;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	ViewPager viewPager;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);//set no-action-bar.
        
        setContentView(R.layout.activity_main);
        
        viewPager = (ViewPager) findViewById(R.id.viewpager_1);
        initIndicatiorView();
        initPagerViews();
        initFragments();
        
        /* (1)
         * ViewPager + View + PagerAdapter
         */
//        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        
        /* (2)
         * ViewPager + Fragment + PagerFragmentAdapter
         * FragmentActivity
         */
        viewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager())); 
        
        viewPager.setOnPageChangeListener(mPageChangeListener);
        viewPager.setOffscreenPageLimit(2);//初始化当前页和左右2页, 默认为1
    }
	
	private void initIndicatiorView(){
		mIndicatorView = findViewById(R.id.iv_tab_indicator);
		mIndicatorParams = (LayoutParams) mIndicatorView.getLayoutParams();
		mIndicatorView.post(new Runnable() {
			@Override
			public void run() {
				//indicator initial..
				mTabBarWidth = findViewById(R.id.tabBarView).getWidth();
				mIndicatorViewWidth = mIndicatorView.getWidth();
				mTabWidth = mTabBarWidth / TAB_COUNT;
				mLeftMargin = (mTabWidth - mIndicatorViewWidth) / 2;
				mIndicatorParams.leftMargin = mLeftMargin;
				mIndicatorView.setLayoutParams(mIndicatorParams);
			}
		});
		findViewById(R.id.textView_page1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(0);
			}
		});
		findViewById(R.id.textView_page2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(1);
			}
		});
		findViewById(R.id.textView_page3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(2);
			}
		});
	}
	
	private void initPagerViews(){
		LayoutInflater lf = getLayoutInflater().from(this);  
        view1 = lf.inflate(R.layout.layout_pager01, null);  
        view2 = lf.inflate(R.layout.layout_pager02, null);  
        view3 = lf.inflate(R.layout.layout_pager03, null);  
  
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中  
        viewList.add(view1);  
        viewList.add(view2);  
        viewList.add(view3);  
	}
	
	private void initFragments(){
		fragments.add(new AaFragment());
		fragments.add(new BbFragment());
		fragments.add(new CcFragment());
	}
    
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		float leftMargin;
		float offset;
		
		@Override
		public void onPageSelected(int position) {
			//Log.d("test", "onPageSelected()--position="+position);
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			//update indicator view
			offset = mTabWidth * positionOffset;
			leftMargin = mLeftMargin + offset + mTabWidth * (position);
			mIndicatorParams.leftMargin = (int) leftMargin;
			mIndicatorView.setLayoutParams(mIndicatorParams);
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			//Log.d("test", "onPageScrollStateChanged()--state="+state);
		}
	};
	
    private class ViewPagerAdapter extends PagerAdapter{
    	private List<View> mListViews;  
        
        public ViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。  
        }  

		@Override
		public int getCount() {
			return mListViews.size();//返回页卡的数量  ;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
    	
		@Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position));//删除页卡  
        }  
  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
             container.addView(mListViews.get(position), 0);//添加页卡  
             return mListViews.get(position);  
        }  
    }
    
    private class ViewPagerFragmentAdapter extends FragmentPagerAdapter{

		public ViewPagerFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
    	
    }
}
