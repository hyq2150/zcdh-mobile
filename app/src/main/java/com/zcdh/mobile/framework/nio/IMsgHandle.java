/**
 * 
 * @author danny, 2013-9-16 下午5:43:31
 */
package com.zcdh.mobile.framework.nio;


/**
 * @author danny, 2013-9-16 下午5:43:31
 */
public interface IMsgHandle {
	
	
	/**
	 * 报文处理函数
	 * @param isSuccess 是否处理成功
	 * @param data ；当处理成功时，返回实际数据类型，当错误时，返回ZcdhException
	 * @author danny, 2013-11-20 上午10:04:23
	 */
	public void handleMessage(final Boolean isSuccess, final Object data);
	
}
