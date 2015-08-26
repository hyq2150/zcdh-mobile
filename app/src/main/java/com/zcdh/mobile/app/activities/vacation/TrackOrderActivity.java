/**
 * 
 * @author jeason, 2014-4-26 上午11:33:45
 */
package com.zcdh.mobile.app.activities.vacation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcHolidayService;
import com.zcdh.mobile.api.model.MyOrderDTO;
import com.zcdh.mobile.api.model.TrackOrder;
import com.zcdh.mobile.api.model.TrackOrderDTO;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-4-26 上午11:33:45
 * 订单跟踪
 */
public class TrackOrderActivity extends BaseActivity implements OnClickListener,RequestListener{
	private TextView tv_order_status;//订单状态
	private TextView tv_order_number;//订单编号
	private TextView tv_order_totalmoney;//订单金额
	private TextView tv_order_time;//下单时间
	private TextView tv_order_contact;//客服
	private ListView lv_order_status_list;
	
	private IRpcHolidayService service;
	private MyOrderDTO orderdto;

	private String order_num;
	public String kREQ_ID_FINDTRACKORDER;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.track_order);
		service = RemoteServiceManager.getRemoteService(IRpcHolidayService.class);
		orderdto = (MyOrderDTO) getIntent().getSerializableExtra("info");
		order_num = getIntent().getStringExtra("order_num");
		initUI();
		getOrderStatus();
	}
	
	protected void initUI() {
		// TODO Auto-generated method stub
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "订单跟踪");
		bindViews();
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午7:36:42
	 */
	private void getOrderStatus() {
		new AsyncTask<Void, Void, TrackOrderDTO>() {

			@Override
			protected TrackOrderDTO doInBackground(Void... params) {
				service.findTrackOrder(order_num)
				.identify(kREQ_ID_FINDTRACKORDER=RequestChannel.getChannelUniqueID(), TrackOrderActivity.this);
				return null;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

		}.execute();
	}

	/**
	 * @param trackOrders
	 * @author jeason, 2014-4-28 下午7:43:16
	 */
	protected void initOrderStatus(List<TrackOrder> trackOrders) {

		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (TrackOrder trackOrder : trackOrders) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status_time", DateUtils.getDateByFormatYMDHMS(trackOrder.getTrack_order_time()));
			map.put("status_text", trackOrder.getContent());
			contents.add(map);
		}
		lv_order_status_list.setAdapter(new SimpleAdapter(this, contents, R.layout.order_status_item, new String[] { "status_time", "status_text" }, new int[] { R.id.status_time, R.id.status_text }));
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午7:15:04
	 */
	private void bindViews() {
		tv_order_contact = (TextView) findViewById(R.id.order_contact);
		tv_order_number = (TextView) findViewById(R.id.order_number);
		tv_order_status = (TextView) findViewById(R.id.order_status);
		tv_order_totalmoney = (TextView) findViewById(R.id.order_totalmoney);
		tv_order_time = (TextView) findViewById(R.id.order_time);
		lv_order_status_list = (ListView) findViewById(R.id.order_status_list);
		
		if(null != orderdto){
			//订单状态
			switch (orderdto.getStatus()) {
			case 1:{
				tv_order_status.setText("等待付款");
				break;
			}
			case 0:{
				tv_order_status.setText("已付款");
				break;
			}	
			default:
				break;
			}
			//订单编号
			tv_order_number.setText(orderdto.getOrderNum());
			//订单金额
			tv_order_totalmoney.setText("¥"+orderdto.getPayMoney().toString());
			//下单时间
			String odertimestr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderdto.getOrderDate());
			tv_order_time.setText(odertimestr);
		}
	
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.framework.nio.RequestListener#onRequestStart(java.lang.String)
	 */
	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.framework.nio.RequestListener#onRequestSuccess(java.lang.String, java.lang.Object)
	 */
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		TrackOrderDTO trackOrder =  (TrackOrderDTO) result;
		if(reqId.equals(kREQ_ID_FINDTRACKORDER)){
			initOrderStatus(trackOrder.getTrackOrders());
			String customer_name = "";
			String qqstr = "";
			String publicChatstr = "";
			qqstr = trackOrder.getCustomerServiceQQ();
			customer_name = trackOrder.getCustomerSrviceName();
			publicChatstr = trackOrder.getPublishServiceNum();
			
			if(null == customer_name){customer_name = "暂无";}
			if(null == qqstr){qqstr = "暂无";}
			if(null == publicChatstr){publicChatstr = "暂无";}
			
			tv_order_contact.setText("客服支持：已为您安排专属客服经理："
					+customer_name+"\n QQ: "+qqstr+" 公众号 : "+publicChatstr);
		}
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.framework.nio.RequestListener#onRequestFinished(java.lang.String)
	 */
	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.framework.nio.RequestListener#onRequestError(java.lang.String, java.lang.Exception)
	 */
	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		
	}

	
}
