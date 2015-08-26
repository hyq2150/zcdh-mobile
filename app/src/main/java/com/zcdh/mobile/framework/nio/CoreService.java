/**
 * 
 * @author danny, 2013-10-30 下午8:00:16
 */
package com.zcdh.mobile.framework.nio;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.zcdh.mobile.utils.NetworkUtils;

/**
 * 网络Socket连接服务类，常驻系统，实现Android手机和服务器端的长连接。
 *  当网络断网或重连时，保证手机端和服务器端的长连接
 * @author danny, 2013-10-30 下午8:00:16
 */
public class CoreService extends Service{
	private static final String TAG = CoreService.class.getSimpleName();
	
	// 上下文
	private static Context context;
	//客户连接
	private static MNioClient nioClient;
	// 网络是否连接上
	boolean networkConnected = false; 
	//是否成功登陆
	static Boolean  isSuccessLogined = true;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "网络状态改变");
			networkConnected = NetworkUtils.isNetworkAvailable(context);
			Log.e(TAG, "网路状态：" + networkConnected);

			if (networkConnected) {// 如果网络重新连接上
				runConnect();
			} else {// 如果网络断掉
				runDisconnect();
			}
		}
	};
	
	synchronized public static MNioClient getNioClient() {
		if (nioClient == null) {
			System.setProperty("java.net.preferIPv6Addresses", "false"); // 解决2.2的socket连接抛出“bad address family”异常
			nioClient = createNioClient();
		}
		return nioClient;
	}
	
	private static MNioClient createNioClient() {
		MNioClient client = null;
		client = MNioClient.getInstance();
		return client;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		Log.e(TAG, "Service is onCreate");
		nioClient=getNioClient();
		
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);

	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.e(TAG, "Service is Start");
		nioClient=getNioClient();
		//检测当前长连接是否正常，若不正常，则重新连接
		runConnect();
	}
	
	public static void runConnect() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				connect();
				Log.e(TAG, "状态为：" + nioClient.isConnect());
			}
		}).start();
	}
	
	public static Boolean connect() {
		return connect(context);
	}
	
	public synchronized static Boolean connect(Context context){
		//网络是否连接，若没有连接，则不重连
		boolean networkConnected = NetworkUtils.isNetworkAvailable(context);
		if (!networkConnected) {
			Log.e(TAG, "无可用的网络连接，不尝试连接！");
			return false;
		}
		
		// 判断用户是否登录,若没有登录，则不尝试连接
//		if(Constants.isLogin)
//		{
//			isSuccessLogined=ZcdhApplication.getInstance().getIsSuccessLogined();
//		}
//		else
//		{
//			isSuccessLogined=true;
//		}
//		if(!isSuccessLogined){
//			Log.d("connect", "没有传入登录权限，不尝试连接");
//			return false;
//		}
//		
		nioClient=getNioClient();
		//没有建立连接实例，无法尝试连接
		if(nioClient==null){
			Log.e(TAG, "无连接实例，不尝试连接！");
			return false;	
		}
		//如果没有连接，重新连接服务器
		if(!nioClient.isConnect()){
			nioClient.connect();
		}
		
		return true;
	}
	
	public static void runDisconnect() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				disConnect();
			}
		}).start();
	}
	
	public synchronized static Boolean disConnect(){
		nioClient=getNioClient();
		if(nioClient==null) {
			Log.e(TAG, "无连接实例，无法断开连接！");
			return false;
		}
		nioClient.disConnect();
		return true;
		
		
	}

	public static Boolean isConnected() {
		nioClient=getNioClient();
		if(nioClient==null) {
			Log.e(TAG, "无连接实例，无法断开连接！");
			return false;
		}
		return nioClient.isConnect();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "Service is onDestroy");
		if (getNioClient() != null) {
			getNioClient().disConnect();
		}
		unregisterReceiver(mReceiver);
		context = null;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.e(TAG, "Service is onBind");
		return null;
	}
}
