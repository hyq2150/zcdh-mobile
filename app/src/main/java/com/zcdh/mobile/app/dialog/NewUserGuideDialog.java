package com.zcdh.mobile.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.utils.SharedPreferencesUtil;

public class NewUserGuideDialog extends Dialog implements View.OnClickListener{
	boolean isShowing = false;
	private ImageView image;
	private Activity activity;
	private int flag = -1;
	
	private int count = 0;

	public NewUserGuideDialog(Context context, int theme) {
		super(context, theme);
		this.activity = (Activity) context;
		setContentView(R.layout.user_guide_dialog);
		image = (ImageView) findViewById(R.id.img);
		image.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(flag==Constants.flag_map){
			if(count==0){
				image.setImageResource(R.drawable.mapguide_02);
				animaImageAction();
			}
			if(count==1){
				dismiss();
				SharedPreferencesUtil.putValue(getContext(), Constants.kSHARE_NEW_USER_GUIDE_MAP, true);
				
			}
			count++;
		}
		if(flag==Constants.flag_postlist){
			dismiss();
			SharedPreferencesUtil.putValue(getContext(), Constants.kSHARE_NEW_USER_GUIDE_LIST, true);
		}
	}

	public void show(int flag){
		Log.i("USER GUIDE", "YES");
		this.flag = flag;
		if(flag==Constants.flag_map){
			image.setImageResource(R.drawable.mapguide_01);
			
		}
		if(flag==Constants.flag_postlist){
			image.setImageResource(R.drawable.mapguide_03);
		}
		animaImageAction();
		isShowing = true;
		if (activity != null && !activity.isFinishing()) {
			super.show();
		}
	}
	public void setIsShowing(boolean is){
		this.isShowing = is;
	}
	public boolean getIsShowing(){
		return this.isShowing;
	}
	
	private void animaImageAction(){
		image.setVisibility(View.VISIBLE);
		//图片渐变模糊度始终  
        AlphaAnimation aa = new AlphaAnimation(0.1f,1.0f);  
        //渐变时间  
        aa.setDuration(600);  
        //展示图片渐变动画  
       image.startAnimation(aa);  
          
	}
}
