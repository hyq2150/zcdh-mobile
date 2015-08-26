package com.zcdh.mobile.framework;

/**
 * @author yangjiannan
 * 
 *         创建日期 2014-3-12 描述 保存项目框架的键值
 */
public class K {

	public static class UserInfo {
		public static final String uid_key = "userId";
	}

	/**
	 * 标识是否异步请求数据
	 */
	public static final boolean SyncRequest = true;
	/**
	 * 网络数据标识符
	 */
	public static final String SUCCESS = "1"; // 成功

	public static final String FAILURE = "0"; // 失败

	//
	public static boolean debug = false;

	public static class AppVersion {

		public enum Versions {
			dev, // 开发
			beta, // 测试
			preview, // 预览版
			release// 发布版
		}

		public static Versions appVersion = Versions.release;
	}

	/**
	 * 
	 * @author yangjiannan
	 * 
	 */
	public static class Hosts {

		/** 用于app更新升级的XML地址 */
		public static String UPGRADE_URL = "";

		/** 用于数据更新升级的XML地址 */
		public static String UPGRADE_DATA_URL = "";

		/** 图片服务器地址 */
		public static String HTTP_SERVER_ADDRESS = "";

		public static String HTTP_SERVER_PORT = "";

		/*** Socket服务器缺省地址 */
		public static String SERVER_ADDRESS_DEFAULT = "";

		/** 功能介绍地址 */
		public static String USER_GUIDE_URL = "";

		public static int SERVER_PORT_DEFAULT = 3005;

		static {
			switch (AppVersion.appVersion) {
			case dev: {// 开发版
				UPGRADE_URL = "http://192.168.1.102:8080/upgrade.xml";
				UPGRADE_DATA_URL = "http://192.168.1.140:8080/upgradeDatabase.uuxml";
				HTTP_SERVER_ADDRESS = "http://192.168.1.5";
				HTTP_SERVER_PORT = "82";
				SERVER_ADDRESS_DEFAULT = "192.168.1.182";
				SERVER_PORT_DEFAULT = 3005;
				USER_GUIDE_URL = "www.zcdhjob.com";
				break;
			}
			case beta: {
				UPGRADE_URL = "http://192.168.1.22:8080/upgrade.xml";
				UPGRADE_DATA_URL = "http://192.168.1.22:8080/upgradeDatabase.xml";
				HTTP_SERVER_ADDRESS = "http://192.168.1.33";
				HTTP_SERVER_PORT = "83";
				SERVER_ADDRESS_DEFAULT = "192.168.1.12";
				SERVER_PORT_DEFAULT = 3005;
				USER_GUIDE_URL = "www.zcdhjob.com";
				break;
			}

			case preview: {
				UPGRADE_URL = "http://113.106.12.108/upgrade.xml";
				UPGRADE_DATA_URL = "http://113.106.12.108/upgradeDatabase.xml";
				HTTP_SERVER_ADDRESS = "http://113.106.12.199";
				HTTP_SERVER_PORT = "81";
				SERVER_ADDRESS_DEFAULT = "113.106.12.108";
				SERVER_PORT_DEFAULT = 3005;
				USER_GUIDE_URL = "www.zcdhjob.com";
				break;
			}
			case release: {
				UPGRADE_URL = "http://113.106.12.108/upgrade.xml";
				UPGRADE_DATA_URL = "http://113.106.12.108/upgradeDatabase.xml";
				HTTP_SERVER_ADDRESS = "http://113.106.12.199";
				HTTP_SERVER_PORT = "81";
				SERVER_ADDRESS_DEFAULT = "113.106.12.198";
				SERVER_PORT_DEFAULT = 3005;
				USER_GUIDE_URL = "www.zcdhjob.com";
			}

			}
		}
	}

	/**
	 * 
	 * @author yangjiannan
	 * 
	 *         创建日期 2014-3-12 描述 : 数据库相关常量
	 */
	public static class DB {

		public static final String DbName = "zcdh.db";

		public static final String version = "2.0.5";

		/**
		 * 保存当前本地基础数据版本号
		 */
		public static String kBASIC_DB_VERSION_KEY = "kdbversion";

	}

	/**
	 * 网络相关
	 * 
	 * @author chenhanbu
	 * 
	 */
	public static class Network {
		/**
		 * 网络错异常代码
		 */
		public final static String kNETWORK_EXCE_EMPTY = "404";// 服务器返回数据为空
		public final static String kNETWORK_EXCE_EMPTY_FLAG = "405";// 服务器返回数据标志为空
		public final static String kNETWORK_EXCE_EMPTY_RESULT = "406";// 服务器返回数据结果为空
		public final static String kNETWORK_EXCE_DISCONN = "407";// 无法连接远程服务器
		public final static String kNETWORK_EXCE_BREAK_CONN = "408";// 远程服务器已经断开连接
		public final static String kNETWORK_EXCE_TIMEOUT = "409";// 连接服务器超时

	}

	/**
	 * 
	 * @author yangjiannan
	 * 
	 *         创建日期 2014-3-12 描述 : 缓存相关，缓存图片
	 */
	public static class Cache {

		// /**
		// * 缓存图片文件夹
		// */
		// public static String kCACHE_IMG_DIR = "";
	}

	public static class System {
		public static final String SYSTEM_SERVICE = "system_service";
		public static final String SYSTEM_SERVICE_COMMAND_CLOESE = "system_command_close";
	}

	/**
	 * 用于Intent 意图传递参数的key常量
	 * 
	 * @author chenhanbu
	 * 
	 */
	public static class Extras {

		public static final String RES_IDS = "res_ids"; // 资源ID

	}

	public static class ResType {
		public static final int RES_ID = 0; //
		public static final int RES_LAYOUT = 1;
	}

//	public static class TestIN {
//		public static final String appKey = "04d5871f897613220a954436078a8a4f";
//	}
}
