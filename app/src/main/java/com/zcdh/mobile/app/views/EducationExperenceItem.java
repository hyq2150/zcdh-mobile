/**
 * 
 * @author jeason, 2014-5-9 上午9:44:33
 */
package com.zcdh.mobile.app.views;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobEducationListDTO;
import com.zcdh.mobile.app.activities.personal.ExperenceListener;

/**
 * @author jeason, 2014-5-9 上午9:44:33
 * 教育经历
 */
@EViewGroup(R.layout.edu_exp_item)
public class EducationExperenceItem extends FrameLayout {

	@ViewById(R.id.tvTitle)
	TextView tvTitle;

	@ViewById(R.id.tvNote)
	TextView tvNote;

	Object mCallbacker;

	@ViewById(R.id.btn_delete)
	ImageView btn_delete;

	ExperenceListener mListener;
	private JobEducationListDTO mItem;
	/**
	 * @author jeason, 2014-5-9 上午9:53:37
	 * @param context
	 */
	public EducationExperenceItem(Context context) {
		this(context, null);
	}

	public EducationExperenceItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 
	 * @author jeason, 2014-5-9 上午10:02:32
	 * @param item
	 * @param index
	 */
	public void initData(ExperenceListener callbacker, JobEducationListDTO item, boolean delete_mode) {
		mListener = callbacker;
		mItem = item;
		setDeleteMode(delete_mode);
		tvTitle.setText(item.getSchool() + "("+item.getDegreeName()+")");
		tvNote.setText(item.getMajorName());
	}
	
	

	public void setDeleteMode(boolean delete_mode) {
		if (delete_mode) {
			btn_delete.setVisibility(View.VISIBLE);
		} else {
			btn_delete.setVisibility(View.GONE);
		}
	}

	/**
	 * 教育经历点击事件
	 * 
	 * @author jeason, 2014-5-9 上午10:09:37
	 */
	@Click(R.id.rl_exp_item)
	void onEduExpItemClick() {
		mListener.onEduExpItemClick(mItem.getEduId());
	}
	
	@Click(R.id.btn_delete)
	void onEduExpItemDelete() {
		mListener.onEduExpItemDelete(mItem.getEduId());
	}

	public JobEducationListDTO getmItem() {
		return mItem;
	}

}
