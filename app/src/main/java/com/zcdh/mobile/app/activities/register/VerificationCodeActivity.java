package com.zcdh.mobile.app.activities.register;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.UserAccountManagerDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 
 * @author yangjiannan 验证码验证手机号码 使用场景： 1）注册新用户，参数 isRegisterNewUser 2)在账号管理中，
 *         绑定手机号码 , 参数 isBindMobile 3）在账号管理中，更改手机号码 ,参数 isModifyMobile 4) 忘记密码
 *         ,参数 isForgetPwd
 */
@EActivity(R.layout.activity_register)
public class VerificationCodeActivity extends BaseActivity implements
		RequestListener, Callback {

	private static final String TAG = VerificationCodeActivity.class
			.getSimpleName();
	private String kREQ_ID_sendSMSVerificationCode;
	private String kREQ_ID_validSMSVerificationCode;
	private String kREQ_ID_updateBindUserMobile;
	private String kREQ_ID_isUserShareSDKMobileSendSM;

	private IRpcJobUservice uservice;

	/**
	 * 手机号码
	 */
	@ViewById(R.id.phoneNumEditText)
	EditText phoneNumEditText;

	/**
	 * 验证码
	 */
	@ViewById(R.id.validateCodeEditText)
	EditText validateCodeEditText;

	/**
	 * 获取验证码按钮，（倒计时按钮）
	 */
	@ViewById(R.id.getValidateCodeBtn)
	Button getValidateCodeBtn;

	/**
	 * 
	 */
	@ViewById(R.id.submitBtn)
	Button submitBtn;

	private ProcessDialog processDialog;

	/**
	 * 是否使用免费得短信验证码 （mob)
	 */
	private boolean isSendSMSCodeUseMob = true;

	/**
	 * 注册新用户
	 */
	@Extra
	boolean isRegisterNewUser;

	/**
	 * 绑定手机号码
	 */
	@Extra
	boolean isBindMobile;

	/**
	 * 更改手机号码
	 */
	@Extra
	boolean isModifyMobileNo;

	/**
	 * 找回密码
	 */
	@Extra
	boolean isForgetPwd;

	// 判断是否收到Mob 发送的短信
	private boolean recivedMobSMS;
//	private SMSBroadcastReceiver mSMSBroadcastReceiver;

	@AfterViews
	void bindViews() {

		String title = "";
		if (isBindMobile) {

			title = "绑定手机";
		}
		if (isModifyMobileNo) {
			title = "更改手机";
			submitBtn.setText("提 交");

		}
		if (isRegisterNewUser) {
			title = "用户注册";
		}
		if (isForgetPwd) {
			title = "找回密码";
		}

		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), title);

		uservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

		processDialog = new ProcessDialog(this);

		// 初始化短信验证sdk
		SMSSDK.initSDK(this, Constants.SMS_APP_KEY, Constants.SMS_APP_SCRETE);
		final Handler handler = new Handler(this);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册短信验证sdk回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		// 实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter(
				Constants.ACTION_SMS_RECEIVED);
		intentFilter.setPriority(Integer.MAX_VALUE);
	}

	public void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

	/**
	 * 绑定账号
	 */
	@OnActivityResult(Constants.kREQUEST_BIND_MOBILE)
	void onResultBindAccount(int resultCode, Intent data) {
		Log.i(TAG, "onResult :" + resultCode);
		setResult(resultCode, data);
		finish();
	}

	/**
	 * 获取手机短信验证码
	 */
	@Click(R.id.getValidateCodeBtn)
	void onGetVerificationCode() {

		String mobilePhoneNo = phoneNumEditText.getText().toString();
		if (!StringUtils.isBlank(mobilePhoneNo)) {
			if (Pattern.compile(SharedPreferencesUtil.getValue(this, Constants.REGEX_PHONE_KEY, Constants.regex_phone)).matcher(mobilePhoneNo)
					.matches()) {
				isUseMobSMSForMobileNo(mobilePhoneNo);
			} else {
				Toast.makeText(getApplicationContext(), "请输入正确地手机号码",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 注册下一步 0 表示验证成功，33，表示手机号码不存在，34：手机短信验证码验证错误
	 */
	@Click(R.id.submitBtn)
	void onSubmit() {

		if (!recivedMobSMS) {
			Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
			return;
		}

		String code = validateCodeEditText.getText().toString();
		String phoneNo = phoneNumEditText.getText().toString();

		boolean validate = true;
		if (StringUtils.isBlank(code)) {
			Toast.makeText(this, "请填写验证码", Toast.LENGTH_SHORT).show();
			validate = false;
		}
		if (StringUtils.isBlank(phoneNo)) {
			Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
			validate = false;
		}
		if (!Pattern.compile(SharedPreferencesUtil.getValue(this, Constants.REGEX_PHONE_KEY, Constants.regex_phone)).matcher(phoneNo).matches()) {
			Toast.makeText(this, "请输入正确地手机号码", Toast.LENGTH_SHORT).show();
			validate = false;
		}
		if (validate) {
			processDialog.show("正在验证...");
			if (isSendSMSCodeUseMob) {
				// validMobile(phoneNo);
				SMSSDK.submitVerificationCode("86", phoneNo, code);
			} else {
				if (isModifyMobileNo) {
					// 修改手机号码
					modifiyMobleNo(phoneNo, code);
				} else {
					validSMSCode(phoneNo, code);
				}
			}
		}

	}

	/**
	 * 请求服务获取手机短信验证码
	 * 
	 * @param phoneNo
	 *            0，表示发送成功，-1，发送失败，2 手机号码已经存在
	 */
	@Background
	void getVerificationCode(String phoneNo) {

		uservice.sendSMSVerificationCodeInRegister(phoneNo)
				.identify(
						kREQ_ID_sendSMSVerificationCode = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 返回值1 表示验证成功，2，表示手机号码已存在，3：验证码错误
	 * 
	 * @param phoneNo
	 * @param verificationCode
	 */
	@Background
	void validSMSCode(String phoneNo, String verificationCode) {
		uservice.validSMSVerificationCodeInRegister(phoneNo, verificationCode)
				.identify(
						kREQ_ID_validSMSVerificationCode = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 根据 从后台服务获取是否调用免费得验证码接口 39 使用shareSDKMobile 发送短信 并且手机不存在, 40 使用服务端发送短信
	 * 并且手机号码不存在， <br>
	 * 41 shareSDKMobile 并且手机号码已经存在, 42 使用服务端发送短信 并且手机号码已经存在
	 */
	@Background
	void isUseMobSMSForMobileNo(String phone) {
		uservice.isUserShareSDKMobileSendSM(phone)
				.identify(
						kREQ_ID_isUserShareSDKMobileSendSM = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 更改手机
	 * 
	 * @param code
	 * @param phoneNo
	 *            0 成功,-1 绑定失败,2 手机号码已经存在 ,34：手机短信验证码验证错误 ， 35 验证码过期
	 */
	@Background
	void modifiyMobleNo(String phoneNo, String code) {
		uservice.updateBindUserMobile(getUserId(), phoneNo, code)
				.identify(
						kREQ_ID_updateBindUserMobile = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 倒计时
	 */
	private void countDownStart() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				int countDownSecond = 60; // 60秒倒计时
				while (countDownSecond >= 0) {
					try {
						Log.i("countDown", countDownSecond + "");
						updateCountDownStatus(countDownSecond);
						Thread.sleep(1000);
						countDownSecond--;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		thread.start();

	}

	/**
	 * 更新倒计时按钮
	 * 
	 * @param second
	 */
	@UiThread
	void updateCountDownStatus(int second) {
		validateCodeEditText.setEnabled(true);
		if (second == 0) {
			getValidateCodeBtn.setEnabled(true);
			getValidateCodeBtn.setText("重新发送");
			getValidateCodeBtn.setBackgroundResource(R.drawable.tags);
		} else {

			getValidateCodeBtn.setEnabled(false);
			getValidateCodeBtn.setText("(" + second + ")");
			getValidateCodeBtn.setBackgroundResource(R.drawable.tags);
		}
	}

	public boolean handleMessage(Message msg) {

		processDialog.dismiss();

		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;

		// 短信验证码sdk注册成功
		if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
			Log.i(TAG, "短信验证结果： " + data);
			if (result == SMSSDK.RESULT_COMPLETE) {
				HashMap<String, String> phone = (HashMap<String, String>) data;
				Log.i(TAG, "短信验证成功： " + phone);
				// validMobile(phone);

				if (isModifyMobileNo || isBindMobile) {
					modifiyMobleNo(phone.get("phone"), validateCodeEditText
							.getText().toString());
				}
				if (isRegisterNewUser) {
					PasswdForAccount_.intent(this).phoneNo(phone.get("phone"))
							.isRegister(isRegisterNewUser)
							.startForResult(Constants.kREQUEST_BIND_MOBILE);
				}
				if (isForgetPwd) {
					PasswdForAccount_.intent(this).phoneNo(phone.get("phone"))
							.isForgetPwd(isForgetPwd).start();
					finish();
				}

			} else {
				Log.e(TAG, "短信验证失败");
				Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
			}

		} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
			// 获取验证码成功后的执行动作
			if (result == SMSSDK.RESULT_COMPLETE) {
				Log.i(TAG, "获取短信验证成功");
				recivedMobSMS = true;
				countDownStart();
			} else {
				Toast.makeText(getApplicationContext(), "获取短信验证失败",
						Toast.LENGTH_SHORT).show();
				Log.e(TAG, "获取短信验证失败");
			}
		}

		return false;
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		if (reqId.equals(kREQ_ID_isUserShareSDKMobileSendSM)) {
			if (result != null) {
				// 39 使用shareSDKMobile 发送短信 并且手机不存在, 40 使用服务端发送短信 并且手机号码不存在，
				// 41 shareSDKMobile 并且手机号码已经存在, 42 使用服务端发送短信 并且手机号码已经存在

				int resultCode = (Integer) result;
				boolean existMobile = false;

				if (resultCode == 39) {
					isSendSMSCodeUseMob = true;
					existMobile = false;
				}
				if (resultCode == 40) {
					isSendSMSCodeUseMob = false;
					validateCodeEditText.setEnabled(true);
					existMobile = false;
				}
				if (resultCode == 41) {
					isSendSMSCodeUseMob = true;
					existMobile = true;
				}
				if (resultCode == 42) {
					isSendSMSCodeUseMob = false;
					existMobile = true;
				}

				if (isForgetPwd) {
					if (existMobile) {
						if (isSendSMSCodeUseMob) {
							SMSSDK.getVerificationCode("86", phoneNumEditText
									.getText().toString());
						} else {
							getVerificationCode(phoneNumEditText.getText()
									.toString());
						}
					} else {
						Toast.makeText(this, "该手机号码未绑定", Toast.LENGTH_SHORT)
								.show();
					}
				} else {

					if (!existMobile) {
						if (isSendSMSCodeUseMob) {
							SMSSDK.getVerificationCode("86", phoneNumEditText
									.getText().toString());
						} else {
							getVerificationCode(phoneNumEditText.getText()
									.toString());
						}
					} else {
						Toast.makeText(this, "此号码已被注册", Toast.LENGTH_SHORT)
								.show();
					}

				}

			}
		}

		if (reqId.equals(kREQ_ID_sendSMSVerificationCode)) { // 获取手机短信验证吗
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					// countDownStart();
					recivedMobSMS = true;
				} else if (success == -1) {
					Toast.makeText(this, "发送失败，请重新获取", Toast.LENGTH_SHORT)
							.show();
				} else if (success == 2) {
					Toast.makeText(this, "此号码已被注册", Toast.LENGTH_SHORT).show();
				}
			}
		}

		if (reqId.equals(kREQ_ID_validSMSVerificationCode)) { // 注册下一步,
																// 用验证码验证手机号码 0
																// 表示验证成功，33，表示手机号码不存在，34：手机短信验证码验证错误
			if (result != null) {
				int resultCode = (Integer) result;
				if (resultCode == 0) {

					if (isModifyMobileNo || isBindMobile) {
						modifiyMobleNo(phoneNumEditText.getText().toString(),
								validateCodeEditText.getText().toString());
					}
					if (isRegisterNewUser) {
						PasswdForAccount_.intent(this)
								.phoneNo(phoneNumEditText.getText().toString())
								.isRegister(isRegisterNewUser)
								.startForResult(Constants.kREQUEST_BIND_MOBILE);
					}
					if (isForgetPwd) {
						PasswdForAccount_
								.intent(this)
								.phoneNo(phoneNumEditText.getText().toString())
								.verificationCode(
										validateCodeEditText.getText()
												.toString())
								.isRegister(isForgetPwd).start();
						finish();
					}
				}
				// if(resultCode==33){
				// Toast.makeText(this, "此号码已被注册过", Toast.LENGTH_SHORT).show();
				// }
				if (resultCode == 34) {
					Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
				}
			}
		}

		/**
		 * 
		 */
		if (reqId.equals(kREQ_ID_updateBindUserMobile)) {
			if (result != null) {
				// 0 成功,-1 绑定失败,2 手机号码已经存在 ,34：手机短信验证码验证错误 ， 35 验证码过期
				int resultCode = (Integer) result;
				if (resultCode == 0) {
					UserAccountManagerDTO account = new UserAccountManagerDTO();
					account.setBindType("mobile");
					account.setBindValue(phoneNumEditText.getText().toString());
					Intent data = new Intent();
					data.putExtra("account", account);
					setResult(RESULT_OK, data);
					finish();
				}
				if (resultCode == 34) {
					Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
				}
				if (resultCode == -1) {
					Toast.makeText(this, "绑定失败，请稍后再试", Toast.LENGTH_SHORT)
							.show();
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

	}

}
