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
import com.zcdh.mobile.api.model.JobTrainListDTO;
import com.zcdh.mobile.app.activities.personal.ExperenceListener;

/**
 * @author jeason, 2014-5-9 上午9:44:33
 * 培训经历项View
 */
@EViewGroup(R.layout.edu_exp_item)
public class TranningExperenceItem extends FrameLayout {

	@ViewById(R.id.tvTitle)
	TextView tvTitle;

	@ViewById(R.id.tvNote)
	TextView tvNote;

	@ViewById(R.id.btn_delete)
	ImageView btn_delete;

	ExperenceListener mListener;
	JobTrainListDTO mItem;

	/**
	 * @author jeason, 2014-5-9 上午9:53:37
	 * @param context
	 */
	public TranningExperenceItem(Context context) {
		this(context, null);
	}

	public TranningExperenceItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 
	 * @author jeason, 2014-5-9 上午10:02:32
	 * @param item
	 * @param index
	 */
	public void initData(ExperenceListener callbacker, JobTrainListDTO item, boolean delete_mode) {
		mListener = callbacker;
		mItem = item;
		setDeleteMode(delete_mode);
		tvTitle.setText(item.getTrainInstitution());
		tvNote.setText(item.getCourseName() + "(" + item.getCredentialName() + ")");
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
	void onTranningItemClick() {
		mListener.onTranningExpItemClick(mItem.getTrainId());
	}

	@Click(R.id.btn_delete)
	void onTranningExpItemDelete() {
		mListener.onTranningExpItemDelete(mItem.getTrainId());
	}

	/**
	 * @return
	 * @author jeason, 2014-5-9 下午5:36:04
	 */
	public JobTrainListDTO getmItem() {
		return mItem;
	}

}
