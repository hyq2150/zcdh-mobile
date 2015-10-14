package com.zcdh.mobile.app.activities.auth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 湖北公共招聘网登陆入口
 * @author huyongqing
 *
 */
@EActivity(R.layout.activity_login_hubei)
public class LoginByHBActivity  extends BaseActivity implements LoginListener {
	
	@ViewById(R.id.userEditText)
	EditText userEditText;

	@ViewById(R.id.pwdEditText)
	EditText pwdEditText;

	@ViewById(R.id.loginBtn)
	Button loginBtn;
	
	private ProcessDialog loading;
	private String account;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	@AfterViews
	void bindViews() {
		SystemServicesUtils.displayCustomTitle(this,
				getSupportActionBar(), getString(R.string.title_login_hubei));
		loading = new ProcessDialog(this);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 通过职场导航账号登录
	 */
	@Click(R.id.loginBtn)
	void doLoginHB() {
		if (check_content()) {
			userEditText.clearFocus();
			//loading.show(getString(R.string.loging));
			LoginHelper.getInstance(this, this).doLoginHB(account, password);
		}
	}

	/**
	 * 根据登录用户id，获取对应的环信账户
	 */
	private void getHxAccount() {
		LoginHelper.getInstance(this, this)
				.getHxAccount(ZcdhApplication.getInstance().getZcdh_uid());
	}
	
	private boolean check_content() {
		account = userEditText.getText().toString();
		password = pwdEditText.getText().toString();
		if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {

			Toast.makeText(this, R.string.please_complete_all_login_field,
					Toast.LENGTH_SHORT).show();
			loading.dismiss();
			return false;
		}		
		return true;
	}

	@Override
	public void requestLoginFinished(int resultCode, String errorMsg) {
		// TODO Auto-generated method stub

		if (Constants.kLOGIN_RESULT_FAILE == resultCode) { // 登录失败
			Log.e("LoginByHBActivity", "errorMsg : " + errorMsg);
			Toast.makeText(getApplicationContext(), errorMsg,
					Toast.LENGTH_SHORT).show();
		}

		if (Constants.kLOGIN_RESULT_SUCCESS == resultCode) { // 登录成功后，获取环信账号
			getHxAccount();
		}

		if (Constants.KGET_HUANXIN_ACCOUNT_SUCCESS == resultCode) {//获取到环信账号
			setResult(RESULT_OK);
			finish();
		}

		Intent loginResultMsg = new Intent(Constants.LOGIN_RESULT_ACTION);
		loginResultMsg.putExtra(Constants.kRESULT_CODE, resultCode);

		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
				.getInstance(this);
		localBroadcastManager.sendBroadcast(loginResultMsg);

		loading.dismiss();		
	}	
}
