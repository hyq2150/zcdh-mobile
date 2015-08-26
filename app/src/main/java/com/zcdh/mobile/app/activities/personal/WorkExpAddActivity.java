package com.zcdh.mobile.app.activities.personal;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobObjectiveIndustryDTO;
import com.zcdh.mobile.api.model.JobWorkExperiencePostDTO;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.search.CategoryPostActivity_;
import com.zcdh.mobile.app.activities.search.IndustryActivity;
import com.zcdh.mobile.app.activities.search.IndustryActivity_;
import com.zcdh.mobile.app.activities.search.PostsActivity;
import com.zcdh.mobile.app.views.EditableDialog;
import com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * 新增工作经验
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_work_exp_add)
@OptionsMenu(R.menu.save)
public class WorkExpAddActivity extends BaseActivity implements EditableDialogListener, RequestListener {

	public static final int kREQUEST_WORK_EXP = 2007;

	private static final int kEDIT_COMPANY_NAME = 10;

	private static final String kDATA_WORK_EXP_ADDED = "kDATA_WORK_EXP_ADD";

	private static final String kDATA_WORK_EXP_EDITED = "kDATA_WORK_EXP_EDITED";

//	public static final String kFLAG_OPERATOR = "kFLAG_OPERATOR"; // 0 标识增加

	protected static final String TAG = WorkExpAddActivity.class.getSimpleName();
																	// 1标识修改

	private String kREQ_ID_addJobWorkExperience;

	private String kREQ_ID_findJobWorkExperiencePostDTO;

	private String kREQ_ID_updateJobWorkExperience;

	private IRpcJobUservice jobUservice;

	@ViewById(R.id.scrollView)
	ScrollView scrollView;

	/**
	 * 工作描述
	 * 
	 */
	@ViewById(R.id.postDesc)
	EditText postDesc;

	/**
	 * 工作经历表单
	 */
	@ViewById(R.id.workExpAddListView)
	ListViewInScrollView workExpFormListView;

	private WorkExpFromAdapter workExpFromAdapter;

	/**
	 * 工作经历
	 */
	@Extra
	JobWorkExperiencePostDTO jobWorkExperiencePostDTO = new JobWorkExperiencePostDTO();

	@Extra
	long jobWorkExperienceId;
	/**
	 * 表单项标题
	 */
	private String[] titles;

	/**
	 * 日期格式化
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日",Locale.CHINA);

	@AfterViews
	void bindViews() {

		if (jobWorkExperienceId > 0) {
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activiyt_work_exp_edit));
		} else {
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activiyt_work_exp_add));
		}
		postDesc.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				jobWorkExperiencePostDTO.setPostDesc(s.toString());
			}
		});

		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

		titles = new String[] { getString(R.string.companyName), getString(R.string.startTime), getString(R.string.endTime), getString(R.string.industry_category), getString(R.string.post_category) };

		workExpFromAdapter = new WorkExpFromAdapter();

		workExpFormListView.setAdapter(workExpFromAdapter);

		scrollView.smoothScrollBy(0, 0);

		loadWorkExpById();
	}

	void loadWorkExpById() {
		jobUservice.findJobWorkExperiencePostDTO(jobWorkExperienceId).identify(kREQ_ID_findJobWorkExperiencePostDTO = RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 添加工作经验
	 */
	@Background
	void saveWorkExp() {
		if (jobWorkExperienceId > 0) {// 如果工作经验ID 大于零，则为更新
			jobUservice.updateJobWorkExperience(jobWorkExperiencePostDTO).identify(kREQ_ID_updateJobWorkExperience = RequestChannel.getChannelUniqueID(), this);
		} else {
			jobWorkExperiencePostDTO.setUserId(ZcdhApplication.getInstance().getZcdh_uid());
			jobUservice.addJobWorkExperience(jobWorkExperiencePostDTO).identify(kREQ_ID_addJobWorkExperience = RequestChannel.getChannelUniqueID(), this);
		}
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		// 根据ID 查询
		if (reqId.equals(kREQ_ID_findJobWorkExperiencePostDTO)) {
			if (result != null) {
				jobWorkExperiencePostDTO = (JobWorkExperiencePostDTO) result;
				workExpFromAdapter.notifyDataSetChanged();
				if (jobWorkExperiencePostDTO.getPostDesc() != null) {
					postDesc.setText(jobWorkExperiencePostDTO.getPostDesc());
				}
			}
		}

		// 添加工作经验
		if (reqId.equals(kREQ_ID_addJobWorkExperience)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) { // 添加成功
					Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra(kDATA_WORK_EXP_ADDED, jobWorkExperiencePostDTO);
//					data.putExtra(kFLAG_OPERATOR, 0);
					setResult(RESULT_OK, data);
					finish();
				} else {
					Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
				}
			}
		}

		// 更新
		if (reqId.equals(kREQ_ID_updateJobWorkExperience)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra(kDATA_WORK_EXP_EDITED, jobWorkExperiencePostDTO);
//					data.putExtra(kFLAG_OPERATOR, 1);
					setResult(RESULT_OK, data);
					finish();
				}
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
		error.printStackTrace();
	}

	/**
	 * 返回
	 */
	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
	}

	/**
	 * 保存
	 */
	@OptionsItem(R.id.action_save)
	void onSave() {
		boolean valided = true;
		String errorMsg = "";
		if (postDesc.getText().toString().length() > 256) {
			valided = false;
			errorMsg = "工作描述超过256字";
		}else if (postDesc.getText().toString().length() < 1) {
			valided = false;
			errorMsg = "请输入256字之内的工作描述";
		}
		if (StringUtils.isBlank(jobWorkExperiencePostDTO.getCorName())) {
			valided = false;
			errorMsg = "请输入公司名称";
		} else if (jobWorkExperiencePostDTO.getStartTime() == null) {
			valided = false;
			errorMsg = "请选择入职时间";
		} else if (jobWorkExperiencePostDTO.getEndTime() == null) {
			valided = false;
			errorMsg = "请选择离职时间";
		} else if (StringUtils.isBlank(jobWorkExperiencePostDTO.getIndustryCode())) {
			valided = false;
			errorMsg = "请选择从事行业";
		} else if (StringUtils.isBlank(jobWorkExperiencePostDTO.getPostCode())) {
			valided = false;
			errorMsg = "请选择从事职位";
		}

		if (valided) {
			saveWorkExp();
		} else {
			Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
		}
	}

	@ItemClick(R.id.workExpAddListView)
	void onItemClick(int position) {
		switch (position) {
		case 0://jobWorkExperiencePostDTO.getCorName()
			if (jobWorkExperienceId > 0) {
				new EditableDialog(this).initData(this, kEDIT_COMPANY_NAME, getString(R.string.dialog_title_commpany_name), jobWorkExperiencePostDTO.getCorName(), -1).show();
			} else {
				new EditableDialog(this).initData(this, kEDIT_COMPANY_NAME, getString(R.string.dialog_title_commpany_name),jobWorkExperiencePostDTO.getCorName() != null ? jobWorkExperiencePostDTO.getCorName():"").show();
			}
			break;
		case 1: // 开始时间
			setDateAction(0);
			break;
		case 2:// 结束时间
			setDateAction(1);
			break;
		case 3:
			JobObjectiveIndustryDTO ind = new JobObjectiveIndustryDTO();
			ind.setCode(jobWorkExperiencePostDTO.getIndustryCode());
			ind.setName(jobWorkExperiencePostDTO.getIndustryName());
			HashMap<String, JobObjectiveIndustryDTO> selectionIndusties = new HashMap<String, JobObjectiveIndustryDTO>();
			selectionIndusties.put(ind.getCode(), ind);
			IndustryActivity_.intent(this).signle(true).selectedIndustries(selectionIndusties).startForResult(
				IndustryActivity.kREQUEST_INDUSTRY);
			break;
		case 4:
			CategoryPostActivity_.intent(this).single(true).startForResult(PostsActivity.kREQUEST_POST);
			break;

		}
	}

	@Override
	public void onEditableConfirm(int key, String content) {
		if (key == kEDIT_COMPANY_NAME && content != null) {
			jobWorkExperiencePostDTO.setCorName(content.trim());
			workExpFromAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 设置日期
	 * 
	 * @param start :0 开始 1 结束
	 * 
	 */
	void setDateAction(final int start) {

		final Calendar date = Calendar.getInstance();

		final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Date tempDate = null;
				String dateStr = String.format("%s-%s-%s",  year, monthOfYear+1, dayOfMonth);
				try {
					tempDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				// if(onSetDateOK){
				if (start == 0) {// 开始时间
					if (jobWorkExperiencePostDTO.getEndTime() != null && jobWorkExperiencePostDTO.getEndTime().getTime() <= tempDate.getTime()) {
						Toast.makeText(getApplication(), "开始时间应小于结束时间", Toast.LENGTH_SHORT).show();
					} else {
						jobWorkExperiencePostDTO.setStartTime(tempDate);
					}

				}

				if (start == 1) {// 结束时间
						
					if (jobWorkExperiencePostDTO.getStartTime() != null && jobWorkExperiencePostDTO.getStartTime().getTime() >= tempDate.getTime()) {

						Toast.makeText(getApplication(), "结束时间应大于开始时间", Toast.LENGTH_SHORT).show();
					} else {
						if (tempDate.getTime() > new Date().getTime()) {
							Toast.makeText(getApplicationContext(), "结束时间不能大于今天", Toast.LENGTH_SHORT).show();
						} else {
							jobWorkExperiencePostDTO.setEndTime(tempDate);
						}
					}
				}
				workExpFromAdapter.notifyDataSetChanged();
			}
		}, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

		// 设置点击其他不能取消
		datePickerDialog.setCancelable(true);

		datePickerDialog.show();
	}

	/**
	 * 接收选择的行业
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(IndustryActivity.kREQUEST_INDUSTRY)
	void onResultIndustry(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String code = data.getExtras().getString(IndustryActivity.kDATA_CODE);
			String name = data.getExtras().getString(IndustryActivity.kDATA_NAME);

			jobWorkExperiencePostDTO.setIndustryCode(code);
			jobWorkExperiencePostDTO.setIndustryName(name);

			workExpFromAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 接收选择的职位类别
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(PostsActivity.kREQUEST_POST)
	void onResultPOST(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

			String code = data.getExtras().getString(PostsActivity.kDATA_CODE);
			String name = data.getExtras().getString(PostsActivity.kDATA_NAME);

			jobWorkExperiencePostDTO.setPostCode(code);
			jobWorkExperiencePostDTO.setPostName(name);

			workExpFromAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 高级搜索设置表单列表适配
	 * 
	 * @author yangjiannan
	 * 
	 */
	class WorkExpFromAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Object getItem(int p) {
			return p;
		}

		@Override
		public long getItemId(int p) {
			return p;
		}

		@Override
		public View getView(int p, View contentView, ViewGroup arg2) {

			Holder h = null;
			if (contentView == null) {
				h = new Holder();
				contentView = LayoutInflater.from(getBaseContext()).inflate(R.layout.simple_listview_item_pairing, null);

				h.itemName = (TextView) contentView.findViewById(R.id.itemNameText);
				h.itemValue = (TextView) contentView.findViewById(R.id.secondItemNameText);
				contentView.setTag(h);
			} else {
				h = (Holder) contentView.getTag();
			}

			h.itemName.setText(titles[p]);
			String valueName = "";
			switch (p) {
			case 0:// 公司名称
				valueName = jobWorkExperiencePostDTO.getCorName();
				break;
			case 1:// 开始时间
				if (jobWorkExperiencePostDTO.getStartTime() != null) valueName = sdf.format(jobWorkExperiencePostDTO.getStartTime());
				break;
			case 2:// 结束时间
				if (jobWorkExperiencePostDTO.getEndTime() != null) valueName = sdf.format(jobWorkExperiencePostDTO.getEndTime());
				break;
			case 3:// 行业类别
				valueName = jobWorkExperiencePostDTO.getIndustryName();
				break;
			case 4: // 职位类别
				valueName = jobWorkExperiencePostDTO.getPostName();
				break;
			}

			if (!StringUtils.isBlank(valueName)) {
				h.itemValue.setText(valueName);
			} else {
				h.itemValue.setText("");
				h.itemValue.setHint(getString(R.string.chosen));
			}

			return contentView;
		}

		class Holder {
			TextView itemName = null;
			TextView itemValue = null;
		}

	}

}
