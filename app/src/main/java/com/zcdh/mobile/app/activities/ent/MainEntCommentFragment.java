package com.zcdh.mobile.app.activities.ent;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.CommentDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.views.CommentItemView;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;

/**
 * 企业主页评价
 * 
 * @author yangjiannan
 * 
 */
@EFragment(R.layout.company_comments)
public class MainEntCommentFragment extends BaseFragment implements RequestListener,
		OnRefreshListener2<ListView>, DataLoadInterface {
	private static final String TAG = MainEntCommentFragment.class.getSimpleName();

	/**
	 * 联网services
	 * */
	private IRpcJobEnterpriseService entService;

	private Long entId;

	private int currentPage = 1;

	private final int pageSize = 10;

	private Page<CommentDTO> commentPage;

	private String kREQ_ID_findEntCommentDTO;

	@ViewById(R.id.listview)
	PullToRefreshListView ptrListView;

	private CommentsAdapter adapter;
	EmptyTipView emptyView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		entService = RemoteServiceManager.getRemoteService(IRpcJobEnterpriseService.class);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		loadData();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}


	@AfterViews
	void bindViews() {
		entId = getArguments().getLong(Constants.kENT_ID);
		Log.i(TAG, "ent_id:" + entId);
		adapter = new CommentsAdapter();
		emptyView = new EmptyTipView(getActivity());
		ptrListView.setEmptyView(emptyView);
		ptrListView.setMode(Mode.BOTH);
		ptrListView.setOnRefreshListener(this);
		ptrListView.setAdapter(adapter);
		ptrListView.getRefreshableView().setDivider(null);
		ptrListView.getRefreshableView().setDividerHeight(5);
	}

	public void refresh() {
		ptrListView.setRefreshing(true);
	}

	/**
	 * 
	 * @author jeason, 2014-6-5 下午3:23:09
	 */
	@Background
	public void loadData() {
		entService.findEntCommentDTO(entId, currentPage, pageSize).identify(kREQ_ID_findEntCommentDTO = RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findEntCommentDTO)) {
			if (result != null) {
				commentPage = (Page<CommentDTO>) result;
				if (commentPage.getCurrentPage() == 1) {
					adapter.updateItems(commentPage.getElements());
				} else {
					adapter.addToBottom(commentPage.getElements());
				}
			}
			
			emptyView.isEmpty(!(adapter.getCount()>0));
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException)error, this);
	}

	private class CommentsAdapter extends BaseAdapter {
		private List<CommentDTO> mComments;

		/**
		 * @author jeason, 2014-6-5 下午3:54:05
		 */
		public CommentsAdapter() {
			mComments = new ArrayList<CommentDTO>();
		}

		public void updateItems(List<CommentDTO> comments) {
			mComments.clear();
			mComments.addAll(comments);
			notifyDataSetChanged();
		}

		public void addToBottom(List<CommentDTO> comments) {
			mComments.addAll(comments);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mComments.size();
		}

		@Override
		public CommentDTO getItem(int position) {
			return mComments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CommentItemView item = new CommentItemView(getActivity());
			item.initData(getActivity(), getItem(position));
			return item;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2
	 * #onPullDownToRefresh
	 * (com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		getLatest();

	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:07
	 */
	private void getLatest() {
		currentPage = 1;
		loadData();
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
		if (commentPage == null) {
			currentPage = 1;
		} else {
			if (commentPage.hasNextPage()) {
				currentPage = commentPage.getNextPage();
			} else {
				onComplete();
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();

				return;
			}
		}
		loadData();
	}

	@UiThread
	void onComplete() {
		ptrListView.onRefreshComplete();
	}

	@Click(R.id.comment)
	void onComment() {
		ActivityDispatcher.toComment(getActivity(), entId);
	}
}
