/**
 * 
 * @author jeason, 2014-5-20 上午9:17:29
 */
package com.zcdh.mobile.app.activities.personal;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobInterviewListDTO;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;

/**
 * @author jeason, 2014-5-19 下午3:02:35
 * @deprecated
 * 面试邀请列表
 */
@EFragment(R.layout.listview)
public class InterviewFm extends BaseFragment implements OnRefreshListener2<ListView>, RequestListener {

	private static final String TAG = InterviewFm.class.getSimpleName();
	@ViewById(R.id.listview)
	PullToRefreshListView listview;

	private InfoAdapter adapter;

	private IRpcJobUservice userService;

	Page<JobInterviewListDTO> interViewListDto;
	EmptyTipView emptyTipView;

	int currentPage = 1;

	private final int pageSize = 10;

	private String kREQ_ID_FINDUSERINTERVIEW;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		adapter = new InfoAdapter(getActivity());
		emptyTipView = new EmptyTipView(getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}
	
	@AfterViews
	void initViews() {
		getInterView();

		listview.setAdapter(adapter);
		listview.setMode(Mode.BOTH);
		listview.setOnRefreshListener(this);
		listview.setEmptyView(emptyTipView);

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		currentPage = 1;
		getInterView();
	}

	/**
	 * 
	 * @author jeason, 2014-5-19 下午5:06:51
	 */
	@Background
	void getInterView() {
		userService.findUserInterview(ZcdhApplication.getInstance().getZcdh_uid(), currentPage, pageSize)
		.identify(kREQ_ID_FINDUSERINTERVIEW=RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		currentPage = 1;
		if (interViewListDto == null) {
			currentPage = 1;
		} else if (interViewListDto.hasNextPage()) {
			currentPage++;
		} else {
			onComplete();
			return;
		}
		getInterView();
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	void onComplete() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listview.onRefreshComplete();
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		if (reqId.equals(kREQ_ID_FINDUSERINTERVIEW)) {
			if (result != null) {
				interViewListDto = (Page<JobInterviewListDTO>) result;

				if (currentPage == 1) {
					adapter.updateAllItems(interViewListDto.getElements());
				} else {
					adapter.addToBottom(interViewListDto.getElements());
				}

			}

		}
		onComplete();

	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		onComplete();
	}

}
