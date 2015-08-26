/**
 * 
 * @author test7, 2012-12-31 下午3:12:27
 */
package com.zcdh.mobile.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author test7, 2012-12-31 下午3:12:27
 */
public class SystemUtil {

	// 获取版本号
	public static Integer getVerCode(Context context) {
		Integer verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	// 获取版本名
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}
	
	public static  boolean isServiceRun(Context context, String serviceClassName){
	     ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	     List<RunningServiceInfo> list = am.getRunningServices(100);
	     for(RunningServiceInfo info : list){
	         if(info.service.getClassName().equalsIgnoreCase(serviceClassName)){
	                  return true;
	         }
	    }
	    return false;
	}
	
	public static Boolean getBooleanVaule(String value){
		Boolean result=false;
		if(value==null) return false;
		if(value.trim().equalsIgnoreCase("1")){
			result=true;
		}
		return result;
	}
	
	public static Integer getIntegerValue(Boolean value){
		Integer result=0;
		if(value==null) return 0;
		if(value){
			result=1;
		}
		return result;
	}
	
	public static String  getUUID(){  
		   return  java.util.UUID.randomUUID().toString().replaceAll("-", "");  
	  }  

}
