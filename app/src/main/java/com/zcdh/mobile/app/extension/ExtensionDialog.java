package com.zcdh.mobile.app.extension;

import java.util.Map;

import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobAppService;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
import com.zcdh.mobile.app.extension.ExtensionInputView.onTextChangedListener;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SharedPreferencesUtil;

@SuppressLint("InflateParams")
public class ExtensionDialog extends Dialog implements
		View.OnClickListener, onTextChangedListener,
		RequestListener {
	private Context context;
	private TextView contentTv, tip;
	private LinearLayout titleLayout;
	private View okBtn, cancelBtn;
	private ExtensionInputView extension;
	private String kREQ_ID_dealInvitationcode;
	private IRpcJobAppService service;
	private String imei;
	public final static String EXTENSION_FIRST = "extension_first";
	private boolean isFirst;

	public ExtensionDialog(Context context,boolean isFirst) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		service = RemoteServiceManager
				.getRemoteService(IRpcJobAppService.class);

		imei = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		this.context = context;
//		setCancelable(false);
		this.isFirst = isFirst;
		initalize();
	}

	private void initalize() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.extensiondialog, null);
		setContentView(view);
		initWindow();
		titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		extension = (ExtensionInputView) findViewById(R.id.extension);
		extension.setListener(this);
		contentTv = (TextView) findViewById(R.id.text_view);
		tip = (TextView) findViewById(R.id.tip);
		contentTv.setVisibility(View.INVISIBLE);
		okBtn = findViewById(R.id.btn_ok);
		cancelBtn = findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);
		okBtn.setOnClickListener(this);
		okBtn.setClickable(false);
		okBtn.setFocusable(false);
		okBtn.setEnabled(false);
		
		dealInvitationcode();
	}

	private void initWindow() {
		Window dialogWindow = getWindow();
		dialogWindow.setBackgroundDrawable(new ColorDrawable(0));
		dialogWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics();
		lp.width = (int) (d.widthPixels * 0.8);
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}

	public void dealInvitationcode() {
		final String account = SharedPreferencesUtil.getValue(context,
				LoginHelper.kLOGIN_ACCOUNT, null);
		BackgroundExecutor.execute(new Task("", 0, "") {

			@Override
			public void execute() {
				// TODO Auto-generated method stub
				service.dealInvitationcode(extension.getExtension().toString(),
						ZcdhApplication.getInstance().getZcdh_uid(), account,
						imei).identify(
						kREQ_ID_dealInvitationcode = RequestChannel
								.getChannelUniqueID(), ExtensionDialog.this);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:
			if (cancelBtn.isShown()) {
				dealInvitationcode();
			} else {
				super.dismiss();
			}
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		default:
			break;
		}
	}

	private void showTip(String tipStr) {
		SpannableStringBuilder builder = new SpannableStringBuilder(tipStr);
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(context
				.getResources().getColor(R.color.extension));
		builder.setSpan(greenSpan, 9, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tip.setVisibility(View.VISIBLE);
		tip.setText(builder);
		extension.setVisibility(View.INVISIBLE);
		contentTv.setVisibility(View.INVISIBLE);
		titleLayout.setVisibility(View.GONE);
		cancelBtn.setVisibility(View.GONE);
		okBtn.setEnabled(true);
		okBtn.setClickable(true);
		super.show();
	}

	@Override
	public void onTextChanged(CharSequence text) {
		// TODO Auto-generated method stub
		if (text.toString().length() != 0) {
			contentTv.setVisibility(View.INVISIBLE);
		}
		if (text.toString().length() == 4) {
			okBtn.setFocusable(true);
			okBtn.setClickable(true);
			okBtn.setEnabled(true);
		} else {
			okBtn.setClickable(false);
			okBtn.setFocusable(false);
			okBtn.setEnabled(false);
		}
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		// TODO Auto-generated method stub
		if (reqId.equals(kREQ_ID_dealInvitationcode)) {
			Map<String, String> map = (Map<String, String>) result;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				Log.e("extension", entry.getKey() + "--->" + entry.getValue());
			}
			switch (Integer.valueOf(map.get("type"))) {
			case 0:// 0-为安装后首次启动
				Log.e("extension", "0-为安装后首次启动");
				super.show();
				SharedPreferencesUtil.putValue(getContext(), EXTENSION_FIRST,true);
				break;
			case 2:// 2-邀请码不合法
				Log.e("extension", "2-邀请码不合法");
				contentTv.setVisibility(View.VISIBLE);
				contentTv.setText(context.getResources().getString(
						R.string.extension_error));
				super.show();
				break;
			case 1:// 1-为安装后非首次启动
				SharedPreferencesUtil.putValue(getContext(), EXTENSION_FIRST,true);
				if (isFirst) {
					super.dismiss();
					break;
				}
			case 5:// 5-该手机已使用邀请码进行过注册
			case 3:// 3-该手机已使用邀请码进行过安装
				Log.e("extension", "3-该手机已使用邀请码进行过安装");
				String tipStr = "已填写过邀请码 : " + map.get("invitationcode")
						+ " \n" + "无需再次输入";
				showTip(tipStr);
				break;
			case 6:// 6-使用邀请码进行注册(成功或失败)
			case 4:// 4-使用邀请码进行安装(成功或失败)
				Log.e("extension", "4-使用邀请码进行安装(成功或失败)");
				super.dismiss();
				switch (Integer.valueOf(map.get("success"))) {
				case 0:
					Toast.makeText(context, "提交邀请码失败", Toast.LENGTH_SHORT)
							.show();
					break;
				case 1:
					Toast.makeText(context, "提交邀请码成功", Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					break;
				}
				break;
			case 7:// 7-推广人员不需填写邀请码
				Log.e("extension", "7-推广人员不需填写邀请码");
				String tipStr1 = "我的推广码邀请码 : " + map.get("invitationcode");
				showTip(tipStr1);
				break;
			case 8://8-该用户账号已注册过邀请码
				Toast.makeText(context, "该用户账号已注册过邀请码", Toast.LENGTH_SHORT)
				.show();
				break;
			default:
				break;
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

	}
}
