package com.zcdh.mobile.app.activities.newsmodel;

import android.app.Activity;
import android.webkit.JavascriptInterface;

public class JavaScriptInterface {
	Activity mContext;

	/** Instantiate the interface and set the context */
	public JavaScriptInterface(Activity c) {
		mContext = c;
	}

	
	@JavascriptInterface
	public void back(){
		mContext.finish();
	}
}
