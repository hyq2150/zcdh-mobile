package com.zcdh.mobile.app.activities.newsmodel;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobAppService;
import com.zcdh.mobile.api.model.InformationTitleInfoDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 资讯列表
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_newslist)
public class NewsListActivity extends BaseActivity implements RequestListener,
		OnItemClickListener, OnRefreshListener2<ListView>, OnClickListener,
		DataLoadInterface {

	private final static String TAG = NewsListActivity.class.getSimpleName();

	public final static String kMODEL_ID = "kMODEL_ID";

	String kREQ_ID_findInformationTitleInfoList;
	String kREQ_ID_addInformationAccessTimesDetail;

	IRpcJobAppService appService;

	@ViewById(R.id.newsListView)
	PullToRefreshListView newsListView;

	EmptyTipView emptyTipView;

	NewsListAdapter newslistAdapter;

	// List<InformationTitleInfoDTO> newsList = new
	// ArrayList<InformationTitleInfoDTO>();

	/**
	 * 以日期分组
	 */
	HashMap<Long, List<InformationTitleInfoDTO>> newsList = new HashMap<Long, List<InformationTitleInfoDTO>>();

	int currentPage = 1;
	int pageSize = 20;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 应用模块id
	 */
	long model_id;

	private boolean hasNextPage;

	/**
	 * 根据获得的图片的尺寸，计算ImageView 显示的高度
	 */
	int imageScaleHeight = 0;

	@AfterViews
	void bindViews() {

		model_id = getIntent().getLongExtra(kMODEL_ID, -1l);
		String title = getIntent().getStringExtra("title");

		appService = RemoteServiceManager
				.getRemoteService(IRpcJobAppService.class);

		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), title);
		newsListView.setOnItemClickListener(this);
		newsListView.setOnRefreshListener(this);
		newsListView.setMode(Mode.PULL_FROM_START);
		emptyTipView = new EmptyTipView(this);
		newsListView.setEmptyView(emptyTipView);
		newslistAdapter = new NewsListAdapter();
		newsListView.setAdapter(newslistAdapter);

		loadData();
	}

	@Background
	public void loadData() {
		appService
				.findInformationTitleInfoList(model_id, currentPage, pageSize)
				.identify(
						kREQ_ID_findInformationTitleInfoList = RequestChannel
								.getChannelUniqueID(),
						this);
		currentPage++;
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findInformationTitleInfoList)) {
			if (result != null) {
				Page<InformationTitleInfoDTO> page = (Page<InformationTitleInfoDTO>) result;
				hasNextPage = page.hasNextPage();
				if (page.getElements() != null) {
					groupByDate(page.getElements());
					newslistAdapter.notifyDataSetChanged();
				}
			}
		}

		emptyTipView.isEmpty(!(newsList != null && newsList.size() > 0));
	}

	@Override
	public void onRequestFinished(String reqId) {
		newsListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				newsListView.onRefreshComplete();
			}
		}, 100);
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyTipView.showException((ZcdhException) error, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent instanceof ListView) {
			ListView newsListView = (ListView) parent;
			Toast.makeText(this, (id == R.id.newsListView) + "",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Background
	void addInformationAccessTimesDetail(java.lang.Long id, long userId) {
		Log.e("addInformationAccessTimesDetail", "excute");
		appService
				.addInformationAccessTimesDetail(id, userId)
				.identify(
						kREQ_ID_addInformationAccessTimesDetail = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (hasNextPage) {
			loadData();
		} else {
			if (!hasNextPage) {
				Toast.makeText(getApplication(), "无更多资讯", Toast.LENGTH_SHORT)
						.show();
			}
			onRequestFinished(null);
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.clickCoverBtn) {
			InformationTitleInfoDTO informationTitleInfoDTO = (InformationTitleInfoDTO) v
					.getTag();
			if (informationTitleInfoDTO != null) {
				long userId = ZcdhApplication.getInstance().getZcdh_uid();
				if (userId == -1) {
					userId = 0;
				}
				Log.i("newslit ", informationTitleInfoDTO.getUrl() + "&userId="
						+ userId);
				NewsBrowserActivity_.intent(this)
						.url(informationTitleInfoDTO.getUrl())
						.title(informationTitleInfoDTO.getTitle()).start();
				addInformationAccessTimesDetail(
						informationTitleInfoDTO.getId(), userId);
			}
		}
	}

	/**
	 * 将资讯列表已日期分组 1407168000000<=>1 8-5 1408636800000<=>3 8-22
	 * 
	 * 1407168000000<=>0 8-5 1408636800000<=>2 8-22 1406736000000<=>1 7-23
	 */
	private void groupByDate(List<InformationTitleInfoDTO> newsList) {

		if (newsList != null && newsList.size() > 0) {

			for (int i = 0; i < newsList.size(); i++) {
				InformationTitleInfoDTO dto = newsList.get(i);
				long key = DateUtils.getDateByStrToYMD(
						DateUtils.getDateByFormatYMD(dto.getPublishDate()))
						.getTime();
				// Log.i("key .... ",
				// key+""+DateUtils.getDateByFormatYMDHMS(dto.getPublishDate()));
				List<InformationTitleInfoDTO> sectionItem = this.newsList
						.get(key);
				if (sectionItem == null) {
					sectionItem = new ArrayList<InformationTitleInfoDTO>();
					this.newsList.put(key, sectionItem);
				}

				sectionItem.add(dto);

			}

			for (long k : this.newsList.keySet()) {
				Log.i("key .... ", k + "<=>" + this.newsList.get(k).size());
			}
		}
	}

	/**
	 * 
	 * @author yangjiannan 每日资讯列表 (在界面中体现的时一个Listiew分段)
	 */
	class SectionItemAdapter extends BaseAdapter implements OnItemClickListener {

		List<InformationTitleInfoDTO> sectionNewsList = new ArrayList<InformationTitleInfoDTO>();
		private DisplayImageOptions options;

		public SectionItemAdapter(List<InformationTitleInfoDTO> sectionNewsList) {
			this.sectionNewsList = sectionNewsList;
			options = new DisplayImageOptions.Builder()
					.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true).build();
		}

		@Override
		public int getCount() {
			return sectionNewsList.size() - 1;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.news_item, null);
				h = new ViewHolder();
				h.newsImg = (ImageView) convertView.findViewById(R.id.newsImg);
				h.titleTxt = (TextView) convertView.findViewById(R.id.titleTxt);
				// h.dateTxt = (TextView)
				// convertView.findViewById(R.id.dateTxt);
				// h.readedCountTxt = (TextView) convertView
				// .findViewById(R.id.readedCountTxt);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			InformationTitleInfoDTO title = sectionNewsList.get(position + 1);
			if (title.getTitleImg() != null) {
				ImageLoader.getInstance().displayImage(
						title.getTitleImg().getBig(), h.newsImg, options);
			}
			h.titleTxt.setText(title.getTitle());
			// h.readedCountTxt.setText(String.format("已阅读%s次",
			// title.getReadTimes()));
			// h.dateTxt.setText(sdf.format(title.getPublishDate()));
			return convertView;
		}

		class ViewHolder {
			ImageView newsImg;// 新闻图片
			TextView titleTxt;// 标题
			// TextView dateTxt; // 日期
			// TextView readedCountTxt; // 阅读次数
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			InformationTitleInfoDTO informationTitleInfoDTO = sectionNewsList
					.get(position + 1);
			if (informationTitleInfoDTO != null) {
				long userId = ZcdhApplication.getInstance().getZcdh_uid();
				if (userId == -1) {
					userId = 0;
				}
				Log.i("newslit ", informationTitleInfoDTO.getUrl() + "&userId="
						+ userId);
				NewsBrowserActivity_.intent(NewsListActivity.this)
						.url(informationTitleInfoDTO.getUrl())
						.title(informationTitleInfoDTO.getTitle()).start();
				addInformationAccessTimesDetail(
						informationTitleInfoDTO.getId(), userId);
			}

		}

	}

	/**
	 * 资讯列表 （类似微信的订阅）
	 * 
	 * @author yangjiannan
	 *
	 */
	class NewsListAdapter extends BaseAdapter {

		private DisplayImageOptions options;

		public NewsListAdapter() {
			options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true).build();
		}

		@Override
		public int getCount() {
			return newsList.size();
		}

		@Override
		public Object getItem(int position) {
			return newsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;

			convertView = LayoutInflater.from(NewsListActivity.this).inflate(
					R.layout.news_section_item, null);
			h = new ViewHolder();
			h.coverRowFl = (FrameLayout) convertView
					.findViewById(R.id.coverRowFl);
			h.dateText = (TextView) convertView.findViewById(R.id.dateTxt);
			h.clickCoverBtn = (Button) convertView
					.findViewById(R.id.clickCoverBtn);
			h.coverImgBtn = (ImageButton) convertView
					.findViewById(R.id.coverImgBtn);
			h.coverTitleText = (TextView) convertView
					.findViewById(R.id.coverTitleText);
			h.newsListView = (ListViewInScrollView) convertView
					.findViewById(R.id.sectionListView);
			h.clickCoverBtn.setOnClickListener(NewsListActivity.this);

			// convertView.setTag(h);
			// if(convertView==null){
			// }else{
			// h = (ViewHolder) convertView.getTag();
			// }
			Object[] keys = newsList.keySet().toArray();
			Arrays.sort(keys);
			List<InformationTitleInfoDTO> sectionNewsList = newsList
					.get(keys[position]);
			if (sectionNewsList != null && sectionNewsList.size() > 0) {
				InformationTitleInfoDTO no1 = sectionNewsList.get(0);

				if (imageScaleHeight == 0) {
					/*
					 * Drawable drawable = h.coverImgBtn.getDrawable();
					 * BitmapFactory.Options Options = new
					 * BitmapFactory.Options(); Options.inJustDecodeBounds =
					 * true; Bitmap bitmap =
					 * ImageUtils.GetBitmapByUrl(no1.getTitleImg
					 * ().getOriginal()); int width =bitmap.getWidth(); int
					 * height = bitmap.getHeight();
					 */
					float scale = 2 / 5;

					int screenWidth = getResources().getDisplayMetrics().widthPixels;
					imageScaleHeight = (int) (screenWidth * scale);
				}

				h.dateText.setText(DateUtils.getDateByFormatYMD(no1
						.getPublishDate()));
				if (no1.getTitleImg() != null
						&& !StringUtils
								.isBlank(no1.getTitleImg().getOriginal())) {
					ImageLoader.getInstance().displayImage(
							no1.getTitleImg().getOriginal(), h.coverImgBtn,
							options);
					Log.i(TAG, no1.getTitleImg().getOriginal());
				}
				h.coverTitleText.setText(no1.getTitle());
				h.clickCoverBtn.setTag(no1);
				// sectionNewsList.remove(no1);
				SectionItemAdapter adapter = new SectionItemAdapter(
						sectionNewsList);
				h.newsListView.setAdapter(adapter);
				h.newsListView.setOnItemClickListener(adapter);
			}

			if (imageScaleHeight != 0) {
				Log.i(TAG, "imageScaleHeight:" + imageScaleHeight);
				h.coverRowFl.getLayoutParams().height = imageScaleHeight;
				h.coverRowFl.requestLayout();
			}

			return convertView;
		}

		class ViewHolder {
			TextView dateText;// 日期
			TextView coverTitleText; // 封面资讯
			ImageButton coverImgBtn;//
			Button clickCoverBtn; // 每日分段资讯的第一条作为封面展示
			ListViewInScrollView newsListView;
			FrameLayout coverRowFl;
		}

	}

}
