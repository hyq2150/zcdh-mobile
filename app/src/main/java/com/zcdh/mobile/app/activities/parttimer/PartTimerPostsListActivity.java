package com.zcdh.mobile.app.activities.parttimer;

import com.baidu.mapapi.model.LatLng;
import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.JobObjectiveAreaDTO;
import com.zcdh.mobile.api.model.SearchConditionDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.app.activities.nearby.SearchResultAdapter;
import com.zcdh.mobile.app.activities.search.AdvancedSearchActivity;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 兼职职位列表
 * @author YJN
 *
 */
@EActivity(R.layout.activity_parttimer_posts)
public class PartTimerPostsListActivity extends BaseActivity implements Subscriber, RequestListener, IXListViewListener, OnItemClickListener, DataLoadInterface {
	
	private static final String TAG = PartTimerPostsListActivity.class.getSimpleName();

	private String kREQ_ID_fromMapToAdvancedSearchList;
	
	private IRpcNearByService nearbyService;
	
	@ViewById(R.id.listbarRl)
	RelativeLayout listbarRl;
	
	/**
	 * 顶部显示的职位数
	 */
	@ViewById(R.id.resultDescriptionTxt)
	TextView resultDescriptionTxt; 

	/**
	 * 筛选按钮
	 */
	@ViewById(R.id.filterBtn)
	Button filterBtn;
	
	/**
	 * 清空高级搜索条件
	 */
	@ViewById(R.id.clearnAdvanceConditionBtn)
	ImageButton clearnAdvanceConditionBtn;

	/**
	 * 职位列表
	 */
	@ViewById(R.id.postListView)
	XListView postListView;
	
	SearchResultAdapter postAdapter;

	@ViewById(R.id.emptyView)
	EmptyTipView emptyTipView;
	
	/**
	 * 条件过滤
	 */
	private SearchConditionDTO searchConditionDTOForFilter = new SearchConditionDTO();

	 
	/**
	 * 高级搜索条件选择的值名称
	 */
	private HashMap<Integer, String> conditionValuesName = new HashMap<Integer, String>();
	
	private HashMap<String, JobObjectiveAreaDTO> selectedAreas = new HashMap<String, JobObjectiveAreaDTO>();
	
	private ArrayList<String> selectedWeek = new ArrayList<String>();
	
	private int currentPage = 1;
	
	private boolean isAdvance;
	
	/**
	 * 
	 */
	private SearchConditionDTO searchConditionDTO = new SearchConditionDTO();
	
	/**
	 * 地图在屏幕的范围
	 */
//	private List<LbsParam> screenBounds;
	
	/**
	 * 职位
	 */
	private ArrayList<JobEntPostDTO> posts = new ArrayList<JobEntPostDTO>();
	
	private boolean hasNextPage;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		MyEvents.register(this);
		
	}

	@AfterViews
	void bindViews(){
		String title = getIntent().getStringExtra("title");
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), title);
		nearbyService = RemoteServiceManager.getRemoteService(IRpcNearByService.class);
		searchConditionDTO.setPostPropertyCode(Constants.POST_PROPERTY_JIANZHI);
		if(!getIntent().getExtras().isEmpty()){ // 是显示 假期工(007.004)还是兼职 (007.002)
			String whichForSearch = getIntent().getExtras().getString("postPropertyCode");
			searchConditionDTO.setPostPropertyCode(whichForSearch);
		}
		
		postListView.setPullRefreshEnable(false);
		postListView.setPullLoadEnable(true);
		postListView.setAutoLoadEnable(false);
		postListView.setXListViewListener(this);
		postListView.setOnItemClickListener(this);
		postAdapter = new SearchResultAdapter(this);
		postListView.setAdapter(postAdapter);
		
		// 中附近地图得到地图在屏幕的范围
		MyEvents.post(Constants.kEVENT_GET_SCREEN_BOUND, 0);
	}
	
	/**
	 * 加载职位
	 */
	@Background
	void loadAdvanced(SearchConditionDTO conditions){
		if(conditions==null)conditions = new SearchConditionDTO();
		LatLng myLocation = ZcdhApplication.getInstance().getMyLocation();
		conditions.setLat(myLocation.latitude);
		conditions.setLon(myLocation.longitude);
		nearbyService
				.findSearchPostDTOByAdvancedSearch(getUserId(),
						conditions,
						currentPage, Constants.Page_SIZE)
				.identify(
						kREQ_ID_fromMapToAdvancedSearchList = RequestChannel
								.getChannelUniqueID(),
						this);
		currentPage++;
	}
	
	
	void showPosts(int total){
		String key = searchConditionDTOForFilter.getKeyWord();
		if (StringUtils.isBlank(key)) {
			key = searchConditionDTO.getKeyWord();// 关键字
		}
		if (StringUtils.isBlank(key)) {
			key = conditionValuesName.get(1); // 职位类别
		}
		String cidi = "";
		String city = "";
		if (selectedAreas!=null && selectedAreas.size()>0) {
			for(String k : selectedAreas.keySet()){
				city += selectedAreas.get(k).getName() + ",";
			}
//			city = city.substring(city.length()-1, city.length());
		} else {
			city = "全国";
/*			if(!isAdvance){
			}else{
				city = "全国";
			}
*/		}
		cidi += city;
		if(!TextUtils.isEmpty(key)){
			cidi += ","+ key;
		}
		cidi += total + " 个职位"; 
		Log.i(TAG, cidi +"");
		resultDescriptionTxt.setText(cidi);
	}
	
	@OnActivityResult(AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH)
	void onResultCondition(int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data.getExtras() != null) {
				searchConditionDTOForFilter = (SearchConditionDTO) data
					.getExtras().getSerializable(
							AdvancedSearchActivity.kDATA_CONDITIONS);
			conditionValuesName = (HashMap<Integer, String>) data.getExtras()
					.getSerializable(AdvancedSearchActivity.kDATA_VALUES_NAME);

			selectedAreas = (HashMap<String, JobObjectiveAreaDTO>) data.getExtras()
					.getSerializable(AdvancedSearchActivity.kDATA_SELECTED_AREAS);
			
			clearnAdvanceConditionBtn.setVisibility(View.VISIBLE);
			
			currentPage = 1;
			
			isAdvance = true;
			posts.clear();
			postAdapter.updateItems(posts);
			//
			searchConditionDTOForFilter.setPostPropertyCode(searchConditionDTO.getPostPropertyCode());
			loadAdvanced(searchConditionDTOForFilter);
		}

	}
	
	
	@Click(R.id.filterBtn)
	void onFilterBtn() {
		PartTimerAdvacedSearchActivity_.intent(this)
		.conditionDTO(searchConditionDTOForFilter)
		.conditionValuesName(conditionValuesName)
		.selectedAreas(selectedAreas)
		.selectedWeek(selectedWeek)
		.startForResult(AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH);
	}
	
	@Click(R.id.clearnAdvanceConditionBtn)
	void onClearConditions(){
		searchConditionDTOForFilter = new SearchConditionDTO();
		conditionValuesName.clear();
		selectedWeek.clear();
		selectedAreas.clear();
		posts.clear();
		postAdapter.updateItems(posts);
		clearnAdvanceConditionBtn.setVisibility(View.GONE);
		loadAdvanced(searchConditionDTO);
	}

	@Override
	public void receive(String key, Object msg) {
		if(Constants.kEVENT_RECIVE_SCREEN_BOUND.equals(key)){
			Log.i("EVEN_BOUND part", "OK");
			if(msg!=null){
//				screenBounds = (List<LbsParam>) msg;
				loadAdvanced(searchConditionDTO);
			}
		}
	}

	@Override
	public void onRequestStart(String reqId) {
		
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if(reqId.equals(kREQ_ID_fromMapToAdvancedSearchList)){
			Page<JobEntPostDTO> page = null;

			if (result != null) {
				page = (Page<JobEntPostDTO>) result;
				if (page.getElements() != null) {
					posts.addAll(page.getElements());
					hasNextPage = page.hasNextPage();
					postAdapter.updateItems(posts);
					
				}
			}
			listbarRl.setVisibility(View.VISIBLE);
			boolean empty = !(posts!=null && posts.size()>0);
			emptyTipView.isEmpty(empty);
			if(empty){
				postListView.setVisibility(View.GONE);
			}else{
				postListView.setVisibility(View.VISIBLE);
			}
			showPosts(page==null?0:page.getTotalRows());
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				onComplete(hasNextPage);
			}

		}, 1000);
	}
	@UiThread
	void onComplete(boolean hasNextPage) {
		postListView.stopLoadMore();
		postListView.setPullLoadEnable(hasNextPage);
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyTipView.showException((ZcdhException)error, this);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override 
	public void onLoadMore() {
		// TODO Auto-generated method stub
		loadAdvanced(isAdvance?searchConditionDTOForFilter:searchConditionDTO);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		long postId = posts.get(position - 1).getPostId();
		DetailsFrameActivity_.intent(this).postId(postId)
				.switchable(true).currentIndex(position - 1)
				.entId(posts.get(position - 1).getEntId())
				.posts(posts).start();
		
	}

	@Override
	public void loadData() {
		loadAdvanced(isAdvance?searchConditionDTOForFilter:searchConditionDTO);
	}
	
}
