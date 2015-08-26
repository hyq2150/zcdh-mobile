/**
 * 
 * @author jeason, 2014-6-16 下午4:18:46
 */
package com.zcdh.mobile.app.activities.messages;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.InformationDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.ent.MainEntLiuyanActivity_;
import com.zcdh.mobile.app.activities.personal.InfoAdapter;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * @author jeason, 2014-6-16 下午4:18:46 企业回信列表
 */
@EActivity(R.layout.activity_msg_from_ent)
public class MessageFromEntActivity extends BaseActivity implements
		RequestListener, OnItemClickListener, OnRefreshListener2<ListView>,
		DataLoadInterface {

	private IRpcNearByService nearbyService;

	@ViewById(R.id.listview)
	PullToRefreshListView ptlListView;

	private int currentPage = 1;

	private final int PageSize = 10;

	private long msg_id;

	private String K_REQ_ID_FINDSUBINFORMATIONLISTBYID;

	private Page<InformationDTO> infos;

	private InfoAdapter adapter;

	private EmptyTipView emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),
				"企业回信");
		msg_id = getIntent().getLongExtra("id", 0l);
		nearbyService = RemoteServiceManager
				.getRemoteService(IRpcNearByService.class);
	}

	@AfterViews
	void bindViews() {
		emptyView = new EmptyTipView(this);

		ptlListView.getRefreshableView().setDivider(null);
		ptlListView.getRefreshableView().setDividerHeight(
				(int) getResources().getDimension(R.dimen.dividerHeightXX));
		ptlListView.setMode(Mode.BOTH);
		ptlListView.setOnItemClickListener(this);
		ptlListView.setOnRefreshListener(this);
		ptlListView.setEmptyView(emptyView);
		adapter = new InfoAdapter(this);
		ptlListView.setAdapter(adapter);

		loadData();
	}

	@Background
	public void loadData() {
		nearbyService
				.findSubInformationListById(msg_id, getUserId(), currentPage,
						PageSize)
				.identify(
						K_REQ_ID_FINDSUBINFORMATIONLISTBYID = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(K_REQ_ID_FINDSUBINFORMATIONLISTBYID)) {
			if (result != null) {
				infos = (Page<InformationDTO>) result;
				if (infos.getCurrentPage() == 1) {
					adapter.updateAllItems(infos.getElements());
				} else {
					adapter.addToBottom(infos.getElements());
				}

				// checkUnRead();
			}
		}
		emptyView
				.isEmpty(!(infos != null && infos.getElements() != null && infos
						.getElements().size() > 0));

	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException(((ZcdhException) error).getErrCode(), this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		InformationDTO info = (InformationDTO) adapter.getItem(position - 1);
		HashMap<String, String> params = StringUtils.getParams(info
				.getCustomParam());
		String action_code = params.get("skipActionCode");
		long entId = 0l;
		if (params.get("entId") != null) {

			entId = Long.parseLong(params.get("entId"));
		}
		// 006.001为留言 006.002为咨询 006.003为纠错
		if (action_code.equals("006.001")) {
			ActivityDispatcher.toEntMsg(this, entId, "001");
		}
		if (action_code.equals("006.002")) {
			if (params.get("postId") != null) {
				try {
					long postId = Long.parseLong(params.get("postId"));
					String postName = params.get("postName");
					MainEntLiuyanActivity_.intent(this).postId(postId)
							.type("002").postName(postName).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (action_code.equals("006.003")) {
			// ActivityDispatcher.toMessageJiucuo(this, entId);
			ActivityDispatcher.toEntMsg(this, entId, Constants.kTYPE_JIUCUO);
		}
		if (action_code.equals("006.004")) {
			long interViewId = Long.parseLong(params.get("id"));
			ActivityDispatcher.toInterviewDetail(this, interViewId);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		getLatest();
		onComplete();

	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:07
	 */
	private void getLatest() {
		currentPage = 1;
		loadData();
		onComplete();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		getMore();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:05
	 */
	private void getMore() {
		if (infos == null) {
			currentPage = 1;
		} else {
			if (infos.hasNextPage()) {
				currentPage = infos.getNextPage();
			} else {
				onComplete();
				Toast.makeText(this, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();

				return;
			}
		}
		loadData();
		onComplete();
	}

	@UiThread
	void onComplete() {
		ptlListView.onRefreshComplete();
	}

}
