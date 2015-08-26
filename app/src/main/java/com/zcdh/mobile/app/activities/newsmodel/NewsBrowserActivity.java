package com.zcdh.mobile.app.activities.newsmodel;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobAppService;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.share.Share;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.ToastUtil;
import com.zcdh.mobile.utils.UnitTransfer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 载入网页
 *
 * @author yangjiannan
 */
@EActivity(R.layout.activity_news_browser)
public class NewsBrowserActivity extends BaseActivity implements
        OnClickListener, RequestListener {

    protected static final String TAG = NewsBrowserActivity.class
            .getSimpleName();

    // isSignUp : 1 分享 isShare
    private static final String PROPERTIES_IS_SIGNUP = "isSignUp";

    private static final String PROPERTIES_IS_SHARE = "isShare";

    private IRpcJobAppService appService;

    private String kREQ_ID_signUp;

    private String kREQ_ID_isSignUp;

    private String kREQ_ID_findUrlShare;

    @ViewById(R.id.emptyTipView)
    EmptyTipView emptyTipView;

    @ViewById(R.id.browser)
    WebView browser;

    @Extra
    String url;

    @Extra
    String title = "";

    @Extra
    long InformationTitleInfoId;

    @Extra
    String signType;

    @Extra
    String fromWhere;

    /**
     * 是否显示分享， 报名等等操作 报名 isSignUp : 1 分享 isShare : 1 评论 isComment: 21(表示有21条评论)
     */
    @Extra
    Map<String, Integer> properties;

    /*
 * 底部操作栏
 */
    @ViewById(R.id.bottomBarLl)
    LinearLayout bottomBarLl;

    private ProcessDialog processDialog;

    /**
     * 底部按钮 key 为icon value 为标题
     */
    private SparseArray<String> barButtonsMeta = new SparseArray<>();

    // 0 成功，-1 失败 49 用户信息不完整，47 已经报名
    private int userInfoValidation = -1;

    /**
     * 0 不显示标题， 1 显示标题
     */
    private int isShowTitle = 1;

    public void onPause() {
        callHiddenWebViewMethod("onPause");
        super.onPause();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appService = RemoteServiceManager
                .getRemoteService(IRpcJobAppService.class);
        if (getIntent().getExtras() != null
                && getIntent().getExtras().size() > 0) {
            String isShowTitle = getIntent().getExtras().getString(
                    "isShowTitle");
            if (!TextUtils.isEmpty(isShowTitle)) {
                try {
                    this.isShowTitle = Integer.valueOf(isShowTitle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (this.isShowTitle == 0) {
//                requestWindowFeature(Window.FEATURE_NO_TITLE);
                SystemServicesUtils.hideActionbar(this);
            }

            fromWhere = getIntent().getStringExtra(Constants.FROM_WHERE);
            IRpcJobAppService actionLogService = RemoteServiceManager
                    .getRemoteService(IRpcJobAppService.class);
            HashMap<String, String> logMap = new HashMap<String, String>();
            logMap.put("DECEIVE_TYPE", "Android");
            if (Constants.FROM_MAP_AD.equals(fromWhere)) { // 从地图广告进入
                logMap.put("VISIT_TYPE", "mapAdVisit");
                logMap.put(Constants.COVER_ID,
                        getIntent().getStringExtra(Constants.COVER_ID));
                actionLogService.addCoverVisitLog(logMap).identify(
                        "logIdentify", null);
            } else if (Constants.FROM_AD.equals(fromWhere)) { // 从发现广告进入
                logMap.put("VISIT_TYPE", "discoverAdVisit");
                logMap.put(Constants.COVER_ID,
                        String.valueOf(InformationTitleInfoId));
                actionLogService.addCoverVisitLog(logMap).identify(
                        "logIdentify", null);
            }
        }
    }

    @AfterViews
    void bindViews() {
        if (!TextUtils.isEmpty(title) && this.isShowTitle != 0) {
            SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), title);
        }

        initWebView();

        processDialog = new ProcessDialog(this);

        // 如果是有操作属性，先取得用户相关信息（如：是否资料完全，是否已经报名等等）
        if (properties != null && properties.size() > 0) {
            isSignUp();
        }
        loadUrl(browser, url);
    }

    private void initWebView() {

        WebSettings webSettings = browser.getSettings();
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);// 可用JS
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCachePath(getExternalCacheDir().toString()
                + File.separator + "test.db");
        webSettings.setAppCacheEnabled(true);
        browser.addJavascriptInterface(new JavaScriptInterface(this),
                "MobileApp");
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        browser.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(final WebView view,
                    final String url) {
                Log.e(TAG, "result : " + url);
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                            .parse(url));
                    startActivity(intent);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    loadUrl(view, url);// 载入网页
                }

                return true;
            }// 重写点击动作,用webview载入

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished result : " + url);
                emptyTipView.isEmpty(false);
                browser.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
                if (TextUtils.isEmpty(title)
                        && !TextUtils.isEmpty(view.getTitle())) {
                    SystemServicesUtils.setActionBarCustomTitle(
                            NewsBrowserActivity.this, getSupportActionBar(),
                            view.getTitle());
                }
            }

        });
        browser.setWebChromeClient(new WebChromeClient());
    }

    void loadUrl(WebView view, String url) {
        view.loadUrl(url);
    }

    private void callHiddenWebViewMethod(String name) {
        if (browser != null) {
            browser.loadUrl("about:blank");
            // try {
            // Method method = WebView.class.getMethod(name);
            // method.invoke(browser);
            // } catch (NoSuchMethodException e) {
            // Log.i("No such method: " + name, e.toString());
            // } catch (IllegalAccessException e) {
            // Log.i("Illegal Access: " + name, e.toString());
            // } catch (InvocationTargetException e) {
            // Log.d("Invocation Target Exception: " + name, e.toString());
            // }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.isShowTitle == 0) {
            // if (!isShowTitle) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定退出面试系统？");
            // 添加选择按钮并注册监听
            isExit.setButton2("取消", listener);
            isExit.setButton("退出", listener);
            // 显示对话框
            isExit.show();
            // }else {
            // return true;
            // }
        }
        return false;
    }

    /** 监听对话框里面的button点击事件 */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * 如果具有报名功能，先查询该用户是否报名
     */
    @Background
    void isSignUp() {
        appService.isSignUp(InformationTitleInfoId, getUserId(), signType)
                .identify(
                        kREQ_ID_isSignUp = RequestChannel.getChannelUniqueID(),
                        this);

    }

    /**
     * 查找分享内容
     */
    @Background
    void findShareContent() {
        appService.findUrlShare(InformationTitleInfoId, getUserId()).identify(
                kREQ_ID_findUrlShare = RequestChannel.getChannelUniqueID(),
                this);
    }

    private void createBar() {
        if (properties != null && properties.size() > 0) {

            if (properties.get(PROPERTIES_IS_SIGNUP) != null) {
                barButtonsMeta.put(R.drawable.yynormal, "报名");
            }

            if (properties.get(PROPERTIES_IS_SHARE) != null) {
                barButtonsMeta.put(R.drawable.yyshare, "分享");
            }

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int screenWidth = metrics.widthPixels / barButtonsMeta.size();
            Log.i(TAG, screenWidth + "");
            bottomBarLl.setWeightSum(barButtonsMeta.size());
            int index = 1;
            int key = 0;
            for (int i = 0; i < barButtonsMeta.size(); i++) {
                key = barButtonsMeta.keyAt(i);
                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                        screenWidth, UnitTransfer.dip2px(this, 48), 1);

                LinearLayout btn = (LinearLayout) LayoutInflater.from(this)
                        .inflate(R.layout.barbtn, null);
                TextView title = (TextView) btn.findViewById(R.id.title);
                ImageView icon = (ImageView) btn.findViewById(R.id.icon);

                title.setText(barButtonsMeta.get(key));
                icon.setImageResource(key);
                btn.setClickable(true);
                btn.setOnClickListener(this);
                btn.setId(key);
                if (key == R.drawable.yynormal) {
                    // 如果已经报名，不可点击
                    if (userInfoValidation == 47) {
                        title.setText("已报名");
                        btn.setClickable(false);
                        title.setTextColor(getResources().getColor(
                                R.color.grey1));
                    }

                }
                bottomBarLl.addView(btn, layoutParam);
                if (index % 2 != 0) {
                    View v = new View(this);
                    v.setBackgroundColor(getResources().getColor(R.color.grey1));
                    LinearLayout.LayoutParams layoutParam_ = new LinearLayout.LayoutParams(
                            UnitTransfer.dip2px(this, 0.5f),
                            UnitTransfer.dip2px(this, 40));
                    layoutParam_.gravity = Gravity.CENTER_VERTICAL;
                    bottomBarLl.addView(v, layoutParam_);
                }
                index++;
            }
            bottomBarLl.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 报名
     */
    @Background
    void signUp() {
        appService.signUp(InformationTitleInfoId, getUserId(), signType)
                .identify(kREQ_ID_signUp = RequestChannel.getChannelUniqueID(),
                        this);
    }

    @Override
    public void onClick(View v) {
        // 分享
        if (v.getId() == R.drawable.yyshare) {
            processDialog.show();
            findShareContent();
        }
        // 报名
        if (v.getId() == R.drawable.yynormal) {
            if (getUserId() > 0) {
                processDialog.show();
                signUp();
            } else {
                Toast.makeText(this,
                        getResources().getString(R.string.login_first),
                        Toast.LENGTH_SHORT).show();
                LoginActivity_.intent(this).start();
            }
        }
    }

    @Override
    public void onRequestStart(String reqId) {

    }

    @Override
    public void onRequestSuccess(String reqId, Object result) {
        if (reqId.equals(kREQ_ID_signUp)) {

            if (result != null && (Integer) result == 0) {
                ToastUtil.show(R.string.job_sign_up_success);
                userInfoValidation = 47;
                bottomBarLl.removeAllViews();
                createBar();
            }
        }

        // 查找用户信息（是否报名）
        if (reqId.equals(kREQ_ID_isSignUp)) {
            if (result != null) {
                userInfoValidation = (Integer) result;
                createBar();
            }
        }

        if (reqId.equals(kREQ_ID_findUrlShare)) {
            if (result != null) {
                // share(shareDtos);
                Share.showShare(this, (List<JobEntShareDTO>) result, false,
                        null);
            }
        }
    }

    @Override
    public void onRequestFinished(String reqId) {
        // TODO Auto-generated method stub
        processDialog.dismiss();
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        // TODO Auto-generated method stub
        if (reqID.equals(kREQ_ID_findUrlShare)) {
            Toast.makeText(this, "获取分享内容失败", Toast.LENGTH_SHORT).show();
        }
    }
}
