package com.example.mydemos_len.activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.mydemos_len.R;
import com.example.mydemos_len.fragment.CcFragment;
import com.example.mydemos_len.fragment.TitlebarFragment;

public class TableHostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_table_host);

        TitlebarFragment toolbar = (TitlebarFragment) getFragmentManager().findFragmentById(R.id.titlebar);
        toolbar.setLeftAction(new TitlebarFragment.TitlebarFragmentActionCallBack() {
            @Override
            public void onAction() {
                TableHostActivity.this.onBackPressed();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.tabViewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new CcFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "page1";
                case 1:
                    return "page2";
                case 2:
                    return "page3";
            }
            return super.getPageTitle(position);
        }
    }
}
