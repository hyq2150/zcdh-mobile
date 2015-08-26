package com.zcdh.mobile.app.activities.main;

import com.makeramen.RoundedImageView;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.api.model.JobUserHomePageTitleDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.activities.personal.FavoritePostActivity_;
import com.zcdh.mobile.app.activities.personal.ResumeActivity_;
import com.zcdh.mobile.app.activities.personal.SubscriptionActivity_;
import com.zcdh.mobile.app.activities.security.AccountManagerActivity_;
import com.zcdh.mobile.app.activities.settings.FeedBackActivity_;
import com.zcdh.mobile.app.activities.settings.SettingsHomeActivity_;
import com.zcdh.mobile.app.extension.ExtensionDialog;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.share.Share;
import com.zcdh.mobile.utils.Gaosi;
import com.zcdh.mobile.utils.ImageUtils;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 显示主菜单
 *
 * @author YJN
 */

@SuppressLint("InflateParams")
public class MenuDialog extends Dialog implements OnClickListener,
        RequestListener {

    private String K_REQ_ID_FINDJOBUSERHOMEPAGETITLEDTO;

    private String K_REQ_ID_SIGNIN;

    private String K_REQ_FINDAPPSHARECONTENT;

    private IRpcJobUservice userService;

    private Activity mainActivity;

    // 头像
    private RoundedImageView head;

    private RelativeLayout rl_head;

    /**
     * 用户名
     */
    private TextView userName;

    /**
     * 积分
     */
    private TextView tv_jifen;

    private TextView tvSignin;

    private ImageView signinIcon;

    private JobUserHomePageTitleDTO titleDto;

    // 分享内容
    private List<JobEntShareDTO> shareContents;

    private static final String TAG = MenuDialog.class.getSimpleName();

    private View contentView = null;

    private int contentWidth;

    private MenuDialogListener menuDialogListener;

    public MenuDialog(Activity context, MenuDialogListener listener) {
        super(context, R.style.fadeInOutDialog);
        menuDialogListener = listener;
        SystemServicesUtils.initShareSDK(context);

        mainActivity = (NewMainActivity) context;
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
                toDismiss();
                menuDialogListener.onHidden();
                // notify that we consumed this event
                return true;
            }
        }
        // let the system handle the event
        return super.onTouchEvent(event);
    }

    protected void onStart() {
    }

    protected void onStop() {

        super.onStop();
    }

    @SuppressWarnings("deprecation")
    private void bindView() {

        // 已登录
        if (ZcdhApplication.getInstance().getZcdh_uid() != -1) {
            contentView = LayoutInflater.from(mainActivity).inflate(
                    R.layout.main_menu_new, null);
            setContentView(contentView);

            RelativeLayout rl_resume = (RelativeLayout) findViewById(R.id.resumeRl);
            rl_resume.setOnClickListener(this);

            RelativeLayout rl_favorites = (RelativeLayout) findViewById(R.id.favriteRl);
            rl_favorites.setOnClickListener(this);

            RelativeLayout rl_feeds = (RelativeLayout) findViewById(R.id.subscriptionRl);
            rl_feeds.setOnClickListener(this);

            RelativeLayout rl_orders = (RelativeLayout) findViewById(R.id.ordersRl);
            rl_orders.setOnClickListener(this);

            head = (RoundedImageView) findViewById(R.id.head);
            head.setOnClickListener(this);
            userName = (TextView) findViewById(R.id.tv_name);
            tv_jifen = (TextView) findViewById(R.id.tv_jifen);

            findViewById(R.id.checkoutLl).setOnClickListener(this);
            findViewById(R.id.accountLl).setOnClickListener(this);

            // 签到
            tvSignin = (TextView) findViewById(R.id.signinText);
            signinIcon = (ImageView) findViewById(R.id.siginIcon);

            //
            loadData();
        } else { // 未登录
            contentView = LayoutInflater.from(mainActivity).inflate(
                    R.layout.main_menu_new_unlogin, null);
            contentView.findViewById(R.id.loginBtn).setOnClickListener(this);
            setContentView(contentView);
        }

        getWindow().getDecorView().setBackgroundColor(
                getContext().getResources().getColor(
                        android.R.color.transparent));

        Window menuWindow = getWindow();
        WindowManager.LayoutParams wl = menuWindow.getAttributes();
        wl.gravity = Gravity.LEFT;
        contentWidth = mainActivity.getWindowManager().getDefaultDisplay()
                .getWidth();
        contentWidth = contentWidth * 7 / 10;
        wl.width = contentWidth;
        menuWindow.setAttributes(wl);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);

        // 分享
        LinearLayout rl_share = (LinearLayout) findViewById(R.id.shareLl);
        rl_share.setOnClickListener(this);

        // 反馈
        LinearLayout rl_feedback = (LinearLayout) findViewById(R.id.feedBackLl);
        rl_feedback.setOnClickListener(this);

        // 系统设置
        RelativeLayout rl_setting = (RelativeLayout) findViewById(R.id.settingsRl);
        rl_setting.setOnClickListener(this);

        // 邀请码
        RelativeLayout extensionRl = (RelativeLayout) findViewById(R.id.extensionRl);
        extensionRl.setOnClickListener(this);
    }

    public void show() {
        if (!ZcdhApplication.getInstance().isExtension_flag()
                && !SharedPreferencesUtil.getValue(mainActivity,
                ExtensionDialog.EXTENSION_FIRST, false)) {
            new ExtensionDialog(mainActivity,
                    true).dealInvitationcode();
            return;
        }
        Animation tranAnimation = new TranslateAnimation(contentWidth * (-1),
                0, 0, 0);
        tranAnimation.setInterpolator(new LinearInterpolator());
        tranAnimation.setDuration(300);
        contentView.startAnimation(tranAnimation);

        super.show();
    }

    public void toDismiss() {
        Animation tranAnimation = new TranslateAnimation(0,
                contentWidth * (-1), 0, 0);
        tranAnimation.setInterpolator(new LinearInterpolator());
        tranAnimation.setDuration(300);
        contentView.startAnimation(tranAnimation);
        tranAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });

    }

    public void loadData() {
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
            userName.setText(titleDto.getUserName());

        } else {
            userName.setText("尚未设置");
        }
        if (titleDto.getSignIn() == 0) {
            tvSignin.setText(getContext().getResources().getString(
                    R.string.sign));
            signinIcon.setImageResource(R.drawable.grzx19);
        } else {
            tvSignin.setText(getContext().getResources().getString(
                    R.string.signed));
            signinIcon.setImageResource(R.drawable.grzx05);
        }
        tv_jifen.setText(String.format("积分:%d", titleDto.getIntegralTotals()));
    }

    /**
     * @author jeason, 2014-7-21 下午2:09:04
     */
    public void getHeadFromURL() {
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    return ImageUtils.GetBitmapByUrl(titleDto.getImg()
                            .getOriginal());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(Bitmap result) {
                if (result != null) {
                    head.setImageBitmap(result);
                    rl_head.setBackgroundDrawable(
                            new BitmapDrawable(Gaosi.BoxBlurFilter(result)));
                }
            }
        }.execute();
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

    /**
     * @author jeason, 2014-7-22 上午9:16:23 签到
     */
    private void signIn() {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkoutLl:
                signIn();
                break;

            case R.id.shareLl:
                onShare();
                break;

            default:
                dispatchActivity(v.getId());
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
        if (reqId.equals(K_REQ_ID_FINDJOBUSERHOMEPAGETITLEDTO)) {
            if (result != null) {
                titleDto = (JobUserHomePageTitleDTO) result;
            }
            setUserInfo();
        }

        if (reqId.equals(K_REQ_ID_SIGNIN)) {
            if (result != null) {
                int success = (Integer) result;
                if (success == 0) {
                    ToastUtil.show(R.string.sign_success);
                    loadData();
                }
            }
        }

        if (reqId.equals(K_REQ_FINDAPPSHARECONTENT)) {
            if (result != null) {
                shareContents = (List<JobEntShareDTO>) result;
                Share.showShare(getContext(), shareContents, false, null);
            }
        }
    }

    @Override
    public void onRequestFinished(String reqId) {

    }

    @Override
    public void onRequestError(String reqID, Exception error) {

    }

    public void dispatchActivity(int viewId) {

        switch (viewId) {
            case R.id.extensionRl:
                new ExtensionDialog(mainActivity,
                        false).dealInvitationcode();
                break;
            case R.id.resumeRl:
                ResumeActivity_.intent(mainActivity).startForResult(Constants.REQUEST_CODE_MODIFY_RESUME);
                break;
            case R.id.favriteRl:
                FavoritePostActivity_.intent(mainActivity).start();
                break;
            case R.id.subscriptionRl:
                SubscriptionActivity_.intent(mainActivity).start();
                break;
            case R.id.settingsRl:
                SettingsHomeActivity_.intent(mainActivity).startForResult(Constants.REQUEST_CODE_SETTING);
                break;
            case R.id.loginBtn:
                LoginActivity_.intent(mainActivity).startForResult(Constants.REQUEST_CODE_LOGIN);
                break;
            case R.id.accountLl:
                MobclickAgent.onEvent(mainActivity, Constants.UMENG_ACCOUNT_ACTION);
                if (titleDto != null && titleDto.getImg() != null) {
                    AccountManagerActivity_.intent(mainActivity)
                            .userPhotoUrl(titleDto.getImg().getOriginal()).start();
                } else {
                    AccountManagerActivity_.intent(mainActivity).start();
                }
                break;
            case R.id.head:
                if (RegisterUtil.isRegisterUser(mainActivity)) {
                    if (titleDto.getImg() != null
                            && !TextUtils
                            .isEmpty(titleDto.getImg().getOriginal())) {
                        AccountManagerActivity_.intent(mainActivity)
                                .gotoModifyPhoto(true)
                                .userPhotoUrl(titleDto.getImg().getOriginal())
                                .start();
                    } else {
                        AccountManagerActivity_.intent(mainActivity)
                                .gotoModifyPhoto(true).start();
                    }

                } else {
                    ActivityDispatcher.to_login(mainActivity);
                }
                break;
            case R.id.ordersRl:
                ActivityDispatcher.toOrders(mainActivity);
                break;
            case R.id.feedBackLl:
                // 意见反馈
                FeedBackActivity_.intent(mainActivity).start();
                break;
        }
    }

    interface MenuDialogListener {

        void onHidden();
        // void onShow();
    }
}
