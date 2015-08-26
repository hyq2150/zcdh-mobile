package com.zcdh.mobile.framework.nio;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import android.os.Message;
import android.util.Log;

import com.zcdh.core.nio.common.MsgResponse;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.app.ZcdhApplication;

public class MNioClientHandler extends IoHandlerAdapter {

	

	// private HashMap<String,AbstractMsgHandle> messgeHandles;

	public static final HashMap<String, Type> returnTypes = new HashMap<String, Type>();
	
	public static final ResponseMessageHandler RESPONSE_HANDLER = new ResponseMessageHandler(ZcdhApplication.getInstance().getMainLooper());
	

	public MNioClientHandler() {
		super();
		// this.messgeHandles = new HashMap<String,AbstractMsgHandle>() ;
		// returnTypes = new HashMap<String, Type>();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		handleMessage(message);

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		Log.i("MNioClientHandler", "关闭服务器连接");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		super.sessionIdle(session, status);
		Log.i("MNioClientHandler", "连接空闲");
		// 如果IoSession闲置，则关闭连接
		if (status == IdleStatus.BOTH_IDLE) {
			// session.write("heartbeat");
			session.close(true);
		}

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		Log.i("MNioClientHandler", "打开Session连接");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		Log.e("客户端发生异常...", cause.getMessage() + "");
		session.close(true);
	}

	// public void putMessgeHandles(String key, AbstractMsgHandle value){
	// this.messgeHandles.put(key, value);
	// }
	//
	// public AbstractMsgHandle getMessgeHandles(String key){
	// return messgeHandles.get(key);
	// }

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		// 设置IoSession闲置时间，参数单位是秒
		Log.w("MNioClientHandler", "创建Session连接");
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 2 * 60);
	}

	
	
	public static void  handleMessage(Object message){
		Log.i("MNioClientHandler", "客户端开始接受消息");
		if (!(message instanceof MsgResponse)) {
			Log.i("MNioClientHandler", "未知类型！");
			return;
		}
		
		MsgResponse res = (MsgResponse) message;
		
		String reqID = res.getRequestId();
		Object resultObj = null;
		Log.i("接收的数据：","method 接收：" + res.getMethod());
		Log.i("接收的数据", res.getJosnString());
		if (reqID != null && !"".equals(reqID)) {

			Message msg = new Message();
			try {
				resultObj = res.DecoderResult(returnTypes.get(reqID));
				
				msg.obj = resultObj;
			} catch (ZcdhException e) {
				resultObj = e;
				e.printStackTrace();
			} catch (Exception e) {
				resultObj = e;
				resultObj = e;
				e.printStackTrace();
			} finally {

				RESPONSE_HANDLER.response(reqID, resultObj);

				returnTypes.remove(reqID);
			}

//			Log.i("MNioClientHandler", "客户端接收到的消息为：");
//			Log.i("MNioClientHandler", "service：" + res.getService()
//					+ ",method:" + res.getMethod());
//			Log.i("MNioClientHandler", "flag：" + res.getFlag() + ",result:"
//					+ res.getResult());

		}
	}
}
