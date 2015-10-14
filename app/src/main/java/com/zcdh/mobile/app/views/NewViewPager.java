/**
 * 
 * @author jeason, 2014-6-13 上午10:54:21
 */
package com.zcdh.mobile.app.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author jeason, 2014-6-13 上午10:54:21
 */
public class NewViewPager extends ViewPager {
	public NewViewPager(Context context) {
		super(context);
	}
	public NewViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
	    // Not satisfied with this method of checking...
	    // working on a more robust solution
//		String name = v.getClass().getName();
	    return v.getClass().getName().equals("com.baidu.mapapi.map.MapView") || super
		    .canScroll(v, checkV, dx, x, y);
	}
}
