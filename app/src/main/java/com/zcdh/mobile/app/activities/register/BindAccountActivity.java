package com.zcdh.mobile.app.activities.register;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.ImgAttachDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
import com.zcdh.mobile.app.activities.auth.LoginListener;
import com.zcdh.mobile.app.activities.security.ThirdPartAuthHelper;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import net.tsz.afinal.bitmap.download.SimpleDownloader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import static com.mob.tools.utils.R.getStringRes;

//import com.zcdh.mobile.app.activities.security.weibo.openapi.User;

/**
 * 
 * @author yangjiannan 绑定
 * 
 */
@EActivity(R.layout.activity_bind_for_new_user)
public class BindAccountActivity extends BaseActivity implements
		RequestListener, LoginListener, Callback {

	private static final String TAG = BindAccountActivity.class.getSimpleName();

	private String kREQ_ID_bindExistsAccount;
	private String kREQ_ID_registerByOpenId;

	private IRpcJobUservice uservice;

	/**
	 * 第三方账号认证成功后，返回的open_id
	 */
	@Extra
	public String third_open_id;

	/**
	 * 登录方式 (如： weibo, QQ ...)
	 */
	@Extra
	public String login_type;

	/**
	 * 手机号码
	 */
	@ViewById(R.id.userEditText)
	EditText userEditText;

	/**
	 * 密码
	 */
	@ViewById(R.id.pwdEditText)
	EditText pwdEditText;

	private String account;
	private String password;

	private ProcessDialog processDialog;
	private boolean isBindAccount;

	/**
	 * 第三方调用，获取头像
	 */
	private ThirdPartAuthHelper thirdPartAuthHelper;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void onDestroy() {
		processDialog.cancel();
		super.onDestroy();
	}

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitleAndIcon(
				getApplicationContext(), getSupportActionBar(), "绑定账号",
				R.drawable.ic_launcher);
		uservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		processDialog = new ProcessDialog(this);
		thirdPartAuthHelper = new ThirdPartAuthHelper(this);
	}

	/**
	 * @description:用户信息检查并赋值
	 * @return 内容输入是否正确
	 * @author jeason, 2014-4-3 上午11:19:52
	 */
	private boolean check_content() {
		account = userEditText.getText().toString();
		password = pwdEditText.getText().toString();
		if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {

			Toast.makeText(this, R.string.please_complete_all_login_field,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		account = account.trim();
		// validate phone_num & email
		if (!Pattern.compile(Constants.regex_email).matcher(account).matches()
				&& !Pattern.compile(SharedPreferencesUtil.getValue(this, Constants.REGEX_PHONE_KEY, Constants.regex_phone)).matcher(account)
						.matches()) {
			Toast.makeText(this, R.string.invalid_email_or_phone_format,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 通过新浪微博无绑定账号直接注册 0：注册成功，10：该微博号已经绑定
	 */
	@Background
	void registerByWeibo(String third_account_head_url) {
		// TODO
		uservice.registerByWeibo(third_open_id,
				loadImage(third_account_head_url)).identify(
				kREQ_ID_registerByOpenId = RequestChannel.getChannelUniqueID(),
				this);
	}

	/**
	 * 发送手机验证码 mobilePhoneNo 手机号码 0：注册成功，9：该QQ号已经绑定
	 */
	@Background
	void registerByQQ(String third_account_head_url) {
		// TODO
		uservice.registerByQQ(third_open_id, loadImage(third_account_head_url))
				.identify(
						kREQ_ID_registerByOpenId = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Background
	void registerByWechat(String third_account_head_url) {
		uservice.registerByWeChat(third_open_id,
				loadImage(third_account_head_url)).identify(
				kREQ_ID_registerByOpenId = RequestChannel.getChannelUniqueID(),
				this);
	}

	/**
	 * 
	 * 绑定账号
	 */
	@Background
	void bindAccount(String third_account_head_url) {
		uservice.bindExistsAccount(account, password, login_type,
				third_open_id, loadImage(third_account_head_url))
				.identify(
						kREQ_ID_bindExistsAccount = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 将第三方账号,绑定职场导航
	 */
	@Click(R.id.bindAccountBtn)
	void onBindAccount() {
		if (check_content()) {
			processDialog.show("创建账号...");
			createAccount(true);
		}
	}

	/**
	 * 跳过，不绑定, 直接用第三方认证的注册并创建账号
	 */
	@Click(R.id.skipedBtn)
	void onSkipBindAndRegister() {
		processDialog.show("创建账号...");
		Log.i(TAG, "open id:" + third_open_id);

		createAccount(false);
	}

	/**
	 * 获取第三方头像，再创建账号
	 * 
	 * @param isBindAccount
	 */

	void createAccount(final boolean isBindAccount /* 是否属于绑定账号 */) {
		this.isBindAccount = isBindAccount;
		if (Constants.LOGIN_TYPE_QQ.equals(login_type)) {
			thirdPartAuthHelper.getUserInfoInQQ(new IUiListener() {

				@Override
				public void onError(UiError arg0) {
					if (isBindAccount) {
						bindAccount(null);
					} else {
						registerByQQ(null);
					}
				}

				@Override
				public void onComplete(Object response) {
					JSONObject json = (JSONObject) response;
					Log.i(TAG, response + "");
					if (json.has("figureurl_qq_2")) {
						try {
							// 获取第三方账户头像
							String url = json.getString("figureurl_qq_2");

							// 同时用获取的头像，创建账号
							if (isBindAccount) {
								bindAccount(url);
							} else {
								registerByQQ(url);
							}
							Log.i(TAG, url);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				public void onCancel() {
					if (isBindAccount) {
						bindAccount(null);
					} else {
						registerByQQ(null);
					}
				}
			});

		}
		if (Constants.LOGIN_TYPE_WECHAT.equals(login_type)) {
			String head = SharedPreferencesUtil.getValue(this,
					Constants.HEAD_IMG_URL, "");
			if (isBindAccount) {
				bindAccount(head);
			} else {
				registerByWechat(head);
			}
		}
		if (Constants.LOGIN_TYPE_WEIBO.equals(login_type)) {
			thirdPartAuthHelper
					.getUserInfoInWeibo(new com.sina.weibo.sdk.net.RequestListener() {

						@Override
						public void onWeiboException(WeiboException exc) {
							Log.e(TAG, exc.getMessage());
							if (isBindAccount) {
								bindAccount(null);
							} else {
								registerByWeibo(null);
							}
						}

						@Override
						public void onComplete(String response) {
							if (!TextUtils.isEmpty(response)) {
								User user = User.parse(response);
								Log.i(TAG, "微博头像 ：" + user.profile_image_url);

								// 同时用获取的头像，创建账号
								if (isBindAccount) {
									bindAccount(user.profile_image_url);
								} else {
									registerByWeibo(user.profile_image_url);
								}
							}
						}
					});
		}

	}

	// 下载第三方账户头像
	private ImgAttachDTO loadImage(final String url) {
		ImgAttachDTO headImg = null;
		if (!StringUtils.isBlank(url)) {
			byte[] fileBytes = new SimpleDownloader().download(url);
			if (fileBytes != null && fileBytes.length > 0) {
				headImg = new ImgAttachDTO();
				headImg.setFileSize((long)fileBytes.length);
				headImg.setFileBytes(fileBytes);
				headImg.setFileExtension("png");
			}
		}
		return headImg;
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_bindExistsAccount)) {
			// result: 0成功，9 QQ_UID已绑定其他账号，10 微博UID已经绑定其他账号，11zcdh账号已经被绑定
			switch ((Integer) result) {
			case 0:
				Toast.makeText(this, R.string.bind_successfully,
						Toast.LENGTH_SHORT).show();
				if (Constants.LOGIN_TYPE_QQ.equals(login_type)) {
					LoginHelper.getInstance(this, this)
							.doLoginQQ(third_open_id);
				} else if (Constants.LOGIN_TYPE_WEIBO.equals(login_type)) {
					LoginHelper.getInstance(this, this).doLoginSinaWeibo(
							third_open_id);
				} else if (Constants.LOGIN_TYPE_WECHAT.equals(login_type)) {
					LoginHelper.getInstance(this, this).doLoginWeChat(
							third_open_id);
				}
				break;
			case 4:
				Toast.makeText(this,
						getResources().getString(R.string.account_not_exists),
						Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(this, R.string.wrong_password,
						Toast.LENGTH_SHORT).show();
				break;
			case 9:
				Toast.makeText(this,
						R.string.qq_uid_has_bound_another_zcdh_account,
						Toast.LENGTH_SHORT).show();
				break;
			case 10:
				Toast.makeText(this,
						R.string.weibo_uid_has_bound_another_zcdh_account,
						Toast.LENGTH_SHORT).show();
				break;
			case 11:
				Toast.makeText(this, R.string.zcdh_uid_has_been_bound,
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

		if (reqId.equals(kREQ_ID_registerByOpenId)) {
			if (result != null) {
				int resultCode = (Integer) result;
				if (resultCode == 0) {
					if (Constants.LOGIN_TYPE_QQ.equals(login_type)) {
						LoginHelper.getInstance(this, this).doLoginQQ(
								third_open_id);
					} else if (Constants.LOGIN_TYPE_WEIBO.equals(login_type)) {
						LoginHelper.getInstance(this, this).doLoginSinaWeibo(
								third_open_id);
					} else if (Constants.LOGIN_TYPE_WECHAT.equals(login_type)) {
						LoginHelper.getInstance(this, this).doLoginWeChat(
								third_open_id);
					}
				}
				if (resultCode == 9 || resultCode == 10) { //
					Toast.makeText(this, "账号已存在", Toast.LENGTH_SHORT).show();

					Log.e(TAG, "用第三方创建账号 错误:账号已存在  " + third_open_id);
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

	@Override
	public void requestLoginFinished(int resultCode, String errorMsg) {
		if (resultCode == Constants.kLOGIN_RESULT_SUCCESS) {
			Intent loginResultMsg = new Intent(Constants.LOGIN_RESULT_ACTION);
			loginResultMsg.putExtra(Constants.kRESULT_CODE, resultCode);

			LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
					.getInstance(this);
			localBroadcastManager.sendBroadcast(loginResultMsg);
			finish();
		} else {
			Log.i(TAG, "注册后的绑定账号失败:" + errorMsg);
		}
		processDialog.dismiss();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case Constants.MSG_USERID_FOUND: {
			Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case Constants.MSG_LOGIN:
			if (isBindAccount) {
				bindAccount(SharedPreferencesUtil.getValue(this,
						Constants.HEAD_IMG_URL, ""));
			} else {
				registerByWechat(SharedPreferencesUtil.getValue(this,
						Constants.HEAD_IMG_URL, ""));
			}
			break;
		case Constants.MSG_AUTH_CANCEL:
			Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constants.MSG_AUTH_ERROR:
			// 失败
			String expName = msg.obj.getClass().getSimpleName();
			if ("WechatClientNotExistException".equals(expName)
					|| "WechatTimelineNotSupportedException".equals(expName)
					|| "WechatFavoriteNotSupportedException".equals(expName)) {
				int resId = getStringRes(this, "wechat_client_inavailable");
				if (resId > 0) {
					showNotification(this.getString(resId));
				}
			}
			break;
		case Constants.MSG_AUTH_COMPLETE:
			SharedPreferencesUtil.putValue(this, Constants.HEAD_IMG_URL,
					(String) msg.obj);
			Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT)
					.show();
			break;
		}

		return false;
	}

	// 在状态栏提示分享操作
	private void showNotification(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
