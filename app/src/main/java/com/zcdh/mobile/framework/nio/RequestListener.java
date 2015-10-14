package com.zcdh.mobile.framework.nio;

/**
 * 
 * @author yangjiannan
 *
 */
public interface RequestListener {

	/**
	 * 开始发起请求
	 * @param reqId
	 */
	void onRequestStart(String reqId);
	/**
	 * 请求成功
	 * @param result 结果
	 */
	void onRequestSuccess(String reqId, Object result);
	
	/**
	 * 请求结束
	 * @param reqId
	 */
	void onRequestFinished(String reqId);
	
	/**
	 * 请求发生错误
	 * @param msg
	 */
	void onRequestError(String reqID, Exception error);
	
	
	
}
