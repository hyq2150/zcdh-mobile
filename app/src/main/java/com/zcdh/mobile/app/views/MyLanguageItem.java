/**
 * 
 * @author jeason, 2014-5-24 下午3:49:45
 */
package com.zcdh.mobile.app.views;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import utils.ActionTarget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobLanguageDTO;

/**
 * @author jeason, 2014-5-24 下午3:49:45
 * 我的语言View
 */
@EViewGroup(R.layout.my_languages_item)
public class MyLanguageItem extends RelativeLayout {
	@ViewById(R.id.tvLanguageName)
	TextView tvLanguageName;
	@ViewById(R.id.tvDesc_content)
	TextView tvDesc_content;
	@ViewById(R.id.remove)
	ImageView remove;

	private JobLanguageDTO item;
	private boolean mEditMode;

	ActionTarget at = new ActionTarget();
	private Object mCallbacker;

	/**
	 * @author jeason, 2014-5-24 下午3:50:26
	 * @param context
	 */
	public MyLanguageItem(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public MyLanguageItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public JobLanguageDTO getItem() {
		return item;
	}

	public void initData(Object callbacker, JobLanguageDTO item, boolean edit_mode) {
		mCallbacker = callbacker;
		this.item = item;
		initView();
		mEditMode = edit_mode;
		if (edit_mode) {
			remove.setVisibility(View.VISIBLE);
		} else {
			remove.setVisibility(View.GONE);
		}
	}

	private void initView() {
		if (getItem() != null) {
			tvLanguageName.setText(getItem().getLanguageName());
			tvDesc_content.setText(String.format("读写:%s/听说:%s", getItem().getHeadWriteParamName(), getItem().getHearSpeakParamName()));
		}
	}

	@Click(R.id.remove)
	void onRemove() {
		if (!mEditMode) {
			return;
		}
		at.invokeMethod(mCallbacker, "onRemoveSelect", getItem());
	}
}
