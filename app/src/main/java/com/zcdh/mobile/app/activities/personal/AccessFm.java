/**
 * 
 * @author jeason, 2014-5-20 上午9:17:29
 */
package com.zcdh.mobile.app.activities.personal;

/**
 * 
 * @author jeason, 2014-5-19 下午3:02:35
 * 谁访问我
 */

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;

import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobEntAccessListDTO;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;

/**
 * @author jeason, 2014-5-19 下午3:02:35
 */
@EFragment(R.layout.listview)
public class AccessFm extends BaseFragment implements IXListViewListener , RequestListener {

	private static final String TAG = AccessFm.class.getSimpleName();
	
	@ViewById(R.id.listview)
	XListView listview;

	private InfoAdapter adapter;

	private IRpcJobUservice userService;

	Page<JobEntAccessListDTO> accessListDto;
	
	@ViewById(R.id.emptyView)
	EmptyTipView emptyTipView;

	int currentPage = 1;

	private final int pageSize = 10;

	private String kREQ_ID_FINDENTACCESS;

	private boolean hasNextPage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		adapter = new InfoAdapter(getActivity());
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
	
		listview.setAdapter(adapter);
		listview.setPullRefreshEnable(false);
		listview.setPullLoadEnable(true);
		listview.setAutoLoadEnable(false);
		listview.setXListViewListener(this);
		listview.setEmptyView(emptyTipView);
		
		getAccess();

	}
	
	@Override
	public void onLoadMore() {
		if (hasNextPage) {
			getAccess();
		} else {
			onComplete();
		}		
	}

	@UiThread
	void onComplete() {
		listview.stopLoadMore();
	}


	/**
	 * 
	 * @author jeason, 2014-5-19 下午5:06:51
	 */
	@Background
	void getAccess() {
		userService.findEntAccess(ZcdhApplication.getInstance().getZcdh_uid(), currentPage, pageSize)
		.identify(kREQ_ID_FINDENTACCESS=RequestChannel.getChannelUniqueID(), this);
		currentPage++;
	}


	@Override
	public void onRequestStart(String reqId) {

	}

	

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		if (reqId.equals(kREQ_ID_FINDENTACCESS)) {
			if (result != null) {
				accessListDto = (Page<JobEntAccessListDTO>) result;
				if (currentPage == 1) {
					adapter.updateAllItems(accessListDto.getElements());
				} else {
					adapter.addToBottom(accessListDto.getElements());
				}
				hasNextPage = accessListDto.hasNextPage();
				listview.setPullLoadEnable(hasNextPage);
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		onComplete();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

}
