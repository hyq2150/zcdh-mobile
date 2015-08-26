/**
 * 
 * @author jeason, 2014-6-4 下午4:14:33
 */
package com.zcdh.mobile.app.activities.detail;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.ImgURLDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.views.AlbumCover;
import com.zcdh.mobile.app.views.AlbumCover_;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-6-4 下午4:14:33
 * 公司环境图片展示页面
 */
@EActivity(R.layout.ent_exhibition)
public class EntAlbumActivity extends BaseActivity implements RequestListener, OnRefreshListener2<GridView>, OnItemClickListener {

	@ViewById(R.id.gridView)
	PullToRefreshGridView gridview;

	IRpcJobEnterpriseService entService;

	private int currentPage = 1;

	private final int pageSize = 10;

	private long entId;

	private Page<ImgURLDTO> pageImages;

	private String kREQ_ID_findEntPhotoByPage;

	ImagesAdapter adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		entId = getIntent().getLongExtra("entId", 0l);
		entService = RemoteServiceManager.getRemoteService(IRpcJobEnterpriseService.class);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "公司环境");
	}

	@AfterViews
	void afterViews() {
		adapter = new ImagesAdapter();
		gridview.setMode(Mode.BOTH);
		gridview.setOnRefreshListener(this);
		gridview.setOnItemClickListener(this);
		gridview.setAdapter(adapter);
		getData();
	}

	/**
	 * 
	 * @author jeason, 2014-6-7 下午3:23:31
	 */
	@Background
	void getData() {
		entService.findEntPhotoByPage(entId, currentPage, pageSize)
		.identify(kREQ_ID_findEntPhotoByPage=RequestChannel.getChannelUniqueID(), this);;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestStart(java.lang
	 * .String)
	 */
	@Override
	public void onRequestStart(String reqId) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestSuccess(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findEntPhotoByPage)) {
			if (result != null) {
				pageImages = (Page<ImgURLDTO>) result;
				if (pageImages.getCurrentPage() == 1) {
					adapter.updateItems(pageImages.getElements());
				} else {
					adapter.addToBottom(pageImages.getElements());
				}
			}
		}

	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		getLatest();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:07
	 */
	private void getLatest() {
		currentPage = 1;
		getData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		getMore();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:05
	 */
	private void getMore() {
		if (pageImages == null) {
			currentPage = 1;
		} else {
			if (pageImages.hasNextPage()) {
				currentPage = pageImages.getNextPage();
			} else {
				onComplete();
				Toast.makeText(this, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();

				return;
			}
		}
		getData();
	}

	@UiThread
	void onComplete() {
		gridview.onRefreshComplete();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		String[] urls = new String[adapter.getCount()];
		int i = 0;
		for (ImgURLDTO img : adapter.getItems()) {
			urls[i] = img.getOriginal();
			i++;
		}
		ActivityDispatcher.toPhotoBrowser(this, adapter.getItem(position).getOriginal(), urls);
	}

	private class ImagesAdapter extends BaseAdapter {
		private List<ImgURLDTO> mImgs;

		/**
		 * @author jeason, 2014-6-5 下午3:54:05
		 */
		public ImagesAdapter() {
			mImgs = new ArrayList<ImgURLDTO>();
		}

		public void updateItems(List<ImgURLDTO> comments) {
			mImgs.clear();
			mImgs.addAll(comments);
			notifyDataSetChanged();
		}

		public void addToBottom(List<ImgURLDTO> comments) {
			mImgs.addAll(comments);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mImgs.size();
		}

		public List<ImgURLDTO> getItems() {
			return mImgs;
		}

		@Override
		public ImgURLDTO getItem(int position) {
			return mImgs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AlbumCover cover;
			if (convertView != null && convertView instanceof AlbumCover) {
				cover = (AlbumCover) convertView;
			} else {
				cover = AlbumCover_.build(EntAlbumActivity.this);
			}
			cover.switchSize();
			cover.initData(getItem(position).getBig());
			return cover;
		}

	}

}
