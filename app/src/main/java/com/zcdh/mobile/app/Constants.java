package com.zcdh.mobile.app;

/**
 * 记录常量
 * 
 * @author yangjiannan
 * 
 */
public class Constants {
	
	public static final String IFLYTEC_APP_ID = "53548971";
	
	public static final String UMENG_NEARBY_MORE_ACTION = "nearbyMoreAction";
	public static final String UMENG_NEARBY_LIST_ACTION = "nearbyListAction";
	public static final String UMENG_JOB_TYPE_ACTION = "jobTypeAction";
	public static final String UMENG_ACCOUNT_ACTION = "accountAction";
	public static final String UMENG_VOICE_BUTTON_ACTION = "voiceButtonAction";
	public static final String UMENG_ENTER_FIND_PAGE = "enterFindPage";
	
	public static final String URL_SCAN_FAIR_POST = "ent/fair/fairOpt!scanFairPost.action";// 投简历
	public static final String URL_SIGNIN_FAIR = "jobhunte/account/accountOpt!jobFairSignIn.action";// 签到
	
	public static final int kREQUEST_INDUSTRY = 2001;
	public static final String kDATA_CODE = "industry_code";
	public static final String kDATA_NAME = "industry_name";
	public static final String kDATA_OFFLINE = "isOffline";
	public static final String kDATA_ONLINE = "isONline";
	public static final String kDATA_INDUSTRY_BUNDLE = "industry_bundle";
	
	public final static String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	public final static int flag_map = 1; //标识地图操作指引
	public final static int flag_postlist = 2; //标识职位列表模式的操作指引 
	public static final String HEAD_IMG_URL = "headimgurl";
	public static final String WECHAT_UNIONID = "unionid";
	public static final int MSG_USERID_FOUND = 1;
	public static final int MSG_LOGIN = 2;
	public static final int MSG_AUTH_CANCEL = 3;
	public static final int MSG_AUTH_ERROR = 4;
	public static final int MSG_AUTH_COMPLETE = 5;
	public static final int MSG_AUTH_LOGIN = 6;
	
	public final static int kLOGIN_RESULT_SUCCESS = 0;// 登录成功
	public final static int kLOGIN_RESULT_FAILE = 1;// 登录失败
	public final static int kLOGIN_RESULT_BIND = 2;// 用的三方登录验证首次登录，需要绑定职场导航账号
	public final static int kLOGIN_RESULT_NOT_LOGINED = 3; // 还未登录
	public static final String ACTION_EXIT = "action_exit_app";
	
	public final static String kAUTH_TYPE_QQ = "qq";
	public final static String kAUTH_TYPE_SINA_WEIBO = "sina_weibo";
	public final static String kAUTH_TYPE_WECHAT = "weChat";
	
	public final static String KLOGIN_THIRD_ACCOUNT = "klogin_third_account";// 记录第三方登录
	public final static String kLOGIN_AUTH_TYPE = "auth_type"; // 记录已经登录的用户是用什么方式登录，1）职场导航账号登录
																// 2）用第三方登录，如：qq，
																// weibo...
	/**
	 * 记录是否首次安装APP
	 */
	public static final String INSTALLATION = "IS_FIRST_INSTALL";
	/**
	 * 登陆类型 LOGIN_TYPE: weiBo ,Mobile,QQ,None 登陆账号： LOGIN_ACCOUNT
	 * :如果是手机号码或者邮箱登陆： 则记录手机号码或者邮箱， 如果是第三方的账号，则是 openId 手机串号： MIEI 手机的唯一串号
	 * 是否第一次装 IS_FIRST_INSTALL : 0 不是第一次安装, 1 第一次安装。
	 */

	public static final String LOGIN_TYPE = "LOGIN_TYPE";
	public static final String LOGIN_TYPE_MOBILE = "Mobile";
	public static final String LOGIN_TYPE_NONE = "None";
	public static final String LOGIN_ACCOUNT = "LOGIN_ACCOUNT";
	public static final String MIEI = "MIEI";
	public static final String APP_ZCDH_ID = "APP_ID";
	public static final String LAT = "LAT";
	public static final String LON = "LON";
	public static final String CHANNEL = "UMENG_CHANNEL";

	/**
	 * 招聘会状态
	 */
	public static final String FAIR_STATUS = "fair_status";
	
	public static final String FROM_WHERE = "fromWhere";
	public static final String FROM_AD = "formFindAd";
	public static final String FROM_MAP_AD = "fromMapAd";

	// 照片上传
	public static final int REQUEST_CODE_TAKE_PIC = 0x01;
	public static final int REQUEST_CODE_CHOOSE_GALLERY = 0x02;

	/**
	 * 纠错
	 */
	public static final String kTYPE_JIUCUO = "003";
	public static final String kTYPE_LIUYAN = "001";
	public static final String kTYPE_ZIXUN = "002";

	/**
	 * 绑定手机号码
	 */
	public static final int kREQUEST_BIND_MOBILE = 3000;

	/**
	 * 绑定电邮
	 */
	public static final int kREQUEST_BIND_EMAIL = 3001;

	/**
	 * 修改头像广播动作
	 */
	public static final String kACTION_MODIFIED_PHOTO = "kACTION_MATCH_MODE";

	// 拍照
	public static final int REQUEST_CODE_TAKE_PHOTO = 10;

	public static final int REQUEST_CODE_GALLERY = 0;

	public static final int ACTION_IMAGE_CROP = 11;

	public static final Object KSTATUS_SILDMENU_OPEN = 1;
	public static final Object KSTATUS_SILDMENU_CLOSED = 0;

	public static final String KEVENT_SILDMENU_STATUS = "KEVENT_SILDMENU_STATUS";
	public static final String kEVENT_NOTIFICATION_MESSAGE = "kEVENT_NOTIFICATION_MESSAGE";

	/**
	 * 登录结果广播动作
	 */
	public static final String LOGIN_RESULT_ACTION = "LOGIN_RESULT_ACTION";
	public static final int REQUEST_CODE_LOGIN = 100;
	public final static int REQUEST_CODE_MODIFY_RESUME = 0x101;

    	public static final int REQUEST_CODE_SETTING = 0x102;
	public static final String ARGUMENTS_NAME = "arg";
	public static final String kAUTO_MENU = "auto_menu";

	public static final String kEVENT_GET_SCREEN_BOUND = "kEVENT_GET_SCREEN_BOUND";
	public static final String kSHARE_NEW_USER_GUIDE_MAP = "kSHARE_NEW_USER_GUIDE";
	public static final String kSHARE_NEW_USER_GUIDE_LIST = "kSHARE_NEW_USER_GUIDE_LIST";

	public static final String kEVENT_RECIVE_SCREEN_BOUND = "kEVENT_RECIVE_SCREEN_BOUND";
	public static final String ACTION_UPDATE_PROFILE = "ACTION_UPDATE_PROFILE";
	public static boolean isShowingPointDetails;

	public static final String JOBFAIR_ID_KEY = "jobfair_id";
	public static final String KFLAG_ENT = "kflag_ent";
	public static final String kMSG_ENT_CHANGED = "entId_changed";
	public static final String kMSG_POST_ID = "post_id";
	public static final String kPOST_ID = "postId";
	public static final int kREQUEST_FAVORITE = 2031;
	/**
	 * 有外部传入企业值key
	 */
	public static final String kENT_ID = "entId";
	public static final int REQUEST_CODE_COMMENT = 0x101;

	public static final String POST_PROPERTY_QUANZHI = "007.001";
	public static final String POST_PROPERTY_JIANZHI = "007.002";
	public static final String POST_PROPERTY_SHIXI = "007.003";
	public static final String POST_PROPERTY_JIAQI = "007.005";

	// 资讯
	public static final String SINGN_TYPE_NEWS = "001";
	// 广告
	public static final String SINGN_TYPE_ADS = "002";

	public static final String COVER_ID = "COVER_ID";

//	public static final String regex_email = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	public static final String regex_email = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
//	String regex_id = "^\\d{15}$|^\\d{17}(?:\\d|x|X)$/^\\d{15}$|^\\d{17}(?:\\d|x|X)$/";
	// 如有新号段 请更新
	public static final String regex_phone = "^(1(([0234578][0-9])|(47)|[8][01236789]))\\d{8}$";
	public static final String REGEX_PHONE_KEY = "regex_phone";
	public static final String REGEX_EMAIL_KEY = "regex_email";

	public static final String regex_idcard = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

	//
	public static final String regex_milID = "^\\d{8}";

	public static final String regex_passport = "^(P\\d{7|G\\d{8}|S\\d{7,8}|D\\d+|1[4,5]\\d{7})$";

	public static final String TENCENT_APP_KEY = "tencent101043530";

	public static final String SINA_APP_KEY = "2631953288";
	/**
	 * 免费短信验证SDK
	 */
	public static final String SMS_APP_KEY = "3360c528a4c0";
	public static final String SMS_APP_SCRETE = "67ccf312323f3624366e88ce6a7ab719";

	// public static final String REDIRECT_URL = "http://www.sharesdk.cn";//
	// 应用的回调页
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 应用的回调页

	public static final String SINA_SCOPE = // 应用申请的高级权限

	"email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static final String SHARESDK_APPKEY = "170d7a84ca84";
	public static final String LOGIN_TYPE_QQ = "QQ";
	public static final String LOGIN_TYPE_WECHAT = "weChat";
	public static final String LOGIN_TYPE_WEIBO = "weiBo";

	// 高级查询广播动作
	public static final String kACTION_CONDITION = "action_condition";
	public static final String kRESULT_CODE = "resultCode";
	public static final String kREQUEST_CODE = "requestCode";

	public static final Integer Page_SIZE = 15;

	public static class Req {
		public static final String kREQ_ID_NAME = "req_id_name"; //

		public static final String kREQ_ID_VALUE = "req_id_value";

		public static final String kREQ_REQUESTLISTNER = "requestlistner";

	}

	public static class db {

		public static final String PACKAGE = "com.zcdh.mobile";
		/**
		 * 数据库名
		 */
		public static final String DATABASE_NAME = "zcdh.db";
		/**
		 * 如果你想把数据库文件存放在SD卡的话
		 */
		public static String DB_PATH_SD = android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/"
				+ PACKAGE + "/databases/";
		/**
		 * * 在内部储存卡的数据库缓存位置
		 */
		public static String DB_PATH_INTERNE = PACKAGE + "/databases/";

		public static String DB_PATH = "";
	}

	/**
	 * 设置项
	 */
	public static class SettingKeys {

		public static final String kSETTING_PUSH_TOGGLE = "kSETTING_PUSH_TOGGLE";// 开启或关闭推送
		public static final String kSETTING_PUSH_WIFI_TOGGLE = "kSETTING_PUSH_WIFI_TOGGLE";// WIFI环境下开启推送
		public static final String kSETTING_PUSH_TIME_TOGGLE = "kSETTING_PUSH_TIME_TOGGLE";// 推送时间段
		public static final String kSETTING_PUSH_START_TIME = "kSETTING_PUSH_START_TIME";// 推送开始时间
		public static final String kSETTING_PUSH_END_TIME = "kSETTING_PUSH_END_TIME";// 推送结束时间段
		public static final String kSETTING_PUSH_INTERVAL_TOGGLE = "kSETTING_PUSH_INTERVAL_TOGGLE";// 推送间隔

		// 是否开启岗位匹配模式，1表示开启，0表示不开启，默认不开启
		public static final String POST_MATCH_MODE_KEY = "POST_MATCH_MODE";
		// 岗位匹配度
		public static final String POST_MATCH_RATE_KEY = "POST_MATCH_RATE";

	}

	public enum ImagEdition {
		SMALL(1), MIDDLE(2), LARGE(3);

		private int value;

		ImagEdition(int size_value) {
			value = size_value;
		}

		public int getValue() {
			return value;
		}
	}
}
