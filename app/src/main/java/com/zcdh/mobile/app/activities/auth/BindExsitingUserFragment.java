package com.zcdh.mobile.app.activities.auth;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;
/**
 * 
 * @author yangjiannan
 * 绑定已有职场导航账号
 */
@Deprecated
@EFragment(R.layout.bind_existing)
public class BindExsitingUserFragment extends Fragment implements
RequestListener, LoginListener {

	@ViewById(R.id.account)
	EditText et_account;
	@ViewById(R.id.pwd)
	EditText et_pwd;
	@ViewById(R.id.bindBtn)
	Button bindBtn;

	// 用户信息
	private String account;
	private String pwd;
	// 第三方登录UID
	private String thirdpart_uid;
	private String login_type;

	private String kREQ_ID_BINDEXISTSACCOUNT;

	// TODO
	IRpcJobUservice userService;
	
	
	@AfterViews
	void bindViews(){
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		
		BindAccountActivity_ bindAccountActivity_= (BindAccountActivity_) getActivity();
		thirdpart_uid = bindAccountActivity_.third_open_id;
		login_type = bindAccountActivity_.login_type;

		if (Constants.LOGIN_TYPE_WEIBO.equals(login_type)) {
			bindBtn.setText(R.string.bind_sina_account);
		} else {
			bindBtn.setText(R.string.bind_qq_account);
		}
	}
	
	@Click(R.id.bindBtn)
	void onBind() {
		if (check_content()) {
			bind();
		}
	}

	@Background
	void bind() {
		// TODO
		//userService.bindExistsAccount(account, pwd, login_type, thirdpart_uid).identify(kREQ_ID_BINDEXISTSACCOUNT = RequestChannel.getChannelUniqueID(), this);
	}

	private boolean check_content() {
		account = et_account.getText().toString();
		pwd = et_pwd.getText().toString();

		if (StringUtils.isBlank(account) || StringUtils.isBlank(pwd)) {
			Toast.makeText(getActivity(), R.string.please_complete_all_info, Toast.LENGTH_SHORT).show();
			return false;
		}

		// validate phone_num & email
		if (!Pattern.compile(Constants.regex_email).matcher(account).matches() && !Pattern.compile(SharedPreferencesUtil.getValue(getActivity(), Constants.REGEX_PHONE_KEY, Constants.regex_phone)).matcher(account).matches()) {
			Toast.makeText(getActivity(), R.string.invalid_email_or_phone_format, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	
	@Override
	public void requestLoginFinished(int resultCode, String errorMsg) {
		if (resultCode == Constants.kLOGIN_RESULT_SUCCESS) {
			Intent itent = new Intent(Constants.LOGIN_RESULT_ACTION);
			LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(itent);
			getActivity().finish();
		}
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_BINDEXISTSACCOUNT)) {
			// result: 0成功，9 QQ_UID已绑定其他账号，10 微博UID已经绑定其他账号，11zcdh账号已经被绑定
			if (0 == (Integer) result) {
				Toast.makeText(getActivity(), R.string.bind_successfully, Toast.LENGTH_SHORT).show();

				if (Constants.LOGIN_TYPE_QQ.equals(login_type)) {
					LoginHelper.getInstance(getActivity(), this).findUserIdByQQ(thirdpart_uid);
				} else {
					LoginHelper.getInstance(getActivity(), this).findUserIdByWeiBo(thirdpart_uid);
				}

			}
			if (9 == (Integer) result) {
				Toast.makeText(getActivity(), R.string.qq_uid_has_bound_another_zcdh_account, Toast.LENGTH_SHORT).show();

			}
			if (10 == (Integer) result) {
				Toast.makeText(getActivity(), R.string.weibo_uid_has_bound_another_zcdh_account, Toast.LENGTH_SHORT).show();

			}
			if (11 == (Integer) result) {
				Toast.makeText(getActivity(), R.string.zcdh_uid_has_been_bound, Toast.LENGTH_SHORT).show();

			}

		}
		
	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		
	}
}
