package com.zcdh.mobile.app.activities.vacation;

import com.zcdh.mobile.framework.nio.MNioClient;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The purpose of this Activity is to manage the activities in a tab. Note:
 * Child Activities can handle Key Presses before they are seen here.
 * 
 * @author Eric Harlow
 */
public class TabGroupActivity extends ActivityGroup {

	private ArrayList<String> childActivityList;

	private int backKeyEventCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (childActivityList == null) childActivityList = new ArrayList<String>();

	}

	@Override
	protected void onResume() {
		super.onResume();
		backKeyEventCount = 0;
	}

	/**
	 * This is called when a child activity of this one calls its finish method.
	 * This implementation calls {@link LocalActivityManager#destroyActivity} on
	 * the child activity and starts the previous activity. If the last child
	 * activity just called finish(),this activity (the parent), calls finish to
	 * finish the entire group.
	 */
	@Override
	public void finishFromChild(Activity childs) {
		LocalActivityManager manager = getLocalActivityManager();
		int index = childActivityList.size() - 1;

		if (index < 1) {
			finish();
			return;
		}

		manager.destroyActivity(childActivityList.get(index), true);
		childActivityList.remove(index);
		index--;
		String lastId = childActivityList.get(index);
		// Toast.makeText(getApplicationContext(), lastId,
		// Toast.LENGTH_SHORT).show();
		Intent lastIntent = manager.getActivity(lastId).getIntent();
		Window newWindow = manager.startActivity(lastId, lastIntent);
		setContentView(newWindow.getDecorView());
	}

	/**
	 * Starts an Activity as a child Activity to this.
	 * 
	 * @param Id Unique identifier of the activity to be started.
	 * @param intent The Intent describing the activity to be started.
	 */
	public void startChildActivity(String Id, Intent intent) {
		Window window = getLocalActivityManager().startActivity(Id, intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		if (window != null) {
			childActivityList.add(Id);
			setContentView(window.getDecorView());
		}
	}

	/**
	 * The primary purpose is to prevent systems before
	 * android.os.Build.VERSION_CODES.ECLAIR from calling their default
	 * KeyEvent.KEYCODE_BACK during onKeyDown.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// preventing default implementation previous to
			// android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Overrides the default implementation for KeyEvent.KEYCODE_BACK so that
	 * all systems call onBackPressed().
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * If a Child Activity handles KeyEvent.KEYCODE_BACK. Simply override and
	 * add this method.
	 * 
	 * 只适用于2.0及以上版本
	 */
	@Override
	public void onBackPressed() {
		int length = childActivityList.size();
		if (length > 1) {
			Activity current = getLocalActivityManager().getActivity(childActivityList.get(length - 1));
			current.finish();
			System.exit(0);
		} else {
			/*
			 * //finish();
			 */
			backKeyEventCount++;
			if (backKeyEventCount == 2) {
				finish();
				// 释放服务连接资源
				MNioClient client = MNioClient.getInstance();
				if (client != null) {
					client.disConnect();
					client = null;

				}

			} else {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			}
		}
	}

}