/**
 * 
 * @author jeason, 2014-7-22 下午2:52:49
 */
package com.zcdh.mobile.app.views;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobUserPortraitDTO;

/**
 * @author jeason, 2014-7-22 下午2:52:49 用户头像View 根据用户性别，是否匿名等条件判断头像显示的一个View
 */
@EViewGroup(R.layout.zcdh_head)
public class HeadByGender extends LinearLayout {

	@ViewById(R.id.tvName)
	TextView tvName;

	@ViewById(R.id.head)
	ImageView head;

	@ViewById(R.id.ll_nohead)
	LinearLayout ll_nohead;
	private DisplayImageOptions options;

	/**
	 * @author jeason, 2014-7-22 下午2:53:23
	 * @param context
	 */
	public HeadByGender(Context context) {
		this(context, null);
	}

	public HeadByGender(Context context, AttributeSet attrs) {
		super(context, attrs);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.considerExifParams(true).build();
	}

	public static int FEMALE = 0;
	public static int MALE = 1;
	public static int ANONYMOUS = 2;

	public void initHeadWithGender(String userName, int gender) {
		ll_nohead.setVisibility(View.VISIBLE);
		head.setVisibility(GONE);
		if (userName.length() >= 2) {
			tvName.setText(userName.substring(userName.length() - 2));
		} else {
			tvName.setText(userName);
		}
		if (gender == 0) {
			ll_nohead.setBackgroundColor(getResources().getColor(
					R.color.head_female));
		} else if (gender == 1) {
			ll_nohead.setBackgroundColor(getResources().getColor(
					R.color.head_male));
		} else {
			ll_nohead.setBackgroundColor(getResources().getColor(
					R.color.head_anonymous));
		}
	}

	public void initHeadWithUserPortrait(JobUserPortraitDTO portrait) {
		// 0 用户没有头像，也没有名字显示系统默认头像，
		// 1 头像用名字组装 并且 性别为男，
		// 2 头像用名字组装，并且性别为女，
		// 3 用户有头像

		switch (portrait.getStatus()) {
		case 0:
			ll_nohead.setVisibility(View.VISIBLE);
			head.setVisibility(GONE);
			break;
		case 1:
			ll_nohead.setVisibility(View.VISIBLE);
			initHeadWithGender(portrait.getUserName(), 1);
			break;
		case 2:
			ll_nohead.setVisibility(View.VISIBLE);
			initHeadWithGender(portrait.getUserName(), 0);
			break;
		case 3:
			head.setVisibility(VISIBLE);
			ll_nohead.setVisibility(GONE);
			ImageLoader.getInstance().displayImage(
					portrait.getPortrait().getMedium(), head, options);
			break;
		default:
			break;
		}
	}

	public void displayImg(String url) {
		ll_nohead.setVisibility(GONE);
		head.setVisibility(View.VISIBLE);
		ImageLoader.getInstance().displayImage(url, head, options);
	}
}
