/**
 * 
 * @author jeason, 2014-5-24 上午9:54:15
 */
package com.zcdh.mobile.app.activities.personal.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.zcdh.mobile.R;
import com.zcdh.mobile.utils.StringUtils;

/**
 * @author jeason, 2014-5-24 上午9:54:15
 * 语言技能熟悉程度选择
 */
public class LanguageLevelSelector implements OnClickListener {
	private PopupWindow popupWindow;
	private LayoutInflater inflater;
	private View view;

	private int displayWidth;
	private int displayHeight;

	private RadioButton master_rw;
	private RadioButton advanced_rw;
	private RadioButton premium_rw;
	private RadioButton not_much_rw;

	private RadioButton master_ls;
	private RadioButton advanced_ls;
	private RadioButton premium_ls;
	private RadioButton not_much_ls;
	
	private LevelSelectorListener mListener;

	/**
	 * @author jeason, 2014-5-22 下午5:46:51
	 */
	public LanguageLevelSelector(Activity activity) {

		displayWidth = activity.getWindowManager().getDefaultDisplay().getWidth();

		inflater = LayoutInflater.from(activity);
		view = inflater.inflate(R.layout.language_level_selector, null);
		bindViews();
	}

	/**
	 * 
	 * @author jeason, 2014-5-22 下午10:58:38
	 */
	private void bindViews() {
		master_rw = (RadioButton) view.findViewById(R.id.master_in_reading);
		master_rw.setOnClickListener(this);
		advanced_rw = (RadioButton) view.findViewById(R.id.advanced_in_reading);
		advanced_rw.setOnClickListener(this);
		premium_rw = (RadioButton) view.findViewById(R.id.premium_in_reading);
		premium_rw.setOnClickListener(this);
		not_much_rw = (RadioButton) view.findViewById(R.id.not_much_in_reading);
		not_much_rw.setOnClickListener(this);
		

		master_ls = (RadioButton) view.findViewById(R.id.master_in_listening_and_speaking);
		master_ls.setOnClickListener(this);
		advanced_ls = (RadioButton) view.findViewById(R.id.advanced_in_listening_and_speaking);
		advanced_ls.setOnClickListener(this);
		premium_ls = (RadioButton) view.findViewById(R.id.premium_in_listening_and_speaking);
		premium_ls.setOnClickListener(this);
		not_much_ls = (RadioButton) view.findViewById(R.id.not_much_in_listening_and_speaking);
		not_much_ls.setOnClickListener(this);
		

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
		master_rw.setChecked(false);
		advanced_rw.setChecked(false);
		premium_rw.setChecked(false);
		not_much_rw.setChecked(false);
		master_ls.setChecked(false);
		advanced_ls.setChecked(false);
		premium_ls.setChecked(false);
		not_much_ls.setChecked(false);
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
		
		String tag = (String) v.getTag();
		if("rw".equals(tag)){ //读写
			master_rw.setChecked(false);
			advanced_rw.setChecked(false);
			premium_rw.setChecked(false);
			not_much_rw.setChecked(false);
		}
		if("lw".equals(tag)){// 听写
			master_ls.setChecked(false);
			advanced_ls.setChecked(false);
			premium_ls.setChecked(false);
			not_much_ls.setChecked(false);
		}
		
		switch (v.getId()) {
		case R.id.master_in_reading:
			mListener.onReadingAndWritingCode("002.001");
			master_rw.setChecked(true);
			break;
		case R.id.master_in_listening_and_speaking:
			mListener.onListeningAndSpeakingCode("002.001");
			master_ls.setChecked(true);
			break;
		case R.id.advanced_in_reading:
			mListener.onReadingAndWritingCode("002.002");
			advanced_rw.setChecked(true);
			break;
		case R.id.advanced_in_listening_and_speaking:
			mListener.onListeningAndSpeakingCode("002.002");
			advanced_ls.setChecked(true);
			break;
		case R.id.premium_in_reading:
			mListener.onReadingAndWritingCode("002.003");
			premium_rw.setChecked(true);
			break;
		case R.id.premium_in_listening_and_speaking:
			mListener.onListeningAndSpeakingCode("002.003");
			premium_ls.setChecked(true);
			break;

		case R.id.not_much_in_reading:
			mListener.onReadingAndWritingCode("002.004");
			not_much_rw.setChecked(true);
			break;
		case R.id.not_much_in_listening_and_speaking:
			mListener.onListeningAndSpeakingCode("002.004");
			not_much_ls.setChecked(true);
			break;
		default:
			break;
		}
		
		

	}
	

	public static abstract class LevelSelectorListener {

		public String readingAndWritingCode;

		public String listeningAndSpeakingCode;

		public abstract void onReadingAndWritingCode(String paramcode);

		public abstract void onListeningAndSpeakingCode(String paramcode);

		boolean isDismissable() {
			return !StringUtils.isBlank(readingAndWritingCode) && !StringUtils.isBlank(listeningAndSpeakingCode);
		}

		public String getReadingAndWritingCode() {
			return readingAndWritingCode;
		}

		public void setReadingAndWritingCode(String readingAndWritingCode) {
			this.readingAndWritingCode = readingAndWritingCode;
		}

		public String getListeningAndSpeakingCode() {
			return listeningAndSpeakingCode;
		}

		public void setListeningAndSpeakingCode(String listeningAndSpeakingCode) {
			this.listeningAndSpeakingCode = listeningAndSpeakingCode;
		}
	}
}
