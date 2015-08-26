package com.zcdh.mobile.app.activities.security;

import com.mob.tools.utils.UIHandler;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zcdh.mobile.app.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;

/**
 *
 * @author yangjiannan 第三方绑定 用第三方的账号登录
 *
 */
public class ThirdPartAuthHelper {

	private static final String QQ_APP_ID = "101043530";

	private static final String TAG = ThirdPartAuthHelper.class.getSimpleName();
	private Activity activity;

	private AuthResultCallback authCallback;

	/**
	 * 腾讯开放平台
	 */
	private static Tencent tencent;

	/**
	 * 微信
	 */
	private Platform wechat;
	/**
	 * 新浪微博
	 */
	private AuthInfo mAuthInfo;
	private SsoHandler mSsoHandler;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private static Oauth2AccessToken mAccessToken;

	/**
	 * 微博用户 信息接口
	 *
	 * @param activity
	 */
	private UsersAPI mUsersAPI;

	public ThirdPartAuthHelper(Activity activity) {
		this.activity = activity;
		if (tencent == null) {
			tencent = Tencent.createInstance(QQ_APP_ID, activity);
		}
		if (mSsoHandler == null) {
			mAuthInfo = new AuthInfo(activity, Constants.SINA_APP_KEY,
					Constants.REDIRECT_URL, Constants.SINA_SCOPE);
			mSsoHandler = new SsoHandler(activity, mAuthInfo);
		}
		if (wechat == null) {
			wechat = new Wechat(activity);
		}
	}

	public ThirdPartAuthHelper(Activity contActivity,
			AuthResultCallback authCallback) {
		this(contActivity);
		this.authCallback = authCallback;
	}

	/**
	 * 腾讯qq用户认证
	 */
	public void requestTencentQQAuth() {
		tencent.login(activity, "all", new IUiListener() {

			@Override
			public void onCancel() {

			}

			@Override
			public void onComplete(Object result) {
				try {
					String thirdpart_uid = ((JSONObject) result)
							.getString("openid");
					authCallback.onAuthResult(thirdpart_uid,
							Constants.LOGIN_TYPE_QQ);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(UiError arg0) {

			}

		});
	}

	/**
	 * 微信用户认证
	 */
	public void requestWechatAuth(final Callback callback) {
		if (wechat.isValid()) {
			String userId = wechat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				UIHandler
						.sendEmptyMessage(Constants.MSG_USERID_FOUND, callback);
				login(wechat.getName(), userId, null, callback);
				return;
			}
		}
		wechat.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform platform, int action, Throwable t) {
				// TODO Auto-generated method stub
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = Constants.MSG_AUTH_ERROR;
					msg.obj = t;
					UIHandler.sendMessage(msg, callback);
				}
				platform.removeAccount();
				t.printStackTrace();
			}

			@Override
			public void onComplete(Platform platform, int action,
					HashMap<String, Object> res) {
				// TODO Auto-generated method stub
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = Constants.MSG_AUTH_COMPLETE;
					if (res.containsKey(Constants.HEAD_IMG_URL)) {
						msg.obj = res.get(Constants.HEAD_IMG_URL);
					}
					UIHandler.sendMessage(msg, callback);
					login(platform.getName(), platform.getDb().getUserId(),
							res, callback);
				}
				platform.removeAccount();
			}

			@Override
			public void onCancel(Platform platform, int action) {
				// TODO Auto-generated method stub
				if (action == Platform.ACTION_USER_INFOR) {
					UIHandler.sendEmptyMessage(Constants.MSG_AUTH_CANCEL,
							callback);
				}
			}
		});
		wechat.SSOSetting(true);
		wechat.showUser(null);

	}

	private void login(String plat, String userId,
			HashMap<String, Object> userInfo, Callback callback) {
		Message msg = new Message();
		msg.what = Constants.MSG_LOGIN;
		msg.obj = userId;
		UIHandler.sendMessage(msg, callback);
	}

    /**
     * 新浪微博用户认证
     */

	public void requestSinaWeiboAuth() {
		mSsoHandler.authorize(new WeiboAuthListener() {

		    @Override
		    public void onCancel() {
		    }

		    @Override
		    public void onComplete(Bundle weibo_info) {
			mAccessToken = Oauth2AccessToken.parseAccessToken(weibo_info);
			if (mAccessToken.isSessionValid()) {
			    Log.i(TAG, "weibo login complete" + mAccessToken.getUid());

			    authCallback.onAuthResult(mAccessToken.getUid(),
				    Constants.LOGIN_TYPE_WEIBO);
			} else {
			    // 以下几种情况，您会收到 Code：
			    // 1. 当您未在平台上注册的应用程序的包名与签名时；
			    // 2. 当您注册的应用程序包名与签名不正确时；
			    // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
			    String code = weibo_info.getString("code");
			    if (!TextUtils.isEmpty(code)) {
				Log.i(TAG, "微博登录失败 \nObtained the code: " + code);
			    }
			    Toast.makeText(activity, "微博登录失败", Toast.LENGTH_SHORT)
				    .show();
			}
		    }

		    @Override
		    public void onWeiboException(WeiboException arg0) {
			Log.e(TAG, "weibo login exception");
			arg0.printStackTrace();
		    }

		});
	}
	/**
	 * 获取qq用户信息
	 */

	public boolean getUserInfoInQQ(IUiListener iUiListener) {
		if (tencent != null && tencent.isSessionValid()) {
			UserInfo info = new UserInfo(activity, tencent.getQQToken());
			info.getUserInfo(iUiListener);
			return true;
		} else {
			return false;
		}
	}

    public void getUserInfoInWeibo(final RequestListener requestListener) {
	if (mAccessToken != null && mAccessToken.isSessionValid()) {
	    Log.i(TAG, "uid:" + mAccessToken.getUid() + "");
	    mUsersAPI = new com.sina.weibo.sdk.openapi.UsersAPI(activity,
		    Constants.SINA_APP_KEY, mAccessToken);
	    long uid = Long.parseLong(mAccessToken.getUid());
	    mUsersAPI.show(uid, requestListener);
	}
    }

	/**
	 *
	 * @param type
	 *            (QQ, weiBo)
	 * @return
	 */
	public boolean isSessionValid(String type) {
		if (Constants.LOGIN_TYPE_QQ.equals(type)) {
			return (tencent != null && tencent.isSessionValid());
		}
		if (Constants.LOGIN_TYPE_WEIBO.equals(type)) {
			return mAccessToken != null && mAccessToken.isSessionValid();
		}
		return false;
	}

	/**
	 * 设置调起成功回调结果
	 *
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void setActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "requestCode" + requestCode);
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		if (tencent != null) {
			tencent.onActivityResult(requestCode, resultCode, data);
		}
	}

}
