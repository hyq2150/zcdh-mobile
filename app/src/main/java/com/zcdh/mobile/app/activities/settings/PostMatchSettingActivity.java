package com.zcdh.mobile.app.activities.settings;

import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobSettingDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;
import com.zcdh.mobile.R;

/**
 * 
 * @author yangjiannan 匹配度
 */
@EActivity(R.layout.post_match_setting_activity)
public class PostMatchSettingActivity extends BaseActivity implements
		RequestListener, DataLoadInterface {

	protected static final String TAG = PostMatchSettingActivity.class
			.getSimpleName();

	public static final String kACTION_MATCH_MODE = "kACTION_MATCH_MODE";

	String kREQ_ID_updateSetting;
	String kREQ_ID_findJobSettingByUserId;

	IRpcJobUservice jobUservice;

	@ViewById(R.id.scrollView)
	PullToRefreshScrollView scrollView;

	@ViewById(R.id.emptyView)
	EmptyTipView emptyView;

	@ViewById(R.id.contentView)
	LinearLayout contentView;

	/**
	 * 设置最小匹配值
	 */
	@ViewById(R.id.minMatchRateSB)
	SeekBar matchSettingSB;

	/**
	 * 匹配设置
	 */
	@ViewById(R.id.matchSettingLl)
	LinearLayout matchSettingLl;

	@ViewById(R.id.minMatchRateText)
	TextView matchRateText;

	/**
	 * 开启匹配
	 */
	@ViewById(R.id.matchRateToggleChk)
	CheckBox matchRateToggleChk;

	/**
	 * 设置项数据
	 */
	HashMap<String, JobSettingDTO> settings = new HashMap<String, JobSettingDTO>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jobUservice = RemoteServiceManager
				.getRemoteService(IRpcJobUservice.class);
	}

	@Background
	public void loadData() {
		jobUservice
				.findJobSettingByUserId(getUserId())
				.identify(
						kREQ_ID_findJobSettingByUserId = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Background
	void saveSetting(JobSettingDTO settingDTO) {
		jobUservice.updateSetting(settingDTO).identify(
				kREQ_ID_updateSetting = RequestChannel.getChannelUniqueID(),
				this);
	}

	@AfterViews
	void bindViews() {

		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), "岗位匹配");

		matchSettingSB
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					int progressChanged = 0;

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						progressChanged = progress;
						Log.i(TAG, progress + "");
						matchRateText.setText(progress + "%");
					}

					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						// Toast.makeText(PostMatchSettingActivity.this,"seek bar progress:"+progressChanged,
						// Toast.LENGTH_SHORT).show();
						JobSettingDTO dto = new JobSettingDTO();
						dto.setCode(Constants.SettingKeys.POST_MATCH_RATE_KEY);
						dto.setUserId(getUserId());
						dto.setValue(progressChanged + "");
						saveSetting(dto);

					}
				});

		// 加载数据
		loadData();
	}

	void showSetting() {
		JobSettingDTO matchRateOpen = settings
				.get(Constants.SettingKeys.POST_MATCH_MODE_KEY);
		JobSettingDTO matchRateValue = settings
				.get(Constants.SettingKeys.POST_MATCH_RATE_KEY);
		if (matchRateOpen != null && "1".equals(matchRateOpen.getValue())) { // 已开启岗位匹配
			matchSettingLl.setVisibility(View.VISIBLE);
			matchRateToggleChk.setChecked(true);

		} else {// 未开启

			matchSettingLl.setVisibility(View.GONE);
		}

		int progress = 0;
		if (matchRateValue != null && matchRateValue.getValue() != null) {
			try {
				progress = Integer.valueOf(matchRateValue.getValue());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		matchRateText.setText(progress + "%");
		matchSettingSB.setProgress(progress);

	}

	@Click(R.id.matchRateToggleChk)
	void onSeekBarChanged() {
		boolean checked = matchRateToggleChk.isChecked();

		matchSettingLl.setVisibility(checked ? View.VISIBLE : View.GONE);
		JobSettingDTO dto = new JobSettingDTO();
		dto.setCode(Constants.SettingKeys.POST_MATCH_MODE_KEY);
		dto.setUserId(getUserId());
		dto.setValue(checked ? 1 + "" : 0 + "");
		saveSetting(dto);
	}

	@Override
	public void onRequestStart(String reqId) {
		if (reqId.equals(kREQ_ID_updateSetting)) {
			matchRateToggleChk.setEnabled(false);
		}
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findJobSettingByUserId)) {
			if (result != null) {
				List<JobSettingDTO> jobSettings = (List<JobSettingDTO>) result;
				for (JobSettingDTO jobSettingDTO : jobSettings) {
					settings.put(jobSettingDTO.getCode(), jobSettingDTO);
				}
				showSetting();
			}
			contentView.setVisibility(View.VISIBLE);
			emptyView.isEmpty(false);
		}

		if (reqId.equals(kREQ_ID_updateSetting)) {
			// Toast.makeText(getApplicationContext(), "success",
			// Toast.LENGTH_SHORT).show();
			sendBroadcast(new Intent(kACTION_MATCH_MODE));

		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		if (reqId.equals(kREQ_ID_updateSetting)) {
			matchRateToggleChk.setEnabled(true);

		}
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException) error, this);
	}
}
