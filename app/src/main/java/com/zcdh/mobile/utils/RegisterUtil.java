package com.zcdh.mobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.zcdh.mobile.app.ZcdhApplication;

public class RegisterUtil {
	
	

	public static boolean isRegisterUser(Context context){
		return ZcdhApplication.getInstance().getZcdh_uid() != -1l;
	}
	public static void intercepterNotRegisterUser(final Activity activity, final String msg){
		
		if(!isRegisterUser(activity)){
			AlertDialog.Builder builder= new AlertDialog.Builder(activity);
			builder.setTitle("登录提示")
			.setPositiveButton("去登录", new OnClickListener() {
				@Override
				//点击了去登录会执行以下操作
				public void onClick(DialogInterface dialog, int which) {
					//SharedPreferencesUtil.putValue(context, Constants.login, value)
	//				ActivityDispatcher.to_login(activity);
					//如果是从职位申请那里进入到当前
					//if("请登录后再申请该职位".equals(msg))
					//{
						//intent.putExtra("ActivityFrom", "PostDetailActivity");
					//}
					//Intent intent = new Intent(context, PostApplyActivity.class);
					//context.startActivity(intent);
					
					
					//((Activity)context).finish();
				}
			})
			.setNegativeButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			})
			.setMessage(msg)
			.setCancelable(false);
			
			builder.create().show();
		}
		
	}
	
	

}
