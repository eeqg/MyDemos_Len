package com.example.mydemos_len.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.mydemos_len.R;

import java.util.List;

/**
 * Created by wp on 2018/10/31.
 */
public class WithTagTextView extends FrameLayout {
	RecyclerView recyclerViewTag;
	TextView tvTitle;
	
	public WithTagTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		
		initView();
	}
	
	private void initView() {
		View rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tag_text_view, this);
		recyclerViewTag = (RecyclerView) rootView.findViewById(R.id.recyclerViewTag);
		recyclerViewTag.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		
		tvTitle = (TextView) findViewById(R.id.tvTitle);
	}
	
	public void setDataInfo(final String title, List<String> tagList) {
		recyclerViewTag.setAdapter(new TagListAdapter(tagList));
		
		ViewTreeObserver observer = recyclerViewTag.getViewTreeObserver();
		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				SpannableString spannableString = new SpannableString(title);
				LeadingMarginSpan.Standard what = new LeadingMarginSpan.Standard(
						recyclerViewTag.getWidth() + dip2px(recyclerViewTag.getContext(), 0), 0);
				spannableString.setSpan(what, 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
				tvTitle.setText(spannableString);
				recyclerViewTag.getViewTreeObserver().removeOnPreDrawListener(this);
				return false;
			}
		});
	}
	
	//adapter
	private class TagListAdapter extends RecyclerView.Adapter {
		private List<String> tagList;
		
		public TagListAdapter(List<String> tagList) {
			this.tagList = tagList;
		}
		
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_list, parent, false);
			return new ItemViewHolder(v);
		}
		
		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			((ItemViewHolder) holder).tvTag.setText(tagList.get(position));
		}
		
		@Override
		public int getItemCount() {
			return tagList.size();
		}
		
		public class ItemViewHolder extends RecyclerView.ViewHolder {
			public TextView tvTag;
			
			public ItemViewHolder(View itemView) {
				super(itemView);
				tvTag = (TextView) itemView.findViewById(R.id.tvTag);
			}
		}
	}
	
	public static int dip2px(Context context, double dpValue) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5);
	}
}
