/**
 * 
 * @author jeason, 2014-5-20 下午4:04:28
 */
package com.zcdh.mobile.app.activities.personal.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.views.EditableDialog;
import com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;

/**
 * @author jeason, 2014-5-20 下午4:04:28 通用标签选择Dialog 标签选择dialog组件类
 *         须实现onInitialTags() onShift() onSearch(String keyWord)
 *         onSearchWordChanged(String keyword) onDialogDismiss
 */
public class TagsDialog {

	private Dialog dialog;
	// tag容器
	private TagsContainer tagContainer;

	private TextView shift;

//	ActionTarget actionTarget;
	Activity mActivity;
	View v;

	PredicateAdapter adapter;
	private EditText et_search;
	private Button search;
	private TextView tagType;
	private LinearLayout ll_search_edit_frame;
	TextView customize_tag;
	TagsDialogListener mListener;

	/**
	 * @author jeason, 2014-4-18 下午2:36:25
	 * @param context
	 */
	public TagsDialog(Activity activity, boolean withCustomize, TagsDialogListener listener) {

		mActivity = activity;
//		actionTarget = new ActionTarget();
		mListener = listener;

		// dialog樣式設置
		dialog = new Dialog(mActivity, R.style.dialog_translucent);
		v = LayoutInflater.from(mActivity).inflate(R.layout.skills_dialog, null);
		ll_search_edit_frame = (LinearLayout) v.findViewById(R.id.search_edit_frame);
		customize_tag = (TextView) v.findViewById(R.id.customize_tag);

		// 是否弃用自定义标签按钮
		if (withCustomize) {
			customize_tag.setVisibility(View.VISIBLE);
			customize_tag.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onCustomizeTag();
				}
			});
		}

		dialog.setContentView(v);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				onDialogDismiss();
			}
		});

		bindview();
	}

	/**
	 * 
	 * @author jeason, 2014-7-10 下午6:48:16 自定义标签对话框
	 */
	protected void onCustomizeTag() {
		new EditableDialog(mActivity).initData(new EditableDialogListener() {
			
			@Override
			public void onEditableConfirm(int key, String content) {
				mListener.onEditableConfirm(key, content);
			}
		}, 0, "自定义标签").show();
	}

	// 对话框标题
	public void setTitle(String title) {
		tagType.setText(title);
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
		tagType = (TextView) v.findViewById(R.id.tagType);
		onInitialTags();
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

	public PredicateAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(PredicateAdapter adapter) {
		this.adapter = adapter;
		this.tagContainer.setAdapter(mActivity, this.adapter);
	}

	private void onInitialTags() {
//		actionTarget.invokeMethod(mActivity, "onInitialTags");
		mListener.onInitialTags();
	}

	/**
	 * 
	 * @author jeason, 2014-5-20 下午4:49:42
	 */
	protected void onShift() {
//		actionTarget.invokeMethod(mActivity, "onShift");
		mListener.onShift();
	}

	public void setSearchInvisible() {
		ll_search_edit_frame.setVisibility(View.GONE);
	}

	void onSearch() {
		String keyword = et_search.getText().toString();
//		actionTarget.invokeMethod(mActivity, "onSearch", keyword);
		mListener.onSearch(keyword);
	}

	void onSearchWordChanged(String content) {
//		actionTarget.invokeMethod(mActivity, "onSearchWordChanged", content);
		mListener.onSearchWordChanged(content);

	}

	/**
	 * 
	 * @author jeason, 2014-6-5 下午10:04:11
	 */
	protected void onDialogDismiss() {
//		actionTarget.invokeMethod(mActivity, "onDialogDismiss");
		mListener.onDialogDismiss();
	}
	
	public interface TagsDialogListener{
		/**
		 * 重写此方法 联网加载数据到Adapter
		 * @author jeason, 2014-7-25 下午4:18:34
		 */
		public void onInitialTags();
		/**
		 * 对话框消失
		 * @author jeason, 2014-7-25 下午4:15:49
		 */
		public void onDialogDismiss();
		
		/**
		 * 当搜索框内容变化时
		 * @param content
		 * @author jeason, 2014-7-25 下午4:15:01
		 */
		public void onSearchWordChanged(String content);
		
		/**
		 * 当按下搜索按钮
		 * @param keyword
		 * @author jeason, 2014-7-25 下午4:14:42
		 */
		public void onSearch(String keyword);
		
		/**
		 * 换一组
		 * @author jeason, 2014-7-25 下午4:14:10
		 */
		public void onShift();
		
		public void onEditableConfirm(int key, String content);
	}
	
}
