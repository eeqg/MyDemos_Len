package com.example.mydemos_len.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydemos_len.R;
import com.example.mydemos_len.bean.TestTitleBean;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
	private Context mContext;
	private ArrayList<TestTitleBean> attempData = new ArrayList<TestTitleBean>();
	private OnItemClickListener listener;
	
	public MyAdapter(Context context, ArrayList<TestTitleBean> data){
		this.mContext = context;
		this.attempData = data;
		LogUtils.d(data.size());
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_title, null);
		return new MyViewHolder(view);
	}
	
	public void setOnItemClickListener(OnItemClickListener l) {
		this.listener = l;
	}
	
	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		holder.titleView.setText(attempData.get(position).title);
		holder.subView.setText(attempData.get(position).subTitle);
		
		if (listener != null) {
			//click
			holder.contentView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					listener.onItemClick(holder.contentView, position, attempData.get(position).title);
				}
			});
		}
	}
	
	@Override
	public int getItemCount() {
		return attempData.size();
	}
	
	
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public View contentView;
		public ImageView imageView;
		public TextView titleView;
		public TextView subView;
		
		public MyViewHolder(View view) {
			super(view);
			contentView = view.findViewById(R.id.contentView);
			imageView = (ImageView) view.findViewById(R.id.item_photo);
			titleView = (TextView) view.findViewById(R.id.item_name);
			subView = (TextView) view.findViewById(R.id.item_number);
		}
	}
	
	public interface OnItemClickListener {
		void onItemClick(View view, int position, String title);
		void onItemLongClick(View view, int position);
	}
}