package com.zcdh.mobile.app.activities.settings;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.utils.NetworkUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 系统设置主页
 *
 * @author yangjiannan
 */
@EActivity(R.layout.activity_settings_home)
public class SettingsHomeActivity extends BaseActivity implements Subscriber,
        RequestListener {

    public final static String kMSG_AUTH_STATU = "auth_status";

    final static String simple_adapter_key = "title";

    public static final int REQUEST_CODE_SETTING = 0x102;

    String kREQ_ID_isOpenUserMatchSet;

    private IRpcJobUservice jobUservice;

    ArrayList<String> settings = new ArrayList<String>();

    @ViewById(R.id.sv_settings)
    ScrollView scrollView;

    @ViewById(R.id.settingsHomeListview)
    ListViewInScrollView settingsHomeListview;

    @ViewById(R.id.exitBtn)
    Button exitBtn;

    private boolean isMatchOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyEvents.register(this);
        jobUservice = RemoteServiceManager
                .getRemoteService(IRpcJobUservice.class);
    }

    @AfterViews
    void bindViews() {
        // 设置标题
        SystemServicesUtils.setActionBarCustomTitle(this,
                getSupportActionBar(), getString(R.string.title_settings_home));

        // ScrollView滚动至最顶端，以显示搜索历史的ListView
        scrollView.smoothScrollBy(0, 0);
        if (NetworkUtils.isNetworkAvailable(getApplication())) {
            isMatchPost();
        } else {
            inflat();
        }
    }

    void inflat() {
        // 初始化设置列表
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        // 修改密码
        HashMap<String, Object> item = new HashMap<String, Object>();
        // 推送设置
        item = new HashMap<String, Object>();
        item.put(simple_adapter_key, getString(R.string.activity_title_push));
        list.add(item);

        // 开启匹配度
        if (isMatchOpened) {
            item = new HashMap<String, Object>();
            item.put(simple_adapter_key,
                    getString(R.string.activity_title_match));
            list.add(item);
        }

        // 软件评分
        item = new HashMap<String, Object>();
        item.put(simple_adapter_key, getString(R.string.gradeForApp));
        list.add(item);

        // 关于我们
        item = new HashMap<String, Object>();
        item.put(simple_adapter_key, getString(R.string.about));
        list.add(item);

        // 检查更新
        item = new HashMap<String, Object>();
        item.put(simple_adapter_key, getString(R.string.checkForUpgrade));
        list.add(item);

        // 系统引导
        item = new HashMap<String, Object>();
        item.put(simple_adapter_key, getString(R.string.gettingStart));
        list.add(item);

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
                list, R.layout.simple_listview_item_accessory,
                new String[]{simple_adapter_key},
                new int[]{R.id.itemNameText});

        settingsHomeListview.setAdapter(adapter);

        if (ZcdhApplication.getInstance().getZcdh_uid() > 0) {
            exitBtn.setVisibility(View.VISIBLE);
        } else {
            exitBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示职位匹配设置 37 打开匹配模式， 38 关闭匹配模式
     */
    @Background
    void isMatchPost() {
        jobUservice
                .isOpenUserMatchSet(getUserId())
                .identify(
                        kREQ_ID_isOpenUserMatchSet = RequestChannel
                                .getChannelUniqueID(),
                        this);
    }

    @ItemClick(R.id.settingsHomeListview)
    void onItemClick(int p) {
        if (isMatchOpened) {
            switch (p) {
                case 0: // 推送设置
                    PushSettingActivity_.intent(this).start();
                    break;
                case 1:// 开启职位匹配模式
                    PostMatchSettingActivity_.intent(this).start();
                    break;
                case 2: // 软件评分
                    //	Toast.makeText(this, "未完待续...", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case 3: // 关于我们
                    AboutActivity_.intent(this).start();
                    break;
                case 4: // 检查更新
                    startActivity(new Intent(this, CheckForUpgradeActivity_.class));
                    break;
                case 5: // 系统引导
                    startActivity(new Intent(this, UserGuideActivity_.class));
                    break;
            }

        } else {
            switch (p) {
                case 0: // 推送设置
                    PushSettingActivity_.intent(this).start();
                    break;
                case 1: // 软件评分
                    //Toast.makeText(this, "未完待续...", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case 2: // 关于我们
                    AboutActivity_.intent(this).start();
                    break;
                case 3: // 检查更新
                    startActivity(new Intent(this, CheckForUpgradeActivity_.class));
                    break;
                case 4: // 系统引导
                    startActivity(new Intent(this, UserGuideActivity_.class));
                    break;
            }
        }

    }

    @Click(R.id.exitBtn)
    void onExitBtn() {
        LoginHelper.doExit(this);
        setResult(RESULT_OK);
        LoginActivity_.intent(this).is_exited(true).start();
        Intent exitActionFilter = new Intent(Constants.ACTION_EXIT);
        sendBroadcast(exitActionFilter);
        finish();
    }

    @Override
    public void receive(String key, Object msg) {
    }

    @Override
    public void onRequestStart(String reqId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestSuccess(String reqId, Object result) {
        if (reqId.equals(kREQ_ID_isOpenUserMatchSet)) {
            if (result != null) {
                // 37 打开匹配模式， 38 关闭匹配模式
                int resultCode = (Integer) result;
                if (resultCode == 37) {
                    isMatchOpened = true;
                }
            }
            inflat();
        }
    }

    @Override
    public void onRequestFinished(String reqId) {

    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        // TODO Auto-generated method stub

    }

}
