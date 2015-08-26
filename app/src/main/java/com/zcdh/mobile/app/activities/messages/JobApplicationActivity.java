/**
 * 
 * @author jeason, 2014-6-14 下午12:02:06
 */
package com.zcdh.mobile.app.activities.messages;

import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobApplyDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.personal.InfoAdapter;
import com.zcdh.mobile.app.views.EmptyTipView;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author jeason, 2014-6-14 下午12:02:06
 * 职位申请列表
 */
@EActivity(R.layout.listview)
public class JobApplicationActivity extends BaseActivity implements IXListViewListener , 
		RequestListener, OnItemClickListener, DataLoadInterface {

	@ViewById(R.id.listview)
	XListView listview;

	private InfoAdapter adapter;

	private IRpcJobUservice userService;

	Page<JobApplyDTO> applyListDto;

	int currentPage = 1;
	private boolean hasNextPage;

	private final int pageSize = 10;

	private String kREQ_ID_FINDUSERAPPLY;
	
	@ViewById(R.id.emptyView)
	EmptyTipView emptyTipView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "职位跟踪");
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		adapter = new InfoAdapter(this);
	}

	@AfterViews
	void bindViews() {
		listview.setAdapter(adapter);
		listview.setEmptyView(emptyTipView);
		listview.setPullRefreshEnable(false);
		listview.setPullLoadEnable(true);
		listview.setAutoLoadEnable(false);
		listview.setXListViewListener(this);
		listview.setDivider(null);
		listview.setDividerHeight((int) getResources().getDimension(R.dimen.dividerHeightXX));
		listview.setOnItemClickListener(this);
		getApplications();
	}
	
	public void loadData(){
		getApplications();
	}


	/**
	 * 
	 * @author jeason, 2014-5-19 下午5:06:51
	 */
	@Background
	void getApplications() {
//		userService.findUserApply(ZcdhApplication.getInstance().getZcdh_uid(), currentPage, pageSize).identify(kREQ_ID_FINDUSERAPPLY = RequestChannel.getChannelUniqueID(), this);
		userService.findJobApplyDTOByUserId(getUserId(), currentPage, pageSize).identify(kREQ_ID_FINDUSERAPPLY = RequestChannel.getChannelUniqueID(), this);
		currentPage++;
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		if (hasNextPage) {
			loadData();
		} else {
			onComplete();
		}		
	}

	@UiThread
	void onComplete() {
		listview.stopLoadMore();
	}


	@Override
	public void onRequestStart(String reqId) {

	}


	@Override
	public void onRequestSuccess(String reqId, Object result) {

		if (reqId.equals(kREQ_ID_FINDUSERAPPLY)) {
			if (result != null) {
				applyListDto = (Page<JobApplyDTO>) result;

				if (currentPage == 2) {
					adapter.updateAllItems(applyListDto.getElements());
				} else {
					adapter.addToBottom(applyListDto.getElements());
				}
				hasNextPage = applyListDto.hasNextPage();
				listview.setPullLoadEnable(hasNextPage);
			}
			
			emptyTipView.isEmpty(adapter.getCount()==0);

		}
		onComplete();

	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyTipView.showException(((ZcdhException)error).getErrCode(), this);
		onComplete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		ActivityDispatcher.toTrackPostApplication(this, ((JobApplyDTO) adapter.getItem(arg2 - 1)).getPostId());
	}
}
