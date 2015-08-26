package com.zcdh.mobile.wxapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zcdh.mobile.api.IRpcHolidayService;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.utils.StringUtils;

public class WXPayEntryActivity extends BaseActivity implements
		IWXAPIEventHandler {

	public static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	private IRpcHolidayService holidayService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory
				.createWXAPI(
						this,
						com.zcdh.mobile.app.activities.vacation.payment.Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		// if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle(R.string.app_tip);
		// builder.setMessage(getString(R.string.pay_result_callback_msg,
		// String.valueOf(resp.errCode)));
		// builder.show();
		// }

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Intent intent = new Intent(TAG);
			intent.putExtra("errCode", resp.errCode);

			if (resp.errCode == 0) {
				paySuccess.execute();
				sendBroadcast(intent);
				ActivityDispatcher.toOrders(this);
			}
			finish();
		}
	}

	AsyncTask<Void, Void, Void> paySuccess = new AsyncTask<Void, Void, Void>() {

		@Override
		protected Void doInBackground(Void... params) {
			String order_num = ZcdhApplication.getInstance().getOrder_num();
			holidayService = RemoteServiceManager
					.getRemoteService(IRpcHolidayService.class);

			if (!StringUtils.isBlank(order_num) && holidayService != null)
				holidayService.paySuccess(order_num);
			ZcdhApplication.getInstance().setOrder_num(null);
			return null;
		}

	};
}