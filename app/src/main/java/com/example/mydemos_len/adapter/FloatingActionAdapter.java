package com.example.mydemos_len.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydemos_len.APP;
import com.example.mydemos_len.R;
import com.example.mydemos_len.bean.TestTitleBean;
import com.example.mydemos_len.utils.LogUtils;

import java.util.ArrayList;

public class FloatingActionAdapter extends RecyclerView.Adapter {
	private Context mContext;
	private ArrayList<TestTitleBean> attempData = new ArrayList<TestTitleBean>();
	private OnItemClickListener listener;
	private final int TYPE_ITEM = 1;
	private final int TYPE_STICKY = 2;
	private final int TYPE_HEADER = 3;
	
	public FloatingActionAdapter(Context context, ArrayList<TestTitleBean> data) {
		this.mContext = context;
		this.attempData = data;
		LogUtils.d(data.size());
	}
	
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_HEADER) {
			View view = LayoutInflater.from(mContext).inflate(R.layout.item_floating_header, null);
			return new HeaderViewHolder(view);
		} else if (viewType == TYPE_STICKY) {
			View view = LayoutInflater.from(mContext).inflate(R.layout.layout_folating_sticky, null);
			return new StickyViewHolder(view);
		} else {
			View view = LayoutInflater.from(mContext).inflate(R.layout.item_title, null);
			return new MyViewHolder(view);
		}
	}
	
	public void setOnItemClickListener(OnItemClickListener l) {
		this.listener = l;
	}
	
	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
		if (holder instanceof MyViewHolder) {
			
			((MyViewHolder) holder).titleView.setText(attempData.get(position - 2).title);
			((MyViewHolder) holder).subView.setText(attempData.get(position - 2).subTitle);
			
			if (listener != null) {
				//click
				((MyViewHolder) holder).contentView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						listener.onItemClick(((MyViewHolder) holder).contentView, position - 2, attempData.get(position - 2).title);
					}
				});
			}
		}
	}
	
	@Override
	public int getItemCount() {
		return attempData.size() + 1;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_HEADER;
		}
		if (position == 1) {
			return TYPE_STICKY;
		}
		return TYPE_ITEM;
	}
	
	private class HeaderViewHolder extends RecyclerView.ViewHolder {
		
		public HeaderViewHolder(View itemView) {
			super(itemView);
		}
	}
	
	private class StickyViewHolder extends RecyclerView.ViewHolder {
		
		public StickyViewHolder(View itemView) {
			super(itemView);
		}
	}
	
	private class MyViewHolder extends RecyclerView.ViewHolder {
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