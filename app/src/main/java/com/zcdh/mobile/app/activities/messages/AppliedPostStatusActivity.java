/**
 * 
 * @author jeason, 2014-6-17 上午10:59:56
 */
package com.zcdh.mobile.app.activities.messages;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.TrackPostDTO;
import com.zcdh.mobile.api.model.TrackPostDetailDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-6-17 上午10:59:56
 * 职位申请跟踪详情
 */
@EActivity(R.layout.job_application_status)
public class AppliedPostStatusActivity extends BaseActivity implements RequestListener, DataLoadInterface {
	@ViewById(R.id.listview)
	PullToRefreshListView ptrListview;

	EmptyTipView emptyView;

	IRpcNearByService nearbyService;

	View headerView;


	LayoutInflater inflater;

	long postId;

	private String K_REQ_ID_findTrackPostDetail;

	TrackPostDetailDTO postDetail;

	ApplicationStatusesAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nearbyService = RemoteServiceManager.getRemoteService(IRpcNearByService.class);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "职位跟踪");
		postId = getIntent().getLongExtra("postId", -1);
		emptyView = new EmptyTipView(this);
		inflater = LayoutInflater.from(this);
	}

	@AfterViews
	void afterViews() {
		ptrListview.setMode(Mode.DISABLED);
		adapter = new ApplicationStatusesAdapter();
		
		headerView = inflater.inflate(R.layout.post_item, null);
		
		loadData();
	}

	/**
	 * 
	 * @author jeason, 2014-7-15 下午5:45:22
	 */
	protected void toPostDetail() {
		ActivityDispatcher.toDetailsFrameActivity(this, postId, false, null, 0);
	}

	@Background
	public void loadData() {
		nearbyService.findTrackPostDetail(getUserId(), postId).identify(K_REQ_ID_findTrackPostDetail = RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public void onRequestStart(String reqId) {
		
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(K_REQ_ID_findTrackPostDetail)) {
			if (result != null) {
				postDetail = (TrackPostDetailDTO) result;
				initData();
				ptrListview.setAdapter(adapter);
			}
			emptyView.isEmpty(!(adapter.getCount()>0));
		}
	}

	private void initData() {
		TextView title = (TextView) headerView.findViewById(R.id.title);
		title.setText(postDetail.getPostName());
		TextView publish_time = (TextView) headerView.findViewById(R.id.publish_time);
		publish_time.setText(DateUtils.getDateByFormatYMD(postDetail.getPublishDate()));
		TextView content = (TextView) headerView.findViewById(R.id.content);
		content.setText(postDetail.getEntName());
		TextView salary = (TextView) headerView.findViewById(R.id.salary);
		salary.setText(postDetail.getSalary());
		TextView location_and_education = (TextView) headerView.findViewById(R.id.location_and_education_and_matchrate);
		location_and_education.setText(String.format("%s/%s", postDetail.getPostAddress(), postDetail.getDegreeName()));
		TextView distance = (TextView) headerView.findViewById(R.id.distance);
		distance.setVisibility(View.GONE);
		// LinearLayout ll_tags_container = (TagsContainer)
		// headerView.findViewById(R.id.ll_tags);
		// LinearLayout.LayoutParams lp = new
		// LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// lp.setMargins(5, 0, 0, 0);
		// if (postDetail.get != null) {
		// if( mPosts.get(position).getTagNames().size()<3){
		//
		// ll_tags_container.initData(context,
		// mPosts.get(position).getTagNames().subList(0,
		// mPosts.get(position).getTagNames().size()), null, Gravity.RIGHT);
		// }else{
		//
		// ll_tags_container.initData(context,
		// mPosts.get(position).getTagNames().subList(0, 3), null,
		// Gravity.RIGHT);
		// }
		// }
		ptrListview.getRefreshableView().addHeaderView(headerView, null, false);
		RelativeLayout tv = (RelativeLayout) inflater.inflate(R.layout.textview_track_status, null);
		tv.findViewById(R.id.postDetailBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				toPostDetail();
			}
		});
		ptrListview.getRefreshableView().addHeaderView(tv, null, false);
		adapter.updateItems(postDetail.getTarckPosts());
	}

	@Override
	public void onRequestFinished(String reqId) {
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException)error, this);
	}

	private class ApplicationStatusesAdapter extends BaseAdapter {
		List<TrackPostDTO> trackDtos = new ArrayList<TrackPostDTO>();

		public void updateItems(List<TrackPostDTO> items) {

			trackDtos.clear();
			if (items != null) trackDtos.addAll(items);
		}

		@Override
		public int getCount() {
			return trackDtos.size();
		}

		@Override
		public TrackPostDTO getItem(int position) {
			return trackDtos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.application_status_item, null);
				holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
				holder.time = (TextView) convertView.findViewById(R.id.tv_publish_time);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_status.setText(getItem(position).getTrackContent());
			holder.time.setText(DateUtils.getDateByFormatYMDHM(getItem(position).getTrackDate()));
			return convertView;
		}
	}

	private class ViewHolder {
		public TextView tv_status;
		public TextView time;
	}
}
