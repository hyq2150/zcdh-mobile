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
import android.util.Log;
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
import com.zcdh.mobile.api.model.JobPrizesDTO;
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
@EActivity(R.layout.activity_winning_exp_add)
@OptionsMenu(R.menu.save)
public class WinningExpAddActivity extends BaseActivity implements EditableDialogListener, RequestListener {

	public static final int kREQUEST_WINNING_EXP = 2007;

	private static final int kEDIT_COMPANY_NAME = 10;

	private static final String kDATA_WORK_EXP_ADDED = "kDATA_WORK_EXP_ADD";

	private static final String kDATA_WORK_EXP_EDITED = "kDATA_WORK_EXP_EDITED";

	protected static final String TAG = WinningExpAddActivity.class.getSimpleName();
																	// 1标识修改

	private String kREQ_ID_addJobPrizes;

	private String kREQ_ID_updateJobPrizes;

	private IRpcJobUservice jobUservice;

	@ViewById(R.id.scrollView)
	ScrollView scrollView;

	/**
	 * 奖项描述
	 * 
	 */
	@ViewById(R.id.postDesc)
	EditText postDesc;

	/**
	 * 奖项表单
	 */
	@ViewById(R.id.winningExpAddListView)
	ListViewInScrollView winningExpFormListView;

	private WinningExpFromAdapter winningExpFromAdapter;

	/**
	 * 奖项
	 */
	@Extra
	JobPrizesDTO winningExperiencePostDTO;

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

		if (winningExperiencePostDTO != null && winningExperiencePostDTO.getPrizes_id() != null && winningExperiencePostDTO.getPrizes_id() > 0) {
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activiyt_winning_exp_edit));
			postDesc.setText(winningExperiencePostDTO.getPrizes_description());
		} else {
			SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activiyt_winning_exp_add));
			winningExperiencePostDTO = new JobPrizesDTO();
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
				winningExperiencePostDTO.setPrizes_description(s.toString());
			}
		});

		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

		titles = new String[] { getString(R.string.winningTime), getString(R.string.winningName)};

		winningExpFromAdapter = new WinningExpFromAdapter();

		winningExpFormListView.setAdapter(winningExpFromAdapter);

		scrollView.smoothScrollBy(0, 0);

	}

	/**
	 * 添加工作经验
	 */
	@Background
	void saveWorkExp() {
		if (winningExperiencePostDTO != null && winningExperiencePostDTO.getPrizes_id() != null && winningExperiencePostDTO.getPrizes_id() > 0) {// 如果工作经验ID 大于零，则为更新
			jobUservice.updateJobPrizes(winningExperiencePostDTO).identify(kREQ_ID_updateJobPrizes = RequestChannel.getChannelUniqueID(), this);
		} else {
			winningExperiencePostDTO.setUser_id(ZcdhApplication.getInstance().getZcdh_uid());
			jobUservice.addJobPrizes(winningExperiencePostDTO).identify(kREQ_ID_addJobPrizes = RequestChannel.getChannelUniqueID(), this);
		}
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		// 添加工作经验
		if (reqId.equals(kREQ_ID_addJobPrizes)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) { // 添加成功
					Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra(kDATA_WORK_EXP_ADDED, winningExperiencePostDTO);
//					data.putExtra(kFLAG_OPERATOR, 0);
					setResult(RESULT_OK, data);
					finish();
				} else {
					Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
				}
			}
		}

		// 更新
		if (reqId.equals(kREQ_ID_updateJobPrizes)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
					Intent data = new Intent();
					data.putExtra(kDATA_WORK_EXP_EDITED, winningExperiencePostDTO);
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
			errorMsg = "奖项描述超过500字";
		}else if(postDesc.getText().toString().length() < 1){
			valided = false;
			errorMsg = "请输入500字之内的奖项描述";
		}
		if (StringUtils.isBlank(winningExperiencePostDTO.getPrizes_name())) {
			valided = false;
			errorMsg = "请输入奖项名称";
		} else if (!StringUtils.isBlank(winningExperiencePostDTO.getPrizes_name()) && winningExperiencePostDTO.getPrizes_name().length() > 50) {
			valided = false;
			errorMsg = "奖项名称（限50个汉字）";
		}else if (winningExperiencePostDTO.getTime() == null) {
			valided = false;
			errorMsg = "请选择获奖时间";
		} else if (StringUtils.isBlank(winningExperiencePostDTO.getPrizes_description())) {
			valided = false;
			errorMsg = "请输入奖项描述";
		}

		if (valided) {
			saveWorkExp();
		} else {
			Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
		}
	}

	@ItemClick(R.id.winningExpAddListView)
	void onItemClick(int position) {
		switch (position) {
		case 1:
			Log.e(TAG, ""+winningExperiencePostDTO.getPrizes_id());
			if (winningExperiencePostDTO != null && winningExperiencePostDTO.getPrizes_id() != null && winningExperiencePostDTO.getPrizes_id() > 0) {
				new EditableDialog(this).initData(this, kEDIT_COMPANY_NAME, getString(R.string.winningName), winningExperiencePostDTO.getPrizes_name(), -1).show();
			} else {
				new EditableDialog(this).initData(this, kEDIT_COMPANY_NAME, getString(R.string.winningName),winningExperiencePostDTO.getPrizes_name() != null ? winningExperiencePostDTO.getPrizes_name():"").show();
			}
			break;
		case 0: // 开始时间
			setDateAction();
			break;
		}
	}

	@Override
	public void onEditableConfirm(int key, String content) {
		if (key == kEDIT_COMPANY_NAME && content != null) {
			if (content.length() > 50) {
				Toast.makeText(this, "请输入奖项名称（限50个汉字）", Toast.LENGTH_SHORT).show();
			}else {
				winningExperiencePostDTO.setPrizes_name(content.trim());
				winningExpFromAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 设置日期
	 * 
	 * @param start :0 开始 1 结束
	 * 
	 */
	void setDateAction() {

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
				if (tempDate.getTime() > new Date().getTime()) {
					Toast.makeText(getApplicationContext(), "获奖时间不能大于今天", Toast.LENGTH_SHORT).show();
				} else {
					winningExperiencePostDTO.setTime(tempDate);
					winningExpFromAdapter.notifyDataSetChanged();
				}
			}
		}, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

		// 设置点击其他不能取消
		datePickerDialog.setCancelable(true);

		datePickerDialog.show();
	}


	class WinningExpFromAdapter extends BaseAdapter {

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
			case 1:// 奖项名称
				valueName = winningExperiencePostDTO.getPrizes_name();
				break;
			case 0:// 获奖时间
				if (winningExperiencePostDTO.getTime() != null) valueName = sdf.format(winningExperiencePostDTO.getTime());
				break;
			}

			if (!StringUtils.isBlank(valueName)) {
				h.itemValue.setText(valueName);
			} else {
				h.itemValue.setText("");
				if (p == 1) {// 奖项名称
					h.itemValue.setHint("请输入奖项名称（限50个汉字）");
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
