package com.example.mydemos_len.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.mydemos_len.R;
import com.example.mydemos_len.fragment.TitlebarFragment;

public class SearchViewActivity extends Activity {

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid", "asdfg"};
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_view);

        TitlebarFragment toolbar = (TitlebarFragment) getFragmentManager().findFragmentById(R.id.toolbar);
        toolbar.setLeftAction(new TitlebarFragment.TitlebarFragmentActionCallBack() {
            @Override
            public void onAction() {
                SearchViewActivity.this.onBackPressed();
            }
        });

        mSearchView = (SearchView) findViewById(R.id.searchView2);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        //
        mSearchView.setSubmitButtonEnabled(false);

    }

}
