/**
 * 
 * @author jeason, 2014-6-5 下午10:11:37
 */
package com.zcdh.mobile.app.activities.detail;

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

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.BasicSysCommentTagDTO;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;

/**
 * @author jeason, 2014-6-5 下午10:11:37
 * 公司印象数据适配器
 */
public class BasicImpressionTagsAdapter extends PredicateAdapter {

	private List<BasicSysCommentTagDTO> impressions;

	private LayoutInflater mFlater;

	private Activity mActivity;

	private ActionTarget at;

	public BasicImpressionTagsAdapter(Activity activity) {
		mActivity = activity;
		mFlater = LayoutInflater.from(mActivity);
		at = new ActionTarget();
		this.impressions = new ArrayList<BasicSysCommentTagDTO>();
	}

	public void updateItems(List<BasicSysCommentTagDTO> list) {
		impressions.clear();
		impressions.addAll(list);
		notifyDataSetChanged();
	}

	public List<BasicSysCommentTagDTO> getItems() {
		return impressions;
	}

	private BasicSysCommentTagDTO getItem(int position) {
		return impressions.get(position);
	}

	@Override
	public View getView(final int position, ViewGroup parentView) {
		BasicSysCommentTagDTO expression = impressions.get(position);
		View view = mFlater.inflate(R.layout.basic_evaluation_tag, null);
		TextView tag_name = (TextView) view.findViewById(R.id.tv_tag_name);
		ImageView selectIV = (ImageView) view.findViewById(R.id.iv_selected);
		tag_name.setText(expression.getTagName());
//		点击事件
		tag_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				如果是当前已选中触发Remove事件否则为添加时间
				if (!isSelected(position)) {
					onSelect(getItem(position));
				} else {
					onRemoveSelect(getItem(position));
				}
			}

		});

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
	 * @author jeason, 2014-6-6 上午9:48:50
	 */
	protected void onRemoveSelect(BasicSysCommentTagDTO item) {
		at.invokeMethod(mActivity, "onRemoveSelect", item);
	}

	private void onSelect(BasicSysCommentTagDTO item) {
		at.invokeMethod(mActivity, "onSelect", item);
	}

	private boolean isSelected(int position) {
		return (Boolean) at.invokeMethod(mActivity, "isSelected", getItem(position));
	}

	@Override
	public int getCount() {
		return impressions.size();
	}

	@Override
	public void setLayout(ViewGroup parentView) {
		int unit = 40; // UnitTransfer.dip2px(getActivity(), 40);
		int height = impressions.size() * unit;
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
		int margin = 20;
		return margin * 2;
	}

}
