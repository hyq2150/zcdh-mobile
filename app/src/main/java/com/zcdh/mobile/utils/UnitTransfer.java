package com.zcdh.mobile.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;


public class UnitTransfer {
	
	private static float density;// 屏幕密度
	
	public static float getDensity() {
		return density;
	}

	public static void setDensity(float densitz) {
		density = densitz;
	}
	/**
	* 获取屏幕密度
	*/
	public static float getScreendensity(Context context) {
	  float scale = context.getResources().getDisplayMetrics().density;
	  return scale;
	}
	/*
	*//**
	* 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	*//*
	public static int dip2px( float dpValue) {
	  final float scale = getDensity();
	  return (int) (dpValue * scale + 0.5f);
	}*/
	
	/**
	* 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	*/
	public static int dip2px(Context context, float dpValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (dpValue * scale + 0.5f);
	}
	


	/**
	* 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	*/
	public static int px2dip(Context context, float pxValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	* 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	*/
	public static int px2dip( float pxValue) {
	  final float scale = getDensity();
	  return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 返回屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getWindowWidth(Context context){
		DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
	}
	/**
	 * 返回屏幕高度
	 * @param context
	 * @return
	 */
	public static int getWindowHeight(Context context){
		DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
	}
	
}
