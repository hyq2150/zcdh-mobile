package com.zcdh.mobile.framework.exceptions;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.zcdh.core.utils.FileUtils;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.ImgAttachDTO;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.framework.nio.AppEverimentArgs;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.FileIoUtil;
import com.zcdh.mobile.utils.StorageUtil;

public class DefaultExceptionHandler implements UncaughtExceptionHandler,
		RequestListener {

	private String TAG = DefaultExceptionHandler.class.getSimpleName();

	private String kREQ_ID_addErrorLog;

	private IRpcJobUservice userService;

	private Context context;

	private AppEverimentArgs mobileDeviceInfo;

	public DefaultExceptionHandler(Context context) {
		this.context = context;
		mobileDeviceInfo = new AppEverimentArgs(context);
		userService = RemoteServiceManager
				.getRemoteService(IRpcJobUservice.class);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.i(TAG, "uncaughtException");
		sendException(getThrowableInfo(ex));
	}

	/**
	 * 获得异常堆栈信息
	 */
	private String getThrowableInfo(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		ex.printStackTrace();
		String info = writer.toString();
		info += "\n";
		info += this.mobileDeviceInfo;
		Log.e("error:", info);
		return info;
	}

	/**
	 * 发送异常信息
	 */
	private void sendException(final String exString) {
		if (K.debug == false) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					File errorLogFile = saveCrashInfo2File(exString);
					if (errorLogFile != null && errorLogFile.exists()) {
						ImgAttachDTO logFile = new ImgAttachDTO();
						logFile.setFileBytes(FileUtils.FileToByte(errorLogFile));
						logFile.setFileSize(errorLogFile.length());
						logFile.setFileExtension("txt");

						userService.addErrorLog(
								ZcdhApplication.getInstance().getZcdh_uid(),
								mobileDeviceInfo.getModel(),
								mobileDeviceInfo.getAndroidVersion(), logFile)
								.identify(
										kREQ_ID_addErrorLog = RequestChannel
												.getChannelUniqueID(),
												DefaultExceptionHandler.this);

					}
				}
			}).start();
		}

	}

	/**
	 * 保存错误信息到文件中
	 */
	private File saveCrashInfo2File(String report) {
		File result = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss",Locale.US);
			long timestamp = System.currentTimeMillis();
			String time = sdf.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".txt";
			String path = StorageUtil.getLocalSaveRootPath() + "error/";
			result = FileIoUtil.writefile(path, fileName, report);
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
			return null;
		}
		return result;
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		Log.i(TAG, "result :" + result);
		if (reqId == kREQ_ID_addErrorLog) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Toast.makeText(context, " 谢谢您的反馈!", Toast.LENGTH_SHORT)
							.show();

				}
			}
		}

	}

	@Override
	public void onRequestFinished(String reqId) {
		System.exit(0);
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub

	}
}
