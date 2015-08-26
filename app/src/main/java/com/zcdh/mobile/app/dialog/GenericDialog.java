package com.zcdh.mobile.app.dialog;

import com.zcdh.mobile.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 
 * @author yangjiannan 修改用户头像
 *
 */
public class GenericDialog extends Dialog implements OnTouchListener {

	public GenericDialog(Context context, int layout) {
		super(context, R.style.picker_dialog_theme);
		View contentView = LayoutInflater.from(context).inflate(layout, null);
		contentView.setOnTouchListener(this);
		setContentView(contentView);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		dismiss();
		return false;
	}

}
