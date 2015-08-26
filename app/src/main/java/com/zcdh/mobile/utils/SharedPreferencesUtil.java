package com.zcdh.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *系统参数工具类
 * @author danny, 2013-10-31 下午8:49:17
 */
public class SharedPreferencesUtil {	
	
	public final static String SETTING = "zcdh";
	
    public static void putValue(Context context,String key, int value) {  
         Editor sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();  
         sp.putInt(key, value);  
         sp.commit();  
    }  
    
    public static void putValue(Context context,String key, boolean value) {  
         Editor sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();  
         sp.putBoolean(key, value);  
         sp.commit();  
    }  
    
    public static void putValue(Context context,String key, long value){
    	 Editor sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();  
         sp.putLong(key, value);  
         sp.commit();  
    }
    
    public static void putValue(Context context,String key, String value) {  
         Editor sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();  
         sp.putString(key, value);  
         sp.commit();  
    }  
    public static void putValue(Context context,String key, float value) {  
    	Editor sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();  
    	sp.putFloat(key, value);  
    	sp.commit();  
    }  
    public static int getValue(Context context,String key, int defValue) {  
        SharedPreferences sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);  
        return sp.getInt(key, defValue);  
    }  
    public static boolean getValue(Context context,String key, boolean defValue) {  
        SharedPreferences sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);  
        return sp.getBoolean(key, defValue);  
    }  
    public static String getValue(Context context,String key, String defValue) {  
        SharedPreferences sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);  
        return sp.getString(key, defValue);  
    }
    public static float getValue(Context context,String key, float defValue) {  
    	SharedPreferences sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);  
    	return sp.getFloat(key, defValue);  
    }
    
    public static long getValue(Context context,String key, long defValue){
    	SharedPreferences sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);  
    	return sp.getLong(key, defValue);  
    }

}
