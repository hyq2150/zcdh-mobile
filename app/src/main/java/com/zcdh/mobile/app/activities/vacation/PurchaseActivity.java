/**
 * @author jeason, 2014-4-22 下午4:44:01
 */
package com.zcdh.mobile.app.activities.vacation;

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
import com.zcdh.mobile.utils.ToastUtil;
import com.zcdh.mobile.wxapi.WXPayEntryActivity;

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

import java.math.BigDecimal;
import java.util.Map;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase);

        pay_receiver = new PayReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WXPayEntryActivity.TAG);
        this.registerReceiver(pay_receiver, filter);
        api = WXAPIFactory.createWXAPI(this,
                com.zcdh.mobile.app.activities.vacation.payment.Constants.APP_ID);
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
        SystemServicesUtils.displayCustomTitle(this, getSupportActionBar(), "订单支付");
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
                if (order_num == null) {
                    return null;
                }
                try {
                    service.toPrePayFor(order_num)
                            .identify(kREQ_ID_TOPREPAYFOR = RequestChannel.getChannelUniqueID(),
                                    PurchaseActivity.this);
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
        original_total_price.setText("¥" + payForDto.getEarnestMoney()
                .multiply(new BigDecimal(payForDto.getPostCount())));
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
            duration.setText(String.format("工作期间：%s~%s",
                    DateUtils.getDateByFormat(payfordto.getStartTime(), "yyyy-MM-dd"),
                    DateUtils.getDateByFormat(payfordto.getEndTime(), "yyyy-MM-dd")));
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
        purchase_title = (TextView) findViewById(R.id.purchase_title);
        ent_name = (TextView) findViewById(R.id.ent_name);
        address = (TextView) findViewById(R.id.address);
        duration = (TextView) findViewById(R.id.duration);
        tv_total_price = (TextView) findViewById(R.id.total_price);
        original_total_price = (TextView) findViewById(R.id.original_total_price);
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
        final boolean isChecked=cb_accept_discount.isChecked();
        new AsyncTask<Void, Void, ConFirmPaymentDTO>() {

            @Override
            protected ConFirmPaymentDTO doInBackground(Void... params) {
                try {
                    service.conformOrderByWeChat(order_num, isChecked)
                            .identify(kREQ_ID_CONFORMORDERBYWECHAT = RequestChannel
                                    .getChannelUniqueID(), PurchaseActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ConFirmPaymentDTO result) {
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
        if (payForDto == null) {
            return;
        }
        boolean use_discount = false;

        if (cb_accept_discount.isChecked()) {
            use_discount = true;
        }

        total_price = payForDto.getEarnestMoney()
                .multiply(new BigDecimal(payForDto.getPostCount()));
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
                ToastUtil.show("订单生成错误");
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

}
