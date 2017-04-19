package com.example.mydemos_len.activitys;

import java.lang.reflect.WildcardType;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mydemos_len.R;

public class SmsActivity extends Activity {

	private InputMethodManager imm;
	private EditText mPhoneNumView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("ACTION_SEND_SMS");
		filter.addAction("ACTION_DELIVER_SMS");
		registerReceiver(mReceiver, filter );
		
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_sendSMS).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//closeSoftInput();
				sendSMS("18759287162", "test_wp_1234567890");
			}
		});
		
		findViewById(R.id.button_contacts).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});
		
		mPhoneNumView = (EditText) findViewById(R.id.editText_phonenum);
	}
	
	private void sendSMS(String number, String msg) {
		// TODO Auto-generated method stub
		SmsManager smsManager = SmsManager.getDefault();
		PendingIntent sentPi = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("ACTION_SEND_SMS"), 0);
		PendingIntent deliverPi = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("ACTION_DELIVER_SMS"), 0);
		
		smsManager.sendTextMessage(number, null, msg, sentPi, deliverPi);
	}
	
	void closeSoftInput(){
		if(imm == null){
	    	   imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	       }
	      // if(imm.isActive()){
	       //     imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	      // }
		View view = this.getCurrentFocus(); 
		if(view != null)
		imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null) return;
		Uri uri = data.getData();
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		if(cursor != null && cursor.getCount() > 0){
			while (cursor.moveToNext()) {
				String number = null;
				String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				Cursor cursor2 = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID+"="+contactId, null, null);
				while(cursor2.moveToNext()){
					number = cursor2.getString(cursor2.getColumnIndex(Phone.NUMBER));
					Log.d("test_wp", "name="+name+", number="+number);
				}
				
				mPhoneNumView.setText(name+ "("+number+")");
			}
		}
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 判断短信是否发送成功
			if("ACTION_SEND_SMS".equals(intent.getAction())){
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
					break;

				default:
					Toast.makeText(context, "发送失败", Toast.LENGTH_LONG).show();
					break;
				}
			}
			
			// 表示对方成功收到短信
			if("ACTION_DELIVER_SMS".equals(intent.getAction())){
	            Toast.makeText(context, "对方接收成功", Toast.LENGTH_LONG).show();
			}
		}
	};
}
