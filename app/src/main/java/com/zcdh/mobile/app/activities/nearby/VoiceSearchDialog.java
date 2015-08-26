package com.zcdh.mobile.app.activities.nearby;

import java.util.Timer;
import java.util.UUID;

import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SpeechUser.Login_State;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.iflytek.JsonParser;
import com.zcdh.mobile.framework.iflytek.MySpeechRecongizer;
import com.zcdh.mobile.utils.StringUtils;

/**
 * 语音搜索对话框（喊工作）,类似微信语音操作方式
 * 
 * @author yangjiannan
 * 
 */
public class VoiceSearchDialog extends Dialog implements SpeechListener,
		RecognizerListener {

	private static final int COUNT_DOWN_LIMIT = 5000;
	private static final int COUNT_DOWN_MESSAGE = 1;
	private String COUNT_DOWN_ID = "";
	/**
	 * 标题
	 */
	TextView titleTxt;

	/**
	 * 取消图标
	 */
	ImageView noMicImg;

	/**
	 * 麦克风录音图标
	 */
	ImageView micImg;

	/***
	 * 松手取消
	 */
	TextView cancelTipsTxt;

	/**
	 * 上滑取消
	 */
	TextView swipeCancelTipTxt;


	private Context context;

	/**
	 * 识别的内容
	 */
	private String sayContent = "";

	/**
	 * 当说完，马上放手的时候，后台还在继续识别刚刚说过的话.
	 * 标识是否后台进行语音识别
	 */
	//private boolean isRecongizBackground;
	
	/**
	 * 标识正在语音对话框
	 */
	//private boolean isShowingVoice;
	/**
	 * 标识当前状态是可录音
	 */
	private boolean isWillVoice;
	/**
	 * 标识当前是将要取消状态
	 */
	private boolean isWillCancel;

	/**
	 * 语音识别类
	 */
	MySpeechRecongizer speechRecongizer;
	
	/**
	 * 
	 */
	VoiceSearchDialogListnner voiceSearchDialogListnner;

	/**
	 * 
	 * @param context
	 * @param theme
	 */
	public VoiceSearchDialog(Context context, int theme , VoiceSearchDialogListnner voiceSearchDialogListnner) {
		super(context, theme);
		this.context = context;
		setContentView(R.layout.voice_search_dialog);
		this.voiceSearchDialogListnner = voiceSearchDialogListnner;
		bindView();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (isShowing())
				dismiss();
		}
		return super.onTouchEvent(event);
	}

	private void bindView() {
		titleTxt = (TextView) findViewById(R.id.tipsTxt);
		noMicImg = (ImageView) findViewById(R.id.noMicImg);
		micImg = (ImageView) findViewById(R.id.micImg);
		cancelTipsTxt = (TextView) findViewById(R.id.cancelTipsTxt);
		swipeCancelTipTxt = (TextView) findViewById(R.id.swipeCancelTipTxt);
	}

	/**
	 * 设置录音识别状态
	 */
	public void beginVoiceTheme() {
		if (!isWillVoice) {
			//Toast.makeText(getContext(), "beginVoiceTheme", Toast.LENGTH_SHORT).show();
			// 1) 开始启动录音识别
			if (speechRecongizer == null) {
				speechRecongizer = new MySpeechRecongizer(getContext(), this,
						this);
			} else {
				speechRecongizer.startRecognizer();
			}
			// 2）设置识别内容
			if (StringUtils.isBlank(sayContent)) {
				String tips = "请说话，如这样说<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;“会计师”";
				titleTxt.setText(Html.fromHtml(tips));
			} else {
				titleTxt.setText(sayContent);
			}
			// 3)底部的士文字
			swipeCancelTipTxt.setText("手指上滑，取消");
			// 录音识别图
			noMicImg.setVisibility(View.GONE);
			micImg.setVisibility(View.VISIBLE);
			// 设置底部操作提示
			cancelTipsTxt.setVisibility(View.GONE);
			swipeCancelTipTxt.setVisibility(View.VISIBLE);

			isWillVoice = true;
			isWillCancel = false;

		}
	}

	/**
	 * 设置取消状态
	 */
	public void endVoiceTheme() {

		if (!isWillCancel) {
			// 停止录音
			if(speechRecongizer!=null){
				speechRecongizer.stopRecognizer();
			}

			// 设置识别内容
			titleTxt.setText("需要取消吗？");
			
			// 录音识别图
			noMicImg.setVisibility(View.VISIBLE);
			micImg.setVisibility(View.GONE);

			// 设置底部操作提示
			cancelTipsTxt.setVisibility(View.VISIBLE);
			swipeCancelTipTxt.setVisibility(View.GONE);
			

			isWillVoice = false;
			isWillCancel = true;
			
			COUNT_DOWN_ID = UUID.randomUUID().toString();
			h.removeMessages(COUNT_DOWN_MESSAGE);
		}
	}
	
	/**
	 * 松手结束语音识别录音
	 */
	public void endVoice(){
		//endVoiceTheme();
		//isRecongizBackground = true;
		speechRecongizer.stopListnning();
		isWillVoice = false;
		isWillCancel = true;
		//isShowingVoice = false;
		super.cancel();
		
	}
	

	public void cancel() {
		super.cancel();
		//isShowingVoice = false;
		endVoiceTheme();	
		// 重置结果
		sayContent = "";
	}

	public void show() {
		super.show();
		//isShowingVoice = true;
		sayContent =  "";
		beginVoiceTheme();
	}

	/**
	 * 获取语音识别出的内容
	 * 
	 * @return
	 */
	public String getSayContent() {
		return sayContent;
	}

	/**
	 * 倒计时
	 */
	public void setCountDown() {

		//Toast.makeText(getContext(), "countDown", Toast.LENGTH_SHORT).show();
		BackgroundExecutor.execute(new Task(null, 0, null) {

			

			@Override
			public void execute() {
				try {
					
					int have = COUNT_DOWN_LIMIT/1000;
					String id = UUID.randomUUID().toString();
					COUNT_DOWN_ID = id;
					while(id.equals(COUNT_DOWN_ID) && have>=0 ){
						
						//倒计时期间，Dialog 关闭
						if(!isShowing() || !isWillVoice){
							break;
						}
						
						//倒计时结束
						if(have==0){
							h.sendEmptyMessage(-1);
							break;
						}
						
						Message msg = new Message();
						msg.arg1 = have;
						msg.what = 0; // 倒计时
						h.sendMessage(msg);
						
						Thread.sleep(1000);
						Log.i("voice","setCountDown******************************************"+ have + "");
						have--;
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	final Handler h = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				//Toast.makeText(getContext(), msg.arg1 + "", Toast.LENGTH_SHORT).show();
				swipeCancelTipTxt.setText("剩余时间，" + msg.arg1 + "秒");
				break;

			case COUNT_DOWN_MESSAGE:
				setCountDown();
				break;
			case -1:
				// 如果是录音状态才取消对话框
				if(isWillVoice){
					cancel();
				}
				break;
			}
		}
	};

	/* ================ 语音回调 ==================== */
	@Override
	public void onBeginOfSpeech() {
		// 开始倒计时，延时执行： 总的录音监听时间(10000ms) - 倒计时时间(5000ms)
		h.sendEmptyMessageDelayed(COUNT_DOWN_MESSAGE,
				MySpeechRecongizer.VAD_BOS - COUNT_DOWN_LIMIT);
		//Toast.makeText(getContext(), "beginOfSpeech", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEndOfSpeech() {
		// 移除倒计时
		h.removeMessages(COUNT_DOWN_MESSAGE);
	}

	@Override
	public void onError(SpeechError e) {
		if (e != null && e.getErrorCode() == 10118) {
			//Toast.makeText(getContext(), "没说话!", Toast.LENGTH_SHORT).show();
			// speechRecongizer.startRecognizer();
			
		}
		//Toast.makeText(getContext(), "onError:" + e.getErrorDescription(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEvent(int arg0, int arg1, int arg2, String arg3) {

	}

	@Override
	public void onResult(RecognizerResult results, boolean arg1) {
		String text = JsonParser.parseIatResult(results.getResultString());
		if (text != null) {
			text = text.replace("。", ",");
			text = text.replace(",", "");
		}
		if(!StringUtils.isBlank(text)){
			
			if(isShowing()){
				//Toast.makeText(getContext(), "frontground:" + text, Toast.LENGTH_SHORT).show();
				if (isWillVoice) {
					sayContent += text;
					titleTxt.setText(sayContent);
				}
			}else{
				//Toast.makeText(getContext(), "background:" + text, Toast.LENGTH_SHORT).show();
				voiceSearchDialogListnner.onVoiceResult(text);
				endVoiceTheme();
			}
		}
	}

	@Override
	public void onVolumeChanged(int volume) {
		if (volume > 0) {
			if (volume < 5) {
				micImg.setImageResource(R.drawable.ic_speech_2);
			} else if (volume < 15) {
				micImg.setImageResource(R.drawable.ic_speech_2);
			} else if (volume < 25) {
				micImg.setImageResource(R.drawable.ic_speech_3);
			} else if (volume < 35) {
				micImg.setImageResource(R.drawable.ic_speech_4);
			} else if (volume < 100) {
				micImg.setImageResource(R.drawable.ic_speech_5);
			}
		}
		if (volume == 0) {
			micImg.setImageResource(R.drawable.ic_speech_1);
		}
	}

	/* ================== 登录语音回调 ==================== */
	@Override
	public void onCompleted(SpeechError e) {
		if (e == null) {
			speechRecongizer.startRecognizer();
		}
	}

	@Override
	public void onData(byte[] arg0) {

	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {

	}


}
