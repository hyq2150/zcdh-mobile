package com.zcdh.mobile.app.maps.bmap;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MyLocationConfigeration;
//import com.baidu.mapapi.map.MyLocationConfigeration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.api.model.LbsParam;
import com.zcdh.mobile.app.ZcdhApplication;

/**
 * 
 * @author yangjiannan
 * 
 */
public class MyBMap {

	private MapView mapView;

	private BaiduMap baiduMap;

	private Context context;

	public static final int defaultZoom = 14;

	private static final String TAG = MyBMap.class.getSimpleName();

	/**
	 * 获取加载职位的范围，以但前手机屏幕为基准，向周围扩大屏幕的1/2倍
	 */
	private LatLngBounds maxLatBounds;

	public static MyBMap initMap(Context context) {
		MyBMap _map = new MyBMap(context);
		_map.initMap();
		return _map;
	}

	private MyBMap(Context context) {
		this.context = context;
	}

	private void initMap() {
		// 1_ 初始化地图
		BaiduMapOptions options = new BaiduMapOptions();
		options.zoomControlsEnabled(false);
		options.scaleControlEnabled(false);
		options.mapStatus(new MapStatus.Builder().target(
				ZcdhApplication.getInstance().getMyLocation()).build());
		this.mapView = new MapView(this.context, options);

		// 2_ 初始化地图操作类
		this.baiduMap = mapView.getMap();
	
		// 3_ 地图状态
		MapStatus.Builder mb = new MapStatus.Builder(baiduMap.getMapStatus());
		mb.zoom(defaultZoom);
		this.baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mb
				.build()));
		// 4_ 设置定位图层
		// BitmapDescriptor myLocIcon =
		// BitmapDescriptorFactory.fromResource(R.drawable.ic_iam3);
		//
		// MyLocationConfigeration locConfig = new
		// MyLocationConfigeration(LocationMode.NORMAL, true, myLocIcon);
		// this.baiduMap.setMyLocationConfigeration(locConfig);
		// this.baiduMap.setMyLocationEnabled(true);
	}

	public BaiduMap getBaiduMap() {
		return this.baiduMap;
	}

	public MapView getMapView() {
		return this.mapView;
	}

	/**
	 * 获取加载职位的范围，以但前手机屏幕为基准，向周围扩大屏幕的1/2倍
	 */
	public LatLngBounds buildMaxBound() {

		// 获取屏幕四个角的屏幕坐标点， 换算成在地图中的经纬度坐标点
		List<LatLng> latlngInScreen = getLalngBoundForExtend(0.5);

		// 设置并保存最大取数范围
		LatLngBounds.Builder llb = new LatLngBounds.Builder();
		llb.include(latlngInScreen.get(1));
		llb.include(latlngInScreen.get(2));
		maxLatBounds = llb.build();
		Log.i("maxLatBounds:", maxLatBounds.northeast + "");
		return maxLatBounds;
	}

	public List<LbsParam> getScreenBound() {
		return getLbsParam(getLalngBoundForScreen());
	}

	private List<LbsParam> getLbsParam(List<LatLng> latlngInScreen) {
		if (latlngInScreen == null)
			return null;
		if (latlngInScreen.size() == 0)
			return null;
		List<LbsParam> lbsparams = new ArrayList<LbsParam>();
		try {
			LatLng g1 = latlngInScreen.get(0);
			LbsParam lbs1 = new LbsParam();
			lbs1.setLat(g1.latitude);
			lbs1.setLon(g1.longitude);

			LatLng g2 = latlngInScreen.get(1);
			LbsParam lbs2 = new LbsParam();
			lbs2.setLat(g2.latitude);
			lbs2.setLon(g2.longitude);

			LatLng g3 = latlngInScreen.get(2);
			LbsParam lbs3 = new LbsParam();
			lbs3.setLat(g3.latitude);
			lbs3.setLon(g3.longitude);

			LatLng g4 = latlngInScreen.get(3);
			LbsParam lbs4 = new LbsParam();
			lbs4.setLat(g4.latitude);
			lbs4.setLon(g4.longitude);

			lbsparams.add(lbs1);
			lbsparams.add(lbs2);
			lbsparams.add(lbs3);
			lbsparams.add(lbs4);
			String msg = "p1:(" + g1.latitude + "," + g1.longitude + ")\n";
			msg += "p2:(" + g2.latitude + "," + g2.longitude + ")\n";
			msg += "p3:(" + g3.latitude + "," + g3.longitude + ")\n";
			msg += "p4:(" + g4.latitude + "," + g4.longitude + ")\n";
			Log.i("point:", msg);
		} catch (ZcdhException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lbsparams;
	}

	/**
	 * 屏幕可视地图区域是否移除最大取数范围
	 * 
	 * @return
	 */
	public boolean hasOutOfMaxBound() {
		if (maxLatBounds == null) {
			maxLatBounds = buildMaxBound();
		}
		List<LatLng> boundOfScreen = getLalngBoundForScreen();
		boolean hasOut = false;
		for (LatLng latLng : boundOfScreen) {

			if (!maxLatBounds.contains(latLng)) {
				hasOut = true;
				// 如果移出了最大取数范围，重新设置
				maxLatBounds = buildMaxBound();
				break;
			}
		}
		return hasOut;
	}

	private List<LatLng> getLalngBoundForScreen() {
		return getLalngBoundForExtend(0);
	}

	private List<LatLng> getLalngBoundForExtend(double extend) {
		int top = this.getMapView().getTop();
		int left = this.getMapView().getLeft();
		int mWidth = this.getMapView().getWidth();
		int mHeight = this.getMapView().getHeight();
		if (baiduMap.getProjection() == null) {
			return null;
		}
		// 左上角
		LatLng g1 = baiduMap.getProjection().fromScreenLocation(
				new Point(left - (int) (mWidth * extend), top
						- (int) (mHeight * extend)));
		// 左下角
		LatLng g2 = baiduMap.getProjection().fromScreenLocation(
				new Point(left - (int) (mWidth * extend), top + mHeight
						+ (int) (mHeight * extend)));
		// 右上角
		LatLng g3 = baiduMap.getProjection().fromScreenLocation(
				new Point(left + mWidth + (int) (mWidth * extend), top
						- (int) (mHeight * extend)));
		// 右下角
		LatLng g4 = baiduMap.getProjection().fromScreenLocation(
				new Point(left + mWidth + (int) (mWidth * extend), top
						+ mHeight + (int) (mHeight * extend)));
		List<LatLng> latLngInscreen = new ArrayList<LatLng>();
		latLngInscreen.add(g1);
		latLngInscreen.add(g2);
		latLngInscreen.add(g3);
		latLngInscreen.add(g4);
		Log.i(TAG, "getit");
		return latLngInscreen;
	}

}
