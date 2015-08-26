/**
 * 
 * @author jeason, 2014-4-22 下午4:44:01
 */
package com.zcdh.mobile.app.activities.vacation;

import java.math.BigDecimal;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcHolidayService;
import com.zcdh.mobile.api.model.ConFirmPaymentDTO;
import com.zcdh.mobile.api.model.PayForDTO;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.wxapi.WXPayEntryActivity;

/**
 * @author jeason, 2014-4-22 下午4:44:01
 * 支付页面
 */
public class PurchaseActivity extends BaseActivity implements RequestListener {

	private TextView discountTxt;
	private CheckBox cb_accept_discount;
	private TextView tv_total_price;
	private TextView original_total_price;
	private BigDecimal total_price;
	private IRpcHolidayService service;
	private TextView ent_name;
	private TextView address;
	private TextView duration;
	private RelativeLayout rl_order;
	private PayForDTO payForDto;
	private String order_num;
	private IWXAPI api;
	private Button confirm_and_purchase;
	private PayReceiver pay_receiver;
	private LoadingIndicator loading;
	private TextView purchase_title;

	private String kREQ_ID_TOPREPAYFOR;
	private String kREQ_ID_CONFORMORDERBYWECHAT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pay_receiver = new PayReceiver();

		IntentFilter filter = new IntentFilter();
		filter.addAction(WXPayEntryActivity.TAG);
		this.registerReceiver(pay_receiver, filter);
		setContentView(R.layout.purchase);
		api = WXAPIFactory.createWXAPI(this, com.zcdh.mobile.app.activities.vacation.payment.Constants.APP_ID);
		api.registerApp(com.zcdh.mobile.app.activities.vacation.payment.Constants.APP_ID);
		service = RemoteServiceManager.getRemoteService(IRpcHolidayService.class, this);
		initUI();
		getArgs();
		loading = new LoadingIndicator(this);
	}

	public class PayReceiver extends BroadcastReceiver {

		private final int default_error_code = -2;

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getIntExtra("errCode", default_error_code) == 0) {
				PurchaseActivity.this.finish();
				Intent itent = new Intent(WXPayEntryActivity.TAG);
				itent.putExtra("errCode", 0);
				sendBroadcast(itent);
			}
		}

	}

	protected void initUI() {
		// TODO Auto-generated method stub
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "订单支付");
		// setTitle("订单支付");
		bindviews();
	}

	/**
	 * 
	 * @author jeason, 2014-4-26 上午9:25:59
	 */
	private void getArgs() {
		order_num = getIntent().getStringExtra("order_num");
		genPayfor();
	}

	private void genPayfor() {
		new AsyncTask<Void, Void, PayForDTO>() {

			@Override
			protected PayForDTO doInBackground(Void... params) {
				if (order_num == null) return null;
				try {
					service.toPrePayFor(order_num).identify(kREQ_ID_TOPREPAYFOR = RequestChannel.getChannelUniqueID(), PurchaseActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
				// 保证整个程序在生命周期内只像服务器下载一次合同条款内容数据

			}
		}.execute();
	}

	/**
	 * 
	 * @author jeason, 2014-4-26 上午9:25:32
	 * @param result
	 */
	protected void initOrder(PayForDTO result) {
		this.payForDto = result;
		original_total_price.setText("¥" + payForDto.getEarnestMoney().multiply(new BigDecimal(payForDto.getPostCount())));
		if (!result.getHasVoucher()) {
			rl_order.setVisibility(View.GONE);
			cb_accept_discount.setChecked(false);
			cb_accept_discount.setEnabled(false);
		}
	}

	/**
	 * 
	 * @author jeason, 2014-4-26 上午9:19:34
	 */
	protected void initInfo(PayForDTO payfordto) {
		purchase_title.setText(payfordto.getPostName());
		ent_name.setText(payfordto.getEntName());
		address.setText("工作地点：" + payfordto.getAreaName());
		if (payfordto.getStartTime() != null && payfordto.getEndTime() != null) {
			duration.setText(String.format("工作期间：%s~%s", DateUtils.getDateByFormat(payfordto.getStartTime(), "yyyy-MM-dd"), DateUtils.getDateByFormat(payfordto.getEndTime(), "yyyy-MM-dd")));
		} else {
			duration.setText("暂无");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zcdh.mobile.activity.framework.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loading.dismiss();
	}

	/**
	 * 
	 * @author jeason, 2014-4-26 上午9:11:02
	 */
	private void bindviews() {
		// btn_right = (Button) findViewById(R.id.rightBtnText);

		purchase_title = (TextView) findViewById(R.id.purchase_title);
		ent_name = (TextView) findViewById(R.id.ent_name);
		address = (TextView) findViewById(R.id.address);
		duration = (TextView) findViewById(R.id.duration);
		tv_total_price = (TextView) findViewById(R.id.total_price);
		original_total_price = (TextView) findViewById(R.id.original_total_price);
		// deposit_price = (TextView) findViewById(R.id.deposit_price);

		discountTxt = (TextView) findViewById(R.id.discount);

		rl_order = (RelativeLayout) findViewById(R.id.rl_coupon);

		cb_accept_discount = (CheckBox) findViewById(R.id.cb_accept_discount);
		cb_accept_discount.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				notify_calculate();
			}
		});

		confirm_and_purchase = (Button) findViewById(R.id.confirm_and_purchase);
		confirm_and_purchase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirm_and_purchase();
				// getWxParamsAndPay();
			}
		});
	}

	/**
	 * 
	 * @author jeason, 2014-4-28 下午8:52:23
	 */
	protected void confirm_and_purchase() {
		/**
		 * 如果订单信息还没加载完 不能执行确认提交订单
		 */
		if (payForDto == null) {
			Toast.makeText(this, "订单信息还没加载完", Toast.LENGTH_SHORT).show();
			return;
		}
		loading.show();
		new AsyncTask<Void, Void, ConFirmPaymentDTO>() {

			@Override
			protected ConFirmPaymentDTO doInBackground(Void... params) {
				try {
					service.conformOrderByWeChat(order_num, cb_accept_discount.isChecked()).identify(kREQ_ID_CONFORMORDERBYWECHAT = RequestChannel.getChannelUniqueID(), PurchaseActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ConFirmPaymentDTO result) {
				// if (result != null) {
				// ZcdhApplication.getInstance().setOrder_num(order_num);
				// //
				// ZcdhApplication.getInstance().setPurchaseActivity(PurchaseActivity.this);
				//
				// PayReq req = new PayReq();
				// Map<String, String> request_params = result.getReqParam();
				// req.appId = request_params.get("appid");
				// req.partnerId = request_params.get("partnerid");
				// req.prepayId = request_params.get("prepayid");
				// req.nonceStr = request_params.get("noncestr");
				// req.timeStamp = request_params.get("timestamp");
				// req.packageValue = request_params.get("package");
				// req.sign = request_params.get("sign");
				//
				// api.sendReq(req);
				// } else {
				// Log.e("payment error", "订单生成错误");
				// }
				// loading.dismiss();
				super.onPostExecute(result);
			}

			@Override
			protected void onPreExecute() {
				loading.show();
				super.onPreExecute();
			}

		}.execute();
	}

	private void notify_calculate() {
		if (payForDto == null) return;
		boolean use_discount = false;

		if (cb_accept_discount.isChecked()) {
			use_discount = true;
		}

		total_price = payForDto.getEarnestMoney().multiply(new BigDecimal(payForDto.getPostCount()));
		if (use_discount) {
			total_price = total_price.subtract(payForDto.getVoucher());
		}
		tv_total_price.setText("¥" + String.valueOf(total_price));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(pay_receiver);
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

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		loading.dismiss();
		if (reqId.equals(kREQ_ID_TOPREPAYFOR)) {
			if (result == null) {
				Toast.makeText(PurchaseActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			PayForDTO payDto = (PayForDTO) result;
			initOrder(payDto);
			initInfo(payDto);
			notify_calculate();
			// 设置优惠值
			discountTxt.setText("¥" + payDto.getVoucher().toString());

		}
		if (reqId.equals(kREQ_ID_CONFORMORDERBYWECHAT)) {
			if (result != null) {
				ConFirmPaymentDTO confirmDto = (ConFirmPaymentDTO) result;
				ZcdhApplication.getInstance().setOrder_num(order_num);
				// ZcdhApplication.getInstance().setPurchaseActivity(PurchaseActivity.this);

				PayReq req = new PayReq();
				Map<String, String> request_params = confirmDto.getReqParam();
				req.appId = request_params.get("appid");
				req.partnerId = request_params.get("partnerid");
				req.prepayId = request_params.get("prepayid");
				req.nonceStr = request_params.get("noncestr");
				req.timeStamp = request_params.get("timestamp");
				req.packageValue = request_params.get("package");
				req.sign = request_params.get("sign");

				api.sendReq(req);
			} else {
				Toast.makeText(PurchaseActivity.this, "订单生成错误", Toast.LENGTH_SHORT).show();
				Log.e("payment error", "订单生成错误");
			}

		}

	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		loading.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "onRequestError网络错误", Toast.LENGTH_SHORT).show();
		Log.e("PurchaseActivity", "reqID : " + reqID + " ----------   ");
		loading.dismiss();
	}

	// void getWxParamsAndPay() {
	// loading.show();
	// new AsyncTask<Void, Void, WeChatParamDTO>() {
	//
	// @Override
	// protected WeChatParamDTO doInBackground(Void... params) {
	// try {
	// return service.genBasicWeChatParam();
	// } catch (Exception e) {
	// Log.e("pay_exception", "微信参数请求错误");
	// Toast.makeText(PurchaseActivity.this, "支付出现错误",
	// Toast.LENGTH_SHORT).show();
	// }
	// return null;
	// }
	//
	// protected void onPostExecute(WeChatParamDTO result) {
	// if (result != null) {
	// wx_params = result;
	// // then do getAccessTocken
	// getAccessTokenAndPay();
	// } else {
	// Log.d("payment", "微信参数请求错误");
	// loading.dismiss();
	// Toast.makeText(PurchaseActivity.this, "支付出现错误",
	// Toast.LENGTH_SHORT).show();
	// }
	// };
	//
	// }.execute();
	// }
	//
	// /**
	// * getAccessToken
	// *
	// * @author jeason, 2014-5-2 下午2:57:18
	// */
	// void getAccessTokenAndPay() {
	// if (wx_params == null) {
	// return;
	// }
	// new AsyncTask<Void, Void, GetAccessTokenResult>() {
	//
	// @Override
	// protected GetAccessTokenResult doInBackground(Void... params) {
	// GetAccessTokenResult result = new GetAccessTokenResult();
	//
	// Log.d("get_AccessToken", "get access token, url = " +
	// wx_params.getRequestTokenUrl());
	//
	// byte[] buf = Util.httpGet(wx_params.getRequestTokenUrl());
	// if (buf == null || buf.length == 0) {
	// result.localRetCode = LocalRetCode.ERR_HTTP;
	// return result;
	// }
	//
	// String content = new String(buf);
	// result.parseFrom(content);
	// return result;
	// }
	//
	// protected void onPostExecute(GetAccessTokenResult result) {
	// if (result != null && !StringUtils.isBlank(result.accessToken)) {
	// // 让服务器生成商品参数 然后生成PrepayId
	// getProArgsandGenPrepayId(result);
	// } else {
	// Log.e("AccessToken", "AccessToken生成出错");
	// Toast.makeText(PurchaseActivity.this, "支付出现错误",
	// Toast.LENGTH_SHORT).show();
	// loading.dismiss();
	// return;
	// }
	//
	// };
	//
	// }.execute();
	// }
	//
	// /**
	// * 获取生成Prepayid所需的ProductArgs 并生成prepayId
	// *
	// * @author jeason, 2014-5-2 下午4:37:59
	// * @param result
	// */
	// protected void getProArgsandGenPrepayId(final GetAccessTokenResult
	// result) {
	// new AsyncTask<Void, Void, PrePayParamDTO>() {
	//
	// @Override
	// protected PrePayParamDTO doInBackground(Void... params) {
	//
	// try {
	// return service.genPrePayIdParam(order_num,
	// cb_accept_discount.isChecked(), result.accessToken);
	// } catch (Exception e) {
	// Log.e("IMHoliday", "genPrePayIdParam error");
	// }
	// return null;
	// }
	//
	// protected void onPostExecute(PrePayParamDTO prepay) {
	// if (prepay != null) {
	// genPrepayId(prepay);
	// } else {
	// Log.e("product_args", "商品参数生成错误");
	// }
	// };
	// }.execute();
	// }
	//
	// /**
	// * @param prepay 生成PrepayId
	// * @author jeason, 2014-5-2 下午5:01:35
	// */
	// protected void genPrepayId(final PrePayParamDTO prepay) {
	// new AsyncTask<Void, Void, GetPrepayIdResult>() {
	//
	// @Override
	// protected GetPrepayIdResult doInBackground(Void... params) {
	// // String url =
	// //
	// String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s",
	// // accessToken);
	// // String entity = genProductArgs();
	//
	// Log.d("genPrepayId", "doInBackground, url = " +
	// prepay.getRequestPrePayURL());
	// Log.d("genPrepayId", "doInBackground, entity = " +
	// prepay.getReqDataParam());
	//
	// try {
	// GetPrepayIdResult prepayResult = new GetPrepayIdResult();
	// String prdArgs = genProductArgs(prepay.getReqMap());
	// if (!StringUtils.isBlank(prdArgs)) {
	// byte[] buf = Util.httpPost(prepay.getRequestPrePayURL(), prdArgs);
	// if (buf == null || buf.length == 0) {
	// prepayResult.localRetCode = LocalRetCode.ERR_HTTP;
	// return prepayResult;
	// }
	//
	// String content = new String(buf);
	// Log.d("genPrepayId", "doInBackground, content = " + content);
	// prepayResult.parseFrom(content);
	// return prepayResult;
	// } else {
	// Log.e("ProductArgs", "服务端返回ProductArgs参数有错");
	// return null;
	// }
	//
	// } catch (Exception e) {
	//
	// }
	// return null;
	// }
	//
	// protected void onPostExecute(GetPrepayIdResult prepayId) {
	// // confirm order and pay
	// if (prepayId != null && !StringUtils.isBlank(prepayId.prepayId)) {
	// confirmAndPay(prepayId, prepay);
	// } else {
	// loading.dismiss();
	// Log.e("PrepayId encounter errors", "prepayId 生成出错");
	// Toast.makeText(PurchaseActivity.this, "订单生成出错",
	// Toast.LENGTH_SHORT).show();
	// }
	// };
	//
	// }.execute();
	// }
	//
	// private String genProductArgs(Map<String, String> args) {
	// JSONObject json = new JSONObject();
	//
	// try {
	// Iterator<Entry<String, String>> it_args = args.entrySet().iterator();
	// while (it_args.hasNext()) {
	// Entry<String, String> item = it_args.next();
	// json.put(item.getKey(), item.getValue());
	// }
	// } catch (Exception e) {
	// Log.e("args", "genProductArgs fail, ex = " + e.getMessage());
	// return null;
	// }
	// String str = json.toString();
	// return str;
	// }
	//
	// /**
	// * @param prepayId
	// * @author jeason, 2014-5-2 下午5:12:27
	// * @param prepay
	// */
	// protected void confirmAndPay(final GetPrepayIdResult prepayId, final
	// PrePayParamDTO prepay) {
	// new AsyncTask<Void, Void, ConFirmPaymentDTO>() {
	//
	// @Override
	// protected ConFirmPaymentDTO doInBackground(Void... params) {
	// try {
	// return service.conFirPaymentDTONew(prepayId.prepayId,
	// cb_accept_discount.isChecked(), order_num, prepay.getOtherParams());
	// } catch (Exception e) {
	//
	// }
	// return null;
	// }
	//
	// protected void onPostExecute(ConFirmPaymentDTO result) {
	// if (result != null) {
	// PayReq req = new PayReq();
	// Map<String, String> request_params = result.getReqParam();
	// req.appId = request_params.get("appid");
	// req.partnerId = request_params.get("partnerid");
	// req.prepayId = request_params.get("prepayid");
	// req.nonceStr = request_params.get("noncestr");
	// req.timeStamp = request_params.get("timestamp");
	// req.packageValue = request_params.get("package");
	// req.sign = request_params.get("sign");
	// api.sendReq(req);
	// } else {
	// Log.d("ConFirmPaymentDTO", "服务器端ConFirmPaymentDTO 获取出错");
	// }
	// };
	// }.execute();
	//
	// }
	//
	// private static enum LocalRetCode {
	// ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
	// }
	//
	// private static class GetAccessTokenResult {
	//
	// private static final String TAG =
	// "MicroMsg.SDKSample.PayActivity.GetAccessTokenResult";
	//
	// public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
	// public String accessToken;
	// public int expiresIn;
	// public int errCode;
	// public String errMsg;
	//
	// public void parseFrom(String content) {
	//
	// if (content == null || content.length() <= 0) {
	// Log.e(TAG, "parseFrom fail, content is null");
	// localRetCode = LocalRetCode.ERR_JSON;
	// return;
	// }
	//
	// try {
	// JSONObject json = new JSONObject(content);
	// if (json.has("access_token")) { // success case
	// accessToken = json.getString("access_token");
	// expiresIn = json.getInt("expires_in");
	// localRetCode = LocalRetCode.ERR_OK;
	// } else {
	// errCode = json.getInt("errcode");
	// errMsg = json.getString("errmsg");
	// localRetCode = LocalRetCode.ERR_JSON;
	// }
	//
	// } catch (Exception e) {
	// localRetCode = LocalRetCode.ERR_JSON;
	// }
	// }
	// }
	//
	// private static class GetPrepayIdResult {
	//
	// private static final String TAG =
	// "MicroMsg.SDKSample.PayActivity.GetPrepayIdResult";
	//
	// public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
	// public String prepayId;
	// public int errCode;
	// public String errMsg;
	//
	// public void parseFrom(String content) {
	//
	// if (content == null || content.length() <= 0) {
	// Log.e(TAG, "parseFrom fail, content is null");
	// localRetCode = LocalRetCode.ERR_JSON;
	// return;
	// }
	//
	// try {
	// JSONObject json = new JSONObject(content);
	// if (json.has("prepayid")) { // success case
	// prepayId = json.getString("prepayid");
	// localRetCode = LocalRetCode.ERR_OK;
	// } else {
	// localRetCode = LocalRetCode.ERR_JSON;
	// }
	//
	// errCode = json.getInt("errcode");
	// errMsg = json.getString("errmsg");
	//
	// } catch (Exception e) {
	// localRetCode = LocalRetCode.ERR_JSON;
	// }
	// }
	// }

}
