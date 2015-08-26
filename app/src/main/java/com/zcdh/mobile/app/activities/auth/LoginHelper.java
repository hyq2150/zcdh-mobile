package com.zcdh.mobile.app.activities.auth;

import java.util.HashMap;
import java.util.UUID;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

//import com.baidu.location.r;
import com.baidu.mapapi.model.LatLng;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.CheckUserRequisiteDTO;
import com.zcdh.mobile.api.model.JobUserResumeMiddleDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.push.PushHelper;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.framework.nio.AppEverimentArgs;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DeviceIDFactory;
import com.zcdh.mobile.utils.SharedPreferencesUtil;

/**
 * 登录助手
 * 
 * @author yangjiannan
 *
 */
@EBean
public class LoginHelper implements RequestListener {

	public final static String kLOGIN_ACCOUNT = "klogin_account";
	private final static String kLOGIN_PWD = "klogin_pwd";

	private final static String kAUTH_TYPE_ZCDH = "zcdh";

	private static final String TAG = LoginHelper.class.getSimpleName();

	private String kREQ_ID_login; // 登录验证
	private String kREQ_ID_loginByQQ;//
	private String kREQ_ID_loginByWeiBo;
	private String kREQ_ID_loginByWeChat;

	private String kREQ_ID_findUserId;// 查找用户ID
	private String kREQ_ID_findUserIdByWeiBo;// 根据微博用户
	private String kREQ_ID_findUserIdByWeChat;
	private String kREQ_ID_findUserIdByQQ;//
	private String kREQ_ID_CheckUserRequisite; // 检查基本信息是否填写
	private String kREQ_ID_findJobUserResumeMiddleDTO;// 检查简历完整度
	private String kREQ_ID_addUserLog; // 记录用户使用

	private IRpcJobUservice jobUservice;
	LoginListener mLoginListener;
	private Context context;

	// 当前验证登录的用户
	private String login_for_account;
	private String login_for_pwd;
	private String login_for_third_openId;

	// 标识是否到绑定账号页面
	// private boolean should_bind_account = false;

	public LoginHelper(Context context) {
		this.context = context;
		this.jobUservice = RemoteServiceManager
				.getRemoteService(IRpcJobUservice.class);
	}

	public static LoginHelper getInstance(Context context) {
		final LoginHelper_ instance = LoginHelper_.getInstance_(context);
		return instance;
	}

	public static LoginHelper getInstance(Context context,
			LoginListener loginListener) {
		final LoginHelper_ instance = LoginHelper_.getInstance_(context);
		instance.mLoginListener = loginListener;
		return instance;
	}

	/**
	 * 用职场导航账号查找userId;
	 */
	@Background
	public void findUserId() {
		Log.i("duploginbug", "findUserId...");
		jobUservice.findUserId(login_for_account).identify(
				kREQ_ID_findUserId = RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 根据QQ 的第三方验证得到的用户查找userId
	 */
	@Background
	public void findUserIdByQQ(String openId) {
		this.login_for_third_openId = openId;
		jobUservice.findUserIdByQQ(login_for_third_openId).identify(
				kREQ_ID_findUserIdByQQ = RequestChannel.getChannelUniqueID(),
				this);
	}

	/**
	 * 根据wechat 的第三方验证得到的用户查找userId
	 */
	@Background
	public void findUserIdByWeChat(String openId) {
		this.login_for_third_openId = openId;
		jobUservice
				.findUserIdByWeChat(login_for_third_openId)
				.identify(
						kREQ_ID_findUserIdByWeChat = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 更具新浪微博第三方验证得到的用查找userId
	 */
	@Background
	public void findUserIdByWeiBo(String openId) {
		this.login_for_third_openId = openId;
		jobUservice
				.findUserIdByWeiBo(login_for_third_openId)
				.identify(
						kREQ_ID_findUserIdByWeiBo = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 自动登录
	 * 
	 * @param loginListner
	 */
	public void doLogin() {
		// 清除保存的UID, 重设为-1
		SharedPreferencesUtil.putValue(context, K.UserInfo.uid_key, -1 + "");
		Log.i("duploginbug", "doLogin start...");
		String auth_type = SharedPreferencesUtil.getValue(context,
				Constants.kLOGIN_AUTH_TYPE, null);
		if (auth_type != null) {
			if (kAUTH_TYPE_ZCDH.equals(auth_type)) {
				String account = SharedPreferencesUtil.getValue(context,
						kLOGIN_ACCOUNT, null);
				String pwd = SharedPreferencesUtil.getValue(context,
						kLOGIN_PWD, null);
				if (account != null && pwd != null) {
					doLoginZCDH(account, pwd);
				} else {
					mLoginListener.requestLoginFinished(
							Constants.kLOGIN_RESULT_NOT_LOGINED, null);
					addUserLog(Constants.LOGIN_TYPE_NONE,
							Constants.LOGIN_TYPE_NONE);
				}
			} else { // 如果是用第三方登录支付
				String openId = SharedPreferencesUtil.getValue(context,
						Constants.KLOGIN_THIRD_ACCOUNT, null);
				if (openId == null) {
					mLoginListener.requestLoginFinished(
							Constants.kLOGIN_RESULT_NOT_LOGINED, null);
					addUserLog(Constants.LOGIN_TYPE_NONE,
							Constants.LOGIN_TYPE_NONE);
				} else {
					if (Constants.kAUTH_TYPE_QQ.equals(auth_type)) {
						doLoginQQ(openId);
					} else if (Constants.kAUTH_TYPE_SINA_WEIBO
							.equals(auth_type)) {
						doLoginSinaWeibo(openId);
					} else if (Constants.kAUTH_TYPE_WECHAT.equals(auth_type)) {
						doLoginWeChat(openId);
					}
				}
			}
		} else {
			mLoginListener.requestLoginFinished(
					Constants.kLOGIN_RESULT_NOT_LOGINED, null);
			addUserLog(Constants.LOGIN_TYPE_NONE, Constants.LOGIN_TYPE_NONE);
		}
	}

	/**
	 * 用职场导航账号登录
	 */
	@Background
	public void doLoginZCDH(String account, String pwd) {
		this.login_for_account = account;
		this.login_for_pwd = pwd;
		this.jobUservice.login(account, pwd).identify(
				kREQ_ID_login = RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 用QQ 账号登录
	 */
	@Background
	public void doLoginQQ(String openId_qq) {
		this.login_for_third_openId = openId_qq;
		Log.i("qq openId", openId_qq);
		this.jobUservice.loginByQQ(openId_qq).identify(
				kREQ_ID_loginByQQ = RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 用微信账号登录
	 */
	@Background
	public void doLoginWeChat(String openId_wechat) {
		this.login_for_third_openId = openId_wechat;
		this.jobUservice.loginByWeChat(openId_wechat).identify(
				kREQ_ID_loginByWeChat = RequestChannel.getChannelUniqueID(),
				this);
	}

	/**
	 * 用新浪微博登录
	 */
	@Background
	public void doLoginSinaWeibo(String openId_sina_weibo) {
		this.login_for_third_openId = openId_sina_weibo;
		this.jobUservice.loginByWeiBo(openId_sina_weibo).identify(
				kREQ_ID_loginByWeiBo = RequestChannel.getChannelUniqueID(),
				this);
	}

	/**
	 * 注销
	 */
	public static void doExit(Context context) {
		ZcdhApplication.getInstance().setZcdh_uid(-1);
		String auth_type = SharedPreferencesUtil.getValue(context,
				Constants.kLOGIN_AUTH_TYPE, null);

		if (LoginHelper.kAUTH_TYPE_ZCDH.equals(auth_type)) {
			SharedPreferencesUtil.putValue(context, LoginHelper.kLOGIN_ACCOUNT,
					null);
			SharedPreferencesUtil.putValue(context, LoginHelper.kLOGIN_PWD,
					null);
		} else if (Constants.kAUTH_TYPE_SINA_WEIBO.equals(auth_type)
				|| Constants.kAUTH_TYPE_QQ.equals(auth_type)
				|| Constants.kAUTH_TYPE_WECHAT.equals(auth_type)) { // 如果是用第三方登录支付
			SharedPreferencesUtil.putValue(context,
					Constants.KLOGIN_THIRD_ACCOUNT, null);
		}
		// 解除百度消息推送绑定
		PushHelper.getInstance(context).unBind();
	}

	public void doCheck() {
		doCheckUserRequisite();
		doCheckUserResumeMiddleDTO();
	}

	/**
	 * 查询出用户的简历完整度
	 */
	@Background
	public void doCheckUserResumeMiddleDTO() {
		jobUservice
				.findJobUserResumeMiddleDTO(
						ZcdhApplication.getInstance().getZcdh_uid())
				.identify(
						kREQ_ID_findJobUserResumeMiddleDTO = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 查询出用户的基本信息完整度
	 */
	@Background
	public void doCheckUserRequisite() {
		jobUservice
				.CheckUserRequisite(ZcdhApplication.getInstance().getZcdh_uid())
				.identify(
						kREQ_ID_CheckUserRequisite = RequestChannel
								.getChannelUniqueID(),
						this);
		;
	}

	/**
	 * 记录用户使用日志
	 * 
	 * @param loginType
	 * @param loginAccount
	 */
	@Background
	void addUserLog(String loginType, String loginAccount) {
		if (!K.debug) {
			AppEverimentArgs appEverimentArgs = ZcdhApplication.getInstance()
					.getEverimentArgs();
			HashMap<String, String> args = appEverimentArgs.getEverimentArgs();
			// 是否第一次装 IS_FIRST_INSTALL : 0 不是第一次安装, 1 第一次安装。
			args.put(
					Constants.INSTALLATION,
					SharedPreferencesUtil.getValue(context,
							Constants.INSTALLATION, 1) + "");
			// 登陆类型 LOGIN_TYPE: weiBo ,Mobile,QQ,None
			args.put(Constants.LOGIN_TYPE, loginType);
			// 登陆账号： LOGIN_ACCOUNT :如果是手机号码或者邮箱登陆： 则记录手机号码或者邮箱， 如果是第三方的账号，则是
			// openId
			args.put(Constants.LOGIN_ACCOUNT, loginAccount);
			// 标识手机的唯一串号
			UUID sid = new DeviceIDFactory(context).getDeviceUuid();
			args.put(Constants.MIEI, sid.toString());
			// APP_ID ： ZCDH_JOB 职场导航求指端APP , ZCDH_ENT 职场导航企业端APP
			args.put(Constants.APP_ZCDH_ID, "ZCDH_JOB");
			// 经纬度
			LatLng loc = ZcdhApplication.getInstance().getMyLocation();
			String lat = null;
			String lon = null;
			if (loc != null) {
				lat = loc.latitude + "";
				lon = loc.longitude + "";
			} else {
				lat = 91 + "";
				lon = 181 + "";
			}
			args.put(Constants.LAT, lat);
			args.put(Constants.LON, lon);

			// 渠道
			try {
				args.put(
						Constants.CHANNEL,
						context.getPackageManager().getApplicationInfo(
								context.getPackageName(),
								PackageManager.GET_META_DATA).metaData
								.getString(Constants.CHANNEL));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			jobUservice.addUserLog(args).identify(
					kREQ_ID_addUserLog = RequestChannel.getChannelUniqueID(),
					this);
		}
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		// if(loginListner==null)return;
		if (result == null) {
			this.mLoginListener.requestLoginFinished(
					Constants.kLOGIN_RESULT_FAILE, "服务繁忙");
			return;
		}

		// 登录验证，用职场导航账号
		if (reqId.equals(kREQ_ID_login)) {
			int resultCode = (Integer) result;
			boolean success = false;
			String errorMsg = "";
			switch (resultCode) {
			case 0:// 登陆成功
				success = true;
				break;
			case 4:// 账号不存在
				errorMsg = context.getResources().getString(
						R.string.account_not_exists);
				break;
			case 5:// 密码错误
				errorMsg = context.getResources().getString(
						R.string.wrong_password);
				break;
			case 7:// 账号已经停用
				errorMsg = context.getResources().getString(
						R.string.account_banned);
				break;
			case 52:
				errorMsg = "无效的手机号码或Email格式";
				break;
			case 53:
				errorMsg = "绑定邮箱没有对应的手机账号";
				break;
			}
			if (success) { // 如果登录成功，获得userId
				findUserId();

			} else {// 如果失败，直接返回原处
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_FAILE, errorMsg);
			}
		}

		// 登录验证， 用qq
		if (reqId.equals(kREQ_ID_loginByQQ)) {
			int resultCode = (Integer) result;
			switch (resultCode) {
			case 0:// 登陆成功
				findUserIdByQQ(login_for_third_openId);
				break;
			case 6:// QQ第一次登陆
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_BIND, "");
				break;
			case 7:// 账号已经停用
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_FAILE, context.getResources()
								.getString(R.string.account_banned));
				break;
			}
		}

		// 登录验证， 用weibo
		if (reqId.equals(kREQ_ID_loginByWeiBo)) {
			int resultCode = (Integer) result;
			switch (resultCode) {
			case 0:// 登陆成功
				findUserIdByWeiBo(login_for_third_openId);
				break;
			case 6:// WeiBo第一次登陆
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_BIND, "");
				break;
			case 7:// 账号已经停用
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_FAILE, context.getResources()
								.getString(R.string.account_banned));
				break;
			}
		}

		// 登录验证，用wechat
		if (reqId.equals(kREQ_ID_loginByWeChat)) {
			int resultCode = (Integer) result;
			switch (resultCode) {
			case 0:// 成功
				findUserIdByWeChat(login_for_third_openId);
				break;
			case 4:// 账号不存在
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_BIND, context.getResources()
								.getString(R.string.account_not_exists));
				break;
			case 5:// 密码错误
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_BIND, context.getResources()
								.getString(R.string.wrong_password));
				break;
			case 6:// 第三方快捷登陆账号不存在
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_BIND, "第三方快捷登陆账号不存在");
				break;
			case 7:// 账号已停用
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_FAILE, context.getResources()
								.getString(R.string.account_banned));
				break;
			case 17:// 用户资料已被销毁
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_FAILE, "用户资料已被销毁");
				break;
			default:
				break;
			}
		}

		// 查找用户ID，职场导航账号登录成功后，
		if (reqId.equals(kREQ_ID_findUserId)) {
			long userId = (Long) result;
			if (userId > 0) {
				// 将用户id保存到Application 实例中，方便使用
				ZcdhApplication.getInstance().setZcdh_uid(userId);
				// 记录用户和密码到本地
				SharedPreferencesUtil.putValue(context, kLOGIN_ACCOUNT,
						login_for_account);
				SharedPreferencesUtil.putValue(context, kLOGIN_PWD,
						login_for_pwd);
				// 记录用什么方式登录成功,此时是用职场导航账号
				SharedPreferencesUtil.putValue(context,
						Constants.kLOGIN_AUTH_TYPE, kAUTH_TYPE_ZCDH);
				if (this.mLoginListener != null) {
					// 登录成功返回调用原处
					this.mLoginListener.requestLoginFinished(
							Constants.kLOGIN_RESULT_SUCCESS, null);
				}
				// 检查用户资料完整情况
				doCheck();
				// 绑定百度消息推送

				PushHelper.getInstance(context).bindBaiduPushServer();
				// 记录登录日志
				addUserLog(Constants.LOGIN_TYPE_MOBILE, login_for_account);
			} else {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.account_not_exists),
						Toast.LENGTH_SHORT).show();
			}
		}

		// 用第三方账号登录(QQ)后, 查找用户ID
		if (reqId.equals(kREQ_ID_findUserIdByQQ)) {
			long userId = (Long) result;
			if (userId > 0) {
				// 将用户id保存到Application 实例中，方便使用
				ZcdhApplication.getInstance().setZcdh_uid(userId);
				// 保存第三方open_id
				SharedPreferencesUtil.putValue(context,
						Constants.KLOGIN_THIRD_ACCOUNT, login_for_third_openId);
				// 记录用什么方式登录成功,此时是第三方登录验证
				SharedPreferencesUtil.putValue(context,
						Constants.kLOGIN_AUTH_TYPE, Constants.kAUTH_TYPE_QQ);
				// 登录成功返回调用原处
				if (this.mLoginListener != null) {
					this.mLoginListener.requestLoginFinished(
							Constants.kLOGIN_RESULT_SUCCESS, null);
				}
				// 检查用户资料完整情况
				doCheck();
				// 绑定百度消息推送
				PushHelper.getInstance(context).bindBaiduPushServer();
				// 记录登录日志
				addUserLog(Constants.LOGIN_TYPE_QQ, login_for_third_openId);
			} else {
				this.mLoginListener.requestLoginFinished(
						Constants.kLOGIN_RESULT_FAILE, context.getResources()
								.getString(R.string.account_not_exists));
			}
		}

		// 用第三方账号登录（wecaht）后，查找用户ID
		if (reqId.equals(kREQ_ID_findUserIdByWeChat)) {
			long userId = (Long) result;
			if (userId > 0) {
				ZcdhApplication.getInstance().setZcdh_uid(userId);
				SharedPreferencesUtil.putValue(context,
						Constants.KLOGIN_THIRD_ACCOUNT, login_for_third_openId);
				SharedPreferencesUtil
						.putValue(context, Constants.kLOGIN_AUTH_TYPE,
								Constants.kAUTH_TYPE_WECHAT);
				if (this.mLoginListener != null) {
					this.mLoginListener.requestLoginFinished(
							Constants.kLOGIN_RESULT_SUCCESS, null);
				}
				// 检查用户资料完整情况
				doCheck();
				// 绑定百度消息推送
				PushHelper.getInstance(context).bindBaiduPushServer();
				// 记录登录日志
				addUserLog(Constants.LOGIN_TYPE_WECHAT, login_for_third_openId);
			} else {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.account_not_exists),
						Toast.LENGTH_SHORT).show();
			}
		}

		// 用第三方账号登录(Sina weibo)后,查找用户ID
		if (reqId.equals(kREQ_ID_findUserIdByWeiBo)) {
			long userId = (Long) result;
			if (userId > 0) {
				// 将用户id保存到Application 实例中，方便使用
				ZcdhApplication.getInstance().setZcdh_uid(userId);
				// 保存第三方open_id
				SharedPreferencesUtil.putValue(context,
						Constants.KLOGIN_THIRD_ACCOUNT, login_for_third_openId);
				// 记录用什么方式登录成功,此时是第三方登录验证
				SharedPreferencesUtil.putValue(context,
						Constants.kLOGIN_AUTH_TYPE,
						Constants.kAUTH_TYPE_SINA_WEIBO);
				// 登录成功返回调用原处
				if (this.mLoginListener != null) {
					this.mLoginListener.requestLoginFinished(
							Constants.kLOGIN_RESULT_SUCCESS, null);
				}
				// 检查用户资料完整情况
				doCheck();
				// 绑定百度消息推送
				PushHelper.getInstance(context).bindBaiduPushServer();
				// 记录登录日志
				addUserLog(Constants.LOGIN_TYPE_WEIBO, login_for_third_openId);
			} else {
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.account_not_exists),
						Toast.LENGTH_SHORT).show();
			}
		}

		// 用户简历完整情况
		if (reqId.equals(kREQ_ID_findJobUserResumeMiddleDTO)) {
			if (result != null) {
				JobUserResumeMiddleDTO chkDto = (JobUserResumeMiddleDTO) result;
				Log.e(TAG,
						"getIsCanPostResume : " + chkDto.getIsCanPostResume());
				Log.e(TAG, "getIsCanSignUp : " + chkDto.getIsCanSignUp());
				ZcdhApplication.getInstance().setJobUserResumeMiddleDTO(chkDto);
			}
		}

		// 用户资料完整情况
		if (reqId.equals(kREQ_ID_CheckUserRequisite)) {

			if (result != null) {
				CheckUserRequisiteDTO chkDto = (CheckUserRequisiteDTO) result;
				ZcdhApplication.getInstance().setRequisiteDTO(chkDto);
			}
		}

		if (reqId.equals(kREQ_ID_addUserLog)) {
			Log.i(TAG, "" + result);
			if (result != null) {
				int resultCode = (Integer) result;
				if (resultCode == 0) {

					// 改变首次安装标识
					int isFirstInstallation = SharedPreferencesUtil.getValue(
							context, Constants.INSTALLATION, 1);
					if (isFirstInstallation == 1) {
						SharedPreferencesUtil.putValue(context,
								Constants.INSTALLATION, 0);
					}
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		if (error != null) {
			Log.i(TAG, error.getMessage() + "");
		}
		this.mLoginListener.requestLoginFinished(Constants.kLOGIN_RESULT_FAILE,
				"服务繁忙，登录失败");
	}

}
