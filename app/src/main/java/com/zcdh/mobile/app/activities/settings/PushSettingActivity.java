package com.zcdh.mobile.app.activities.settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobSettingDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 订阅推送设置
 * 
 * @author yangjiannan 2014-05-21
 */
@EActivity(R.layout.activity_push_setting)
public class PushSettingActivity extends BaseActivity implements RequestListener {

	

	String kREQ_ID_updateSetting;
	String kREQ_ID_findJobSettingByUserId;

	IRpcJobUservice jobUservice;

	@ViewById(R.id.wifi_rl)
	RelativeLayout wifiLayout;

	@ViewById(R.id.time_rl)
	RelativeLayout timeLayout;

	@ViewById(R.id.timeLl)
	LinearLayout timeLl;

	@ViewById(R.id.interval_rl)
	RelativeLayout inLayout;

	/**
	 * 开启推送
	 */
	@ViewById(R.id.pushToggleChk)
	CheckBox pushToggleChk;

	/**
	 * wifi环境下推送
	 */
	@ViewById(R.id.wifiPushToggleChk)
	CheckBox wifiToggleChk;

	/**
	 * 开启设置推送时间
	 */
	@ViewById(R.id.pushTimeToggleChk)
	CheckBox pushTimeToggleChk;

	/**
	 * 开始时间
	 */
	@ViewById(R.id.startText)
	TextView startText;

	/**
	 * 结束时间
	 */
	@ViewById(R.id.endText)
	TextView endText;

	/**
	 * 推送间隔
	 */
	@ViewById(R.id.intervalText)
	TextView pushIntervalText;

	HashMap<String, JobSettingDTO> settings = new HashMap<String, JobSettingDTO>();

	/**
	 * 推送间隔选项
	 */
	String[] pushIntervals = null;

	// 时间单位
	private static final int UNIT_MINITE = 0;
	private static final int UNIT_HOURS = 1;

	private int current_time_unit = -1;

	/**
	 * 时间格式化
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	
	
	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activity_title_push));
		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		loadSettings();
	}

	@Background
	void loadSettings() {
		jobUservice.findJobSettingByUserId(getUserId()).identify(kREQ_ID_findJobSettingByUserId = RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 保存设置
	 */
	@Background
	void updateSetting(JobSettingDTO settingDTO) {
		settingDTO.setUserId(getUserId());
		jobUservice.updateSetting(settingDTO).identify(kREQ_ID_updateSetting = RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 开启与关闭推送
	 * 
	 * @param cb
	 * @param isChecked
	 */
	@CheckedChange(R.id.pushToggleChk)
	void onPushToggled(CompoundButton cb, boolean isChecked) {

		JobSettingDTO jobSettingDTO = new JobSettingDTO();
		jobSettingDTO.setCode(Constants.SettingKeys.kSETTING_PUSH_TOGGLE);
		jobSettingDTO.setValue("" + (isChecked ? 1 : 0));
		settings.put(Constants.SettingKeys.kSETTING_PUSH_TOGGLE, jobSettingDTO);
		updateSetting(jobSettingDTO);
		showSettings();
	}

	/**
	 * 在wifi 环境下接收推送
	 * 
	 * @param cb
	 * @param isChecked
	 */
	@CheckedChange(R.id.wifiPushToggleChk)
	void onWifiPushToggle(CompoundButton cb, boolean isChecked) {
		JobSettingDTO jobSettingDTO = new JobSettingDTO();
		jobSettingDTO.setCode(Constants.SettingKeys.kSETTING_PUSH_WIFI_TOGGLE);
		jobSettingDTO.setValue("" + (isChecked ? 1 : 0));
		settings.put(Constants.SettingKeys.kSETTING_PUSH_WIFI_TOGGLE, jobSettingDTO);
		updateSetting(jobSettingDTO);
		showSettings();
	}

	/**
	 * 设置推送时间段开关
	 * 
	 * @param cb
	 * @param isChecked
	 */
	@CheckedChange(R.id.pushTimeToggleChk)
	void onPushTimeToggle(CompoundButton cb, boolean isChecked) {
		JobSettingDTO st = new JobSettingDTO();
		st.setCode(Constants.SettingKeys.kSETTING_PUSH_TIME_TOGGLE);
		if (isChecked) {
			st.setValue(1 + "");

		} else {
			st.setValue(0 + "");

		}
		settings.put(Constants.SettingKeys.kSETTING_PUSH_TIME_TOGGLE, st);
		updateSetting(st);
		showSettings();
	}

	/**
	 * 设置日期
	 * 
	 * @param start :0 开始 1 结束
	 * 
	 */
	void setTimeAction(final int start) {

		final Calendar date = Calendar.getInstance();

		final TimePickerDialog datePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				JobSettingDTO st = new JobSettingDTO();
				

				if (start == 0) { // 开始时间
					if (settings.get(Constants.SettingKeys.kSETTING_PUSH_END_TIME) != null) {
						
						try {
							long min = sdf.parse(hourOfDay+":"+minute).getTime();
							long end = sdf.parse(settings.get(Constants.SettingKeys.kSETTING_PUSH_END_TIME).getValue()).getTime();
							
							if (end < min) {
								Toast.makeText(getApplicationContext(), "开始时间要小于结束时间", Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					st.setCode(Constants.SettingKeys.kSETTING_PUSH_START_TIME);
					st.setValue(hourOfDay + ":" + minute);
					settings.put(Constants.SettingKeys.kSETTING_PUSH_START_TIME, st);
				} 
				if(start==1){ // 结束时间
					if (settings.get(Constants.SettingKeys.kSETTING_PUSH_START_TIME) != null) {
						//Toast.makeText(getApplicationContext(), settings.get(Constants.SettingKeys.kSETTING_PUSH_START_TIME).getValue()+"", Toast.LENGTH_SHORT).show();
						try {
							long min = sdf.parse(hourOfDay+":"+minute).getTime();
							long start = sdf.parse(settings.get(Constants.SettingKeys.kSETTING_PUSH_START_TIME).getValue()).getTime();
							if (start > min) {
								Toast.makeText(getApplicationContext(), "结束时间要大于开始时间", Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					st.setCode(Constants.SettingKeys.kSETTING_PUSH_END_TIME);
					st.setValue(hourOfDay + ":" + minute);
					settings.put(Constants.SettingKeys.kSETTING_PUSH_END_TIME, st);
				}
				updateSetting(st);
				showSettings();
			}
		}, date.HOUR_OF_DAY, date.MINUTE, true);

		// 设置点击其他不能取消
		datePickerDialog.setCancelable(true);

		datePickerDialog.show();
	}

	/**
	 * 设置开始时间
	 */
	@Click(R.id.startTime_rl)
	void onStartTimeClick() {
		setTimeAction(0);
	}

	/**
	 * 设置结束时间
	 */
	@Click(R.id.endTime_rl)
	void onEndTimeClick() {
		setTimeAction(1);
	}

	/**
	 * 选择推送间隔
	 * @throws ParseException 
	 */
	@Click(R.id.interval_rl)
	void onChosenInterval()  {

		String hasSetPushTime = "";
		if (settings.get(Constants.SettingKeys.kSETTING_PUSH_TIME_TOGGLE) != null) {

			hasSetPushTime = settings.get(Constants.SettingKeys.kSETTING_PUSH_TIME_TOGGLE).getValue();
		}
		if ("1".equals(hasSetPushTime)) {
			long max = 0;

			JobSettingDTO startTime = settings.get(Constants.SettingKeys.kSETTING_PUSH_START_TIME);
			JobSettingDTO endTime = settings.get(Constants.SettingKeys.kSETTING_PUSH_END_TIME);
			if (startTime != null && endTime != null) {
				try {
					
					long start = sdf.parse(startTime.getValue()).getTime();
					long end = sdf.parse(endTime.getValue()).getTime();
					max = (end - start)/1000 / 60 /60;   //=====  毫秒/秒/分钟
 					//Toast.makeText(this, max + "", Toast.LENGTH_SHORT).show();
					
					if (max >= 1) {
						current_time_unit = UNIT_HOURS;
						pushIntervals = new String[(int)max];
						for (int i = 0; i < max; i++) {
							pushIntervals[i] = (i + 1) + "h";
						}
						
					} else {
						current_time_unit = UNIT_MINITE;
						long a = end - start;
						if(a<0)a = Math.abs(a);
						pushIntervals = new String[(int)a/1000/60];
						for (int i = 0; i < pushIntervals.length; i++) {
							pushIntervals[i] = (i + 1) + "m";
							
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				

			}
		} else {

			pushIntervals = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"

			};
		}

		new AlertDialog.Builder(this).setCancelable(true).setTitle("推送间隔").setItems(pushIntervals, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				setInterval(which);
			}
		}).create().show();
	}

	void setInterval(int which) {
		//Toast.makeText(this, pushIntervals[which] + "", Toast.LENGTH_SHORT).show();
		JobSettingDTO intervalSetting = new JobSettingDTO();
		intervalSetting.setCode(Constants.SettingKeys.kSETTING_PUSH_INTERVAL_TOGGLE);
		intervalSetting.setValue(pushIntervals[which]);
		updateSetting(intervalSetting);
		settings.put(Constants.SettingKeys.kSETTING_PUSH_INTERVAL_TOGGLE, intervalSetting);
		showSettings();
	}

	void showSettings() {
		JobSettingDTO pushToggle = settings.get(Constants.SettingKeys.kSETTING_PUSH_TOGGLE);
		JobSettingDTO wifiToggle = settings.get(Constants.SettingKeys.kSETTING_PUSH_WIFI_TOGGLE);
		JobSettingDTO pushTimeToggle = settings.get(Constants.SettingKeys.kSETTING_PUSH_TIME_TOGGLE);
		JobSettingDTO start = settings.get(Constants.SettingKeys.kSETTING_PUSH_START_TIME);
		JobSettingDTO end = settings.get(Constants.SettingKeys.kSETTING_PUSH_END_TIME);
		JobSettingDTO interval = settings.get(Constants.SettingKeys.kSETTING_PUSH_INTERVAL_TOGGLE);

		if (pushToggle != null) {
			String checked = pushToggle.getValue();
			if ("1".equals(checked)) {
				pushToggleChk.setChecked(true);

				wifiLayout.setVisibility(View.VISIBLE);
				timeLayout.setVisibility(View.VISIBLE);
				timeLl.setVisibility(View.VISIBLE);
				inLayout.setVisibility(View.VISIBLE);

			} else {
				pushToggleChk.setChecked(false);

				wifiLayout.setVisibility(View.GONE);
				timeLayout.setVisibility(View.GONE);
				timeLl.setVisibility(View.GONE);
				inLayout.setVisibility(View.GONE);
			}
		}

		if (wifiToggle != null) {
			String checked = wifiToggle.getValue();
			if ("1".equals(checked)) {
				wifiToggleChk.setChecked(true);
			} else {
				wifiToggleChk.setChecked(false);
			}
		}

		if (start != null) {
//			Integer startTime = Integer.valueOf(start.getValue());
//			String min = 0 + "";
//			if (startTime % 60 == 0) {
//				min = "00";
//			} else {
//				min = (startTime % 60) + "";
//			}
			//String startTimeStr = (startTime / 60) + ":" + min;
			String startTimeStr = start.getValue();
			if(!StringUtils.isBlank(startTimeStr)){
				startText.setText(startTimeStr);
			}

		}
		if (end != null) {
//			Integer endTime = Integer.valueOf(end.getValue());
//			String min = 0 + "";
//			if (endTime % 60 == 0) {
//				min = "00";
//			} else {
//				min = (endTime % 60) + "";
//			}
//			String endTimeStr = (endTime / 60) + ":" + min;
			String endTimeStr = end.getValue();
			if(!StringUtils.isBlank(endTimeStr)){
				endText.setText(endTimeStr);
			}

		}
		if (interval != null) {
			if (interval.getValue() != null && !"".equals(interval)) {
				String intervalStr = interval.getValue();
				pushIntervalText.setText(intervalStr);
				inLayout.setVisibility(View.VISIBLE);
			}
		}

		if (pushTimeToggle != null) {
			String checked = pushTimeToggle.getValue();
			String checked1 = null;
			if (pushToggle != null) {
				checked1 = pushToggle.getValue();
			}

			if ("1".equals(checked)) {
				pushTimeToggleChk.setChecked(true);
				timeLl.setVisibility(View.VISIBLE);
				if (!"1".equals(checked1)) {
					timeLl.setVisibility(View.GONE);
					inLayout.setVisibility(View.GONE);
				}
			} else {
				pushTimeToggleChk.setChecked(false);
				timeLl.setVisibility(View.GONE);
				inLayout.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		// 加载设置
		if (reqId.equals(kREQ_ID_findJobSettingByUserId)) {
			if (result != null) {
				List<JobSettingDTO> jobSettings = (List<JobSettingDTO>) result;
				for (JobSettingDTO jobSettingDTO : jobSettings) {
					settings.put(jobSettingDTO.getCode(), jobSettingDTO);
				}
			}
			showSettings();
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

}
