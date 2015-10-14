package com.zcdh.mobile.app.activities.main;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.huanxin.Constant;
import com.huanxin.DemoHXSDKHelper;
import com.huanxin.adapter.EMCallBackAdapter;
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
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.NetworkUtils;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * 程序主框架页面
 *
 * @author jeason, 2014-7-25 下午1:42:56
 */
@EActivity(R.layout.layout_content)
public class NewMainActivity extends BaseActivity {

    private static final String TAG = NewMainActivity.class.getSimpleName();

    private long exitTime = 0;

    // 网络状态
    private NetworkStatusChangedReceiver networkStatusChangedReceiver;

    //匹配模式改变
    private BroadcastReceiver matchRateModeChangeReceiver;

    private MainPageFragment fragmentMain;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // 网络状态改变
        networkStatusChangedReceiver = new NetworkStatusChangedReceiver();
        IntentFilter networkStatusIntentFilter = new IntentFilter();
        networkStatusIntentFilter
                .addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkStatusIntentFilter
                .addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.registerReceiver(networkStatusChangedReceiver,
                networkStatusIntentFilter);
        // 开启岗位匹配模式
        matchRateModeChangeReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                fragmentMain.reloadPostForNearby();
            }
        };
        this.registerReceiver(matchRateModeChangeReceiver, new IntentFilter(
                PostMatchSettingActivity.kACTION_MATCH_MODE));

        loginHx();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        fragmentMain.reloadMenuDialog(true);
        fragmentMain.refreshData();
        fragmentMain.refreshAppModule();
        loginHx();
    }

    /**
     * 登陆成功回调
     */
    @OnActivityResult(Constants.REQUEST_CODE_LOGIN)
    void onResultLoginSuccess(int resultCode,Intent data) {
        if (resultCode==RESULT_OK) {
            fragmentMain.reloadMenuDialog(true);
            fragmentMain.refreshData();
            fragmentMain.refreshAppModule();

            loginHx();
        }
    }
    /**
     * 退出登录回调
     * @param resultCode 请求码
     * @param data 返回数据Intent
     */
    @OnActivityResult(SettingsHomeActivity.REQUEST_CODE_SETTING)
    void onResultSetting(int resultCode,Intent data) {
        if (resultCode==RESULT_OK) {
            fragmentMain.reloadMenuDialog(true);
            fragmentMain.refreshData();
            fragmentMain.refreshAppModule();
        }
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
     * 账户管理页面的回调
     */
    @OnActivityResult(Constants.REQUEST_CODE_ACCOUNT_MANAGER)
    void onResultAccountManager(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            fragmentMain.reloadMenuDialog(false);
        }
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

    /**
     * 客服消息页面回调
     * @param resultCode
     * @param data
     */
    @OnActivityResult(Constants.REQUEST_CODE_MESSAGE)
    void onResultHxMsg(int resultCode, Intent data) {
        fragmentMain.onResultDispatch(
                Constants.REQUEST_CODE_MESSAGE, resultCode,
                data);
    }

    @AfterViews
    public void bindViews() {
        if (fragmentMain == null) {
            fragmentMain = new MainPageFragment_();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction.replace(android.R.id.content, fragmentMain);
            fragmentTransaction.commit();
        }
    }

    /**
     * 捕捉返回键，如果当前显示菜单，先隐藏
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
                ToastUtil.show(R.string.press_again_to_exit);
                exitTime = System.currentTimeMillis();
            } else {
                ZcdhApplication.getInstance().exit();
                finish();
                super.onBackPressed();
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
        unregisterReceiver(networkStatusChangedReceiver);
        unregisterReceiver(matchRateModeChangeReceiver);
        super.onDestroy();
    }

    /**
     * 环信即时通讯登录
     */
    public void loginHx() {
        long zcdhId=getUserId();
        final String username=ZcdhApplication.getInstance().getUserName();
        final String password=ZcdhApplication.getInstance().getPassword();
        if (zcdhId>0 && username!=null && password!=null) {
            ((DemoHXSDKHelper)DemoHXSDKHelper.getInstance()).login(username, password,
                    new EMCallBackAdapter() {
                        @Override
                        public void onSuccess() {
                            // 登陆成功，保存用户名密码
                            ZcdhApplication.getInstance().setUserName(username);
                            ZcdhApplication.getInstance().setPassword(password);
                            try {
                                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                                // ** manually load all local groups and
                                EMGroupManager.getInstance().loadAllGroups();
                                EMChatManager.getInstance().loadAllConversations();

                                Log.e(TAG, "环信已经登录！！！！！！！！ ");

                                sendMessageToCustomer();
                            } catch (Exception e) {
                                e.printStackTrace();
                                // 取好友或者群聊失败，不让进入主页面
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        ToastUtil.showLong(R.string.huanxin_login_failed);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(final int code, final String message) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    ToastUtil.showLong(R.string.huanxin_login_failed);
                                }
                            });
                        }
                    });
        }
    }

    /**
     * 登录完成后主动给客服发送一条消息，便于客服人员主动联系客户
     */
    private void sendMessageToCustomer() {
        String adminName;
        if (K.AppVersion.appVersion.equals(K.AppVersion.Versions.release)) {
            adminName=Constant.ADMIN_USERNAME_RELEASE;
        } else {
            adminName=Constant.ADMIN_USERNAME_DEBUG;
        }
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance()
                .getConversation(adminName);
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
        message.setChatType(EMMessage.ChatType.Chat);
        //设置消息body
        TextMessageBody txtBody = new TextMessageBody(Constant.DEFAULT_MESSAGE);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(adminName);
        //把消息加入到此会话对象中
        conversation.addMessage(message);
        //发送消息
        EMChatManager.getInstance().sendMessage(message, null);
    }
}
