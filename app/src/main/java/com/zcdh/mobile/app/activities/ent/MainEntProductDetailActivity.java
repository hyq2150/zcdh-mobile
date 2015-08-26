package com.zcdh.mobile.app.activities.ent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.util.Log;
import android.webkit.WebView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 产品详细
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_product_detail)
public class MainEntProductDetailActivity extends BaseActivity {

	@Extra
	String productUrl;

	@ViewById(R.id.webView)
	WebView webView;

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(getApplicationContext(),
				getSupportActionBar(),
				getString(R.string.activity_title_product_detal));
		webView.loadUrl(productUrl);
		loadUrl(webView, productUrl);
		Log.i("url-----", productUrl);
	}
	
	
	void loadUrl(WebView wv, String url){
		webView.loadUrl(url);
	}
	
	@OptionsItem(android.R.id.home)
	void onBack(){
		finish();
	}
}
