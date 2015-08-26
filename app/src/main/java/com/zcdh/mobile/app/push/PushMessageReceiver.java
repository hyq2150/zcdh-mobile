package com.zcdh.mobile.app.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.framework.events.MyEvents;

public class PushMessageReceiver extends BroadcastReceiver {

	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();

	/**
	 * @param context
	 *            Context
	 * @param intent
	 * 
	 *            接收的 intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.i(TAG, "MyPushMessage onReceive..... ");
		MyEvents.post(Constants.kEVENT_NOTIFICATION_MESSAGE, 1);
	}
}