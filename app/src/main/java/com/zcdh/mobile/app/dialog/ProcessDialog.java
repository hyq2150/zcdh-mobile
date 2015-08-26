package com.zcdh.mobile.app.dialog;
import com.zcdh.mobile.R;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 正在处理的模态dialog
 * @author yangjiannan
 *
 */
public class ProcessDialog extends Dialog {
	
	private TextView tipsText;
	private ImageView loadingImg;
	
	public ProcessDialog(Context context){
		this(context, R.style.process_dialog_theme);
	}

	public ProcessDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.process_dialog);
		tipsText = (TextView) findViewById(R.id.tipsText);
		loadingImg = (ImageView) findViewById(R.id.loadingImg2);
		
		
	}
	
	public void show(String tips){
		if(!isShowing()){
			show();
			this.setCanceledOnTouchOutside(false);
		}
		tipsText.setText(tips);
	
	}

	public void show(){
		super.show();
		Animation ani = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
		loadingImg.startAnimation(ani);
	}
}
