package com.zcdh.mobile.app.maps.bmap;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * 定位
 * @author yangjiannan
 *
 */
public class MyBLocationCient {

	/**
	 * 定位
	 */
	LocationClient locationClient;
	
	Context context;
	
	BDLocationListener locationListenner;
	
	public MyBLocationCient(Context context, BDLocationListener bdLocationListener){
		this.context = context;
		this.locationListenner = bdLocationListener;
		initLocationClient();
	}
	
	/**
	 * 初始化设置定位
	 */
	void initLocationClient() {
		locationClient = new LocationClient(context); // 声明LocationClient类
		locationClient.registerLocationListener(locationListenner); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		//option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		//option.disableCache(true);// 禁止启用缓存定位
		//option.setPoiNumber(5); // 最多返回POI个数
		//option.setPoiDistance(1000); // poi查询距离
		//option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
		locationClient.setLocOption(option);
	}
	
	/**
	 * 发起定位请求
	 */
	public void requestLocation() {
		if (this.locationClient != null){
			locationClient.start();
			this.locationClient.requestLocation();
		}
	}


	public void stop(){
		locationClient.stop();
	}
}
