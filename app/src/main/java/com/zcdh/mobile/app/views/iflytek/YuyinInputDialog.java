/**
 * 
 * @author YJN, 2013-10-31 上午9:07:07
 */
package com.zcdh.mobile.app.views.iflytek;

import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.tencent.open.TaskGuide;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.utils.StringUtils;

/**
 * @author YJN, 2013-10-31 上午9:07:07
 */
public class YuyinInputDialog extends AlertDialog implements
		View.OnClickListener, MyVoiceRecognizerListner, SpeechListener{

	private static final String TAG = YuyinInputDialog.class.getSimpleName();
	

	protected static final int RESULT_SUCCESS = 1;
	protected static final int START_LISTNING = 2;
	private static final int VOICE_SEARCH_OK = 3;
	protected static final int VOICE_SEARCH_ERROR = 4;
	MediaPlayer mediaPlayer;
	RelativeLayout dialogContent;
	TextView title;
	ImageView recordImg;
	ImageView recogniztionImg;
	
	Animation waittingAnim;

	Button overBtn;
	Button cancelBtn;
	// TextView errorText;

	boolean isRecording = false; // 是否停止
	boolean canceled = false;

	MyVoiceRecognizerListner recognizerListner;

	/** 识别对象 */
	private SpeechRecognizer iatRecognizer;
	
	// 标识是否重试
	protected boolean retry;
	
	//是否说完了
	protected boolean spech_over;
	
	String result = "";
	
	boolean is_serching = false;

	/**
	 * 开始录音
	 */
	public void startRecogntion() {
		
		

		// Toast.makeText(getContext(), "开始录音", Toast.LENGTH_SHORT).show();
		if (!isShowing()) {
			show();
		}
	}

	/**
	 * 语音识别听写
	 */
	RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			Log.i("onBeginOfSpeech", "beginSpeech");
			isRecording = true;
			title.setText("倾听中...");
			cancelBtn.setVisibility(View.VISIBLE);
			overBtn.setVisibility(View.GONE);
		}

		@Override
		public void onError(SpeechError err) {
			Log.i("SpeechError", err.getErrorDescription());
			// errorText.setText(err.getErrorDescription());
			isRecording = false;
			result = "";
			cancelBtn.setVisibility(View.GONE);
			overBtn.setVisibility(View.VISIBLE);
			if(err.getErrorCode()==10118){
				title.setText("您说话声音是否太小~");
			}else{
				title.setText("倾听中...");
				cancelBtn.setVisibility(View.GONE);
				overBtn.setVisibility(View.VISIBLE);
			}
			
		}

		@Override
		public void onEndOfSpeech() {
			Log.i(TAG,"onEndOfSpeech*****************onEndOfSpeech");
			isRecording = false;
			
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, String msg) {
			Log.i(TAG,"onEvent#####################"+ eventType + ":" + msg);
			
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			Log.i("onResult------------", text + "");
			if (recognizerListner != null && !is_serching) {
				//dismiss();
				title.setText("正在识别...");
				
				recogniztionImg.setVisibility(View.VISIBLE);
				recogniztionImg.startAnimation(waittingAnim);
				Message msg = new Message();
				msg.what = RESULT_SUCCESS;
				msg.obj = text;
				//handler.sendMessageDelayed(msg, 2500);
				Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			if (volume > 0) {
				Log.i(TAG,"******************音量:********   " +  volume + "");
			}
			if (volume > 0) {
				if (volume < 5) {
					recordImg.setImageResource(R.drawable.ic_speech_1);
				} else if (volume < 15) {
					recordImg
							.setImageResource(R.drawable.ic_speech_2);
				} else if (volume < 25) {
					recordImg
							.setImageResource(R.drawable.ic_speech_3);
				} else if (volume < 35) {
					recordImg
							.setImageResource(R.drawable.ic_speech_4);
				} else if (volume < 100) {
					recordImg
							.setImageResource(R.drawable.ic_speech_5);
				}
			}
			if(volume==0){
				recordImg.setImageResource(R.drawable.ic_speech_1);
			}
		}

	};

	/**
	 * @author YJN, 2013-10-31 上午9:07:10
	 * @param context
	 */
	public YuyinInputDialog(Context context,
			MyVoiceRecognizerListner recognizerListner) {
		super(context);
		//初始化语言识别
		SpeechUser.getUser().login(context, null, null, "appid=" + Constants.IFLYTEC_APP_ID,
						this);
	}

	public YuyinInputDialog(Context context,
			MyVoiceRecognizerListner recognizerListner, int theme) {
		super(context, theme);
		this.recognizerListner = recognizerListner;
		
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化语言识别
				
		SpeechUser.getUser().login(getContext(), null, null, "appid=" + Constants.IFLYTEC_APP_ID,
								this);
		
		setContentView(R.layout.voice_recognizer_dialog);

		dialogContent = (RelativeLayout) findViewById(R.id.content);
		
		title = (TextView) findViewById(R.id.title);

		recordImg = (ImageView) findViewById(R.id.recodeImg);
		recogniztionImg = (ImageView) findViewById(R.id.recogniztionImg);

		overBtn = (Button) findViewById(R.id.overBtn);
		overBtn.setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
		
		waittingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
		
	}
	
	protected void onStart(){
		super.onStart();
		
		result = "";
		title.setText("请大声说出你想找的职位~");
		
		handler.sendEmptyMessageDelayed(START_LISTNING, 1000);
		mediaPlayer = MediaPlayer.create(getContext(), R.raw.voice_record);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.start();
		
		cancelBtn.setVisibility(View.VISIBLE);
		overBtn.setVisibility(View.GONE);
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		iatRecognizer.stopListening();
		recordImg.setImageResource(R.drawable.ic_speech_1);
		mediaPlayer.release();
		title.setText("请大声说出你想找的职位~");
		
	}

	/**
	 * 隐藏对话框
	 */
	private void myDismiss() {
		super.dismiss();

		if (dialogContent != null) {
			dialogContent.removeAllViewsInLayout();
		}
	}

	public void dismissMessage() {
		this.myDismiss();
	}

	public void dismissConfirm() {
		this.myDismiss();
	}
	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.overBtn) {
			
			result = "";
			iatRecognizer.startListening(recognizerListener);
			spech_over = false;
		}
		if(v.getId()==R.id.cancelBtn){
			dismiss();
			cancelBtn.setVisibility(View.VISIBLE);
			overBtn.setVisibility(View.GONE);
		}
	}
	
	Handler handler = new Handler(){
		
		public void handleMessage(Message msg){
			
			if(msg.what == START_LISTNING){
				// 显示听写对话框
				if(iatRecognizer!=null){
					iatRecognizer.startListening(recognizerListener);
				}
			}
			
			if(msg.what==RESULT_SUCCESS){
				
				recogniztionImg.setVisibility(View.GONE);
				recogniztionImg.clearAnimation();

				result += msg.obj;
				if(!StringUtils.isBlank(result)){
					result = result.replace(",", "");
					result = result.replace("。", "");
					title.setText("正在搜索:" +result);
					//recognizerListner.onResult(msg.obj+"");
					iatRecognizer.stopListening();
					is_serching = true;
					recognizerListner.doSearch(result);
				}
			}
			
			if(msg.what==VOICE_SEARCH_OK){
				is_serching = false;
				recognizerListner.onVoiceSearchOK();
				dismiss();
			}
			
			if(msg.what==VOICE_SEARCH_ERROR){
				is_serching = false;
				title.setText(msg.obj+"");
				cancelBtn.setVisibility(View.GONE);
				overBtn.setVisibility(View.VISIBLE);
			}
		}
	};
	
	public void onError(String error){
		Message msg = new Message();
		msg.what = VOICE_SEARCH_ERROR;
		msg.obj = error;
		handler.sendMessage(msg);
	}

	@Override
	public void onVoiceSearchOK() {
		handler.sendEmptyMessage(VOICE_SEARCH_OK);
	}

	@Override
	public void doSearch(String keyWord) {
		
	}

	@Override
	public void onCompleted(SpeechError error) {
		
		if(error==null){
			// 语音识别对象
			iatRecognizer = SpeechRecognizer.createRecognizer(getContext());
			// 清空Grammar_ID，防止识别后进行听写时Grammar_ID的干扰
			iatRecognizer.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);

			// 设置听写引擎
			iatRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");

			//YIN
			iatRecognizer.setParameter(SpeechConstant.VOLUME, "100");
			
			// 设置采样率参数，支持8K和16K
			iatRecognizer.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
			
			//开始录音
			handler.sendEmptyMessageDelayed(START_LISTNING, 1000);
		}
	}

	@Override
	public void onData(byte[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		
	}


}
