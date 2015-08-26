package com.zcdh.mobile.app.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.InformationCoverDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.newsmodel.NewsBrowserActivity_;
import com.zcdh.mobile.utils.ListUtils;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.List;
import java.util.Map.Entry;

/**
 * A {@link PagerAdapter} which behaves like an {@link android.widget.Adapter}
 * with view types and view recycling.
 */
public class RecyclingPagerAdapter extends PagerAdapter {
	static final int IGNORE_ITEM_VIEW_TYPE = AdapterView.ITEM_VIEW_TYPE_IGNORE;

	private final RecycleBin recycleBin;
	private Context context;
	private List<InformationCoverDTO> covers;
	private InformationCoverDTO mDto;
	private DisplayImageOptions options;
	private int size;
	private boolean isInfiniteLoop;

	public RecyclingPagerAdapter(Context context,
			List<InformationCoverDTO> covers) {
		this(new RecycleBin());
		this.context = context;
		this.covers = covers;
		this.size = ListUtils.getSize(covers);
		isInfiniteLoop = false;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.considerExifParams(true).build();
	}

	RecyclingPagerAdapter(RecycleBin recycleBin) {
		this.recycleBin = recycleBin;
		recycleBin.setViewTypeCount(getViewTypeCount());
	}

	@Override
	public void notifyDataSetChanged() {
		recycleBin.scrapActiveViews();
		super.notifyDataSetChanged();
	}

	@Override
	public final Object instantiateItem(ViewGroup container, final int position) {
		int viewType = getItemViewType(position);
		View view = null;
		if (viewType != IGNORE_ITEM_VIEW_TYPE) {
			view = recycleBin.getScrapView(position, viewType);
		}
		view = getView(position, view, container);
		container.addView(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDto = covers.get(getPosition(position));
				if (mDto != null) {
					if (mDto.getOpenType() == 1) {
						NewsBrowserActivity_.intent(context)
								.url(mDto.getAnroidURL())
								.title(mDto.getTitle())
								.InformationTitleInfoId(mDto.getCoverId())
								//.properties(mDto.getProperties())
								.signType(Constants.SINGN_TYPE_ADS)
								.fromWhere(Constants.FROM_AD).start();
					} else {
						Class<?> activity_cls = SystemServicesUtils
								.getClass(mDto.getAnroidURL());
						if (activity_cls != null) {
							Intent intent = new Intent(context, activity_cls);
							Bundle data = new Bundle();
							for (Entry<String, String> param : StringUtils
									.getParams(mDto.getCustomParam())
									.entrySet()) {
								data.putString(param.getKey(), param.getValue());
							}
							data.putString("title", mDto.getTitle());
							data.putString(Constants.FROM_WHERE,
									Constants.FROM_AD);
							intent.putExtras(data);
							context.startActivity(intent);
						}
					}
				}

			}
		});
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(covers);
	}

	@Override
	public final void destroyItem(ViewGroup container, int position,
			Object object) {
		View view = (View) object;
		container.removeView(view);
		int viewType = getItemViewType(position);
		if (viewType != IGNORE_ITEM_VIEW_TYPE) {
			recycleBin.addScrapView(view, position, viewType);
		}
	}

	@Override
	public final boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public int getViewTypeCount() {
		return 1;
	}

	// Argument potentially used by subclasses.
	public int getItemViewType(int position) {
		return 0;
	}

	public View getView(int position, View view, ViewGroup container) {

		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.framework_introduce_fragment, container, false);
			holder.imageView = (ImageView) view.findViewById(R.id.image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String res = covers.get(getPosition(getPosition(position))).getCover()
				.getOriginal();
		if (res != null) {
			ImageLoader.getInstance().displayImage(res, holder.imageView,
					options);
		}
		return view;

	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		// return isInfiniteLoop ? position % size : position;
		if (size > 0) {
			return isInfiniteLoop ? position % size : position;
		} else {
			return position;
		}
	}

	public RecyclingPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	public void setCovers(List<InformationCoverDTO> covers) {
		this.size = ListUtils.getSize(covers);
		this.covers.clear();
		this.covers = covers;
	}
}