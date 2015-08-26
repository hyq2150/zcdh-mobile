package com.zcdh.mobile.framework.nio;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zcdh.core.annotation.RpcMethod;
import com.zcdh.core.annotation.RpcService;
import com.zcdh.core.nio.codec.MsgCodecFactory;
import com.zcdh.core.nio.codec.MsgRequestEncoder;
import com.zcdh.core.nio.codec.MsgResponseDecoder;
import com.zcdh.core.nio.common.MsgRequest;
import com.zcdh.core.nio.common.MsgResponse;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.utils.GenericTypeUtils;
import com.zcdh.mobile.utils.JsonUtil;
import com.zcdh.mobile.utils.NetworkUtils;

/**
 * NIO异步通信客户端
 * 
 * @author danny, 2013-10-30 下午3:34:56
 */
public class MNioClient {

	protected static final String TAG = MNioClient.class.getSimpleName();

	// 服务器地址
	private String HOST;

	// 服务器端口
	private int PORT;

	// 服务器连接
	IoConnector connector;

	// 回话
	IoSession session = null;

	// 业务逻辑句柄
	MNioClientHandler clientHandler = null;

	private static MNioClient instance;

	private MNioClient() {

	}

	synchronized public static MNioClient getInstance() {
		if (instance == null) {
			synchronized (MNioClient.class) {
				System.setProperty("java.net.preferIPv6Addresses", "false"); // 解决2.2的socket连接抛出“bad
				// family”异常
				instance = new MNioClient();
			}
		}
		return instance;
	}

	/**
	 * 连接远程服务器
	 * 
	 * @return
	 * @author danny, 2013-10-30 下午5:08:55
	 */
	public boolean connect() {

		HOST = K.Hosts.SERVER_ADDRESS_DEFAULT; // ZcdhApplication.getInstance().getServerAdress();
		PORT = K.Hosts.SERVER_PORT_DEFAULT; // ZcdhApplication.getInstance().getServerPort();
		Log.e("MNioClient", "开始连接服务器" + HOST + ":" + PORT);
		try {
			if ((this.session != null) && (this.session.isConnected())) {
				Log.e("tag:MNioClient", "Already connected. Disconnect first.");
			}
			// 创建一个非阻塞的客户端程序
			connector = new NioSocketConnector();
			// 设置链接超时时间
			connector.setConnectTimeoutMillis(30000L);
			// connector.setConnectTimeout(30000);
			// 添加过滤器
			if (connector.getFilterChain().contains("codec")) {
				connector.getFilterChain().remove("codec");
			}
			connector.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new MsgCodecFactory(
							new MsgResponseDecoder(Charset.forName("utf-8")),
							new MsgRequestEncoder(Charset.forName("utf-8")))));
			// 添加业务逻辑处理器类
			clientHandler = new MNioClientHandler();
			connector.setHandler(clientHandler);
			session = null;
			ConnectFuture future = connector.connect(new InetSocketAddress(
					HOST, PORT));// 创建连接
			future.awaitUninterruptibly();// 等待连接创建完成

			if (!future.isConnected()) {
				Log.e("isConnected and returned", "yes");
				return false;
			}

			session = future.getSession();// 获得session
			session.getConfig().setUseReadOperation(true);
			Log.e("MNioClient", "客户端连接成功：" + HOST + ":" + PORT);
			return true;
		} catch (Exception e) {
			Log.e("客户端链接异常...", e.getMessage() + "");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 重新连接服务器
	 * 
	 * @return
	 * @author danny,
	 */
	public boolean restartConnect() {
		disConnect();
		return connect();
	}

	/**
	 * 断开服务器连接
	 * 
	 * @author danny, 2013-10-30 下午5:18:43
	 */
	public void disConnect() {
		Log.e(TAG, "断开服务器连接");
		if (this.session != null) {
			if (this.session.isConnected()) {
				MsgRequest req = new MsgRequest(); // 发送请求
				req.setService(K.System.SYSTEM_SERVICE);
				req.setMethod(K.System.SYSTEM_SERVICE_COMMAND_CLOESE);
				this.session.write(req);
				this.session.getCloseFuture().awaitUninterruptibly();

			}
			this.session.close(true);
		}

		if (connector != null) {
			connector.dispose();
		}
	}

	/**
	 * 检查是否连接
	 * 
	 * @return
	 * @author danny, 2013-10-30 下午7:54:19
	 */
	public Boolean isConnect() {
		if (connector == null)
			return false;
		if (!connector.isActive())
			return false;
		if (session == null)
			return false;
		if (!session.isConnected())
			return false;
		if (session.isReadSuspended()) {
			Log.e(TAG, "isReadSuspended");
			return false;
		}
		return true;
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
	public <T> T sendSyncMessage(MsgRequest req, TypeReference<T> clazz)
			throws ZcdhException {

		MsgResponse res = sendSyncMessage(req);

		if (res == null)
			throw new ZcdhException("服务器返回数据为空！");
		if (res.getFlag() == null)
			throw new ZcdhException("服务器返回数据标志为空！");
		if (res.getResult() == null)
			throw new ZcdhException("服务器返回数据结果为空！");

		String flag = res.getFlag();
		if (flag.equalsIgnoreCase(K.SUCCESS)) {
			return JsonUtil.toObject(res.getResult(), clazz);
		} else {
			ZcdhException exception = JsonUtil.toObject(res.getResult(),
					ZcdhException.class);
			throw exception;
		}

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
	public <T> T sendSyncMessage(MsgRequest req, Class<T> clazz)
			throws ZcdhException {

		MsgResponse res = sendSyncMessage(req);
		if (res == null)
			throw new ZcdhException("服务器返回数据为空！");
		if (res.getFlag() == null)
			throw new ZcdhException("服务器返回数据标志为空！");
		if (res.getResult() == null)
			throw new ZcdhException("服务器返回数据结果为空！");

		String flag = res.getFlag();
		if (flag.equalsIgnoreCase(K.SUCCESS)) {
			return JsonUtil.toObject(res.getResult(), clazz);
		} else {
			ZcdhException exception = JsonUtil.toObject(res.getResult(),
					ZcdhException.class);
			throw exception;
		}

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
	public synchronized Object sendSyncMessage(MsgRequest req, Type type)
			throws ZcdhException {

		MsgResponse res = sendSyncMessage(req);

		if (res == null)
			throw new ZcdhException(K.Network.kNETWORK_EXCE_EMPTY, "服务器返回数据为空！");
		Log.e("MNioClient", "服务返回结果：" + res.toString());
		if (res.getFlag() == null)
			throw new ZcdhException(K.Network.kNETWORK_EXCE_EMPTY_FLAG,
					"服务器返回数据标志为空！");
		if (res.getResult() == null)
			throw new ZcdhException(K.Network.kNETWORK_EXCE_EMPTY_RESULT,
					"服务器返回数据结果为空！");

		String flag = res.getFlag();

		if (flag.equalsIgnoreCase(K.SUCCESS)) {
			return res.DecoderResult(type);
		} else {
			ZcdhException exception = (ZcdhException) res.DecoderResult(type);
			Log.e("请求服务发生异常", "错误码：" + exception.getErrCode() + "错误信息："
					+ exception.getErrMessage());
			// throw exception;
			return exception;
		}

	}

	/**
	 * 发送同步请求
	 * 
	 * @param req
	 *            ，请求参数
	 * @return 返回请求结果
	 * @author danny, 2013-10-30 下午4:31:01
	 */
	public MsgResponse sendSyncMessage(MsgRequest req) {
		MsgResponse res = null;
		initSession(req);
		// session.getConfig().setUseReadOperation(true);
		try {// 发送
			session.write(req).awaitUninterruptibly();
			// 接收
			ReadFuture readFuture = session.read();
			if (readFuture.awaitUninterruptibly(20 * 1000, TimeUnit.SECONDS)) {
				// session.getConfig().setUseReadOperation(false);
				res = (MsgResponse) readFuture.getMessage();
			} else {// 读超时
				res = new MsgResponse(req, K.FAILURE, "连接服务器超时！");

				// ssession.getConfig().setUseReadOperation(false);
			}

		} catch (Exception e) {
			res = new MsgResponse(req, K.FAILURE, e.getMessage());
			// session.getConfig().setUseReadOperation(true);
			e.printStackTrace();
			return res;
		}
		return res;
	}

	/**
	 * 初始化会话连接
	 * 
	 * @param req
	 * @return
	 */
	private boolean initSession(MsgRequest req) {
		boolean success = true;
		int EXC_CODE = -1;
		String excMsg = null;

		if (!NetworkUtils.isNetworkAvailable(ZcdhApplication.getInstance())) {
			success = false;
			EXC_CODE = RequestException.EXC_CODE_NETWORK;
			excMsg = ZcdhApplication.getInstance().getString(
					R.string.network_suck);
		} else {

			// 如果服务连接没有连接远程服务器，重新连接一次
			if (!isConnect()) {
				restartConnect();
			}
			// 如果Session为空
			if (!connector.isActive() || session == null) {
				EXC_CODE = RequestException.EXC_CODE_SESSION;
				excMsg = "连接服务器超时";
				success = false;
			}
			if (session != null && !session.isConnected()) {
				EXC_CODE = RequestException.EXC_CODE_SESSION;
				excMsg = "服务器繁忙，请稍候再试试";
				success = false;
			}
		}

		if (!success) {
			RequestException requestException = new RequestException();
			requestException.setErrCode(EXC_CODE + "");
			requestException.setErrMessage(excMsg);
			Log.e("request", excMsg);
			MNioClientHandler.RESPONSE_HANDLER.response(req.getReqCode(),
					requestException);
		}

		return success;
	}

	@SuppressWarnings("unchecked")
	public <T> T getInterfaceProxy(Class<T> iface) {

		// 创建代理
		T proxy = (T) Proxy.newProxyInstance(iface.getClassLoader(),
				new Class<?>[] { iface }, new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {

						// 初始化
						String methodName = method.getName();
						String serviceName = "";
						HashMap<String, Object> params = new HashMap<String, Object>();

						// 检查此接口是否被公布
						RpcMethod rpcMethod = method
								.getAnnotation(RpcMethod.class);
						if (rpcMethod == null || rpcMethod.value() == null
								|| rpcMethod.value().equalsIgnoreCase(""))
							throw new ZcdhException("此接口尚未被公布，无法调用：" + method);
						methodName = rpcMethod.value();

						RpcService rpcService = method.getDeclaringClass()
								.getAnnotation(RpcService.class);
						if (rpcService == null || rpcService.value() == null
								|| rpcService.value().equalsIgnoreCase(""))
							throw new ZcdhException("此服务尚未被公布，无法调用："
									+ serviceName);
						serviceName = rpcService.value();

						// String reqIdPropertyName =
						// "KREQ_ID_"+methodName.toUpperCase();

						// 广播事件，使发起此次后端请求的Activity 接收此次请求ID
						// RequestDispatcher.postRequestID(reqId,
						// reqIdPropertyName);

						MsgRequest req = new MsgRequest(); // 发送请求
						/**
						 * 获取客户端环境 请求的设备的属性 手机型号 DECEIVE_NAME 手机类型
						 * DECEIVE_TYPE：IOS,Android 系统版本号 SYS_VERSION app版本号
						 * APP_VERSION 网络连接方式 NETWORK_STATUS 网络运营商
						 * MOBILE_NETWORK_OPERATORS： CMCC 移动， CTCC 电信，CUCC 联通
						 */
						AppEverimentArgs appEverimentArgs = ZcdhApplication
								.getInstance().getEverimentArgs();

						req.setMeta(appEverimentArgs.getEverimentArgs());

						Log.e(TAG, appEverimentArgs.getEverimentArgs().toString());

						// 设定服务名
						req.setService(serviceName);
						// 设定服务方法名
						req.setMethod(methodName);
						// 设定方法参数名
						Class<?>[] parameterTypes = method.getParameterTypes();
						Annotation[][] parameterAnnotations = method
								.getParameterAnnotations();
						// System.out.println("参赛类型："+parameterTypes.length+","+"注解数组："+parameterAnnotations.length);
						if (parameterTypes.length != args.length) {
							throw new ZcdhException(serviceName + "."
									+ "methodName" + "：请求参数与注解参数要求不一致");
						}

						if (parameterTypes.length > 0) {// 有参数调用方法
							try {
								params = req.EncoderParam(parameterTypes,
										parameterAnnotations, args);
							} catch (ZcdhException e) {
								throw new ZcdhException(serviceName + "."
										+ "methodName" + "："
										+ e.getErrMessage());
							} catch (Exception e) {
								throw new ZcdhException(serviceName + "."
										+ "methodName" + "：构建参数未知错误："
										+ e.getMessage());
							}
						}
						req.setParams(params);

						Type resultType = GenericTypeUtils
								.getGenericTypeRawType((ParameterizedType) method
										.getGenericReturnType());
						RequestChannel<Type> requestChannel = new RequestChannel<Type>();
						// MsgRequest 和 请求返回数据类型都放入数据暂存池
						requestChannel.setTempDataPool(
								RequestChannel.getChannelUniqueID(), req,
								resultType);
						Log.e("request", RequestChannel.getChannelUniqueID());
						return requestChannel;

					}
				});
		return proxy;
	}

	public void sendAsynMessage(MsgRequest req) {
		if (initSession(req) && session != null) {
			session.write(req);
			Log.e(TAG, "发送数据完成");
		} else {
			Log.e(TAG, "session为null");
		}
	}

	public void sendAsynMessage(MsgRequest req, AbstractMsgHandle handle) {
		session.write(req);
	}

	public void sendAsynMessage(MsgRequest req, MsgResponse res) {
		session.write(req);
	}

	public String getHOST() {
		return HOST;
	}

	public void setHOST(String hOST) {
		HOST = hOST;
	}

	public int getPORT() {
		return PORT;
	}

	public void setPORT(int pORT) {
		PORT = pORT;
	}
}
