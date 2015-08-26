package com.zcdh.mobile.app.activities.newsmodel;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcCUCCSpecialService;
import com.zcdh.mobile.api.model.InformationCoverDTO;
import com.zcdh.mobile.api.model.InformationTitleDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.adapter.RecyclingPagerAdapter;
import com.zcdh.mobile.app.adapter.TitlesAdapter;
import com.zcdh.mobile.app.maps.bmap.MyBLocationCient;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestException;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.GridViewInScrollView;
import com.zcdh.mobile.framework.widget.AutoScrollViewPager;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
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
import java.util.HashMap;
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
    private boolean flagLoaded;

    @ViewById(R.id.scrollView)
    PullToRefreshScrollView scrollView;

    @ViewById(R.id.emptyView)
    EmptyTipView emptyTipView;

    @ViewById(R.id.pager)
    AutoScrollViewPager pager;

    private IRpcCUCCSpecialService cUCCSpecialService;

    private List<InformationCoverDTO> covers = new ArrayList<InformationCoverDTO>();

    private List<InformationTitleDTO> titles = new ArrayList<InformationTitleDTO>();

    private String kREQ_ID_FINDINFORMATIONCOVERLIST;

    private String kREQ_ID_FINDINFORMATIONTITLELIST;

    private RecyclingPagerAdapter adapter;

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

    }

    /**
     * 此方法在父 Activity 中调用 加载数据，先定位，定位完成拿到地理位置后下一步加载广告
     */
    public void loadData() {
        if (!flagLoaded) {
            // 1) 定位 ， 下一步加载广告
            locationCient = new MyBLocationCient(getActivity(), this);
            locationCient.requestLocation();

        }
    }

    @ItemClick(R.id.grid_view)
    void onItemClick(int position) {
        if (position == titles.size()) {
            if (getUserId() >= 1) {
                HashMap<Long, InformationTitleDTO> selectedFuncs
                        = new HashMap<Long, InformationTitleDTO>();
                for (InformationTitleDTO title : titles) {
                    selectedFuncs.put(title.getId(), title);
                }
                AddMoreDiscoverActivity_
                        .intent(getActivity())
                        .selectedFuncs(selectedFuncs)
                        .startForResult(
                                AddMoreDiscoverActivity.kREQUEST_ADD_DISCOVERY);
            } else {
                ToastUtil.show(R.string.login_first);
                ActivityDispatcher.to_login(getActivity());
            }
        } else {
            InformationTitleDTO title = titles.get(position);
            switch (title.getOpenType()) {
                case 1:
                    Bundle data = new Bundle();
                    for (Entry<String, String> param : StringUtils.getParams(
                            title.getCustomParam()).entrySet()) {
                        Log.e("FragmentNewsModel", param.getKey() + " : " + param.getValue());
                        data.putString(param.getKey(), param.getValue());
                    }
//				data.putString("isShowTitle", title.getIsShowTitle() + "");
                    NewsBrowserActivity_.IntentBuilder_ ib = NewsBrowserActivity_
                            .intent(getActivity());
                    ib.get().putExtras(data);
                    ib.title(title.getTitle());
                    ib.url(title.getAnroidURL()).start();
                    break;
                case 2:
                    Class<?> activity_cls = SystemServicesUtils.getClass(title
                            .getAnroidURL());
                    Log.e("FragmentNewsModel", "activity_cls : " + activity_cls);
                    if (activity_cls != null) {
                        Intent intent = new Intent(getActivity(), activity_cls);
                        Bundle data1 = new Bundle();
                        for (Entry<String, String> param : StringUtils.getParams(
                                title.getCustomParam()).entrySet()) {
                            Log.e("FragmentNewsModel", param.getKey() + " : " + param.getValue());
                            data1.putString(param.getKey(), param.getValue());
                        }
                        data1.putLong(NewsListActivity.kMODEL_ID, title.getId());
                        data1.putString("title", title.getTitle());
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
     * @author jeason, 2014-6-6 下午8:00:08
     */
    @Background
    void doLoadApps() {
        LatLng center = ZcdhApplication.getInstance().getMyLocation();
        cUCCSpecialService
                .findInformationTitle1(getUserId(), center.longitude,
                        center.latitude, 1, 100)
                .identify(
                        kREQ_ID_FINDINFORMATIONTITLELIST = RequestChannel
                                .getChannelUniqueID(),
                        this);
    }

    /**
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onReceiveLocation(BDLocation loc) {
        LatLng point = new LatLng(loc.getLatitude(), loc.getLongitude());
        ZcdhApplication.getInstance().setMyLocation(point);
        locationCient.stop();
                /*
                 * 2) 定位完成，加载广告
		 */
        doLoadAds();
    }

    /**
     * 添加发现模块返回值
     */
    public void onResultAddDiscovery(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
            this.covers = new ArrayList<InformationCoverDTO>();
            this.titles = new ArrayList<InformationTitleDTO>();
            doLoadAds();
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

        if (reqId.equals(kREQ_ID_FINDINFORMATIONTITLELIST)) {
            if (result != null) {
                Page<InformationTitleDTO> page = (Page<InformationTitleDTO>) result;
                titles = page.getElements();
                title_adapter.setTitles(titles);
                flagLoaded = true;
                // contentView.setVisibility(View.VISIBLE);
                modelsContainer.setVisibility(View.VISIBLE);
            }
            if (covers != null && covers.size() > 0) {
                adsContainer.setVisibility(View.VISIBLE);
            }
            emptyTipView.isEmpty(false);
        }
    }

    @Override
    public void onRequestFinished(String reqId) {
        // TODO Auto-generated method stub
        scrollView.onRefreshComplete();
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        flagLoaded = false;
        if (error != null) {
            RequestException exception = (RequestException) error;
            emptyTipView.showException(exception.getErrCode(), this);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        pager.stopAutoScroll();
        doLoadAds();
    }
}
