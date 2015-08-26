package com.zcdh.mobile.framework.events;

import java.util.ArrayList;

/**
 * 消息订阅与发布
 * @author yangjiannan
 *
 */
public class MyEvents {
	
	public static final String kMSG_EXIT = "exit";// 退出
	public static final String kMSG_AUTH_STATUS = "auth_status"; //登录状态
	
	private static final ArrayList<Subscriber> subs = new  ArrayList<Subscriber>();

	public static void post(String key, Object msg){
		for (Subscriber subscriber : subs) {
			if(subscriber!=null){
				subscriber.receive(key, msg);
			}
		}
	}

	public static void register(Subscriber subscriber){
		if(subscriber!=null){
			subs.add(subscriber);
		}
	}
	
	public static void unregister(Subscriber subscriber){
		if(subs.contains(subscriber)){
			subs.remove(subscriber);
		}
	}
	
	
	public interface Subscriber{
		public void receive(String key, Object msg);
	}
}
