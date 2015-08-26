/**
 * 
s * @author jeason, 2014-4-8 上午11:48:07
 */
package com.zcdh.mobile.app.activities.auth;

import java.util.regex.Pattern;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-4-8 上午11:48:07
 * 根据email重置密码
 */
@EActivity(R.layout.reset_password_by_email)
public class ResetPwdByEmailActivity extends BaseActivity implements RequestListener {

	private String kREQ_ID_RESETPWDBYEMAIL;
	@ViewById(R.id.account)
	EditText et_account;

	private String account;

	private IRpcJobUservice userService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.reset_pwd));
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
	}

	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
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
		// TODO Auto-generated method stub

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
		// 返回：
		// -1 失败，0 成功 4 账号不存在 ， 12 用户不存在 ， 13 用户绑定邮箱
		if (reqId.equals(kREQ_ID_RESETPWDBYEMAIL)) {
			int int_result = (Integer) result;
			switch (int_result) {
			case -1:
				Toast.makeText(this, R.string.failed_to_reset_pwd, Toast.LENGTH_SHORT).show();

				break;
			case 0:
				Toast.makeText(this, R.string.reset_pwd_successfully, Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 4:
				Toast.makeText(this, R.string.account_not_exists, Toast.LENGTH_SHORT).show();
				break;
			case 12:
				Toast.makeText(this, R.string.account_not_exists, Toast.LENGTH_SHORT).show();
				break;
			case 13:
				Toast.makeText(this, R.string.reset_pwd_failed_due_to_a_none_email_registed_account, Toast.LENGTH_SHORT).show();

				break;
			default:
				break;

			}
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	private boolean check_content() {
		account = et_account.getText().toString();
		if (StringUtils.isBlank(account) || StringUtils.isBlank(account)) {
			Toast.makeText(this, R.string.please_complete_all_info, Toast.LENGTH_SHORT).show();
			return false;
		}

		// validate phone_num & email
		if (!Pattern.compile(SharedPreferencesUtil.getValue(this, Constants.REGEX_PHONE_KEY, Constants.regex_phone)).matcher(account).matches()) {
			Toast.makeText(this, R.string.invalid_phone_format, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Click(R.id.resetBtn)
	void onResetPwd() {
		if (!check_content()) return;
		doReset();
	}

	/**
	 * 
	 * @author jeason, 2014-4-18 下午11:25:41
	 */
	@Background
	void doReset() {
		userService.resetPwdByEmail(account).identify(kREQ_ID_RESETPWDBYEMAIL = RequestChannel.getChannelUniqueID(), this);
	}
}
