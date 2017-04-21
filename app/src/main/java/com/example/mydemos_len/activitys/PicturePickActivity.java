package com.example.mydemos_len.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mydemos_len.R;
import com.example.mydemos_len.utils.GlideLoader;
import com.example.mydemos_len.utils.ShowImageAdapter;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PicturePickActivity extends Activity {
	private final int REQUEST_CODE_GALLERY = 0;
	private final int REQUEST_CODE_CAMERA = 1;
	private final int REQUEST_CODE_CROP = 2;
	private final int REQUEST_CODE_SELECTOR = 3;
	/** 临时存放图片的地址，如需修改，请记得创建该路径下的文件夹 */
	private static final String tempFilePath = "file:///sdcard/temp.jpg";
	/* 头像名称 */
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	
	ImageView imgView;
	
	private ArrayList<String> path = new ArrayList<>();
	private ShowImageAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_pick);
		
		imgView = (ImageView) findViewById(R.id.imageView2);
		RecyclerView recycler = (RecyclerView) findViewById(R.id.rvShowImage);
		
		recycler.setLayoutManager(new GridLayoutManager(this, 3));
		adapter = new ShowImageAdapter(this, path);
		recycler.setAdapter(adapter);
	}
	
	public void fromGallery(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_CODE_GALLERY);
	}
	
	public void fromCamera(View view) {
		// 激活相机
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(intent.resolveActivity(getPackageManager()) != null){
			
			// 判断存储卡是否可以用，可用进行存储
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
				// 从文件中创建uri
				Uri uri = Uri.fromFile(tempFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
			// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAMERA
			startActivityForResult(intent, REQUEST_CODE_CAMERA);
		} else {
			Toast.makeText(this, "No system camera found", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 判断sdcard是否被挂载
	 */
	private boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
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
	
	public void formImageSelector(View view) {
		ImageConfig imageConfig
				= new ImageConfig.Builder(
				// GlideLoader 可用自己用的缓存库
				new GlideLoader())
				// 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
				.steepToolBarColor(getResources().getColor(R.color.blue))
				// 标题的背景颜色 （默认黑色）
				.titleBgColor(getResources().getColor(R.color.blue))
				// 提交按钮字体的颜色  （默认白色）
				.titleSubmitTextColor(getResources().getColor(R.color.white))
				// 标题颜色 （默认白色）
				.titleTextColor(getResources().getColor(R.color.white))
				// 开启多选   （默认为多选）  (单选 为 singleSelect)
				.singleSelect()
				//剪切
				//.crop()
				// 多选时的最大数量   （默认 9 张）
				.mutiSelectMaxSize(9)
				// 已选择的图片路径
				.pathList(path)
				// 拍照后存放的图片路径（默认 /temp/picture）
				.filePath("/ImageSelector/Pictures")
				// 开启拍照功能 （默认开启）
				.showCamera()
				.requestCode(REQUEST_CODE_SELECTOR)
				.build();
		ImageSelector.open(PicturePickActivity.this, imageConfig);   // 开启图片选择器
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
					Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
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
			case REQUEST_CODE_SELECTOR:
				if (data == null) {
					return;
				}
				
				List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
				
				for (String path : pathList) {
					Log.i("ImagePathList", path);
				}
				
				path.clear();
				path.addAll(pathList);
				adapter.notifyDataSetChanged();
				break;
		}
	}
}
