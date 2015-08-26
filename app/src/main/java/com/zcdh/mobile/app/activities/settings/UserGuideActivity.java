package com.zcdh.mobile.app.activities.settings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zcdh.mobile.R;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.start_at.GettingStartedActivity;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;
/**
 * 系统引导
 * @author yangjiannan
 *
 */
@EActivity(R.layout.activity_user_guide)
public class UserGuideActivity extends BaseActivity {

	@ViewById(R.id.browser)
	WebView browser;
	
	static final String url ="http://"+ K.Hosts.USER_GUIDE_URL + "/pages/jobhunte/help/help.jsp";

//	private static final String TAG = UserGuideActivity.class.getSimpleName(); 
	
	@AfterViews
	void bindViews(){
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), "功能介绍");
		
		final LoadingIndicator indicator = new LoadingIndicator(this);
		
		indicator.show();
		browser.getSettings().setJavaScriptEnabled(true);// 可用JS
		browser.setScrollBarStyle(0);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		browser.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				view.loadUrl(url);
				return true;
			}// 重写点击动作,用webview载入

			@Override
			public void onPageFinished(WebView view, String url) {
				indicator.dismiss();
				super.onPageFinished(view, url);
			}

		});

		browser.loadUrl(url);
	}
	
	
	
	/**
	 * 查看欢迎引导页
	 */
	@Click(R.id.showGuideStepBtn)
	void showGuideStepBtn(){
		startActivity(new Intent(this, GettingStartedActivity.class));
	}
	
	/**
	 * 重置操作指引
	 */
	@Click(R.id.resetUsageGuideBtn)
	void onResetUsageGuide(){
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		ab.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferencesUtil.putValue(UserGuideActivity.this, Constants.kSHARE_NEW_USER_GUIDE_LIST, false);
				SharedPreferencesUtil.putValue(UserGuideActivity.this, Constants.kSHARE_NEW_USER_GUIDE_MAP, false);
				SharedPreferencesUtil.putValue(UserGuideActivity.this, GettingStartedActivity.kGETTING_STARTED, false);
			}
		});
		ab.setTitle("重置操作指引");
		ab.setMessage("确定要重置操作指引吗？");
		ab.create().show();
	}
	
	

}
