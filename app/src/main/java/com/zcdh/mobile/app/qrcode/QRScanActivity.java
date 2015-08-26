package com.zcdh.mobile.app.qrcode;

import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.EntInfoDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.ent.MainEntActivity_;
import com.zcdh.mobile.app.activities.newsmodel.NewsBrowserActivity_;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;

import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:25:31
 * 
 * 版本: V_1.0.0
 * 
 * 描述: 扫描界面
 */
public class QRScanActivity extends BaseActivity implements Callback,
		RequestListener {

	private static final String TAG = QRScanActivity.class.getSimpleName();

	private IRpcJobEnterpriseService enterpriseService;
	private EntInfoDTO entInfoDTO;
	private String kREQ_ID_findEntInfoDTOByShortURL;
	private ProcessDialog processDialog;

	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	private boolean isNeedCapture = false;

	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qr_scan);
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);

		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE,
				0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);

		enterpriseService = RemoteServiceManager
				.getRemoteService(IRpcJobEnterpriseService.class);
		processDialog = new ProcessDialog(this);
	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();

		if (!TextUtils.isEmpty(result)) {
			// e: http://www.zcdhjob.com/1/xasdfax
			String chars = "http://www.zcdhjob.com/1/";
			String url = result;
			Log.e(TAG, "url : " + result);
			if (result.contains(chars)) {
				final String code = url.replace(chars, "");
				if (!TextUtils.isEmpty(code)) {
					processDialog.show("正在处理...");
					new Thread() {
						public void run() {
							enterpriseService
									.findEntInfoDTOByShortURL(code)
									.identify(
											kREQ_ID_findEntInfoDTOByShortURL = RequestChannel
													.getChannelUniqueID(),
											QRScanActivity.this);
						}
					}.start();
				}
			} else if (result.contains("http://")) {
				if (url.contains(Constants.URL_SCAN_FAIR_POST) // 投简历
						|| url.contains(Constants.URL_SIGNIN_FAIR)// 签到
				) {
					if (ZcdhApplication.getInstance().getZcdh_uid() > 0) {
						url = url + "&userId="
								+ ZcdhApplication.getInstance().getZcdh_uid();
						if (getIntent().getBooleanExtra("fromMessageCenter",
								false)) {
							if (url.contains(Constants.URL_SIGNIN_FAIR)) {// 签到
								doSignIn(url);
							}
							if (url.contains(Constants.URL_SCAN_FAIR_POST)) {// 投简历
								doSign(url);
							}
							finish();
						} else {
							if (url.contains(Constants.URL_SCAN_FAIR_POST)) {// 投简历
								if (!RegisterUtil.isRegisterUser(this)) {
									Toast.makeText(this, "尚未登录！",
											Toast.LENGTH_SHORT).show();
									ActivityDispatcher.to_login(this);
									finish();
									return;
								}
								if (ZcdhApplication.getInstance().getJobUserResumeMiddleDTO() != null
										&& ZcdhApplication.getInstance().getJobUserResumeMiddleDTO().getIsCanSignUp() == 0) {
									if (ZcdhApplication.getInstance().getRequisiteDTO() != null
										&& ZcdhApplication.getInstance().getRequisiteDTO().getIsUserRequisiteFilled() == 0) {
										Toast.makeText(this, "资料尚未完整", Toast.LENGTH_SHORT).show();
										ActivityDispatcher.toBasicInfo(this);
										return;
									}
									Toast.makeText(this, "简历尚未完整", Toast.LENGTH_SHORT).show();
									ActivityDispatcher.toMyResumeActivity(this);
									return;
								}
							}
							Intent intent = new Intent();
							intent.putExtra("URL", url);
							setResult(RESULT_OK, intent);
							finish();
						}
					} else {
						Toast.makeText(this,
								getResources().getString(R.string.login_first),
								Toast.LENGTH_SHORT).show();
						ActivityDispatcher.to_login(this);
						finish();
					}
				} else {
					NewsBrowserActivity_.intent(this).url(result).start();
					finish();
				}
			} else {
				Toast.makeText(this, result + "", Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			Toast.makeText(this, "无法识别", Toast.LENGTH_SHORT).show();
			finish();
		}

		// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
		// handler.sendEmptyMessage(R.id.restart_preview);
	}

	private static final int handlerSuccess = 1001;
	private static final int handlerFair = 1002;
	private static final int handlerSign = 1003;
	private final MyHandler signHandler = new MyHandler(this);

	private static class MyHandler extends Handler {
		private final WeakReference<QRScanActivity> mActivity;

		public MyHandler(QRScanActivity activity) {
			super();
			// TODO Auto-generated constructor stub
			mActivity = new WeakReference<QRScanActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			QRScanActivity activity = mActivity.get();
			switch (msg.what) {
			case handlerSuccess:
				Toast.makeText(activity, "签到成功", Toast.LENGTH_SHORT).show();
				break;
			case handlerSign:
				Toast.makeText(activity, "已签到", Toast.LENGTH_SHORT).show();
				break;
			case handlerFair:
				Toast.makeText(activity, "签到失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	}

	// 投简历
	private void doSign(final String url) {
		if (!RegisterUtil.isRegisterUser(this)) {
			Toast.makeText(this, "尚未登录！", Toast.LENGTH_SHORT).show();
			ActivityDispatcher.to_login(this);
			return;
		}
		if (ZcdhApplication.getInstance().getJobUserResumeMiddleDTO() != null
				&& ZcdhApplication.getInstance().getJobUserResumeMiddleDTO().getIsCanSignUp() == 0) {
			if (ZcdhApplication.getInstance().getRequisiteDTO() != null
				&& ZcdhApplication.getInstance().getRequisiteDTO().getIsUserRequisiteFilled() == 0) {
				Toast.makeText(this, "资料尚未完整", Toast.LENGTH_SHORT).show();
				ActivityDispatcher.toBasicInfo(this);
				return;
			}
			Toast.makeText(this, "简历尚未完整", Toast.LENGTH_SHORT).show();
			ActivityDispatcher.toMyResumeActivity(this);
			return;
		}
		NewsBrowserActivity_.intent(this).url(url).start();
	}

	/**
	 * 签到
	 * 
	 * @param url
	 */
	private void doSignIn(final String url) {
		if (!RegisterUtil.isRegisterUser(this)) {
			Toast.makeText(this, "尚未登录！", Toast.LENGTH_SHORT).show();
			ActivityDispatcher.to_login(this);
			return;
		}
		BackgroundExecutor.execute(new Task("", 0, "") {
			@Override
			public void execute() {
				// TODO Auto-generated method stub
				try {
					HttpGet httpRequest = new HttpGet(url);// 建立http get联机
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest);// 发出http请求
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(httpResponse
								.getEntity());// 获取相应的字符串
						Log.e("Job", "result : " + result);
						JSONObject jsonObject = new JSONObject(result);
						/**
						 * 0 签到成功 1 异常 2 已签到
						 */
						if (jsonObject.getInt("result") == 0) {
							signHandler.sendEmptyMessage(handlerSuccess);
						} else if (jsonObject.getInt("result") == 1) {
							signHandler.sendEmptyMessage(handlerFair);
						} else if (jsonObject.getInt("result") == 2) {
							signHandler.sendEmptyMessage(handlerSign);
						}
					} else {
						signHandler.sendEmptyMessage(handlerFair);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					handler.sendEmptyMessage(handlerFair);
				}
			}
		});
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width
					/ mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height
					/ mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);

		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(QRScanActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findEntInfoDTOByShortURL)) {
			if (result != null) {
				entInfoDTO = (EntInfoDTO) result;
				processDialog.dismiss();
				MainEntActivity_.intent(QRScanActivity.this)
						.entId(entInfoDTO.getEntId()).start();
				finish();
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