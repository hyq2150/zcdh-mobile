/**
 * 
 * @author jeason, 2014-6-12 上午10:02:53
 */
package com.zcdh.mobile.app.views;

import java.util.Date;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.InformationDTO;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.TimeUtil;

/**
 * @author jeason, 2014-6-12 上午10:02:53 消息列表项View
 * 
 */
@EViewGroup(R.layout.message_item)
public class MessageItem extends RelativeLayout {

	@ViewById(R.id.msg_icon)
	ImageView msg_icon;

	@ViewById(R.id.redPointImg)
	ImageView redPointImg;

	@ViewById(R.id.tv_title)
	TextView tv_title;

	@ViewById(R.id.tv_publish_time)
	TextView tv_publish_time;

	@ViewById(R.id.tv_desc)
	TextView tv_description;

	private DisplayImageOptions options;

	/**
	 * @author jeason, 2014-6-12 上午10:03:03
	 * @param context
	 */
	public MessageItem(Context context) {
		this(context, null);
	}

	public MessageItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		options = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
	}

	public void initWithInformationDTO(InformationDTO info) {
		String imgUrl = "";
		if (info.getAndoridImg() != null) {
			imgUrl = info.getAndoridImg().getBig();
		}
		Integer isRead = 0;
		if (info.getIsRead() != null) {
			isRead = info.getIsRead();
		}
		init(imgUrl, info.getTitle(), info.getDesc(), info.getPushTime(),
				isRead == 1);
	}

	public void init(String img_url, String title_str, String desc_str,
			Date date, boolean isRead) {
		ImageLoader.getInstance().displayImage(img_url, msg_icon, options);
		tv_title.setText(title_str);
		if (!StringUtils.isBlank(desc_str)) {
			tv_description.setVisibility(View.VISIBLE);
			tv_description.setText(desc_str);
		} else {
			tv_description.setVisibility(View.GONE);
		}
		if (date != null) {
			tv_publish_time.setText(TimeUtil.converTime(date.getTime()));
		} else {
			tv_publish_time.setVisibility(View.GONE);
		}

		if (!isRead) {
			redPointImg.setVisibility(View.VISIBLE);
		} else {
			redPointImg.setVisibility(View.GONE);
		}
	}

}
