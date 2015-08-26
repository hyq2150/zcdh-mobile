package com.zcdh.mobile.framework.activities;

import com.umeng.analytics.MobclickAgent;
import com.zcdh.mobile.app.ZcdhApplication;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Activity基类 所有非含Fragment的Activity均继承此类
 * @author jeason, 2014-7-25 下午3:08:14
 */
public class BaseActivity extends AppCompatActivity {

	protected void onResume() {
		super.onResume();
		// 统计时长
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		// 保证
		// onPageEnd
		// 在onPause
		// 之前调用,因为
		// onPause
		// 中会保存信息
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);

	}

	public long getUserId() {
		return ZcdhApplication.getInstance().getZcdh_uid();
	}

	

}
