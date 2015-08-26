/**
 * 
 * @author jeason, 2014-7-11 上午11:30:09
 */
package com.zcdh.mobile.app.activities.messages;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobInterviewDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-7-11 上午11:30:09 面试邀请详情
 */
@EActivity(R.layout.interview_invitation)
public class InterviewInvitationActivity extends BaseActivity implements
		RequestListener {
	@ViewById(R.id.tvTitle)
	TextView tvTitle;

	@ViewById(R.id.tvEntname)
	TextView tvEntname;

	@ViewById(R.id.tvTime)
	TextView tvTime;

	@ViewById(R.id.content)
	TextView content;

	@ViewById(R.id.postNameText)
	TextView postNameText;
	
	@ViewById(R.id.llpost_item)
	LinearLayout llpost_item;

	@Extra
	long interViewId = 0l;

	private IRpcJobUservice userService;

	private String kREQ_ID_FINDUSERINTERVIEWDETAIL;

	private JobInterviewDTO interViewDto;
	private LoadingIndicator loading;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),
				"面试邀请");
		userService = RemoteServiceManager.getRemoteService(
				IRpcJobUservice.class, this);
		loading = new LoadingIndicator(this);
	}

	@AfterViews
	void afterViews() {
		loading.show();
		loadData();
	}

	@Background
	void loadData() {
		userService
				.findUserInterviewDetail(interViewId)
				.identify(
						kREQ_ID_FINDUSERINTERVIEWDETAIL = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestStart(java.lang
	 * .String)
	 */
	@Override
	public void onRequestStart(String reqId) {
		loading.show();
	}

	@Click(R.id.llpost_item)
	void onClickDetail() {
			ActivityDispatcher.toDetailsFrameActivity(this,
					interViewDto.getPostId(), false, null, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestSuccess(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_FINDUSERINTERVIEWDETAIL)) {
			if (result != null) {
				interViewDto = (JobInterviewDTO) result;
				initView();
			}
		}
	}

	/**
	 * 
	 * @author jeason, 2014-7-11 下午1:45:58
	 */
	private void initView() {
		tvTitle.setText(interViewDto.getTitle());
		tvEntname.setText(interViewDto.getEntName());
		tvTime.setText(DateUtils.getDateByFormatYMDHMS(interViewDto
				.getSendTime()));
		content.setText(interViewDto.getContent());
		postNameText.setText(interViewDto.getPostName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestFinished(java.
	 * lang.String)
	 */
	@Override
	public void onRequestFinished(String reqId) {
		loading.dismiss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestError(java.lang
	 * .String, java.lang.Exception)
	 */
	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		loading.dismiss();
		llpost_item.setClickable(false);
		Toast.makeText(this, getResources().getString(R.string.service_error),
				Toast.LENGTH_SHORT).show();
	}
}
