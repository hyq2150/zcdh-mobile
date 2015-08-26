package com.zcdh.mobile.framework.nio;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.core.utils.StringUtils;


/**
 * 持有每个请求的唯一标识码、以及相应的处理返回结果监听器 
 * 
 * @author yangjiannan
 * 
 */
public class RequestDispatcher {
	
	//用于在异步请求返回结果是调用相应的发起者
	private static final HashMap<String, RequestListener> requestListners = new HashMap<String, RequestListener>();
	
	//用于订阅接收每个请求产生的请求ID
	private static final ArrayList<RequestListener> subscribers = new ArrayList<RequestListener>();

	/**
	 * 取得用于处理当前请求的监听器
	 * @return
	 */
	public static RequestListener getRequestListener(String reqId){
		return requestListners.get(reqId);
	}

	
	public static void postRequestID(String reqId, String reqIdPropertyName){
		
		runBiz(2, reqId, reqIdPropertyName, null);
	}
	
	
	public static void setRequestListner(RequestListener requestListener){
		runBiz(1, null, null, requestListener);
	}

	/**
	 * 销毁订阅
	 * @param requestListener
	 */
	public static void unsubscribe(RequestListener requestListener) {
		runBiz(0, null, null, requestListener);
		
	}
	
	/**
	 * 线程安全同步
	 * @param type
	 * @param reqId
	 * @param propertyName
	 * @param listener
	 */
	private static synchronized void runBiz(int type, String reqId, String propertyName, RequestListener listener){
		if(type==0){
			System.out.println("remoing.....");
			if(subscribers.contains(listener))subscribers.remove(listener);
			System.out.println("当前拥有远程调用功能的数量："+subscribers.size());
			
		}
		if(type==1){
			System.out.println("adding.....");
			if(listener!=null && (listener instanceof RequestListener)){
				if(!subscribers.contains(listener)){
					subscribers.add(listener);
				}
			}else{
				throw new ZcdhException("不是RequestListener 类型");
			}
		}
		if(type==2){
			for (RequestListener requestListener : subscribers) {
				if(requestListener!=null){
					Field[] fs = null;
					Field[] fs_parent = requestListener.getClass().getSuperclass().getDeclaredFields();
					Field[] fs_self = requestListener.getClass().getDeclaredFields();
					int len = 0;
					if(fs_parent!=null){
						len += fs_parent.length;
					}
					if(fs_self!=null){
						len += fs_self.length;
					}
					fs = new Field[len];
					for (int i = 0; i < fs_parent.length; i++) {
						fs[i] = fs_parent[i];
					}
					for (int i = 0; i < fs_self.length; i++) {
						fs[fs_parent.length + i] = fs_self[i];
					}
					
					//if(requestListener instanceof SearchActivity){
						
					//	System.out.println(".....");
					//}
				
					int i = 0;
					for (Field field : fs) {
						
						String name = field.getName().toUpperCase();
						if(i==31){
							
							System.out.println("......");
						}
						i++;
						if(!StringUtils.isBlank(name) 
								&& name.equals(propertyName)){
							field.setAccessible(true);
							try {
								field.set(requestListener, reqId);
								requestListners.put(reqId, requestListener);
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							break;
							
						}
					}
				}
			}
		}
	}
}
