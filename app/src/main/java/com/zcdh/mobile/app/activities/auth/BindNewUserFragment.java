package com.zcdh.mobile.app.activities.auth;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author yangjiannan 绑定并新建账号
 * 
 */
@Deprecated
@EFragment(R.layout.regist)
public class BindNewUserFragment extends Fragment implements
		RequestListener, LoginListener {

	// 关于用户的联网操作
	private IRpcJobUservice userService;

	private String kREQ_ID_bindNewAccount = "";

	// FinalDb dbTool;

	// bind views
	@ViewById(R.id.phone)
	EditText et_phone;
	@ViewById(R.id.email)
	EditText et_email;
	@ViewById(R.id.pwd)
	EditText et_pwd;
	@ViewById(R.id.pwd2)
	EditText et_pwd_confirm;

	@ViewById(R.id.registBtn)
	Button registBtn;

	// 用户信息
	private String phone;
	private String email;
	private String pwd;
	private String pwd_confirm;

	// request id
	public String kREQ_ID_REGISTER;
	public String kREQ_ID_FINDUSERID;

	/**
	 * 第三方open id
	 */
	private  String thirdpart_uid = "";

	/**
	 * 登录方式
	 */
	private String login_type = "";

	@AfterViews
	void bindViews() {
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		
		BindAccountActivity_ bindAccountActivity_= (BindAccountActivity_) getActivity();
		thirdpart_uid = bindAccountActivity_.third_open_id;
		login_type = bindAccountActivity_.login_type;

		registBtn.setText("绑定账号");

	}

	/**
	 * @description:用户信息检查并赋值
	 * @return 内容输入是否正确
	 * @author jeason, 2014-4-3 上午11:19:52
	 */
	private boolean check_content() {
		phone = et_phone.getText().toString();
		email = et_email.getText().toString();
		pwd = et_pwd.getText().toString();
		pwd_confirm = et_pwd_confirm.getText().toString();
		if (StringUtils.isBlank(phone) || StringUtils.isBlank(email)
				|| StringUtils.isBlank(pwd) || StringUtils.isBlank(pwd_confirm)) {
			Toast.makeText(getActivity(), R.string.please_complete_all_info,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// validate phone_num & email
		Pattern regex = Pattern.compile(SharedPreferencesUtil.getValue(getActivity(), Constants.REGEX_PHONE_KEY, Constants.regex_phone));
		Matcher matcher = regex.matcher(phone);
		if (!matcher.matches()) {
			Toast.makeText(getActivity(), R.string.invalid_phone_format,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		regex = Pattern.compile(Constants.regex_email);
		matcher = regex.matcher(email);
		if (!matcher.matches()) {
			Toast.makeText(getActivity(), R.string.invalid_email_format,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// see if pwd1 equals pwd2
		if (!pwd.equals(pwd_confirm)) {

			Toast.makeText(getActivity(), R.string.not_the_same_password,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Click(R.id.registBtn)
	void onBindNewUser() {
		if (check_content()) { //
			userService
					.bindNewAccount(phone, email, pwd, login_type,
							thirdpart_uid)
					.identify(
							kREQ_ID_bindNewAccount = RequestChannel
									.getChannelUniqueID(),
							this);
			;
		}
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_bindNewAccount)) {
			int actual_result = (Integer) result;

			// 返回结果result： 0为成功，8为账号已经存在
			switch (actual_result) {
			case 0:
				// 提示绑定成功 并请求zcdh_uid 进行登录
				Toast.makeText(getActivity(), R.string.bind_successfully, Toast.LENGTH_SHORT).show();

				if (Constants.LOGIN_TYPE_QQ.equals(login_type)) {
					LoginHelper.getInstance(getActivity(), this).findUserIdByQQ(thirdpart_uid);
				} else {
					LoginHelper.getInstance(getActivity(), this).findUserIdByWeiBo(thirdpart_uid);
				}
				break;
			case 3:
			case 2:
				Toast.makeText(getActivity(), "账号已存在", Toast.LENGTH_SHORT).show();
				break;
			case 8:
				// 提示账号已经存在
				Toast.makeText(getActivity(), R.string.bind_failed_account_exists, Toast.LENGTH_SHORT).show();
				break;
			case 9:
				Toast.makeText(getActivity(), R.string.qq_uid_has_bound_another_zcdh_account, Toast.LENGTH_SHORT).show();
				break;
			case 10:
				Toast.makeText(getActivity(), R.string.weibo_uid_has_bound_another_zcdh_account, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

			return;
		}
	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestLoginFinished(int resultCode, String errorMsg) {
		if (resultCode == Constants.kLOGIN_RESULT_SUCCESS) {
			Intent itent = new Intent(Constants.LOGIN_RESULT_ACTION);
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(itent);
			getActivity().finish();
		}
	}
}
