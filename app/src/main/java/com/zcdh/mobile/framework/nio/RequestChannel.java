package com.zcdh.mobile.framework.nio;

import java.lang.reflect.Type;
import java.util.HashMap;

import android.util.Log;

import com.zcdh.core.nio.common.MsgRequest;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.framework.K;

/**
 * 
 * @author yangjiannan 用于将服务调用与发起请求端给定的requestID 绑定
 * 
 */
public class RequestChannel<T> implements RequestChannelInter<T> {

	/**
	 * 建立连接异常
	 */
	public static final int REQ_SESSION_ERROR = 0;

	/**
	 * 已发送服务请求
	 * 
	 */
	public static final int REQ_SENDED = 1;

	/**
	 * 请求结果处理池
	 */
	private final static HashMap<String, RequestListener> REQUE_HASH_MAP = new HashMap<String, RequestListener>();

	/**
	 * 每次调用服务的时候，会有一个request id， 标识每次服务调用； 而如何将request id 从服务调用的地方到
	 * 中间件发送数据的地方，藉由Threadlocal 保存。 ThreaLcoal 使用详情, 请Goolge、Baidu 之 :)
	 */
	private final static ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<String>();

	/**
	 * 待发送数据暂存池
	 */
	private final static HashMap<String, MsgRequest> REQU_MSG_MAP = new HashMap<String, MsgRequest>();

	/**
	 * 
	 * @param id
	 * @param data
	 *            要发送的数据
	 * @param resultTye
	 *            请求返回结果的数据类型
	 * 
	 *            将请求数据对象加入到待发送数据暂存池 1）服务调用服务方法后，会把待发送的数据放入 REQU_MSG_MAP 中；
	 *            服务方法返回 RequestChannel 对象 2）用返回的RequestChannel 对象调用
	 *            identify(String identify， RequestListener channelHandler) 方法，
	 *            用 identify参数 将 channelHandler绑定，标识请求结果处理 3）在 identify(String ，
	 *            RequestListener) 中发送数据到后端服务器
	 */
	public void setTempDataPool(String id, MsgRequest data, Type resultType) {
		MNioClientHandler.returnTypes.put(id, resultType);
		REQU_MSG_MAP.put(id, data);
		THREAD_LOCAL.set(id);
	}

	/**
	 * @param identify
	 *            标识当前服务调用
	 * @param channelHandler
	 *            负责当前请求结果的处理
	 */
	public void identify(String identify, RequestListener channelHandler) {
		if (identify != null) {
			// 1) 从数据暂存池中拿出与当前请求对应的地数据，数据可以包含两类: a.发送到后端的数据 b.请求返回的数据类型
			String tempDataId = THREAD_LOCAL.get();

			MsgRequest data = REQU_MSG_MAP.get(tempDataId); // 数据
			Type resultType = MNioClientHandler.returnTypes.get(tempDataId); // 返回类型

			REQU_MSG_MAP.remove(tempDataId);
			MNioClientHandler.returnTypes.remove(tempDataId);

			// 2) 将channelHandler 放入结果处理池
			REQUE_HASH_MAP.put(identify, channelHandler);
			MNioClientHandler.returnTypes.put(identify, resultType);

			// 3) 用 identify参数 将 channelHandler绑定，标识请求结果处理，并发送数据到后端服务器
			data.setReqCode(identify);
			Log.e("MNioClient", "服务请求参数：" + data.toString());
			try {
				if (K.SyncRequest) {
					MNioClient.getInstance().sendAsynMessage(data);
				} else {
					Object responseMsg = MNioClient.getInstance()
							.sendSyncMessage(data);
					MNioClientHandler.handleMessage(responseMsg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("identify 为空 !!!!!!!!!!!!!!!!!!!!!");
		}

	}

	/**
	 * 
	 * @return RequestListener 获得负责当前请求结果的处理
	 */
	public static RequestListener getChannelHandlerByIdentify(String identifiy) {

		return REQUE_HASH_MAP.get(identifiy);
	}

	/**
	 * 
	 * @return String 产生一个请求ID
	 */
	public static String getChannelUniqueID() {
		return java.util.UUID.randomUUID().toString().replaceAll("-", "");
	}

	public void clean() {
		THREAD_LOCAL.remove();
	}

	/**
	 * 取得存放在当前线程的请求ID
	 * 
	 * @return
	 */
	public static String getCurrentThreadRequestId() {
		return THREAD_LOCAL.get();
	}

	public static void removeRequestListener(String... reqIds) {
		if (reqIds != null) {
			Log.e("removeRequestListener", reqIds + "");
			for (String string : reqIds) {
				if (REQUE_HASH_MAP != null) {
					Log.e("RequestChannel","removeRequestListener ed"+ string + ":"
							+ REQUE_HASH_MAP.get(string));
					REQUE_HASH_MAP.remove(string);
				}
			}
		}
	}

	/**
	 * 访问服务异常
	 */
	public static void handleResultOnException() {
		for (String key : REQUE_HASH_MAP.keySet()) {
			RequestListener requestListener = REQUE_HASH_MAP.get(key);
			ZcdhException exception = new ZcdhException();
			exception.setErrCode(RequestException.EXC_CODE_SESSION + "");
			requestListener.onRequestError(key, exception);
		}
	}

}
