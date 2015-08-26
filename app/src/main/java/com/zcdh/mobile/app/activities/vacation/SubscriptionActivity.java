package com.zcdh.mobile.app.activities.vacation;

import java.text.SimpleDateFormat;
import java.util.List;

import org.androidannotations.api.BackgroundExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcHolidayService;
import com.zcdh.mobile.api.model.FitPostListDTO;
import com.zcdh.mobile.api.model.HomePageDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 预约页面
 * @author jeason, 2014-7-25 下午2:46:46
 */
public class SubscriptionActivity extends BaseActivity implements OnClickListener, RequestListener {

	/**
	 * *预约首页信息集合
	 **/
	private HomePageDTO homepageDto;
	/**
	 * *适合用户的岗位列表
	 */
	private List<FitPostListDTO> fitPostsList;
	/**
	 * 暑假工服务
	 **/
	private IRpcHolidayService holidayService;

	// 暑假工开放时间
	private TextView sumjobsOpenTimeTxt;
	// 总共参与的企业数量
	private TextView entCountTxt;
	// 总共提供的岗位数量
	private TextView postTotalCountTxt;
	// 适合用户的岗位数量
	private TextView postCountSuitableForUserTxt;
	// 点击查看岗位分布详情
	private TextView post_Detail_Ditribution_List_Txt;

	// 已预约人数
	private TextView numHaveAppointedTxt;

	// 预约按钮
	private Button appointed_btn;

	// 适合岗位的列表弹出框
	PopupWindow mPopupWindow;
	// 适合岗位的列表
	ListView lvPopupList;
	// popwindow的显示高度,特别是在内部为list的情况下，可以很好的控制弹出框显示的列表项条数
	private int NUM_OF_VISIBLE_LIST_ROWS = 5;

	/***
	 * 合同条款内容
	 * **/
	private TextView contract_ContentTxt;

	/***
	 * 点击查看全部合同条款内容*
	 * */

	// 合同内容是否展开
	private boolean spread = false;

	public String kREQ_ID_findHolidayHomePageDTO;
	public String kREQ_ID_findFitPostListDTO;
	public String kREQ_ID_findClause;
	public String kREQ_ID_subscribe;

	public final int request_code_purpose = 0x01;

	// private TextView seeContractDetailTxt;

	// ==================acitivity=====================
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscription_new);
		holidayService = RemoteServiceManager.getRemoteService(IRpcHolidayService.class, this);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "暑假工");
		initUI();
		loadData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (request_code_purpose == requestCode) {// 不需要判断setResult()的resultCode,只要是从意向界面返回，结果操作都需要重新加载数据
			loadData();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.
	 * actionbarsherlock.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.alter_intention, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.activities.BaseActivity#onOptionsItemSelected
	 * (com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_alter_intention) {
			ActivityDispatcher.toPurposeActivity(this, request_code_purpose);
		}
		return super.onOptionsItemSelected(item);
	}

	protected void initUI() {
		setTitle("暑假工");
		// 暑假工开放时间
		sumjobsOpenTimeTxt = (TextView) findViewById(R.id.summerjob_opentime_text);
		// 总共参与的企业数量
		entCountTxt = (TextView) findViewById(R.id.ent_count_text);
		// 总共提供的岗位数量
		postTotalCountTxt = (TextView) findViewById(R.id.post_total_count_text);
		// 适合用户的岗位数量
		postCountSuitableForUserTxt = (TextView) findViewById(R.id.post_count_suitable_for_user);
		// 点击查看岗位分布详情
		post_Detail_Ditribution_List_Txt = (TextView) findViewById(R.id.post_detail_ditribution_list_text);
		post_Detail_Ditribution_List_Txt.setTag(R.id.post_detail_ditribution_list_text);
		post_Detail_Ditribution_List_Txt.setOnClickListener(this);

		// this.rightBtnText.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(SubscriptionActivity.this,
		// SummerIntentionActivity.class);
		// intent.putExtra("requestCode", 1);
		// startActivityForResult(intent, 1);
		// }
		// });

		// 已预约人数
		numHaveAppointedTxt = (TextView) findViewById(R.id.num_have_appointed);
		// 预约优惠
		// discount_Txt = (TextView)
		// findViewById(R.id.discount_for_advance_appointment);

		// 预约
		appointed_btn = (Button) findViewById(R.id.advance_appointment_btn);
		appointed_btn.setTag(R.id.advance_appointment_btn);
		appointed_btn.setOnClickListener(this);

		// //点击查看更多合同条款内容
		// seeContractDetailTxt = (TextView)
		// findViewById(R.id.look_over_agreement);
		// seeContractDetailTxt.setTag(R.id.look_over_agreement);
		// seeContractDetailTxt.setOnClickListener(this);

		// 合同条款内容
		contract_ContentTxt = (TextView) findViewById(R.id.agreement_content);

	}

	void handleData() {
		// 暑假工是否已开放
		if (null != homepageDto) {
			if (null != homepageDto.getIsSubscribe()) {
				if (homepageDto.getIsSubscribe()) {
					appointed_btn.setText("已预约");
				} else {
					// 预约优惠
					if (null != homepageDto.getVoucherMoney()) {
						appointed_btn.setText("马上预约,立减" + homepageDto.getVoucherMoney() + "元");
					}
				}
			}

			// if(null != homepageDto.getIsStart())
			// {
			// if(homepageDto.getIsStart()){
			// setTitle("暑假工(已开放)");
			// }else{
			// setTitle("暑假工(未开放)");
			// }
			// }
			// 暑假工开放时间
			if (null != homepageDto.getStartTime()) {
				String startTimeStr = new SimpleDateFormat("MM月dd日 hh:mm").format(homepageDto.getStartTime());
				sumjobsOpenTimeTxt.setText(startTimeStr);
			}
			// 进驻企业数
			if (null != homepageDto.getEntCount()) {
				String text1 = "进驻企业:<font color = #00B2EE>" + homepageDto.getEntCount().toString() + "</font>家";
				entCountTxt.setText(Html.fromHtml(text1));
				// entCountTxt.setText("进驻企业:"+homepageDto.getEntCount().toString()+"家");
			}
			// 提供岗位数量
			if (null != homepageDto.getPostCount()) {
				String text2 = "提供岗位:<font color = #00B2EE>" + homepageDto.getPostCount().toString() + "</font>个";
				postTotalCountTxt.setText(Html.fromHtml(text2));
				// postTotalCountTxt.setText("提供岗位:"+homepageDto.getPostCount().toString()+"个");
			}
			// 已预约人数
			if (null != homepageDto.getSubscribedCount()) {
				int num = homepageDto.getSubscribedCount();
				String text = "已有<font color = #EE7621>" + num + "</font>人预约";
				numHaveAppointedTxt.setText(Html.fromHtml(text));
			}

			if (null != homepageDto.getFitPostsCount()) {
				postCountSuitableForUserTxt.setText(homepageDto.getFitPostsCount().toString());
			}
		}

		// 合同条款内容
		contract_ContentTxt.setText(ZcdhApplication.getInstance().getContractStr());

		// 适合用户的岗位列表，岗位列表的大小即是适合的岗位数量
		if (null != fitPostsList) {
			// int num = fitPostsList.size();
			// String text = "已有<font color = #ff4040>"+num+"</font>人预约";
			// numHaveAppointedTxt.setText(Html.fromHtml(text));

			// 初始化适合岗位的列表弹出框
			initPopupWindow();
		}
	}

	void loadData() {
		BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {

			@Override
			public void execute() {
				try {
					 holidayService.findHolidayHomePageDTO(getUserId(), Constants.BANNDER_ID)
					 .identify(kREQ_ID_findHolidayHomePageDTO=RequestChannel.getChannelUniqueID(), SubscriptionActivity.this);
					 holidayService.findFitPostListDTO(getUserId());
					// 保证整个程序在生命周期内只像服务器下载一次合同条款内容数据
					 holidayService.findClause().identify(kREQ_ID_findClause=RequestChannel.getChannelUniqueID(), SubscriptionActivity.this);
				} catch (Throwable e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
				}
			}

		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = v.getTag() == null ? -1 : (Integer) v.getTag();
		switch (tag) {
		// 查看岗位分布详情
		case R.id.post_detail_ditribution_list_text: {
			if (null == mPopupWindow) return;// 防止当从服务器获取的适合岗位列表为空时出错
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			} else {
				// mPopupWindow.showAsDropDown(post_Detail_Ditribution_List_Txt);
				mPopupWindow.showAtLocation(post_Detail_Ditribution_List_Txt, Gravity.CENTER, 0, 0);
			}
			break;
		}

		// 预约
		case R.id.advance_appointment_btn: {
			// 判断是否有登录
			if (!RegisterUtil.isRegisterUser(getApplicationContext())) {
				RegisterUtil.intercepterNotRegisterUser(this, "登录后预约");
			} else if (null != homepageDto) {
				if (homepageDto.getIsSubscribe()) {// 如果已经预约过了
					Toast.makeText(getApplicationContext(), "您已经预约过了", Toast.LENGTH_SHORT).show();
					return;
				}
				holidayService.subscribe(getUserId(), Constants.BANNDER_ID);
			}

			break;
		}
		// //点击查看条款内容
		// case R.id.look_over_agreement:{
		// spread = (!spread);
		// if(spread){
		// contract_ContentTxt.setMaxLines(1000);
		// seeContractDetailTxt.setText("点击收起条款详情");
		// }else{
		// contract_ContentTxt.setMaxLines(2);
		// seeContractDetailTxt.setText("点击查看全部");
		// }
		// break;
		// }

		}
	}

	private void initPopupWindow() {

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.ent_detail_more_popupwindow, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		mPopupWindow = new PopupWindow(layout);

		mPopupWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		FitPostsListAdapter fitPostsAdapter = new FitPostsListAdapter(SubscriptionActivity.this, fitPostsList);
		lvPopupList.setAdapter(fitPostsAdapter);
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 如果要对点击列表的具体一项做出来，在这里添加代码
			}
		});

		// 控制popupwindow的宽度和高度自适应
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopupWindow.setWidth(lvPopupList.getMeasuredWidth());
		mPopupWindow.setHeight((lvPopupList.getMeasuredHeight()) * NUM_OF_VISIBLE_LIST_ROWS);// 弹出窗口设置高度为五个项的高度

		// 控制popupwindow点击屏幕其他地方消失
		mPopupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bottom_and_top_round));// 设置背景图片，不能在布局中设置，要通过代码来设置
		mPopupWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
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
		// TODO Auto-generated method stub

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
		if (reqId.equals(kREQ_ID_findClause)) {

			if (result != null) {
				ZcdhApplication.getInstance().setContractStr((String) result);
				handleData();
			}
		}

		if (reqId.equals(kREQ_ID_findFitPostListDTO)) {
			if (result != null) {
				this.fitPostsList = (List<FitPostListDTO>) result;
				handleData();
			}
		}

		if (reqId.equals(kREQ_ID_findHolidayHomePageDTO)) {
			if (result != null) {
				this.homepageDto = (HomePageDTO) result;

				handleData();
			}
		}

		if (reqId.equals(kREQ_ID_subscribe)) {
			if (result != null) {
				int act_result = (Integer) result;
				if (0 == act_result) {
					appointed_btn.setText("已预约");
					Toast.makeText(getApplicationContext(), "预约成功", Toast.LENGTH_SHORT).show();
				} else if (3 == act_result) {
					Toast.makeText(getApplicationContext(), "您已预约过了", Toast.LENGTH_SHORT).show();
				} else {

				}
			}
		}
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
		// TODO Auto-generated method stub

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

	}

}
