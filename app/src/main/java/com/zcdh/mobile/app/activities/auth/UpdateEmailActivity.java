/**
 * 
 * @author jeason, 2014-5-14 下午10:03:38
 */
package com.zcdh.mobile.app.activities.auth;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobUserInfoDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jeason, 2014-5-14 下午10:03:38
 * 更新email
 */
@EActivity(R.layout.update_email)
@OptionsMenu(R.menu.save2)
public class UpdateEmailActivity extends BaseActivity implements RequestListener {
	
	public static final int KREQUEST_UPDATE_EMAIL = 2028;

	IRpcJobUservice userService;

	@ViewById(R.id.current_pass)
	EditText current_pass;
	@ViewById(R.id.email1)
	EditText email1;
	// @ViewById(R.id.email2)
	// EditText email2;
	@ViewById(R.id.tv_current_email)
	TextView tv_current_email;

	String current_pass_str;
	String email1_str;
	// String email2_str;

	LoadingIndicator loading;

	public String kREQ_ID_FINDJOBUSERINFODTO;
	public String kREQ_ID_UPDATEUSEREMAIL;
	
	@Extra
	String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		loading = new LoadingIndicator(this);
		doGetUserInfo();
	}

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.update_email));
	}

	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
	}

	/**
	 * 
	 * @author jeason, 2014-5-14 下午11:10:32
	 */
	@Background
	void doGetUserInfo() {
		userService.findJobUserInfoDTO(getUserId()).identify(kREQ_ID_FINDJOBUSERINFODTO = RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * @description:用户信息检查并赋值
	 * @return 内容输入是否正确
	 * @author jeason, 2014-4-3 上午11:19:52
	 */
	private boolean checkContent() {
		current_pass_str = current_pass.getText().toString();
		email1_str = email1.getText().toString();
		// email2_str = email2.getText().toString();
		if (StringUtils.isBlank(current_pass_str) || StringUtils.isBlank(email1_str) ){
			Toast.makeText(this, R.string.please_complete_all_field, Toast.LENGTH_SHORT).show();
			return false;
		}

		Pattern regex = Pattern.compile(Constants.regex_email);
		Matcher matcher = regex.matcher(email1_str);
		if (!matcher.matches()) {
			Toast.makeText(this, R.string.invalid_email_format, Toast.LENGTH_SHORT).show();
			return false;
		}

		// see if pwd1 equals pwd2
		// if (!email1_str.equals(email2_str)) {
		//
		// Toast.makeText(this, R.string.not_the_same_email,
		// Toast.LENGTH_SHORT).show();
		// return false;
		// }

		return true;
	}

	@OptionsItem(R.id.action_save)
	void onUpdate() {
		if (checkContent()) {
			loading.show();
			doUpdateEmail();
		}
	}

	@Background
	void doUpdateEmail() {
		userService.updateUserEmail(getUserId(), current_pass_str, email1_str).identify(kREQ_ID_UPDATEUSEREMAIL = RequestChannel.getChannelUniqueID(), this);
		;
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

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		loading.dismiss();
		if (result != null) {
			if (reqId.equals(kREQ_ID_FINDJOBUSERINFODTO)) {

				JobUserInfoDTO user = (JobUserInfoDTO) result;
				if(!StringUtils.isBlank(user.getEmail())){
					tv_current_email.setText(String.format("%s", user.getEmail()));
				} else{
					tv_current_email.setText(String.format("当前未设置邮箱"));
				}
			}

			if (reqId.equals(kREQ_ID_UPDATEUSEREMAIL)) {
				int act_result = (Integer) result;
				if (act_result == 0) {
					Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra("email", email1_str);
					setResult(RESULT_OK, data);
					finish();
					return;
				}
				if (act_result == 18) {
					Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
					return;
				}
				if (act_result == 4) {
					Toast.makeText(this, getResources().getString(R.string.account_not_exists), Toast.LENGTH_SHORT).show();
					return;
				}
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
