package com.zcdh.mobile.app;

import java.util.Map.Entry;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 从外部app访问职场导航app
 * 
 * @author YJN
 *
 */
public class ZcdhAppBrowsableActivity extends BaseActivity {

	private static final String TAG = ZcdhAppBrowsableActivity.class
			.getSimpleName();

	public void onCreate(Bundle stateSaved) {
		super.onCreate(stateSaved);
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		for (RunningAppProcessInfo pid : am.getRunningAppProcesses()) {
			Log.i(TAG, "processName :" + pid.processName);
			am.killBackgroundProcesses(pid.processName);
		}

		String dataString = getIntent().getDataString();
		Log.i(TAG, "dataString :" + dataString);
		if (!TextUtils.isEmpty(dataString)) {
			dataString = dataString.substring(dataString.indexOf("?") + 1);
			Log.i(TAG, "dataString param:" + dataString);
			Bundle data = new Bundle();
			Class<?> clazz = null;
			for (Entry<String, String> param : StringUtils
					.getParams(dataString).entrySet()) {
				if ("url".equals(param.getKey())) {
					clazz = SystemServicesUtils.getClass(param.getValue());
				}
				data.putString(param.getKey(), param.getValue());
				Log.i(TAG, param.getKey());
			}

			if (clazz != null) {
				Intent targetIntent = new Intent(this, clazz);
				targetIntent.putExtras(data);
				startActivity(targetIntent);
				finish();
			}
		}

	}

}
