package com.example.mydemos_len.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mydemos_len.R;
import com.example.mydemos_len.widget.TagContainerLayout;
import com.example.mydemos_len.widget.TagView;

import java.util.ArrayList;
import java.util.List;

public class TagViewActivity extends Activity {
	private TagContainerLayout mTagContainerLayout1, mTagContainerLayout2,
			mTagContainerLayout3, mTagContainerLayout4, mTagcontainerLayout5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_view);
		
		List<String> list1 = new ArrayList<String>();
		list1.add("Java");
		list1.add("C++");
		list1.add("Python");
		list1.add("Swift");
		list1.add("你好，这是一个TAG。你好，这是一个TAG。你好，这是一个TAG。你好，这是一个TAG。");
		list1.add("PHP");
		list1.add("JavaScript");
		list1.add("Html");
		list1.add("Welcome to use AndroidTagView!");
		
		List<String> list2 = new ArrayList<String>();
		list2.add("China");
		list2.add("USA");
		list2.add("Austria");
		list2.add("Japan");
		list2.add("Sudan");
		list2.add("Spain");
		list2.add("UK");
		list2.add("Germany");
		list2.add("Niger");
		list2.add("Poland");
		list2.add("Norway");
		list2.add("Uruguay");
		list2.add("Brazil");
		
		String[] list3 = new String[]{"Persian", "波斯语", "فارسی", "Hello", "你好", "سلام"};
		String[] list4 = new String[]{"Adele", "Whitney Houston"};
		
		List<String> list5 = new ArrayList<String>();
		list5.add("Custom Red Color");
		list5.add("Custom Blue Color");
		
		mTagContainerLayout1 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);
		mTagContainerLayout2 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout2);
		mTagContainerLayout3 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout3);
		mTagContainerLayout4 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout4);
		mTagcontainerLayout5 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout5);
		
		// Set custom click listener
		mTagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
			@Override
			public void onTagClick(int position, String text) {
				Toast.makeText(TagViewActivity.this, "click-position:" + position + ", text:" + text,
						Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onTagLongClick(final int position, String text) {
				AlertDialog dialog = new AlertDialog.Builder(TagViewActivity.this)
						.setTitle("long click")
						.setMessage("You will delete this tag!")
						.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mTagContainerLayout1.removeTag(position);
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.create();
				dialog.show();
			}
			
			@Override
			public void onTagCrossClick(int position) {
				//                mTagContainerLayout1.removeTag(position);
				Toast.makeText(TagViewActivity.this, "Click TagView cross! position = " + position,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		// After you set your own attributes for TagView, then set tag(s) or add tag(s)
		mTagContainerLayout1.setTags(list1);
		mTagContainerLayout2.setTags(list2);
		mTagContainerLayout3.setTags(list3);
		mTagContainerLayout4.setTags(list4);
		
		ArrayList<int[]> colors = new ArrayList<int[]>();
		//int[]color = {backgroundColor, tagBorderColor, tagTextColor}
		int[] col1 = {Color.parseColor("#ff0000"), Color.parseColor("#000000"), Color.parseColor("#ffffff")};
		int[] col2 = {Color.parseColor("#0000ff"), Color.parseColor("#000000"), Color.parseColor("#ffffff")};
		
		colors.add(col1);
		colors.add(col2);
		
		mTagcontainerLayout5.setTags(list5,colors);
		final EditText text = (EditText) findViewById(R.id.text_tag);
		Button btnAddTag = (Button) findViewById(R.id.btn_add_tag);
		btnAddTag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mTagContainerLayout1.addTag(text.getText().toString());
				// Add tag in the specified position
				//                mTagContainerLayout1.addTag(text.getText().toString(), 4);
			}
		});
	}
}
