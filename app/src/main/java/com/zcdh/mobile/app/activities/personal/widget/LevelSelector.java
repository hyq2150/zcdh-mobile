/**
 * 
 * @author jeason, 2014-5-22 下午4:48:36
 */
package com.zcdh.mobile.app.activities.personal.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zcdh.mobile.R;

/**
 * @author jeason, 2014-5-22 下午4:48:36
 * 技能掌握程度选择器
 */
public class LevelSelector implements OnClickListener {
	private PopupWindow popupWindow;
	private LayoutInflater inflater;
	private View view;

	private int displayWidth;
	private int displayHeight;

	private Button master;
	private Button advanced;
	private Button premium;
	private Button not_much;
	private LevelSelectorListener mListener;

	/**
	 * @author jeason, 2014-5-22 下午5:46:51
	 */
	public LevelSelector(Activity activity) {

		displayWidth = activity.getWindowManager().getDefaultDisplay().getWidth();

		inflater = LayoutInflater.from(activity);
		view = inflater.inflate(R.layout.level_selector, null);
		bindViews();
	}

	/**
	 * 
	 * @author jeason, 2014-5-22 下午10:58:38
	 */
	private void bindViews() {
		master = (Button) view.findViewById(R.id.master);
		master.setOnClickListener(this);
		advanced = (Button) view.findViewById(R.id.advanced);
		advanced.setOnClickListener(this);
		premium = (Button) view.findViewById(R.id.premium);
		premium.setOnClickListener(this);
		not_much = (Button) view.findViewById(R.id.not_much);
		not_much.setOnClickListener(this);

		view.measure(0, 0);
		displayHeight = view.getMeasuredHeight();
		popupWindow = new PopupWindow(view, displayWidth, displayHeight);
		popupWindow.setContentView(view);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
	}

	public void show(View v, LevelSelectorListener listener) {
		mListener = listener;

		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + v.getWidth(), location[1]);
	}

	public void onDismiss() {
		if (popupWindow != null) popupWindow.dismiss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.master:
			if (mListener != null) {
				mListener.onLevelSelected("002.001");
			}
			break;
		case R.id.advanced:
			if (mListener != null) {
				mListener.onLevelSelected("002.002");
			}
			break;
		case R.id.premium:
			if (mListener != null) {
				mListener.onLevelSelected("002.003");
			}
			break;
		case R.id.not_much:
			if (mListener != null) {
				mListener.onLevelSelected("002.004");
			}
			break;

		default:
			break;
		}
		onDismiss();
	}

	public interface LevelSelectorListener {
		public void onLevelSelected(String paramcode);
	}
}
