package com.zcdh.mobile.framework.nio;

import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;

/**
 * 搜集app ，设备相关的信息
 * 
 * @author yangjiannan
 *
 */
public class AppEverimentArgs {

	private static final String TAG = AppEverimentArgs.class.getSimpleName();

	/**
	 * 设备类型
	 */
	public static final String DECEIVE_NAME = "DECEIVE_NAME";
	/**
	 * 手机类型
	 */

	public static final String DECEIVE_TYPE = "DECEIVE_TYPE"; // IOS,Android

	/**
	 * 系统版本号
	 */
	public static final String SYS_VERSION = "SYS_VERSION";

	/**
	 * app版本号
	 */
	public static final String APP_VERSION = "APP_VERSION";

	/**
	 * 网络连接方式
	 */
	public static final String NETWORK_STATUS = "NETWORK_STATUS";

	/**
	 * 网络运营商
	 */
	public static final String MOBILE_NETWORK_OPERATORS = "MOBILE_NETWORK_OPERATORS";

	private static final String USER_ID = "USER_ID";

	private Context context;

	public AppEverimentArgs(Context context) {
		this.context = context;
	}

	/**
	 * App版本信息
	 * 
	 * @return
	 */
	public String getAppVersion() {
		PackageInfo pk = null;
		try {
			pk = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return pk.versionName + "(" + pk.versionCode + ")";
	}

	/**
	 * android 系统版本
	 * 
	 * @return
	 */
	public String getAndroidVersion() {
		String androidVersion = "SDK 版本:" + Build.VERSION.SDK_INT
				+ "," + "android 系统版本:" + Build.VERSION.RELEASE;
		return androidVersion;
	}

	/**
	 * 设备型号
	 * 
	 * @return
	 */
	public String getModel() {
		return Build.MODEL;
	}

	/**
	 * 所使用的网络类型
	 * 
	 * @return
	 */
	public String getNetworkType() {
		String networkType = "";
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				networkType += "wifi";
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				networkType += "";
				int subType = info.getSubtype();
				// 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EDGE，电信的2G为CDMA，电信的3G为EVDO
				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					return networkType += "2G";
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_EHRPD:
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					return networkType += "3G";
				case TelephonyManager.NETWORK_TYPE_LTE:
					return networkType += "4G";
				default:
					return networkType += "unknow";
				}
			}
		}
		return networkType;
	}

	/**
	 * 获取网络运营商 CMCC 移动， CTCC 电信，CUCC 联通
	 * 
	 * @return
	 */
	public String getNetworkOperators() {

		String operator = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String operatorCode = telephonyManager.getSimOperator();
		Log.i(TAG, operatorCode + "");
		Log.i(TAG, "运营商：" + telephonyManager.getSimOperatorName());
		// String operatorName = "";
		if (operatorCode != null) {
			if (operatorCode.equals("46000") || operatorCode.equals("46002")) {
				// operatorName = "中国移动";
				operator = "CMCC";
			} else if (operatorCode.equals("46001")) {
				// operatorName = "中国联通";
				operator = "CUCC";
			} else if (operatorCode.equals("46003")) {
				// operatorName = "中国电信";
				operator = "CTCC";
			}

		}
		if (TextUtils.isEmpty(operator))
			operator = "unknow";
		return operator;
	}

	/**
	 * 请求的设备的属性 手机型号 DECEIVE_NAME 手机类型 DECEIVE_TYPE：IOS,Android 系统版本号
	 * SYS_VERSION app版本号 APP_VERSION 网络连接方式 NETWORK_STATUS 网络运营商
	 * MOBILE_NETWORK_OPERATORS ( CMCC 移动， CTCC 电信，CUCC 联通 )
	 * 
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getEverimentArgs() {

		HashMap<String, String> args = new HashMap<String, String>();
		args.put(DECEIVE_NAME, getModel());
		args.put(DECEIVE_TYPE, "Android");
		args.put(SYS_VERSION, getAndroidVersion());
		args.put(APP_VERSION, getAppVersion());
		args.put(NETWORK_STATUS, getNetworkType());
		args.put(MOBILE_NETWORK_OPERATORS, getNetworkOperators());
		args.put(USER_ID, ZcdhApplication.getInstance().getZcdh_uid() + "");
		// 渠道
		try {
			args.put(
					Constants.CHANNEL,
					context.getPackageManager().getApplicationInfo(
							context.getPackageName(),
							PackageManager.GET_META_DATA).metaData
							.getString(Constants.CHANNEL));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return args;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("App 版本：").append(getAppVersion()).append("\n")
				.append("系统版本:").append(getAndroidVersion()).append("\n")
				.append("设备类型：").append(getModel()).append("\n")
				.append("网络类型:").append(getNetworkType()).append("\n")
				.append("运营商:").append(getNetworkOperators()).append("\n");
		return sb.toString();
	}

}
