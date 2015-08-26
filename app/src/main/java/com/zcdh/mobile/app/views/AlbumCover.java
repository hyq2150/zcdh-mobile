package com.zcdh.mobile.app.views;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;

/**
 * 相册封面
 * 
 * @author jeason, 2014-7-25 下午2:54:47
 */
@EViewGroup(R.layout.album_cover)
public class AlbumCover extends RelativeLayout {

	@ViewById(R.id.tv_name)
	TextView tv_name;

	@ViewById(R.id.iv_cover)
	ImageView iv_cover;
	private DisplayImageOptions options;

	public AlbumCover(Context context) {
		this(context, null);
	}

	public AlbumCover(Context context, AttributeSet attrs) {
		super(context, attrs);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.considerExifParams(true).build();
	}

	public void switchSize() {
		Resources r = Resources.getSystem();
		float screen_width = r.getDisplayMetrics().widthPixels
				- (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 15,
						r.getDisplayMetrics()) * 4);
		float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				screen_width / 3, r.getDisplayMetrics());
		float height = width * 3 / 5;
		Log.i("ALB1", screen_width + "");
		Log.i("ALB2", width + "");
		iv_cover.setLayoutParams(new LayoutParams((int) width, (int) height));
	}

	public void initData(String uri) {
		ImageLoader.getInstance().displayImage(uri, iv_cover, options);
		tv_name.setVisibility(View.GONE);
	}

	@SuppressWarnings("unused")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// For simple implementation, or internal size is always 0.
		// We depend on the container to specify the layout size of
		// our view. We can't really know what it is since we will be
		// adding and removing different arbitrary views and do not
		// want the layout to change as this happens.
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();
		// 高度和宽度一样
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
				childWidthSize, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
