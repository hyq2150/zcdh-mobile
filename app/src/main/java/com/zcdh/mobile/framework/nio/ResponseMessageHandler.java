package com.zcdh.mobile.framework.nio;

import java.util.HashMap;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zcdh.core.nio.except.ZcdhException;

public class ResponseMessageHandler extends Handler {
	
	public static final int EXCEPTION = 0;
	public static final int SUCCESS = 1;
	
	public static final String kREQ_ID = "reqId";
	public static final String kREQ_RESPONSE =  "respnose";

	public ResponseMessageHandler(Looper looper) {
		super(looper);
	}
	
	public void response(String reqID, Object result){
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put(ResponseMessageHandler.kREQ_ID, reqID);
		response.put(ResponseMessageHandler.kREQ_RESPONSE, result); 
		Message msg = new Message();
		msg.obj = response;
		Log.i("response", reqID);
		sendMessage(msg);
	}
	

	public void handleMessage(Message msg) {

		if(msg.obj==null)return;
		
		//返回结果，有请求ID 和 返回结果组成
		HashMap<String, Object> response = (HashMap<String, Object>) msg.obj;
		
		String reqID = (String) response.get(kREQ_ID);
		Object result = response.get(kREQ_RESPONSE);
		
		if(reqID!=null){
			RequestListener requestListener = RequestChannel
					.getChannelHandlerByIdentify(reqID);
			if (requestListener != null) {
				
				if(result instanceof Exception){
					Exception exception = null;
					if(result instanceof ZcdhException){
						exception = (ZcdhException) result;
					}else{
						Log.e("服务端异常：", ((Exception) result).getMessage());
						exception = new ZcdhException(((Exception) result).getMessage());
					}
					requestListener.onRequestError(reqID, exception);
					//Toast.makeText(ZcdhApplication.getInstance(), exception.getErrMessage()+"", Toast.LENGTH_SHORT).show();
				}else{
					if (result instanceof ZcdhException) {
						requestListener.onRequestError(reqID,
								(ZcdhException) result);
					} else {
						requestListener.onRequestSuccess(reqID, result);
					}
				}
				
				requestListener.onRequestFinished(reqID);
			}
		}
		
	}

}
