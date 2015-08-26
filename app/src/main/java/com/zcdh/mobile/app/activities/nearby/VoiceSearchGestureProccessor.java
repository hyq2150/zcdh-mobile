package com.zcdh.mobile.app.activities.nearby;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 处理语音喊工作的手势操作
 * @author yangjiannan
 *
 */
public class VoiceSearchGestureProccessor {

	VoiceSearchGestureListnner gestureListnner;
	
	/**
	 * 应该上移多少变为 取消
	 */
	private static final float up_differ = 120f;
	private float  first_raw_y;
	

	
	public VoiceSearchGestureProccessor (VoiceSearchGestureListnner gestureListnner){
		this.gestureListnner = gestureListnner;
		
	}
	
	public void proccess(MotionEvent event){
		
		if(first_raw_y==0){
			first_raw_y = event.getRawY();
			
		}
		//Log.i("xy deffer", (first_raw_y - event.getRawY()) + "");
		
		if(first_raw_y-event.getRawY()>up_differ){
			//Log.i("cancel .... ", "yes !!!!");
			gestureListnner.onWillCancel(event);
		}else{
			gestureListnner.onWillVoice(event);
		}
		
		if(event.getAction()==MotionEvent.ACTION_UP){
			if(first_raw_y-event.getRawY()>up_differ){
				//Log.i("cancel .... ", "yes !!!!");
				gestureListnner.onCancel(event);
			}else{
				gestureListnner.onVoiceEnd(event);
			}
		}
	}

}

interface VoiceSearchGestureListnner{
	

	/**
	 * 手势移到开始录音
	 */
	void onWillVoice(MotionEvent event);
	
	/**
	 * 即将取消
	 */
	void onWillCancel(MotionEvent event);
	
	/**
	 * 取消
	 */
	void onCancel(MotionEvent event);
	
	void onVoiceEnd(MotionEvent event);
	
}
