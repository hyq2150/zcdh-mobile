package com.zcdh.mobile.app.activities.job_fair;

import java.lang.ref.WeakReference;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.api.model.JobFairDetailExtDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.qrcode.QRScanActivity;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.share.Share;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 招聘会详情
 */
@EActivity(R.layout.job_fair_detail_new)
public class JobFairDetailActivityNew extends BaseActivity implements
		RequestListener, OnClickListener, OnRefreshListener<ScrollView> {

	public static final int ACTION_START_FRO_RESULT_CODE = 1001;
	@ViewById(R.id.refreshScrollview)
	PullToRefreshScrollView refreshScrollview;
	// 招聘会状态
	@ViewById(R.id.iv_status)
	ImageView iv_status;
	// 招聘会类别
	@ViewById(R.id.fair_category)
	TextView fair_category;
	// 招聘会标题
	@ViewById(R.id.fair_title)
	TextView fair_title;
	// 招聘会时间
	@ViewById(R.id.fair_time)
	TextView fair_time;
	// 招聘会地址
	@ViewById(R.id.fair_address)
	TextView fair_address;
	// 查看参会流程
	@ViewById(R.id.view_the_process)
	Button view_the_process;
	// 更多招聘会详情
	@ViewById(R.id.more_recruitment_details)
	Button more_recruitment_details;
	// 报名
	@ViewById(R.id.fair_sign_up)
	Button fair_sign_up;
	// 签到
	@ViewById(R.id.fair_sign)
	Button fair_sign;
	// 参会职位
	@ViewById(R.id.participants_position)
	Button participants_position;
	// 参会企业
	@ViewById(R.id.participants_enterprises)
	Button participants_enterprises;
	// 扫一扫投简历
	@ViewById(R.id.sweep_resume)
	Button sweep_resume;
	// 面试结果
	@ViewById(R.id.interview_results)
	Button interview_results;
	// 实时数据
	@ViewById(R.id.real_time_data)
	Button real_time_data;
	// 企业登录
	@ViewById(R.id.enterprise_login)
	Button enterprise_login;

	private boolean joined = false;
	private String fair_status;
	private List<JobEntShareDTO> shareContents;
	private String K_REQ_ID_SHARE_CONTENT;
	private String K_REQ_ID_JOININ;
	private String K_REQ_ID_findJobFairDetailExtByFairId;
	private IRpcJobFairService jobfairService;
	private Long jobfair_id;
	private JobFairDetailExtDTO jobFairDetail;
	private LoadingIndicator loading;
	private ViewTheProcessDialog viewTheProcess;
	private String signURL;
	private final MyHandler handler = new MyHandler(this);
	private static final int handlerSuccess = 1001;
	private static final int handlerFair = 1002;
	private static final int handlerSign = 1003;

	private static class MyHandler extends Handler {
		private final WeakReference<JobFairDetailActivityNew> mActivity;

		public MyHandler(JobFairDetailActivityNew activity) {
			super();
			// TODO Auto-generated constructor stub
			mActivity = new WeakReference<JobFairDetailActivityNew>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			JobFairDetailActivityNew activity = mActivity.get();
			switch (msg.what) {
			case handlerSuccess:
				activity.loading.show();
				activity.getDetail();
				break;
			case handlerFair:
				activity.loading.dismiss();
				Toast.makeText(activity, "签到失败", Toast.LENGTH_SHORT).show();
				break;
			case handlerSign:
				activity.loading.dismiss();
				Toast.makeText(activity, "你已签到", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		jobfair_id = Long.valueOf(getIntent().getExtras().getString(
				Constants.JOBFAIR_ID_KEY));
		fair_status = getIntent().getExtras().getString(Constants.FAIR_STATUS);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),
				"招聘会");
		jobfairService = RemoteServiceManager
				.getRemoteService(IRpcJobFairService.class);
		loading = new LoadingIndicator(this);
	}

	@AfterViews
	void bindView() {
		refreshScrollview.setOnRefreshListener(this);
		setClickListener();
		loading.show();
		getDetail();
	}

	private void setClickListener() {
		fair_address.setOnClickListener(this);
		view_the_process.setOnClickListener(this);
		more_recruitment_details.setOnClickListener(this);
		fair_sign_up.setOnClickListener(this);
		fair_sign.setOnClickListener(this);
		participants_position.setOnClickListener(this);
		participants_enterprises.setOnClickListener(this);
		sweep_resume.setOnClickListener(this);
		interview_results.setOnClickListener(this);
		real_time_data.setOnClickListener(this);
		enterprise_login.setOnClickListener(this);
	}

	/**
	 * 获取招聘会类型 0 社会招聘，1 校园招聘，2 其他, 3 假期工
	 */
	private String getFairCategory(int category) {
		String categoryStr = getResources().getString(R.string.fair_category)
				+ "  :  ";
		switch (category) {
		case 0:
			categoryStr = categoryStr
					+ getResources().getString(R.string.social_recruitment);
			break;
		case 1:
			categoryStr = categoryStr
					+ getResources().getString(R.string.campus_recruitment);
		case 2:
			categoryStr = categoryStr
					+ getResources().getString(R.string.other_recruitment);
			break;
		case 3:
			categoryStr = categoryStr
					+ getResources().getString(R.string.holiday_recruitment);
			break;
		default:
			break;
		}
		return categoryStr;
	}

	/**
	 * 招聘会状态 0: 不通过 ，1：生效，上线，-1：审核中,2:草稿中 3：失效，暂停，4：已关闭 5：已结束
	 * 
	 * @param status
	 * @return
	 */
	private int getFairStatusIMG(int status) {
		Log.e("Job", "status : " + status);
		switch (status) {
		case 1:
			return R.drawable.fairs_status_ing;
		case 3:
			return R.drawable.fairs_status_stop;
		case 4:
			return R.drawable.fairs_status_close;
		case 5:
			return R.drawable.fairs_status_end;
		}
		return status;
	}

	private void initViews() {
		fair_title.setText(jobFairDetail.getTitle());
		fair_category.setText(getFairCategory(jobFairDetail.getFairType()));
		Log.e("Job", jobFairDetail.getTimeRange());
		fair_time.setText(jobFairDetail.getTimeRange());
//		setFairTime(jobFairDetail.getStartTime(), jobFairDetail.getEndTime());
		fair_address.setText(jobFairDetail.getAddress());
		fair_status = String.valueOf(jobFairDetail.getStatus());
		iv_status.setImageResource(getFairStatusIMG(jobFairDetail.getStatus()));
		participants_position.setText(getSpannableStringBuilder(getResources()
				.getString(R.string.participants_position)
				+ "  "
				+ jobFairDetail.getJoinPostCount() + " 个"));
		participants_enterprises
				.setText(getSpannableStringBuilder(getResources().getString(
						R.string.participants_enterprises)
						+ "  " + jobFairDetail.getJoinEntCount() + " 家"));

		initJoinBtn();
	}

	private SpannableStringBuilder getSpannableStringBuilder(String txt) {
		SpannableStringBuilder builder = new SpannableStringBuilder(txt);
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(this
				.getResources().getColor(R.color.participants));
		builder.setSpan(greenSpan, 4, txt.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	@Background
	public void getDetail() {
		// TODO Auto-generated method stub
		jobfairService
				.findJobFairDetailExtByFairId(jobfair_id,
						ZcdhApplication.getInstance().getZcdh_uid())
				.identify(
						K_REQ_ID_findJobFairDetailExtByFairId = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.share_select, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	private void initJoinBtn() {
		if (jobFairDetail.getIsSignIn() == 1) {
			fair_sign.setText(getResources().getString(R.string.signed));
		} else {
			fair_sign.setText(getResources().getString(R.string.sign));
		}

		if (joined) {
			fair_sign_up.setText(getResources().getString(
					R.string.jbo_you_have_signed_up));
		} else {
			fair_sign_up.setText(getResources().getString(R.string.sign_up));
		}
	}

	//报名
	void signUp() {
		if (!RegisterUtil.isRegisterUser(this)) {
			Toast.makeText(this, "尚未登录！", Toast.LENGTH_SHORT).show();
			ActivityDispatcher.to_login(this);
			return;
		}
		if (ZcdhApplication.getInstance().getJobUserResumeMiddleDTO() != null
				&& ZcdhApplication.getInstance().getJobUserResumeMiddleDTO().getIsCanSignUp() == 0) {
			if (ZcdhApplication.getInstance().getRequisiteDTO() != null
				&& ZcdhApplication.getInstance().getRequisiteDTO().getIsUserRequisiteFilled() == 0) {
				Toast.makeText(this, "资料尚未完整", Toast.LENGTH_SHORT).show();
				ActivityDispatcher.toBasicInfo(this);
				return;
			}
			Toast.makeText(this, "简历尚未完整", Toast.LENGTH_SHORT).show();
			ActivityDispatcher.toMyResumeActivity(this);
			return;
		}
		
		if (joined) {
			ActivityDispatcher.toRegisterListActivity(this, jobfair_id);
			return;
		} else {
			switch (Integer.valueOf(fair_status)) {
			case 1:
				doSignUp();
				break;
			default:
				Toast.makeText(this, "招聘会已暂停或结束，不允许报名", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}

	private void doSignIn(final String url) {
		loading.show();
		BackgroundExecutor.execute(new Task("", 0, "") {

			@Override
			public void execute() {
				// TODO Auto-generated method stub
				try {
					HttpGet httpRequest = new HttpGet(url);// 建立http get联机
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest);// 发出http请求
					Log.e("Job", "url : " + url);
					Log.e("Job", "httpResponse getStatusCode : " + httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(httpResponse
								.getEntity());// 获取相应的字符串
						Log.e("Job", "result : " + result);
						JSONObject jsonObject = new JSONObject(result);
						/**
						 * 0  签到成功 1  异常 2  已签到
						 */
						if (jsonObject.getInt("result") == 0) {
							handler.sendEmptyMessage(handlerSuccess);
						} else if(jsonObject.getInt("result") == 1){
							handler.sendEmptyMessage(handlerFair);
						}else if (jsonObject.getInt("result") == 2) {
							handler.sendEmptyMessage(handlerSign);
						}
					} else {
						handler.sendEmptyMessage(handlerFair);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					handler.sendEmptyMessage(handlerFair);
				}
			}
		});
	}

	@Background
	void doSignUp() {
		jobfairService.joinInFair(jobfair_id, getUserId()).identify(
				K_REQ_ID_JOININ = RequestChannel.getChannelUniqueID(), this);
	}

	@OptionsItem(R.id.action_share_select)
	void onShare() {
		SystemServicesUtils.initShareSDK(this);
		if (shareContents == null) {
			findShareContent();
		} else {
			Share.showShare(this, shareContents, false, null);
		}
	}

	@Background
	void findShareContent() {
		jobfairService.findFairShareContent(jobfair_id).identify(
				K_REQ_ID_SHARE_CONTENT = RequestChannel.getChannelUniqueID(),
				this);
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub
		loading.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		loading.dismiss();
		// TODO Auto-generated method stub
		if (reqId.equals(K_REQ_ID_SHARE_CONTENT)) {
			if (result != null) {
				shareContents = (List<JobEntShareDTO>) result;
				Share.showShare(this, shareContents, false, null);
			}
		}
		if (reqId.equals(K_REQ_ID_findJobFairDetailExtByFairId)) {
			if (result != null) {
				jobFairDetail = (JobFairDetailExtDTO) result;
				initViews();
				if (jobFairDetail.getIsSignUp() == 1) {
					joined = true;
				} else {
					joined = false;
				}
				initJoinBtn();
			}
		}
		if (reqId.equals(K_REQ_ID_JOININ)) {
			joined = true;
			initJoinBtn();
			ActivityDispatcher.toRegisterListActivity(this, jobfair_id);
			Toast.makeText(this,
					getResources().getString(R.string.job_sign_up_success),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		loading.dismiss();
		refreshScrollview.onRefreshComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		loading.dismiss();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ACTION_START_FRO_RESULT_CODE
				&& resultCode == RESULT_OK) {
			Log.e("job", "resultCode : " + resultCode);
			try {
				signURL = data.getStringExtra("URL");
				Log.e("Job", "signURL : " + signURL);
				if (signURL != null) {
					if (signURL.contains(Constants.URL_SCAN_FAIR_POST)) {//投简历
						openWebview(signURL, false,true);
					} else if (signURL.contains(Constants.URL_SIGNIN_FAIR)) {// 签到
						sign(signURL);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	//签到
	void sign(final String url) {
		if (!RegisterUtil.isRegisterUser(this)) {
			Toast.makeText(this, "尚未登录！", Toast.LENGTH_SHORT).show();
			ActivityDispatcher.to_login(this);
			return;
		}
		if (jobFairDetail.getIsSignIn() == 1) {
			Toast.makeText(this, "你已签到", Toast.LENGTH_SHORT).show();
			return;
		} else {
			switch (Integer.valueOf(fair_status)) {
			case 1:
				doSignIn(jobFairDetail.getSignInUrl());
				break;
			default:
				Toast.makeText(this, "招聘会已暂停或结束，不允许签到", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	}

	/**
	 * 扫一扫投简历
	 */
	private void sweep() {
		if (!RegisterUtil.isRegisterUser(this)) {
			Toast.makeText(this, "尚未登录！", Toast.LENGTH_SHORT).show();
			ActivityDispatcher.to_login(this);
			return;
		}
		if (ZcdhApplication.getInstance().getJobUserResumeMiddleDTO() != null
				&& ZcdhApplication.getInstance().getJobUserResumeMiddleDTO().getIsCanSignUp() == 0) {
			if (ZcdhApplication.getInstance().getRequisiteDTO() != null
				&& ZcdhApplication.getInstance().getRequisiteDTO().getIsUserRequisiteFilled() == 0) {
				Toast.makeText(this, "资料尚未完整", Toast.LENGTH_SHORT).show();
				ActivityDispatcher.toBasicInfo(this);
				return;
			}
			Toast.makeText(this, "简历尚未完整", Toast.LENGTH_SHORT).show();
			ActivityDispatcher.toMyResumeActivity(this);
			return;
		}
		Intent intent = new Intent(this, QRScanActivity.class);
		this.startActivityForResult(intent, ACTION_START_FRO_RESULT_CODE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sweep_resume:// 扫一扫投简历
			sweep();
			break;
		case R.id.fair_address:// 招聘会地址
			if (!TextUtils.isEmpty(jobFairDetail.getAddress())
					&& jobFairDetail.getLat() != null
					&& jobFairDetail.getLon() != null) {
				if (jobFairDetail.getLat() > 0 && jobFairDetail.getLon() > 0) {
					ActivityDispatcher.toNavigate(this, jobFairDetail.getLat(),
							jobFairDetail.getLon());
				} else {
					Toast.makeText(this, "经纬度数据出错", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "招聘会地址数据出错", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.view_the_process:// 查看参会流程
			Log.e("Fuck", "jobFairDetail.getJoinFlowUrl() : " + jobFairDetail.getJoinFlowUrl());
			if (jobFairDetail.getJoinFlowUrl() != null) {
				viewTheProcess(jobFairDetail.getJoinFlowUrl());
			} else {
				Toast.makeText(this, "参会流程图片为空", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.more_recruitment_details:// 更多招聘会详情
			openWebview(jobFairDetail.getMoreDetailUrl(), true,true);
			break;
		case R.id.fair_sign:// 签到 取签到方式 (1-按钮签到 2-扫码签到)
			Log.e("Job", "signInType : " + jobFairDetail.getSignInType());
			Log.e("Job", "signIn : " + jobFairDetail.getIsSignIn());
			if (jobFairDetail.getSignInType() != null) {
				switch (Integer.valueOf(jobFairDetail.getSignInType())) {
				case 1:
					sign(jobFairDetail.getSignInUrl());
					break;
				case 2:
					if (jobFairDetail.getIsSignIn() == 1) {
						Toast.makeText(this, "你已签到", Toast.LENGTH_SHORT).show();
						return;
					} else {
						sweep();
					}
					break;
				}
			}
			break;
		case R.id.fair_sign_up:// 报名
			signUp();
			break;
		case R.id.participants_position:// 参会职位
			Intent intent = new Intent(this,
					ParticipantsPositionActivity_.class);
			intent.putExtra("fairId", jobfair_id);
			startActivity(intent);
			break;
		case R.id.participants_enterprises:// 参会企业
			Intent intent0 = new Intent(this,
					ParticipantsEnterprisesActivity_.class);
			intent0.putExtra("fairId", jobfair_id);
			startActivity(intent0);
			break;
		case R.id.interview_results:// 面试结果
			Log.e("Job", "面试结果 : " + jobFairDetail.getInterviewResultUrl());
			openWebview(jobFairDetail.getInterviewResultUrl(), false,true);
			break;
		case R.id.real_time_data:// 实时数据
			Log.e("Job", "实时数据 : " + jobFairDetail.getRealDataUrl());
			openWebview(jobFairDetail.getRealDataUrl(), false,true);
			break;
		case R.id.enterprise_login:// 企业登录
			Log.e("Job", "企业登录 : " + jobFairDetail.getEntLoginUrl());
			openWebview(jobFairDetail.getEntLoginUrl(), false,false);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @param url
	 * @param isShare
	 * @param isShowTitle 0 不显示标题， 1 显示标题
	 */
	private void openWebview(String url, boolean isShare,boolean isShowTitle) {
		JobFairDetailFragmentNew_.intent(this).url(url)
				.title(jobFairDetail.getTitle()).isShowTitle(isShowTitle).fairID(jobfair_id)
				.isShare(isShare).start();
	}

	private void viewTheProcess(String url) {
		if (viewTheProcess == null) {
			viewTheProcess = new ViewTheProcessDialog(this);
		}
		viewTheProcess.setImageUrl(url);
		viewTheProcess.show();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		loading.show();
		getDetail();
	}
}
