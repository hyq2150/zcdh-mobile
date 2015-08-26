/**
 * 
 * @author jeason, 2014-6-19 上午9:59:40
 */
package com.zcdh.mobile.app.activities.job_fair;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.JobFairEnt;
import com.zcdh.mobile.app.activities.base.PaginableFm;
import com.zcdh.mobile.app.activities.ent.MainEntActivity_;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;

import org.androidannotations.api.BackgroundExecutor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jeason, 2014-6-19 上午9:59:40 可分页企业列表
 */
public class EnterpriseListFragment extends PaginableFm<JobFairEnt> {

	private IRpcJobFairService jobfairService;
	private long jobfair_id;
	private LayoutInflater inflater;
	private final int PageSize = 10;

	private DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		is_searchable = true;
		super.onCreate(savedInstanceState);
		jobfair_id = getArguments().getLong("jobfair_id", 0l);
		inflater = LayoutInflater.from(getActivity());
		jobfairService = RemoteServiceManager
				.getRemoteService(IRpcJobFairService.class);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
	}

	@Override
	public void getData(final int current_page, final String reqId,
			final String keyword, final String code, final String name) {
		super.getData(current_page, reqId, keyword, code, name);
		BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {

			@Override
			public void execute() {
				try {
					jobfairService.findJobFairEntByIndustryCode(jobfair_id,
							keyword, code, currentPage, PageSize).identify(
							K_REQ_GETPAGE, EnterpriseListFragment.this);
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
				Page<JobFairEnt> page = (Page<JobFairEnt>) result;
				if (page.getCurrentPage() == 1) {
					adapter.updateItems(page.getElements());
				} else {
					adapter.addToBottom(page.getElements());
				}
				hasNextPage = page.hasNextPage();
				listview.setPullLoadEnable(hasNextPage);
			}
			emptyView.isEmpty(adapter.getCount() == 0);
		}

	}

	@Override
	public View getView(View convertView, JobFairEnt item) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.enterprise_item, null);
			holder.ent_name = (TextView) convertView
					.findViewById(R.id.ent_name);
			holder.entlogoIm = (ImageView) convertView
					.findViewById(R.id.entLogoIm);
			holder.placenumText = (TextView) convertView
					.findViewById(R.id.placeNumText);
			holder.infoText = (TextView) convertView
					.findViewById(R.id.infoText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ent_name.setText(item.getEntNameTemp());
		if (!TextUtils.isEmpty(item.getBoothNo())) {
			holder.placenumText.setText(item.getBoothNo());
			holder.placenumText.setVisibility(View.VISIBLE);
			holder.infoText.setText("展位");
		} else {
			holder.infoText.setText("在线招聘");
			holder.placenumText.setVisibility(View.GONE);
		}
		if (item.getEntLogo() != null
				&& !TextUtils.isEmpty(item.getEntLogo().getBig())) {
			ImageLoader.getInstance().displayImage(item.getEntLogo().getBig(),
					holder.entlogoIm, options);
		} else {
			holder.entlogoIm.setImageResource(R.drawable.companylogo);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView ent_name;
		ImageView entlogoIm;
		TextView placenumText;
		TextView infoText;

	}

	@Override
	public void onItemClickWithItem(View view, JobFairEnt item, int position) {
		// ActivityDispatcher.toEntMain(getActivity(), item.getEntId(),0);
		MainEntActivity_.intent(this).entId(item.getEntId())
				.jobfair_id(jobfair_id).default_index(0).start();
	}

	public void loadFilterData() {
		dropdownFilterView.loadData(jobfair_id, 1);
	}

	public void onFilterProccess(String code, String name) {

	}

	@Override
	public void onPageSelected(int page) {
		if (page != 1 && dropdownFilterView.getVisibility() == View.VISIBLE)
			displayFilterView();
	}

}
