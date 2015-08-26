package com.zcdh.mobile.app.activities.nearby;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.R.bool;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.LbsParam;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.maps.bmap.MyBLocationCient;
import com.zcdh.mobile.app.maps.bmap.MyBMap;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.SystemServicesUtils;
/**
 * 
 * @author yangjiannan
 * 周边 poi 查询
 */
@EActivity(R.layout.activity_nearby)
public class PoiSearchActivity extends BaseActivity implements BDLocationListener, 
OnGetPoiSearchResultListener, OnMapLoadedCallback, OnMapStatusChangeListener, OnMarkerClickListener {

	@ViewById(R.id.mapContainer)
	RelativeLayout mapContainer;
	
	private final static int POI_BUS = 0;
	private final static int POI_FOOD = 1;
	private final static int POI_BANK= 2;
	private final static int POI_MARKET = 3;
	private final static int POI_PRIVILEGE = 4;
	
	/**
	 * 公交
	 */
	@ViewById(R.id.busBtn)
	LinearLayout busBtn;
	
	/**
	 * 美食
	 */
	@ViewById(R.id.foodBtn)
	LinearLayout foodBtn;
	
	/**
	 * 银行
	 */
	@ViewById(R.id.bankBtn)
	LinearLayout bankBtn;
	
	/**
	 * 超市
	 */
	@ViewById(R.id.marketBtn)
	LinearLayout marketBtn;
	
	/**
	 * 优惠
	 */
	@ViewById(R.id.privilegeBtn)
	LinearLayout privilegeBtn;
	
	
	MyBMap map ;
	
	/**
	 * 地图操作类
	 */
	BaiduMap baiduMap;
	
	/**
	 * 定位
	 */
	MyBLocationCient myBLocationCient;
	
	
	/**
	 * 该地理范围东北坐标
	 */
	LatLng northeast;
	/**
	 * 该地理范围西南坐标
	 */
	LatLng southwest;
	
	/**
	 * poi 搜索范围
	 */
	PoiBoundSearchOption poiBoundOptions;
	
	/**
	 * poi 查询
	 */
	PoiSearch poiSearch;
	
	/**
	 * 当前poi 查询
	 */
	int current_poi = POI_BUS;

	//记录地图是否加载完成
	private boolean MapLoaded;
	
	/**
	 * 当前位置中心点
	 */
	@Extra
	double center_lat;
	@Extra
	double center_lon;
	
	
	@AfterViews
	void bindViews(){
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), "周边");
		
//		if(getIntent().getExtras()!=null)
//			current_poi = getIntent().getIntExtra("index", 0);
		
		
		map = MyBMap.initMap(this);
		RelativeLayout.LayoutParams mapRlLP = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mapContainer.addView(map.getMapView(), mapRlLP);
		
		
		//地图操作
		baiduMap = map.getBaiduMap();
		baiduMap.setOnMapLoadedCallback(this);
		baiduMap.setOnMapStatusChangeListener(this);
		baiduMap.setOnMarkerClickListener(this);
		//初始化定位
		
		myBLocationCient = new MyBLocationCient(this, this);
		if(center_lat!=0 && center_lon!=0){
			LatLng center = new LatLng(center_lat, center_lon);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(center);
			baiduMap.setMapStatus(msu);
		}else{
			//myBLocationCient.requestLocation();
		}
		
		//初始化poi 查询
		poiSearch = PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(this);
	}

	/**
	 * 发起poi搜索
	 * 1)设定范围
	 * 2)关键字
	 */
	void searchPoi(String keyword){
		if(poiBoundOptions==null){
			poiBoundOptions = new PoiBoundSearchOption();
		}
		List<LbsParam> latlons = map.getScreenBound();
		LatLng westsouth = new LatLng(latlons.get(1).getLat(), latlons.get(1).getLon());//获取西南坐标
		LatLng northeast = new LatLng(latlons.get(2).getLat(), latlons.get(2).getLon());//获取东北坐标
		LatLngBounds.Builder lbb = new LatLngBounds.Builder();
		lbb.include(westsouth);
		lbb.include(northeast);
		
		poiBoundOptions.bound(lbb.build());
		
//		Log.i("westsouth input:", westsouth.latitude+" , " +westsouth.longitude);
//		Log.i("northeast input:", northeast.latitude+" , " +northeast.longitude);
//		Log.i("westsouth output:", lbb.build().southwest.latitude+" , " +lbb.build().southwest.longitude);
//		Log.i("northeast output:", lbb.build().northeast.latitude+" , " +lbb.build().northeast.longitude);
		baiduMap.clear();
		poiBoundOptions.keyword(keyword);
		poiSearch.searchInBound(poiBoundOptions);
		showMyLocationPoint();
	}
	
	void searchPoi(){
		String keyword = null;
		switch (current_poi) {
		case POI_BUS:
			keyword = "公交站";
			break;
		case POI_FOOD:
			keyword ="美食";
			break;
		case POI_BANK:
			keyword = "银行";
			break;
		case POI_MARKET:
			keyword = "超市";
			break;
		case POI_PRIVILEGE:
			keyword = "优惠";
			break;
		}
		setBtnsStyle();
		searchPoi(keyword);
	}
	
	/**
	 * 查看公交站
	 */
	@Click(R.id.busBtn)
	void onBusBtn(){
		current_poi = POI_BUS;
		searchPoi();
	}
	
	/**
	 *  查看附近美食
	 */
	@Click(R.id.foodBtn)
	void onFoodBtn(){
		current_poi = POI_FOOD;
		searchPoi();
	}
	
	/**
	 * 查看附近银行
	 */
	@Click(R.id.bankBtn)
	void onBankBtn(){
		current_poi = POI_BANK;
		searchPoi();
	}
	
	/**
	 * 查看附近超市
	 */
	@Click(R.id.marketBtn)
	void onMarketBtn(){
		current_poi = POI_MARKET;
		searchPoi();
	}
	
	/**
	 * 查看附近优惠
	 */
	@Click(R.id.privilegeBtn)
	void onPrivilegeBtn(){
		current_poi = POI_PRIVILEGE;
		searchPoi();
	}
	
	void setBtnsStyle(){
		switch (current_poi) {
		case POI_BUS:
			busBtn.findViewById(R.id.busImgBtn).setBackgroundResource(R.drawable.bus_active);
			foodBtn.findViewById(R.id.foodImgBtn).setBackgroundResource(R.drawable.food_selector);
			bankBtn.findViewById(R.id.bankImgBtn).setBackgroundResource(R.drawable.bank_selector);
			marketBtn.findViewById(R.id.marketImgBtn).setBackgroundResource(R.drawable.market_selector);
			privilegeBtn.findViewById(R.id.privilegeImgBtn).setBackgroundResource(R.drawable.privilege_selector);
			break;

		case POI_BANK:
			busBtn.findViewById(R.id.busImgBtn).setBackgroundResource(R.drawable.bus_selector);
			foodBtn.findViewById(R.id.foodImgBtn).setBackgroundResource(R.drawable.food_selector);
			bankBtn.findViewById(R.id.bankImgBtn).setBackgroundResource(R.drawable.bank_active);
			marketBtn.findViewById(R.id.marketImgBtn).setBackgroundResource(R.drawable.market_selector);
			privilegeBtn.findViewById(R.id.privilegeImgBtn).setBackgroundResource(R.drawable.privilege_selector);
			break;
		case POI_FOOD:
			busBtn.findViewById(R.id.busImgBtn).setBackgroundResource(R.drawable.bus_selector);
			foodBtn.findViewById(R.id.foodImgBtn).setBackgroundResource(R.drawable.food_acive);
			bankBtn.findViewById(R.id.bankImgBtn).setBackgroundResource(R.drawable.bank_selector);
			marketBtn.findViewById(R.id.marketImgBtn).setBackgroundResource(R.drawable.market_selector);
			privilegeBtn.findViewById(R.id.privilegeImgBtn).setBackgroundResource(R.drawable.privilege_selector);
			break;
		case POI_MARKET:
			busBtn.findViewById(R.id.busImgBtn).setBackgroundResource(R.drawable.bus_selector);
			foodBtn.findViewById(R.id.foodImgBtn).setBackgroundResource(R.drawable.food_selector);
			bankBtn.findViewById(R.id.bankImgBtn).setBackgroundResource(R.drawable.bank_selector);
			marketBtn.findViewById(R.id.marketImgBtn).setBackgroundResource(R.drawable.market_active);
			privilegeBtn.findViewById(R.id.privilegeImgBtn).setBackgroundResource(R.drawable.privilege_selector);
			break;
		case POI_PRIVILEGE:
			busBtn.findViewById(R.id.busImgBtn).setBackgroundResource(R.drawable.bus_selector);
			foodBtn.findViewById(R.id.foodImgBtn).setBackgroundResource(R.drawable.food_selector);
			bankBtn.findViewById(R.id.bankImgBtn).setBackgroundResource(R.drawable.bank_selector);
			marketBtn.findViewById(R.id.marketImgBtn).setBackgroundResource(R.drawable.market_selector);
			privilegeBtn.findViewById(R.id.privilegeImgBtn).setBackgroundResource(R.drawable.privilege_active);
			break;
		}
	}
	
	/* =================  地图事件 ============== */
	@Override
	public void onMapLoaded() {
		MapLoaded = true;
		searchPoi();
		showMyLocationPoint();
	}

	@Override
	public void onMapStatusChange(MapStatus arg0) {
		
	}

	@Override
	public void onMapStatusChangeFinish(MapStatus arg0) {
		if(MapLoaded){
			
			searchPoi();
			
		}
	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) {
		
	}
	

	@Override
	public boolean onMarkerClick(Marker marker) {
		Overlay overlay = marker;
		Bundle bundle = overlay.getExtraInfo();
		if(bundle!=null){
			boolean hasDetailCenter = bundle.getBoolean("hasDetail");
			String uid = bundle.getString("uid");
			String detail = bundle.getString("detail");
			if(hasDetailCenter){
				poiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(uid));
			}else{
				Toast.makeText(this, detail, Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}
	
	/*=================== 定位回调函数 ================ */
	@Override
	public void onReceiveLocation(BDLocation location) {
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		
		MapStatus.Builder mb = new MapStatus.Builder();
		mb.target(latLng);
		
		baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mb.build()));
		
	}


//	@Override
	public void onReceivePoi(BDLocation location) {
		
	}


	/* ================ POI 查询回调函数 =============== */
	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(this, "成功，查看详情页面", Toast.LENGTH_SHORT)
					.show();
		}
	}


	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			
//			PoiOverlay overlay = new MyPoiOverlay(baiduMap);
//			baiduMap.setOnMarkerClickListener(overlay);
//			overlay.setData(result);
//			overlay.addToMap();
			
			//overlay.zoomToSpan();
			
			//构建Marker图标
			int icon = -1;
			switch (current_poi) {
			case POI_BUS:
				icon = R.drawable.ic_bus_normal;
				break;
			case POI_FOOD:
				icon =R.drawable.ic_food_normal;
				break;
			case POI_BANK:
				icon = R.drawable.ic_bank_normal;
				break;
			case POI_MARKET:
				icon = R.drawable.ic_shop_normal;
				break;
			case POI_PRIVILEGE:
				icon = R.drawable.ic_discount_normal;
				break;
			}
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(icon);
			
			List<PoiInfo> poiLists = result.getAllPoi();
			for (int i = 0; i < poiLists.size(); i++) {
				PoiInfo poiInfo = poiLists.get(i);
				
				//定义Maker坐标点
				LatLng point = poiInfo.location;
				//构建MarkerOption，用于在地图上添加Marker
				OverlayOptions option = new MarkerOptions()
				.position(point)
				.icon(bitmap);
				//在地图上添加Marker，并显示
				Bundle bundle = new Bundle();
				bundle.putBoolean("hasDetail", poiInfo.hasCaterDetails);
				bundle.putString("uid", poiInfo.uid);
				bundle.putString("detail", poiInfo.name);
				baiduMap.addOverlay(option).setExtraInfo(bundle);
			}
			

			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(this, strInfo, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 显示我的位置
	 */
	void showMyLocationPoint(){
		LatLng myLocationLatlng = ZcdhApplication.getInstance().getMyLocation();
		if(myLocationLatlng!=null){
			
			BitmapDescriptor bitmapDesc = BitmapDescriptorFactory.fromResource(R.drawable.ic_iam);
			
			MarkerOptions myLocation = new MarkerOptions()
			.position(myLocationLatlng)
			.icon(bitmapDesc)
			.anchor(MapViewLayoutParams.ALIGN_CENTER_VERTICAL,
					MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL);
			
			baiduMap.addOverlay(myLocation);
		}
	}


//	/**
//	 *  自定义 Overlay
//	 * @author yangjiannan
//	 *
//	 */
//	private class MyPoiOverlay extends PoiOverlay {
//
//		public MyPoiOverlay(BaiduMap baiduMap) {
//			super(baiduMap);
//		}
//
//		@Override
//		public boolean onPoiClick(int index) {
//			super.onPoiClick(index);
//			PoiInfo poi = getPoiResult().getAllPoi().get(index);
//			if (poi.hasCaterDetails) {
//				poiSearch.searchPoiDetail((new PoiDetailSearchOption())
//						.poiUid(poi.uid));
//			}
//			return true;
//		}
//	}



	
}
