package com.zcdh.mobile.app.activities.search;

import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;

/**
 * 当列表需要多选的功能时，为选中的项提供显示的面板
 * 
 * @author yangjiannan
 * 
 */
public class MultSelectionPannel implements OnClickListener {

	/**
	 * 相关联Activity
	 */
	Activity attachedActivity;

	/**
	 * 显示的面板
	 */
	RelativeLayout multSelectionPannel;

	/**
	 * 显示与隐藏开关
	 */
	ImageButton toggleBtn;

	/**
	 * Pannel bar
	 */
	RelativeLayout pannelBar;

	/**
	 * 显示所有项的容器
	 */
	RelativeLayout selectionItemsContainer;

	/**
	 * 指示已经选择的数量
	 */
	TextView countIndicatorText; // 已选择数量

	/**
	 * 数据
	 * 
	 * @param activity
	 */
	HashMap<String, String> items = new HashMap<String, String>();

	public static int max = 3;

	boolean hidden_flag = false;

	private RemoveItemListner removeItemListner;
	
	public MultSelectionPannel(Activity activity, RemoveItemListner removeItemListner){
		this(activity);
		this.removeItemListner = removeItemListner;
		this.multSelectionPannel.setVisibility(View.VISIBLE);
	}

	public MultSelectionPannel(Activity activity) {
		this.attachedActivity = activity;
		bindViews();
	}

	/**
	 * 绑定UI
	 */
	void bindViews() {
		multSelectionPannel = (RelativeLayout) this.attachedActivity
				.findViewById(R.id.multSelectionPannel);
		toggleBtn = (ImageButton) multSelectionPannel
				.findViewById(R.id.toggleBtn);
		pannelBar = (RelativeLayout) multSelectionPannel
				.findViewById(R.id.pannelBar);
		selectionItemsContainer = (RelativeLayout) multSelectionPannel
				.findViewById(R.id.selectedItemsContainer);
		countIndicatorText = (TextView) multSelectionPannel
				.findViewById(R.id.countIndicatorText);
		countIndicatorText.setText("0/" + max);

		pannelBar.setOnClickListener(this);
		toggleBtn.setOnClickListener(this);
	}

	public void addItemsToPannel() {
		selectionItemsContainer.removeAllViews();
		int i = 0;
		for (String code: items.keySet()) {

			String name = items.get(code);

			final View selectionItem = LayoutInflater.from(attachedActivity)
					.inflate(R.layout.selection_item, null);

			final int id = i + 1;
			selectionItem.setId(i + 1);

			((TextView) selectionItem.findViewById(R.id.itemNameText))
					.setText(name);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			if (id > 1) {
				params.addRule(RelativeLayout.BELOW, id - 1);
			}
			params.topMargin = com.zcdh.mobile.utils.UnitTransfer.dip2px(
					attachedActivity, 5);
			selectionItemsContainer.addView(selectionItem, params);

			selectionItem.setClickable(true);
			selectionItem.setTag(code);
			selectionItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selectionItemsContainer.removeView(selectionItem);
					// CategoryPostActivity.selectedPosts.remove(v.getTag() +
					// "");
					String key = (String) v.getTag();
					items.remove(key);
					removeItemListner.onRemoveItem(key);
					addItemsToPannel();
				}
			});

			i++;
		}
		countIndicatorText.setText(items.size() + "/" + max);
	}

	public void addItems(String code, String name) {
		if(items.containsKey(code))return;
		if (items.size() < max) {
			items.put(code, name);
			addItemsToPannel();
		} else {
			Toast.makeText(attachedActivity, "最多选择" + max + "个",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.pannelBar || v.getId() == R.id.toggleBtn) {
			if(items!=null && items.size()>0){
				setHiden(hidden_flag); 
			}else{
				Toast.makeText(attachedActivity, "请先选择项目", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void setHiden(boolean hidden){
		if(hidden){
			selectionItemsContainer.setVisibility(View.GONE);
			hidden_flag = false;
			toggleBtn.setImageResource(R.drawable.xiabian_50x50);
		}else{
			selectionItemsContainer.setVisibility(View.VISIBLE);
			hidden_flag = true;
			toggleBtn.setImageResource(R.drawable.icon_up);
		}
	}

	public void refresh(HashMap<String, String> items) {
		this.items = items;
		addItemsToPannel();
	}

}
