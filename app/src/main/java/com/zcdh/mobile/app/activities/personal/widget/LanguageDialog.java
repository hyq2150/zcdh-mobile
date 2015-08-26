/**
 * 
 * @author jeason, 2014-5-23 下午9:01:32
 */
package com.zcdh.mobile.app.activities.personal.widget;

import java.util.List;

import utils.ActionTarget;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobLanguageDTO;
import com.zcdh.mobile.app.views.TagsContainer;

/**
 * @author jeason, 2014-5-23 下午9:01:32
 * 语言选择dialog 需手动添加并重写onReInitialLanguage
 * 建议使用TagsDialog
 * @deprecated
 */
public class LanguageDialog {
	private Dialog dialog;
	private TagsContainer tagContainer;
	ActionTarget actionTarget;
	Activity mActivity;
	LanguageAdapter adapter;
	View v;

	public LanguageDialog(Activity activity) {

		mActivity = activity;
		adapter = new LanguageAdapter(mActivity);
		actionTarget = new ActionTarget();
		dialog = new Dialog(mActivity, R.style.dialog_translucent);
		v = LayoutInflater.from(mActivity).inflate(R.layout.language_dialog, null);
		dialog.setContentView(v);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		//避免多层弹窗报错
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				adapter.onDismiss();
			}
		});
		bindview();
	}

	/**
	 * 
	 * @author jeason, 2014-5-20 下午4:34:03
	 */
	private void bindview() {
		tagContainer = (TagsContainer) v.findViewById(R.id.skill_container);
		tagContainer.setAdapter(mActivity, adapter);
		
		//请求技能数据
		onReInitialLanguage();
	}

	/**
	 * 
	 * @author jeason, 2014-5-20 下午4:46:47
	 */
	private void onReInitialLanguage() {
		actionTarget.invokeMethod(mActivity, "onReInitialLanguage");
	}

	/**
	 * 获取到语言数据后
	 * @param items
	 * @param selectedTags
	 * @author jeason, 2014-5-24 上午9:47:39
	 */
	public void reInitialLanguage(List<JobLanguageDTO> items, List<JobLanguageDTO> selectedTags) {
		if (selectedTags != null) adapter.updateSelectedItems(selectedTags);
		adapter.updateItems(items);
	}

	public void show() {
		int DisplayWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth() - 10;
		int DisplayHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight() / 2;
		Window window = dialog.getWindow();
		window.setLayout(DisplayWidth, DisplayHeight);
		window.setGravity(Gravity.BOTTOM);
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}

}
