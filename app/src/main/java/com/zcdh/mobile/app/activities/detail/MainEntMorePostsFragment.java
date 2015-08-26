package com.zcdh.mobile.app.activities.detail;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 公司其他职位
 * 
 * @author yangjiannan
 * 
 */
@EFragment(R.layout.fragment_more_posts_list)
public class MainEntMorePostsFragment extends BaseFragment implements RequestListener, OnRefreshListener2<ListView>,
		OnItemClickListener, Subscriber, DataLoadInterface{

	private static final String TAG = MainEntMorePostsFragment.class.getSimpleName();
	private IRpcJobEnterpriseService entService;

	private String kREQ_ID_findEntPostDTOByEntId;
	private String kREQ_ID_findEntPostDTOByFairId;

	@ViewById(R.id.postsListView)
	PullToRefreshListView postsListView;

	private EmptyTipView emptyView;

	private MorePostsAdapter postsAdapter;

	private int currentPage = 1;
	private int pageSize = 20;

	private List<JobEntPostDTO> posts = new ArrayList<JobEntPostDTO>();

	private long entId = -1;

	private int selected = -1;

	/**
	 * 标识是否是企业主页
	 */

	boolean flag_ent = false;

	Page<JobEntPostDTO> postsPage;
	long jobfair_id = 0;


	public void onDestroy() {
		super.onDestroy();
		MyEvents.unregister(this);
	}


	@Override
	public void onResume() {
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
		MyEvents.register(this);
		entService = RemoteServiceManager.getRemoteService(IRpcJobEnterpriseService.class);

		entId = getArguments().getLong(Constants.kENT_ID);
		flag_ent = getArguments().getBoolean(Constants.KFLAG_ENT);
		jobfair_id = getArguments().getLong(Constants.JOBFAIR_ID_KEY);

		emptyView = new EmptyTipView(getActivity());
		postsListView.setEmptyView(emptyView);
		postsListView.setMode(Mode.PULL_FROM_END);
		postsListView.setOnRefreshListener(this);
		postsListView.setOnItemClickListener(this);

		postsAdapter = new MorePostsAdapter();
		postsListView.setAdapter(postsAdapter);

//		loadData();
	}

	// 唤醒的时候检查 entId 是否有变更，因为在职位详情页会去到不同的职位，entId 也由可能变更
	@Override
	public void receive(String key, Object msg) {
		if (Constants.kMSG_ENT_CHANGED.equals(key)) {
			long id = (Long) msg;
			if (entId != id) {
				entId = id;
				posts.clear();
				loadData();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// Toast.makeText(getActivity(), position-1+"",
		// Toast.LENGTH_SHORT).show();
		long postId = posts.get(position - 1).getPostId();

		if (flag_ent) {
			DetailsFrameActivity_.intent(getActivity()).entId(entId).postId(postId).switchable(true).currentIndex(position).posts(posts).start();
		} else {
			MyEvents.post(Constants.kMSG_POST_ID, postId);

			((DetailsFrameActivity) getActivity()).goesPager(0);
			selected = position;
			postsAdapter.notifyDataSetChanged();

		}

	}

	/**
	 * 加载更多职位
	 */
	@Background
	public void loadData() {
		Log.i("jobfair id", jobfair_id + "");
		if(jobfair_id>0){ //如果jobfair_id 大于0 ，显示所有该企业在该场招聘会的职位
			entService.findEntPostDTOByEntIdFairId(jobfair_id, entId, currentPage, pageSize).
					identify(kREQ_ID_findEntPostDTOByFairId=RequestChannel.getChannelUniqueID(), this);
		}else{
			entService.findEntPostDTOByEntId(entId, currentPage, pageSize).
					identify(kREQ_ID_findEntPostDTOByEntId=RequestChannel.getChannelUniqueID(), this);
		}
	}

	public void loadData(long ent_id){
		this.entId = ent_id;
		loadData();
	}


	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findEntPostDTOByEntId)
				|| reqId.equals(kREQ_ID_findEntPostDTOByFairId)) {
			if (result != null) {
				postsPage = (Page<JobEntPostDTO>) result;
				if (postsPage.getCurrentPage() == 1) {
					postsAdapter.updateItems(postsPage.getElements());
				} else {
					postsAdapter.addToBottom(postsPage.getElements());
				}
			}

			emptyView.isEmpty(!(postsAdapter.getCount()>0));
		}

	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@UiThread
	void onComplete() {
		postsListView.onRefreshComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException)error, this);
	}

	/**
	 *
	 * @author yangjiannan
	 *
	 */
	class MorePostsAdapter extends BaseAdapter {

		public void updateItems(List<JobEntPostDTO> comments) {
			posts.clear();
			posts.addAll(comments);
			notifyDataSetChanged();
		}

		public void addToBottom(List<JobEntPostDTO> comments) {
			posts.addAll(comments);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return posts.size();
		}

		@Override
		public Object getItem(int position) {
			return posts.get(position).getPostId();
		}

		@Override
		public long getItemId(int position) {
			return posts.get(position).getPostId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder h = null;
			if (convertView == null) {
				h = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.more_post_item, null);
				h.postNameText = (TextView) convertView.findViewById(R.id.postNameText);
				h.publishDateText = (TextView) convertView.findViewById(R.id.publishDateText);
				h.degreeText = (TextView) convertView.findViewById(R.id.degreeText);
				h.paymentText = (TextView) convertView.findViewById(R.id.paymentText);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}

			JobEntPostDTO post = posts.get(position);
			h.postNameText.setText(post.getPostAliases());
			h.publishDateText.setVisibility(View.VISIBLE);
			h.publishDateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(post.getPublishDate()));
			h.degreeText.setText(post.getDegree());
			h.paymentText.setText(post.getSalary());
			if (position == selected) {
				convertView.setBackgroundColor(Color.WHITE);
			}else{
				convertView.setBackgroundResource(R.drawable.list_item_grey_selector);
			}
			return convertView;
		}

		class ViewHolder {
			TextView postNameText;
			TextView publishDateText;
			TextView paymentText;
			TextView degreeText;

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
		if (postsPage == null) {
			currentPage = 1;
		} else {
			if (postsPage.hasNextPage()) {
				currentPage = postsPage.getNextPage();
			} else {
				onComplete();
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		loadData();
	}

}
