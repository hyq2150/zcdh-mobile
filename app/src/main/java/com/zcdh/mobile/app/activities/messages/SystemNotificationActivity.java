/**
 * 
 * @author jeason, 2014-7-21 下午2:55:27
 */
package com.zcdh.mobile.app.activities.messages;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.InformationDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.newsmodel.NewsBrowserActivity_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-7-21 下午2:55:27
 * 系统资讯页面
 */
@EActivity(R.layout.messages)
public class SystemNotificationActivity extends BaseActivity implements RequestListener, 
		OnItemClickListener, OnRefreshListener2<ListView>, DataLoadInterface {

	IRpcNearByService nearbyService;

	@ViewById(R.id.listview)
	PullToRefreshListView ptlListView;

	private int currentPage = 1;

	private final int PageSize = 10;

	long msg_id;

	String K_REQ_ID_FINDSUBINFORMATIONLISTBYID;

	Page<InformationDTO> infos;

	InfoAdapter adapter;

	EmptyTipView emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "系统通知");
		msg_id = getIntent().getLongExtra("id", 0l);
		nearbyService = RemoteServiceManager.getRemoteService(IRpcNearByService.class);
	}

	@AfterViews
	void afterView() {
		emptyView = new EmptyTipView(this);

		ptlListView.getRefreshableView().setDivider(null);
		ptlListView.getRefreshableView().setDividerHeight(5);

		ptlListView.setMode(Mode.BOTH);
		ptlListView.setOnItemClickListener(this);
		ptlListView.setOnRefreshListener(this);
		ptlListView.setEmptyView(emptyView);
		adapter = new InfoAdapter(this);
		ptlListView.setAdapter(adapter);

		findInfos();
	}
	
	public void loadData(){
		findInfos();
	}

	@Background
	void findInfos() {
		nearbyService.findSubInformationListById(msg_id, getUserId(), currentPage, PageSize).identify(K_REQ_ID_FINDSUBINFORMATIONLISTBYID = RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(K_REQ_ID_FINDSUBINFORMATIONLISTBYID)) {
			if (result != null) {
				infos = (Page<InformationDTO>) result;
				if (infos.getCurrentPage() == 1) {
					adapter.updateAllItems(infos.getElements());
				} else {
					adapter.addToBottom(infos.getElements());
				}
				// checkUnRead();
			}
			emptyView.isEmpty(infos==null);
		}

	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException(((ZcdhException)error).getErrCode(), this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		NewsBrowserActivity_.intent(this).url(((InformationDTO) adapter.getItem(position - 1)).getAnroidURL()).title(((InformationDTO) adapter.getItem(position - 1)).getTitle()).start();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		getLatest();
		onComplete();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:07
	 */
	private void getLatest() {
		currentPage = 1;
		findInfos();
		onComplete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2
	 * #onPullUpToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		getMore();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:05
	 */
	private void getMore() {
		if (infos == null) {
			currentPage = 1;
		} else {
			if (infos.hasNextPage()) {
				currentPage = infos.getNextPage();
			} else {
				onComplete();
				Toast.makeText(this, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();

				return;
			}
		}
		findInfos();
		onComplete();
	}

	@UiThread
	void onComplete() {
		ptlListView.onRefreshComplete();
	}

	private class InfoAdapter extends BaseAdapter {
		private DisplayImageOptions options;
		private Context mContext;
		private List mItems;

		public InfoAdapter(Context context) {
			mContext = context;
			mItems = new ArrayList();
			options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.build();
		}

		public void updateAllItems(List items) {
			mItems.clear();
			mItems.addAll(items);
			notifyDataSetChanged();
		}

		public void addToBottom(List items) {
			mItems.addAll(items);
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			InfoViewHolder holder;
			if (convertView == null) {

				convertView = LayoutInflater.from(mContext).inflate(R.layout.info_item, null);

				holder = new InfoViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.flag = (TextView) convertView.findViewById(R.id.flag);
				holder.note = (TextView) convertView.findViewById(R.id.tv_note);
				holder.title = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				convertView.setTag(holder);

			} else {
				holder = (InfoViewHolder) convertView.getTag();
			}

			if (mItems.get(position) instanceof InformationDTO) {
				holder.initWithInformationDTO((InformationDTO) mItems.get(position));
			}
			return convertView;
		}

		public class InfoViewHolder {
			public ImageView icon;
			public TextView title;
			public TextView note;
			public TextView flag;
			public TextView tv_time;

			/**
			 * @param informationDTO
			 * @author jeason, 2014-7-4 上午11:44:03 企业回信
			 */
			public void initWithInformationDTO(InformationDTO informationDTO) {

				if (informationDTO.getAndoridImg() != null) 
					ImageLoader.getInstance().displayImage(informationDTO.getAndoridImg().getMedium(), icon, options);
				if (informationDTO.getIsRead() == 0) {
					title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.redpoint, 0);
					title.setCompoundDrawablePadding(-5);
				} else {
					title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					title.setCompoundDrawablePadding(-5);
				}
				if (!StringUtils.isBlank(informationDTO.getTitle())) title.setText(informationDTO.getTitle());

				note.setVisibility(View.GONE);
				flag.setVisibility(View.GONE);
				tv_time.setText(DateUtils.getDateByFormatYMDHM(informationDTO.getPushTime()));
			}

		}
	}

}
