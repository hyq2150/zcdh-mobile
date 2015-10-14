package com.zcdh.mobile.app.views.iflytek;

/**
 * 语音识别结果回调
 * @author yangjiannan
 *
 */
public interface MyVoiceRecognizerListner {

	void onVoiceSearchOK();
	
	void doSearch(String keyWord);
	
	void onError(String error);
}
