package com.zcdh.mobile.app.activities.newsmodel;

import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobAppService;
import com.zcdh.mobile.api.model.InformationTitleInfoDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author YJN
 *
 */
@EActivity(R.layout.activity_news_list_special)
public class NewsListSpecialActivity extends BaseActivity implements
		RequestListener, IXListViewListener, OnItemClickListener,
		YuyinInputListner {

	private static final String TAG = NewsListSpecialActivity.class
			.getSimpleName();
	private String kREQ_ID_findInformationTitleInfoList;
	private String kREQ_ID_seachSubInformationListByKeyWord;

	private IRpcJobAppService appService;

	@ViewById(R.id.emptyView)
	EmptyTipView emptyView;

	@ViewById(R.id.newsListView)
	XListView newsListView;

	private List<InformationTitleInfoDTO> newsList = new ArrayList<InformationTitleInfoDTO>();

	private NewsItemAdapter newsAdapter;

	/* 搜索相关 */
	private String keyword;
	private YuYinInputView yuyinInputView;

	@ViewById(R.id.searchBtn)
	Button searchBtn;

	@ViewById(R.id.keywordEditText)
	EditText keywordEditText;

	private ProcessDialog processDialog;

	/* end搜索相关 */

	/**
	 * 应用模块id
	 */
	private long model_id;
	private Integer currentPage = 1;
	private Integer pageSize = 15;
	private boolean hasNextPage;

	@AfterViews
	void bindViews() {

		appService = RemoteServiceManager
				.getRemoteService(IRpcJobAppService.class);
		model_id = getIntent().getLongExtra(NewsListActivity.kMODEL_ID, -1l);
		String title = getIntent().getStringExtra("title");

		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), title);
		newsListView.setPullRefreshEnable(false);
		newsListView.setPullLoadEnable(true);
		newsListView.setAutoLoadEnable(false);
		newsListView.setXListViewListener(this);
		newsListView.setOnItemClickListener(this);
		newsListView.setPullLoadEnable(false);
		newsAdapter = new NewsItemAdapter();
		newsListView.setAdapter(newsAdapter);

		searchBtn.setVisibility(View.VISIBLE);
		processDialog = new ProcessDialog(this);
		loadData();
	}

	@Background
	public void loadData() {
		appService
				.findSubInformationListByAmount(this.model_id, currentPage,
						pageSize)
				.identify(
						kREQ_ID_findInformationTitleInfoList = RequestChannel
								.getChannelUniqueID(),
						this);
		currentPage++;

	}

	@Background
	void search(String keyword) {
		if (!keyword.equals(this.keyword)) {
			this.keyword = keyword;
			currentPage = 1;
			newsList.clear();
		}
		appService
				.seachSubInformationListByKeyWord(keyword, model_id,
						currentPage, pageSize)
				.identify(
						kREQ_ID_seachSubInformationListByKeyWord = RequestChannel
								.getChannelUniqueID(),
						this);
		currentPage++;

	}

	/**
	 * 添加访问次数
	 * 
	 * @param id
	 * @param userId
	 */
	@Background
	void addInformationAccessTimesDetail(java.lang.Long id, long userId) {
		Log.e("addInformationAccessTimesDetail", "excute");
		appService.addInformationAccessTimesDetail(id, userId).identify(
				RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 语音结果
	 */
	@Override
	public void onComplete(String content) {
		if (!TextUtils.isEmpty(content)) {
			processDialog.show();
			this.keywordEditText.setText(content);
			search(content);
		}
	}

	@TextChange(R.id.keywordEditText)
	void keywordChange(CharSequence text, TextView hello, int before,
			int start, int count) {
		if (TextUtils.isEmpty(keywordEditText.getText().toString())) {
			currentPage = 1;
			newsList.clear();
			loadData();
		}
	}

	@Click(R.id.searchBtn)
	void onSearch() {
		String keyword = keywordEditText.getText().toString();
		if (!TextUtils.isEmpty(keyword)) {
			processDialog.show();
			search(keyword);
		}
		/*
		 * else{ currentPage=1; newsList.clear(); processDialog.show();
		 * loadData(); }
		 */
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (yuyinInputView != null && yuyinInputView.isShowing()) {
			yuyinInputView.dismiss();
		}
	}
	@Click(R.id.micBtn)
	void onMic() {
		if (yuyinInputView == null)
			yuyinInputView = new YuYinInputView(this, this);
		yuyinInputView.showAtParent(findViewById(R.id.body));
		keywordEditText.clearFocus();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		Log.i(TAG, "");
		InformationTitleInfoDTO informationTitleInfoDTO = newsList
				.get(position - 1);
		NewsBrowserActivity_.intent(this).url(informationTitleInfoDTO.getUrl())
				.title(informationTitleInfoDTO.getTitle())
				.InformationTitleInfoId(informationTitleInfoDTO.getId())
				.properties(informationTitleInfoDTO.getProperties())
				.signType(Constants.SINGN_TYPE_NEWS).start();
		addInformationAccessTimesDetail(informationTitleInfoDTO.getId(),
				getUserId());
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		if (!TextUtils.isEmpty(this.keyword)) {
			search(this.keyword);
		} else {
			loadData();
		}
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findInformationTitleInfoList)) {
			if (result != null) {
				Page<InformationTitleInfoDTO> page = (Page<InformationTitleInfoDTO>) result;
				Log.i("hasNextPage", "hasNextPage" + page.hasNextPage());
				Log.i("hasNextPage", "getTotalRows" + page.getTotalRows());
				newsList.addAll(page.getElements());
				hasNextPage = page.hasNextPage();
				if (page.getElements() != null) {
					Log.i(TAG, "newsList size:" + newsList.size());
					Log.i(TAG, "getElements size:" + page.getElements().size());

					newsAdapter.notifyDataSetChanged();
				}
				// 2bprogramer.io
			}
			if (newsList != null && newsList.size() > 0) {
				newsListView.setVisibility(View.VISIBLE);
			} else {
				newsListView.setVisibility(View.GONE);
			}//
			emptyView.isEmpty(newsList.size() == 0);

		}

		if (reqId.equals(kREQ_ID_seachSubInformationListByKeyWord)) {
			if (result != null) {
				Page<InformationTitleInfoDTO> page = (Page<InformationTitleInfoDTO>) result;
				Log.i("hasNextPage", "hasNextPage" + page.hasNextPage());
				Log.i("hasNextPage", "getTotalRows" + page.getTotalRows());
				newsList.addAll(page.getElements());
				hasNextPage = page.hasNextPage();
				if (page.getElements() != null) {
					Log.i(TAG, "newsList size:" + newsList.size());
					Log.i(TAG, "getElements size:" + page.getElements().size());

					newsAdapter.notifyDataSetChanged();
				}

			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				onComplete(hasNextPage);
			}

		}, 1000);

		processDialog.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	@UiThread
	void onComplete(boolean hasNextPage) {
		Log.i(TAG, "加载更多 Oncomplete");
		newsListView.stopLoadMore();
		newsListView.setPullLoadEnable(hasNextPage);
	}

	/**
	 * 
	 * @author yangjiannan 每日资讯列表 (在界面中体现的时一个Listiew分段)
	 */
	class NewsItemAdapter extends BaseAdapter implements OnItemClickListener {
		private DisplayImageOptions options;

		public NewsItemAdapter() {
			options = new DisplayImageOptions.Builder()
					.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true).build();
		}

		@Override
		public int getCount() {
			return newsList.size();
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
						.inflate(R.layout.news_special_item, null);
				h = new ViewHolder();
				h.newsImg = (ImageView) convertView.findViewById(R.id.newsImg);
				h.titleTxt = (TextView) convertView.findViewById(R.id.titleTxt);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			InformationTitleInfoDTO title = newsList.get(position);
			if (title.getTitleImg() != null) {
				ImageLoader.getInstance().displayImage(
						title.getTitleImg().getBig(), h.newsImg, options);
			}
			h.titleTxt.setText(title.getTitle());
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
			InformationTitleInfoDTO informationTitleInfoDTO = newsList
					.get(position);
			if (informationTitleInfoDTO != null) {
				long userId = ZcdhApplication.getInstance().getZcdh_uid();
				if (userId == -1) {
					userId = 0;
				}
				Log.i("newslit ", informationTitleInfoDTO.getUrl() + "&userId="
						+ userId);
				NewsBrowserActivity_.intent(NewsListSpecialActivity.this)
						.url(informationTitleInfoDTO.getUrl())
						.title(informationTitleInfoDTO.getTitle()).start();
				addInformationAccessTimesDetail(
						informationTitleInfoDTO.getId(), userId);
			}

		}

	}

}
