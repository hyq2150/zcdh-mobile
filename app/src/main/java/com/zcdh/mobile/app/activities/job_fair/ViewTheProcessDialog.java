package com.zcdh.mobile.app.activities.job_fair;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;

public class ViewTheProcessDialog extends Dialog {

	private Context context;
	private ImageView process_imageview;
	private DisplayImageOptions options;
	private String imageUrl;

	public ViewTheProcessDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true).build();
		initalize();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		ImageLoader.getInstance().displayImage(imageUrl, process_imageview,
				options);
	}

	private void initalize() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_process_dialog, null);
		setContentView(view);
		initWindow();
		process_imageview = (ImageView) findViewById(R.id.process_imageview);
	}

	private void initWindow() {
		Window dialogWindow = getWindow();
		dialogWindow.setBackgroundDrawable(new ColorDrawable(0));
		dialogWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics();
		lp.width = (int) (d.widthPixels * 0.9);
		lp.height = (int) (d.heightPixels * 0.7);
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
