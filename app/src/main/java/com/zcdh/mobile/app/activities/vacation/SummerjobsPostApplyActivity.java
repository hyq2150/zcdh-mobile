package com.zcdh.mobile.app.activities.vacation;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.core.utils.StringUtils;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcHolidayService;
import com.zcdh.mobile.api.model.EntPostByOrderDTO;
import com.zcdh.mobile.api.model.PlaceOrderDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.wxapi.WXPayEntryActivity;

/**
 * 暑假工申请页面
 * 
 * @author jeason, 2014-7-25 下午2:46:21
 */
public class SummerjobsPostApplyActivity extends BaseActivity implements
		RequestListener, OnClickListener {

	private static final String TAG = SummerjobsPostApplyActivity.class.getSimpleName();

	private PlaceOrderDTO placeOrderDto;

	private IRpcHolidayService service;
	private int amount = 1;

	private TextView postNameTxt;
	private TextView entNameTxt;
	private TextView workAdressTxt;
	private TextView workTimeTxt;
	private TextView deposit_price;
	private TextView btn_minus;
	private TextView btn_plus;
	private TextView et_amount;
	private TextView tv_total_price;

	// 工作申请提交
	private Button postApplyBtn;
	// 存放显示条款内容的text
	private TextView contract_ContentTxt;
	// 是否同意条款
	private CheckBox is_agree;
	private double total_price;

	private String order_num;
	
	// 岗位综合信息
	private EntPostByOrderDTO post;
	private PayReceiver pay_receiver;
	private LoadingIndicator loading;

	public String kREQ_ID_GENERATEORDER;
	public String kREQ_ID_FINDVALIDPOSTCOUNT;
	public String kREQ_ID_FINDCLAUSE;
	public String kREQ_ID_APPLYHOLIDAYPOST;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summerjobs_post_apply);
		post = (EntPostByOrderDTO) getIntent().getExtras().get("post");
		pay_receiver = new PayReceiver();

		IntentFilter filter = new IntentFilter();
		filter.addAction(WXPayEntryActivity.TAG);
		this.registerReceiver(pay_receiver, filter);
		service = RemoteServiceManager.getRemoteService(
				IRpcHolidayService.class, this);
		loading = new LoadingIndicator(this);
		initUI();
		loadData();
	}

	// @Override
	protected void initUI() {
		// TODO Auto-generated method stub
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),
				"申请职位");
		postNameTxt = (TextView) findViewById(R.id.postname);
		entNameTxt = (TextView) findViewById(R.id.entname);
		workAdressTxt = (TextView) findViewById(R.id.post_adress);
		workTimeTxt = (TextView) findViewById(R.id.work_time);
		is_agree = (CheckBox) findViewById(R.id.is_agree);

		deposit_price = (TextView) findViewById(R.id.deposit_price);
		tv_total_price = (TextView) findViewById(R.id.total_price);

		contract_ContentTxt = (TextView) findViewById(R.id.agreement_content);

		// 工作申请提交
		postApplyBtn = (Button) this.findViewById(R.id.btn_apply);
		postApplyBtn.setTag(R.id.btn_apply);
		postApplyBtn.setOnClickListener(this);

		postNameTxt.setText(post.getPostName());
		entNameTxt.setText(post.getEntName());
		workAdressTxt.setText("工作地点：" + post.getAreaName());
		workTimeTxt.setText("工作期间：" + post.getWorkTime());
		et_amount = (TextView) findViewById(R.id.et_amount);
		et_amount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				notify_calculate();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		btn_minus = (TextView) findViewById(R.id.btn_minus);
		btn_plus = (TextView) findViewById(R.id.btn_plus);
		et_amount.setText(String.valueOf(amount));
		btn_minus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				decrease();
			}
		});
		btn_plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				populate();
			}
		});
	}

	/**
	 * 
	 * @author jeason, 2014-4-26 上午9:11:53
	 */
	private void genOrder() {
		if (placeOrderDto == null)
			return;

		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				if (post == null)
					return null;
				try {
					service.generateOrderForHolidayJob(SharedPreferencesUtil.getValue(getApplicationContext(), Constants.JOBFAIR_ID_KEY, 0l), getUserId(), post.getPostId(), amount).identify(
							kREQ_ID_GENERATEORDER = RequestChannel
							.getChannelUniqueID(),
					SummerjobsPostApplyActivity.this);
				} catch (Exception e) {

				}
				return null;
			}
		}.execute();
	}

	// @Override
	protected void loadData() {
		// TODO Auto-generated method stub
		// super.loadData();
		new AsyncTask<Void, Void, PlaceOrderDTO>() {

			@Override
			protected PlaceOrderDTO doInBackground(Void... params) {

				// 保证整个程序在生命周期内只像服务器下载一次合同条款内容数据
				try {
					if (StringUtils.isBlank(ZcdhApplication.getInstance()
							.getContractStr())) {
						service.findClause().identify(kREQ_ID_FINDCLAUSE = RequestChannel.getChannelUniqueID(), SummerjobsPostApplyActivity.this);
					}
					service.applyHolidayPostForJobfair(SharedPreferencesUtil.getValue(getApplicationContext(), Constants.JOBFAIR_ID_KEY, 0l),getUserId(), post.getPostId()).identify(
							kREQ_ID_APPLYHOLIDAYPOST = RequestChannel
							.getChannelUniqueID(),
					SummerjobsPostApplyActivity.this);;
//					service.applyHolidayPost(getUserId(), post.getPostId())
//							.identify(
//									kREQ_ID_APPLYHOLIDAYPOST = RequestChannel
//											.getChannelUniqueID(),
//									SummerjobsPostApplyActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = v.getTag() == null ? -1 : (Integer) v.getTag();
		switch (tag) {
		case R.id.btn_apply: {
			doApply();
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 上午11:35:23
	 */
	private void doApply() {
		if (!is_agree.isChecked()) {
			Toast.makeText(this, "请先同意合同条款", Toast.LENGTH_SHORT).show();
			return;
		}
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				Log.e("Summer", "fair id : " + SharedPreferencesUtil.getValue(getApplicationContext(), Constants.JOBFAIR_ID_KEY, 0l));
				try {
					service.findValidPostCountForHolidayJob(SharedPreferencesUtil.getValue(getApplicationContext(), Constants.JOBFAIR_ID_KEY, 0l),post.getPostId()).identify(
									kREQ_ID_FINDVALIDPOSTCOUNT = RequestChannel
											.getChannelUniqueID(),
									SummerjobsPostApplyActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}

		}.execute();
	}

	private int getAmount() {
		try {
			amount = Integer.parseInt(et_amount.getText().toString());
			return amount;
		} catch (Exception e) {
			return 1;
		}
	}

	private void populate() {
		amount = getAmount();
		amount++;
		et_amount.setText(String.valueOf(amount));
		// notify_calculate();
	}

	private void decrease() {
		amount = getAmount();
		if (amount > 1)
			amount--;
		et_amount.setText(String.valueOf(amount));

		// notify_calculate();

	}

	private void notify_calculate() {
		if (placeOrderDto == null)
			return;

		total_price = placeOrderDto.getEarnestMoney().doubleValue()
				* getAmount();
		tv_total_price.setText("¥" + String.valueOf(total_price));
	}

	public class PayReceiver extends BroadcastReceiver {

		private final int default_error_code = -2;

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getIntExtra("errCode", default_error_code) == 0) {
				SummerjobsPostApplyActivity.this.finish();
				sendBroadcast(intent);
			}
		}

	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(pay_receiver);
		super.onDestroy();
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestSuccess(String reqId, final Object result) {
		if (reqId.equals(kREQ_ID_FINDCLAUSE)) {
			if (result != null) {
				ZcdhApplication.getInstance().setContractStr((String) result);
			}
		}

		if (reqId.equals(kREQ_ID_FINDVALIDPOSTCOUNT)) {
			int act_result = 0;
			if (result != null) {
				act_result = (Integer) result;
			}
			if (act_result == 0) {
				Toast.makeText(SummerjobsPostApplyActivity.this, "当前申请名额已满！",
						Toast.LENGTH_SHORT).show();
				// genOrder();//必须注释
				return;
			}

			if (getAmount() <= act_result) {
				genOrder();
			} else {
				new AlertDialog.Builder(SummerjobsPostApplyActivity.this)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										et_amount.setText(String
												.valueOf((Integer) result));
										amount = (Integer) result;
										genOrder();
									}
								})
						.setNegativeButton("取消", null)
						.setMessage(
								String.format("当前只剩下%s个申请名额,按确定继续申请",
										String.valueOf(result))).create()
						.show();
			}
		}

		if (reqId.equals(kREQ_ID_GENERATEORDER)) {
			loading.dismiss();
			if (result == null) {
				Toast.makeText(SummerjobsPostApplyActivity.this, "网络错误",
						Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			order_num = (String) result;
			ActivityDispatcher.purchaseVacationJob(
					SummerjobsPostApplyActivity.this, order_num);
		}

		if (reqId.equals(kREQ_ID_APPLYHOLIDAYPOST)) {
			if (result != null) {
				placeOrderDto = (PlaceOrderDTO) result;
				deposit_price.setText("¥"
						+ String.valueOf(placeOrderDto.getEarnestMoney()));
				et_amount.setText("1");

				contract_ContentTxt.setText(ZcdhApplication.getInstance()
						.getContractStr());
			}

		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		Log.e(TAG,"reqID :　" + reqID);
		error.printStackTrace();
	}
}
