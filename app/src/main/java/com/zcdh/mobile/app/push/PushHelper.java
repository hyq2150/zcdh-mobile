package com.zcdh.mobile.app.push;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.zcdh.mobile.api.IRpcPushService;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginActivity_;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
import com.zcdh.mobile.app.activities.auth.LoginListener;
import com.zcdh.mobile.app.activities.newsmodel.NewsBrowserActivity_;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.JsonUtil;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.api.BackgroundExecutor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * 消息推送设置
 * 
 * @author yangjiannan
 * 
 */
public class PushHelper implements RequestListener ,
	LoginListener {

	/**
	 * 标识是否绑定百度推送的api key
	 */
	public static final String kPUSH_API_KEY_BINDED = "kPUSH_API_KEY_BINDED";

	private static final String TAG = PushHelper.class.getSimpleName();

	private String kREQ_ID_bindBaiduPushServerUserIdAndChannelId;
	
	//TODO
//	private String kREQ_ID_testEntReply; 

	private IRpcPushService pushService;
	
	private Context context;
	
	/**
	 * 测试推送
	 */
//	private IRpcPushMessageService iRpcPushMessageService;

	private String customContentString;

	public static PushHelper getInstance(Context context) {
		//final PushHelper _instance = new PushHelper(context);
		return new PushHelper(context);
	}

	public PushHelper(Context context) {
		this.context = context;
		this.pushService = RemoteServiceManager.getRemoteService(IRpcPushService.class);
//		iRpcPushMessageService = RemoteServiceManager.getRemoteService(IRpcPushMessageService.class);
	}

	/**
	 * 绑定消息推送
	 * 
	 */
	
	public void bindBaiduPushServer() {
		Log.i(TAG, "bindBaiduPushServer");
		PushSettings.enableDebugMode(context, true);
		PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, SystemServicesUtils.getMetaValue(context, "com.baidu.lbsapi.API_KEY"));
	}
	

	/**
	 * 解除绑定
	 */
	public void unBind() {
		// 1) 记录改为未绑定
		SharedPreferencesUtil.putValue(context, kPUSH_API_KEY_BINDED, false);
		// 2) 停止百度消息推送服务
		PushManager.stopWork(context);
	}

	/**
	 * 将职场导航账号与消息推送绑定
	 */
	public void bindZcdhForBaiduPushServer(final String pushUserId, final String pushChannelId) {
		
		//Toast.makeText(context, "bind push", Toast.LENGTH_SHORT).show();

		BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {

			@Override
			public void execute() {
				try {
					Log.i(TAG, "bindZcdhForBaiduPushServer");
					long uid = ZcdhApplication.getInstance().getZcdh_uid();
					PushHelper.this.pushService.bindBaiduPushServerUserIdAndChannelId(uid, pushUserId, pushChannelId).identify(kREQ_ID_bindBaiduPushServerUserIdAndChannelId = RequestChannel.getChannelUniqueID(), PushHelper.this);
				} catch (Throwable e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
				}
			}

		});
	}
	
	
	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_bindBaiduPushServerUserIdAndChannelId)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {// 记录绑定百度消息推送成功
					SharedPreferencesUtil.putValue(context, kPUSH_API_KEY_BINDED, true);
					
					//Toast.makeText(context, "bind server success", Toast.LENGTH_SHORT).show();
				}else{
					Log.e(TAG, "");
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}
	
	@Override
	public void requestLoginFinished(int resultCode, String errorMsg) {
		if (resultCode == Constants.kLOGIN_RESULT_SUCCESS) {
		//	Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
			showNotificationDetails();
		} else {
			LoginActivity_.intent(this.context).start();
		}
	}


	/**
	 * 更具推送消息去到相应地界面
	 * 
	 * @param customContentString
	 */
	public void doDispatchByNotification(String customContentString) {
		Log.i("customContentString", customContentString +"");
		this.customContentString = customContentString;
		// 判断是否登录
		ZcdhApplication app = (ZcdhApplication) context.getApplicationContext();
		if (app != null) {
			if (app.getZcdh_uid() != -1) {
				showNotificationDetails();
			}else{
				LoginHelper.getInstance(context, this).doLogin();
			}
		}
		
	}
	
	/**
	 * 跳入相应的activity 显示通知详细信息
	 */
	public void showNotificationDetails(){
		// {"postCode":"0000600","skipActionCode":"001","industryCode":"001.002","openType":"2","areaCode":"019.004"}
		
		Log.i(TAG, customContentString + "");
		if (!StringUtils.isBlank(customContentString)) {

			HashMap<String, String> pm = JsonUtil.toObject(customContentString, HashMap.class);
			String openType = pm.get("openType");
			String param = pm.get("customParam") + "";
		
			Log.i(TAG, "param:" + customContentString);
			String androidURL = pm.get("android_url");
			
			if ("2".equals(openType)) {
				/*	
				HashMap<String, String> params = null;
				if(!TextUtils.isEmpty(param)){
					 params = StringUtils.getParams(param);
				}
			
				if(param==null)return;
				
				Class activityClazz = null;
				try {
					activityClazz = Class.forName(androidURL);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(activityClazz!=null){
					Intent intent = new Intent(context,
							activityClazz);
					for (Entry<String, String> item : params.entrySet()){
						intent.putExtra(item.getKey(),
								item.getValue());
					}
					//intent.putExtra("index", position);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}*/
				if(!TextUtils.isEmpty(param)){
					String msgId = getValueFromCustomParamByKey(customContentString, "id");
					String androidURL_ =  getValueFromCustomParamByKey(customContentString, "anroidURL");
					Log.i(TAG, "androidURL:" + androidURL_);
					Log.i(TAG, "message id:" +  msgId);
					if(!TextUtils.isEmpty(androidURL_) && !TextUtils.isEmpty(msgId)){
						Class<?> activity_cls = SystemServicesUtils.getClass(androidURL_);
						Intent intent = new Intent(context, activity_cls);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("id", Long.valueOf(msgId));
						context.startActivity(intent);
					}
				}
				
				
			}
			if ("1".equals(openType)) {
				Bundle data = new Bundle();
				NewsBrowserActivity_.IntentBuilder_ ib = NewsBrowserActivity_
					.intent(context);
				ib.flags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ib.get().putExtras(data);
				ib.url(androidURL).start();
			}
		}
		
		
		
	}
	

	/**
	 * CustomContent 字符串， 为传回个手机端的参数格式为，key1 = value2 , key2 = value2
	 * @param key
	 * @return		返回的结果是去除空格之后的对应的值，如果有两个key 是一样的则去第一个可以的值， 如果说没有匹配的返回null
	 * @author focus, 2014-7-7 下午3:04:57
	 */
	public static String getValueFromCustomParamByKey(String customParam,String key){
		
		Log.i(TAG, "customParam:" + customParam);
		// 参数为空，返回直接结果  
		if(TextUtils.isEmpty(customParam) || TextUtils.isEmpty(key)) return null;
		// 假如是不包含，则也返回空 
		if(customParam.contains(key) == false) return null;
		
		key = key.trim();
		customParam = customParam.trim();
		
		// 取得key字符串的下标
		int index = customParam.indexOf(key);
		// 字符串截取		 
		String cutStr = customParam.substring(index);
		String[] datas = cutStr.split(",");
		
		for(String startStr:datas){
			if(startStr.startsWith(key)){
				String[] subDatas = startStr.split("=");
				for(String resultKey:subDatas){
					if(key.equals(resultKey.trim())){
						if(subDatas.length >=2){
							String result= subDatas[1].trim();
							result =  result.replace("\"", "");
							//Log.i(TAG, "key:" + result.contains("\"\""));
							return result;
						}// end if
					}// end if
				}// end for
			}// end if
		}// end for
		
		return null;
	}
}
