package com.zcdh.mobile.app;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.mobile.api.model.CheckUserRequisiteDTO;
import com.zcdh.mobile.api.model.JobUserResumeMiddleDTO;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.framework.nio.AppEverimentArgs;
import com.zcdh.mobile.utils.SharedPreferencesUtil;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

public class ZcdhApplication extends MultiDexApplication {

	private boolean extension_flag = true;

	private static final String kDATA_LAT = "kdata_lat"; // 唯独
	private static final String kDATA_LON = "kdata_lon"; //

	private static ZcdhApplication app;

	private CheckUserRequisiteDTO requisiteDTO;
	
	private JobUserResumeMiddleDTO jobUserResumeMiddleDTO;
	

	private String order_num;
	/**
	 * 合同条款
	 * 
	 */
	private String contractStr = null;

	/***
	 * 我定位的位置
	 */
	private LatLng myLocation;

	@Override
	public void onCreate() {
		super.onCreate();
		if (K.debug && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}
		MobclickAgent.openActivityDurationTrack(false);
		// 异常错误捕捉
		/*Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(
				this));*/

		// FrontiaApplication.initFrontiaApplication(this);
		SDKInitializer.initialize(getApplicationContext());
		// LBS_AK = PushMessageUtils
		// .getMetaValue(this, "com.baidu.lbsapi.API_KEY");
		app = this;

		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.memoryCache(new LruMemoryCache(2 * 1024 * 1024));
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		// config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public static ZcdhApplication getInstance() {
		return app;
	}

	public long getZcdh_uid() {
		String uid = SharedPreferencesUtil.getValue(this, K.UserInfo.uid_key,
				-1 + "");
		return Long.valueOf(uid);
	}

	public void setZcdh_uid(long zcdh_uid) {
		SharedPreferencesUtil.putValue(this, K.UserInfo.uid_key, zcdh_uid + "");
	}

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}

	public String getContractStr() {
		return contractStr;
	}

	public void setContractStr(String contractStr) {
		this.contractStr = contractStr;
	}

	/**
	 * 
	 * @return
	 */
	public CheckUserRequisiteDTO getRequisiteDTO() {
		return requisiteDTO;
	}

	public void setRequisiteDTO(CheckUserRequisiteDTO requisiteDTO) {
		this.requisiteDTO = requisiteDTO;
	}

	public void setMyLocation(LatLng location) {
		if (location.latitude == 0 && location.longitude == 0) {
			return;
		}
		// 保存
		SharedPreferencesUtil.putValue(getApplicationContext(), kDATA_LAT,
				(float) location.latitude);
		SharedPreferencesUtil.putValue(getApplicationContext(), kDATA_LON,
				(float) location.longitude);
		this.myLocation = location;
	}

	public LatLng getMyLocation() {
		// 如果是当前位置为null, 拿最后一次保存的位置
		if (myLocation == null) {
			double lat = SharedPreferencesUtil.getValue(
					getApplicationContext(), kDATA_LAT, 0f);
			double lon = SharedPreferencesUtil.getValue(
					getApplicationContext(), kDATA_LON, 0f);
			if (lat == 0 || lon == 0) {
				return new LatLng(22.276247, 113.583288);
			}
			return new LatLng(lat, lon);
		}
		return this.myLocation;
	}

	/**
	 * 退出应用
	 */
	public void exit() {
		// 重置Application 的数据
		myLocation = null;
		requisiteDTO = null;
		order_num = null;
	}

	public AppEverimentArgs getEverimentArgs() {
		return new AppEverimentArgs(this);
	}

	public boolean isExtension_flag() {
		return extension_flag;
	}

	public void setExtension_flag(boolean extension_flag) {
		this.extension_flag = extension_flag;
	}

	public JobUserResumeMiddleDTO getJobUserResumeMiddleDTO() {
		return jobUserResumeMiddleDTO;
	}

	public void setJobUserResumeMiddleDTO(
			JobUserResumeMiddleDTO jobUserResumeMiddleDTO) {
		this.jobUserResumeMiddleDTO = jobUserResumeMiddleDTO;
	}
	
}
