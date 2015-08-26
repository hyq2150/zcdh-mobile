/**
 * 
 * @author jeason, 2014-7-18 上午9:13:14
 */
package com.zcdh.mobile.app.activities.detail;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteStep;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.maps.bmap.MyBLocationCient;
import com.zcdh.mobile.app.maps.bmap.MyBMap;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.StringUtils;

/**
 * @author jeason, 2014-7-18 上午9:13:14
 * 公司地址导航页
 * 计算当前位置到达公司的出行路线
 */
@EActivity(R.layout.navigator)
public class NavigatorActivity extends BaseActivity implements OnMapLoadedCallback, OnMapStatusChangeListener, OnMarkerClickListener, BDLocationListener, OnGetRoutePlanResultListener {

	private MyBMap map;

	private BaiduMap baiduMap;

	private MyBLocationCient myBlocation;

	@ViewById(R.id.mapContainer)
	LinearLayout mapContainer;
	private PoiSearch poiSearch;
	private RoutePlanSearch mSearch;

	@ViewById(R.id.routeDetail)
	LinearLayout routeDetail;

	@ViewById(R.id.lvDetail)
	ListView lvDetail;

//	公交路线 驾车路线 步行路线方案
	private List<TransitRouteLine> transitRouteLines;
	private List<DrivingRouteLine> drivingRouteLines;
	private List<WalkingRouteLine> walkingRouteLines;

	@ViewById(R.id.btn_transit)
	ImageButton btn_transit;

	@ViewById(R.id.btn_driving)
	ImageButton btn_driving;

	@ViewById(R.id.btn_walk)
	ImageButton btn_walking;

	private int currentLineIndex = 0;

	private EmptyTipView emptyView;

	@Extra
	double dest_latitude;

	@Extra
	double dest_longtitude;

	private String city_name = "北京";

	private PlanNode destNode;

	private PlanNode startNode;

	private View detailView;

	private int way = 1;
	private ProcessDialog loading;

	private RouteStepAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		emptyView = new EmptyTipView(this);
		adapter = new RouteStepAdapter();
		loading = new ProcessDialog(this);
		myBlocation = new MyBLocationCient(this, this);
		getSupportActionBar().hide();
	}
	
	protected void onDestory() {
		loading =null;
		super.onDestroy();
	}

	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		public void onGetPoiResult(PoiResult result) {
			// 获取POI检索结果
		}

		public void onGetPoiDetailResult(PoiDetailResult result) {
			// 获取Place详情页检索结果
		}
	};

	@AfterViews
	void afterViews() {

		map = MyBMap.initMap(this);
		mSearch = RoutePlanSearch.newInstance();
		mapContainer.addView(map.getMapView(), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

		// 地图操作
		baiduMap = map.getBaiduMap();
		baiduMap.setOnMapLoadedCallback(this);
		baiduMap.setOnMapStatusChangeListener(this);
		baiduMap.setOnMarkerClickListener(this);

		// 初始化poi 查询
		poiSearch = PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(poiListener);
		mSearch.setOnGetRoutePlanResultListener(this);
		if (dest_latitude != 0 && dest_longtitude != 0) {
			destNode = PlanNode.withLocation(new LatLng(dest_latitude, dest_longtitude));
			// destNode = PlanNode.withCityNameAndPlaceName("珠海", "百合花园");
		}
		lvDetail.setEmptyView(emptyView);
		lvDetail.setAdapter(adapter);
		
		//
		RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) routeDetail.getLayoutParams();
		rl.height = getResources().getDisplayMetrics().heightPixels/2;
		routeDetail.setLayoutParams(rl);
		
		if (detailView == null) {
			holderLine = new DetailViewHolder();
			detailView = LayoutInflater.from(this).inflate(R.layout.transportation_detail_header, null);
			holderLine.imgIcon = (ImageView) detailView.findViewById(R.id.imgIcon);
			holderLine.detailText = (TextView) detailView.findViewById(R.id.detailText);
			holderLine.accessoryImg = (ImageView) detailView.findViewById(R.id.accessoryImg);
			holderLine.imgIcon.setVisibility(View.GONE);
			routeDetail.addView(detailView, 0);
			detailView.setOnClickListener(solutionShifter);
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

	@Override
	public void onMapStatusChange(MapStatus arg0) {

	}

	@Override
	public void onMapStatusChangeFinish(MapStatus arg0) {

	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) {

	}

	@Override
	public void onMapLoaded() {
		myBlocation.requestLocation();
	}

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		startNode = PlanNode.withLocation(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
		if(!TextUtils.isEmpty(arg0.getCity())){
			city_name = arg0.getCity().replace("市", "");
		}
		// reCalculateRoute();
		byWalking();
	}

	/**
	 * 
	 * @author jeason, 2014-7-18 下午1:39:54
	 */
	private void reCalculateRoute() {
		currentLineIndex = 0;
		if (startNode != null && destNode != null) {
			loading.show();
			switch (way) {
			case 1:
				mSearch.transitSearch((new TransitRoutePlanOption()).from(startNode).city(city_name).to(destNode));
				break;
			case 2:
				mSearch.drivingSearch((new DrivingRoutePlanOption()).from(startNode).to(destNode));
				break;
			case 3:
				mSearch.walkingSearch((new WalkingRoutePlanOption()).from(startNode).to(destNode));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		loading.dismiss();
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			adapter.clear();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			adapter.clear();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			drivingRouteLines = result.getRouteLines();
			
			setDrivingRouteLine();
		}
	}

	/**
	 * 
	 * @author jeason, 2014-7-18 下午4:38:35
	 */
	private void setDrivingRouteLine() {
		baiduMap.clear();
		if (currentLineIndex >= drivingRouteLines.size()) {
			currentLineIndex = 0;
		}
		try {
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(drivingRouteLines.get(currentLineIndex));
			overlay.addToMap();
			overlay.zoomToSpan();
			setCurrentDetail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if(loading!=null){
			loading.dismiss();
		}
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			adapter.clear();
		}
		if (result.error == SearchResult.ERRORNO.ST_EN_TOO_NEAR) {
			Toast.makeText(this, "距离太短,请选择步行", Toast.LENGTH_SHORT).show();
			//byWalking();
			adapter.clear();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}

		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			transitRouteLines = result.getRouteLines();
			setTransitRouteLine();
		}
	}

	/**
	 * 
	 * @author jeason, 2014-7-18 下午4:39:50
	 */
	private void setTransitRouteLine() {
		if(transitRouteLines!=null){
			baiduMap.clear();
			if (currentLineIndex >= transitRouteLines.size()) {
				currentLineIndex = 0;
			}
			try {
				TransitRouteOverlay overlay = new TransitRouteOverlay(baiduMap);
				baiduMap.setOnMarkerClickListener(overlay);
				overlay.setData(transitRouteLines.get(currentLineIndex));
				overlay.addToMap();
				overlay.zoomToSpan();
				setCurrentDetail();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if(loading!=null){
			loading.dismiss();
		}
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			adapter.clear();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			adapter.clear();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			walkingRouteLines = result.getRouteLines();

			setWalkingRouteLine();
		}
	}

	/**
	 * 
	 * @author jeason, 2014-7-18 下午4:41:39
	 */
	private void setWalkingRouteLine() {
		baiduMap.clear();
		if (walkingRouteLines!=null && currentLineIndex >= walkingRouteLines.size()) {
			currentLineIndex = 0;
		}
		try {
			WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(walkingRouteLines.get(currentLineIndex));
			overlay.addToMap();
			overlay.zoomToSpan();
			setCurrentDetail();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Click(R.id.btn_transit)
	void byMassTransition() {
		btn_driving.setImageResource(R.drawable.btn_driving);
		btn_walking.setImageResource(R.drawable.btn_walking);
		btn_transit.setImageResource(R.drawable.buslinedown_50x50);
		way = 1;
		reCalculateRoute();
	}

	@Click(R.id.btn_driving)
	void byDriving() {
		btn_driving.setImageResource(R.drawable.carlinedown_50x50);
		btn_walking.setImageResource(R.drawable.btn_walking);
		btn_transit.setImageResource(R.drawable.btn_transit);
		way = 2;
		reCalculateRoute();

	}

	@Click(R.id.btn_walk)
	void byWalking() {
		btn_driving.setImageResource(R.drawable.btn_driving);
		btn_walking.setImageResource(R.drawable.footlinedown_50x50);
		btn_transit.setImageResource(R.drawable.btn_transit);
		way = 3;
		reCalculateRoute();
	}

	class DetailViewHolder {
		ImageView imgIcon;
		TextView detailText;
		ImageView accessoryImg;
	}

	DetailViewHolder holderLine;

	private OnClickListener solutionShifter = new OnClickListener() {

		@Override
		public void onClick(View v) {
			currentLineIndex++;
			switch (way) {
			case 1:

				setTransitRouteLine();
				break;
			case 2:

				setDrivingRouteLine();

				break;
			case 3:

				setWalkingRouteLine();

				break;

			default:
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	void setCurrentDetail() {
		
		RouteLine<?> line = null;
		String lineIndex = "";
		switch (way) {
		case 1:
			if (transitRouteLines != null && !transitRouteLines.isEmpty()) {
				if (currentLineIndex >= transitRouteLines.size()) {
					currentLineIndex = 0;
				}
				line = transitRouteLines.get(currentLineIndex);
				lineIndex = "(" + (currentLineIndex + 1) +"/" + transitRouteLines.size() +")";
			}
			break;
		case 2:
			if (drivingRouteLines != null && !drivingRouteLines.isEmpty()) {
				if (currentLineIndex >= drivingRouteLines.size()) {
					currentLineIndex = 0;
				}
				line = drivingRouteLines.get(currentLineIndex);
//				List<DrivingStep> steps = line.getAllStep();
				lineIndex ="(" + (currentLineIndex + 1) +"/" + drivingRouteLines.size() +")";
			}
			break;
		case 3:
			if (walkingRouteLines != null && !walkingRouteLines.isEmpty()) {
				if (currentLineIndex >= walkingRouteLines.size()) {
					currentLineIndex = 0;
				}
				line = walkingRouteLines.get(currentLineIndex);
				lineIndex = "(" + (currentLineIndex + 1) +"/" + walkingRouteLines.size() +")";

			}
			break;
		default:
			break;
		}
		
		TextView nextLineText = (TextView) detailView.findViewById(R.id.nextLineText);
		nextLineText.setText(lineIndex +"下一方案");
		
		if(line!=null && line.getAllStep()!=null){
			int distanceTotal = 0; // 米
			int duraingTotal = 0; // 秒
			for (int i = 0; i < line.getAllStep().size(); i++) {
				if (line.getAllStep().get(i) instanceof DrivingStep) {
					DrivingStep ds = (DrivingStep) line.getAllStep().get(i);
					distanceTotal += ds.getDistance();
					duraingTotal += ds.getDuration();
				}
				if (line.getAllStep().get(i) instanceof TransitStep) {
					TransitStep ts = (TransitStep) line.getAllStep().get(i);
					distanceTotal += ts.getDistance();
					duraingTotal += ts.getDuration();
				}
				if (line.getAllStep().get(i) instanceof WalkingStep) {
					WalkingStep ws = (WalkingStep) line.getAllStep().get(i);
					distanceTotal += ws.getDistance();
					duraingTotal += ws.getDuration();
				}
			}
			String lineDescription = "";
			if(distanceTotal>0){
				
				if(distanceTotal>1000){
					lineDescription += distanceTotal/1000 + "千米 ";
				}else{
					lineDescription += distanceTotal + "米 ";
				}
			}
			if(duraingTotal>0){
				
				int m = 0;
				int h = 0;
				if(duraingTotal%3600==0){
					h = duraingTotal/3600; 
				}else{
					h = (duraingTotal-(duraingTotal%3600))/3600;
				}
				
				double min = (duraingTotal%3600)/60;
				m = (int) Math.ceil(min);
				String time = "";
				if(h>0){
					time += h +"小时";
				} 
				if(m>0){
					time += m +"分钟";
				}
				if(!TextUtils.isEmpty(time)){
					lineDescription += "用时"+time;
				}
				//lineDescription += sdf.format(date);
			}
			if(!StringUtils.isBlank(lineDescription)){
				holderLine.detailText.setText(lineDescription);
			}
		}
		
		if (adapter != null && line != null) {
			adapter.updaterItems((List<RouteStep>) line.getAllStep());
		}
	}

	@Click(R.id.btn_back)
	void onBack() {
		finish();
	}

	private class RouteStepAdapter extends BaseAdapter {
		List<RouteStep> mItems = new ArrayList<RouteStep>();

		public void clear(){
			mItems.clear();
			holderLine.detailText.setText("未找到结果");
			TextView nextLineText = (TextView) detailView.findViewById(R.id.nextLineText);
			nextLineText.setText("");
			//routeDetail.setVisibility(View.GONE);
			notifyDataSetChanged();
		}
		
		public void updaterItems(List<RouteStep> items) {
			mItems.clear();
			mItems.addAll(items);
			notifyDataSetChanged();
			//routeDetail.setVisibility(View.VISIBLE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DetailViewHolder holder;
			if (convertView == null) {
				holder = new DetailViewHolder();
				convertView = LayoutInflater.from(NavigatorActivity.this).inflate(R.layout.transportation_detail, null);
				holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
				holder.detailText = (TextView) convertView.findViewById(R.id.detailText);
				convertView.setTag(holder);
			} else {
				holder = (DetailViewHolder) convertView.getTag();
			}
//			String type;
			if (getItem(position) instanceof DrivingStep) {

				holder.imgIcon.setImageResource(R.drawable.carlinedown_50x50);
				holder.detailText.setText(((DrivingStep) getItem(position)).getInstructions());
			}
			if (getItem(position) instanceof TransitStep) {
				
//				int distance = ((TransitStep) getItem(position)).getDistance();
//				int during = ((TransitStep) getItem(position)).getDuration();
				//Toast.makeText(getApplicationContext(), "距离："+distance +" 耗时：" +during, Toast.LENGTH_SHORT).show();
				holder.detailText.setText(((TransitStep) getItem(position)).getInstructions());
				switch (((TransitStep) getItem(position)).getStepType()) {
				case BUSLINE:
					holder.imgIcon.setImageResource(R.drawable.buslinedown_50x50);

					break;
				case SUBWAY:
					holder.imgIcon.setImageResource(R.drawable.buslinedown_50x50);

					break;
				case WAKLING:
					holder.imgIcon.setImageResource(R.drawable.footlinedown_50x50);

					break;

				default:
					break;
				}
			}
			if (getItem(position) instanceof WalkingStep) {
				holder.imgIcon.setImageResource(R.drawable.footlinedown_50x50);
				holder.detailText.setText(((WalkingStep) getItem(position)).getInstructions());
			}

			return convertView;
		}
	}
}
