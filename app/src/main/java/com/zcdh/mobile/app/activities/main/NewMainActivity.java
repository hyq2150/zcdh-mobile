package com.zcdh.mobile.app.activities.main;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.nearby.AddMoreFuncActivity;
import com.zcdh.mobile.app.activities.nearby.NearbyMapFragment;
import com.zcdh.mobile.app.activities.newsmodel.AddMoreDiscoverActivity;
import com.zcdh.mobile.app.activities.search.AdvancedSearchActivity;
import com.zcdh.mobile.app.activities.search.AreaActivity;
import com.zcdh.mobile.app.activities.settings.PostMatchSettingActivity;
import com.zcdh.mobile.app.activities.settings.SettingsHomeActivity;
import com.zcdh.mobile.app.push.MyPushMessageReceiver;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.utils.NetworkUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.WindowFeature;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/**
 * 程序主框架页面
 *
 * @author jeason, 2014-7-25 下午1:42:56
 */
@WindowFeature(Window.FEATURE_NO_TITLE)
@EActivity(R.layout.layout_content)
public class NewMainActivity extends BaseActivity implements Subscriber {

    private static final String TAG = NewMainActivity.class.getSimpleName();

    private long exitTime = 0;

    // 接收登录成功广播
    private BroadcastReceiver loginSuccessReceiver;

    private BroadcastReceiver msgReceiver;

    // 注销成功
    private BroadcastReceiver exitSuccessReceiver;

    // 网络状态
    private NetworkStatusChangedReceiver networkStatusChangedReceiver;

    // 基本资料变更
    private BroadcastReceiver profileUpdatedReceiver;

    // 查询条件接收
    private BroadcastReceiver conditionForSearchReceiver;

    private BroadcastReceiver matchRateModeChangeReceiver;

    // 修改用户头像
    private BroadcastReceiver modifiedUserPhotoReceiver;

    // 本地广播
    private LocalBroadcastManager localBroadcastManager;

    private boolean flag_need_refresh;

    private MainPageFragment fragmentMain;

    @Override
    protected void onCreate(Bundle bundle) {

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        msgReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "PushMessage 刷新消息");

                fragmentMain.refreshMessage();
            }
        };

        registerReceiver(msgReceiver, new IntentFilter(
                MyPushMessageReceiver.NEW_MSG));

        // 登录成功
        loginSuccessReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (Constants.LOGIN_RESULT_ACTION.equals(intent.getAction())) {
                    // 标识需要重新加载和刷新侧边菜单
                    int resultCode = intent.getIntExtra(Constants.kRESULT_CODE,
                            -1);
                    if (resultCode == Constants.kLOGIN_RESULT_SUCCESS) {
                        flag_need_refresh = true;
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(
                Constants.LOGIN_RESULT_ACTION);
        localBroadcastManager.registerReceiver(loginSuccessReceiver,
                intentFilter);

        // 注销成功
        exitSuccessReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                flag_need_refresh = true;
            }
        };
        IntentFilter existIntentFilter = new IntentFilter(Constants.ACTION_EXIT);
        this.registerReceiver(exitSuccessReceiver, existIntentFilter);

        // 网络状态改变
        networkStatusChangedReceiver = new NetworkStatusChangedReceiver();
        IntentFilter networkStatusIntentFilter = new IntentFilter();
        networkStatusIntentFilter
                .addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkStatusIntentFilter
                .addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.registerReceiver(networkStatusChangedReceiver,
                networkStatusIntentFilter);

        // 基本资料修改变更
        profileUpdatedReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                flag_need_refresh = true;
            }
        };
        IntentFilter profielUpdatedIntentFilter = new IntentFilter(
                Constants.ACTION_UPDATE_PROFILE);
        this.registerReceiver(profileUpdatedReceiver, profielUpdatedIntentFilter);

        // 高级查询，条件筛选
        conditionForSearchReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                int requestCode = intent.getIntExtra(Constants.kREQUEST_CODE,
                        -1);
                int resultCode = intent.getIntExtra(Constants.kRESULT_CODE, -1);

                // 高级查询
                if (requestCode == AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH) {
                    fragmentMain.onResultDispatch(
                            AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH,
                            resultCode, intent);
                }
            }
        };
        IntentFilter conditionIntentFilter = new IntentFilter(
                Constants.kACTION_CONDITION);
        this.registerReceiver(conditionForSearchReceiver, conditionIntentFilter);

        // 开启岗位匹配模式
        matchRateModeChangeReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                fragmentMain.reloadPostForNearby();
            }
        };
        this.registerReceiver(matchRateModeChangeReceiver, new IntentFilter(
                PostMatchSettingActivity.kACTION_MATCH_MODE));

        // 修改用户头像
        modifiedUserPhotoReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                fragmentMain.reloadMenuDialog();
                updateUserIcon();
            }
        };
        this.registerReceiver(modifiedUserPhotoReceiver, new IntentFilter(
                Constants.kACTION_MODIFIED_PHOTO));
        super.onCreate(bundle);

        MyEvents.register(this);

    }

    /**
     * 高级搜索页面的回调
     */
    @OnActivityResult(AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH)
    void onResultCondition(int resultCode, Intent data) {

        fragmentMain.onResultDispatch(
                AdvancedSearchActivity.kREQUEST_ADVANCE_SEARCH, resultCode,
                data);
    }

    /**
     * 地区选择结果页面的回调
     */
    @OnActivityResult(AreaActivity.kREQUEST_AREA)
    void onResultArea(int resultCode, Intent data) {

        fragmentMain.onResultDispatch(AreaActivity.kREQUEST_AREA, resultCode,
                data);
    }

    /**
     * 添加更多功能回调
     */
    @OnActivityResult(AddMoreFuncActivity.kREQUEST_ADD_FUNCS)
    void onResultAddFuncs(int resultCode, Intent data) {
        fragmentMain.onResultDispatch(AddMoreFuncActivity.kREQUEST_ADD_FUNCS,
                resultCode, data);
    }

    /**
     * 添加发现模块
     */
    @OnActivityResult(AddMoreDiscoverActivity.kREQUEST_ADD_DISCOVERY)
    void onResultAddDiscovery(int resultCode, Intent data) {

        fragmentMain.onResultDispatch(
                AddMoreDiscoverActivity.kREQUEST_ADD_DISCOVERY, resultCode,
                data);

    }

    @AfterViews
    public void bindViews() {
        if (fragmentMain==null) {
            fragmentMain = new MainPageFragment_();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(android.R.id.content, fragmentMain);
            fragmentTransaction.commit();
        }
    }

    /**
     * 捕捉返回键，如果当前显示菜单，刚隐藏
     */
    @Override
    public void onBackPressed() {
        if (NearbyMapFragment.isShowSearchBar) {
            fragmentMain.getNearbyFragment().resetLayoutOfMap();
        } else if (Constants.isShowingPointDetails) {
            fragmentMain.getNearbyFragment().onHiddenPreview();
        } else if (NearbyMapFragment.isShowPostList) {
            fragmentMain.getNearbyFragment().switchMode();
        } else {

            if ((System.currentTimeMillis() - exitTime) > 3000) {
                Toast.makeText(getApplicationContext(), "再按一次退出职场导航",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ZcdhApplication.getInstance().exit();
                finish();
                super.onBackPressed();
                // System.exit(0);
            }
        }
    }

    /**
     * 如果有网络状态改变
     *
     * @author yangjiannan
     */
    public class NetworkStatusChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                fragmentMain.refreshData();
            }
        }
    }

    @Override
    protected void onDestroy() {

        localBroadcastManager.unregisterReceiver(loginSuccessReceiver);

        unregisterReceiver(networkStatusChangedReceiver);
        unregisterReceiver(exitSuccessReceiver);
        unregisterReceiver(profileUpdatedReceiver);
        unregisterReceiver(conditionForSearchReceiver);
        unregisterReceiver(matchRateModeChangeReceiver);
        unregisterReceiver(modifiedUserPhotoReceiver);
        unregisterReceiver(msgReceiver);
        MyEvents.unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // 如果登录成功 刷新左侧栏的界面 并发出一条广播提醒消息Fragment进行更新列表操作
        if (requestCode == Constants.REQUEST_CODE_LOGIN
                && resultCode == RESULT_OK) {
            fragmentMain.reloadMenuDialog();
            sendBroadcast(new Intent(MyPushMessageReceiver.NEW_MSG));
        }

        if (requestCode == SettingsHomeActivity.REQUEST_CODE_SETTING
                && resultCode == Activity.RESULT_OK) {
            fragmentMain.reloadMenuDialog();
            sendBroadcast(new Intent(MyPushMessageReceiver.NEW_MSG));
        }

    }

    @Override
    public void onResume() {
        if (flag_need_refresh) {
            // 重新加载侧边栏菜单
            fragmentMain.reloadMenuDialog();
            // 获取头像
            fragmentMain.loadPortal();
            // 刷新所有数据
            fragmentMain.refreshData();

            flag_need_refresh = false;
        }
        super.onResume();
    }

   /* @Override
    public void onOpen() {
        MyEvents.post(Constants.KEVENT_SILDMENU_STATUS,
                Constants.KSTATUS_SILDMENU_OPEN);
    }

    @Override
    public void onClosed() {
        MyEvents.post(Constants.KEVENT_SILDMENU_STATUS,
                Constants.KSTATUS_SILDMENU_CLOSED);
    }*/

    /***
     * 更新主界面右上角用户头像
     */
    public void updateUserIcon() {

        fragmentMain.loadPortal();

    }

    @Override
    public void receive(String key, Object msg) {
        if (Constants.kEVENT_NOTIFICATION_MESSAGE.equals(key)) {
            Log.i(TAG, "MyPushMessage 刷新消息");
        }
    }

}
