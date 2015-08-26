/**
 * 
 * @author jeason, 2014-5-9 下午7:04:47
 */
package com.zcdh.mobile.app.activities.personal;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobEducationDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.activities.search.MajorsActivity;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

//import com.zcdh.mobile.app.activities.search.CategoryMajorActivity_;

/**
 * @author jeason, 2014-5-9 下午7:04:47
 * 添加教育经历
 */
@EActivity(R.layout.edit_exp)
@OptionsMenu(R.menu.save)
public class AddEduExpActivity extends BaseActivity implements RequestListener {
	@ViewById(R.id.tvInstitution_content)
	TextView tvInstitution_content;

	@ViewById(R.id.tvStartTime_content)
	TextView tvStartTime_content;

	@ViewById(R.id.tvEndTime_content)
	TextView tvEndTime_content;

	@ViewById(R.id.tvSpecialty_content)
	TextView tvSpecialty_content;

	@ViewById(R.id.tvDiploma_content)
	TextView tvDiploma_content;

	@ViewById(R.id.et_add_on)
	EditText et_add_on;

	Calendar calendar_start;

	Calendar calendar_end;

	JobEducationDTO eduDto;

	public final int ADD_SCHOOL_REQUEST_CODE = 0x01;
	public final int ADD_MAJOR_REQUEST_CODE = 0x01;

	public String kREQ_ID_ADDEDUCATION;

	IRpcJobUservice userService;
	LoadingIndicator loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eduDto = new JobEducationDTO();
		eduDto.setUserId(getUserId());
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "添加教育经历");
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
	}

	/**
	 * 
	 * @author jeason, 2014-5-10 下午2:59:34
	 */
	@AfterViews
	void initViews() {
		loading = new LoadingIndicator(this);
	}

	@Click(R.id.rlInstitution)
	void onClickInstitution() {
		ActivityDispatcher.toSchoolFinder(this, ADD_SCHOOL_REQUEST_CODE);
	}

	@Click(R.id.rlStartTime)
	void onClickStartTime() {
		calendar_start = Calendar.getInstance();

		new DatePickerDialog(AddEduExpActivity.this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				calendar_start.set(year, monthOfYear, dayOfMonth);
				tvStartTime_content.setText(DateUtils.getDateByFormatYM(calendar_start.getTime()));

				eduDto.setStartTime(calendar_start.getTime());
			}
		}, calendar_start.get(Calendar.YEAR), calendar_start.get(Calendar.MONTH), calendar_start.get(Calendar.DAY_OF_MONTH)).show();

	}

	@Click(R.id.rlEndTime)
	void onClickEndTime() {
		calendar_end = Calendar.getInstance();

		new DatePickerDialog(AddEduExpActivity.this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				calendar_end.set(year, monthOfYear, dayOfMonth);
				tvEndTime_content.setText(DateUtils.getDateByFormatYM(calendar_end.getTime()));

				eduDto.setEndTime(calendar_end.getTime());

			}
		}, calendar_end.get(Calendar.YEAR), calendar_end.get(Calendar.MONTH), calendar_end.get(Calendar.DAY_OF_MONTH)).show();

	}

	@OptionsItem(R.id.action_save)
	void onSave() {
		if (checkContent()) {
			loading.show();
			doAddEducation();
		}
	}

	@Background
	void doAddEducation() {
		userService.addEducation(eduDto).identify(kREQ_ID_ADDEDUCATION=RequestChannel.getChannelUniqueID(), this);
	}

	private boolean checkContent() {
		eduDto.setContent(et_add_on.getText().toString());

		if (eduDto.getStartTime() == null) {
			Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (eduDto.getEndTime() == null) {
			Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!eduDto.getEndTime().after(eduDto.getStartTime())) {
			Toast.makeText(this, "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtils.isBlank(eduDto.getSchoolName())) 
		{
			Toast.makeText(this, "请选择学校", Toast.LENGTH_SHORT).show();
			return false;
		}


		if (StringUtils.isBlank(eduDto.getDegreeCode())) {
			Toast.makeText(this, "请选择学历", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (StringUtils.isBlank(eduDto.getMajorCode())) {
			Toast.makeText(this, "请选择专业", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_SCHOOL_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			tvInstitution_content.setText(data.getStringExtra("school_name"));
			eduDto.setSchoolCode(data.getStringExtra("school_code"));
			eduDto.setSchoolName(data.getStringExtra("school_name"));
		}
	}

	/**
	 * 专业列表返回值
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(MajorsActivity.kREQUEST_MAJOR)
	void onResultMajor(int resultCode, Intent data) {
		if (data != null && data.getExtras() != null) {
			String code = data.getExtras().getString(MajorsActivity.kDATA_CODE);
			String name = data.getExtras().getString(MajorsActivity.kDATA_NAME);
			tvSpecialty_content.setText(name);
			eduDto.setMajorCode(code);
			eduDto.setMajorName(name);
		}
	}

	@Click(R.id.rlSpecialty)
	void onAddMajor() {
		ActivityDispatcher.addMajor(this);
	}

	// 学历
	@Click(R.id.rlDiploma)
	void onClickDiploma() {
		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.diploma).setItems(R.array.diploma, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tvDiploma_content.setText(getResources().getStringArray(R.array.diploma)[which]);
				eduDto.setDegreeName(getResources().getStringArray(R.array.diploma)[which]);
				eduDto.setDegreeCode(getResources().getStringArray(R.array.diploma_codes)[which]);
			}
		}).create().show();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestSuccess(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		loading.dismiss();
		if (reqId.equals(kREQ_ID_ADDEDUCATION)) {
			if ((Integer) result == 0) {
				// 完成添加后
				setResult(RESULT_OK);
				finish();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestFinished(java.
	 * lang.String)
	 */
	@Override
	public void onRequestFinished(String reqId) {
		loading.dismiss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.framework.nio.RequestListener#onRequestError(java.lang
	 * .String, java.lang.Exception)
	 */
	@Override
	public void onRequestError(String reqID, Exception error) {
		loading.dismiss();

	}
}
