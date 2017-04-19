package com.example.mydemos_len.activitys;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydemos_len.R;

public class WidgetActivity extends Activity implements OnItemClickListener, OnClickListener {

	//base on "http://blog.csdn.net/top_code/article/details/9326129" .
	private AutoCompleteTextView mAutoCompleteView;
	private ArrayList<Personer> personListAll;
	private ArrayList<Personer> mPersonListShow;
	private PhoneAdapter mAdapter;
	private WindowManager windowManager;
	private LayoutParams parms;
	private TextView toastView;
	private TimeCountDown mToastTimer;
	private int animStyleId = android.R.style.Animation_Toast; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		setContentView(R.layout.activity_widget);
		
		loadPhoneData();
		
		mAutoCompleteView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		mAdapter = new PhoneAdapter(personListAll);
		mAutoCompleteView.setAdapter(mAdapter);
		mAutoCompleteView.setThreshold(1);  //设置输入一个字符 提示，默认为2  
		mAutoCompleteView.setOnItemClickListener(this);
		
		findViewById(R.id.button_customToast).setOnClickListener(this);
		
		//initCustomViewGroup();
	}

	private void initCustomViewGroup() {
		ViewGroup customView = (ViewGroup) findViewById(R.id.customViewGroup1);
		TextView view1 = new TextView(this);
		view1.setText("AAA");
		view1.setBackgroundColor(Color.parseColor("#80ff0000"));
		TextView view2 = new TextView(this);
		view2.setText("BBB");
		view2.setBackgroundColor(Color.parseColor("#8000ff00"));
		TextView view3 = new TextView(this);
		view3.setText("CCC");
		view3.setBackgroundColor(Color.parseColor("#800000ff"));
		
		customView.addView(view1);
		customView.addView(view2);
		customView.addView(view3);
	}
	
	private void loadPhoneData() {
		// initial data.
		personListAll = new ArrayList<Personer>();
		personListAll.clear();
		
		Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
		if(cursor != null && cursor.getCount() > 0){
			while(cursor.moveToNext()){
				String name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
				String phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				phoneNumber = phoneNumber.replaceFirst("\\+86", "").replaceAll("-", "").trim().replaceAll("\\s*", "");
				long contactId = cursor.getLong(cursor.getColumnIndex(Phone.CONTACT_ID));
				long photoId = cursor.getLong(cursor.getColumnIndex(Phone.PHOTO_ID));
				Bitmap photo = null;
				if(photoId > 0){
					Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);  
                    InputStream is = Contacts.openContactPhotoInputStream(getContentResolver(), uri); 
					photo = BitmapFactory.decodeStream(is);
				}else{
					photo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
				}
				//Log.d("test_wp", name+" :  "+phoneNumber);
				
				personListAll.add(new Personer(name, phoneNumber, photo));
			}
			
			cursor.close();
		}
		Log.d("test_wp", "personList="+personListAll);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		String phoneNumber = mPersonListShow.get(position).phoneNumber;
		mAutoCompleteView.setText(phoneNumber);
		mAutoCompleteView.setSelection(phoneNumber.length());//设置光标位置
	}

	private class PhoneAdapter extends BaseAdapter implements Filterable{

		private ArrayFilter arrayFilter;
		
		public PhoneAdapter(ArrayList<Personer> personListShow) {
			super();
			mPersonListShow= personListShow;
		}

		@Override
		public int getCount() {
			return mPersonListShow.size();
		}

		@Override
		public Object getItem(int position) {
			return mPersonListShow.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = View.inflate(getApplicationContext(), R.layout.item_person, null);
				holder = new ViewHolder();
				holder.photoView = (ImageView) convertView.findViewById(R.id.item_photo);
				holder.tvName = (TextView) convertView.findViewById(R.id.item_name);
				holder.tvPhoneNumber = (TextView) convertView.findViewById(R.id.item_number);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			Personer personer = mPersonListShow.get(position);
			holder.photoView.setImageBitmap(personer.photo);
			holder.tvName.setText(personer.name);
			holder.tvPhoneNumber.setText(personer.phoneNumber);
			
			return convertView;
		}

		@Override
		public Filter getFilter() {
			//过滤实现
			if(arrayFilter == null){
				arrayFilter = new ArrayFilter();
			}
			return arrayFilter;
		}
		
		class ViewHolder{
			public TextView tvName;
			public TextView tvPhoneNumber;
			public ImageView photoView;
		}
		
	}
	
	private class Personer{
		String name, phoneNumber;
		Bitmap photo;
		public Personer(String name, String phoneNumber, Bitmap photo) {
			this.name = name;
			this.phoneNumber = phoneNumber;
			this.photo = photo;
		}
		@Override
		public String toString() {
			return "Personer [name=" + name + ", phoneNumber=" + phoneNumber
					+ ", photo=" + photo + "]";
		}
	}
	
	private class ArrayFilter extends Filter {

		private ArrayList<Personer> mUnfilteredData;

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if(mUnfilteredData  == null){
				mUnfilteredData = new ArrayList<Personer>(mPersonListShow);
			}
			
			if(constraint == null || constraint.length() == 0){
				filterResults.values = mUnfilteredData;
				filterResults.count = mUnfilteredData.size();
			}else{
				String constraintString = constraint.toString().toLowerCase();
				ArrayList<Personer> newValues = new ArrayList<Personer>();
				for(int i= 0; i < mUnfilteredData.size(); i++){
					Personer personer = mUnfilteredData.get(i);
					if(personer != null){
						if(personer.phoneNumber != null && personer.phoneNumber.startsWith(constraintString)){
							newValues.add(personer);
						}else if(personer.name != null && personer.name.startsWith(constraintString)){
							newValues.add(personer);
						}
					}
				}
				filterResults.values = newValues;
				filterResults.count = newValues.size();
			}
			
			return filterResults;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			mPersonListShow = (ArrayList<Personer>) results.values;
			if (results.count > 0) {  
                mAdapter.notifyDataSetChanged();  
            } else {  
                mAdapter.notifyDataSetInvalidated();  
            }  
		}
	}
	
	private void showOrHideToastView(boolean show, String text){
    	Log.d("test_wp", "--showOrHideToastView()--show="+show);
    	if(toastView == null || windowManager == null){
    		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    		parms = new WindowManager.LayoutParams();
    		parms.flags  = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE  
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
    		parms.width = WindowManager.LayoutParams.WRAP_CONTENT;
    		parms.height = WindowManager.LayoutParams.WRAP_CONTENT;
    		parms.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    		//parms.type = WindowManager.LayoutParams.TYPE_TOAST;
    		parms.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
    		parms.format = PixelFormat.RGBA_8888;
    		parms.windowAnimations = animStyleId;
    		parms.y = getResources().getDisplayMetrics().widthPixels / 5;
    		
    		toastView = new TextView(this);
    		toastView.setFocusable(false);
    		toastView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
    		toastView.setText(text);
    		toastView.setTextColor(Color.parseColor("#ffffff"));
    		toastView.setTextSize(16);
    		//toastView.setBackgroundResource(R.drawable.bg_pr_toast);
    		toastView.setBackground(getResources().getDrawable(android.R.drawable.toast_frame));
    	}
    	
        //mToastView.setVisibility(show?View.VISIBLE:View.GONE);
        //mToastView.setText(text);
        if(show){
            if(mToastTimer == null){
                mToastTimer = new TimeCountDown(6*1000, 1000, 0);
            }
            mToastTimer.cancel();
            mToastTimer.start();
            if(toastView.getParent() != null){
            	windowManager.removeView(toastView);
            }
            windowManager.addView(toastView, parms );
        }else{
        	windowManager.removeView(toastView);
        }
    }

	private class TimeCountDown extends CountDownTimer{

        int mType;
        public TimeCountDown(long millisInFuture, long countDownInterval, int type) {
            super(millisInFuture, countDownInterval);
            mType = type;
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            if(mType == TYPE_BTN){
//                mSendSmsView.setEnabled(false);
//                mSendSmsView.setText(millisUntilFinished / 1000 + getString(R.string.lenovo_retrieve_password_text2));
//            }else if(mType == TYPE_TOAST){
//                //do nothing.
//            }
        }

        @Override
        public void onFinish() {
//            if(mType == TYPE_BTN){
//                mSendSmsView.setEnabled(true);
//                mSendSmsView.setText(isAirModeOn()?getString(R.string.lenovo_retrieve_password_closeAirMode)
//                        :getString(R.string.lenovo_retrieve_password_getpassword));
//                mRandomCode = "";    //reset code.
//            }else if(mType == TYPE_TOAST){
                showOrHideToastView(false, null);
//            }
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_customToast:
			showOrHideToastView(true, "test-test-电话号码-test-test");
			break;

		default:
			break;
		}
	}
}
