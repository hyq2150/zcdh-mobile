package com.zcdh.mobile.app.activities.newsmodel;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcAppConfigService;
import com.zcdh.mobile.api.IRpcCUCCSpecialService;
import com.zcdh.mobile.api.model.AdminAppConfigModelDTO;
import com.zcdh.mobile.api.model.InformationCoverDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.adapter.RecyclingPagerAdapter;
import com.zcdh.mobile.app.maps.bmap.MyBLocationCient;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.GridViewInScrollView;
import com.zcdh.mobile.framework.widget.AutoScrollViewPager;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.SystemUtil;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * 发现Fragment
 *
 * @author yangjiannan
 */
@EFragment(R.layout.fragment_news_model)
public class FragmentNewsModel extends BaseFragment implements
        BDLocationListener, RequestListener, DataLoadInterface,
        OnRefreshListener<ScrollView> {

    private static final String TAG = FragmentNewsModel.class.getSimpleName();

    /**
     * 标识是否加载完成
     */
    private boolean isAdLoaded = false;//广告

    @ViewById(R.id.scrollView)
    PullToRefreshScrollView scrollView;

    @ViewById(R.id.emptyView)
    EmptyTipView emptyTipView;

    @ViewById(R.id.pager)
    AutoScrollViewPager pager;

    private IRpcCUCCSpecialService cUCCSpecialService;

    private IRpcAppConfigService appConfigService;

    private List<InformationCoverDTO> covers = new ArrayList<>();

    private List<AdminAppConfigModelDTO> titles = new ArrayList<>();

    private String kREQ_ID_FINDINFORMATIONCOVERLIST;

    private String kREQ_ID_findAppConfigModelByParentId;
    private String KREQ_ID_FIND_APPCONFIG_MODULE_MAIN_PAGE;
    //轮播广告对应的适配器
    private RecyclingPagerAdapter adapter;

    //模块GridView对应的适配器
    private TitlesAdapter title_adapter;

    @ViewById(R.id.grid_view)
    GridViewInScrollView grid_view;

    private float pagerHeight = 0;

    @ViewById(R.id.adsContainer)
    LinearLayout adsContainer;

    @ViewById(R.id.modelsContainer)
    LinearLayout modelsContainer;

    private MyBLocationCient locationCient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cUCCSpecialService = RemoteServiceManager
                .getRemoteService(IRpcCUCCSpecialService.class);
        appConfigService = RemoteServiceManager.getRemoteService(IRpcAppConfigService.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @AfterViews
    void afterViews() {

        scrollView.setOnRefreshListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        pagerHeight = dm.widthPixels * (3 / 4.0f);
        LayoutParams pagerLayoutParam = (LayoutParams) pager.getLayoutParams();
        pagerLayoutParam.height = (int) pagerHeight;
        pager.setLayoutParams(pagerLayoutParam);

        // ScrollView滚动至最顶端，以显示搜索历史的ListView
        // scrollView.smoothScrollBy(0, 0);

        adapter = new RecyclingPagerAdapter(getActivity(), covers);
        pager.setInterval(3000);
        pager.setAdapter(adapter);

        title_adapter = new TitlesAdapter(getActivity(), titles);
        grid_view.setAdapter(title_adapter);
        // doGetImage();

        loadData();
    }

    /**
     * 此方法在父 Activity 中调用 加载数据，先定位，定位完成拿到地理位置后下一步加载广告
     */
    public void loadData() {
        if (!isAdLoaded) {
            // 1) 定位 ， 下一步加载广告
            locationCient = new MyBLocationCient(getActivity(), this);
            locationCient.requestLocation();
        }
    }

    @ItemClick(R.id.grid_view)
    void onItemClick(int position) {
        if (position == titles.size()) {
            if (getUserId() >= 1) {
                AddMoreDiscoverActivity_
                        .intent(getActivity())
                        .startForResult(
                                AddMoreDiscoverActivity.kREQUEST_ADD_DISCOVERY);
            } else {
                ToastUtil.show(R.string.login_first);
                ActivityDispatcher.toLogin(getActivity());
            }
        } else {
            AdminAppConfigModelDTO title = titles.get(position);
            switch (Integer.valueOf(title.getOpen_type())) {
                case 1:
                    Bundle data = new Bundle();
                    for (Entry<String, String> param : StringUtils.getParams(
                            title.getCustom_param()).entrySet()) {
                        Log.e("FragmentNewsModel", param.getKey() + " : " + param.getValue());
                        data.putString(param.getKey(), param.getValue());
                    }
//				data.putString("isShowTitle", title.getModel_name() + "");
                    NewsBrowserActivity_.IntentBuilder_ ib = NewsBrowserActivity_
                            .intent(getActivity());
                    ib.get().putExtras(data);
                    ib.title(title.getModel_name());
                    if (title.getIs_url_autofilluserid() != null && title.getIs_url_autofilluserid() == 1) {
                        if (getUserId() >= 1) {
                            ib.url(title.getModelUrl() + "?uid=" + getUserId()).start();
                        } else {
                            ToastUtil.show(R.string.login_first);
                            ActivityDispatcher.toLogin(getActivity());
                        }
                    } else {
                        ib.url(title.getModelUrl()).start();
                    }

                    break;
                case 2:
                    Class<?> activity_cls = SystemServicesUtils.getClass(title
                            .getModelUrl());
                    if (activity_cls != null) {
                        Intent intent = new Intent(getActivity(), activity_cls);
                        Bundle data1 = new Bundle();
                        for (Entry<String, String> param : StringUtils.getParams(
                                title.getCustom_param()).entrySet()) {
                            data1.putString(param.getKey(), param.getValue());
                        }
                        data1.putLong(NewsListActivity.kMODEL_ID, title.getReference_id());
                        data1.putString("title", title.getModel_name());
                        data1.putBoolean("fromMessageCenter", true);
                        intent.putExtras(data1);
                        getActivity().startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 请求各个子模块数据
     *
     * @author jeason, 2014-6-6 下午8:00:08
     */
    @Background
    void doLoadApps() {
        appConfigService
                .findAppConfigModelByParentId(
                        getUserId(),
                        SharedPreferencesUtil.getValue(getActivity(), FragmentNewsModel.class.getSimpleName(), 0l),
                        ZcdhApplication.getInstance().getMyLocation().latitude,
                        ZcdhApplication.getInstance().getMyLocation().longitude,
                        Constants.DEVICES_TYPE,
                        SystemUtil.getVerCode(getActivity()),
                        SystemServicesUtils.getAppID(getActivity()))
                .identify(kREQ_ID_findAppConfigModelByParentId = RequestChannel.getChannelUniqueID(), this);
    }

    /**
     * 请求广告轮播数据
     *
     * @author jeason, 2014-6-6 下午7:11:29
     */
    @Background
    public void doLoadAds() {
        if (cUCCSpecialService != null) {
            LatLng center = ZcdhApplication.getInstance().getMyLocation();
            cUCCSpecialService
                    .findAppCoverDTO(getUserId(), 3 /* 标识android */,
                            center.longitude, center.latitude)
                    .identify(
                            kREQ_ID_FINDINFORMATIONCOVERLIST = RequestChannel
                                    .getChannelUniqueID(),
                            this);
        }
    }

    //定位回调
    @Override
    public void onReceiveLocation(BDLocation loc) {
        LatLng point = new LatLng(loc.getLatitude(), loc.getLongitude());
        ZcdhApplication.getInstance().setMyLocation(point);
        locationCient.stop();
        /**
         * 2) 定位完成，加载广告
         */
        doLoadAds();
    }

    /**
     * 添加发现模块返回值
     */
    public void onResultAddDiscovery(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            doLoadApps();
        }
    }

    @Override
    public void onRequestStart(String reqId) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRequestSuccess(String reqId, Object result) {
        //加载图片轮播
        if (reqId.equals(kREQ_ID_FINDINFORMATIONCOVERLIST)) {
            if (result != null) {
                covers = (List<InformationCoverDTO>) result;
                adapter.setCovers(covers);
                if (covers.size() > 1) {
                    adapter.setInfiniteLoop(true);
                    adapter.notifyDataSetChanged();
                    pager.startAutoScroll();
                } else {
                    adapter.setInfiniteLoop(false);
                    adapter.notifyDataSetChanged();
                    pager.stopAutoScroll();
                }
            }
            //加载各个项目
            doLoadApps();
        }

        if (reqId.equals(KREQ_ID_FIND_APPCONFIG_MODULE_MAIN_PAGE)) {
            ArrayList<AdminAppConfigModelDTO> appConfigList = (ArrayList<AdminAppConfigModelDTO>) result;
            if (appConfigList != null && appConfigList.size() > 0) {
                for (AdminAppConfigModelDTO dto : appConfigList) {
                    if (dto.getCustom_item_code() != null && dto.getCustom_item_code().equals(Constants.APPCONFIG_CUSTOM_ITEM_CODE_MAIN)) {
                        SharedPreferencesUtil.putValue(ZcdhApplication.getInstance(), Constants.APPCONFIG_CUSTOM_ITEM_CODE_MAIN, dto.getId());
                    } else if (dto.getCustom_item_code() != null && dto.getCustom_item_code().equals(Constants.APPCONFIG_CUSTOM_ITEM_CODE_INFORMATION)) {
                        SharedPreferencesUtil.putValue(ZcdhApplication.getInstance(), Constants.APPCONFIG_CUSTOM_ITEM_CODE_INFORMATION, dto.getId());
                    }
                }
                doLoadApps();
            }
        }

        if (reqId.equals(kREQ_ID_findAppConfigModelByParentId)) {
            isAdLoaded = true;
            List<AdminAppConfigModelDTO> appconfig = (List<AdminAppConfigModelDTO>) result;
            if (appconfig == null || appconfig.isEmpty()) {
                emptyTipView.isEmpty(true);
                return;
            }
            titles.clear();
            titles.addAll(appconfig);
            title_adapter.setTitles(titles);
            modelsContainer.setVisibility(View.VISIBLE);
            if (covers != null && covers.size() > 0) {
                adsContainer.setVisibility(View.VISIBLE);
            }
            emptyTipView.isEmpty(!(covers != null && covers.size() > 0));
        }
    }

    @Override
    public void onRequestFinished(String reqId) {
        // TODO Auto-generated method stub
        if (scrollView.isRefreshing()) {
            scrollView.onRefreshComplete();
        }
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        isAdLoaded = false;
        if (error != null) {
            ZcdhException exception = (ZcdhException) error;
            emptyTipView.showException(exception.getErrCode(), this);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        pager.stopAutoScroll();
        doLoadAds();
    }
}