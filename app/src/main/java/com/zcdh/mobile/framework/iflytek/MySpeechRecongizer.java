package com.zcdh.mobile.framework.iflytek;

import android.content.Context;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SpeechUser.Login_State;
import com.zcdh.mobile.app.Constants;

/**
 * 语音识别
 * @author yangjiannan
 *
 */
public class MySpeechRecongizer {
	
	/**
	 * 语音识别检测时长
	 */
	public static final int VAD_BOS = 10000;
	public static final int vad_eos = 10000;

	/** 识别对象 */
	private SpeechRecognizer iatRecognizer;
	
	/**
	 * 
	 */
	private SpeechListener speechListenner;
	
	private RecognizerListener recognizerListener;
	
	/**
	 * 
	 */
	private Context context;
	
	public MySpeechRecongizer(Context context, SpeechListener speechListenner, RecognizerListener recognizerListener){
		this.context = context;
		this.speechListenner = speechListenner;
		this.recognizerListener = recognizerListener;
		if(Login_State.Unlogin==SpeechUser.getUser().getLoginState()){
			SpeechUser.getUser().login(context, null, null, "appid="+Constants.IFLYTEC_APP_ID, this.speechListenner);
		}
	}
	/**
	 * 初始化语音识别
	 */
	public void initRecognizer() {
		// 语音识别对象
		iatRecognizer = SpeechRecognizer.createRecognizer(context);
		// 清空Grammar_ID，防止识别后进行听写时Grammar_ID的干扰
		iatRecognizer.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);

		// 设置听写引擎
		iatRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");

		// YIN
		iatRecognizer.setParameter(SpeechConstant.VOLUME, "100");

		// 设置采样率参数，支持8K和16K
		iatRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
		
		//设置检测时长
		iatRecognizer.setParameter(SpeechConstant.VAD_BOS, ""+VAD_BOS);
		iatRecognizer.setParameter(SpeechConstant.VAD_EOS, "" + vad_eos);

	}

	/**
	 * 停止录音识别
	 */
	public void stopRecognizer() {
		if(iatRecognizer!=null){
			if(iatRecognizer.isListening())iatRecognizer.cancel();
		}
		//Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 开始录音识别
	 */
	public void startRecognizer() {
		initRecognizer();
		if(iatRecognizer.isListening())iatRecognizer.cancel();
		iatRecognizer.startListening(recognizerListener);
		//Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
	}
	public void stopListnning() {
		if(iatRecognizer!=null)iatRecognizer.stopListening();
	}
	
}
