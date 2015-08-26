package com.zcdh.mobile.app.activities.start_at;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobAppService;
import com.zcdh.mobile.api.IRpcJobStartService;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.ImgDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
import com.zcdh.mobile.app.activities.auth.LoginListener;
import com.zcdh.mobile.app.activities.main.NewMainActivity_;
import com.zcdh.mobile.app.maps.bmap.MyBLocationCient;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestException;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.upgrade.UpdateAppService;
import com.zcdh.mobile.framework.upgrade.UpdateAppService.DownloadProgressListner;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.NetworkUtils;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.SimpleAnimationListener;

/**
 * @author yangjiannan App 预加载页面
 */
@EActivity(R.layout.framework_loading)
public class LoadingActivity extends Activity implements
        DownloadProgressListner, LoginListener, BDLocationListener,
        RequestListener {

    /**
     * 记录上一次请求位置的时间
     */
    private final static String kREQUEST_LOCATION_TIME = "kREQUEST_LOCATION_TIME";

    /**
     * 缓存上一次请求地理位置后获得的广告图片地址
     */
    private final static String kCACHED_ADS_IMG_URL = "kCACHED_ADS_IMG_URL";

    private String kREQ_ID_findFlashScreenByImg;

    private String kREQ_ID_getMobileAndEmailRegex;

    private String kREQ_ID_isUseInvitationcode;

    private UpdateAppService updateAppService;

    private IRpcJobStartService jobStartService;

    private MyBLocationCient locationCient;

    private static final String TAG = LoadingActivity.class.getSimpleName();

    private static final long ads_delayed = 1500;

    private static final long intervals = 1000;

    private IRpcJobAppService service;

    private IRpcJobUservice jobUservice;

    /**
     * 记录登录验证完成
     */
    private boolean COMPLETE_LOGIN;

    /**
     * 检查更新完成
     */
    private boolean COMPLETE_UPGRADE;

    /**
     * 从webview 中传入的参数
     */
    @Extra
    String dataString;

    /**
     * 欢迎页
     */
    @ViewById(R.id.welcomeImg)
    ImageView welcomeImg;

    /**
     * 广告图片
     */
    @ViewById(R.id.adsImg)
    ImageView adsImg;

    private boolean destoryed;

    // 网络状态
    private NetworkStatusChangedReceiver networkStatusChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 网络状态改变
        networkStatusChangedReceiver = new NetworkStatusChangedReceiver();
        IntentFilter networkStatusIntentFilter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkStatusChangedReceiver,
                networkStatusIntentFilter);
        if (NetworkUtils.isNetworkAvailable(this)) {
            getServices();
        } else {
            ToastUtil.show(R.string.network_suck);
        }
    }

    /**
     * 如果有网络状态改变
     */
    public class NetworkStatusChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                getServices();
            }
        }
    }

    void getServices() {
        updateAppService = new UpdateAppService(LoadingActivity.this,
                LoadingActivity.this);
        jobStartService = RemoteServiceManager
                .getRemoteService(IRpcJobStartService.class);
        service = RemoteServiceManager
                .getRemoteService(IRpcJobAppService.class);
        jobUservice = RemoteServiceManager
                .getRemoteService(IRpcJobUservice.class);
        getMobileAndEmailRegex();
        start();
    }

    public void onDestroy() {
        AnimateFirstDisplayListener.displayedImages.clear();
        if (locationCient != null) {
            locationCient.stop();
        }
        if (kREQ_ID_findFlashScreenByImg != null) {
            RequestChannel.removeRequestListener(kREQ_ID_findFlashScreenByImg);
        }

        handler.removeCallbacksAndMessages(null);
        unregisterReceiver(networkStatusChangedReceiver);
        destoryed = true;
        super.onDestroy();
    }

    private void start() {
		/*
		 * 判断上一次 请求间隔 1） 先开始定位， 定位后的下一步是加载广告
		 */
        isUseInvitationcode();
        String pre_reqquest_time = SharedPreferencesUtil.getValue(this,
                kREQUEST_LOCATION_TIME, null);
        if (pre_reqquest_time != null) {
            try {
                // Date preTime = new Date(preTimeValue);
                Date preDate = DateUtils
                        .getDateByStrToYMDHMS(pre_reqquest_time);
                long interval = new Date().getTime() - preDate.getTime();
                if (interval < intervals) { // 跟上次请求相比较，还在时间间隔内，从缓存中取得图片地址显示
                    String cachedAdsUrl = SharedPreferencesUtil.getValue(this,
                            kCACHED_ADS_IMG_URL, null);
                    if (cachedAdsUrl != null) {
                        loadAdsImg(cachedAdsUrl);
                    } else {
                        doLoadAds();
                    }
                } else { // 超过时间间隔重新请求定位
                    doLocation();
                }
            } catch (Exception e) {
                e.printStackTrace();
                doLocation();
            }
        } else {
            doLocation();
        }
    }

    @Background
    void getMobileAndEmailRegex() {
        jobUservice
                .getMobileAndEmailRegex()
                .identify(
                        kREQ_ID_getMobileAndEmailRegex = RequestChannel
                                .getChannelUniqueID(),
                        this);
    }

    /**
     * 开始请求百度定位
     */
    private void doLocation() {
        Log.e(TAG, "####################doLocation###########################");
        locationCient = new MyBLocationCient(this, this);
        locationCient.requestLocation();
    }

    /**
     * 后台服务加载广告图片地址
     */
    @Background
    void doLoadAds() {
        isLocationSuccess = true;
        LatLng loc = ZcdhApplication.getInstance().getMyLocation();
        jobStartService
                .findFlashScreenByImg(loc.latitude, loc.longitude, 3)
                .identify(
                        kREQ_ID_findFlashScreenByImg = RequestChannel
                                .getChannelUniqueID(),
                        this);
    }

    /**
     * 邀请码功能开关
     */
    public void isUseInvitationcode() {
        BackgroundExecutor.execute(new Task("", 0, "") {

            @Override
            public void execute() {
                // TODO Auto-generated method stub
                service.isUseInvitationcode().identify(
                        kREQ_ID_isUseInvitationcode = RequestChannel
                                .getChannelUniqueID(), LoadingActivity.this);
            }
        });
    }

    /**
     * 加载图片
     */
    private void loadAdsImg(final String url) {

        Message msg =Message.obtain();
        msg.what = 1;
        if (!TextUtils.isEmpty(url)) {
            msg.obj = url;
        }
        // 显示广告图片
        handler.sendMessageDelayed(msg, ads_delayed);
        // 缓存广告图片地址
        SharedPreferencesUtil.putValue(LoadingActivity.this,
                kCACHED_ADS_IMG_URL, url);
        // 记录此次请求广告的时间
        SharedPreferencesUtil.putValue(
                LoadingActivity.this,
                kREQUEST_LOCATION_TIME,
                DateUtils
                        .getDateByFormatYMDHMS(new Date()) + "");

        // 3) 登录和检查版本更新
        handler.sendEmptyMessageDelayed(5, ads_delayed);

    }

    /**
     * 检查更新
     */
    void doLogin() {
        LoginHelper.getInstance(this, this).doLogin();
    }

    /**
     * 检查更新
     */
    @Background
    void doUpgrade() {
        if (!K.debug) {
            boolean should = updateAppService.checkIsUpdate();
            doUpgradeDone(should);
        } else {
            COMPLETE_UPGRADE = true;
            checkComplete();
        }
    }

    @UiThread
    void doUpgradeDone(boolean shouldUpgrade) {
        if (shouldUpgrade) {
            // 显示是否版本更新对话框
            updateAppService.showNoticeDialog();
        } else {
            COMPLETE_UPGRADE = true;
            checkComplete();
        }
    }

    /**
     * 预加载完成
     */
    private void checkComplete() {
        if (destoryed) {
            return;
        }
        if (COMPLETE_LOGIN && COMPLETE_UPGRADE) {
            if (TextUtils.isEmpty(dataString)) {
                toMainScreen();
                finish();
            } else {
                // 跳入指定的页面
                Bundle data = new Bundle();
                Class<?> clazz = null;
                for (Entry<String, String> param : StringUtils.getParams(
                        dataString).entrySet()) {
                    if ("url".equals(param.getKey())) {
                        clazz = SystemServicesUtils.getClass(param.getValue());
                    }
                    data.putString(param.getKey(), param.getValue());
                    Log.i(TAG, param.getKey());
                }

                Log.e(TAG, "class : " + clazz);
                if (clazz != null) {
                    Intent targetIntent = new Intent(this, clazz);
                    targetIntent.putExtras(data);
                    startActivity(targetIntent);
                    finish();
                }
            }
        }

    }

    /**
     * 进入app
     */
    private void toMainScreen() {
        // 是否经过 app 介绍展示
        boolean used_introduce = SharedPreferencesUtil.getValue(
                getApplicationContext(),
                GettingStartedActivity.kGETTING_STARTED, false);
        if (!used_introduce) {
            startActivity(new Intent(this, GettingStartedActivity.class));
        } else {
            NewMainActivity_.intent(LoadingActivity.this).start();
        }
        finish();
    }

    /* ================ 2 )登录================ */
    @Override
    public void requestLoginFinished(int resultCode, String errorMsg) {
        if (resultCode == Constants.kLOGIN_RESULT_FAILE) {
            LoginHelper.doExit(this);
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        }
        COMPLETE_LOGIN = true;
        checkComplete();
    }

    private boolean isLocationSuccess = false;

    /* ================ 3 )定位 ================ */
    @Override
    public void onReceiveLocation(final BDLocation loc) {
        LatLng point = new LatLng(loc.getLatitude(), loc.getLongitude());
        Log.e(TAG, "*************************************************");
        Log.e(TAG, "latitude : " + point.latitude);
        Log.e(TAG, "latitude : " + loc.getAddrStr());
        Log.e(TAG, "longitude : " + point.longitude);
        Log.e(TAG, "*************************************************");
        ZcdhApplication.getInstance().setMyLocation(point);
        locationCient.stop();
		/*
		 * 2） 加载广告，下一步完成登录、检查更新
		 */
        if (!isLocationSuccess) {
            isLocationSuccess = true;
            doLoadAds();
        }

    }

    @Override
    public void onRequestStart(String reqId) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRequestSuccess(String reqId, Object result) {
        // 是否打开了邀请码使用开关
        if (reqId.equals(kREQ_ID_isUseInvitationcode)) {
            switch ((int) result) {
                case 0:
                    ZcdhApplication.getInstance().setExtension_flag(true);
                    break;
                case 1:
                    ZcdhApplication.getInstance().setExtension_flag(false);
                    break;
                default:
                    break;
            }

        }

        if (reqId.equals(kREQ_ID_getMobileAndEmailRegex)) {
            Map<String, String> src = (Map<String, String>) result;
            if (null != result) {
                SharedPreferencesUtil.putValue(this, Constants.REGEX_PHONE_KEY,
                        src.get(Constants.REGEX_PHONE_KEY));
                SharedPreferencesUtil.putValue(this, Constants.REGEX_EMAIL_KEY,
                        src.get(Constants.REGEX_EMAIL_KEY));
            }
        }
        if (reqId.equals(kREQ_ID_findFlashScreenByImg)) {
            List<ImgDTO> imgs = null;
            if (result != null) {
                imgs = (List<ImgDTO>) result;
            }
            if (imgs != null && imgs.size() > 0) {
                ImgDTO img = imgs.get(0);
                final String url = img.getUrlPre() + "/1/" + img.getImgName();
                Log.e(TAG, "SCREEN : " + url);
                loadAdsImg(url);
            } else {
                Log.e(TAG, "handler.sendEmptyMessage(5)");
                handler.sendEmptyMessageDelayed(5, ads_delayed);
            }
        }
    }

    @Override
    public void onRequestFinished(String reqId) {

    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        if (error != null) {
            ZcdhException exc = (ZcdhException) error;
            if ((RequestException.EXC_CODE_SESSION + "").equals(exc
                    .getErrCode())) {
                ToastUtil.show(exc.getErrMessage());
                toMainScreen();
            }
        }
    }

    /* ================ 下载新版本 回调 ================ */
    @Override
    public void onDownloadStart() {

    }

    @Override
    public void onDownloadFinished() {
        toMainScreen();
        finish();
    }

    @Override
    public void onDownloadProgress(int p) {
    }

    @Override
    public void onError() {

    }

    @Override
    public void onCancel() {
        toMainScreen();
        finish();
    }

    @Override
    public void onDownloadSuccess() {
        updateAppService.installApk();
        finish();
    }

    private void animaWelcome(String url) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.3f, 1.0f,
                0.2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        scaleAnimation.setDuration(800);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.3f);
        alphaAnimation.setDuration(800);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                welcomeImg.setVisibility(View.GONE);
            }
        });
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        welcomeImg.startAnimation(animationSet);
        handler.sendMessageDelayed(Message.obtain(handler,2,url),500);
    }

    private final MyHandler handler = new MyHandler(this);

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private static class MyHandler extends Handler {

        //使用弱引用来防止内存泄露
        private final WeakReference<LoadingActivity> mActivity;

        private DisplayImageOptions options;

        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        public MyHandler(LoadingActivity activity) {
            mActivity = new WeakReference<>(activity);
            options = new DisplayImageOptions.Builder()
                    .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true).build();
        }

        @Override
        public void handleMessage(Message msg) {
            LoadingActivity activity = mActivity.get();
            switch (msg.what) {
                case 1:
                    if (msg.obj != null) {
                        String url = msg.obj.toString();
                        activity.animaWelcome(url);
                    }
                    break;
                case 2:
                    activity.adsImg.setVisibility(View.VISIBLE);
                    AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.4f, 1.0f);
                    alphaAnimation2.setDuration(1000);
                    activity.adsImg.startAnimation(alphaAnimation2);
                    String url = (String) msg.obj;
                    ImageLoader.getInstance().displayImage(url, activity.adsImg,
                            options, animateFirstListener);
                    break;
                case 5:
                    activity.doLogin();
                    activity.doUpgrade();
                    break;
                default:
                    break;
            }
        }
    }

    ;

}
