package com.zcdh.mobile.framework.activities;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechUser;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.views.iflytek.JsonParser;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.iflytek.MySpeechRecongizer;
import com.zcdh.mobile.utils.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 语音识别
 * 
 * @author yangjiannan
 * 
 */
public class FWSpeechRecognizerActivity extends BaseActivity implements
		SpeechListener, RecognizerListener, OnClickListener {
	
	public static final int kREQUEST_SPEECH_RECONGIZER = 2016;

	public static final String kSPEECH_RESULT = "kSPEECH_RESULT";

	public static final String kEVENT_SPEECH_RESULT = "kEVENT_SPEECH_RESULT";

//	/** 识别对象 */
//	private SpeechRecognizer iatRecognizer;

	/**
	 * 标识是否正在录音
	 * 
	 * @param context
	 */
	boolean FLAG_RECORDING;

//	/**
//	 * 显示识别的文字
//	 */
//	private TextView contentTxt;

	/**
	 * 显示音量波动的图片
	 */
	private ImageView micImg;

	/**
	 * 识别的内容
	 * 
	 * @param savedInstanceState
	 */
	StringBuffer content = new StringBuffer();
	
	/**
	 * 
	 */
	
	MySpeechRecongizer speechRecongizer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fw_acivity_speech_recognizer);
		// 绑定视图
		bindViews();
		// 初始化语音
		
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		end();
		return super.onTouchEvent(event);
	}


	
	void bindViews() {
		
//		contentTxt = (TextView) findViewById(R.id.contextText);
		micImg = (ImageView) findViewById(R.id.micImg);
		
		//初始化语音识别组件
		speechRecongizer =  new MySpeechRecongizer(this, this, this);
	}
	
	
	private void end(){
		// 1)停止录音
		speechRecongizer.stopRecognizer();
		// 2)注销讯飞语音
		SpeechUser.getUser().logout();
		// 3）发布接收的语音识别结果
		MyEvents.post(kEVENT_SPEECH_RESULT, content+"");
		
		finish();
	}

	/**
	 * SpeechListener 讯飞语音用户验证完成,之后初始化语音识别（要验证完成后再设置语音识别）
	 */
	@Override
	public void onCompleted(SpeechError error) {
		if (error == null) {
			// 开始录音
			speechRecongizer.startRecognizer();
		}
	}
	
	

	@Override
	public void onData(byte[] arg0) {
		
	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {
		Log.i("onEvent ..... ", arg1 + "");
	}

	@Override
	public void onClick(View v) {

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
		end();
	}

	@Override
	public void onError(SpeechError err) {
		 //Toast.makeText(this, err.getPlainDescription(true),Toast.LENGTH_SHORT).show();
		end();
	}

	@Override
	public void onEvent(int arg0, int arg1, int arg2, String arg3) {
		// Toast.makeText(this, "onevent", Toast.LENGTH_SHORT).show();
//		if (!iatRecognizer.isListening()) {
//			FLAG_RECORDING = false;
//			end();
//		}
	}

	@Override
	public void onResult(RecognizerResult results, boolean arg1) {
		String text = JsonParser.parseIatResult(results.getResultString());
		if (text != null) {
			text = text.replace("。", ",");
			text = text.replace(",", "");
		}
		if (!StringUtils.isBlank(text)) {
//			if (content == null)
//				content = new StringBuffer();
//			content.append(text);
//			contentTxt.setText(content.toString());
			Intent data = new Intent();
			data.putExtra(kSPEECH_RESULT, text);
			setResult(RESULT_OK, data);
			
			content.append(text);
		}
		end();
	}

	@Override
	public void onVolumeChanged(int volume) {
		if (volume > 0) {
			Log.i("*******音量:********", volume + "");
		}
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

}
