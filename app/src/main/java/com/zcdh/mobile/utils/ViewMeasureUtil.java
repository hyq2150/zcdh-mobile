/**
 * 
 * @author YJN, 2013-10-29 下午2:21:36
 */
package com.zcdh.mobile.utils;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;
/**
 * @author YJN, 2013-10-29 下午2:21:36
 * 获取、设置控件信息
 */
public class ViewMeasureUtil {

	
	/*
	 * 获取控件宽
	 */
	public static int getWidth(View view)
	{
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.AT_MOST),MeasureSpec.makeMeasureSpec(0, MeasureSpec.AT_MOST));
        return (view.getMeasuredWidth());
	}
	/*
	 * 获取控件高
	 */
	public static int getHeight(View view)
	{
	    int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());       
	}
	
	
	/*
	 * 设置控件所在的位置X，并且不改变宽高，
	 * X为绝对位置，此时Y可能归0
	 */
	public static void setLayoutX(View view,int x)
	{
		MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
		margin.setMargins(x,margin.topMargin, x+margin.width, margin.bottomMargin);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		view.setLayoutParams(layoutParams);
	}
	/*
	 * 设置控件所在的位置Y，并且不改变宽高，
	 * Y为绝对位置，此时X可能归0
	 */
	public static void setLayoutY(View view,int y)
	{
		MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
		margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		view.setLayoutParams(layoutParams);
	}
	/*
	 * 设置控件所在的位置YY，并且不改变宽高，
	 * XY为绝对位置
	 */
	public static void setLayout(View view,int x,int y)
	{
		MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
		margin.setMargins(x,y, x+margin.width, y+margin.height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		view.setLayoutParams(layoutParams);
	}
}