package com.zcdh.mobile.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class PushMessageUtils {
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	public static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	public static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";
	
	public static String logStringCache = "";
	
	// 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
        	return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
            	apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    
    public static boolean hasBind(Context context) {
    	return SharedPreferencesUtil.getValue(context, "bind_flag", false);
    }
    
    public static void setBind(Context context, boolean flag) {
    	SharedPreferencesUtil.putValue(context, "bind_flag", flag);
    	
    }
    
    public static void setAppid(Context context, String value){
    	SharedPreferencesUtil.putValue(context, "app_id", value);
    }
    
    public static String getAppid(Context context){
    	return SharedPreferencesUtil.getValue(context, "app_id", "");
    }
    
    public static void setChannelid(Context context, String value){
    	SharedPreferencesUtil.putValue(context, "channe_id", value);
    }
    
    public static String getChannelid(Context context){
    	return SharedPreferencesUtil.getValue(context, "channe_id", "");
    }
    
    public static void setClientid(Context context, String value){
    	SharedPreferencesUtil.putValue(context, "client_id", value);
    }
    
    public static String getClientid(Context context){
    	return SharedPreferencesUtil.getValue(context, "client_id", "");
    }
    

	
	
}
