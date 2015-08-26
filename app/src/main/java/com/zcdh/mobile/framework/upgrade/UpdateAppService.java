package com.zcdh.mobile.framework.upgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.utils.StringUtils;

/**
 * 软件更新
 * 
 * @author yangjiannan
 *
 */
public class UpdateAppService {

	private Context context;

	private DownloadProgressListner progressListner;

	// 提示语
	private String defaultUupdateMsg = "有最新的软件包，请及时更新下载吧~";
	private static String packgeName;// = "com.zcdh.mobile";

	public UpdateInfo updateInfo;

	private Dialog noticeDialog;

	/* 下载包安装路径 */
	private static final String saveFileName = "zcdh_update_"
			+ UUID.randomUUID() + ".apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;

	private static final int DOWN_START = 0;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;
	private final GlobalHandler globalHandler = new GlobalHandler(this);

	private static class GlobalHandler extends Handler {
		private final WeakReference<UpdateAppService> mService;

		public GlobalHandler(UpdateAppService s) {
			// TODO Auto-generated constructor stub
			mService = new WeakReference<UpdateAppService>(s);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			UpdateAppService us = mService.get();
			switch (msg.what) {
			case DOWN_START:
				if (us.progressListner != null) {
					us.progressListner.onDownloadStart();
				}
				break;
			case DOWN_UPDATE:
				if (us.progressListner != null) {
					us.progressListner.onDownloadProgress(us.progress);
				}
				if (us.mProgress != null) {
					us.mProgress.setProgress(us.progress);
				}
				break;
			case DOWN_OVER:
				if (us.progressListner != null) {
					us.progressListner.onDownloadSuccess();
				} else {
					us.installApk();
				}
				break;
			default:
				break;
			}
		}

	}

	public UpdateAppService(Context context,
			DownloadProgressListner progressListner) {
		this(context);
		this.progressListner = progressListner;
	}

	public UpdateAppService(Context context) {
		this.context = context;
		packgeName = context.getPackageName();
	}

	/**
	 * 从服务器获取xml解析并进行比对版本号
	 */
	public Boolean checkIsUpdate() {
		Boolean result = false;
		try {
			updateInfo = getUpdataInfo();
			if (updateInfo != null
					&& !StringUtils.isBlank(updateInfo.getVersion().trim())) {
				Integer currentVercode = Integer.valueOf(getVerCode(context)
						.toString().trim());
				Integer upgradeVercode = Integer.valueOf(updateInfo
						.getVersion().trim());

				if (upgradeVercode > currentVercode) {
					Log.i("upgrade ", "版本号大于当前需升级");
					return true;
				} else {
					Log.i("upgrade ", "版本号小于当前不需升级 ");
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}

	/**
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
	 */
	public static UpdateInfo getUpdataInfo() throws Exception {
		// 包装成url的对象
		URL url = new URL(K.Hosts.UPGRADE_URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		InputStream is = conn.getInputStream();
		if (is == null)
			return null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");// 设置解析的数据源
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();// 实体
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText()); // 获取版本号
				} else if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText()); // 获取要升级的APK文件
				} else if ("description".equals(parser.getName())) {
					info.setDescription(parser.nextText()); // 获取该文件的信息
				} else if ("versionName".equals(parser.getName())) {
					info.setVersionName(parser.nextText()); // 获取该文件的信息
				} else if ("restype".equals(parser.getName())) {
					info.setRestype(parser.nextText()); // 获取该文件的信息
				} else if ("resid".equals(parser.getName())) {
					info.setResid(parser.nextText()); // 获取该文件的信息
				} else if ("forcedUpdate".equals(parser.getName())) { // 是否强制升级
					info.setForcedUpdate(parser.nextText());
				}
				break;
			}
			type = parser.next();
		}
		Log.i("LoadingActivity", "upgrading... get info end");
		return info;
	}

	// 外部接口让主Activity调用
	public void showNoticeDialog() {
		String vername = getVerName(context);

		Builder builder = new Builder(context);
		builder.setTitle("发现新版本");

		String msg = "";
		msg += "当前版本：" + vername + "\n";
		msg += "最新版本：" + updateInfo.getVersionName() + "\n";
		msg += "更新内容：\n";
		if (null == updateInfo.getDescription()
				|| updateInfo.getDescription().equalsIgnoreCase("")) {
			msg += defaultUupdateMsg;
		} else {
			msg += updateInfo.getDescription();
		}

		final LayoutInflater inflater = LayoutInflater.from(context);

		View v = inflater.inflate(R.layout.framework_upgrade_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		// 更新内容
		TextView updatedesc = (TextView) v.findViewById(R.id.updatedesc);
		updatedesc.setText(msg);

		// 下载进度面板
		final LinearLayout updatProgressLL = (LinearLayout) v
				.findViewById(R.id.updatProgressLL);
		updatProgressLL.setVisibility(View.GONE);
		Button cancelUpdate = (Button) v.findViewById(R.id.cancelUpdate);
		cancelUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				noticeDialog.dismiss();
				interceptFlag = true;
				if ("0".equals(updateInfo.getForcedUpdate())) {
					progressListner.onDownloadFinished();
				}

				if ("1".equals(updateInfo.getForcedUpdate())) {
					progressListner.onCancel();
				}
			}
		});

		// 选择面板
		final LinearLayout selectDialogLL = (LinearLayout) v
				.findViewById(R.id.selectDialogLL);
		selectDialogLL.setVisibility(View.VISIBLE);
		// 下载更新
		Button downBtn = (Button) v.findViewById(R.id.downBtn);
		downBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDialogLL.setVisibility(View.GONE);
				updatProgressLL.setVisibility(View.VISIBLE);
				downloadApk();
			}
		});
		// 以后再说
		Button laterBtn = (Button) v.findViewById(R.id.laterBtn);
		if ("0".equals(updateInfo.getForcedUpdate())) {
			laterBtn.setText("稍后升级");
		}

		if ("1".equals(updateInfo.getForcedUpdate())) {
			laterBtn.setText("退出");
		}

		laterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				noticeDialog.dismiss();
				if ("0".equals(updateInfo.getForcedUpdate())) {
					progressListner.onDownloadFinished();
				}

				if ("1".equals(updateInfo.getForcedUpdate())) {
					progressListner.onCancel();
				}

			}
		});

		builder.setView(v);

		noticeDialog = builder.create();
		noticeDialog.setCancelable(false);
		noticeDialog.setCanceledOnTouchOutside(false);
		noticeDialog.show();
	}

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	public void downloadApk() {
		interceptFlag = false;
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@SuppressWarnings("deprecation")
		@Override
		@SuppressLint("WorldReadableFiles")
		public void run() {
			try {
				globalHandler.sendEmptyMessage(DOWN_START);
				String updateServerAdress = updateInfo.getUrl();
				URL url = new URL(updateServerAdress);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				Log.i("saveFileName", saveFileName);
				FileOutputStream fos = context.openFileOutput(saveFileName,
						Context.MODE_WORLD_READABLE
								| Context.MODE_WORLD_READABLE);
				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					globalHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						globalHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				UpdateAppService.this.progressListner.onError();
			} catch (IOException e) {
				e.printStackTrace();
				UpdateAppService.this.progressListner.onError();
			} finally {
				if (noticeDialog != null) {
					noticeDialog.dismiss();
				}
			}

		}
	};

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	public void installApk() {
		File apkfile = new File(context.getFilesDir(), saveFileName);
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile),
				"application/vnd.android.package-archive");
		context.startActivity(i);
		/*
		 * Activity active = (Activity) context; active.finish();
		 */

	}

	// 获取版本号
	public static Integer getVerCode(Context context) {
		Integer verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(packgeName, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	// 获取版本名
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(packgeName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

	public static boolean isNetworkAvailable(Context ctx) {

		try {
			ConnectivityManager cm = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = cm.getActiveNetworkInfo();

			return (info != null && info.isConnected());

		} catch (Exception e) {

			e.printStackTrace();

			return false;

		}

	}

	public UpdateInfo getUpdateInfo() {
		return this.updateInfo;
	}

	public interface DownloadProgressListner {
		public void onDownloadStart();

		public void onDownloadSuccess();

		public void onDownloadFinished();

		public void onDownloadProgress(int p);

		public void onError();

		public void onCancel();
	}

}
