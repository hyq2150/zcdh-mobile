package com.zcdh.mobile.app.activities.auth;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.main.NewMainActivity_;
import com.zcdh.mobile.app.activities.register.VerificationCodeActivity_;
import com.zcdh.mobile.app.activities.security.AuthResultCallback;
import com.zcdh.mobile.app.activities.security.ThirdPartAuthHelper;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.utils.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Pattern;

import cn.sharesdk.framework.ShareSDK;

import static com.mob.tools.utils.R.getStringRes;


/**
 * 登陆页面
 *
 * @author jeason, 2014-7-24 下午6:43:17
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements LoginListener,
        Callback {

    private static final String kDATA_DISPACHER_CLAZZ = "kBUNLDE_DISPACHER_CLAZZ";

    private static final String kDATA_PUSH_MSG_ID = "kDATA_PUSH_MSG_ID";

    private static final String TAG = "LoginActivity";

    @ViewById(R.id.body)
    LinearLayout bodyLl;

    @ViewById(R.id.userEditText)
    EditText userEditText;

    @ViewById(R.id.pwdEditText)
    EditText pwdEditText;

    @ViewById(R.id.loginBtn)
    Button loginBtn;

    @ViewById(R.id.hubei_auth)
    ImageButton loginBtnHB;

    private String account;

    private String password;

    private String thirdpart_uid;

    private ProcessDialog loading;

    private String Login_type;

    // 标识用户从app注销
    @Extra
    boolean is_exited;

    private ThirdPartAuthHelper authHelper;

    /*---- 点击推送通知 ,判断用户未登录时  ----*/
    @Extra
    String classz_dispatcher;

    @Extra
    long push_msg_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getIntent().getExtras() != null) {
            classz_dispatcher = getIntent().getExtras().getString(
                    kDATA_DISPACHER_CLAZZ);
            push_msg_id = getIntent().getExtras().getLong(kDATA_PUSH_MSG_ID);
        }
    }

    @Override
    protected void onResume() {
        if (loading != null) {
            loading.dismiss();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginBtn.setText("登 录");
        authHelper.setActivityResult(requestCode, resultCode, data);
    }

    @AfterViews
    void bindViews() {
        SystemServicesUtils.displayCustomTitle(this,
                getSupportActionBar(), getString(R.string.title_login));

        if (K.AppVersion.appVersion.equals(K.AppVersion.Versions.release) && SystemServicesUtils.getChannel(this).equals("hubei")) {
            //湖北公共招聘网渠道登录入口
            loginBtnHB.setVisibility(View.VISIBLE);
        } else {
            loginBtnHB.setVisibility(View.GONE);
        }

        authHelper = new ThirdPartAuthHelper(this, new AuthResultCallback() {

            @Override
            public void onAuthResult(String openId, String authType) {
                thirdpart_uid = openId;
                Login_type = authType;
                if (Constants.LOGIN_TYPE_QQ.equals(authType)) {
                    LoginHelper.getInstance(LoginActivity.this,
                            LoginActivity.this).doLoginQQ(openId);
                }
                if (Constants.LOGIN_TYPE_WEIBO.equals(authType)) {
                    LoginHelper.getInstance(LoginActivity.this,
                            LoginActivity.this).doLoginSinaWeibo(openId);
                }
            }
        });

        loading = new ProcessDialog(this);
    }

    /**
     * 重置密码
     */
    @Click(R.id.tv_find_pass)
    void onResetPwd() {
        VerificationCodeActivity_.intent(this).isForgetPwd(true).start();
    }

    /**
     * 注册
     */
    @Click(R.id.registerBtn)
    void onRegisterBtn() {
        VerificationCodeActivity_.intent(this).isRegisterNewUser(true).start();
    }

    /**
     * 通过职场导航账号登录
     */
    @Click(R.id.loginBtn)
    void onLoginByZcdh() {
        if (check_content()) {
            userEditText.clearFocus();
            loading.show(getString(R.string.loging));
            LoginHelper.getInstance(this, this).doLoginZCDH(account, password);
        }
    }

    /**
     * 通过湖北公共招聘网账号登录
     */
    @Click(R.id.hubei_auth)
    void onLoginByHB() {
        startActivityForResult(new Intent(this,
                LoginByHBActivity_.class), Constants.REQUEST_CODE_LOGIN);
        finish();
    }

    /**
     * wechat登录
     */
    @Click(R.id.wechat_auth)
    void onLoginByWechat() {
        loading.show("请稍后...");
        authHelper.requestWechatAuth(this);
    }

    /**
     * 用qq 登录
     */
    @Click(R.id.qq_auth)
    void onLoginByQQ() {
        loading.show("请稍后...");
        authHelper.requestTencentQQAuth();
    }

    /**
     * 通过新浪微博登录
     */
    @Click(R.id.sina_auth)
    void onLoginBySinaWeibo() {
        loading.show("请稍后...");
        authHelper.requestSinaWeiboAuth();
    }

    /**
     * 登录验证完成
     */
    @Override
    public void requestLoginFinished(int resultCode, String errorMsg) {

        if (Constants.kLOGIN_RESULT_BIND == resultCode) { // 去到账号绑定页面(第三方登录账号)
            ActivityDispatcher.toBind(this, Login_type, thirdpart_uid);
            finish();
        }

        if (Constants.kLOGIN_RESULT_FAILE == resultCode) { // 登录失败
            Toast.makeText(getApplicationContext(), errorMsg,
                    Toast.LENGTH_SHORT).show();
        }

        if (Constants.kLOGIN_RESULT_SUCCESS == resultCode) { // 职场导航APP登录成功后，获取环信账号
//            finish();
            getHxAccount();
        }

        if (Constants.KGET_HUANXIN_ACCOUNT_SUCCESS == resultCode) {//获取到环信账号
//            setResult(RESULT_OK);
//            finish();
            NewMainActivity_.intent(this).start();
        }

//        Intent loginResultMsg = new Intent(Constants.LOGIN_RESULT_ACTION);
//        loginResultMsg.putExtra(Constants.kRESULT_CODE, resultCode);
//
//        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
//                .getInstance(this);
//        localBroadcastManager.sendBroadcast(loginResultMsg);

        loading.dismiss();

        // 推送通知：显示相关详细页面
        if (!TextUtils.isEmpty(classz_dispatcher) && push_msg_id > 0) {
            Class<?> activity_cls = SystemServicesUtils
                    .getClass(classz_dispatcher);
            Intent intent = new Intent(this, activity_cls);
            intent.putExtra("id", push_msg_id);
            startActivity(intent);
        }

    }

    /**
     * 根据登录用户id，获取对应的环信账户
     */
    private void getHxAccount() {
        LoginHelper.getInstance(this, this)
                .getHxAccount(ZcdhApplication.getInstance().getZcdh_uid());
    }

    private boolean check_content() {
        account = userEditText.getText().toString();
        password = pwdEditText.getText().toString();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            ToastUtil.show(R.string.please_complete_all_login_field);
            return false;
        }
        account = account.trim();
        // validate phone_num & email
        if (!Pattern.compile(Constants.regex_email).matcher(account).matches()
                && !Pattern.compile(SharedPreferencesUtil
                .getValue(this, Constants.REGEX_PHONE_KEY, Constants.regex_phone)).matcher(account)
                .matches()) {
            ToastUtil.show(R.string.invalid_email_or_phone_format);
            return false;
        }
        return true;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT)
                        .show();
            }
            break;
            case Constants.MSG_LOGIN:
                thirdpart_uid = (String) msg.obj;
                Login_type = Constants.LOGIN_TYPE_WECHAT;
                LoginHelper.getInstance(this, this)
                        .doLoginWeChat(thirdpart_uid);
                break;
            case Constants.MSG_AUTH_CANCEL:
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT)
                        .show();
                break;
            case Constants.MSG_AUTH_ERROR:
                // 失败
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }
                String expName = msg.obj.getClass().getSimpleName();
                if ("WechatClientNotExistException".equals(expName)
                        || "WechatTimelineNotSupportedException".equals(expName)
                        || "WechatFavoriteNotSupportedException".equals(expName)) {
                    int resId = getStringRes(this, "wechat_client_inavailable");
                    if (resId > 0) {
                        showNotification(this.getString(resId));
                    }
                }
                break;
            case Constants.MSG_AUTH_COMPLETE:
                SharedPreferencesUtil.putValue(this, Constants.HEAD_IMG_URL,
                        (String) msg.obj);
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT)
                        .show();
                break;
        }
        return false;
    }

    // 在状态栏提示分享操作
    private void showNotification(String text) {
        ToastUtil.show(text);
    }
}
