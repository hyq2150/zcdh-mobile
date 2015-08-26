package com.zcdh.mobile.app.views.iflytek;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SpeechUser.Login_State;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.utils.StringUtils;

/**
 * 
 * @author yangjiannan 讯飞语音听写输入
 */
public class YuYinInputView extends PopupWindow implements SpeechListener,
		RecognizerListener, OnClickListener {

	private static final String TAG = YuYinInputView.class.getSimpleName();

	/**
	 * 识别的内容
	 */
	private StringBuffer content;

	private Activity context;

	private YuyinInputListner inputListner;

	DisplayMetrics dm;
	/**
	 * 显示语音输入界面
	 */
	private View SpeechInpuView;

	/**
	 * 说完了
	 */
	private ImageButton overBtn;
	/**
	 * 继续听写按钮
	 */
	private ImageButton cancelBtn;
	/**
	 * 显示音量波动的图片
	 */
	private ImageView soundValueImg;

	/**
	 * 显示识别的文字
	 */
	private TextView contentTxt;

	/** 识别对象 */
	private SpeechRecognizer iatRecognizer;

	/**
	 * 标识是否正在录音
	 * 
	 * @param context
	 */
	boolean FLAG_RECORDING;

	public YuYinInputView(Context context, YuyinInputListner inputListner) {
		this.context = (Activity) context;
		this.inputListner = inputListner;
		bindViews();
	}

	/**
	 * 初始化绑定views
	 */
	private void bindViews() {

		SpeechInpuView = LayoutInflater.from(context).inflate(
				R.layout.fw_speech_inputview, null);

		overBtn = (ImageButton) SpeechInpuView.findViewById(R.id.overBtn);
		cancelBtn = (ImageButton) SpeechInpuView.findViewById(R.id.cancelBtn);
		soundValueImg = (ImageView) SpeechInpuView
				.findViewById(R.id.soundValueImg);
		contentTxt = (TextView) SpeechInpuView.findViewById(R.id.contentTxt);

		overBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);

		// 设置PopuWindow
		setContentView(SpeechInpuView);
		dm = context.getResources().getDisplayMetrics();
		setWidth(dm.widthPixels);
		setHeight(LayoutParams.MATCH_PARENT);
		setOutsideTouchable(true);
	}

	/**
	 * 初始化语音识别
	 */
	private void initRecognizer() {
		// 语音识别对象
		iatRecognizer = SpeechRecognizer.createRecognizer(this.context);
		// 清空Grammar_ID，防止识别后进行听写时Grammar_ID的干扰
		iatRecognizer.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);

		// 设置听写引擎
		iatRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");

		// YIN
		iatRecognizer.setParameter(SpeechConstant.VOLUME, "100");

		// 设置采样率参数，支持8K和16K
		iatRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, "16000");

	}

	/**
	 * 开始录音识别
	 */
	private void startRecognizer() {
		initRecognizer();
		iatRecognizer.startListening(this);
	}

	/**
	 * SpeechListener 讯飞语音用户验证完成,之后初始化语音识别（要验证完成后再设置语音识别）
	 */
	@Override
	public void onCompleted(SpeechError error) {
		// Toast.makeText(context, "logined", Toast.LENGTH_SHORT).show();
		if (error == null) {
			// 开始录音
			startRecognizer();
		}
	}

	@Override
	public void onData(byte[] arg0) {

	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {
		Log.e("onEvent ..... ", arg1 + "");
	}

	@Override
	public void onClick(View v) {
		// if(v.getId()==R.id.cancelBtn){
		// hiddenInputView();
		// }
		if (v.getId() == R.id.overBtn) {
			iatRecognizer.stopListening();
			FLAG_RECORDING = false;
//			contentTxt.setText("正在识别...");
		}
		if (v.getId() == R.id.cancelBtn) {
			iatRecognizer.cancel();
			dismiss();
		}
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		if (iatRecognizer.isListening()) {
			iatRecognizer.cancel();
		}
	}

	/**
	 * RecognizerListener
	 */
	@Override
	public void onBeginOfSpeech() {
		FLAG_RECORDING = true;
	}

	@Override
	public void onEndOfSpeech() {
		FLAG_RECORDING = false;
		try {
			if (isShowing()) {
				dismiss();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// Toast.makeText(context, "stop speech", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(SpeechError err) {
		// Toast.makeText(context, err.getPlainDescription(true),
		// Toast.LENGTH_SHORT).show();
		if (isShowing()) {
			dismiss();
		}
	}

	@Override
	public void onEvent(int arg0, int arg1, int arg2, String arg3) {
		// Toast.makeText(context, "onevent", Toast.LENGTH_SHORT).show();
		if (!iatRecognizer.isListening()) {
			FLAG_RECORDING = false;
		}
	}

	@Override
	public void onResult(RecognizerResult results, boolean arg1) {
		String text = JsonParser.parseIatResult(results.getResultString());
		if (text != null) {
			text = text.replace("。", ",");
			text = text.replace(",", "");
		}
		if (!StringUtils.isBlank(text)) {
			if (content == null)
				content = new StringBuffer();
			content.append(text);
		}

		if (!StringUtils.isBlank(text)) {
			contentTxt.setText(content);
			if (this.inputListner != null && content != null
					&& !"".equals(content.toString())) {
				this.inputListner.onComplete(text);
			}
		}
		// 接收结果后，如果录音已经停止，取消显示语音识别
		// Toast.makeText(context, iatRecognizer.isListening() + "",
		// Toast.LENGTH_SHORT).show();
		if (!FLAG_RECORDING) {
			dismiss();
		}
	}

	@Override
	public void onVolumeChanged(int volume) {
		if (volume > 0) {
			Log.i(TAG,"******************音量:********"+volume + "");
		}
		if (volume > 0) {
			if (volume < 5) {
				soundValueImg.setImageResource(R.drawable.ic_speech_2);
			} else if (volume < 15) {
				soundValueImg.setImageResource(R.drawable.ic_speech_2);
			} else if (volume < 25) {
				soundValueImg.setImageResource(R.drawable.ic_speech_3);
			} else if (volume < 35) {
				soundValueImg.setImageResource(R.drawable.ic_speech_4);
			} else if (volume < 100) {
				soundValueImg.setImageResource(R.drawable.ic_speech_5);
			}
		}
		if (volume == 0) {
			soundValueImg.setImageResource(R.drawable.ic_speech_1);
		}
	}

	/**
	 * 显示语音听写视图
	 * 
	 * @param v
	 */
	public void showAtParent(View v) {
		if (isShowing()) {
			return;
		}
		contentTxt.setText("正在倾听,请开始说话~");
		content = new StringBuffer();
		// 1)登录讯飞语音
		if (Login_State.Unlogin == SpeechUser.getUser().getLoginState()) {
			SpeechUser.getUser().login(context, null, null,
					"appid=" + Constants.IFLYTEC_APP_ID, this);
		} else {
			startRecognizer();
		}
		showAtLocation(v, Gravity.BOTTOM, 0, 0);
	}

}
