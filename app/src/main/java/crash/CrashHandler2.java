package crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.mydemos_len.APP;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: CrashHandler
 * @Description UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 * @author hq
 * @date 2015年1月20日 下午4:58:57
 *
 */
public class CrashHandler2 implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	//系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	//CrashHandler实例
	private static CrashHandler2 instance;
	//程序的Context对象
	private Context mContext;
	//用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();
	//用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	//自定义线程池
	private static ExecutorService FULL_TASK_EXECUTOR;
	//要发送的服务器地址
	private static String ServerAddress = "http://192.168.1.110:8080/YiSaPushServer/collections.jsp";
	static {
		FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
	};
	/** 保证只有一个CrashHandler实例 */
	private CrashHandler2() {
	}
	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler2 getInstance() {
		if (instance == null)
			instance = new CrashHandler2();
		return instance;
	}
	/**
	 * 初始化
	 */
	public void init(Context context) {
		mContext = context;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			//退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}
	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//收集设备参数信息
		collectDeviceInfo(mContext);
		//使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		//保存日志文件
		saveCatchInfo2File(ex);
		return true;
	}
	/**
	 * 收集设备参数信息
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}
	private String getFilePath() {
		String file_dir = "";
		// SD卡是否存在
		boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		// Environment.getExternalStorageDirectory()相当于File file=new File("/sdcard")
		boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();
		if (isSDCardExist && isRootDirExist) {
			file_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crashlog/";
		} else {
			// MyApplication.getInstance().getFilesDir()返回的路劲为/data/data/PACKAGE_NAME/files，其中的包就是我们建立的主Activity所在的包
			file_dir = APP.getInstance().getFilesDir().getAbsolutePath() + "/crashlog/";
		}
		return file_dir;
	}
	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCatchInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			String file_dir = getFilePath();
			new SendErrorMsgTask().executeOnExecutor(FULL_TASK_EXECUTOR, sb.toString());
			//          if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			//          File dir = new File(file_dir);
			//          if (!dir.exists()) {
			//              dir.mkdirs();
			//          }
			//          File file = new File(file_dir + fileName);
			//          if (!file.exists()) {
			//              file.createNewFile();
			//          }
			//          FileOutputStream fos = new FileOutputStream(file);
			//          fos.write(sb.toString().getBytes());
			//          //发送给开发人员
			//          sendCrashLog2PM(file_dir + fileName);
			//          fos.close();
			//          }
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
	/**
	 * 将捕获的导致崩溃的错误信息发送给开发人员
	 * 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
	 */
	private void sendCrashLog2PM(String fileName) {
		//      if (!new File(fileName).exists()) {
		//          Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
		//          return;
		//      }
		//      FileInputStream fis = null;
		//      BufferedReader reader = null;
		//      String s = null;
		//      try {
		//          fis = new FileInputStream(fileName);
		//          reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
		//          while (true) {
		//              s = reader.readLine();
		//              if (s == null)
		//                  break;
		//              //由于目前尚未确定以何种方式发送，所以先打出log日志。
		//              Log.i("info", s.toString());
		//          }
		//      } catch (FileNotFoundException e) {
		//          e.printStackTrace();
		//      } catch (IOException e) {
		//          e.printStackTrace();
		//      } finally { // 关闭流
		//          try {
		//              reader.close();
		//              fis.close();
		//          } catch (IOException e) {
		//              e.printStackTrace();
		//          }
		//      }
	}
	
	/**
	 * @ClassName: SendErrorMsgTask
	 * @Description: TODO((异步发送信息到服务器端)
	 * @author hq
	 * @date 2015年1月21日 上午9:59:48
	 *
	 */
	class SendErrorMsgTask extends AsyncTask<String, Object, Object>{
		/**
		 * @Title: onPreExecute
		 * @Description: TODO(后台发送)
		 * @param  params 发送的文本信息
		 * @throws
		 */
		@Override
		protected Object doInBackground(String... params) {
			// Log.d("-doInBackground--", "true");
			// HttpClientUtil http = new HttpClientUtil();
			// List <NameValuePair> paramList = new ArrayList <NameValuePair>();   //Post运作传送变量必须用NameValuePair[]数组储存
			// paramList.add(new BasicNameValuePair("str", params[0]));
			// return http.doPost(ServerAddress, paramList);
			return null;
		}
	}
}