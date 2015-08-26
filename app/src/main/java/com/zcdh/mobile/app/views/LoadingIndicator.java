/**
 * 
 * @author jeason, 2014-4-18 下午2:36:20
 */
package com.zcdh.mobile.app.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zcdh.mobile.R;

/**
 * @author jeason, 2014-4-18 下午2:36:20
 * 数据加载标识
 */
public class LoadingIndicator {

	private Dialog dialog;
	private Context mContext;


	/**
	 * @author jeason, 2014-4-18 下午2:36:25
	 * @param context
	 */
	public LoadingIndicator(Context context) {
		mContext = context;
		dialog = new Dialog(context, R.style.dialog_translucent);
		dialog.setContentView(R.layout.loading_indicator);
		dialog.setCancelable(false);
	}

	public void show() {
		ImageView loadingImg = (ImageView) dialog.findViewById(R.id.loadingImg);
		Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.loading);
		loadingImg.startAnimation(anim);
		dialog.show();
	}

	Handler handler = new Handler();

	public void dismiss() {
		dialog.dismiss();
	}
}
