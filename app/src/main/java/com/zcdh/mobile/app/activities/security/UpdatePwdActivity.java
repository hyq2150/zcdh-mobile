/**
 * 
 * @author jeason, 2014-5-14 下午2:56:43
 */
package com.zcdh.mobile.app.activities.security;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-5-14 下午2:56:43
 * 更新密码
 */
@EActivity(R.layout.update_pwd)
public class UpdatePwdActivity extends BaseActivity implements RequestListener {

	IRpcJobUservice userService;

	@ViewById(R.id.old_pass)
	EditText old_pass;
	@ViewById(R.id.pwd)
	EditText pwd;
	@ViewById(R.id.pwd2)
	EditText pwd2;

	String old_password;
	String password;
	String password2;

	ProcessDialog loading;

	public String kREQ_ID_UPDATEPWD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		loading = new ProcessDialog(this);
	}

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.update_pwd));

	}

	private boolean checkContent() {
		old_password = this.old_pass.getText().toString();
		password = this.pwd.getText().toString();
		password2 = this.pwd2.getText().toString();

		if (StringUtils.isBlank(old_password) || StringUtils.isBlank(password) || StringUtils.isBlank(password2)) {
			Toast.makeText(this, R.string.please_complete_all_field, Toast.LENGTH_SHORT).show();
			return false;
		}

		if (password.length() < 6) {
			Toast.makeText(this, R.string.password_length_too_short, Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!password.equals(password2)) {

			Toast.makeText(this, R.string.not_the_same_password, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Click(R.id.upateBtn)
	void onUpdate() {
		if (checkContent()) {
			loading.show();
			doUpdatepass();
		}
	}

	@Background
	void doUpdatepass() {
		userService.updatePwd(getUserId(), password, old_password).identify(kREQ_ID_UPDATEPWD = RequestChannel.getChannelUniqueID(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestStart(java.lang
	 * .String)
	 */
	@Override
	public void onRequestStart(String reqId) {
		
		loading.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestSuccess(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		// TODO Auto-generated method stub
		loading.dismiss();
		// 0 成功 ， 5 密码错误
		int real_result = (Integer) result;
		switch (real_result) {
		case 0:
			Toast.makeText(this, R.string.update_pwd_successfully, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case 5:
			Toast.makeText(this, R.string.failed_to_reset_pwd, Toast.LENGTH_SHORT).show();
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestFinished(java.
	 * lang.String)
	 */
	@Override
	public void onRequestFinished(String reqId) {
		loading.dismiss();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestError(java.lang
	 * .String, java.lang.Exception)
	 */
	@Override
	public void onRequestError(String reqID, Exception error) {
		loading.dismiss();

	}
}
