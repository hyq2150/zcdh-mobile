/**
 * 
 * @author jeason, 2014-5-12 下午8:53:55
 */
package com.zcdh.mobile.app.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.utils.StringUtils;

/**
 * @author jeason, 2014-5-12 下午8:53:55
 * 对话框模式的Editext
 */
public class EditableDialog extends AlertDialog {

	private EditText textfield;
	EditableDialogListener mListener;
	private int mKey;
	
	/**
	 * @author jeason, 2014-5-12 下午8:53:59
	 * @param context
	 */
	public EditableDialog(Context context) {
		super(context);
		View v = LayoutInflater.from(context).inflate(R.layout.editable_dialog, null);
		setView(v);
		textfield = (EditText) v.findViewById(R.id.editable_text);
		textfield.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus) {
		            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		            
		        }
		    }
		});
		textfield.requestFocus();
	}

	/**
	 * 
	 * @param listener
	 * @param key 识别key
	 * @param title 标题
	 * @return
	 * @author jeason, 2014-5-27 下午3:48:28
	 */
	public AlertDialog initData(EditableDialogListener listener, int key, String title) {
		return initData(listener, key, title, null, -1);
	}
	public AlertDialog initData(EditableDialogListener listener, int key, String title, String defaultValue) {
		textfield.setText(defaultValue);
		return initData(listener, key, title, defaultValue, -1);
	}
	public AlertDialog initData(EditableDialogListener listener, int key, String title, int inputType, String defaultValue) {
		return initData(listener, key, title, defaultValue, inputType);
	}
	public AlertDialog initData(EditableDialogListener listener, int key, String title, int inputType) {
		return initData(listener, key, title, null, inputType);
	}

	public AlertDialog initData(EditableDialogListener listener, int key, String title, String defaultContent, int inputType) {
		mListener = listener;
		mKey = key;
		if(inputType!=-1){
			textfield.setInputType(inputType);
		}
		textfield.requestFocus();
		if (defaultContent != null){
			textfield.setText(defaultContent);
			textfield.selectAll();
		}
		this.setTitle(title);
		Builder ab = new Builder(getContext());
		ab.setNegativeButton(R.string.cancel, null);
		this.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getText(R.string.confirm), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (StringUtils.isBlank(textfield.getText().toString())) {
					return;
				}
				mListener.onEditableConfirm(mKey, textfield.getText().toString());
			}
		});
		this.setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getText(R.string.cancel), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancel();
			}
		});
		
		return this;
	}

	public interface EditableDialogListener {
		public void onEditableConfirm(int key, String content);
	}
}
