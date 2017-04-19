package com.example.mydemos_len.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mydemos_len.R;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class TitlebarFragment extends Fragment implements View.OnClickListener{

    private View toolbarleft;
    private View toolbarRight;
    private TitlebarFragmentActionCallBack leftAction;
    private TitlebarFragmentActionCallBack rightAction;

    public interface TitlebarFragmentActionCallBack{
        void onAction();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_toolbar, null);

        toolbarleft = rootView.findViewById(R.id.ivToolbarLeft);
        toolbarleft.setOnClickListener(this);
        toolbarRight = rootView.findViewById(R.id.ivToolbarRight);
        toolbarRight.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setLeftAction(TitlebarFragmentActionCallBack callBack){
        toolbarleft.setVisibility(View.VISIBLE);
        this.leftAction = callBack;
    }

    public void setRightAction(TitlebarFragmentActionCallBack callBack){
        toolbarRight.setVisibility(View.VISIBLE);
        this.rightAction = callBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivToolbarLeft:
                if(leftAction != null){
                    leftAction.onAction();
                }
                break;
            case R.id.ivToolbarRight:
                if(rightAction != null){
                    rightAction.onAction();
                }
                break;
        }
    }
}
