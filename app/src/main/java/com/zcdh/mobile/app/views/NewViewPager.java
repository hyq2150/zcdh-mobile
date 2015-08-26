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
	
//	private static final String TAG = NewViewPager.class.getSimpleName();
//	private boolean isCanScroll= true;
	
	public NewViewPager(Context context) {
		super(context);
	}

	public NewViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
//	public void isCanScroll(boolean is){
//		this.isCanScroll = is;
//	}

	/*@Override  
    public boolean onTouchEvent(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
        if (isCanScroll) {  
            return super.onTouchEvent(arg0);  
        } else {  
            return false;  
        }  
  
    }  */
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		// Not satisfied with this method of checking...
		// working on a more robust solution
//		String name = v.getClass().getName();
		if (v.getClass().getName().equals("com.baidu.mapapi.map.MapView")) {
			return true;
		}
		// if(v instanceof MapView){
		// return true;
		// }
		return super.canScroll(v, checkV, dx, x, y);
	}
	
	/*@Override  
    public boolean onInterceptTouchEvent(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
		Log.i(TAG, "onInterceptTouchEvent:" + isCanScroll);
        if (isCanScroll) {  
            return super.onInterceptTouchEvent(arg0);  
        } else {  
            return false;  
        }  
  
    } */ 
}
