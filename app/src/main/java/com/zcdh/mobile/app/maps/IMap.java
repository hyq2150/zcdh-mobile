package com.zcdh.mobile.app.maps;

import com.baidu.mapapi.map.BaiduMap;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * 定义地图接口
 * 提供曝露给activity 的地图接口
 * @author yangjiannan
 *
 */
public interface IMap {

	/**
	 * 获得地图控制器
	 * @return
	 */
	BaiduMap getMap();
	
	/**
	 * 返回地图视图
	 * @return View
	 */
	View getMapView();
	
	/** ========= 地图状态操作方法 ======== */
	
	/**
	 * 回收资源，销毁地图
	 */
	void destroy();
	
	/**
	 * 唤醒地图
	 */
	void resume();
	
	/**
	 * 暂停题图
	 */
	void pause();
	
	/**
	 * 隐藏与显示放大和缩小控件
	 */
	void showZoopControl(boolean show);
	
	/** ======== 地图操作接口方法============ */
	void snapshot(final ISnapshotCallBack iSnapshotCallBack);
	
}
