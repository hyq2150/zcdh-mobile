/**
 * @author jeason, 2014-6-11 下午2:54:28
 */
package com.zcdh.mobile.app.activities.main;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.huanxin.applib.controller.HXSDKHelper;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcAppConfigService;
import com.zcdh.mobile.api.model.AdminAppConfigModelDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
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
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.SystemUtil;
import com.zcdh.mobile.widget.IndicatorLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-6-11 下午2:54:28 程序主框架Fragment
 *         内嵌附近NearbyMapFragment，消息MessagesFragment，发现FragmentNewsModel
 */
@EFragment(R.layout.main_frame)
public class MainPageFragment extends BaseFragment implements
        RequestListener, MenuDialogListener,
        MessagesFragment.OnMessageListener, EMEventListener {

    private static final String TAG = MainPageFragment.class.getSimpleName();

    //获取模块配置
    private IRpcAppConfigService appConfigService;

    @ViewById(R.id.il_main)
    IndicatorLayout mIndicatorLayout;

    @ViewById(R.id.attractPointImg)
    ImageView attractPointImg;

    @ViewById(R.id.pager)
    NewViewPager mViewPager;

    private MenuDialog menuDialog;

    private List<Fragment> fragments = new ArrayList<>();

    private List<String> titles = new ArrayList<>();

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

    private String KREQ_ID_FIND_APPCONFIG_MODULE_MAIN_PAGE;
    private String KREQ_ID_FIND_APPCONFIG_MODULE_BY_ID;

    //环信连接监听
    private MyConnectionListener connectionListener = null;

    public NearbyMapFragment getNearbyFragment() {
        return nearByFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        // register the event listener when enter the foreground
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage,
                        EMNotifierEvent.Event.EventOfflineMessage,
                        EMNotifierEvent.Event.EventConversationListChanged});
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (connectionListener != null) {
            EMChatManager.getInstance().removeConnectionListener(connectionListener);
        }
    }

    @AfterViews
    void bindViews() {

        mainActivity = (NewMainActivity) getActivity();

        appConfigService = RemoteServiceManager
                .getRemoteService(IRpcAppConfigService.class);

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
        messageFragment.setOnMessageListener(this);
        fragments.add(nearByFragment);
        fragments.add(messageFragment);
        fragments.add(explorerFragment);

        // 内嵌Fragment必须传入getChildFragmentManager() 否则导致界面操作异常
        adapter = new MainFragmentsAdapter(getChildFragmentManager(), fragments, getActivity());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setPageTransformer(true, new PagerTransformer());

        // 初始化Tabbar
        initTabBar();
        setListener();

        if (getUserId() < 0) {
            // 判断是否自动显示菜单
            if (!SharedPreferencesUtil.getValue(getActivity(),
                    Constants.kAUTO_MENU, false)) {
                menuDialog.show();
            }
        }

        //如果已经登录，则根据不同用户角色，获取服务端的动态模块配置
        if (getUserId() > 1) {
            refreshAppModule();
        }
        connectionListener = new MyConnectionListener();
        EMChatManager.getInstance().addConnectionListener(connectionListener);
    }

    @Background
    void findAppConfigModelByID(long modelID) {
        appConfigService
                .findAppConfigModelByParentId(
                        ZcdhApplication.getInstance().getZcdh_uid(),
                        modelID,
                        ZcdhApplication.getInstance().getMyLocation().latitude,
                        ZcdhApplication.getInstance().getMyLocation().longitude,
                        Constants.DEVICES_TYPE,
                        SystemUtil.getVerCode(mainActivity),
                        SystemServicesUtils.getAppID(mainActivity))
                .identify(
                        KREQ_ID_FIND_APPCONFIG_MODULE_BY_ID = RequestChannel
                                .getChannelUniqueID(),
                        MainPageFragment.this);
    }

    /**
     *

    @Background
    void findAppConfigModelInfo() {
        appConfigService.findAllAppConfigModelInfo(
                ZcdhApplication.getInstance().getZcdh_uid(),
                ZcdhApplication.getInstance().getMyLocation().latitude,
                ZcdhApplication.getInstance().getMyLocation().longitude,
                Constants.DEVICES_TYPE,
                SystemUtil.getVerCode(mainActivity),
                SystemServicesUtils.getAppID(mainActivity)
        ).identify(
                KREQ_ID_FIND_APPCONFIG_MODULE_MAIN_PAGE = RequestChannel
                        .getChannelUniqueID(),
                MainPageFragment.this);
    }
     */
    private void setListener() {

       /* mIndicatorLayout.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                nearByFragment.closeAction();
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
        });*/
    }

    private void initTabBar() {
        mIndicatorLayout.setViewPager(mViewPager);
        setTabTextStyle();
    }

    public void setTabTextStyle() {
        mIndicatorLayout.setTextColorResource(R.color.white);
        mIndicatorLayout.setTextSize((int) getResources().getDimension(R.dimen.maintab_title_size));
    }

    @Click(R.id.fl_menu)
    void onMenuToggler() {
        nearByFragment.closeAction();
        menuDialog.show();
    }

    // MessagesFragment 回调显示红点
    //新的未读消息
    public void onNotifyUnReaded() {
        if (mIndicatorLayout.getChildCount() > 2) {
            mIndicatorLayout.setSingleTabBackgroundResId(1, R.drawable.red_point);
        }
    }

    //消息已读，没有新的消息
    public void onNotifyReaded() {
        if (mIndicatorLayout.getChildCount() > 2) {
            mIndicatorLayout.setSingleTabBackgroundResId(1, R.color.transparent);
        }
    }

    /**
     * 刷新模块
     */
    public void refreshAppModule() {
        findAppConfigModelByID(SharedPreferencesUtil.getValue(getActivity(),Constants.APPCONFIG_CUSTOM_ITEM_CODE_MAIN,0l));
    }

    /**
     * 网络状态改变时，重新加载数据
     */
    public void refreshData() {
        messageFragment.findMsgs();
        explorerFragment.doLoadAds();
        nearByFragment.loadData();
        refreshHeadTips();
    }

    /**
     * 更新头像旁的小红点
     */
    public void refreshHeadTips() {
        int visible = getUserId() > 0 ? View.INVISIBLE : View.VISIBLE;
        attractPointImg.setVisibility(visible);
    }

    public void reloadPostForNearby() {
        nearByFragment.loadData();
    }

    /**
     * ========== onActivityResult dispatcher ===========
     */
    public void onResultDispatch(int requestCode, int resultCode, Intent data) {

        if (requestCode == AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH) {
            nearByFragment.onResultCondition(resultCode, data);
        }
        if (requestCode == AreaActivity.kREQUEST_AREA) {
            nearByFragment.onResultArea(resultCode, data);
        }

        if (requestCode == AddMoreDiscoverActivity.kREQUEST_ADD_DISCOVERY) {
            explorerFragment.onResultAddDiscovery(resultCode, data);
        }
        if (requestCode == AddMoreFuncActivity.kREQUEST_ADD_FUNCS) {
            nearByFragment.onResultAddFuncs(resultCode, data);
        }
        if (requestCode == Constants.REQUEST_CODE_MESSAGE) {
            messageFragment.checkHuanxinMsg();
        }
    }

    static class MainFragmentsAdapter extends FragmentPagerAdapter {

        private boolean isDefault = true;

        private List<Fragment> list;

        private List<String> mTitleList;

        private Context mContext;

        public static final int[] TITLE_RES_ARRAYs = {R.string.nearby, R.string.message,
                R.string.discover};

        public MainFragmentsAdapter(FragmentManager fm, List<Fragment> list, Context context) {
            super(fm);
            this.list = list;
            mContext = context;
        }

        public MainFragmentsAdapter(FragmentManager fm, List<Fragment> list, List<String> titleList,
                                    Context context) {
            this(fm, list, context);
            mTitleList = titleList;
            isDefault = false;
        }

        @Override
        public Fragment getItem(int p) {
            return list.get(p);
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return isDefault ? mContext.getString(TITLE_RES_ARRAYs[position])
                    : mTitleList.get(position);
        }

        public void setData(boolean isDefault,List<String> titles) {
            this.isDefault = isDefault;
            this.mTitleList=titles;
        }
    }

    @Override
    public void onRequestStart(String reqId) {

    }

    @Override
    public void onRequestSuccess(String reqId, Object result) {
        //解析app模块配置返回结果
        if (reqId.equals(KREQ_ID_FIND_APPCONFIG_MODULE_MAIN_PAGE)) {
            ArrayList<AdminAppConfigModelDTO> appConfigList = (ArrayList<AdminAppConfigModelDTO>) result;
            if (appConfigList != null && appConfigList.size() > 0) {
                for (AdminAppConfigModelDTO dto : appConfigList) {
                    if (dto.getCustom_item_code() != null && dto.getCustom_item_code().equals(Constants.APPCONFIG_CUSTOM_ITEM_CODE_MAIN)) {
                        findAppConfigModelByID(dto.getId());
                        break;
                    }
                }
            }
        }

        if (reqId.equals(KREQ_ID_FIND_APPCONFIG_MODULE_BY_ID)) {
            List<AdminAppConfigModelDTO> appConfigList = (List<AdminAppConfigModelDTO>) result;
            if (appConfigList != null && appConfigList.size() > 0) {
                findMainPageModel(appConfigList);
            }
        }
    }

    @Override
    public void onRequestFinished(String reqId) {

    }

    @Override
    public void onRequestError(String reqID, Exception error) {

    }

    public void reloadMenuDialog(boolean needRefreshAppConfig) {
        menuDialog.loadAllData(needRefreshAppConfig);
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

    /**
     * 解析主页面模块的配置信息
     */
    private void findMainPageModel(List<AdminAppConfigModelDTO> list) {
        List<AdminAppConfigModelDTO> pageModels = new ArrayList<>();
        for (AdminAppConfigModelDTO dto : list) {
            pageModels.add(dto);
        }

        if (!pageModels.isEmpty()) {
            initFragmentsAndTitles(pageModels);
        }
    }

    private void initFragmentsAndTitles(List<AdminAppConfigModelDTO> pageModels) {
        fragments.clear();
        titles.clear();
        for (AdminAppConfigModelDTO dto : pageModels) {
            if (dto.getModelUrl() != null) {
                if (dto.getModelUrl().contains(NearbyMapFragment_.class.getSimpleName())) {
                    if (nearByFragment == null) {
                        nearByFragment = new NearbyMapFragment_();
                    }
                    SharedPreferencesUtil
                            .putValue(getActivity(), NearbyMapFragment.class.getSimpleName(),
                                    dto.getId());
                    fragments.add(nearByFragment);
                    titles.add(dto.getModel_name());
                } else if (dto.getModelUrl().contains(MessagesFragment_.class.getSimpleName())) {
                    if (messageFragment == null) {
                        messageFragment = new MessagesFragment_();
                    }
                    SharedPreferencesUtil
                            .putValue(getActivity(), MessagesFragment.class.getSimpleName(),
                                    dto.getId());
                    fragments.add(messageFragment);
                    titles.add(dto.getModel_name());
                } else if (dto.getModelUrl().contains(FragmentNewsModel_.class.getSimpleName())) {
                    if (explorerFragment == null) {
                        explorerFragment = new FragmentNewsModel_();
                    }
                    SharedPreferencesUtil
                            .putValue(getActivity(), FragmentNewsModel.class.getSimpleName(),
                                    dto.getId());
                    fragments.add(explorerFragment);
                    titles.add(dto.getModel_name());
                } else {
                    fragments.add(SubMainPageFragment.newInstance());
                    titles.add(dto.getModel_name());
                }
            }
        }

        if (fragments.size() > 0) {
            if (adapter==null) {
                adapter = new MainFragmentsAdapter(getChildFragmentManager(), fragments, titles,
                        getActivity());
            }
            adapter.setData(false,titles);
            adapter.notifyDataSetChanged();
            mViewPager.setAdapter(adapter);
            //这里编写重新加载ViewPager的代码
            mIndicatorLayout.setViewPager(mViewPager);
        }
    }

    /**
     * 环信连接监听listener
     */
    public class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            new Thread() {
                @Override
                public void run() {
                    HXSDKHelper.getInstance().notifyForRecevingEvents();
                }
            }.start();
        }

        @Override
        public void onDisconnected(final int error) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    }
                }
            });
        }
    }

    private AlertDialog.Builder conflictBuilder;

    /**
     * 账号在其他设备登录，会自动弹出
     */
    private void showConflictDialog() {
//        DemoHXSDKHelper.getInstance().logout(false, null);
        LoginHelper.doExit(mainActivity);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!mainActivity.isFinishing()) {
            try {
                if (conflictBuilder == null) {
                    conflictBuilder = new AlertDialog.Builder(mainActivity);
                }
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                conflictBuilder = null;
                                mainActivity.finish();
                                startActivity(new Intent(mainActivity, LoginActivity_.class));
                            }
                        });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
            } catch (Exception e) {
//                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    /**
     * 环信消息监听
     */
    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        if (emNotifierEvent.getEvent() == EMNotifierEvent.Event.EventNewMessage) {
            messageFragment.findMsgs();
            EMMessage message = (EMMessage) emNotifierEvent.getData();
            // 提示新消息
            HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
        }
    }
}