package com.zcdh.mobile.app.activities.nearby;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.views.SegmentedGroup;

/**
 * 地图设置对话框
 * 
 * @author YJN
 *
 */
public class MapSettingDialog extends Dialog {

	private static final String TAG = MapSettingDialog.class.getSimpleName();

	MapSettingProxy proxy;

	/**
	 * 地图2d图层切换
	 */
	ImageButton mapType2dBtn;
	/**
	 * 卫星地图图层切换
	 */
	ImageButton mapType3dBtn;

	SegmentedGroup segmentGroup;

	public MapSettingDialog(Activity context, MapSettingProxy proxy) {
		super(context, R.style.process_dialog_theme);
		this.proxy = proxy;
		bindView();
	}

	void bindView() {
		//getWindow().setWindowAnimations(R.anim.menu_dialog_anim);
		setContentView(R.layout.map_setting);

		if (segmentGroup == null) {
			segmentGroup = (SegmentedGroup) findViewById(R.id.segmented2);
			segmentGroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							String tempPropertyCode = "";
							switch (checkedId) {
							case R.id.quanzhiBtn:
								tempPropertyCode = Constants.POST_PROPERTY_QUANZHI;
								break;

							case R.id.jianzhiBtn:
								tempPropertyCode = Constants.POST_PROPERTY_JIANZHI;
								break;

							case R.id.jiaqiBtn:
								tempPropertyCode = Constants.POST_PROPERTY_JIAQI;
								break;
							}
							/*
							 * if (!tempPropertyCode.equals(postPropertyCode)) {
							 * hasMovingMap = true; postPropertyCode =
							 * tempPropertyCode; loadPoints(KLAST_LOAD_FLAG);
							 * 
							 * }
							 */
							proxy.onSelectPostType(tempPropertyCode);
							dismiss();
						}
					});
		}

	}

	/**
	 * 解决在2.x 系统下，点击对话框外面无法dismiss
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// I only care if the event is an UP action
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// create a rect for storing the window rect
			Rect r = new Rect(0, 0, 0, 0);
			// retrieve the windows rect
			this.getWindow().getDecorView().getHitRect(r);
			// check if the event position is inside the window rect
			boolean intersects = r.contains((int) event.getX(),
					(int) event.getY());
			// if the event is not inside then we can close the activity
			if (!intersects) {
				// close the activity
				// this.finish();
				Log.i(TAG, "touched outside");
				dismiss();
				// notify that we consumed this event
				return true;
			}
		}
		// let the system handle the event
		return super.onTouchEvent(event);
	}
}

interface MapSettingProxy {
	void onSelectPostType(String postPropertyCode);
}
