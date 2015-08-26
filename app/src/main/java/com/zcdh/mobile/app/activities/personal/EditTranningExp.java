/**
 * 
 * @author jeason, 2014-5-9 下午7:05:23
 */
package com.zcdh.mobile.app.activities.personal;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobEducationListDTO;
import com.zcdh.mobile.api.model.JobTrainDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.activities.search.CertificateFinderActivity;
import com.zcdh.mobile.app.activities.search.MajorsActivity;
import com.zcdh.mobile.app.views.EditableDialog;
import com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.ObjectUtils;
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

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

//import com.zcdh.mobile.app.activities.search.CategoryMajorActivity_;

/**
 * @author jeason, 2014-5-9 下午7:05:23
 * 添加编辑培训经历
 */
@EActivity(R.layout.add_tranning)
@OptionsMenu(R.menu.save)
public class EditTranningExp extends BaseActivity implements RequestListener, EditableDialogListener {
	JobEducationListDTO eduExpDto;

	@ViewById(R.id.tvINSTITUTION_content)
	TextView tvINSTITUTION_content;

	@ViewById(R.id.tvStartTime_content)
	TextView tvStartTime_content;

	@ViewById(R.id.tvEndTime_content)
	TextView tvEndTime_content;

	@ViewById(R.id.tvCertificate_content)
	TextView tvCertificate_content;

	@ViewById(R.id.tvCourse_content)
	TextView tvCourse_content;

	@ViewById(R.id.et_add_on)
	EditText et_add_on;

	Calendar calendar_start;

	Calendar calendar_end;

	public final int ADD_INSTITUTION_REQUEST_CODE = 0x02;
	public final int ADD_COURSE_REQUEST_CODE = 0x02;

	public final int ADD_INSTITUTION_KEY = 0x02;

	JobTrainDTO trainDtoOriginal;

	JobTrainDTO trainDtoAfter;

	public String kREQ_ID_EDITTRAIN;

	IRpcJobUservice userService;

	LoadingIndicator loading;

	public static String TRANNING_ID_KEY = "tranning_id";

	private String kREQ_ID_FINDTRAINNING;

	long tranning_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tranning_id = getIntent().getLongExtra(TRANNING_ID_KEY, 0l);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "添加培训经历");

		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

	}

	/**
	 * 
	 * @author jeason, 2014-5-10 下午2:59:34
	 */
	@AfterViews
	void initViews() {
		loading = new LoadingIndicator(this);
		loading.show();
		getData();
		et_add_on.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				trainDtoAfter.setContent(s.toString());
				notifyDataChanged();
			}
		});
	}

	boolean changed = false;

	private void notifyDataChanged() {
		changed = false;
		if (!ObjectUtils.compareTrainningDTO(trainDtoAfter, trainDtoOriginal)) {
			changed = true;
		}
		supportInvalidateOptionsMenu();
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (changed) {
			getMenuInflater().inflate(R.menu.save, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Background
	void getData() {
		userService.findTrainDTOByTrainId(tranning_id).identify(kREQ_ID_FINDTRAINNING = RequestChannel.getChannelUniqueID(), this);
	}

	@Click(R.id.rl_Institution)
	void onAddInstitution() {
		new EditableDialog(this).initData(this, ADD_INSTITUTION_KEY, "机构名称").show();
	}

	@Click(R.id.rlStartTime)
	void onClickStartTime() {
		calendar_start = Calendar.getInstance();

		new DatePickerDialog(EditTranningExp.this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				calendar_start.set(year, monthOfYear, dayOfMonth);
				tvStartTime_content.setText(DateUtils.getDateByFormatYM(calendar_start.getTime()));

				trainDtoAfter.setStartTime(calendar_start.getTime());
				notifyDataChanged();
			}
		}, calendar_start.get(Calendar.YEAR), calendar_start.get(Calendar.MONTH), calendar_start.get(Calendar.DAY_OF_MONTH)).show();

	}

	@Click(R.id.rlEndTime)
	void onClickEndTime() {
		calendar_end = Calendar.getInstance();

		new DatePickerDialog(EditTranningExp.this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				calendar_end.set(year, monthOfYear, dayOfMonth);
				tvEndTime_content.setText(DateUtils.getDateByFormatYM(calendar_end.getTime()));

				trainDtoAfter.setEndTime(calendar_end.getTime());
				notifyDataChanged();

			}
		}, calendar_end.get(Calendar.YEAR), calendar_end.get(Calendar.MONTH), calendar_end.get(Calendar.DAY_OF_MONTH)).show();

	}

	@Click(R.id.rlCourse)
	void onClickCourse() {
		// tvCourse_content.setText("养殖技术");
		// trainDto.setCourseCode("026.001.002");
		// trainDto.setCourseName("养殖技术");
		ActivityDispatcher.addTranning(this);

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
			tvCourse_content.setText(name);
			trainDtoAfter.setCourseCode(code);
			trainDtoAfter.setCourseName(name);
			notifyDataChanged();
		}
	}

	@Click(R.id.rlCertificate)
	void onClickCertificate() {
		// tvCertificate_content.setText("高级办公自动化应用师");
		// trainDto.setCredentialCode("001.003");
		// trainDto.setCredentialName("高级办公自动化应用师");
		ActivityDispatcher.addCerticate(this);
	}

	@OptionsItem(R.id.action_save)
	void onSave() {
		if (checkContent()) {
			loading.show();
			userService.updateTrain(trainDtoAfter).identify(kREQ_ID_EDITTRAIN = RequestChannel.getChannelUniqueID(), this);
		}
	}

	private boolean checkContent() {

		trainDtoAfter.setContent(et_add_on.getText().toString());

		if (trainDtoAfter.getStartTime() == null) {
			Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (trainDtoAfter.getEndTime() == null) {
			Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!trainDtoAfter.getEndTime().after(trainDtoAfter.getStartTime())) {
			Toast.makeText(this, "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (StringUtils.isBlank(trainDtoAfter.getTrainInstitution())) {
			Toast.makeText(this, "请选择培训机构", Toast.LENGTH_SHORT).show();

			return false;
		}

		if (StringUtils.isBlank(trainDtoAfter.getCourseCode())) {
			Toast.makeText(this, "请选择课程", Toast.LENGTH_SHORT).show();

			return false;
		}

		if (StringUtils.isBlank(trainDtoAfter.getCredentialCode())) {
			Toast.makeText(this, "请选择证书", Toast.LENGTH_SHORT).show();

			return false;
		}
		return true;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_INSTITUTION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
			trainDtoAfter.setTrainInstitution(data.getStringExtra("institution_name"));
			notifyDataChanged();
		}
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
		if (reqId.equals(kREQ_ID_FINDTRAINNING)) {
			if (result != null) {
				trainDtoOriginal = (JobTrainDTO) result;
				trainDtoAfter = new JobTrainDTO();
				ObjectUtils.updateObject(trainDtoOriginal, trainDtoAfter);
				initView();
			}
		}

		if (reqId.equals(kREQ_ID_EDITTRAIN)) {
			if ((Integer) result == 0) {
				// 完成添加后
				setResult(RESULT_OK);
				finish();
			}
		}

	}

	/**
	 * 
	 * @author jeason, 2014-6-23 下午6:12:42
	 */
	private void initView() {
		tvINSTITUTION_content.setText(trainDtoAfter.getTrainInstitution());
		tvStartTime_content.setText(DateUtils.getDateByFormatYM(trainDtoAfter.getStartTime()));
		tvEndTime_content.setText(DateUtils.getDateByFormatYM(trainDtoAfter.getEndTime()));
		tvCourse_content.setText(trainDtoAfter.getCourseName());
		tvCertificate_content.setText(trainDtoAfter.getCredentialName());
		et_add_on.setText(trainDtoAfter.getContent());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener#
	 * onEditableConfirm(int, java.lang.String)
	 */
	@Override
	public void onEditableConfirm(int key, String content) {
		if (key == ADD_INSTITUTION_KEY) {
			
			tvINSTITUTION_content.setText(content);
			trainDtoAfter.setTrainInstitution(content);
			notifyDataChanged();
		}
	}
	

	/**
	 * 专业列表返回值
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(CertificateFinderActivity.kREQUEST_CERTIFICATE)
	void onResultCertificate(int resultCode, Intent data) {
		if (data != null && data.getExtras() != null) {
			String code = data.getExtras().getString(CertificateFinderActivity.kDATA_CODE);
			String name = data.getExtras().getString(CertificateFinderActivity.kDATA_NAME);
			tvCertificate_content.setText(name);
			trainDtoAfter.setCredentialName(name);
			trainDtoAfter.setCredentialCode(code);
			notifyDataChanged();
		}
	}
}
