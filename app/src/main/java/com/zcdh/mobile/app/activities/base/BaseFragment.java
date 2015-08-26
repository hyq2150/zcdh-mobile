/**
 * 
 * @author jeason, 2014-5-14 下午3:24:51
 */
package com.zcdh.mobile.app.activities.base;

import com.umeng.analytics.MobclickAgent;
import com.zcdh.mobile.app.ZcdhApplication;
import android.support.v4.app.Fragment;

/**
 * @author jeason, 2014-5-14 下午3:24:51
 * Fragment 基类 所有Fragment均继承此类
 */
public class BaseFragment extends Fragment {
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(getActivity());
//		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(getActivity());
	}

	public long getUserId() {
		return ZcdhApplication.getInstance().getZcdh_uid();
	}

}
