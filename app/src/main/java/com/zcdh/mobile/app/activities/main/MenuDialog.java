package com.zcdh.mobile.app.activities.main;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcAppConfigService;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.AdminAppConfigModelDTO;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.api.model.JobUserHomePageTitleDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.security.AccountManagerActivity_;
import com.zcdh.mobile.app.activities.settings.FeedBackActivity_;
import com.zcdh.mobile.app.activities.settings.SettingsHomeActivity;
import com.zcdh.mobile.app.extension.ExtensionDialog;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.share.Share;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.SystemUtil;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示主菜单
 *
 * @author YJN
 */

@SuppressLint("InflateParams")
public class MenuDialog extends Dialog
        implements PullToRefreshBase.OnRefreshListener<ListView>, /*MyEvents.Subscriber,*/
        OnClickListener, AdapterView.OnItemClickListener,
        RequestListener {

    //个人信息
    private String K_REQ_ID_FINDJOBUSERHOMEPAGETITLEDTO;

    //签到
    private String K_REQ_ID_SIGNIN;

    //分享内容
    private String K_REQ_FINDAPPSHARECONTENT;

    //模块信息
    private String KREQ_ID_FIND_APP_CONFIG_MODEL_INFO;

    //模块
    private String KREQ_ID_FIND_APPCONFIG_MODULE_BY_ID;

    private IRpcJobUservice userService;

    private IRpcAppConfigService appConfigService;

    private Activity mainActivity;

    // 头像
    private RoundedImageView ivHead;

    private RelativeLayout rlHead;

    /**
     * 用户名
     */
    private TextView tvUserName;

    /**
     * 积分
     */
    private TextView tvJifen;

    private TextView tvAccount;

    private TextView tvSignin;

    private RelativeLayout rlBtns;

    private PullToRefreshListView configListview;

    private MenuDialogAdapter adapter;

    private JobUserHomePageTitleDTO titleDto;

    // 分享内容
    private List<JobEntShareDTO> shareContents;

    private static final String TAG = MenuDialog.class.getSimpleName();

    private int contentWidth;

    private MenuDialogListener menuDialogListener;

    private ArrayList<AdminAppConfigModelDTO> datas = new ArrayList<AdminAppConfigModelDTO>();

    public MenuDialog(Activity context, MenuDialogListener listener) {
        super(context, R.style.pop_dialog_theme);
        menuDialogListener = listener;
        SystemServicesUtils.initShareSDK(context);
//        MyEvents.register(this);
        mainActivity = context;
        appConfigService = RemoteServiceManager
                .getRemoteService(IRpcAppConfigService.class);
        userService = RemoteServiceManager
                .getRemoteService(IRpcJobUservice.class);
        bindView();
    }

    /**
     * 解决在2.x 系统下，点击对话框外面无法dismiss
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // I only care if the event is an UP action
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // create a rect for storing the window rect
            Rect r = new Rect(0, 0, 0, 0);
            // retrieve the windows rect
            this.getWindow().getDecorView().getHitRect(r);
            // check if the event position is inside the window rect
            boolean intersects = r.contains((int) event.getX(),
                    (int) event.getY());
            // if the event is not inside then we can close the activity
            if (!intersects) {
                // close the activity
                // this.finish();
                Log.i(TAG, "touched outside");
//                toDismiss();
                dismiss();
                menuDialogListener.onHidden();
                // notify that we consumed this event
                return true;
            }
        }
        // let the system handle the event
        return super.onTouchEvent(event);
    }

    private void bindView() {
        setContentView(R.layout.main_menu_new);
        adapter = new MenuDialogAdapter(mainActivity, datas);
        configListview = (PullToRefreshListView) findViewById(R.id.menu_listview);
        configListview.setAdapter(adapter);
        configListview.setOnItemClickListener(this);
        configListview.setOnRefreshListener(this);

        ivHead = (RoundedImageView) findViewById(R.id.head);
        ivHead.setOnClickListener(this);

        tvUserName = (TextView) findViewById(R.id.tv_name);
        tvJifen = (TextView) findViewById(R.id.tv_jifen);
        tvAccount = (TextView) findViewById(R.id.tv_account);

        findViewById(R.id.headInfo).setOnClickListener(this);
        findViewById(R.id.ll_checkout).setOnClickListener(this);
        findViewById(R.id.ll_account).setOnClickListener(this);

        // 签到
        tvSignin = (TextView) findViewById(R.id.tv_sign);
        rlBtns = (RelativeLayout) findViewById(R.id.rl_btns);

        Window menuWindow = getWindow();
        getWindow().getDecorView().setBackgroundColor(
                getContext().getResources().getColor(
                        android.R.color.transparent));
        WindowManager.LayoutParams wl = menuWindow.getAttributes();
        wl.gravity = Gravity.LEFT;
//        contentWidth = ScreenUtils.getScreenWidth(ZcdhApplication.getInstance());
        contentWidth = mainActivity.getWindowManager().getDefaultDisplay()
                .getWidth();
        contentWidth = contentWidth * 7 / 10;
        wl.width = contentWidth;
        wl.height = WindowManager.LayoutParams.MATCH_PARENT;
        menuWindow.setAttributes(wl);
        rlHead = (RelativeLayout) findViewById(R.id.rl_head);

        // 分享
        findViewById(R.id.shareLl).setOnClickListener(this);

        // 反馈
        findViewById(R.id.feedBackLl).setOnClickListener(this);

        loadAllData(true);
    }

    @Override
    public void show() {
        if (!ZcdhApplication.getInstance().isExtension_flag()
                && !SharedPreferencesUtil.getValue(mainActivity,
                ExtensionDialog.EXTENSION_FIRST, false)) {
            new ExtensionDialog(mainActivity,
                    true).dealInvitationcode();
            return;
        }
        super.show();
    }

    public void loadData() {
        //用户信息数据
        BackgroundExecutor.execute(new Task("", 0, "") {
            @Override
            public void execute() {
                userService.findJobUserHomePageTitleDTO(
                        ZcdhApplication.getInstance().getZcdh_uid()).identify(
                        K_REQ_ID_FINDJOBUSERHOMEPAGETITLEDTO = RequestChannel
                                .getChannelUniqueID(), MenuDialog.this);
            }
        });
    }

    /**
     * @author jeason, 2014-6-16 上午11:00:06
     */
    private void setUserInfo() {
        if (titleDto.getImg() != null) {
            getHeadFromURL();
        }
        if (!StringUtils.isBlank(titleDto.getUserName())) {
            tvUserName.setText(titleDto.getUserName());

        } else {
            tvUserName.setText("尚未设置");
        }

        Drawable signDrawable = getContext().getResources().getDrawable(R.drawable.icon_sign);
        Drawable signedDrawable = getContext().getResources().getDrawable(R.drawable.icon_signed);
        signDrawable.setBounds(0, 0, signDrawable.getMinimumWidth(),
                signDrawable.getMinimumHeight());
        signedDrawable.setBounds(0, 0, signedDrawable.getMinimumWidth(),
                signedDrawable.getMinimumHeight());

        if (titleDto.getSignIn() == 0) {
            tvSignin.setText(getContext().getResources().getString(
                    R.string.sign));

            tvSignin.setCompoundDrawables(null, signDrawable
                    , null, null);
        } else {
            tvSignin.setText(getContext().getResources().getString(
                    R.string.signed));
            tvSignin.setCompoundDrawables(null,
                    signedDrawable, null, null);
        }

        tvAccount.setTextColor(Color.WHITE);
        tvSignin.setTextColor(Color.WHITE);

        tvJifen.setText(String.format(
                ZcdhApplication.getInstance().getResources().getString(R.string.user_jifen),
                titleDto.getIntegralTotals()));
        setWidgetAttribute(true);
    }

    /**
     * @author jeason, 2014-7-21 下午2:09:04
     */
    public void getHeadFromURL() {
        ImageLoader.getInstance().displayImage(titleDto.getImg().getOriginal(), ivHead,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        rlHead.setBackgroundResource(R.drawable.gaosihead);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        rlHead.setBackgroundResource(R.drawable.gaosihead);
                    }
                });
    }

    void findShareContent() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            @Override
            public void execute() {
                try {
                    userService.findAppShareContent(
                            ZcdhApplication.getInstance().getZcdh_uid())
                            .identify(
                                    K_REQ_FINDAPPSHARECONTENT = RequestChannel
                                            .getChannelUniqueID(),
                                    MenuDialog.this);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler()
                            .uncaughtException(Thread.currentThread(), e);
                }
            }

        });
    }

    void findAppConfigModelByID(final long modelID) {
        BackgroundExecutor.execute(new Task("", 0, "") {
            @Override
            public void execute() {
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
                                MenuDialog.this);
            }
        });
    }

    /**
     * @author jeason, 2014-7-22 上午9:16:23 签到
     */
    private void signIn() {
        if (ZcdhApplication.getInstance().getZcdh_uid() < 0) {
            ToastUtil.show(R.string.login_first);
            return;
        }
        if (titleDto != null && titleDto.getSignIn() == 0) {

            BackgroundExecutor.execute(new Task(null, 0, null) {

                @Override
                public void execute() {
                    userService.signIn(
                            ZcdhApplication.getInstance().getZcdh_uid())
                            .identify(
                                    K_REQ_ID_SIGNIN = RequestChannel
                                            .getChannelUniqueID(),
                                    MenuDialog.this);
                }
            });

        } else {
            ToastUtil.show(R.string.signed_today);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        findAppConfigModelByID(SharedPreferencesUtil.getValue(mainActivity, Constants.APPCONFIG_CUSTOM_ITEM_CODE_INFORMATION, 0l));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AdminAppConfigModelDTO title = (AdminAppConfigModelDTO) adapter.getItem(position - 1);
        //如果是未登录用户，除了系统设置和邀请码，其他不能点击
        if (ZcdhApplication.getInstance().getZcdh_uid() < 0) {
            if (!datas.get(position - 1).getModelUrl()
                    .contains(SettingsHomeActivity.class.getSimpleName()) &&
                    !datas.get(position - 1).getModel_code()
                            .contains(ExtensionDialog.class.getSimpleName())) {
                ToastUtil.show(R.string.login_first);
                return;
            }
        }
        switch (Integer.valueOf(title.getOpen_type())) {
            case 1:
                SystemServicesUtils.openWithWebView(
                        mainActivity,
                        title.getCustom_param(),
                        title.getModel_name(),
                        title.getImgUrl());
                break;
            case 2:
                if (title.getModelUrl().contains(ExtensionDialog.class.getSimpleName())) {
                    new ExtensionDialog(mainActivity, false).dealInvitationcode();
                } else {
                    try {
                        Class<?> activity_cls = SystemServicesUtils.getClass(title
                                .getModelUrl());
                        Log.e(TAG, "activity_cls : " + activity_cls);
                        mainActivity.startActivityForResult(new Intent(mainActivity, activity_cls),
                                SettingsHomeActivity.REQUEST_CODE_SETTING);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //签到
            case R.id.ll_checkout:
                signIn();
                break;
            //分享
            case R.id.shareLl:
                onShare();
                break;
            //登录
            case R.id.headInfo://当没有登录时，点击登录
                if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
                    ActivityDispatcher.toLogin(mainActivity);
                }
                break;
            //账号管理
            case R.id.ll_account:
                if (ZcdhApplication.getInstance().getZcdh_uid() < 0) {
                    ToastUtil.show(R.string.login_first);
                    return;
                }
                MobclickAgent.onEvent(mainActivity, Constants.UMENG_ACCOUNT_ACTION);
                if (titleDto != null && titleDto.getImg() != null) {
                    AccountManagerActivity_.intent(mainActivity)
                            .userPhotoUrl(titleDto.getImg().getOriginal()).start();
                } else {
                    AccountManagerActivity_.intent(mainActivity).start();
                }
                break;
            //头像点击
            case R.id.head:
                if (RegisterUtil.isRegisterUser(mainActivity)) {
                    if (titleDto.getImg() != null
                            && !TextUtils
                            .isEmpty(titleDto.getImg().getOriginal())) {
                        AccountManagerActivity_.intent(mainActivity)
                                .gotoModifyPhoto(true)
                                .userPhotoUrl(titleDto.getImg().getOriginal())
                                .startForResult(Constants.REQUEST_CODE_ACCOUNT_MANAGER);
                    } else {
                        AccountManagerActivity_.intent(mainActivity)
                                .gotoModifyPhoto(true).startForResult(
                                Constants.REQUEST_CODE_ACCOUNT_MANAGER);
                    }

                } else {
                    ActivityDispatcher.toLogin(mainActivity);
                }
                break;
            // 意见反馈
            case R.id.feedBackLl:
                FeedBackActivity_.intent(mainActivity).start();
                break;
            default:
                break;
        }
    }

    void onShare() {
        SystemServicesUtils.initShareSDK(mainActivity);
        if (shareContents == null) {
            // 后台读取分享内容
            findShareContent();
        } else {
            // 弹出分享菜单
            Share.showShare(getContext(), shareContents, false, null);
        }
    }

    @Override
    public void onRequestStart(String reqId) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRequestSuccess(String reqId, Object result) {

        //用户信息
        if (reqId.equals(K_REQ_ID_FINDJOBUSERHOMEPAGETITLEDTO)) {
            if (result != null) {
                titleDto = (JobUserHomePageTitleDTO) result;
            }
            setUserInfo();
        }

        //签到结果
        if (reqId.equals(K_REQ_ID_SIGNIN)) {
            if (result != null) {
                int success = (Integer) result;
                if (success == 0) {
                    ToastUtil.show(R.string.sign_success);
                    loadData();
                } else {
                    ToastUtil.show(R.string.sign_failed);
                }
            }
        }

        //分享内容
        if (reqId.equals(K_REQ_FINDAPPSHARECONTENT)) {
            if (result != null) {
                shareContents = (List<JobEntShareDTO>) result;
                Share.showShare(getContext(), shareContents, false, null);
            }
        }

        if (reqId.equals(KREQ_ID_FIND_APPCONFIG_MODULE_BY_ID)) {
            List<AdminAppConfigModelDTO> appconfig = (List<AdminAppConfigModelDTO>) result;
            if (appconfig != null && !appconfig.isEmpty()) {
                datas.clear();
                for (int i = 0; i < appconfig.size(); i++) {
                    datas.add(appconfig.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRequestFinished(String reqId) {
        if (configListview.isRefreshing()) {
            configListview.onRefreshComplete();
        }
    }

    @Override
    public void onRequestError(String reqID, Exception error) {
        error.printStackTrace();
    }

    interface MenuDialogListener {

        void onHidden();
    }

    public void loadAllData(boolean needRefreshAppConfig) {
        //重置各个控件的状态
        setWidgetAttribute(false);
        //更新用户信息
        loadData();
        //更新模块配置
        if (needRefreshAppConfig) {
            findAppConfigModelByID(SharedPreferencesUtil.getValue(mainActivity, Constants.APPCONFIG_CUSTOM_ITEM_CODE_INFORMATION, 0l));
        }
    }

    public void setWidgetAttribute(boolean isEnable) {
        ivHead.setEnabled(isEnable);
        ivHead.setEnabled(isEnable);
        rlBtns.setEnabled(isEnable);
        tvJifen.setVisibility(isEnable ? View.VISIBLE : View.INVISIBLE);
        if (!isEnable) {
            tvUserName.setText(R.string.str_click_to_login);
            Drawable signDrawable = getContext().getResources().getDrawable(R.drawable.icon_sign);
            signDrawable.setBounds(0, 0, signDrawable.getMinimumWidth(),
                    signDrawable.getMinimumHeight());
            tvSignin.setCompoundDrawables(null, signDrawable, null, null);
            tvSignin.setText(R.string.str_checkout);
            rlHead.setBackgroundColor(
                    ZcdhApplication.getInstance().getResources().getColor(R.color.menu_background));
            ivHead.setImageResource(R.drawable.default_head);
        }
    }
}