/**
 * 
 * @author danny, 2013-10-31 下午3:08:04
 */
package com.zcdh.mobile.framework.nio;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zcdh.core.nio.common.MsgRequest;
import com.zcdh.core.nio.common.MsgResponse;
import com.zcdh.core.nio.except.ZcdhException;

/**
 * 远程服务管理类
 * 
 * @author danny, 2013-10-31 下午3:08:04
 */
public class RemoteServiceManager {

	/**
	 * 发送同步请求
	 * 
	 * @param req
	 *            ，请求参数
	 * @return 返回请求结果
	 * @author danny, 2013-10-30 下午4:31:01
	 */
	public static MsgResponse sendSyncMessage(MsgRequest req) {
		return MNioClient.getInstance().sendSyncMessage(req);
	}

	/**
	 * 发送同步请求
	 * 
	 * @param req
	 *            ：请求参数
	 * @param clazz
	 *            ：返回数据类型
	 * @return //返回 clazz类型数据
	 * @throws ZcdhException
	 * @author danny, 2013-10-30 下午4:33:17
	 * @see 调用方式：client.sendSyncMessage(req, Test.class);
	 */
	public static <T> T sendSyncMessage(MsgRequest req, Class<T> clazz)
			throws ZcdhException {
		return MNioClient.getInstance().sendSyncMessage(req, clazz);
	}

	/**
	 * 发送同步请求
	 * 
	 * @param req
	 *            ：请求参数
	 * @param clazz
	 *            ：返回数据类型
	 * @return //返回 clazz类型数据
	 * @throws ZcdhException
	 * @author danny, 2013-10-30 下午4:33:17
	 * @see 调用方式：client.sendSyncMessage(req, new TypeReference<List<User>>(){});
	 */
	public static <T> T sendSyncMessage(MsgRequest req, TypeReference<T> clazz)
			throws ZcdhException {
		return MNioClient.getInstance().sendSyncMessage(req, clazz);
	}

	public static <T> T getRemoteService(Class<T> iface) {
		return MNioClient.getInstance().getInterfaceProxy(iface);
	}

	public static <T> T getRemoteService(Class<T> iface,
			RequestListener requestListener) {
		RequestDispatcher.setRequestListner(requestListener);
		return MNioClient.getInstance().getInterfaceProxy(iface);
	}

	public static void removeService(RequestListener requestListener) {
		Log.e("RemoteServiceManager", "移除远程服务调用功能("+requestListener.getClass().getName()+")");
		System.out.println("移除远程服务调用功能("+requestListener.getClass().getName()+")");
		RequestDispatcher.unsubscribe(requestListener);
	}
	
	public static void test(String aa){
		System.out.println(aa);
	}
}
