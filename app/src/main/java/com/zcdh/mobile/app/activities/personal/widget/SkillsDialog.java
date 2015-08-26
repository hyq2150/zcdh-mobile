/**
 * 
 * @author jeason, 2014-5-20 下午4:04:28
 * 技能选择Dialog
 */
package com.zcdh.mobile.app.activities.personal.widget;

import java.util.List;

import utils.ActionTarget;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobTechnicalDTO;
import com.zcdh.mobile.app.activities.personal.SkillTagsAdapter;
import com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener;
import com.zcdh.mobile.app.views.TagsContainer;

/**
 * @author jeason, 2014-5-20 下午4:04:28
 * @deprecated
 * 弃用 建议使用TagsDialog
 */
public class SkillsDialog {
	private Dialog dialog;
	private TagsContainer tagContainer;
	private TextView shift;
//	ActionTarget actionTarget;
	Activity mActivity;
	View v;
	SkillTagsAdapter adapter;
	private EditText et_search;
	private Button search;
	TagsDialogListener mListener;

	
	/**
	 * @author jeason, 2014-4-18 下午2:36:25
	 * @param context
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SkillsDialog(Activity activity,TagsDialogListener listener) {
		this.mListener = listener;
		mActivity = activity;
//		actionTarget = new ActionTarget();
		adapter = new SkillTagsAdapter(mActivity);
		dialog = new Dialog(mActivity, R.style.dialog_translucent);
		v = LayoutInflater.from(mActivity).inflate(R.layout.skills_dialog, null);
		dialog.setContentView(v);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
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
		shift = (TextView) v.findViewById(R.id.shift);
		shift.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onShift();
			}
		});
		et_search = (EditText) v.findViewById(R.id.keywordEditText);
		ImageButton micBtn = (ImageButton) v.findViewById(R.id.micBtn);
		micBtn.setImageResource(R.drawable.icon_search_pressed);
		v.findViewById(R.id.spi).setVisibility(View.GONE);
		
		search = (Button) v.findViewById(R.id.searchBtn);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSearch();
			}
		});

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				onSearchWordChanged(s.toString());

			}
		});
		tagContainer.setAdapter(mActivity, adapter);
	}

	/**
	 * 
	 * @author jeason, 2014-5-20 下午4:49:42
	 */
	protected void onShift() {
//		actionTarget.invokeMethod(mActivity, "onShift");
		mListener.onShift();
	}

	void onSearch() {
		String keyword = et_search.getText().toString();
//		actionTarget.invokeMethod(mActivity, "onSearch", keyword);
		mListener.onSearch(keyword);
	}
	
	void onSearchWordChanged(String content){
//		actionTarget.invokeMethod(mActivity, "onSearchWordChanged", content);
		mListener.onSearchWordChanged(content);

	}

	/**
	 * 
	 * @author jeason, 2014-5-20 下午4:46:47
	 */
	private void onReInitialSkills() {
//		actionTarget.invokeMethod(mActivity, "onReInitialSkills", "");
		mListener.onInitialTags();
	}

	public void reInitialSkills(List<JobTechnicalDTO> items, List<JobTechnicalDTO> selectedTags) {
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
