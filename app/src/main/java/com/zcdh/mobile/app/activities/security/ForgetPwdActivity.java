package com.zcdh.mobile.app.activities.security;

import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.activities.BaseActivity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

/**
 * 忘记密码
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_register)
public class ForgetPwdActivity extends BaseActivity {

	
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
		if (second == 0) {
			getValidateCodeBtn.setEnabled(true);
			getValidateCodeBtn.setText("重新发送");
			getValidateCodeBtn.setBackgroundResource(R.drawable.btn_login);
		} else {

			getValidateCodeBtn.setEnabled(false);
			getValidateCodeBtn.setText("(" + second + ")");
			getValidateCodeBtn.setBackgroundResource(R.drawable.btn_register);
		}
	}
	
}
