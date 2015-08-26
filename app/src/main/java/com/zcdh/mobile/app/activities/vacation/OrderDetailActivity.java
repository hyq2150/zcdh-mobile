/**
 * 
 * @author jeason, 2014-4-28 下午12:50:47
 */
package com.zcdh.mobile.app.activities.vacation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcHolidayService;
import com.zcdh.mobile.api.model.MyOrderDTO;
import com.zcdh.mobile.api.model.OrderDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-4-26 下午12:50:47
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements RequestListener {

	
	private IRpcHolidayService service;
	private MyOrderDTO orderdto;

	private OrderDTO order;
	private String order_num;
	private final int PAID = 0;
	private final int PENDING = 1;

	private LinearLayout ll_pay;
	private LinearLayout ll_manage;
	private Button btn_manage;
	private Button btn_pay;

	private TextView tv_order_status;
	private TextView tv_order_number;
	private TextView tv_order_time;
	private TextView tv_post_name;

	private TextView tv_ent_name;
	private TextView tv_address;
	private TextView tv_duration;
	private TextView tv_total;
	private LoadingIndicator loading;
	private TextView tv_contact;

	private String kREQ_ID_findOrderDTO;
	private String kREQ_ID_CANCELORDER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loading = new LoadingIndicator(this);
		setContentView(R.layout.order_info);
		initUI();
		service = RemoteServiceManager.getRemoteService(IRpcHolidayService.class, this);

		orderdto = (MyOrderDTO) getIntent().getSerializableExtra("info");
		order_num = getIntent().getStringExtra("order_num");
		getOrder();
	}

	// @Override
	protected void initUI() {
		// TODO Auto-generated method stub
		setTitle("");
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "订单详情");
		bindViews();
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午3:18:44
	 */
	private void bindViews() {

		tv_address = (TextView) findViewById(R.id.address);

		tv_duration = (TextView) findViewById(R.id.duration);

		tv_ent_name = (TextView) findViewById(R.id.ent_name);

		tv_order_number = (TextView) findViewById(R.id.order_number);

		tv_order_status = (TextView) findViewById(R.id.order_status);

		tv_order_time = (TextView) findViewById(R.id.order_time);

		tv_post_name = (TextView) findViewById(R.id.postname);

		tv_total = (TextView) findViewById(R.id.total);

		ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
		ll_manage = (LinearLayout) findViewById(R.id.ll_manage);
		btn_manage = (Button) findViewById(R.id.btn_manage);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		tv_contact = (TextView) findViewById(R.id.order_contact);

	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午2:24:12
	 */
	private void getOrder() {
		new AsyncTask<Void, Void, OrderDTO>() {

			@Override
			protected OrderDTO doInBackground(Void... params) {

				try {
					 service.findOrderDTO(order_num)
					 .identify(kREQ_ID_findOrderDTO=RequestChannel.getChannelUniqueID(), OrderDetailActivity.this);
					 
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(OrderDTO result) {
			};
		}.execute();
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午2:26:39
	 */
	protected void initViews() {
		tv_address.setText("工作地点:" + orderdto.getAreaName());
		if (orderdto.getStartTime() != null && orderdto.getEndTime() != null) {
			tv_duration.setText("工作时间:" + DateUtils.getDateByFormat(orderdto.getStartTime(), "yyyy-MM-dd") + "~" + DateUtils.getDateByFormat(orderdto.getEndTime(), "yyyy-MM-dd"));
		} else {
			tv_duration.setText("工作时间:" + "暂无");
		}
		tv_ent_name.setText(orderdto.getEntName());
		tv_order_number.setText(orderdto.getOrderNum());
		if (orderdto.getStatus() == 0) {
			tv_order_status.setText("已付款");
		} else {
			tv_order_status.setText("未付款");
		}
		tv_order_time.setText(DateUtils.getDateByFormat(orderdto.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
		tv_post_name.setText(orderdto.getPostName());

		tv_total.setText("¥" + orderdto.getPayMoney().toPlainString());

		// 已付款
		if (orderdto.getStatus() == PAID) {
			ll_pay.setVisibility(View.GONE);

			//暂时屏蔽退款功能
			ll_manage.setVisibility(View.GONE);
			
			btn_manage.setText("申请退款");
			btn_manage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					refund();
				}
			});
		} else if (orderdto.getStatus() == PENDING) {
			btn_manage.setText("取消订单");
			btn_manage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					cancelOrder();
				}
			});
			ll_pay.setVisibility(View.VISIBLE);

			btn_pay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goPay();
				}
			});
		}
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午3:24:58
	 */
	protected void goPay() {
		ActivityDispatcher.purchaseVacationJob(this, order_num);
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午3:24:01
	 */
	protected void cancelOrder() {
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... params) {
				try {
					 service.cancelOrder(order_num)
							.identify(kREQ_ID_CANCELORDER=RequestChannel.getChannelUniqueID(), OrderDetailActivity.this);
					 return null;
				} catch (Exception e) {
					Log.e("cancel order", "取消订单出错" + e.getMessage());
					e.printStackTrace();
				}
				return 1;
			}

			@Override
			protected void onPreExecute() {
				loading.show();
				super.onPreExecute();
			}

		}.execute();
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午3:22:27
	 */
	protected void refund() {
		Toast.makeText(this, "refund", Toast.LENGTH_SHORT).show();
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
		loading.dismiss();
		if (reqId.equals(kREQ_ID_CANCELORDER)) {
			int act_result = (Integer) result;
			if (act_result == 0) {
				Toast.makeText(OrderDetailActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				data.putExtra("order_num", order_num);
				setResult(RESULT_OK, data);
				finish();
			} else {
				Toast.makeText(OrderDetailActivity.this, "取消失败", Toast.LENGTH_SHORT).show();

			}
		}

		if (reqId.equals(kREQ_ID_findOrderDTO)) {
			if (result != null) {
				order = (OrderDTO) result;
				String customer_name = "";
				String qqstr = "";
				String publicChatstr = "";
				customer_name = order.getCustomerSrviceName();
				qqstr = order.getCustomerServiceQQ();
				publicChatstr = order.getPublishServiceNum();
				if (null == customer_name) {
					customer_name = "暂无";
				}
				if (null == qqstr) {
					qqstr = "暂无";
				}
				if (null == publicChatstr) {
					publicChatstr = "暂无";
				}
				tv_contact.setText("客服支持：已为您安排专属客服经理：" + customer_name + "\n QQ: " + qqstr + "  公众号: " + publicChatstr);

				initViews();
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
	}

}
