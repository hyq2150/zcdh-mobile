/**
 * @author jeason, 2014-6-11 下午2:54:28
 */
package com.zcdh.mobile.app.activities.main;

import com.makeramen.RoundedImageView;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.IconPagerAdapter;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobUserPortraitDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.activities.main.MenuDialog.MenuDialogListener;
import com.zcdh.mobile.app.activities.messages.MessagesFragment;
import com.zcdh.mobile.app.activities.messages.MessagesFragment_;
import com.zcdh.mobile.app.activities.nearby.AddMoreFuncActivity;
import com.zcdh.mobile.app.activities.nearby.NearbyMapFragment;
import com.zcdh.mobile.app.activities.nearby.NearbyMapFragment_;
import com.zcdh.mobile.app.activities.newsmodel.AddMoreDiscoverActivity;
import com.zcdh.mobile.app.activities.newsmodel.FragmentNewsModel;
import com.zcdh.mobile.app.activities.newsmodel.FragmentNewsModel_;
import com.zcdh.mobile.app.activities.search.AdvancedSearchActivity;
import com.zcdh.mobile.app.activities.search.AreaActivity;
import com.zcdh.mobile.app.views.NewViewPager;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.ImageUtils;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.UnitTransfer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-6-11 下午2:54:28 程序主框架Fragment
 *         内嵌附近NearbyMapFragment，消息MessagesFragment，发现FragmentNewsModel
 *
 */
@SuppressLint("InflateParams")
@EFragment(R.layout.main_frame)
public class MainPageFragment extends BaseFragment implements
        RequestListener, MenuDialogListener {

    private static final String TAG = MainPageFragment.class.getSimpleName();

    private String kREQ_ID_findUserPortrait;

    private IRpcJobUservice jobUservice;

    @ViewById(R.id.rl_nav)
    RelativeLayout rl_nav;

    @ViewById(R.id.attractPointImg)
    ImageView attractPointImg;

    @ViewById(R.id.rg_nav_content)
    RadioGroup rg_nav_content;

    @ViewById(R.id.iv_nav_indicator)
    LinearLayout iv_nav_indicator;

    @ViewById(R.id.iv_nav_left)
    ImageView iv_nav_left;

    @ViewById(R.id.iv_nav_right)
    ImageView iv_nav_right;

    @ViewById(R.id.head)
    RoundedImageView head;

    private int titleWidth;

    private int indicatorWidth = 48;// px

    private LayoutInflater mInflater;

    private int currentIndicatorLeft = 0;

    protected String[] tabTitle = {"附近", "消息", "发现"}; // 标题

    protected int screenHeight;

    protected int screenWidth;

    protected boolean specialIndex;

    private MenuDialog menuDialog;

    @ViewById(R.id.pager)
    NewViewPager mViewPager;

    private List<Fragment> fragments = new ArrayList<Fragment>();

    // 头像
    @ViewById(R.id.btn_menu)
    ImageButton btn_menu;

    /**
     * 附近
     */
    private NearbyMapFragment nearByFragment;

    /**
     * 消息
     */
    private MessagesFragment messageFragment;

    /**
     * 发现
     */
    private FragmentNewsModel explorerFragment;

    private MainFragmentsAdapter adapter;

    private NewMainActivity mainActivity;

    private BroadcastReceiver reconnectReceiver;

    private boolean hasNewMsg = false;

    public NearbyMapFragment getNearbyFragment() {
        return this.nearByFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mainActivity = (NewMainActivity) getActivity();

        DisplayMetrics metrics = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        jobUservice = RemoteServiceManager
                .getRemoteService(IRpcJobUservice.class);

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }


    @Override
    public void onDestroy() {
//        getActivity().unregisterReceiver(reconnectReceiver);
        super.onDestroy();
    }

    @AfterViews
    void bindViews() {
        menuDialog = new MenuDialog(mainActivity, this);
        if (Build.VERSION.SDK_INT >= 10) {
            menuDialog
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            onHidden();
                        }
                    });
        }

        nearByFragment = new NearbyMapFragment_();
        messageFragment = new MessagesFragment_();
        explorerFragment = new FragmentNewsModel_();
        messageFragment.setCallbacker(this);
        fragments.add(nearByFragment);
        fragments.add(messageFragment);
        fragments.add(explorerFragment);

        // 内嵌Fragment必须传入getChildFragmentManager() 否则导致界面操作异常
        adapter = new MainFragmentsAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setPageTransformer(true, new PagerTransformer());
        // 初始化Tabbar
        initTabBar();
        ((RadioButton) rg_nav_content.getChildAt(0)).performClick();
        setListener();

        // 加载头像
        if (getUserId() > 0) {
            loadPortal();
        } else {
            // 判断是否自动显示菜单
            if (!SharedPreferencesUtil.getValue(getActivity(),
                    Constants.kAUTO_MENU, false)) {
                menuDialog.show();
            }
        }
    }

    /**
     * 个人头像
     */
    @Background
    public void loadPortal() {
        if (getUserId() > 0) {
            jobUservice
                    .findUserPortrait(getUserId())
                    .identify(
                            kREQ_ID_findUserPortrait = RequestChannel
                                    .getChannelUniqueID(),
                            this);
        } else {
            setPortrait(null);
        }
    }

    @UiThread
    void setPortrait(final String url) {
        if (StringUtils.isBlank(url)) {
            head.setImageResource(R.drawable.menumain_normal);
        } else {
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    try {
                        return ImageUtils.GetBitmapByUrl(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                protected void onPostExecute(Bitmap result) {
                    if (result != null) {
                        head.setImageBitmap(result);
//						btn_menu.setVisibility(View.GONE);
                        attractPointImg.setVisibility(View.GONE);
//						head.setVisibility(View.VISIBLE);
                    }
                }
            }.execute();
        }
    }

    /*public void setPortraitByBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            head.setImageBitmap(bitmap);
        }
    }
*/
    private void setListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                if (rg_nav_content != null
                        && rg_nav_content.getChildCount() > position) {
                    ((RadioButton) rg_nav_content.getChildAt(position))
                            .performClick();
                    nearByFragment.closeAction();
                }
                // 加载消息数据
                if (position == 1) {
                    messageFragment.loadData();
                }

                // 加载发现的数据
                if (position == 2) {
                    MobclickAgent.onEvent(mainActivity, Constants.UMENG_ENTER_FIND_PAGE);
                    explorerFragment.loadData();
                }
            }
        });

        rg_nav_content
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (rg_nav_content.getChildAt(checkedId) != null) {

                            if (specialIndex) {
                                specialIndex = false;
                                int currentIndicatorLeft_first = screenWidth
                                        / adapter.getCount()
                                        * mViewPager.getCurrentItem();
                                indicatorAnimation(currentIndicatorLeft_first);

                                mViewPager.setCurrentItem(checkedId, false); // ViewPager

                            } else {

                                indicatorAnimation(((RadioButton) rg_nav_content
                                        .getChildAt(checkedId))
                                        .getLeft());

                                mViewPager.setCurrentItem(checkedId); // ViewPager
                                // 跟随一起
                                // 切换

                                // 记录当前 下标的距最左侧的 距离
                                currentIndicatorLeft = ((RadioButton) rg_nav_content
                                        .getChildAt(checkedId)).getLeft();
                            }

                        }
                    }
                });
    }

    /**
     * 指示器执行位移动画
     * @param xDelta
     */
    private void indicatorAnimation(int xDelta) {
        TranslateAnimation animation = new TranslateAnimation(
                currentIndicatorLeft,
                xDelta, 0f, 0f);
        animation
                .setInterpolator(new LinearInterpolator());
        animation.setDuration(300);
        animation.setFillAfter(true);

        // 执行位移动画
        iv_nav_indicator.startAnimation(animation);
    }

    private void initTabBar() {
        DisplayMetrics dm = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.i(TAG, "screen width:" + dm.widthPixels);

        titleWidth = dm.widthPixels / (tabTitle.length + 1);

        Log.i(TAG, "indicatorWidth:" + titleWidth);
        indicatorWidth = UnitTransfer.dip2px(mainActivity, indicatorWidth);

        RelativeLayout.LayoutParams cursor_Params = (RelativeLayout.LayoutParams) iv_nav_indicator
                .getLayoutParams();
        cursor_Params.width = titleWidth;// 初始化滑动下标的宽
        iv_nav_indicator.setLayoutParams(cursor_Params);
        // mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, getActivity());

        // 获取布局填充器
        mInflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        // 另一种方式获取
        // LayoutInflater mInflater = LayoutInflater.from(this);

        initNavigationHSV();

        mViewPager.setOffscreenPageLimit(tabTitle.length);
    }

    private void initNavigationHSV() {

        rg_nav_content.removeAllViews();

        for (int i = 0; i < tabTitle.length; i++) {
            RadioButton rb = (RadioButton) mInflater.inflate(
                    R.layout.main_tab_nav_radiogroup_item, null);
            rb.setId(i);
            rb.setText(tabTitle[i]);
            rb.setTextColor(rg_nav_content.getResources().getColor(R.color.white));
            RadioGroup.LayoutParams layout = new RadioGroup.LayoutParams(
                    titleWidth, LayoutParams.MATCH_PARENT);
            // layout.leftMargin = titleWidth/2;
			/*
			 * if(i==0){ layout.gravity = Gravity.RIGHT; }
			 * if(i==tabTitle.length-1){ layout.gravity = Gravity.LEFT; }
			 */
            rb.setLayoutParams(layout);

            rg_nav_content.addView(rb);
        }

    }

    @Click(R.id.menuToggleLl)
    void onMenuToggler() {
        nearByFragment.closeAction();
        menuDialog.show();
    }

    @Click(R.id.btn_menu)
    void onMenuOpen() {
        // mainActivity.onToggleMenu();
        nearByFragment.closeAction();
        menuDialog.show();
    }

    @Click(R.id.head)
    void onPortraitClick() {
        // mainActivity.onToggleMenu();
        nearByFragment.closeAction();
        menuDialog.show();
    }

    // MessagesFragment 回调显示红点
    public void notifyUnReaded() {

        hasNewMsg = true;
        rg_nav_content.getChildAt(1)
                .setBackgroundResource(R.drawable.red_point);

    }

    public void notifyReaded() {

        rg_nav_content.getChildAt(1).setBackgroundResource(R.color.blues);
        rg_nav_content.getChildAt(0).setBackgroundResource(R.color.blues);
        rg_nav_content.getChildAt(2).setBackgroundResource(R.color.blues);
    }

    /**
     * 刷新消息
     */
    public void refreshMessage() {
        Log.i(TAG, "refreshMessage");
        messageFragment.findMsgs();
    }

    public void refreshData() {
        messageFragment.findMsgs();
        explorerFragment.doLoadAds();
        nearByFragment.loadData();
    }

    public void reloadPostForNearby() {
        nearByFragment.loadData();
    }

    /** ========== onActivityResult dispatcher =========== */
    public void onResultDispatch(int requestCode, int resultCode, Intent data) {

        if (requestCode == AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH) {
            onResultAdvanceCondition(resultCode, data);
        }
        if (requestCode == AreaActivity.kREQUEST_AREA) {
            onResultArea(resultCode, data);
        }

        if (requestCode == AddMoreDiscoverActivity.kREQUEST_ADD_DISCOVERY) {
            onResultAddDiscovery(resultCode, data);
        }
        if (requestCode == AddMoreFuncActivity.kREQUEST_ADD_FUNCS) {
            onResultAddFuncs(resultCode, data);
        }
    }

    /**
     * 添加更多功能
     *
     * @param resultCode
     */
    void onResultAddFuncs(int resultCode, Intent data) {
        nearByFragment.onResultAddFuncs(resultCode, data);
    }

    /**
     * 添加发现模块
     *
     * @param resultCode
     * @param Data
     */
    void onResultAddDiscovery(int resultCode, Intent Data) {
        explorerFragment.onResultAddDiscovery(resultCode, Data);
    }

    /**
     * 高级搜索条件
     *
     * @param resultCode
     * @param data
     */
    void onResultAdvanceCondition(int resultCode, Intent data) {
        nearByFragment.onResultCondition(resultCode, data);
    }

    /**
     * 选择地区
     *
     * @param resultCode
     * @param data
     */
    void onResultArea(int resultCode, Intent data) {
        nearByFragment.onResultArea(resultCode, data);
    }

    private class MainFragmentsAdapter extends FragmentPagerAdapter implements
            IconPagerAdapter {

        int[] titleRes = {R.string.nearby, R.string.message, R.string.discover};

        public MainFragmentsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getIconResId(int index) {
            if (index == 1 && hasNewMsg && mViewPager.getCurrentItem() != 1) {
                return R.drawable.redpoint;
            }
            return 0;

        }

        @Override
        public Fragment getItem(int p) {
            return fragments.get(p);
        }

        @Override
        public int getCount() {

            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(titleRes[position]);
        }

    }

    @Override
    public void onRequestStart(String reqId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestSuccess(String reqId, Object result) {
        if (reqId.equals(kREQ_ID_findUserPortrait)) {
            if (result != null) {
                JobUserPortraitDTO portraitDTO = (JobUserPortraitDTO) result;
                if (portraitDTO.getPortrait() != null
                        && !StringUtils.isBlank(portraitDTO.getPortrait()
                        .getMedium())) {
                    setPortrait(portraitDTO.getPortrait().getMedium());
                } else {
                    setPortrait(null);
                }
            }
        }
    }

    @Override
    public void onRequestFinished(String reqId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        // TODO Auto-generated method stub

    }

    public void reloadMenuDialog() {
        menuDialog.dismiss();
        menuDialog = new MenuDialog(mainActivity, this);
        menuDialog.show();

    }

    @Override
    public void onHidden() {
        // 判断是否自动显示菜单
        if (!SharedPreferencesUtil.getValue(getActivity(),
                Constants.kAUTO_MENU, false)) {
            SharedPreferencesUtil.putValue(getActivity(), Constants.kAUTO_MENU,
                    true);
            nearByFragment.showNewUserGuide();
        }
    }

}
