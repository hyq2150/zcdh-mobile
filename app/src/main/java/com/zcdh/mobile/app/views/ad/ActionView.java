/**
 * 
 */
package com.zcdh.mobile.app.views.ad;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zcdh.core.utils.StringUtils;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobAppService;
import com.zcdh.mobile.api.model.InformationCoverDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.newsmodel.NewsBrowserActivity_;
import com.zcdh.mobile.app.views.ad.MultiDirectionSlidingDrawer.OnDrawerCloseListener;
import com.zcdh.mobile.app.views.ad.MultiDirectionSlidingDrawer.OnDrawerOpenListener;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 广告活动View模块 chensheng 2015年4月22日下午7:08:25
 */
public class ActionView extends LinearLayout implements OnClickListener,
		OnDrawerOpenListener, OnDrawerCloseListener, RequestListener {
	private Context context;
	/**
	 * 根布局
	 */
	private LinearLayout root;
	/**
	 * 广告图宣传图片
	 */
	private ImageView actionImageView;
	/**
	 * 最新活动点击layout
	 */
	private FrameLayout newActionLayout;

	private MultiDirectionSlidingDrawer slidingDrawer;

	private final String TAG = "ActionView";
	/**
	 * 图片是否加载成功
	 */
	private boolean isLoaderSuccess = false;

	private boolean isShow = false;

	private Handler mHandler = new MyHandler(this);

	private InformationCoverDTO coverData;
	/**
	 * 统计广告位点击
	 */
	private IRpcJobAppService actionLogService;
	/**
	 * 广告统计参数
	 */
	private HashMap<String, String> logMap;

	private String clickIdentify;

	private String autoIdentify;
	private boolean isAutoShow = true;
	private DisplayImageOptions options; // 配置图片加载及显示选项

	/**
	 * @param context
	 */
	public ActionView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ActionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private static class MyHandler extends Handler {
		private final WeakReference<ActionView> actionView;

		public MyHandler(ActionView av) {
			super();
			// TODO Auto-generated constructor stub
			actionView = new WeakReference<ActionView>(av);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			final ActionView av = actionView.get();
			if (msg.what == 1000) {
				Bitmap bm = (Bitmap) msg.obj;
				av.setBitmap(bm);
			} else if (msg.what == 1001) {
				av.closeSlider();
			} else if (msg.what == 1002) {
				av.slidingDrawer.openSlider();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Message msg1 = av.mHandler.obtainMessage(1001);
						av.mHandler.sendMessage(msg1);
					}
				}, 4000);
			}
		}

	}

	/**
	 * 初始化View
	 */
	private void initView(Context context) {
		this.context = context;
		root = (LinearLayout) View.inflate(context,
				R.layout.action_view_layout, this);
		// root.setOnClickListener(this);
		actionImageView = (ImageView) root.findViewById(R.id.action_pic);
		actionImageView.setOnClickListener(this);
		// newActionImageView = (ImageView)
		// root.findViewById(R.id.action_button_pic);
		newActionLayout = (FrameLayout) root.findViewById(R.id.action_button);
		newActionLayout.setVisibility(View.GONE);
		slidingDrawer = (MultiDirectionSlidingDrawer) root
				.findViewById(R.id.drawer);
		slidingDrawer.setOnDrawerCloseListener(this);
		slidingDrawer.setOnDrawerOpenListener(this);
		root.setVisibility(View.INVISIBLE);
		// queue = Volley.newRequestQueue(context);
		// loader = new ImageLoader(queue, new BitmapLruCache());
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true).build();
		actionLogService = RemoteServiceManager
				.getRemoteService(IRpcJobAppService.class);
		logMap = new HashMap<String, String>();
		logMap.put("DECEIVE_TYPE", "Android");
	}

	private void setBitmap(Bitmap bm) {
		if (bm == null) {
			Log.e(TAG, "bm is null !!!");
			isLoaderSuccess = false;
			return;
		}
		isLoaderSuccess = true;
		if (!isAutoShow) {
			root.setVisibility(View.INVISIBLE);
		} else {
			root.setVisibility(View.VISIBLE);
		}
		LayoutParams ll = (LayoutParams) slidingDrawer
				.getLayoutParams();
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		ll.width = width;
		ll.height = bm.getHeight() * width / bm.getWidth()
				+ newActionLayout.getHeight();
		slidingDrawer.setLayoutParams(ll);
		actionImageView.requestLayout();
		slidingDrawer.requestLayout();
		root.setBackgroundColor(0x88333333);
		actionImageView.setImageBitmap(bm);

		Message msg = mHandler.obtainMessage(1002);
		mHandler.sendMessageDelayed(msg, 100);
	}

	/**
	 * 点击广告图片跳转
	 */
	private void actionImageViewClick() {
		if (slidingDrawer.isMoving()) {
			return;
		}
		logMap.put("VISIT_TYPE", "mapAdClick");
		clickIdentify = "mapAdClick";
		actionLogService.addCoverVisitLog(logMap).identify(clickIdentify, this);
		closeSlider();
		if (coverData != null) {
			if (coverData.getOpenType() == 2) {
				Class<?> activity_cls = SystemServicesUtils.getClass(coverData
						.getAnroidURL());
				Log.e(TAG, "activity_cls : " + activity_cls.getSimpleName());
				if (activity_cls != null) {
					Intent intent = new Intent(context, activity_cls);
					Bundle data = new Bundle();
					for (Entry<String, String> param : StringUtils.getParams(
							coverData.getCustomParam()).entrySet()) {
						data.putString(param.getKey(), param.getValue());
					}
					data.putString(Constants.FROM_WHERE, Constants.FROM_MAP_AD);
					data.putString(Constants.COVER_ID, coverData.getCoverId()
							+ "");
					data.putString("title", coverData.getTitle());
					intent.putExtras(data);
					context.startActivity(intent);
				}
			} else if (coverData.getOpenType() == 1) {
				NewsBrowserActivity_.intent(context)
						.url(coverData.getAnroidURL())
						.title(coverData.getTitle())
						.InformationTitleInfoId(coverData.getCoverId())
						//.properties(coverData.getProperties())
						.signType(Constants.SINGN_TYPE_ADS)
						.fromWhere(Constants.FROM_MAP_AD).start();
			}
		}
	}

	public void setCoverData(InformationCoverDTO data) {
		this.coverData = data;
		if (coverData != null) {
			logMap.put(Constants.COVER_ID, coverData.getCoverId() + "");
			logMap.put("USER_ID", ZcdhApplication.getInstance().getZcdh_uid()
					+ "");
			String url = coverData.getCover().getOriginal();
			if (url != null) {
				showAction(url);
			}
		}
	}

	/**
	 * 
	 * chensheng 2015年4月22日下午7:03:31
	 * 
	 * @param url
	 *            void
	 */
	private void showAction(String url) {
		ImageLoader.getInstance().displayImage(url, actionImageView, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						// TODO Auto-generated method stub
						if (arg2 == null) {
							return;
						}
						Message msg = mHandler.obtainMessage(1000);
						msg.obj = arg2;
						mHandler.sendMessageDelayed(msg, 1000);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
					}
				});
	}

	@Override
	public void onClick(View v) {
		if (v.equals(actionImageView)) {
			actionImageViewClick();
		} else if (v.equals(root)) {
			closeSlider();
		}
	}

	public void showActionView(boolean isShow) {
		isAutoShow = isShow;
		if (isLoaderSuccess && isShow) {
			root.setVisibility(View.VISIBLE);
		} else {
			closeSlider();
			root.setVisibility(View.INVISIBLE);
		}
	}

	public void closeSlider() {
		if (!isShow) {
			slidingDrawer.close();
			root.setBackgroundColor(0x00000000);
			root.setOnClickListener(null);
			root.setClickable(false);
			root.setEnabled(false);
		}

	}

	@Override
	public void onDrawerClosed() {
		root.setBackgroundColor(0x00000000);
		root.setOnClickListener(null);
		root.setClickable(false);
		root.setEnabled(false);
		isShow = true;
	}

	@Override
	public void onDrawerOpened() {
		root.setBackgroundColor(0x88333333);
		root.setOnClickListener(this);
		root.setClickable(true);
		root.setEnabled(true);
		isShow = false;
		Log.e(TAG, "onDrawerOpened");
		logMap.put("VISIT_TYPE", "mapAdPop");
		autoIdentify = "mapAdPop";
		actionLogService.addCoverVisitLog(logMap).identify(autoIdentify,
				ActionView.this);

	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(clickIdentify)) {
			Log.i(TAG, "log click ad success !");
		} else if (reqId.equals(autoIdentify)) {
			Log.i(TAG, "log pop ad success !");
		}

	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}
}
