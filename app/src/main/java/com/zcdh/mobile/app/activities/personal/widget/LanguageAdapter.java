/**
 * 
 * @author jeason, 2014-5-24 上午9:39:33
 */
package com.zcdh.mobile.app.activities.personal.widget;

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
import com.zcdh.mobile.api.model.JobLanguageDTO;
import com.zcdh.mobile.app.activities.personal.widget.LanguageLevelSelector.LevelSelectorListener;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;

/**
 * @author jeason, 2014-5-24 上午9:39:33
 * 语言技能数据源适配器 需在所调用的Activity中手动添加 onRemoveSelect onSelect 方法
 */
public class LanguageAdapter extends PredicateAdapter {

	private List<JobLanguageDTO> skills;

	private List<JobLanguageDTO> selectedSkills;

	LayoutInflater inflater;

	Activity mActivity;

	ActionTarget at = new ActionTarget();

	LanguageLevelSelector levelSelector;

	public LanguageAdapter(Activity activity) {

		mActivity = activity;
		inflater = LayoutInflater.from(activity);
		skills = new ArrayList<JobLanguageDTO>();
		selectedSkills = new ArrayList<JobLanguageDTO>();
		levelSelector = new LanguageLevelSelector(activity);

	}

	private JobLanguageDTO getItem(int position) {
		return skills.get(position);
	}

	public void updateSelectedItems(List<JobLanguageDTO> selectedTags) {
		selectedSkills.clear();
		selectedSkills.addAll(selectedTags);
	}

	public void updateItems(List<JobLanguageDTO> items) {

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
					levelSelector.show(v, new LevelSelectorListener() {

						@Override
						public void onReadingAndWritingCode(String rwParamcode) {
							setReadingAndWritingCode(rwParamcode);
							if (isDismissable()) {
								onSelect(getItem(position), getReadingAndWritingCode(), getListeningAndSpeakingCode());
								levelSelector.onDismiss();
							}
						}

						@Override
						public void onListeningAndSpeakingCode(String lsParamcode) {
							setListeningAndSpeakingCode(lsParamcode);
							if (isDismissable()) {
								onSelect(getItem(position), getReadingAndWritingCode(), getListeningAndSpeakingCode());
								levelSelector.onDismiss();
							}
						}
					});
				} else {
					onRemoveSelect(getItem(position));
				}
			}
		});
		tv_name.setText(getItem(position).getLanguageName());
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
	protected void onRemoveSelect(JobLanguageDTO item) {
		at.invokeMethod(mActivity, "onRemoveSelect", item);

	}

	/**
	 * @param item
	 * @author jeason, 2014-5-22 下午3:46:31
	 * @param paramcode
	 */
	protected void onSelect(JobLanguageDTO item, String rwParamCode, String lsParamCode) {
		item.setReadWriteParamCode(rwParamCode);
		item.setHeadWriteParamName(getParamNameByCode(rwParamCode));

		item.setHearSpeakParamCode(lsParamCode);
		item.setHearSpeakParamName(getParamNameByCode(lsParamCode));

		at.invokeMethod(mActivity, "onSelect", item);
	}

	private String getParamNameByCode(String paramCode) {
		if ("002.001".equals(paramCode)) {
			return "精通";
		}
		if ("002.002".equals(paramCode)) {
			return "熟悉";
		}
		if ("002.003".equals(paramCode)) {
			return "会些";
		}
		if ("002.004".equals(paramCode)) {
			return "知道";
		}
		return "精通";
	}

	private boolean isSelected(int position) {
		JobLanguageDTO skill = skills.get(position);
		for (JobLanguageDTO dto : selectedSkills) {
			if (dto.getLanguageCode().equals(skill.getLanguageCode())) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.adapters.PredicateAdapter#getMarginOffset()
	 */
	@Override
	public int getMarginOffset() {
		return 10 * 2 + 20;
	}

}
