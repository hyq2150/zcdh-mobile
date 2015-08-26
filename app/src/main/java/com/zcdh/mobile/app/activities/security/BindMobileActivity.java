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
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * 绑定手机号码
 * @author yangjiannan
 *
 */
@EActivity(R.layout.activity_bind_mobile)
public class BindMobileActivity extends BaseActivity implements RequestListener{

	
	String kREQ_ID_sendSMSVerificationCode;
	String kREQ_ID_validSMSVerificationCode;
	String kREQ_ID_bindAccount;
	
	IRpcJobUservice uservice;
	
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
	
	ProcessDialog processDialog;
	
	@Extra
	boolean update;
	
	@AfterViews
	void bindViews(){
		if(update){
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), "更改手机");
		}else{
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), "绑定手机");
		}
		uservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		processDialog = new ProcessDialog(this);
	}
	
	/**
	 * 获取手机短信验证码
	 */
	@Click(R.id.getValidateCodeBtn)
	void onGetVerificationCode(){
		
		String mobilePhoneNo =  phoneNumEditText.getText().toString();
		if(!StringUtils.isBlank(mobilePhoneNo)){
			if(Pattern.compile(SharedPreferencesUtil.getValue(this, Constants.REGEX_PHONE_KEY, Constants.regex_phone)).matcher(mobilePhoneNo)
					.matches()){
				getVerificationCode(mobilePhoneNo);
				//countDownStart();
			}else{
				Toast.makeText(getApplicationContext(), "请输入正确地手机号码", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 绑定
	 * 0 表示验证成功，33，表示手机号码不存在，34：手机短信验证码验证错误 
	 */
	@Click(R.id.registerBtn1)
	void onBindMobile(){
		
		String code = validateCodeEditText.getText().toString();
		String phoneNo = phoneNumEditText.getText().toString();
		boolean validate = true;
		if(StringUtils.isBlank(code)){
			Toast.makeText(this, "请填写验证码", Toast.LENGTH_SHORT).show();
			validate = false;
		}
		if(StringUtils.isBlank(phoneNo)){
			Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show();
			validate = false;
		}
		if(!Pattern.compile(SharedPreferencesUtil.getValue(this, Constants.REGEX_PHONE_KEY, Constants.regex_phone)).matcher(phoneNo)
				.matches()){
			Toast.makeText(this, "请输入正确地手机号码", Toast.LENGTH_SHORT).show();
			validate = false;
		}
		if(validate){
			processDialog.show("正在验证...");
			bindMobile(phoneNo);
		}
		 
	}
	
	/**
	 * 请求服务获取手机短信验证码
	 * @param phoneNo
	 * 0，表示发送成功，-1，发送失败，2 手机号码已经存在 
	 */
	@Background
	void getVerificationCode(String phoneNo){
		
		uservice.sendSMSVerificationCodeInRegister(phoneNo)
		.identify(kREQ_ID_sendSMSVerificationCode=RequestChannel.getChannelUniqueID(), this);
	}
	
	
	 /**
	  *  返回值1 表示验证成功，2，表示手机号码已存在，3：验证码错误
	  * @param phoneNo
	  * @param verificationCode
	  */
	@Background
	void valiateSMSCodeForBindMobile(String phoneNo, String verificationCode){
		//1. 先由验证码验证手机号码
		uservice.validSMSVerificationCodeInRegister(phoneNo, verificationCode)
		.identify(kREQ_ID_validSMSVerificationCode=RequestChannel.getChannelUniqueID(), this);
		
	}
	
	/**
	 * 绑定手机账号  0 成功,-1 绑定失败,2 手机号码已经存在,3 邮箱已经存在,9 qq 已经绑定， 10 微博已经绑定 
	 */
	@Background
	void bindMobile(String phone){
		uservice.bindAccount(getUserId(), "mobile" , phone)
		.identify(kREQ_ID_bindAccount=RequestChannel.getChannelUniqueID(), this);
	}
	
	/**
	 * 倒计时
	 */
	private void countDownStart(){
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int countDownSecond = 60; //60秒倒计时
				while (countDownSecond>=0) {
					try {
						Log.i("countDown", countDownSecond +"");
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
	 * @param second
	 */
	@UiThread
	void updateCountDownStatus(int second){
		if(second==0){
			getValidateCodeBtn.setEnabled(true);
			getValidateCodeBtn.setText("重新发送");
			getValidateCodeBtn.setBackgroundResource(R.drawable.btn_login);
		}else{
			getValidateCodeBtn.setEnabled(false);
			getValidateCodeBtn.setText(second +"");
			getValidateCodeBtn.setBackgroundResource(R.drawable.btn_register);
		}
	}
	
	

	@Override
	public void onRequestStart(String reqId) {
		
	}

	
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if(reqId.equals(kREQ_ID_sendSMSVerificationCode)){ //获取手机短信验证吗
			if(result!=null){
				int success = (Integer) result;
				if(success==0){
					countDownStart();
					
				}else if(success==-1){
					Toast.makeText(this, "发送失败，请重新获取", Toast.LENGTH_SHORT).show();
				}else if(success==2){
					Toast.makeText(this, "此号码已被绑定", Toast.LENGTH_SHORT).show();
				}
			}
		}
		if(reqId.equals(kREQ_ID_validSMSVerificationCode)){ // 注册下一步, 用验证码验证手机号码 0 表示验证成功，33，表示手机号码不存在，34：手机短信验证码验证错误 
			if(result!=null){
				int resultCode = (Integer) result;
				if(resultCode==0){
					//绑定成功，返回绑定的账号信息
					bindMobile(phoneNumEditText.getText().toString());
				}
				if(resultCode==33 || resultCode==34){
					Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
		if(reqId.equals(kREQ_ID_bindAccount)){
			if(result!=null){
				int resultCode = (Integer) result;
				//0 成功,-1 绑定失败,2 手机号码已经存在,3 邮箱已经存在,9 qq 已经绑定， 10 微博已经绑定 
				if(resultCode==0){
					Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
					UserAccountManagerDTO accountManagerDTO = new UserAccountManagerDTO();
					accountManagerDTO.setBindType("mobile");
					accountManagerDTO.setBindValue(phoneNumEditText.getText().toString());
					Intent data = new Intent();
					data.putExtra("account", accountManagerDTO);
					setResult(RESULT_OK, data);
					finish();
				}
				if(resultCode==-1){
					Toast.makeText(this, "绑定手机失败", Toast.LENGTH_SHORT).show();
				}
				
				if(resultCode==2){
					Toast.makeText(this, "该号码已被绑定", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		if(reqId.equals(kREQ_ID_bindAccount) 
				&& reqId.equals(kREQ_ID_validSMSVerificationCode)){
			processDialog.dismiss();
		}
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		processDialog.dismiss();
	}
}
