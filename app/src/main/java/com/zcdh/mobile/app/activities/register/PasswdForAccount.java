package com.zcdh.mobile.app.activities.register;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.UserAccountManagerDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
import com.zcdh.mobile.app.activities.auth.LoginListener;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 注册，修改密码
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.passwd_for_register)
public class PasswdForAccount extends BaseActivity implements
		RequestListener, LoginListener {
	
	private static final  String TAG = PasswdForAccount.class.getSimpleName(); 

	private String kREQ_ID_register;
	private String kREQ_ID_bindUserMobileAndUpdatePwd;
	private String kREQ_ID_updatePWDByMobile;

	private IRpcJobUservice uservice;

	@Extra
	String phoneNo;

	@ViewById(R.id.pwdEditText)
	EditText pwdEditText;

	@ViewById(R.id.confirmPwdEditText)
	EditText confirmPwdEditText;
	
	private ProcessDialog processDialog;
	
	/**
	 * 是否从账号管理中绑定
	 */
	@Extra
	boolean isBindAccount;
	
	/**
	 * 注册
	 */
	@Extra
	boolean isRegister;
	
	/**
	 * 忘记密码
	 */
	@Extra
	boolean isForgetPwd;
	
	@Extra
	String verificationCode;
	

	public void onDestroy(){
		processDialog.cancel();
		super.onDestroy();
	}

	@AfterViews
	void bindViews() {
		String title = "设置密码";
		if(isForgetPwd){
			title = "重置密码";
		}
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), title);
		uservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		processDialog = new ProcessDialog(this);
	}

	/**
	 * 完成新用户注册
	 */
	@Click(R.id.registerBtn2)
	void onRegister() {
		String pwd1 = pwdEditText.getText().toString();
		String pwd2 = confirmPwdEditText.getText().toString();

		if (!StringUtils.isBlank(pwd1)) {

			if (!StringUtils.isBlank(pwd2)) {
				if (pwd1.equals(pwd2)) {
					if(pwd1.length()>=6){
						processDialog.show();
						if(isBindAccount){
							bindAccount();
						}
						if(isRegister){
							register();
						}
						if(isForgetPwd){
							updatePwd();
						}
					}else{
						Toast.makeText(this, "密码长度不能小于6位", Toast.LENGTH_SHORT)
						.show();
					}
					
				} else {
					Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 忘记密码，重置密码
	 * 0 修改密码成功, 4 账号不存在, -1 修改密码失败 ，34：手机短信验证码验证错误 ， 35 验证码过期
	 */
	private void updatePwd() {
		uservice.updatePWDByMobile(phoneNo, pwdEditText.getText().toString(), verificationCode)
		.identify(kREQ_ID_updatePWDByMobile=RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 新建账号(新注册用户 或 在账号管理中绑定账号)
	 * 
	 */
	@Background
	void register() {
		uservice.register(phoneNo, pwdEditText.getText().toString(), null)
				.identify(
						kREQ_ID_register = RequestChannel.getChannelUniqueID(),
						this);
	}
	
	/**
	 *  账号管理，绑定一个手机账号
	 *  	0 成功,-1 绑定失败,2 手机号码已经存在 
	 */
	@Background
	void bindAccount(){
		uservice.bindUserMobileAndUpdatePwd(getUserId(), phoneNo, pwdEditText.getText().toString())
		.identify(kREQ_ID_bindUserMobileAndUpdatePwd=RequestChannel.getChannelUniqueID(), this);
		
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		
		// 注册新用户
 		if (reqId.equals(kREQ_ID_register)) {
			if (result != null) {
				int success = (Integer) result;
				// 2 手机号码已经存在，0 注册成功
				switch (success) {
				case 0: //注册成功后，做登录
					
					LoginHelper.getInstance(this, this).doLoginZCDH(phoneNo, pwdEditText.getText().toString());
					
					break;
				case 2:
					Toast.makeText(this, R.string.regist_failed_phone_exists,
							Toast.LENGTH_SHORT).show();
					processDialog.dismiss();
					break;
				}
			}
		}
		
		// 绑定或更改手机号码
		if(reqId.equals(kREQ_ID_bindUserMobileAndUpdatePwd)){
			if(result !=null){
				int resultCode = (Integer) result;
				
				//	0 成功,-1 绑定失败,2 手机号码已经存在 
				
				switch (resultCode) {
				case -1:
					Toast.makeText(this, "绑定失败",
							Toast.LENGTH_SHORT).show();
					break;
				case 0: //注册成功后，做登录
					
					//LoginHelper.getInstance(this, this).doLoginZCDH(phoneNo, pwdEditText.getText().toString());
					UserAccountManagerDTO account = new UserAccountManagerDTO();
					account.setBindType("mobile");
					account.setBindValue(phoneNo);
					Intent data = new Intent();
					data.putExtra("account", account);
					setResult(RESULT_OK, data);
					finish();
					break;
				case 2:
					Toast.makeText(this, R.string.regist_failed_phone_exists,
							Toast.LENGTH_SHORT).show();
					processDialog.dismiss();
					break;
				}
			}
		}
		
		//忘记密码
		if(reqId.equals(kREQ_ID_updatePWDByMobile)){
			//0 修改密码成功, 4 账号不存在, -1 修改密码失败 ，34：手机短信验证码验证错误 ， 35 验证码过期
			if(result!=null){
				int resultCode = (Integer) result;
				if(resultCode==0){
					Toast.makeText(this, "重置密码成功",
							Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Toast.makeText(this, "重置密码失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	@Override
	public void onRequestFinished(String reqId) {
		processDialog.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 注册后，登录成功
	 */
	@Override
	public void requestLoginFinished(int resultCode, String errorMsg) {
		processDialog.dismiss();
		if(resultCode==Constants.kLOGIN_RESULT_SUCCESS){
			Intent loginResultMsg = new Intent(Constants.LOGIN_RESULT_ACTION);
			loginResultMsg.putExtra(Constants.kRESULT_CODE, resultCode);
			
			LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
			localBroadcastManager.sendBroadcast(loginResultMsg);
			finish();
		}else{
			Log.i(TAG, "注册后的登录失败:" + errorMsg);
		}
		
	}

}
