/**
 * 
 * @author jeason, 2014-6-5 下午12:05:03
 */
package com.zcdh.mobile.app.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.EntProductDTO;
import com.zcdh.mobile.utils.DateUtils;

/**
 * @author jeason, 2014-6-5 下午12:05:03 产品项预览View
 */
public class ProductPreviewItem extends LinearLayout {

	private ImageView productImg;
	private TextView tv_title;
	private TextView tv_date;
	private TextView content;
	private DisplayImageOptions options;

	public ProductPreviewItem(Context context) {
		this(context, null);
	}

	public ProductPreviewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(getContext(), R.layout.product_preview_item, this);
		options = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
		productImg = (ImageView) findViewById(R.id.productImg);
		tv_title = (TextView) findViewById(R.id.title);
		tv_date = (TextView) findViewById(R.id.publishTime);
		content = (TextView) findViewById(R.id.content);
	}

	public void initView(EntProductDTO entProductDTO) {
		if (entProductDTO.getTitleImg() != null)
			ImageLoader.getInstance().displayImage(
					entProductDTO.getTitleImg().getBig(), productImg, options);
		tv_title.setText(entProductDTO.getTitle());
		tv_date.setText(DateUtils.getDateByFormatYMD(entProductDTO
				.getPublishDate()));
		content.setText(entProductDTO.getDesc());
	}

}
