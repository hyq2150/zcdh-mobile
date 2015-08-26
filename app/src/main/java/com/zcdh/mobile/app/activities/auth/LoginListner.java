package com.zcdh.mobile.app.activities.auth;

/**
 * 登录验证回调
 * @author yangjiannan
 *
 */
public interface LoginListner {

	/**
	 * 请求登录验证完成
	 */
	void requestLoginFinished(int resultCode, String errorMsg);
	
}
