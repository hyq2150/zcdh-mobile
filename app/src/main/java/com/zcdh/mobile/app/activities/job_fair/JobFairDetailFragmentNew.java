/**
 * 
 * @author jeason, 2014-6-19 上午9:58:48
 */
package com.zcdh.mobile.app.activities.job_fair;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.newsmodel.JavaScriptInterface;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.share.Share;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.job_fair_detail_webview)
public class JobFairDetailFragmentNew extends BaseActivity implements RequestListener {

	protected static final String TAG = JobFairDetailFragmentNew.class
			.getSimpleName();

	private IRpcJobFairService jobfairService;
	private String K_REQ_ID_SHARE_CONTENT;

	@ViewById(R.id.browser)
	WebView browser;

	@Extra
	String url;

	@Extra
	String title = "";
	
	@Extra
	boolean isShare;

	@Extra
	long InformationTitleInfoId;
	
	@Extra
	long fairID;

	private List<JobEntShareDTO> shareContents;
	
	private ProcessDialog processDialog;

	/**
	 * 0 不显示标题， 1 显示标题
	 */
	@Extra
	boolean isShowTitle = true;

	public void onPause() {
		callHiddenWebViewMethod("onPause");
		super.onPause();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jobfairService = RemoteServiceManager
				.getRemoteService(IRpcJobFairService.class);
			if (!isShowTitle) {
				SystemServicesUtils.hideActionbar(this);
			}else {
				if (!TextUtils.isEmpty(title)) {
					SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), title);
				}
			}

			HashMap<String, String> logMap = new HashMap<String, String>();
			logMap.put("DECEIVE_TYPE", "Android");
			logMap.put(Constants.COVER_ID,
						String.valueOf(InformationTitleInfoId));
	}

	@AfterViews
	void bindViews() {
		if (!TextUtils.isEmpty(title) && !isShowTitle) {
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(),title);
		}
		processDialog = new ProcessDialog(this);
		processDialog.show();
		initWebView();

		if (null != url) {
			loadUrl(browser, url);
		}else {
			Toast.makeText(this, "链接为空", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@SuppressWarnings("deprecation")
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK && !isShowTitle)  
        {  
//        	if (!isShowTitle) {
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
//			}else {
//				return true;
//			}
        }  
        return false;  
    }  
	
	/**监听对话框里面的button点击事件*/  
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
    {  
        public void onClick(DialogInterface dialog, int which)  
        {  
            switch (which)  
            {  
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
    
	private void initWebView() {

		WebSettings webSettings = browser.getSettings();
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		webSettings.setDomStorageEnabled(true);
		webSettings.setJavaScriptEnabled(true);// 可用JS
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setAppCachePath(getExternalCacheDir().toString() + File.separator + "test.db");
		webSettings.setAppCacheEnabled(true);
		browser.addJavascriptInterface(new JavaScriptInterface(this),
				"MobileApp");
		browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		//browser.setScrollBarStyle(0);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		browser.requestFocus();
		browser.setVisibility(View.VISIBLE);
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
				processDialog.dismiss();
				browser.setVisibility(View.VISIBLE);
				super.onPageFinished(view, url);
				if (TextUtils.isEmpty(title)
						&& !TextUtils.isEmpty(view.getTitle())) {
					SystemServicesUtils.setActionBarCustomTitle(
							JobFairDetailFragmentNew.this, getSupportActionBar(),
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
//		if (browser != null) {
//			browser.loadUrl("about:blank");
//		}
		try {
		Method method = WebView.class.getMethod(name);
		method.invoke(browser);
	} catch (NoSuchMethodException e) {
		Log.i("No such method: " + name, e.toString());
	} catch (IllegalAccessException e) {
		Log.i("Illegal Access: " + name, e.toString());
	} catch (InvocationTargetException e) {
		Log.d("TAG","Invocation Target Exception: " + name +"  " + e.toString());
	}
	}

	/**
	 * 查找分享内容
	 */
	@Background
	void findShareContent() {
		jobfairService.findFairShareContent(fairID).identify(
				K_REQ_ID_SHARE_CONTENT = RequestChannel.getChannelUniqueID(),
				this);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {

		if (reqId.equals(K_REQ_ID_SHARE_CONTENT)) {
			if (result != null) {
				shareContents = (List<JobEntShareDTO>) result;
				Share.showShare(this, shareContents, false,
						null);
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		if (reqID.equals(K_REQ_ID_SHARE_CONTENT)) {
			Toast.makeText(this, "获取分享内容失败", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (isShare) {
			getMenuInflater().inflate(R.menu.share_select, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@OptionsItem(R.id.action_share_select)
	void onShare() {
		SystemServicesUtils.initShareSDK(this);
		if (shareContents == null) {
			findShareContent();
		} else {
			Share.showShare(this, shareContents, false, null);
		}
	}
}
