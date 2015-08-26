/**
 * 
 * @author jeason, 2014-5-7 下午3:15:14
 */
package com.zcdh.mobile.app.activities.personal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.FinalDb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobUserInfoDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
import com.zcdh.mobile.app.views.EditableDialog;
import com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.biz.entities.ZcdhArea;
import com.zcdh.mobile.biz.entities.ZcdhParam;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.ObjectUtils;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-5-7 下午3:15:14
 * 个人信息修改
 */
@EActivity(R.layout.basic_info)
public class BasicInfoActivity extends BaseActivity implements RequestListener, EditableDialogListener,
OnRefreshListener<ScrollView>, DataLoadInterface {
	
	protected static final String TAG = BasicInfoActivity.class.getSimpleName();

	private IRpcJobUservice userService;

	private String kREQ_ID_FINDJOBUSERINFODTO;

	private String kREQ_ID_UPDATEJOBUSERINFODTO;

	private JobUserInfoDTO user_info_original;

	private JobUserInfoDTO user_info_after;

	private boolean changed = false;
	
	@ViewById(R.id.scrollView)
	PullToRefreshScrollView scrollView;
	
	@ViewById(R.id.emptyTipView)
	EmptyTipView emptyTipView;
	
	@ViewById(R.id.contentView)
	LinearLayout contentView;
	
	/**
	 * 邮箱
	 */
	@ViewById(R.id.tvMail_content)
	TextView tvMail_content;
	
	/**
	 * 手机号码
	 */
	@ViewById(R.id.phoneValueText)
	TextView phoneValueText;

	@ViewById(R.id.tvName_content)
	TextView tvName_content;

	@ViewById(R.id.tvGender_content)
	TextView tvGender_content;

	@ViewById(R.id.tvHowlong_content)
	TextView tvHowlong_content;

	@ViewById(R.id.tvBirthplace_content)
	TextView tvBirthplace_content;

	@ViewById(R.id.tvType_content)
	TextView tvType_content;

	@ViewById(R.id.tvWorkStatus_content)
	TextView tvWorkStatus_content;

	@ViewById(R.id.tvMarriage_content)
	TextView tvMarriage_content;

	@ViewById(R.id.tvID_content)
	TextView tvID_content;

	@ViewById(R.id.tvBirthday_content)
	TextView tvBirthday_content;

	@ViewById(R.id.tvAddress_content)
	TextView tvAddress_content;

	@ViewById(R.id.tvHighestEdu_content)
	TextView tvHighestEdu_content;

	@ViewById(R.id.tvIDType_content)
	TextView tvIDType_content;

	@ViewById(R.id.tvLanguage_content)
	TextView tvLanguage_content;

	private ArrayList<HashMap<String, String>> groupProvinces;
	private SparseArray<LinkedList<HashMap<String, String>>> subGroupsCities;
	private FinalDb dbTool;
	private final String PARENT_CODE_REGION = "-1";

	private List<ZcdhArea> provinces;

	private final String AREA_FIELD_TYPE_NATIVE = "native";

	private final String AREA_FIELD_TYPE_CURRENT = "current";

	private final int key_name = 0x01;
	private final int key_id = 0x02;
	private final int key_phone = 0x04;
	private final int key_mail = 0x03;

	private List<ZcdhParam> gender_params;
	private final String CATEGORY_CODE_GENDER = "003";

	private List<ZcdhParam> service_year_params;
	private final String CATEGORY_CODE_SERVICE_YEAR = "005";

	private List<ZcdhParam> rencai_type_params;
	private final String CATEGORY_CODE_RENCAI_TYPE = "021";

	private List<ZcdhParam> jobstatuses_params;
	private final String CATEGORY_CODE_JOBSTATUSES = "013";

	private List<ZcdhParam> marriages_params;
	private final String CATEGORY_CODE_MARRIAGES = "008";

	private List<ZcdhParam> idtype_params;
	private final String CATEGORY_CODE_IDTYPE = "009";

	private List<ZcdhParam> diploma_params;
	private final String CATEGORY_CODE_DIPLOMA = "004";

	private Calendar birthday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		dbTool = DbUtil.create(this);
	}
	

	@AfterViews
	void bindViews() {
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "基本信息");
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activity_title_basic_info));
		
		scrollView.setOnRefreshListener(this);
		
		loadData();
		initAreaTask.execute();
	}
	

	/**
	 * 
	 * @author jeason, 2014-5-8 上午9:26:00
	 */
	@Background
	public void loadData() {
		userService.findJobUserInfoDTO(ZcdhApplication.getInstance().getZcdh_uid()).identify(kREQ_ID_FINDJOBUSERINFODTO = RequestChannel.getChannelUniqueID(), this);
	}
	
	//手机号码
	@Click(R.id.rl_phone)
	void onPhoneNumClick(){
		new EditableDialog(this).initData(this, key_phone, "输入手机号码", InputType.TYPE_CLASS_PHONE,
				user_info_original.getMobile()==null?"":user_info_original.getMobile()).show();
	}
	
	@Click(R.id.rlMail)
	void onMailClick()
	{
		new EditableDialog(this).initData(this, key_mail, "输入邮箱地址", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
				user_info_original.getEmail()==null?"":user_info_original.getEmail()).show();
	}

	@Click(R.id.rlName)
	void onClickName() {
		new EditableDialog(this).initData(this, key_name, "输入姓名", 
				user_info_original.getUserName()==null?"":user_info_original.getUserName()).show();

	}

	String[] genders;

	@Click(R.id.rlGender)
	void onClickGender() {

		if (genders == null) {
			genders = new String[gender_params.size() - 1];
			for (int i = 0; i < gender_params.size(); i++) {
				String name = gender_params.get(i).getParam_name();
				if (!"不限".equals(name)) {
					genders[i] = name;
				}
			}
		}

		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.gender_select).setItems(genders, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tvGender_content.setText(genders[which]);
				user_info_after.setGerderCode(gender_params.get(which).getParam_code());
				notify_change();
			}
		}).create().show();
	}

	String[] service_years;

	@Click(R.id.rl_how_long)
	void onClickServiceYear() {

		if (service_years == null) {
			service_years = new String[service_year_params.size() - 1];
			for (int i = 0; i < service_year_params.size(); i++) {
				String name = service_year_params.get(i).getParam_name();
				if (!"不限".equals(name)) {
					service_years[i] = name;
				}
			}
		}

		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.service_year).setItems(service_years, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tvHowlong_content.setText(service_years[which]);
				user_info_after.setServiceYearCode(service_year_params.get(which).getParam_code());
				notify_change();
			}
		}).create().show();
	}

	@Click(R.id.rlBirthplace)
	void onClickBirthPlace() {
		onAreasClick(AREA_FIELD_TYPE_NATIVE);
	}

	String[] rencai_types;

	@Click(R.id.rlType)
	void onClickType() {

		if (rencai_types == null) {
			rencai_types = new String[rencai_type_params.size()];
			for (int i = 0; i < rencai_type_params.size(); i++) {
				String name =  rencai_type_params.get(i).getParam_name();
				if("未知类别".equals(name))name = "其他";
				rencai_types[i] = name;
			}
		}

		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.rencai_type).setItems(rencai_types, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				tvType_content.setText(rencai_types[which]);
				user_info_after.setTalentTypeCode(rencai_type_params.get(which).getParam_code());
				notify_change();
			}
		}).create().show();
	}

	String[] jobStatuses;

	@Click(R.id.rlWorkStatus)
	void onClickJobStatus() {

		if (jobStatuses == null) {
			jobStatuses = new String[jobstatuses_params.size()];
			for (int i = 0; i < jobstatuses_params.size(); i++) {
				jobStatuses[i] = jobstatuses_params.get(i).getParam_name();
			}
		}

		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.work_status).setItems(jobStatuses, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tvWorkStatus_content.setText(jobStatuses[which]);
				user_info_after.setJobStatusCode(jobstatuses_params.get(which).getParam_code());
				notify_change();
			}
		}).create().show();
	}

	String[] marriages;

	@Click(R.id.rlMarriage)
	void onClickMarriage() {

		if (marriages == null) {
			marriages = new String[marriages_params.size()];
			for (int i = 0; i < marriages_params.size(); i++) {
				marriages[i] = marriages_params.get(i).getParam_name();
			}
		}

		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.marriage).setItems(marriages, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tvMarriage_content.setText(marriages[which]);
				user_info_after.setIsMarriedCode(marriages_params.get(which).getParam_code());
				notify_change();
			}
		}).create().show();
	}

	String[] IDTypes;

	@Click(R.id.rlIDType)
	void onClickIDType() {

		if (IDTypes == null) {
			IDTypes = new String[idtype_params.size()];
			for (int i = 0; i < idtype_params.size(); i++) {
				IDTypes[i] = idtype_params.get(i).getParam_name();
			}
		}

		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.IDType).setItems(R.array.ID_type, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "WHICH: " + which);
				tvIDType_content.setText(getResources().getStringArray(R.array.ID_type)[which]);
				user_info_after.setCredentialTypeCode(getResources().getStringArray(R.array.ID_type_value)[which]);
				notify_change();
			}
		}).create().show();
	}

	@Click(R.id.rlID)
	void onClickID() {
		new EditableDialog(this).initData(this, key_id, "输入证件号码", InputType.TYPE_CLASS_TEXT, user_info_original.getCredentials()==null?
				"":user_info_original.getCredentials()).show();
	}

	@Click(R.id.rlBirthday)
	void onClickBirthday() {
		birthday = Calendar.getInstance();
		new DatePickerDialog(BasicInfoActivity.this, new DatePickerDialog.OnDateSetListener() {

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
					Toast.makeText(getApplicationContext(), "出生年月不能大于今天", Toast.LENGTH_SHORT).show();
				}else {
					birthday.set(year, monthOfYear, dayOfMonth);
					tvBirthday_content.setText(DateUtils.getDateByFormatYMD(birthday.getTime()));
					user_info_after.setBirth(birthday.getTime());
					notify_change();
				}
			}
		}, birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH), birthday.get(Calendar.DAY_OF_MONTH)).show();
	}

	@Click(R.id.rlAddress)
	void onClickrlAddress() {
		onAreasClick(AREA_FIELD_TYPE_CURRENT);
	}

	String[] diplomas;

	@Click(R.id.rlHighestEdu)
	void onClickHighestEdu() {

		if (diplomas == null) {
			diplomas = new String[diploma_params.size() - 1];
			for (int i = 0; i < diploma_params.size(); i++) {
				String paramName = diploma_params.get(i).getParam_name();
				if (!"不限".equals(paramName)) {
					diplomas[i] = paramName;
				}
			}
		}

		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.diploma).setItems(diplomas, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tvHighestEdu_content.setText(diplomas[which]);
				user_info_after.setDegreeCode(diploma_params.get(which).getParam_code());
				notify_change();
			}
		}).create().show();
	}

	@Click(R.id.rlLanguage)
	void onClickLanguage() {
		ActivityDispatcher.toLanguagesEdit(this, LanguageEditActivity.REQUEST_CODE_LANGUAGE);
	}

	@Override
	public void onRequestStart(String reqId) {
		if (reqId.equals(kREQ_ID_FINDJOBUSERINFODTO)) {

		}
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_FINDJOBUSERINFODTO)) {
			user_info_original = (JobUserInfoDTO) result;
			user_info_after = new JobUserInfoDTO();
			ObjectUtils.updateObject(user_info_original, user_info_after);
			initView();
			emptyTipView.isEmpty(false);
			contentView.setVisibility(View.VISIBLE);
		}

		if (reqId.equals(kREQ_ID_UPDATEJOBUSERINFODTO)) {
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
			ObjectUtils.updateObject(user_info_after, user_info_original);
			ResumeActivity.FLAG_REFRESH = 1;
//			notify_change();
			changed = false;
			supportInvalidateOptionsMenu();
			// 重新检查是否完善资料
			LoginHelper.getInstance(this).doCheck();
//			LoginHelper.getInstance(this).doCheckUserRequisite();
//			LoginHelper.getInstance(this).doCheckUserResumeMiddleDTO();
			
			//广播修改了基本资料信息， 侧边栏显示的用户信息重新加载
			Intent actionUpdateProfile = new Intent(Constants.ACTION_UPDATE_PROFILE);
			sendBroadcast(actionUpdateProfile);
		}
	}

	/**
	 * 
	 * @author jeason, 2014-5-8 上午11:32:45
	 * @param user_info2
	 */
	private void initView() {
		this.tvAddress_content.setText(user_info_after.getAddressName());
		if (user_info_after.getBirth() != null) this.tvBirthday_content.setText(DateUtils.getDateByFormatYMD(user_info_after.getBirth()));
		this.tvBirthplace_content.setText(user_info_after.getPanmeldenName());
		this.tvGender_content.setText(user_info_after.getGerder());
		this.tvHighestEdu_content.setText(user_info_after.getDegreeName());
		this.tvHowlong_content.setText(user_info_after.getServiceYearName());
		this.tvID_content.setText(user_info_after.getCredentials());
		this.tvMarriage_content.setText(user_info_after.getIsMarried());
		this.tvName_content.setText(user_info_after.getUserName());
		this.tvIDType_content.setText(user_info_after.getCredentialTypeName());
		this.tvType_content.setText(user_info_after.getTalentTypeName());
		this.tvWorkStatus_content.setText(user_info_after.getJobStautsName());
		this.tvMail_content.setText(user_info_after.getEmail());
		if (user_info_after!=null 
				&& user_info_after.getLaguageTypeCount()!=null 
				&& user_info_after.getLaguageTypeCount() > 0) {
			tvLanguage_content.setText(String.valueOf(user_info_after.getLaguageTypeCount()) + "种");
		}

		if(!StringUtils.isBlank(user_info_after.getMobile())){
			phoneValueText.setText(user_info_after.getMobile());
		}
	}

	@Override
	public void onRequestError(String reqId, Exception error) {
		emptyTipView.showException((ZcdhException)error, this);
	}

	@Override
	public void onRequestFinished(String reqId) {
		scrollView.onRefreshComplete();
	}

	private void notify_change() {
		changed = false;
		if (!ObjectUtils.compareJobUserInfo(user_info_after, user_info_original)) {
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

	@OptionsItem(R.id.action_save)
	void onUpdate() {
		if (check_content()) {
			doUpdate();
		}
	}

	@Background
	void doUpdate() {
		userService.updateJobUserInfoDTO(user_info_after).identify(kREQ_ID_UPDATEJOBUSERINFODTO = RequestChannel.getChannelUniqueID(), this);

	}

	/**
	 * @return
	 * @author jeason, 2014-6-3 下午9:51:06
	 */
	private boolean check_content() {
		if (StringUtils.isBlank(user_info_after.getUserName())) {
			Toast.makeText(this, "请填写姓名", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (StringUtils.isBlank(user_info_after.getTalentTypeCode())) {
			Toast.makeText(this, "请选择人才类型", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (StringUtils.isBlank(user_info_after.getPanmeldenCode())) {
			Toast.makeText(this, "请选择户口所在地", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (StringUtils.isBlank(user_info_after.getServiceYearCode())) {
			Toast.makeText(this, "请选择工作年限", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (StringUtils.isBlank(user_info_after.getGerderCode())) {
			Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
			return false;
		}

		
		return true;
	}

	/**
	 * 
	 * @param idcard
	 * @param type enum 身份证：1， 护照：2，军人证：3；
	 * @return
	 * @author jeason, 2014-7-15 上午10:04:54
	 */
	private boolean validateIDCard(String cardNo, int type) {
		try {
			switch (type) {
			case 1:
				Log.i(TAG, "验证：身份证");
				return Pattern.compile(Constants.regex_idcard).matcher(cardNo).matches();
			case 2:
				Log.i(TAG, "验证：护照");
				return Pattern.compile(Constants.regex_passport).matcher(cardNo).matches();
			case 3:
				Log.i(TAG, "验证：.....");
				return Pattern.compile(Constants.regex_milID).matcher(cardNo).matches();
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private AsyncTask<Void, Void, Void> initAreaTask = new AsyncTask<Void, Void, Void>() {

		@Override
		protected Void doInBackground(Void... params) {
			initAreas();
			initOtherParams();
			return null;
		}

	};

	void initAreas() {
		groupProvinces = new ArrayList<HashMap<String, String>>();
		subGroupsCities = new SparseArray<LinkedList<HashMap<String, String>>>();

		provinces = dbTool.findAllByWhere(ZcdhArea.class, String.format("parent_code = '%s'", PARENT_CODE_REGION));

		int i = 0;
		for (ZcdhArea province : provinces) {
			HashMap<String, String> province_map = new HashMap<String, String>();
			province_map.put("value", province.getCode());
			province_map.put("name", province.getName());

			groupProvinces.add(province_map);
			LinkedList<HashMap<String, String>> cities_under = new LinkedList<HashMap<String, String>>();

			List<ZcdhArea> cities = dbTool.findAllByWhere(ZcdhArea.class, String.format("parent_code = '%s'", province.getCode()));

			for (ZcdhArea city : cities) {
				HashMap<String, String> city_info = new HashMap<String, String>();
				city_info.put("name", city.getName());
				city_info.put("value", city.getCode());
				cities_under.add(city_info);
			}
			subGroupsCities.put(i, cities_under);
			i++;
		}
	}

	/**
	 * 
	 * @author jeason, 2014-5-17 下午5:20:22
	 */
	protected void initOtherParams() {
		this.gender_params = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_GENDER));
		this.idtype_params = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_IDTYPE));
		this.diploma_params = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_DIPLOMA));
		this.marriages_params = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_MARRIAGES));
		this.jobstatuses_params = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_JOBSTATUSES));
		this.rencai_type_params = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_RENCAI_TYPE));
		this.service_year_params = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_SERVICE_YEAR));
	}

	void onAreasClick(final String area_field_type) {
		while (groupProvinces == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String[] province_strs = new String[groupProvinces.size()];
		int i = 0;
		for (HashMap<String, String> province_str : groupProvinces) {
			String name = province_str.get("name");
			if (!"全国".equals(name)) {
				province_strs[i] = name;
				i++;
			}
		}
		new AlertDialog.Builder(this).setCancelable(true).setTitle(R.string.area_select).setItems(province_strs, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, final int which_level1) {
				int cities_size = subGroupsCities.get(which_level1).size();
				String[] cities_strs = new String[cities_size];
				if (cities_size == 0) {
					if (area_field_type.equals(AREA_FIELD_TYPE_NATIVE)) {
						tvBirthplace_content.setText(groupProvinces.get(which_level1).get("name"));
						user_info_after.setPanmeldenCode(groupProvinces.get(which_level1).get("value"));
					} else {
						tvAddress_content.setText(groupProvinces.get(which_level1).get("name"));
						user_info_after.setAddressCode(groupProvinces.get(which_level1).get("value"));
					}
					notify_change();

					return;
				}

				int j = 0;
				for (HashMap<String, String> city_map : subGroupsCities.get(which_level1)) {
					cities_strs[j] = city_map.get("name");
					j++;
				}
				new AlertDialog.Builder(BasicInfoActivity.this).setCancelable(true).setTitle(R.string.area_select).setItems(cities_strs, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (area_field_type.equals(AREA_FIELD_TYPE_NATIVE)) {

							tvBirthplace_content.setText(subGroupsCities.get(which_level1).get(which).get("name"));
							user_info_after.setPanmeldenCode(subGroupsCities.get(which_level1).get(which).get("value"));
						} else {
							tvAddress_content.setText(subGroupsCities.get(which_level1).get(which).get("name"));
							user_info_after.setAddressCode(subGroupsCities.get(which_level1).get(which).get("value"));

						}
						notify_change();

					}
				}).create().show();
				cities_strs = null;
			}
		}).create().show();
		province_strs = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zcdh.mobile.app.views.EditableDialog.EditableDialogListener#
	 * onEditableConfirm(java.lang.String)
	 */
	@Override
	public void onEditableConfirm(int key, String content) {
		switch (key) {
		case key_id:
			if (!StringUtils.isBlank(content)) {
				if (StringUtils.isBlank(content)) {
					Toast.makeText(this, "请填写证件号码", Toast.LENGTH_SHORT).show();
				} else {
					boolean validateResult = false;
					// 获取009.001 身份证 009.002 军人证 009.003 护照 009.004 其他
					if ("009.001".equals(user_info_after.getCredentialTypeCode())) {
						validateResult = validateIDCard(content, 1);
					}
					if ("009.002".equals(user_info_after.getCredentialTypeCode())) {
						validateResult = validateIDCard(content, 2);
					}
					if ("009.003".equals(user_info_after.getCredentialTypeCode())) {
						validateResult = validateIDCard(content, 3);
					}
					if ("009.004".equals(user_info_after.getCredentialTypeCode())) {
						validateResult = validateIDCard(content, 4);
					}

					if (!validateResult) {
						Toast.makeText(this, "请输入正确地证件号码", Toast.LENGTH_SHORT).show();
					}else{
						tvID_content.setText(content);
						user_info_after.setCredentials(content);
						notify_change();
					}
				}
			}
			
			break;
		case key_name:
			tvName_content.setText(content);
			user_info_after.setUserName(content);
			notify_change();
			break;
		case key_mail:
			if (!StringUtils.isBlank(content)) {
				Pattern regex = Pattern.compile(Constants.regex_email);
				Matcher matcher = regex.matcher(content);
				if (!matcher.matches()) {
					Toast.makeText(this, getResources().getString(R.string.invalid_email_format), Toast.LENGTH_SHORT).show();
					return;
				}else {
					tvMail_content.setText(content);
					user_info_after.setEmail(content);
					notify_change();
				}
			}
			break;
		case key_phone:
			if(!StringUtils.isBlank(content)){
				Pattern regex = Pattern.compile(SharedPreferencesUtil.getValue(this, Constants.REGEX_PHONE_KEY, Constants.regex_phone));
				Matcher matcher = regex.matcher(content);
				if (!matcher.matches()) {
					Toast.makeText(this, getResources().getString(R.string.invalid_phone_format), Toast.LENGTH_SHORT).show();
					return;
				}else{
					phoneValueText.setText(content);
					user_info_after.setMobile(content);
					notify_change();
				}
			}
			break;
		}
	}

	@OnActivityResult(LanguageEditActivity.REQUEST_CODE_LANGUAGE)
	void onLanguageBack(int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) tvLanguage_content.setText(String.valueOf(data.getIntExtra("language_count", 0)) + "种");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (changed) {
				showTip();
				return false;
			} else {
				finish();
				return true;
			}
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if (changed) {
				showTip();
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	void showTip() {
		new AlertDialog.Builder(this).setCancelable(true).setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setTitle(R.string.friendly_hint).setNegativeButton(R.string.cancel, null).setMessage(R.string.edit_mode_exit).create().show();

	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		
		loadData();
	}

	
}
