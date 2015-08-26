package com.zcdh.mobile.framework.nio;

/**
 * 
 * @author yangjiannan
 * 用于将服务调用与发起请求端给定的requestID 绑定
 */
public interface RequestChannelInter<T> {

	void identify(String identify, RequestListener requestHandler);
}
