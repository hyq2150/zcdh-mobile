package com.zcdh.mobile.app.activities.nearby;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcCUCCSpecialService;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.IRpcSearchInNearByService;
import com.zcdh.mobile.api.model.InformationCoverDTO;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.JobObjectiveAreaDTO;
import com.zcdh.mobile.api.model.JobSearchTagDTO;
import com.zcdh.mobile.api.model.JobUserLoginInfo;
import com.zcdh.mobile.api.model.LbsParam;
import com.zcdh.mobile.api.model.PointDTO;
import com.zcdh.mobile.api.model.PostPointDTO;
import com.zcdh.mobile.api.model.SearchConditionDTO;
import com.zcdh.mobile.api.model.ToMapPointDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.app.activities.parttimer.PartTimerAdvacedSearchActivity_;
import com.zcdh.mobile.app.activities.search.AdvancedSearchActivity;
import com.zcdh.mobile.app.activities.search.AdvancedSearchActivity_;
import com.zcdh.mobile.app.activities.search.AreaActivity;
import com.zcdh.mobile.app.activities.search.AreaActivity_;
import com.zcdh.mobile.app.dialog.NewUserGuideDialog;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.maps.bmap.GeoCodingRequest;
import com.zcdh.mobile.app.maps.bmap.MyBLocationCient;
import com.zcdh.mobile.app.maps.bmap.MyBMap;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.ad.ActionView;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.BitmapUtils;
import com.zcdh.mobile.utils.DeviceIDFactory;
import com.zcdh.mobile.utils.NetworkUtils;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.UnitTransfer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 附近 1）地图(语音、关键字、根据标签搜索职位标识） 2）职位列表、高级搜索 3）地图与职位列表相互切换
 *
 * @author yangjiannan
 */
@EFragment(R.layout.fragment_map)
public class NearbyMapFragment extends BaseFragment implements
        SearchBarListener, OnMapStatusChangeListener, OnMapLoadedCallback,
        RequestListener, OnMapClickListener, BDLocationListener,
        OnMarkerClickListener, OnItemClickListener, OnClickListener,
        DataLoadInterface, IXListViewListener, Subscriber, MapSettingProxy {

    private static final String TAG = NearbyMapFragment.class.getSimpleName();

    /**
     * 是否显示搜索栏
     */
    public static boolean isShowSearchBar = false;

    /**
     * 是否显示职位列表
     */
    public static boolean isShowPostList = false;

    /**
     * 记录显示模式 （地图模式或是列表模式）
     */
    private static final String kMODE = "kmode";

    /**
     * 地图模式
     */
    private static final int kMODE_MAP = 0;

    /**
     * 列表模式
     */
    private static final int kMODE_LIST = 1;

    /**
     * 记录当前模式
     */
    private int currentMode;

    /**
     * 是否初始化职位列表模式
     */
    private boolean isInitListViewMode;

    /**
     * 地图首次加载时
     */
    private String kREQ_ID_enterIntoMapFromNearBy;

    /**
     * 标识移动地图时请求职位标注点搜索服务请求
     */
    private String kREQ_ID_moveInMap;

    /**
     * 标识语音搜索服务请求
     */
    private String kREQ_ID_findPointByVoice;

    /**
     * 标识关键字和标签搜索服务请求
     */
    private String kREQ_ID_findPointByKeyWordOrTag;

    /**
     * 标识从地图到职位列表服务请求
     */
    private String kREQ_ID_fromMapToAdvancedSearchList;

    /**
     * 标识从职位列表到地图服务请求
     */
    private String kREQ_ID_fromSearchListToMapByAdvanceSearch;

    /**
     * 条件过滤
     */
    private String KREQ_ID_findSearchPostDTOForFilter;

    /**
     * 高级查询
     */
    private String KREQ_ID_findSearchPostDTOForAdvanceSearch;

    /**
     * 通过地图的标识点查找职位
     */
    private String kREQ_ID_findSearchPostDTOByPostIds;

    /**
     * 上传我的位置
     */
    private String kREQ_addLoginInfo;

    /**
     * 当前是否正在请求数据
     */
    private boolean isLoading;

    /**
     * 记录最后一次加载职位列表是通过高级搜索还是通过地图
     */
    private boolean kLAST_LOAD_POST_FILTER;

    /**
     * 高级查询
     */
    private boolean kLAST_LOAD_POST_ADVANCE;

    /**
     * 记录当前最后的请求
     */
    private int KLAST_LOAD_FLAG;

    /**
     * 首次进入地图
     */
    private static final int kLOAD_POINTS_FIRST = 0;

    /**
     * 移动地图
     */
    private static final int kLOAD_IN_MOVING = 1;

    /**
     * 搜索职位标注点（语音）
     */
    private static final int kLOAD_VOICE = 2;

    /**
     * 搜索职位标注点(关键字)
     */
    private static final int kLOAD_KEYWORD_OR_TAG = 3;

    private boolean hasMovingMap = true;

    /**
     * 记录是否上传我当前的位置
     */
    private boolean isUploadLocation;

    private IRpcNearByService nearbyService_old;

    private IRpcSearchInNearByService nearbyService;

    private IRpcJobUservice jobUservice;

    private IRpcCUCCSpecialService actionService;

    /**
     * 地图中加载的职位数据
     */
    private PointDTO points;

    /**
     * 从职位列表到地图的标识点
     */
    private ToMapPointDTO fromListViewToMappoints;

    private ProcessDialog processDialog;

    /**
     * 定位
     */
    private MyBLocationCient myBLocationClent;

    /**
     * 地图
     */
    private MyBMap map;

    /**
     * 地图操作类
     */
    private BaiduMap baiduMap;

    /**
     * 职位标注点
     */
    private List<Marker> pointMarkers = new ArrayList<Marker>();

    /**
     * 加载提示
     */
    @ViewById(R.id.loaderImg)
    ImageView loaderImg;

    /**
     * 2D 或3D 地图显示
     */
    @ViewById(R.id.mapTypeFlagImg)
    ImageView mapTypeFlagImg;

    /**
     * 搜索bar
     */
    private SearchBar searchBar;

    /**
     * 地图模式容器
     */
    @ViewById(R.id.mapModeContainer)
    RelativeLayout mapModeContainer;

    @ViewById(R.id.btnsContainerRl)
    LinearLayout btnsContainerRl;

    /**
     * 地图容器
     */
    @ViewById(R.id.mapContainer)
    RelativeLayout mapContainer;

    /**
     * 底部布局容器
     */
    @ViewById(R.id.bottom)
    LinearLayout bottom;

    /**
     * 浮在地图上的按钮布局
     */
    @ViewById(R.id.widgetsContainer)
    View widgetsContainer;

    @ViewById(R.id.mapSettingBtn)
    RelativeLayout mapSettingBtn;

    @ViewById(R.id.body)
    View body;

    @ViewById(R.id.action_view)
    ActionView actionView;

    private Dialog mapSettingDialog;

    /**
     * 地图2d图层切换
     */
    private ImageButton mapType2dBtn;

    /**
     * 卫星地图图层切换
     */
    private ImageButton mapType3dBtn;

    /**
     * 标注点详细列表
     */
    @ViewById(R.id.pointDetailsListView)
    XListView pointDetailsListView;

    private SearchResultAdapter pointDetailAdapter;

    /**
     * 标注点详细
     */
    private ArrayList<JobEntPostDTO> pointDetailOfPosts = new ArrayList<JobEntPostDTO>();

    /**
     * 搜索bar 容器
     */
    @ViewById(R.id.barContainer)
    View barContainer;

    /**
     * 显示标识点相应的职位
     */
    @ViewById(R.id.pointDetailContainer)
    LinearLayout pointDetailContainer;

    /**
     * 岗位数量
     */
//	@ViewById(R.id.postCountText)
//	TextView postCountTxt;

    /**
     * 在打开或关闭左侧菜单时候遮盖地图
     */
    private ImageView coverImg;

    /**
     * 清空条件
     */
    @ViewById(R.id.conditionText)
    TextView conditionText;

    /**
     * 搜索条件和结果显示
     */
    @ViewById(R.id.conditionPannelLl)
    RelativeLayout conditionPannelLl;

    /**
     * 清空高级搜索条件
     */
    @ViewById(R.id.clearnAdvanceConditionBtn)
    ImageButton clearnAdvanceConditionBtn;

    private LatLng center;

    /**
     * 在显示标注点的详细列表之前，临时记录当前地图的中心坐标
     */
    private LatLng tempCenter;

    /******************** 职位列表相关 ********************* */

    @ViewById(R.id.postListContainer)
    LinearLayout postListContainer;

    @ViewById(R.id.listbarRl)
    View listbarRl;

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
     * 职位列表
     */
    @ViewById(R.id.postListView)
    XListView postListView;

    @ViewById(R.id.emptyView)
    EmptyTipView emptyTipView;

    /**
     *
     */
    private SearchResultAdapter postAdapter;

    /**
     * 高级搜索条件
     */
    private SearchConditionDTO searchConditionDTOForAdvance = new SearchConditionDTO();

    /**
     * 条件过滤
     */
    private SearchConditionDTO searchConditionDTOForFilter = new SearchConditionDTO();

    /**
     * 地图模式条件
     */
    private SearchConditionDTO searchConditionDTOForMap = new SearchConditionDTO();

    private String postPropertyCode = Constants.POST_PROPERTY_QUANZHI;

    /**
     * 选择的标签
     */
    private JobSearchTagDTO selectedTag;

    /**
     * 高级搜索条件选择的值名称
     */
    private HashMap<Integer, String> conditionValuesName = new HashMap<Integer, String>();

    private HashMap<String, JobObjectiveAreaDTO> selectedAreas
            = new HashMap<String, JobObjectiveAreaDTO>();

    private ArrayList<String> selectedWeek = new ArrayList<String>();

    private int postDetailPage = 1;

    private int currentPage = 1;

    private int pageSize = 15;

    /**
     * 职位
     */
    private ArrayList<JobEntPostDTO> posts = new ArrayList<JobEntPostDTO>();

    /**
     * 标识列表是否只加载地图中显示职位
     */
    private boolean kLOAD_FOR_MAP_ONLY;

    private boolean hasNextPage;

    /**
     * 当前点选的标注点
     */
    private PostPointDTO selectedPoint;

    private Marker selectedMark;

    private boolean makeChangedOfMaxBounds;

    private float currentZoomLevel;

    private boolean hasTipsMaxZoom;

    private int screenHeight;

    /**
     * 显示新用户操作指引
     */
    private NewUserGuideDialog userGuidDialog;

    // 标注的当前位置
    private Marker currentLocationMarker;

    // 记录标注点包含的职位个数
    private int showDetailsPostsCount;

    private Bitmap bitmap = null;

    private List<InformationCoverDTO> actionDatas;

    private String actionIdentify;

    private final static int LAODING_ACTION_CODE = 1000;

    private final static int NEW_USER_GUIDE_POSTLIST = 1001;

    private final static int NEW_USER_GUIDE_MAP = 1002;

    private final static int NEW_USER_GUIDE_DELAYED = 2000;

    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private final WeakReference<NearbyMapFragment> mNearbyMapFragment;

        public MyHandler(NearbyMapFragment nearbyMapFragment) {
            // TODO Auto-generated constructor stub
            mNearbyMapFragment = new WeakReference<>(
                    nearbyMapFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            NearbyMapFragment fragment = mNearbyMapFragment.get();
            switch (msg.what) {
                case NEW_USER_GUIDE_MAP:
                    fragment.userGuidDialog.show(Constants.flag_map);
                    break;
                case NEW_USER_GUIDE_POSTLIST:
                    fragment.userGuidDialog.show(Constants.flag_postlist);
                    break;
                case LAODING_ACTION_CODE:
                    Log.e(TAG, "********LAODING_ACTION_CODE**********");
                    LatLng location = ZcdhApplication.getInstance().getMyLocation();
                    fragment.actionIdentify = RequestChannel.getChannelUniqueID();
                    fragment.actionService.findAppCoverDTOForMap(
                            fragment.getUserId(), 3, location.longitude,
                            location.latitude, "002").identify(
                            fragment.actionIdentify, fragment);
                    Log.e(TAG, "**********0000000000000 LAODING_ACTION_CODE 0000000000*******");
                    break;
                default:
                    break;
            }
        }
    }

    /* ================== 生命周期管理 =================== */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nearbyService_old = RemoteServiceManager
                .getRemoteService(IRpcNearByService.class);

        nearbyService = RemoteServiceManager
                .getRemoteService(IRpcSearchInNearByService.class);

        jobUservice = RemoteServiceManager
                .getRemoteService(IRpcJobUservice.class);

        actionService = RemoteServiceManager
                .getRemoteService(IRpcCUCCSpecialService.class);

        MyEvents.register(this);
    }

    @SuppressLint("InlinedApi")
    @AfterViews
    void bindViews() {
        int mode = SharedPreferencesUtil.getValue(getActivity(), kMODE, 0);
        switch (mode) {
            case kMODE_LIST:
                bindViewsForListViewModel();
                break;
            case kMODE_MAP:
                bindViewsForMapModel();
                break;
        }
        processDialog = new ProcessDialog(getActivity());
        processDialog.setCancelable(true);
        // 初始化SearchBar
        searchBar = new SearchBar();
        searchBar.initBar(this, getActivity(), barContainer);

        // 设置标注点详细列表
        // pointDetailsListView.setMode(Mode.PULL_FROM_END);
        // pointDetailsListView.setOnRefreshListener(this);
        pointDetailsListView.setPullRefreshEnable(false);
        pointDetailsListView.setPullLoadEnable(false);
        pointDetailsListView.setAutoLoadEnable(false);
        pointDetailsListView.setXListViewListener(this);
        pointDetailsListView.setOnItemClickListener(this);
        pointDetailAdapter = new SearchResultAdapter(getActivity());
        pointDetailsListView.setAdapter(pointDetailAdapter);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);

        screenHeight = metrics.heightPixels;

        // 如果是2g网络 加载职位列表
        if (NetworkUtils.getCurrentNetworkType(getActivity()).equals("2G")) {
            Toast.makeText(getActivity(), "当前为2G网络,默认加载职位列表",
                    Toast.LENGTH_SHORT).show();
            kLOAD_FOR_MAP_ONLY = false;
            switchModeView(searchBar.getSwitchModeBtn());
        }
        userGuidDialog = new NewUserGuideDialog(getActivity(),
                R.style.Theme_FullScreen);
    }

    /**
     * 列表模式View绑定初始化设置
     */
    void bindViewsForListViewModel() {
        postListView.setEmptyView(emptyTipView);
        postListView.setPullRefreshEnable(false);
        postListView.setPullLoadEnable(true);
        postListView.setAutoLoadEnable(false);
        postListView.setXListViewListener(this);
        postListView.setOnItemClickListener(this);
        postAdapter = new SearchResultAdapter(getActivity());
        postListView.setAdapter(postAdapter);

        // 记录已初始化职位列表
        isInitListViewMode = true;
    }

    /**
     * 地图模式View绑定与初始化设置
     */
    void bindViewsForMapModel() {

        // 1_ 初始化地图
        this.map = MyBMap.initMap(getActivity());
        this.baiduMap = this.map.getBaiduMap();
        baiduMap.setOnMapStatusChangeListener(this);
        baiduMap.setOnMapLoadedCallback(this);
        baiduMap.setOnMapClickListener(this);
        baiduMap.setOnMarkerClickListener(this);

        // 3_ 添加mapView
        LayoutParams mapRlLP = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mapContainer.addView(map.getMapView(), mapRlLP);

        // 4_ 遮盖图
        coverImg = new ImageView(getActivity());
        mapContainer.addView(coverImg, mapRlLP);

        doLocation();
    }

    /**
     * 请求定位
     */
    void doLocation() {
        // 2_ 初始化定位
        myBLocationClent = new MyBLocationCient(getActivity(), this);
        // 发起定位请求
        if (ZcdhApplication.getInstance().getMyLocation() == null) {
            myBLocationClent.requestLocation();
        } else {
            uploadLocation();
            mHandler.sendEmptyMessage(LAODING_ACTION_CODE);
        }
    }

    @Override
    public void onResume() {
        if (map != null) {
            map.getMapView().onResume();
        }
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        if (map != null) {
            map.getMapView().onPause();
        }
        closeAction();
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroy() {
        if (map != null && baiduMap != null) {
            baiduMap.clear();
            map.getMapView().onDestroy();
        }
        super.onDestroy();
    }

    public void closeAction() {
        if (null != actionView) {
            actionView.closeSlider();
        }
    }

    /**
     * 添加更多功能
     */
    public void onResultAddFuncs(int resultCode, Intent Data) {
        searchBar.refreshMoreFuncs();
    }

    /**
     * 条件
     */
    @SuppressWarnings("unchecked")
    @OnActivityResult(AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH)
    public void onResultCondition(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            boolean isAdvanceSearch = data.getBooleanExtra(
                    AdvancedSearchActivity.kDATA_IS_ADVANCESEARCH, false);
            if (isAdvanceSearch) {
                searchConditionDTOForAdvance = (SearchConditionDTO) data
                        .getExtras().getSerializable(
                                AdvancedSearchActivity.kDATA_CONDITIONS);
                if (searchConditionDTOForAdvance != null) {
                    searchConditionDTOForAdvance
                            .setLon(ZcdhApplication.getInstance().getMyLocation().longitude);
                    searchConditionDTOForAdvance
                            .setLat(ZcdhApplication.getInstance().getMyLocation().latitude);
                }
            } else {
                searchConditionDTOForFilter = (SearchConditionDTO) data
                        .getExtras().getSerializable(
                                AdvancedSearchActivity.kDATA_CONDITIONS);
            }
            conditionValuesName = (HashMap<Integer, String>) data.getExtras()
                    .getSerializable(AdvancedSearchActivity.kDATA_VALUES_NAME);

            selectedAreas = (HashMap<String, JobObjectiveAreaDTO>) data
                    .getExtras().getSerializable(
                            AdvancedSearchActivity.kDATA_SELECTED_AREAS);

            clearnAdvanceConditionBtn.setVisibility(View.VISIBLE);

            kLOAD_FOR_MAP_ONLY = false;
            currentPage = 1;
            updateConditions(isAdvanceSearch);
        }

    }

    /**
     * 选择地区
     */
    public void onResultArea(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String areaName = data.getExtras().getString(
                    AreaActivity.kDATA_AREA_NAME);
            String areaCode = data.getExtras().getString(
                    AreaActivity.kDATA_AREA_CODE);
            if (!"0".equals(areaCode)) {
                startLoadingAnimation();
                geoCodingLatlngByAddress(areaName);
            }
        }
    }

	/* ==================== SearchBarListenner ==================== */

    @Override
    public void onTagClick(JobSearchTagDTO tag) {
        if (tag != null) {
            if (currentMode == kMODE_LIST) {
                searchConditionDTOForFilter = new SearchConditionDTO();
                searchConditionDTOForFilter.setTagCode(tag.getTagCode());
                updateConditions(false);
            }
            if (currentMode == kMODE_MAP) {
                selectedTag = tag;
                searchConditionDTOForMap = new SearchConditionDTO();
                searchConditionDTOForMap.setTagCode(tag.getTagCode());
                loadPoints(kLOAD_KEYWORD_OR_TAG);
            }
        }
    }

    @Override
    public void onKeywordSearch(String keyword) {
        if (!StringUtils.isBlank(keyword)) {
            if (currentMode == kMODE_LIST) {
                searchConditionDTOForFilter = new SearchConditionDTO();
                searchConditionDTOForFilter.setKeyWord(keyword);
                updateConditions(false);
            }
            if (currentMode == kMODE_MAP) {
                hasMovingMap = true;
                searchConditionDTOForMap = new SearchConditionDTO();
                searchConditionDTOForMap.setKeyWord(keyword);
                loadPoints(kLOAD_KEYWORD_OR_TAG);
            }
        }
    }

    @Override
    public void onVoiceEnd() {

    }

    @Override
    public void onVoiceResult(String sayContent) {
        // Toast.makeText(getActivity(), sayContent + " = onVoiceResult",
        // Toast.LENGTH_SHORT).show();
        if (!StringUtils.isBlank(sayContent)) {
            startLoadingAnimation();
            if (currentMode == kMODE_LIST) {
                searchConditionDTOForFilter = new SearchConditionDTO();
                searchConditionDTOForFilter.setKeyWord(sayContent);
                updateConditions(false);
            }
            if (currentMode == kMODE_MAP) {
                hasMovingMap = true;
                searchConditionDTOForMap = new SearchConditionDTO();
                searchConditionDTOForMap.setKeyWord(sayContent);
                loadPoints(kLOAD_KEYWORD_OR_TAG);
            }
        }
    }

    /**
     * 重地图 与 职位列表相互切换
     */
    @Override
    public void onSwitchMode(ImageButton v) {
        switchModeView(v);
    }

    void switchModeView(ImageButton switchModeBtn) {
        if (conditionPannelLl.isShown()) {
            conditionPannelLl.setVisibility(View.GONE);
        }
        // 从地图切换到职位列表模式
        switch (currentMode) {
            case kMODE_MAP:
                MobclickAgent.onEvent(getActivity(), Constants.UMENG_NEARBY_LIST_ACTION);
                isShowPostList = true;
                // 列表模式新用户操作指引
                boolean hasShowNewUserGuide = SharedPreferencesUtil.getValue(
                        getActivity(), Constants.kSHARE_NEW_USER_GUIDE_LIST, false);
                if (!hasShowNewUserGuide) {
                    mHandler.sendEmptyMessageDelayed(NEW_USER_GUIDE_POSTLIST,
                            NEW_USER_GUIDE_DELAYED);
                }
                // 切换界面
                if (!isInitListViewMode) {
                    bindViewsForListViewModel();
                }
                widgetsContainer.setVisibility(View.GONE);
                mapModeContainer.setVisibility(View.GONE);
                btnsContainerRl.setVisibility(View.GONE);
                postListContainer.setVisibility(View.VISIBLE);
                switchModeBtn.setImageResource(R.drawable.ic_map_switcher);
                currentMode = kMODE_LIST;
                if (kLAST_LOAD_POST_FILTER) {
                    loadPostForFilter();
                } else {
                    // 标识列表当前只加载地图中的职位
                    kLOAD_FOR_MAP_ONLY = true;
                    // 加载数据显示到职位列表
                    currentPage = 1;
                    if (hasMovingMap) {
                        // 将先前的职位先清空
                        posts.clear();
                        postAdapter.updateItems(posts);
                        emptyTipView.startLoadingAnim();
                        listbarRl.setVisibility(View.GONE);
                        loadPostFromMap();
                    }
                }
                hasMovingMap = false;
                actionView.showActionView(false);
                break;
            case kMODE_LIST:
                isShowPostList = false;
                currentMode = kMODE_MAP;
                widgetsContainer.setVisibility(View.VISIBLE);
                mapModeContainer.setVisibility(View.VISIBLE);
                btnsContainerRl.setVisibility(View.VISIBLE);
                postListContainer.setVisibility(View.GONE);
                switchModeBtn.setImageResource(R.drawable.list_normal);
                actionView.showActionView(true);
                break;
            default:
                break;
        }
    }

    /**
     * 显示搜索面板
     */
    public void onShow(boolean show) {
        isShowSearchBar = show;
    }

    /* ====================== 职位列表回调 ========================== */
    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        Log.i(TAG, "加载更多..列表");
        if (hasNextPage) {
            if (Constants.isShowingPointDetails) {
                loadPostByPoint();
            } else {
                if (kLOAD_FOR_MAP_ONLY) { //
                    loadPostFromMap();
                } else {
                    if (kLAST_LOAD_POST_ADVANCE) {
                        loadPostByAdvance();
                    } else {
                        loadPostForFilter(); // 高级搜索
                    }
                }
            }
        }

    }

    @UiThread
    void onComplete(boolean hasNextPage) {
        Log.i(TAG, "加载更多 Oncomplete");
        postListView.stopLoadMore();
        pointDetailsListView.stopLoadMore();
        if (Constants.isShowingPointDetails) {
            pointDetailsListView.setPullLoadEnable(hasNextPage);
        } else {
            postListView.setPullLoadEnable(hasNextPage);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        switch (parent.getId()) {
            //附近职位列表
            case R.id.postListView:
                DetailsFrameActivity_.intent(getActivity())
                        .postId(posts.get(position - 1).getPostId())
                        .entId(posts.get(position - 1).getEntId()).switchable(true)
                        .currentIndex(position - 1)
                        .posts(posts)
                        .start();
                break;
            //地图上图标点击时显示的职位列表
            case R.id.pointDetailsListView:
                if (pointDetailOfPosts.size() > 1) {
                    DetailsFrameActivity_.intent(getActivity())
                            .postId(pointDetailOfPosts.get(position - 1).getPostId())
                            .entId(pointDetailOfPosts.get(position - 1).getEntId())
                            .switchable(true)
                            .posts(pointDetailOfPosts)
                            .currentIndex(position - 1).start();
                } else {
                    DetailsFrameActivity_.intent(getActivity())
                            .postId(pointDetailOfPosts.get(position - 1).getPostId())
                            .entId(pointDetailOfPosts.get(position - 1).getEntId())
                            .switchable(false).start();
                }
                break;
            default:
                break;
        }

    }

    /* ================== 地图改变事件 ================== */
    @Override
    public void onMapStatusChange(MapStatus status) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus status) {
        // Log.i("status changed Finish", "yes");
        if (Constants.isShowingPointDetails) {
            return;
        }

        if (currentZoomLevel == baiduMap.getMaxZoomLevel()
                && status.zoom == baiduMap.getMaxZoomLevel()) {
            if (!hasTipsMaxZoom) {
                Toast.makeText(getActivity(), "地图已是最大缩放级别", Toast.LENGTH_SHORT)
                        .show();
                hasTipsMaxZoom = true;
                return;
            }
        }
        if (status.zoom != baiduMap.getMaxZoomLevel()) {
            hasTipsMaxZoom = false;
        }

        if (center == null) { // 判断中心坐标点是否为空，来断定是否首次打开进入地图
            loadPoints(kLOAD_POINTS_FIRST);
        } else { // 否则是地图状态改变，效果与地图移动等同效果处理
            // 判断屏幕是否移除最大取数范围
            if (map.hasOutOfMaxBound()) {
                makeChangedOfMaxBounds = false;
                loadPoints(kLOAD_IN_MOVING);
            }
            if (makeChangedOfMaxBounds) {
                map.buildMaxBound();
                makeChangedOfMaxBounds = false;
                loadPoints(kLOAD_IN_MOVING);
            }
        }

        center = baiduMap.getMapStatus().target;
        tempCenter = center;
        if (currentZoomLevel != status.zoom) {
            loadPoints(kLOAD_IN_MOVING);
        }
        currentZoomLevel = status.zoom;
        hasMovingMap = true;
    }

    @Override
    public void onMapStatusChangeStart(MapStatus status) {

    }

    /* ================ 地图加载完成事件 ================= */
    @Override
    public void onMapLoaded() {
        center = ZcdhApplication.getInstance().getMyLocation();
        tempCenter = center;
        if (center != null) {
            loadPoints(kLOAD_POINTS_FIRST);
        }
        showNewUserGuide();
    }

    /**
     * 显示新用户指引
     */
    public void showNewUserGuide() {
        // 第一次显示菜单没完成，也不显示操作指引
        if (!this.isVisible()) {
            return;
        }
        if (!SharedPreferencesUtil.getValue(getActivity(),
                Constants.kAUTO_MENU, false)) {
            return;
        }

        // 在地图模式，显示新用户操作指引
        boolean showNewUserGuide = SharedPreferencesUtil.getValue(
                getActivity(), Constants.kSHARE_NEW_USER_GUIDE_MAP, false);
        if (!showNewUserGuide) {
            mHandler.sendEmptyMessageDelayed(NEW_USER_GUIDE_MAP,
                    NEW_USER_GUIDE_DELAYED);
        }
    }

    /* =============== 地图点击 ================== */
    @Override
    public void onMapClick(LatLng arg0) {
        baiduMap.animateMapStatus(MapStatusUpdateFactory
                .newLatLng(tempCenter == null ? center : tempCenter));
        resetLayoutOfMap();
        if (selectedMark != null && selectedPoint != null) {
            setMarkerTheme(selectedMark, selectedPoint, false);
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi arg0) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Overlay options = (Overlay) marker;
        Bundle extra = options.getExtraInfo();
        if (extra != null) {
            PostPointDTO point = (PostPointDTO) extra.getSerializable("post");
            showDetailsPostsCount = extra.getInt("count");
            // 将上一个选中的标注点样式重置
            if (selectedMark != null) {
                setMarkerTheme(selectedMark, selectedPoint, false);
            }

            selectedPoint = point;
            // 保存本次点击的标注点
            selectedMark = marker;
            // 处理多点重合的标注点
            // 1)当地图放大到最大时，则显示标注点的详细列表
            // 2)当重合的标注点只有5个以下时，则显示标注点得详细列表
            if (point.getIsMutiplePost() == 1
                    && baiduMap.getMapStatus().zoom < baiduMap
                    .getMaxZoomLevel()
                    && point.getMultiPostPoint().getIsShowPosts() == 0) {

                makeChangedOfMaxBounds = true;
                Point p = baiduMap.getProjection().toScreenLocation(
                        new LatLng(point.getMultiPostPoint().getLat(), point
                                .getMultiPostPoint().getLon()));
                baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomBy(2, p));

            } else {
                // 加载标注点详细列表数据
                postDetailPage = 1;
                pointDetailOfPosts.clear(); // 先清空标注点的详细列表数据
                loadPostByPoint();
                // 设置点选后标注点得样式
                setMarkerTheme(marker, point, true);
            }
        }
        return false;
    }

    /* ================ 定位回调函数 ==================== */
    @Override
    public void onReceiveLocation(BDLocation location) {
        processDialog.dismiss();
        center = new LatLng(location.getLatitude(), location.getLongitude());
        ZcdhApplication.getInstance().setMyLocation(center);
        tempCenter = center;
        resetLayoutOfMap();
        try {
            makeChangedOfMaxBounds = true;
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(
                    center, MyBMap.defaultZoom));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 重新显示我的位置
        showMyLocationPoint();
    }

    /**
     * 切换地点
     */
    @Click(R.id.cityBtn)
    void onCityPlaceBtn() {
        AreaActivity_.intent(getActivity()).signle(true)
                .startForResult(AreaActivity.kREQUEST_AREA);
    }

    /**
     * 定位
     */
    @Click(R.id.locationBtn)
    void onLocationBtn() {
        processDialog.show("回到附近...");
        myBLocationClent.requestLocation();
    }

    /**
     * 周边
     */
    @Click(R.id.nearbyRl)
    void onNearbyRl() {
        PoiSearchActivity_.intent(getActivity()).center_lat(center.latitude)
                .center_lon(center.longitude).start();
        searchBar.showOrhiddenPannelByMoreBtn();
    }

    /**
     * 地图设置
     */
    @Click(R.id.mapSettingBtn)
    void onMapSettingBtn() {
        if (mapSettingDialog == null) {
            mapSettingDialog = new MapSettingDialog(getActivity(), this);
            mapType3dBtn = (ImageButton) mapSettingDialog
                    .findViewById(R.id.mapType3dBtn);
            mapType2dBtn = (ImageButton) mapSettingDialog
                    .findViewById(R.id.mapType2dBtn);
            mapType2dBtn.setOnClickListener(this);
            mapType3dBtn.setOnClickListener(this);
        }
        mapSettingDialog.show();
        MobclickAgent.onEvent(getActivity(), Constants.UMENG_JOB_TYPE_ACTION);

    }

    /**
     * 放大地图
     */
    @Click(R.id.zoomInBtn)
    void onZoomInBtn() {
        makeChangedOfMaxBounds = true;
        baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomIn());
    }

    /**
     * 缩小地图
     */
    @Click(R.id.zoomOutBtn)
    void onZoomOutBtn() {
        makeChangedOfMaxBounds = true;
        baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomOut());
    }

    /**
     * 隐藏预览职位列表
     */
    @Click(R.id.currentPostCountLl)
    public void onHiddenPreview() {

        baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(tempCenter));
        resetLayoutOfMap();
        if (selectedMark != null && selectedPoint != null) {
            setMarkerTheme(selectedMark, selectedPoint, false);
        }
    }

    /**
     * 清空条件
     */
    @Click(R.id.clearBtn)
    void onClearConditionBtn() {
        clearSearch();
    }

    /**
     * 清空高级搜索
     */
    @Click(R.id.clearnAdvanceConditionBtn)
    void onClearnAdvanceConditionBtn() {
        clearSearch();
    }

    /**
     * 筛选（高级搜索）
     */
    @Click(R.id.filterBtn)
    void onFilterBtn() {
        if (Constants.POST_PROPERTY_QUANZHI.equals(postPropertyCode)) {
            AdvancedSearchActivity_
                    .intent(getActivity())
                    .conditionDTO(searchConditionDTOForFilter)
                    .conditionValuesName(conditionValuesName)
                    .selectedAreas(selectedAreas)
                    .isAdvanceSearch(false)
                    .startForResult(
                            AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH);
        } else {
            PartTimerAdvacedSearchActivity_
                    .intent(getActivity())
                    .conditionDTO(searchConditionDTOForFilter)
                    .conditionValuesName(conditionValuesName)
                    .selectedAreas(selectedAreas)
                    .selectedWeek(selectedWeek)
                    .startForResult(
                            AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH);
        }
    }

	/* ============ 调用后端服务 ================== */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapType2dBtn:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                mapType2dBtn.setImageResource(R.drawable.mapflat_active);
                mapType3dBtn.setImageResource(R.drawable.mapset_normal);
                break;
            case R.id.mapType3dBtn:
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                mapType2dBtn.setImageResource(R.drawable.mapflat_normal);
                mapType3dBtn.setImageResource(R.drawable.mapset_active);
                break;
            default:
                break;
        }
        mapSettingDialog.dismiss();
    }

    private void processGetActionResult(Object result) {
        if (result != null) {
            actionDatas = (List<InformationCoverDTO>) result;
            Log.e(TAG, "actionDatas.size : " + actionDatas.size());
            if (actionDatas != null && actionDatas.size() > 0) {
                actionView.setCoverData(actionDatas.get(0));
            }
        }
    }

    @Override
    public void onRequestStart(String reqId) {

    }

    @Override
    public void onRequestSuccess(String reqId, Object result) {

        if (isLoading) {
            loadPoints(KLAST_LOAD_FLAG);
            isLoading = false;
        }

        // 进入地图时加载
        if (reqId.equals(kREQ_ID_enterIntoMapFromNearBy)) {
            if (result != null) {
                points = (PointDTO) result;
            }
            showPoints();
        }

        // 移动地图时，加载新的职位标注点
        if (reqId.equals(kREQ_ID_moveInMap)) {
            if (result != null) {
                points = (PointDTO) result;
                showPoints();
            }
        }

        // 语音搜索
        if (reqId.equals(kREQ_ID_findPointByVoice)) {
            if (result != null) {
                points = (PointDTO) result;
                showPoints();//
            }
        }
        // 关键字和标签搜索职位
        if (reqId.equals(kREQ_ID_findPointByKeyWordOrTag)) {
            if (result != null) {
                points = (PointDTO) result;
                showPoints();
            } else {

            }
        }

        // 从职位列表到地图
        if (reqId.equals(kREQ_ID_fromSearchListToMapByAdvanceSearch)) {
            if (result != null) {
                fromListViewToMappoints = (ToMapPointDTO) result;
                showPoints();
            } else {
                tips();
            }
        }

        // 职位列表高级搜索
        if (reqId.equals(KREQ_ID_findSearchPostDTOForFilter)
                || reqId.equals(KREQ_ID_findSearchPostDTOForAdvanceSearch)) {
            Page<JobEntPostDTO> page = null;
            int totalRows = 0;
            if (result != null) {
                page = (Page<JobEntPostDTO>) result;
                if (page.getElements() != null) {
                    posts.addAll(page.getElements());
                    hasNextPage = page.hasNextPage();
                    totalRows = page.getTotalRows();
                }
            }
            showPostInListView(hasNextPage, totalRows);

        }

        // 将地图的职位标注点数据加载到职位列表显示
        if (reqId.equals(kREQ_ID_fromMapToAdvancedSearchList)) {
            Page<JobEntPostDTO> page = null;
            int totalRows = 0;
            if (result != null) {
                page = (Page<JobEntPostDTO>) result;
                if (page.getElements() != null) {
                    posts.addAll(page.getElements());
                    hasNextPage = page.hasNextPage();
                    totalRows = page.getTotalRows();

                }
            }
            // 如果在地图中带有条件搜索，切换到列表模式时，应该显示删除搜索条件的按钮(垃圾桶按钮)
            if (!StringUtils.isBlank(searchConditionDTOForMap.getTagCode())
                    || !StringUtils.isBlank(searchConditionDTOForMap
                    .getKeyWord())) {
                clearnAdvanceConditionBtn.setVisibility(View.VISIBLE);
            } else {
                clearnAdvanceConditionBtn.setVisibility(View.GONE);
            }
            showPostInListView(hasNextPage, totalRows);

        }

        // 显示地图中标注点的职位详细
        if (reqId.equals(kREQ_ID_findSearchPostDTOByPostIds)) {
            if (result != null) {
                Page<JobEntPostDTO> page = (Page<JobEntPostDTO>) result;
                if (page.getElements() != null) {
                    pointDetailOfPosts.addAll(page.getElements());
                    hasNextPage = page.hasNextPage();
                    if (selectedPoint.getIsMutiplePost() == 1) {
                        showMutilPointDetails(page.getTotalRows());
                    } else if (selectedPoint.getIsMutiplePost() == 0
                            && pointDetailOfPosts.size() >= 1) {
                        showSinglePointDetail(page.getTotalRows());
                    }
                }
            }
        }

        if (reqId.equals(kREQ_addLoginInfo)) {
            if (result != null) {
                int success = (Integer) result;
                if (success == 0) {
                    isUploadLocation = true;
                }
            }
        }

        if (reqId.equals(actionIdentify)) {
            Log.e(TAG, "actionIdentify : " + actionIdentify);
            processGetActionResult(result);
        }

    }

    @Override
    public void onRequestFinished(String reqId) {
        endLoadAnimation();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                onComplete(hasNextPage);
            }
        }, 500);
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        isLoading = false;
        if (error != null) {
            Toast.makeText(getActivity(),
                    ((ZcdhException) error).getErrMessage() + "",
                    Toast.LENGTH_SHORT).show();
            if (emptyTipView != null && error != null) {
                emptyTipView.showException((ZcdhException) error, this);
            }
        }
    }

    /**
     * 反地理编码查询
     */
    @Background
    void geoCodingLatlngByAddress(String address) {
        LatLng latlng = GeoCodingRequest.geoCodingLatlngByAddress(address);
        Log.i("geoCodingLatlngByAddress", latlng + "");
        if (latlng != null) {
            center = latlng;
            tempCenter = center;
            updateMapStatusInBackground(MapStatusUpdateFactory.newLatLngZoom(
                    center, MyBMap.defaultZoom));
        }
    }

    @UiThread
    void updateMapStatusInBackground(MapStatusUpdate status) {
        if (status != null && baiduMap != null) {
            baiduMap.animateMapStatus(status);
        }
    }

    /**
     * 重置搜索，恢复默认条件 1) 恢复从地图到列表 2) 从列表的高级搜索恢复到 从地图到列表状态 3) 地图中的默认搜索
     */
    private void clearSearch() {

        searchBar.clear();
        if (conditionPannelLl.isShown()) {
            conditionPannelLl.setVisibility(View.GONE);
        }
        // 重置列表模式相关
        if (postAdapter != null) {
            posts.clear();
            postAdapter.updateItems(posts);
            currentPage = 1;
        }
        // 重置列表模式和地图模式的搜索条件
        kLOAD_FOR_MAP_ONLY = true;
        selectedAreas.clear();
        selectedTag = null;
        searchConditionDTOForMap = new SearchConditionDTO();
        searchConditionDTOForFilter = new SearchConditionDTO();
        if (currentMode == kMODE_LIST) {
            conditionValuesName = new HashMap<Integer, String>();
            clearnAdvanceConditionBtn.setVisibility(View.GONE);
            loadPostFromMap();
            loadPostByAdvance();
        } else if (currentMode == kMODE_MAP) {
            hasMovingMap = true;
            if (conditionPannelLl.isShown()) {
                conditionPannelLl.setVisibility(View.GONE);
            }
            loadPoints(kLOAD_IN_MOVING);
        }
    }

    /**
     * 高级搜索重置条件
     */
    private void updateConditions(boolean isAdvanceSearch) {

        clearnAdvanceConditionBtn.setVisibility(View.VISIBLE);

        Log.i(TAG, "posts:" + posts.size());
        posts.clear();
        if (postAdapter != null) {
            postAdapter.updateItems(posts);
        }
        currentPage = 1;
        kLOAD_FOR_MAP_ONLY = false;
        if (isAdvanceSearch) {
            if (currentMode == kMODE_MAP) { // 如果是地图模式，切换到列表模式
                isShowPostList = true;

                // 列表模式新用户操作指引
                boolean hasShowNewUserGuide = SharedPreferencesUtil.getValue(
                        getActivity(), Constants.kSHARE_NEW_USER_GUIDE_LIST,
                        false);
                if (!hasShowNewUserGuide) {
                    mHandler.sendEmptyMessageDelayed(NEW_USER_GUIDE_POSTLIST,
                            NEW_USER_GUIDE_DELAYED);
                }

                // 切换界面
                if (!isInitListViewMode) {
                    bindViewsForListViewModel();
                }
                actionView.showActionView(false);
                mapModeContainer.setVisibility(View.GONE);
                btnsContainerRl.setVisibility(View.GONE);
                postListContainer.setVisibility(View.VISIBLE);
                searchBar.getSwitchModeBtn().setImageResource(R.drawable.ic_map_switcher);

                currentMode = kMODE_LIST;

                hasMovingMap = false;
            }
            loadPostByAdvance();
        } else {
            loadPostForFilter();

        }
    }

    /**
     * 上传我的位置
     */
    @Background
    void uploadLocation() {
        if (!isUploadLocation) {
            JobUserLoginInfo loginInfo = new JobUserLoginInfo();
            LatLng myLocation = ZcdhApplication.getInstance().getMyLocation();
            loginInfo.setLat(myLocation.latitude);
            loginInfo.setLon(myLocation.longitude);
            loginInfo.setUserId(getUserId());
            loginInfo.setSysType(1);
            loginInfo.setModelNumber(Build.MODEL);
            loginInfo.setSysVersion(Build.VERSION.RELEASE);
            loginInfo.setMiei(new DeviceIDFactory(getActivity())
                    .getDeviceUuid() + "");
            jobUservice.addLoginInfo(loginInfo).identify(
                    kREQ_addLoginInfo = RequestChannel.getChannelUniqueID(),
                    this);
        }
    }

    /**
     * 加载职位标注点到地图
     *
     * @param load_flag 标识请求服务渠道
     */
    private void loadPoints(int load_flag) {
        if (nearbyService == null) {
            return;
        }
        searchConditionDTOForMap.setPostPropertyCode(postPropertyCode);
        KLAST_LOAD_FLAG = load_flag;
        // 判断是由请求未完
        if (isLoading) {
            return;
        }

        startLoadingAnimation();
        resetLayoutOfMap();
        isLoading = true;
        // 显示加载进度

        // 发起请求
        switch (load_flag) {
            case kLOAD_POINTS_FIRST: // 首次加载到当前位置地图
                loadPointsForMapFirst();
                break;
            case kLOAD_VOICE:
                loadPointForMapByVoice();// 语音搜索
                break;
            case kLOAD_IN_MOVING:
                loadPointsForMapMoving();// 移动地图
                break;
            case kLOAD_KEYWORD_OR_TAG:
                loadPointForMapByKeywordAndTag(); // 关键字标签搜索
                break;
            // case kLOAD_FROM_LISTVIEW:
            // loadPointsFromListView();// 从职位列表到地图
            // break;
        }
    }

    /**
     * 加载地图职位的标注点
     */
    @Background
    void loadPointsForMapFirst() {
        float isMaxScale = 0; // 1 地图已经是放大到最大了，0 还可以放大
        if (baiduMap.getMapStatus().zoom == baiduMap.getMaxZoomLevel()) {
            isMaxScale = 1;
        }
        nearbyService_old
                .enterIntoMapFromNearByNew(getUserId(), map.getScreenBound(),
                        (int) isMaxScale,
                        searchConditionDTOForMap.getPostPropertyCode())
                .identify(
                        kREQ_ID_enterIntoMapFromNearBy = RequestChannel
                                .getChannelUniqueID(),
                        this);
    }

    /**
     * 移动地图时候加载职位标注点
     */
    @Background
    void loadPointsForMapMoving() {
        int isMaxScale = 0; // 1 地图已经是放大到最大了，0 还可以放大
        if (baiduMap.getMapStatus().zoom == baiduMap.getMaxZoomLevel()) {
            isMaxScale = 1;
        }
        nearbyService.moveInMap(getUserId(), searchConditionDTOForMap,
                map.getScreenBound(), isMaxScale).identify(
                kREQ_ID_moveInMap = RequestChannel.getChannelUniqueID(), this);
    }

    /**
     * 在地图中语音搜索
     */
    @Background
    void loadPointForMapByVoice() {
        int isMaxScale = 0; // 1 地图已经是放大到最大了，0 还可以放大
        if (baiduMap.getMapStatus().zoom == baiduMap.getMaxZoomLevel()) {
            isMaxScale = 1;
        }
        nearbyService.findPointByVoice(getUserId(),
                searchConditionDTOForMap.getKeyWord(),
                searchConditionDTOForMap.getPostPropertyCode(),
                map.getScreenBound(), isMaxScale).identify(
                kREQ_ID_findPointByVoice = RequestChannel.getChannelUniqueID(),
                this);
    }

    /**
     * 在地图中关键字或标签搜索
     */
    @Background
    void loadPointForMapByKeywordAndTag() {
        int isMaxScale = 0; // 1 地图已经是放大到最大了，0 还可以放大
        if (baiduMap.getMapStatus().zoom == baiduMap.getMaxZoomLevel()) {
            isMaxScale = 1;
        }
        nearbyService
                .findPointByKeyWordOrTag(getUserId(),
                        searchConditionDTOForMap.getKeyWord(),
                        searchConditionDTOForMap.getTagCode(),
                        searchConditionDTOForMap.getPostPropertyCode(),
                        map.getScreenBound(), isMaxScale)
                .identify(
                        kREQ_ID_findPointByKeyWordOrTag = RequestChannel
                                .getChannelUniqueID(),
                        this);

    }

    /**
     * 加载职位列表数据
     */
    @Background
    void loadPostFromMap() {
        if (nearbyService == null) {
            return;
        }
        kLAST_LOAD_POST_FILTER = false;

        hasNextPage = false;
        // why post user id;
        LatLng myLocation = ZcdhApplication.getInstance().getMyLocation();
        searchConditionDTOForMap.setLat(myLocation.latitude);
        searchConditionDTOForMap.setLon(myLocation.longitude);
        searchConditionDTOForMap.setPostPropertyCode(postPropertyCode);
        nearbyService
                .fromMapToAdvancedSearchList(getUserId(),
                        searchConditionDTOForMap, map.getScreenBound(),
                        currentPage, pageSize)
                .identify(
                        kREQ_ID_fromMapToAdvancedSearchList = RequestChannel
                                .getChannelUniqueID(),
                        this);
        currentPage++;

    }

    @Background
    void loadPostByAdvance() {
        searchConditionDTOForAdvance.setPostPropertyCode(postPropertyCode);
        if (nearbyService == null) {
            return;
        }

        kLAST_LOAD_POST_ADVANCE = true;
        hasNextPage = true;
        nearbyService
                .findSearchPostDTOByAdvancedSearch(getUserId(),
                        searchConditionDTOForAdvance, currentPage, pageSize)
                .identify(
                        KREQ_ID_findSearchPostDTOForAdvanceSearch = RequestChannel
                                .getChannelUniqueID(),
                        this);
        currentPage++;
    }

    /**
     * 职位列表高级搜索
     */
    @Background
    void loadPostForFilter() {
        if (nearbyService == null) {
            return;
        }
        kLAST_LOAD_POST_FILTER = true;

        // 取消高级查询，改为过滤查询
        kLAST_LOAD_POST_ADVANCE = false;

        hasNextPage = false;
        List<LbsParam> lbs = null;

        // 如果是高级查询，取消只限定在本地区查询的限制
        if (kLAST_LOAD_POST_ADVANCE) {
            lbs = null;
        }

        LatLng myLocation = ZcdhApplication.getInstance().getMyLocation();
        searchConditionDTOForFilter.setLat(myLocation.latitude);
        searchConditionDTOForFilter.setLon(myLocation.longitude);
        searchConditionDTOForFilter.setPostPropertyCode(postPropertyCode);
        nearbyService
                .filtrateResultInSearchList(getUserId(),
                        searchConditionDTOForFilter, lbs, currentPage, pageSize)
                .identify(
                        KREQ_ID_findSearchPostDTOForFilter = RequestChannel
                                .getChannelUniqueID(),
                        this);

        currentPage++;
    }

    /**
     * 点击标注点加载职位
     */
    @Background
    void loadPostByPoint() {

        hasNextPage = false;
        startLoadingAnimation();
        List<Long> postIds = new ArrayList<Long>();
        int isMutil = selectedPoint.getIsMutiplePost();
        if (isMutil == 1) {
            postIds = selectedPoint.getMultiPostPoint().getPostIds();
        }
        if (isMutil == 0) {
            postIds.add(selectedPoint.getSinglePostPoint().getPost_id());
        }

        nearbyService
                .findSearchPostDTOByPostIds(getUserId(), postIds,
                        postDetailPage, pageSize)
                .identify(
                        kREQ_ID_findSearchPostDTOByPostIds = RequestChannel
                                .getChannelUniqueID(),
                        this);
        postDetailPage++;
    }

    public void loadData() {

        switch (currentMode) {
            case kMODE_LIST:
                if (kLAST_LOAD_POST_FILTER) {
                    loadPostForFilter();
                } else if (kLAST_LOAD_POST_ADVANCE) {
                    loadPostByAdvance();
                } else {
                    loadPostFromMap();
                }
                break;
            case kMODE_MAP:
                loadPoints(KLAST_LOAD_FLAG);
                break;
            default:
                break;
        }

    }

    /**
     * 在地图中显示职位的标注点
     */
    void showPoints() {
        try {
            clearMarkers();
            // 同时显示我的位置
            showMyLocationPoint();

            if (fromListViewToMappoints != null) {
                points = fromListViewToMappoints.getPoint();
                if (fromListViewToMappoints.getLbs() != null) {
                    LbsParam eastnorth = fromListViewToMappoints.getLbs()
                            .get(2); // 东北方向坐标，即屏幕右上角对应的坐标点
                    LbsParam southwest = fromListViewToMappoints.getLbs()
                            .get(3); // 西南方向，
                    // 即屏幕左下角对应的坐标点
                    LatLngBounds.Builder llb = new LatLngBounds.Builder();
                    llb.include(new LatLng(eastnorth.getLat(), eastnorth
                            .getLon()));
                    llb.include(new LatLng(southwest.getLat(), southwest
                            .getLon()));
                    baiduMap.setMapStatus(MapStatusUpdateFactory
                            .newLatLngBounds(llb.build()));
                }
                fromListViewToMappoints = null;
            }
            if (points.getPostPoints() != null
                    && points.getPostPoints().size() > 0) {

                for (int i = 0; i < points.getPostPoints().size(); i++) {

                    PostPointDTO post = points.getPostPoints().get(i);

                    // 定义Maker坐标点
                    LatLng point = null;
                    // 构建Marker图标
                    BitmapDescriptor bitmapDesc = null;
                    // 构建MarkerOption，用于在地图上添加Marker
                    MarkerOptions option = null;
                    int count = 1;
                    bitmap = null;
                    int bitmapId = 0;
                    if (post.getIsMutiplePost() == 1) {
                        count = post.getMultiPostPoint().getTotals();
                        point = new LatLng(post.getMultiPostPoint().getLat(),
                                post.getMultiPostPoint().getLon());
                        if (Constants.POST_PROPERTY_JIANZHI
                                .equals(searchConditionDTOForMap
                                        .getPostPropertyCode())) {
                            bitmapId = R.drawable.tag08;
                        }
                        if (Constants.POST_PROPERTY_JIAQI
                                .equals(searchConditionDTOForMap
                                        .getPostPropertyCode())) {
                            bitmapId = R.drawable.tag09;
                        }
                        if (Constants.POST_PROPERTY_QUANZHI
                                .equals(searchConditionDTOForMap
                                        .getPostPropertyCode())) {
                            bitmapId = R.drawable.mark_multi_normal;
                        }
                    } else if (post.getIsMutiplePost() == 0) {
                        point = new LatLng(post.getSinglePostPoint().getLat(),
                                post.getSinglePostPoint().getLon());
                        if (Constants.POST_PROPERTY_JIANZHI
                                .equals(searchConditionDTOForMap
                                        .getPostPropertyCode())) {
                            bitmapId = R.drawable.tag06;
                        }
                        if (Constants.POST_PROPERTY_JIAQI
                                .equals(searchConditionDTOForMap
                                        .getPostPropertyCode())) {
                            bitmapId = R.drawable.tag07;
                        }
                        if (Constants.POST_PROPERTY_QUANZHI
                                .equals(searchConditionDTOForMap
                                        .getPostPropertyCode())) {
                            bitmapId = R.drawable.mark_normal;
                        }
                    }
                    bitmap = BitmapUtils.drawTextToBitmap(getActivity(),
                            bitmapId, count + "");
                    bitmapDesc = BitmapDescriptorFactory.fromBitmap(bitmap);
                    option = new MarkerOptions().position(point)
                            .icon(bitmapDesc).zIndex(1);

                    // 在地图上添加Marker，并显示

                    Bundle extra = new Bundle();
                    extra.putInt("count", count);
                    extra.putSerializable("post", points.getPostPoints().get(i));
                    Marker marker = (Marker) baiduMap.addOverlay(option);
                    marker.setExtraInfo(extra);

                    pointMarkers.add(marker);

                }
            } else {
                tips();
            }
            // 如果带有关键字、标签搜索,显示搜索条件和结果
            String conditionName = "";
            if (selectedTag != null) {
                conditionName = selectedTag.getTagName();
            }
            if (!StringUtils.isBlank(searchConditionDTOForMap.getKeyWord())) {
                conditionName = searchConditionDTOForMap.getKeyWord();
            }
            if (!StringUtils.isBlank(conditionName)) {
                if (points.getPostPoints() != null
                        && points.getPostPoints().size() > 0) {
                    conditionText.setText("找到  " + conditionName + " 的职位 "
                            + points.getTotalPosts() + "个");
                } else {
                    conditionText.setText("抱歉，没有找到 " + conditionName + " 的职位 ");
                }
                conditionPannelLl.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示单个标识点详细
     */
    void showSinglePointDetail(int total) {

        showMutilPointDetails(total);

    }

    /**
     * 显示标注点得详细列表
     */

    void showMutilPointDetails(int total) {

//		postCountTxt.setText("共" + showDetailsPostsCount + "个职位");

        // 标注点详细列表
        pointDetailsListView.setVisibility(View.VISIBLE);
        pointDetailContainer.setVisibility(View.VISIBLE);
        barContainer.setVisibility(View.GONE);
        pointDetailAdapter.updateItems(pointDetailOfPosts);

        // 设置预览的高度
        int items = pointDetailOfPosts.size();

        int height = UnitTransfer.dip2px(ZcdhApplication.getInstance(),
                90 * items);

        if (height > screenHeight / 2) {
            height = screenHeight / 2;
        }

        pointDetailsListView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, height));

        // 动画显示详细列表
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f, 0.9f);
        alphaAnimation.setDuration(350);
        pointDetailContainer.startAnimation(alphaAnimation);

        // 标识详细列表已经在显示状态
        Constants.isShowingPointDetails = true;

        // 将点击的标注点动画一到地图中心可见
        // 当详细列表显示的时候，计算地图的中心位置

        // 将地图中心店改为选中的标注点
        LatLng selectedLatlng = null;
        if (selectedPoint.getIsMutiplePost() == 1) {
            double lat = selectedPoint.getMultiPostPoint().getLat();
            double lng = selectedPoint.getMultiPostPoint().getLon();
            selectedLatlng = new LatLng(lat, lng);
        } else {
            double lat = selectedPoint.getSinglePostPoint().getLat();
            double lng = selectedPoint.getSinglePostPoint().getLon();
            selectedLatlng = new LatLng(lat, lng);
        }

        // 记录临时地图的中心坐标点
        tempCenter = baiduMap.getMapStatus().target;

        // 计算地图可见中心点
        int top = (map.getMapView().getMeasuredHeight() - height) / 2;
        int left = getResources().getDisplayMetrics().widthPixels / 2;

        MapStatus.Builder mb = new MapStatus.Builder();
        mb.target(selectedLatlng);
        mb.targetScreen(new Point(left, top));

        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mb
                .build()));
        // baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(selectedLatlng));
        // baiduMap.setMapStatus();

    }

    /**
     * 在职位列表中显示职位
     */
    void showPostInListView(boolean hasNextPage, int total) {

        listbarRl.setVisibility(View.VISIBLE);

        if (posts != null && posts.size() > 0) {

            String key = searchConditionDTOForFilter.getKeyWord();
            if (StringUtils.isBlank(key)) {
                key = searchConditionDTOForMap.getKeyWord();// 关键字
            }
            if (StringUtils.isBlank(key)) {
                key = conditionValuesName.get(1); // 职位类别
            }
            String cidi = "";
            String city = "";
            if (selectedAreas != null && selectedAreas.size() > 0) {
                for (String k : selectedAreas.keySet()) {
                    city += selectedAreas.get(k).getName() + ",";
                }
            } else {
                if (kLOAD_FOR_MAP_ONLY) {
                    city = "附近 ";
                } else {
                    city = "全国";
                }
            }
            cidi += city;
            if (!TextUtils.isEmpty(key)) {
                cidi += "," + key;
            }
            cidi += total + " 个职位";
            Log.i(TAG, cidi + "");
            resultDescriptionTxt.setText(cidi);
            postAdapter.updateItems(posts);
            emptyTipView.isEmpty(false);
            postListView.setVisibility(View.VISIBLE);
        } else {
            resultDescriptionTxt.setText("未找到任何相关的职位");
            postListView.setVisibility(View.GONE);
            emptyTipView.isEmpty(true);
        }
        this.hasNextPage = hasNextPage;
    }

    /**
     * 开始加载数据的动画
     */
    @UiThread
    void startLoadingAnimation() {
        loaderImg.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.loading));
        loaderImg.setVisibility(View.VISIBLE);
    }

    /**
     * 结束加载数据的动画
     */
    @UiThread
    void endLoadAnimation() {
        loaderImg.clearAnimation();
        loaderImg.setVisibility(View.GONE);
    }

    /**
     * 重置地图也的布局
     */
    public void resetLayoutOfMap() {

        searchBar.showOrHiddenSearchBar();
        searchBar.hiddenEditTextKeyboard();

        // 隐藏标注点详细
        pointDetailContainer.setVisibility(View.GONE);
        Constants.isShowingPointDetails = false;

        barContainer.setVisibility(View.VISIBLE);

    }

    void setMarkerTheme(Overlay overlayMarker, PostPointDTO point,
            boolean active) {
        BitmapDescriptor selectedBitmapDesc = null;
        int bitmapResid = -1;
        int count = 1;
        if (point.getIsMutiplePost() == 1) {
            count = point.getMultiPostPoint().getTotals();
            if (Constants.POST_PROPERTY_JIANZHI.equals(searchConditionDTOForMap
                    .getPostPropertyCode())) {
                if (active) {
                    bitmapResid = R.drawable.mark_multi_active;
                } else {
                    bitmapResid = R.drawable.tag08;
                }
            }
            if (Constants.POST_PROPERTY_JIAQI.equals(searchConditionDTOForMap
                    .getPostPropertyCode())) {

                if (active) {
                    bitmapResid = R.drawable.mark_multi_active;
                } else {
                    bitmapResid = R.drawable.tag09;
                }
            }
            if (Constants.POST_PROPERTY_QUANZHI.equals(searchConditionDTOForMap
                    .getPostPropertyCode())) {
                if (active) {
                    bitmapResid = R.drawable.mark_multi_active;
                } else {
                    bitmapResid = R.drawable.mark_multi_normal;
                }
            }
        } else {

            if (Constants.POST_PROPERTY_JIANZHI.equals(searchConditionDTOForMap
                    .getPostPropertyCode())) {
                if (active) {
                    bitmapResid = R.drawable.mark_active;
                } else {
                    bitmapResid = R.drawable.tag06;
                }
            }
            if (Constants.POST_PROPERTY_JIAQI.equals(searchConditionDTOForMap
                    .getPostPropertyCode())) {

                if (active) {
                    bitmapResid = R.drawable.mark_active;
                } else {
                    bitmapResid = R.drawable.tag07;
                }
            }
            if (Constants.POST_PROPERTY_QUANZHI.equals(searchConditionDTOForMap
                    .getPostPropertyCode())) {
                if (active) {
                    bitmapResid = R.drawable.mark_active;
                } else {
                    bitmapResid = R.drawable.mark_normal;
                }
            }
        }
        Bitmap bitmap = BitmapUtils.drawTextToBitmap(getActivity(),
                bitmapResid, count + "");
        selectedBitmapDesc = BitmapDescriptorFactory.fromBitmap(bitmap);
        Marker marker = (Marker) overlayMarker;
        marker.setIcon(selectedBitmapDesc);
    }

    /**
     * 提示用户没有找到相关的职位
     */
    void tips() {
        Toast.makeText(getActivity(), "当前区域没有找到任何合适的职位", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * 清除标注点
     */
    void clearMarkers() {
        BitmapUtils.BpRecyle(bitmap);
        for (int i = 0; i < pointMarkers.size(); i++) {
            pointMarkers.get(i).remove();
        }
    }

    /**
     * 显示我的位置
     */
    // TODO
    void showMyLocationPoint() {

        LatLng myLocationLatlng = ZcdhApplication.getInstance().getMyLocation();
        if (myLocationLatlng != null) {

            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }

            BitmapDescriptor bitmapDesc = BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_iam);

            MarkerOptions myLocation = new MarkerOptions()
                    .position(myLocationLatlng)
                    .icon(bitmapDesc)
                    .anchor(MapViewLayoutParams.ALIGN_CENTER_VERTICAL,
                            MapViewLayoutParams.ALIGN_CENTER_HORIZONTAL)
                    .zIndex(0);
            currentLocationMarker = (Marker) baiduMap.addOverlay(myLocation);

            pointMarkers.add(currentLocationMarker);
        }
    }

    public void refreshData() {
        if (searchBar != null) {
            searchBar.loadTagTypes();
            searchBar.loadMoreFuncs();
            loadPoints(KLAST_LOAD_FLAG);
        }
    }

    /**
     * 从职位列表切换到地图
     */
    public void switchMode() {
        onSwitchMode(searchBar.getSwitchModeBtn());
    }

    /**
     * 隐藏搜索bar面板
     */
    public void hiddenBar() {
        searchBar.showOrHiddenSearchBar();
    }

    @Override
    public void receive(String key, Object msg) {
        if (Constants.kEVENT_GET_SCREEN_BOUND.equals(key)) {
            send();
        }
    }

    @Background
    void send() {
        MyEvents.post(Constants.kEVENT_RECIVE_SCREEN_BOUND,
                map.getScreenBound());
    }

    public void onSelectPostType(String tempPropertyCode) {
        if (!tempPropertyCode.equals(postPropertyCode)) {
            hasMovingMap = true;
            postPropertyCode = tempPropertyCode;
            loadPoints(KLAST_LOAD_FLAG);
        }
    }
}
