package com.example.mydemos_len.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mydemos_len.R;

import java.io.File;

public class PicturePickActivity extends Activity {
	private final int REQUEST_CODE_GALLERY = 0;
	private final int REQUEST_CODE_CAMERA = 1;
	private final int REQUEST_CODE_CROP = 2;
	/** 临时存放图片的地址，如需修改，请记得创建该路径下的文件夹 */
	private static final String tempFilePath = "file:///sdcard/temp.jpg";
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	
	ImageView imgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_pick);
		
		imgView = (ImageView) findViewById(R.id.imageView2);
	}
	
	public void fromGallery(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_CODE_GALLERY);
	}
	
	public void fromCamera(View view) {
		// 激活相机
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
			// 从文件中创建uri
			Uri uri = Uri.fromFile(tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}
	
	/**
	 * 判断sdcard是否被挂载
	 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void crop(Uri uri) {
		crop(uri, 0, 0, 0, 0);
	}
	
	/**
	 * 图片剪切
	 *
	 * @param uri     图片
	 * @param w       输出宽
	 * @param h       输出高
	 * @param aspectX 宽比例
	 * @param aspectY 高比例
	 */
	private void crop(Uri uri, int w, int h, int aspectX, int aspectY) {
		//默认
		if (w == 0 && h == 0) {
			w = h = 480;
		}
		if (aspectX == 0 && aspectY == 0) {
			aspectX = aspectY = 1;
		}
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra("outputX", w);
		intent.putExtra("outputY", h);
		// 输出格式
		intent.putExtra("outputFormat", "JPEG");
		// 输出路径
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(tempFilePath));
		// 不启用人脸识别
		intent.putExtra("noFaceDetection", true);
		//intent.putExtra("return-data", false);
		intent.putExtra("return-data", true);
		
		startActivityForResult(intent, REQUEST_CODE_CROP);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
			case REQUEST_CODE_GALLERY:
				if (data != null) {
					Uri uri = data.getData();
					crop(uri);
				}
				break;
			case REQUEST_CODE_CAMERA:
				// 从相机返回的数据
				if (hasSdcard()) {
					crop(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片！", 0).show();
				}
				break;
			case REQUEST_CODE_CROP:
				if (data != null) {
					Bitmap bitmap = data.getParcelableExtra("data");
					imgView.setImageBitmap(bitmap);
				}
				
				// try {
				// 	// 将临时文件删除
				// 	tempFile.delete();
				// } catch (Exception e) {
				// 	e.printStackTrace();
				// }
				break;
		}
	}
}
