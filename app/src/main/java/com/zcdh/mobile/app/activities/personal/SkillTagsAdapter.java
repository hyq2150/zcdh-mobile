/**
 * 
 * @author jeason, 2014-5-22 下午2:43:05
 */
package com.zcdh.mobile.app.activities.personal;

import java.util.ArrayList;
import java.util.List;

import utils.ActionTarget;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobTechnicalDTO;
import com.zcdh.mobile.app.activities.personal.widget.LevelSelector;
import com.zcdh.mobile.app.activities.personal.widget.LevelSelector.LevelSelectorListener;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;

/**
 * @author jeason, 2014-5-22 下午2:43:05
 * 技能标签数据适配器
 */
public class SkillTagsAdapter extends PredicateAdapter {

	private List<JobTechnicalDTO> skills;
	private List<JobTechnicalDTO> selectedSkills;
	LayoutInflater inflater;
	Activity mActivity;
	ActionTarget at = new ActionTarget();
	LevelSelector levelSelector;

	public SkillTagsAdapter(Activity activity) {
		mActivity = activity;
		inflater = LayoutInflater.from(activity);
		skills = new ArrayList<JobTechnicalDTO>();
		selectedSkills = new ArrayList<JobTechnicalDTO>();
		levelSelector = new LevelSelector(activity);
	}

	private JobTechnicalDTO getItem(int position) {
		return skills.get(position);
	}

	public void updateSelectedItems(List<JobTechnicalDTO> items) {
		selectedSkills.clear();
		selectedSkills.addAll(items);
	}

	public void updateItems(List<JobTechnicalDTO> items) {
		skills.clear();
		skills.addAll(items);
		notifyDataSetChanged();
	}

	public void onDismiss() {
		levelSelector.onDismiss();
	}

	@Override
	public View getView(final int position, ViewGroup parentView) {
		View view = inflater.inflate(R.layout.skill_tags_item_no_level, null);
		TextView tv_name = (TextView) view.findViewById(R.id.skillNameText);
		ImageView selectIV = (ImageView) view.findViewById(R.id.iv_indicator);
		tv_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isSelected(position)) {
					levelSelector.onDismiss();
					levelSelector.show(v, new LevelSelectorListener() {

						@Override
						public void onLevelSelected(String paramcode) {
							onSelect(getItem(position), paramcode);
						}
					});
				} else {
					onRemoveSelect(getItem(position));
				}
			}
		});
		tv_name.setText(getItem(position).getTechnicalName());
		if (isSelected(position)) {
			selectIV.setVisibility(View.VISIBLE);
			selectIV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onRemoveSelect(getItem(position));
				}
			});
		}
		return view;
	}

	/**
	 * @param item
	 * @author jeason, 2014-5-22 下午4:10:51
	 */
	protected void onRemoveSelect(JobTechnicalDTO item) {
		at.invokeMethod(mActivity, "onRemoveSelect", item);

	}

	/**
	 * @param item
	 * @author jeason, 2014-5-22 下午3:46:31
	 * @param paramcode
	 */
	protected void onSelect(JobTechnicalDTO item, String paramcode) {
		item.setParamCode(paramcode);
		at.invokeMethod(mActivity, "onSelect", item);
	}

	private boolean isSelected(int position) {
		JobTechnicalDTO skill = skills.get(position);
		for (JobTechnicalDTO dto : selectedSkills) {
			if (dto.getTechnicalCode().equals(skill.getTechnicalCode())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getCount() {
		return skills.size();
	}

	@Override
	public void setLayout(ViewGroup parentView) {
		int unit = 40;
		int height = skills.size() * unit;
		parentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.framework.adapters.PredicateAdapter#getMarginOffset()
	 */
	@Override
	public int getMarginOffset() {
		int margin = 20;
		return margin * 2;
	}

}
