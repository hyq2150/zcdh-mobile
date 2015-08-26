/**
 * 
 * @author jeason, 2014-6-4 下午9:28:44
 */
package com.zcdh.mobile.app.activities.detail;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.CommentDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.views.CommentItemView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-6-4 下午9:28:44
 * 公司评论 弃用
 * @deprecated
 */
@EActivity(R.layout.company_comments)
public class CompanyCommentsActivity extends BaseActivity implements RequestListener, OnRefreshListener2<ListView> {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "更多评论");
		entService = RemoteServiceManager.getRemoteService(IRpcJobEnterpriseService.class);
		entId = getIntent().getLongExtra("entId", 0l);
	}

	@AfterViews
	void afterView() {
		adapter = new CommentsAdapter();
		ptrListView.setMode(Mode.BOTH);
		ptrListView.setOnRefreshListener(this);
		ptrListView.setAdapter(adapter);
		ptrListView.getRefreshableView().setDivider(null);
		ptrListView.getRefreshableView().setDividerHeight(5);
		findComment();
	}

	/**
	 * 
	 * @author jeason, 2014-6-5 下午3:23:09
	 */
	@Background
	void findComment() {
		entService.findEntCommentDTO(entId, currentPage, pageSize)
		.identify(kREQ_ID_findEntCommentDTO=RequestChannel.getChannelUniqueID(), this);;
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
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

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
			CommentItemView item = new CommentItemView(CompanyCommentsActivity.this);
			item.initData(CompanyCommentsActivity.this, getItem(position));
			return item;
		}

	}

	/*
	 * /* (non-Javadoc)
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
		findComment();
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
				Toast.makeText(this, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();

				return;
			}
		}
		findComment();
	}

	@UiThread
	void onComplete() {
		ptrListView.onRefreshComplete();
	}

	@Click(R.id.comment)
	void onComment() {
		ActivityDispatcher.toComment(this, entId);
	}
}
