package com.zcdh.mobile.app.views.iflytek;

/**
 * 语音识别结果回调
 * @author yangjiannan
 *
 */
public interface MyVoiceRecognizerListner {

	public void onVoiceSearchOK();
	
	public void doSearch(String keyWord);
	
	public void onError(String error);
}
