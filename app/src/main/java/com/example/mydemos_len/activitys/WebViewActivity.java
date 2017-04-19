package com.example.mydemos_len.activitys;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.widget.TextView;

import com.example.mydemos_len.R;

public class WebViewActivity extends Activity {

	private WebView webView;
	private ProgressBar mProgressView;
	private String mUrl;
	private ShareActionProvider mShareActionProvider;
	private List<ResolveInfo> mShareAppInfoList = new ArrayList<ResolveInfo>();
	private Dialog mShareDialog;
	private String mWebTitle;
	
	private OnTouchListener gridTouchListendr = new OnTouchListener() {
		
		private int dy;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				dy = (int) event.getY();
				break;
				
			case MotionEvent.ACTION_MOVE:
				int my = (int) event.getY();
				if(Math.abs(my - dy) > 30){
					
				}
				break;
				
			case MotionEvent.ACTION_UP:
				int uy = (int) event.getY();
				Log.d("test_wp", "moveY---"+Math.abs(uy - dy));
				if(Math.abs(uy - dy) < 30){
					return false;
				}
				
				break;

			default:
				break;
			}

			return true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		
		mProgressView = (ProgressBar) findViewById(R.id.progressBar_web);
		
		webView = (WebView) findViewById(R.id.webView1);
		//settings
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBlockNetworkImage(false);//设置后才正常显示图片
		webSettings.setDomStorageEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setPluginState(PluginState.ON);
		webSettings.setAppCacheEnabled(true);
		webSettings.setDatabaseEnabled(true);
		// 设置加载进来的页面自适应手机屏幕
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		//webSettings.setBuiltInZoomControls(true); //要在activity finish()的时候处理一下, 否则可能报错.
		//webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setRenderPriority(RenderPriority.HIGH);// 提高渲染的优先级
		
		//webView.setDrawingCacheEnabled(true);
		
		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				Log.d("test_wp", "----onPageStarted()---");
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				Log.d("test_wp", "----onPageFinished()---");
				//view.animate().alpha(1f).setDuration(300);
				//webView.getSettings().setBlockNetworkImage(false);
			}
			
			@Override
			@Deprecated
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Log.d("test_wp", "----onReceivedError()---errorCode="+errorCode);
				Log.d("test_wp", "----onReceivedError()---description="+description);
			}
		});
		//判断页面加载过程
		webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
            	mProgressView.setProgress(newProgress);
                if (newProgress >= 100) {
                    // 网页加载完成
                	Log.d("test_wp", "----onProgressChanged()---");
                	mProgressView.setVisibility(View.GONE);
                } else {
                    // 加载中
                	mProgressView.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onReceivedTitle(WebView view, String title) {
            	super.onReceivedTitle(view, title);
            	mWebTitle = title;
            	actionBar.setTitle(title);
            }
        });
		webView.setDownloadListener(new WebviewDownloadListener());
		mUrl = "http://clk1.haowin.cn/url/hNHLTBTm/K9WLyy";
		webView.loadUrl(mUrl);
		
		//init data
		mShareAppInfoList.clear();
		mShareAppInfoList = getShareAppInfo();
		Log.d("test_wp", "mShareAppInfoList=="+mShareAppInfoList);
	}
	
	private List<ResolveInfo> getShareAppInfo() {
		// share app info list.
		Intent intentShare = getShareIntent();
		return getPackageManager().queryIntentActivities(intentShare, PackageManager.MATCH_DEFAULT_ONLY);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.web_view, menu);
		
		MenuItem item = menu.findItem(R.id.menu_item_share);
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		mShareActionProvider.setShareIntent(getShareIntent());
		mShareActionProvider.setOnShareTargetSelectedListener(new OnShareTargetSelectedListener() {
			@Override
			public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
				Log.d("test_wp", "WebViewActivity--onShareTargetSelected()--intent="+intent);
				
				return false;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public Intent getShareIntent() {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    //intent.putExtra(Intent.EXTRA_SUBJECT, "主题");
	    intent.putExtra(Intent.EXTRA_TEXT, mWebTitle+"\n http://clk1.haowin.cn/url/hNHLTBTm/K9WLyy");
	    //intent.putExtra(Intent.EXTRA_ORIGINATING_URI, "http://clk1.haowin.cn/url/hNHLTBTm/K9WLyy");
	    intent.setType("text/plain");
	    //intent.setType("image/*");
	    Intent.createChooser(intent, "Share");
	    return intent;
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			break;
		case R.id.action_refresh:
			if(webView != null){
				webView.reload();
			}
			break;
		case R.id.action_ue:
			ActivityInfo defaultBrowser = getBrowserApp(getApplicationContext());
			Intent intent = new Intent();
            intent.setClassName(defaultBrowser.packageName, defaultBrowser.name);
            intent.setData(Uri.parse(mUrl));
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
			break;
		case R.id.action_share:
			showShareDialog();
			break;
		case R.id.action_copyurl:
			ClipboardManager cliMg = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("test", mUrl);
			cliMg.setPrimaryClip(clip );
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showShareDialog() {
		/*if(mShareDialog == null){
			View view = LayoutInflater.from(this).inflate(R.layout.layout_share_view, null);
			ListView shareListView = (ListView) view.findViewById(R.id.listView_share);
			shareListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ResolveInfo info = mShareAppInfoList.get(position);
					Intent intent = getShareIntent();
					intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
					startActivity(intent);
				}
			});
			shareListView.setAdapter(new ShareAdapter());
			
			mShareDialog = new AlertDialog.Builder(this, R.style.mydialog)
				.setView(view)
				.create();
			mShareDialog.setCanceledOnTouchOutside(true);
			
			Window dialogWindow = mShareDialog.getWindow();
            //dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            LayoutParams attributes = dialogWindow.getAttributes();
            //attributes.width = LayoutParams.MATCH_PARENT;
            //attributes.height = LayoutParams.WRAP_CONTENT;
			DisplayMetrics dm = new DisplayMetrics();  
		    getWindowManager().getDefaultDisplay().getMetrics(dm);  
			Rect rect = new Rect();  
			View view2 = getWindow().getDecorView();  
			view2.getWindowVisibleDisplayFrame(rect);  
			attributes.height = dm.heightPixels - rect.top;  
			attributes.width = dm.widthPixels;
			
			dialogWindow.setAttributes(attributes);
		}
		
		mShareDialog.show();*/
		
		View view = LayoutInflater.from(this).inflate(R.layout.layout_share_view, null);
		GridView gridView = (GridView) view.findViewById(R.id.gridView_share);
		gridView.setOnTouchListener(gridTouchListendr );
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ResolveInfo info = mShareAppInfoList.get(position);
				Intent intent = getShareIntent();
				intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
				startActivity(intent);
			}
		});
		view.findViewById(R.id.spaceView_share).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mShareDialog.dismiss();
            }
        });
		gridView.setAdapter(new ShareAdapter());
		
		mShareDialog = new Dialog(this, R.style.mydialog);  
		mShareDialog.setContentView(view);  
		 LayoutParams lay = mShareDialog.getWindow().getAttributes();  
		 setParams(lay);  
		 mShareDialog.show();  
		 
	}
	
	private void setParams(LayoutParams lay) {  
		  DisplayMetrics dm = new DisplayMetrics();  
		  getWindowManager().getDefaultDisplay().getMetrics(dm);  
		  //Rect rect = new Rect();  
		  //View view = getWindow().getDecorView();  
		  //view.getWindowVisibleDisplayFrame(rect);  
		  //lay.height = dm.heightPixels ;  
		  lay.width = dm.widthPixels; 
		  lay.height = LayoutParams.WRAP_CONTENT;
		  lay.gravity = Gravity.BOTTOM;
	}  

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack()){
                webView.goBack();//返回上一页面
                return true;
            }else{
                this.finish();
            }
        }
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(webView != null){
			webView.requestLayout();
			webView.onResume();
			webView.resumeTimers();
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(webView != null){
			webView.onPause();
			webView.pauseTimers();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("test_wp", "----ondestroy()---");
		if (webView != null) {
			webView.setVisibility(View.GONE);
			
			webView.stopLoading();
			//webView.onPause();
			//webView.pauseTimers();
			webView.removeAllViews();
//			mWebView.clearCache(true);
			webView.destroy();
//			mWebView=null;
		}
	}
	
	@Override
	public void finish() {
		Log.d("test_wp", "----finish()---");
	    ViewGroup view = (ViewGroup) getWindow().getDecorView();
	    view.removeAllViews();
	    super.finish();
	}
	
	/***
     * 获取默认浏览器信息
     * @param context
     * @return
     */
    public ActivityInfo getBrowserApp(Context context) {
        /**检查是否有默认浏览器*/
        Intent intent1 = new Intent(Intent.ACTION_VIEW);  
        intent1.setData(Uri.parse("http://www.google.com"));  
        PackageManager pm = context.getPackageManager(); 
        ResolveInfo defaultInfo = pm.resolveActivity(intent1, PackageManager.MATCH_DEFAULT_ONLY);
        if(defaultInfo != null && !"android".equals(defaultInfo.activityInfo.packageName)){
            return defaultInfo.activityInfo;
        }
        /**如果没有设置默认的话直接找第一个浏览器*/
        String default_browser = "android.intent.category.DEFAULT";
        String browsable = "android.intent.category.BROWSABLE";
        String view = "android.intent.action.VIEW";

        Intent intent = new Intent(view);
        intent.addCategory(default_browser);
        intent.addCategory(browsable);
        Uri uri = Uri.parse("http://");
        intent.setDataAndType(uri, null);

        // 找出手机当前安装的所有浏览器程序
        List<ResolveInfo> resolveInfoList = context.getPackageManager()
                .queryIntentActivities(intent,
                        PackageManager.GET_INTENT_FILTERS);
        if (resolveInfoList.size() > 0) {
            ActivityInfo activityInfo = resolveInfoList.get(0).activityInfo;
            return activityInfo;
        } else {
            return null;
        }
    }
	
	private class WebviewDownloadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
			// Uri uri = Uri.parse(url);
			// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			// startActivity(intent);
			if (mimetype.contains("audio") || mimetype.contains("video")) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(url), mimetype);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// Intent i = new Intent(context, DownloadService.class);
				// i.putExtra(DownloadService.DOWNLOAD_TASK_KEY,
				// DownloadService.TASK_DOWNLOAD_WF_HARD_AD);
				// i.putExtra("appName", "正在下载中");
				// i.putExtra("hardAdUrl", url);
				// context.startService(i);
				try {
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**分享列表适配器*/
    private class ShareAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mShareAppInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mShareAppInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if(convertView == null){
                convertView = LayoutInflater.from(WebViewActivity.this).inflate(R.layout.share_list_item, null);
            }
            ImageView iconView = (ImageView)convertView.findViewById(R.id.iv_app_icon);
            TextView appName = (TextView)convertView.findViewById(R.id.tv_app_name);
            
            ResolveInfo resoverInfo = mShareAppInfoList.get(position);
            CharSequence name = resoverInfo.loadLabel(getPackageManager());
        	appName.setText(name);
        	
            new LoadDrawableTask(iconView, resoverInfo).execute();
            
            return convertView;
        }
    }
    
    private class LoadDrawableTask extends AsyncTask<Void, Void, Drawable> {
		private ResolveInfo resolveInfo ;
	    private final WeakReference<ImageView> imageViewReference;
	 
	    public LoadDrawableTask(ImageView imageView, ResolveInfo resolveInfo) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	        this.resolveInfo = resolveInfo;
	    }
	 
	    @Override
	    protected Drawable doInBackground(Void... params) {
			Drawable drawable = resolveInfo.loadIcon(getPackageManager());
			return drawable;
	    }
	 
	    @Override
	    protected void onPostExecute(Drawable drawable) {
	        if (imageViewReference != null && drawable != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageDrawable(drawable);
	            }
	        }
	    }
	}
}
