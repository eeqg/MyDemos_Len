package com.example.mydemos_len.activitys;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydemos_len.R;

public class ShareActivity extends Activity implements OnItemClickListener {
	private List<ResolveInfo> mShareAppInfoList = new ArrayList<ResolveInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		mShareAppInfoList.clear();
        mShareAppInfoList = getShareAppInfo();
        
        GridView gridView = (GridView) findViewById(R.id.gridView_share);
        gridView.setAdapter(new ShareAdapter());
        gridView.setOnItemClickListener(this);
        findViewById(R.id.spaceView_share).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private List<ResolveInfo> getShareAppInfo() {
        // share app info list.
        Intent intentShare = getShareIntent(null);
        return getPackageManager().queryIntentActivities(intentShare, PackageManager.MATCH_DEFAULT_ONLY);
    }
	
	public Intent getShareIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        Intent.createChooser(intent, "Share");
        return intent;
	}
	
	/**分享列表适配器*/
    private class ShareAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mShareAppInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mShareAppInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(ShareActivity.this).inflate(R.layout.share_list_item, null);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		finish();
		
		ResolveInfo info = mShareAppInfoList.get(position);
		Intent shareIntent = getShareIntent("for text.....");
		shareIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(shareIntent);
	}

}
