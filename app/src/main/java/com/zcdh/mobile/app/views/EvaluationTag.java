/**
 * 
 * @author jeason, 2014-5-16 下午8:22:00
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcdh.mobile.R;

/**
 * @author jeason, 2014-5-16 下午8:22:00
 * 自我评价标签View
 */
@EViewGroup(R.layout.basic_evaluation_tag)
public class EvaluationTag extends LinearLayout {

	private boolean mIs_basic_tag = true;

	@ViewById(R.id.tv_tag_name)
	TextView tv_tag_name;

	@ViewById(R.id.iv_indicator)
	ImageView iv_indicator;

	Object mCallbacker;
	ActionTarget at = new ActionTarget();

	private String tag_name;
	private String tag_code;
	private boolean mSelected = false;

	/**
	 * @author jeason, 2014-5-16 下午8:23:26
	 * @param context
	 */
	public EvaluationTag(Context context) {
		this(context, null);
	}

	public EvaluationTag(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void initEvaluationTag(Object callbacker, String tag_name, String tag_code, boolean is_basic_tag, boolean selected) {
		mCallbacker = callbacker;
		mSelected = selected;
		mIs_basic_tag = is_basic_tag;
		this.tag_code = tag_code;
		this.tag_name = tag_name;
		this.tv_tag_name.setText(tag_name);
		if (!is_basic_tag) {
			iv_indicator.setVisibility(View.VISIBLE);
			iv_indicator.setBackgroundResource(R.drawable.input_clear_normal);
		} else {
			if (mSelected) {
				iv_indicator.setVisibility(View.VISIBLE);
				iv_indicator.setBackgroundResource(R.drawable.icon_yes);
			}
		}
	}

	@Click(R.id.tag_wrapper)
	void onClickWrapper() {
		onSelectTag();
	}

	@Click(R.id.iv_indicator)
	void onClickTagName() {
		if (mIs_basic_tag)
			onSelectTag();
		else {
			removeTag();
		}
	}

	/**
	 * 
	 * @author jeason, 2014-5-16 下午8:44:22
	 */
	private void removeTag() {
		iv_indicator.setVisibility(View.GONE);
		mSelected = false;
		at.invokeMethod(mCallbacker, "removeSelected", tag_name, tag_code);
	}

	@Click(R.id.tv_tag_name)
	void onSelectTag() {
		if (!mIs_basic_tag) return;

		if (!mSelected) {
			iv_indicator.setVisibility(View.VISIBLE);
			mSelected = true;
			iv_indicator.setBackgroundResource(R.drawable.list_remove);
			at.invokeMethod(mCallbacker, "onSelectTag", tag_name, tag_code);
		} else {
			removeTag();
		}
	}
}
