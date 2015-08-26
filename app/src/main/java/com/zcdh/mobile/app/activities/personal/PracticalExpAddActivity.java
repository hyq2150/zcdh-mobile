package com.zcdh.mobile.app.activities.personal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
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

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobPracticeDTO;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.EditableDialog;
import com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 新增工作经验
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_practical_exp_add)
@OptionsMenu(R.menu.save)
public class PracticalExpAddActivity extends BaseActivity implements EditableDialogListener, RequestListener {

	public static final int kREQUEST_PRACTICAL_EXP = 2007;

	private static final int kEDIT_PRACTICAL_NAME = 10;

	private static final String kDATA_WORK_EXP_ADDED = "kDATA_WORK_EXP_ADD";

	private static final String kDATA_WORK_EXP_EDITED = "kDATA_WORK_EXP_EDITED";

	private static final String kFLAG_OPERATOR = "kFLAG_OPERATOR"; // 0 标识增加

	protected static final String TAG = PracticalExpAddActivity.class.getSimpleName();
																	// 1标识修改

	private String kREQ_ID_addJobPractice;

	private String kREQ_ID_updateJobPractice;

	private IRpcJobUservice jobUservice;

	@ViewById(R.id.scrollView)
	ScrollView scrollView;

	/**
	 * 实践描述
	 * 
	 */
	@ViewById(R.id.postDesc)
	EditText postDesc;

	/**
	 * 实践经历表单
	 */
	@ViewById(R.id.practicalExpAddListView)
	ListViewInScrollView practicalExpFormListView;

	private PracticalExpFromAdapter practicalExpFromAdapter;

	/**
	 * 实践经历
	 */
	@Extra
	JobPracticeDTO practicalExperiencePostDTO;

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

		if (practicalExperiencePostDTO != null && practicalExperiencePostDTO.getPractice_id() != null && practicalExperiencePostDTO.getPractice_id() > 0) {
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activiyt_practical_exp_edit));
			postDesc.setText(practicalExperiencePostDTO.getPractice_description());
		} else {
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activiyt_practical_exp_add));
			practicalExperiencePostDTO = new JobPracticeDTO();
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
				practicalExperiencePostDTO.setPractice_description(s.toString());
			}
		});

		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

		titles = new String[] {getString(R.string.startTime), getString(R.string.endTime), getString(R.string.practicalName)};

		practicalExpFromAdapter = new PracticalExpFromAdapter();

		practicalExpFormListView.setAdapter(practicalExpFromAdapter);

		scrollView.smoothScrollBy(0, 0);

//		loadPracticalExpById();
	}

//	void loadPracticalExpById() {
//		jobUservice.findJobWorkExperiencePostDTO(practicalExperiencePostDTO.getPractice_id()).identify(kREQ_ID_findJobWorkExperiencePostDTO = RequestChannel.getChannelUniqueID(), this);
//	}

	/**
	 * 添加实践经历
	 */
	@Background
	void savePracticalExp() {

		if (practicalExperiencePostDTO != null && practicalExperiencePostDTO.getPractice_id() != null && practicalExperiencePostDTO.getPractice_id() > 0) {// 如果工作经验ID 大于零，则为更新
			jobUservice.updateJobPractice(practicalExperiencePostDTO).identify(kREQ_ID_updateJobPractice = RequestChannel.getChannelUniqueID(), this);
		} else {
			practicalExperiencePostDTO.setUser_id(ZcdhApplication.getInstance().getZcdh_uid());
			jobUservice.addJobPractice(practicalExperiencePostDTO).identify(kREQ_ID_addJobPractice = RequestChannel.getChannelUniqueID(), this);
		}
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		// 添加实践经历
		if (reqId.equals(kREQ_ID_addJobPractice)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) { // 添加成功
					Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra(kDATA_WORK_EXP_ADDED, practicalExperiencePostDTO);
					data.putExtra(kFLAG_OPERATOR, 0);
					setResult(RESULT_OK, data);
					finish();
				} else {
					Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
				}
			}
		}

		// 更新实践经历
		if (reqId.equals(kREQ_ID_updateJobPractice)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra(kDATA_WORK_EXP_EDITED, practicalExperiencePostDTO);
					data.putExtra(kFLAG_OPERATOR, 1);
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
		if (postDesc.getText().toString().length() > 500) {
			valided = false;
			errorMsg = "实践描述超过500字";
		}else if(postDesc.getText().toString().length() < 1){
			valided = false;
			errorMsg = "请输入500字之内的实践描述";
		}
		if (StringUtils.isBlank(practicalExperiencePostDTO.getPractice_name())) {
			valided = false;
			errorMsg = "请输入实践名称";
		} else if (!StringUtils.isBlank(practicalExperiencePostDTO.getPractice_name()) && practicalExperiencePostDTO.getPractice_name().length() > 50) {
			valided = false;
			errorMsg = "实践名称（限50个汉字）";
		}else if (practicalExperiencePostDTO.getStart_time() == null) {
			valided = false;
			errorMsg = "请选择开始时间";
		} else if (practicalExperiencePostDTO.getEnd_time() == null) {
			valided = false;
			errorMsg = "请选择结束时间";
		} else if (StringUtils.isBlank(practicalExperiencePostDTO.getPractice_description())) {
			valided = false;
			errorMsg = "请输入实践描述";
		}

		if (valided) {
			savePracticalExp();
		} else {
			Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
		}
	}

	@ItemClick(R.id.practicalExpAddListView)
	void onItemClick(int position) {
		switch (position) {
		case 2:
			if (practicalExperiencePostDTO != null && practicalExperiencePostDTO.getPractice_id() != null && practicalExperiencePostDTO.getPractice_id() > 0) {
				new EditableDialog(this).initData(this, kEDIT_PRACTICAL_NAME, getString(R.string.practicalName), practicalExperiencePostDTO.getPractice_name(), -1).show();
			} else {
				new EditableDialog(this).initData(this, kEDIT_PRACTICAL_NAME, getString(R.string.practicalName),practicalExperiencePostDTO.getPractice_name() != null ? practicalExperiencePostDTO.getPractice_name():"").show();
			}
			break;
		case 0: // 开始时间
			setDateAction(0);
			break;
		case 1:// 结束时间
			setDateAction(1);
			break;
		}
	}

	@Override
	public void onEditableConfirm(int key, String content) {
		if (key == kEDIT_PRACTICAL_NAME && content != null) {
			if (content.length() > 50) {
				Toast.makeText(this, "请输入实践名称（限50个汉字）", Toast.LENGTH_SHORT).show();
			}else {
				practicalExperiencePostDTO.setPractice_name(content.trim());
				practicalExpFromAdapter.notifyDataSetChanged();
			}
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
					tempDate = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA).parse(dateStr);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				if (start == 0) {// 开始时间
					if (practicalExperiencePostDTO.getEnd_time() != null && practicalExperiencePostDTO.getEnd_time().getTime() <= tempDate.getTime()) {
						Toast.makeText(getApplication(), "开始时间应小于结束时间", Toast.LENGTH_SHORT).show();
					} else {
						practicalExperiencePostDTO.setStart_time(tempDate);
					}
				}

				if (start == 1) {// 结束时间
						
					if (practicalExperiencePostDTO.getStart_time() != null && practicalExperiencePostDTO.getStart_time().getTime() >= tempDate.getTime()) {

						Toast.makeText(getApplication(), "结束时间应大于开始时间", Toast.LENGTH_SHORT).show();
					} else {
						if (tempDate.getTime() > new Date().getTime()) {
							Toast.makeText(getApplicationContext(), "结束时间不能大于今天", Toast.LENGTH_SHORT).show();
						} else {
							practicalExperiencePostDTO.setEnd_time(tempDate);
						}
					}
				}
				practicalExpFromAdapter.notifyDataSetChanged();
				// }
				// onSetDateOK = false;
			}
		}, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

		// 设置点击其他不能取消
		datePickerDialog.setCancelable(true);

		datePickerDialog.show();
	}

	class PracticalExpFromAdapter extends BaseAdapter {

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
			case 2:// 实践名称
				valueName = practicalExperiencePostDTO.getPractice_name();
				break;
			case 0:// 开始时间
				if (practicalExperiencePostDTO.getStart_time() != null) valueName = sdf.format(practicalExperiencePostDTO.getStart_time());
				break;
			case 1:// 结束时间
				if (practicalExperiencePostDTO.getEnd_time() != null) valueName = sdf.format(practicalExperiencePostDTO.getEnd_time());
				break;
			}

			if (!StringUtils.isBlank(valueName)) {
				h.itemValue.setText(valueName);
			} else {
				h.itemValue.setText("");
				if (p == 2) {// 实践名称
					h.itemValue.setHint("请输入实践名称（限50个汉字）");
				}else {
					h.itemValue.setHint(getString(R.string.chosen));
				}
			}

			return contentView;
		}

		class Holder {
			TextView itemName = null;
			TextView itemValue = null;
		}
	}
}
