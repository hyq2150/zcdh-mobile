/**
 * 
 * @author jeason, 2014-6-19 上午10:00:14
 */
package com.zcdh.mobile.app.activities.job_fair;

import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.JobFairPost;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.base.PaginableFm;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.utils.SharedPreferencesUtil;

import org.androidannotations.api.BackgroundExecutor;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-6-19 上午10:00:14 招聘会岗位列表Fragment
 */
public class PostListFragment extends PaginableFm<JobFairPost> {

	private static final String TAG = PostListFragment.class.getSimpleName();

	private IRpcJobFairService jobfairService;

	private LayoutInflater inflater;

	private final int PageSize = 15;

	private List<JobEntPostDTO> posts = new ArrayList<JobEntPostDTO>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		is_searchable = true;
		super.onCreate(savedInstanceState);
		jobfair_id = getArguments().getLong("jobfair_id", 0l);
		Log.e(TAG, "jobfair_id:" + jobfair_id);
		inflater = LayoutInflater.from(getActivity());
		jobfairService = RemoteServiceManager
				.getRemoteService(IRpcJobFairService.class);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		et_keyword.setHint("请输入职位名 或 企业名搜索职位");
		return view;
	}

	@Override
	public void getData(final int current_page, final String reqId,
			final String keyword, final String code, final String name) {
		super.getData(current_page, reqId, keyword, code, name);
		BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {

			@Override
			public void execute() {
				try {
					jobfairService.findJobFairPostByPostTypeCode(jobfair_id, keyword, code, currentPage, PageSize).identify(reqId,
							PostListFragment.this); 
					currentPage++;
				} catch (Throwable e) {
					Thread.getDefaultUncaughtExceptionHandler()
							.uncaughtException(Thread.currentThread(), e);
				}
			}

		});
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(K_REQ_GETPAGE)) {
			if (result != null) {
				Page<JobFairPost> page = (Page<JobFairPost>) result;
				if (page.getCurrentPage() == 1) {
					adapter.updateItems(page.getElements());
				} else {
					adapter.addToBottom(page.getElements());
				}
				for (int i = 0; i < page.getElements().size(); i++) {
					JobFairPost post = (JobFairPost) (page.getElements().get(i));
					JobEntPostDTO post2 = new JobEntPostDTO();
					post2.setPostId(post.getPostId());
					post2.setEntId(post.getEntId());
					post2.setPostAliases(post.getPostName());
					posts.add(post2);
				}
				hasNextPage = page.hasNextPage();
				listview.setPullLoadEnable(hasNextPage);
				
				
			}
			emptyView.isEmpty(adapter.getCount() == 0);
		}
		
	}

	@Override
	public View getView(View convertView, JobFairPost item) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.jobfair_post_item, null);
			holder.ent_name = (TextView) convertView
					.findViewById(R.id.ent_name);
			holder.post_name = (TextView) convertView
					.findViewById(R.id.post_name);
			holder.placenum_text = (TextView) convertView
					.findViewById(R.id.placeNumText);
			holder.infoText = (TextView) convertView
					.findViewById(R.id.infoText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ent_name.setText(item.getEntName());
		holder.post_name.setText(item.getPostNameTemp());
		if (!TextUtils.isEmpty(item.getBoothNo())) {
			holder.placenum_text.setText(item.getBoothNo());
			holder.placenum_text.setVisibility(View.VISIBLE);
			holder.infoText.setText("展位");
		} else {
			holder.infoText.setText("在线招聘");
			holder.placenum_text.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView post_name;
		TextView ent_name;
		TextView placenum_text;
		TextView infoText;
	}

	@Override
	public void onItemClickWithItem(View view, JobFairPost item, int position) {
		SharedPreferencesUtil.putValue(getActivity(),Constants.JOBFAIR_ID_KEY, jobfair_id);
		DetailsFrameActivity_.intent(getActivity()).postId(item.getPostId())
				.switchable(true).currentIndex(position - 1).posts(posts)
				.entId(item.getEntId()).start();
	}

	public void loadFilterData() {
		dropdownFilterView.loadData(jobfair_id, 0);
	}

	@Override
	public void onPageSelected(int page) {
		if (page != 2 && dropdownFilterView.getVisibility() == View.VISIBLE)
			displayFilterView();
	}
}
