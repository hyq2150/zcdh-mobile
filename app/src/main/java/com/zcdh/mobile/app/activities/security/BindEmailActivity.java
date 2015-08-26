package com.zcdh.mobile.app.activities.security;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.UserAccountManagerDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * 
 * @author yangjiannan 绑定邮箱
 */
@EActivity(R.layout.activity_bind_email)
public class BindEmailActivity extends BaseActivity implements RequestListener {

	String kREQ_ID_bindAccount;

	IRpcJobUservice uservice;

	/**
	 * 电邮
	 */
	@ViewById(R.id.emailEditText)
	EditText emailEditText;

	ProcessDialog processDialog;

	@Extra
	boolean update;

	@Extra
	String updateEmail;

	@AfterViews
	void bindViews() {
		if (update) {
			SystemServicesUtils.setActionBarCustomTitle(this,
					getSupportActionBar(), "修改邮箱");
			emailEditText.setText(updateEmail);
			emailEditText.setSelectAllOnFocus(true);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		} else {
			SystemServicesUtils.setActionBarCustomTitle(this,
					getSupportActionBar(), "绑定邮箱");
		}
		uservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		processDialog = new ProcessDialog(this);
	}

	@Click(R.id.bindBtn)
	void onBindEmail() {
		String email = emailEditText.getText().toString();

		if (!StringUtils.isBlank(email)) {
			if (Pattern.compile(Constants.regex_email).matcher(email).matches()) {
				processDialog.show();
				bindEmail(email);
			} else {
				Toast.makeText(this, "请填写正确的电邮", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "请填写要绑定的电邮", Toast.LENGTH_SHORT).show();
		}
	}

	@Background
	void bindEmail(String email) {
		/*uservice.bindAccount(getUserId(), "email", email)
				.identify(
						kREQ_ID_bindAccount = RequestChannel
								.getChannelUniqueID(),
						this);*/
		uservice.updateBindUserEmail(getUserId(), email)
		.identify(kREQ_ID_bindAccount=RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (result != null) {
			// 0 成功,-1 绑定失败,2 手机号码已经存在,3 邮箱已经存在,9 qq 已经绑定， 10 微博已经绑定
			int resultCode = (Integer) result;
			switch (resultCode) {
			case 0:
				Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
				UserAccountManagerDTO accountManagerDTO = new UserAccountManagerDTO();
				accountManagerDTO.setBindType("email");
				accountManagerDTO.setBindValue(emailEditText.getText()
						.toString());
				Intent data = new Intent();
				data.putExtra("account", accountManagerDTO);
				setResult(RESULT_OK, data);
				finish();

				break;
			case -1:
				Toast.makeText(this, "绑定失败", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(this, "邮箱已存在", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		processDialog.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}
}
