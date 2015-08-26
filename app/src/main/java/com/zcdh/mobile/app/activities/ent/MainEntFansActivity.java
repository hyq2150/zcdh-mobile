package com.zcdh.mobile.app.activities.ent;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.EntFansDTO;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 粉丝列表
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_main_ent_fans)
public class MainEntFansActivity extends BaseActivity implements
		RequestListener, OnRefreshListener<ListView> {

	IRpcJobEnterpriseService enterpriseService;

	String KREQ_ID_findEntFansDTO;

	@ViewById(R.id.fansListView)
	PullToRefreshListView fansListView;

	// @ViewById(R.id.emptyView)
	// ScrollView emptyView;

	@Extra
	long entId;

	/**
	 * 粉丝
	 */
	List<EntFansDTO> fansList = new ArrayList<EntFansDTO>();

	FansAdapter fansAdapter;

	private Integer currentPage = 1;

	private Integer pageSize = 20;

	EmptyTipView emptyView;

	@Override
	protected void onDestroy() {
		RemoteServiceManager.removeService(this);
		super.onDestroy();

	}

	@AfterViews
	void bindViews() {

		enterpriseService = RemoteServiceManager
				.getRemoteService(IRpcJobEnterpriseService.class);

		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), getString(R.string.activity_title_fans));
		emptyView = new EmptyTipView(this);
		fansListView.setOnRefreshListener(this);
		fansListView.setMode(Mode.PULL_FROM_END);
		fansListView.setEmptyView(emptyView);
		fansAdapter = new FansAdapter();
		fansListView.setAdapter(fansAdapter);

		loadData();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

		loadData();
	}

	@Background
	void loadData() {
		enterpriseService
				.findEntFansDTO(entId, currentPage, pageSize)
				.identify(
						KREQ_ID_findEntFansDTO = RequestChannel
								.getChannelUniqueID(),
						this);
		currentPage++;
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(KREQ_ID_findEntFansDTO)) {
			if (result != null) {
				Page<EntFansDTO> page = (Page<EntFansDTO>) result;
				fansList.addAll(page.getElements());
				fansAdapter.notifyDataSetChanged();

			}

			emptyView.isEmpty(!(fansList.size() > 0));
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@UiThread
	void onComplete() {
		fansListView.onRefreshComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @author yangjiannan
	 * 
	 */
	class FansAdapter extends BaseAdapter {
		private DisplayImageOptions options;

		public FansAdapter() {
			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.email)
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true).build();
		}

		@Override
		public int getCount() {
			return fansList.size();
		}

		@Override
		public Object getItem(int position) {
			return fansList.get(position);
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
						.inflate(R.layout.fans_item, null);
				h = new ViewHolder();
				h.headImg = (ImageView) convertView.findViewById(R.id.headImg);
				h.noHeadTxt = (TextView) convertView
						.findViewById(R.id.noHeadTxt);
				h.nameTxt = (TextView) convertView.findViewById(R.id.nameTxt);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}
			EntFansDTO fans = fansList.get(position);
			if (fans.getImg() != null
					&& !StringUtils.isBlank(fans.getImg().getBig())) {
				ImageLoader.getInstance().displayImage(fans.getImg().getBig(),
						h.headImg, options);
				h.headImg.setVisibility(View.VISIBLE);
				h.noHeadTxt.setVisibility(View.GONE);
			} else {
				h.headImg.setVisibility(View.GONE);
				h.noHeadTxt.setVisibility(View.VISIBLE);
				h.noHeadTxt.setText(fans.getUserName());
			}
			h.nameTxt.setText(fans.getUserName());

			return convertView;
		}

		class ViewHolder {
			ImageView headImg;
			TextView noHeadTxt;
			TextView nameTxt;
		}
	}

}
