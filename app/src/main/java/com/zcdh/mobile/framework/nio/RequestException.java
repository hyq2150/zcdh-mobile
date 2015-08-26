package com.zcdh.mobile.framework.nio;

import com.zcdh.core.nio.except.ZcdhException;

/**
 * 
 * @author 请求异常
 *
 */
public class RequestException extends ZcdhException {
	
	/**
	 * 服务连接异常
	 */
	public static final int EXC_CODE_SESSION = 0;
	
	/**
	 *  网络连接异常
	 */
	public static final int EXC_CODE_NETWORK = 1;
	
	/**
	 *  服务异常
	 */
	public static final int EXC_CODE_SERVICE = 2;

	
	
}
