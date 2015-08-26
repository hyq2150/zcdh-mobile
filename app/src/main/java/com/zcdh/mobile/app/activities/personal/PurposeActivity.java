package com.zcdh.mobile.app.activities.personal;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobObjectiveAreaDTO;
import com.zcdh.mobile.api.model.JobObjectiveDTO;
import com.zcdh.mobile.api.model.JobObjectiveIndustryDTO;
import com.zcdh.mobile.api.model.JobObjectivePostDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.search.AreaActivity;
import com.zcdh.mobile.app.activities.search.AreaActivity_;
import com.zcdh.mobile.app.activities.search.CategoryPostActivity_;
import com.zcdh.mobile.app.activities.search.IndustryActivity;
import com.zcdh.mobile.app.activities.search.IndustryActivity_;
import com.zcdh.mobile.app.activities.search.ParamsActivity;
import com.zcdh.mobile.app.activities.search.ParamsActivity_;
import com.zcdh.mobile.app.activities.search.PostsActivity;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 求职意向
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_purpose)
public class PurposeActivity extends BaseActivity implements
		SalaryDialogWheelListner, RequestListener, OnClickListener,
		DataLoadInterface {

	private static final int MAX = 3;

	private IRpcJobUservice jobUservice;

	private String kREQ_ID_findUserObjectiveDTO;
	private String kREQ_ID_updateObjectiveDTO;

	/**
	 * 求职意向
	 */
	private JobObjectiveDTO jobObjectiveDTO = new JobObjectiveDTO();

	/**
	 * 意向行业
	 */
	private List<JobObjectiveIndustryDTO> industryList = new ArrayList<JobObjectiveIndustryDTO>();

	/**
	 * 意向职位
	 */
	private List<JobObjectivePostDTO> postList = new ArrayList<JobObjectivePostDTO>();

	/**
	 * 意向地区
	 */
	private List<JobObjectiveAreaDTO> areaList = new ArrayList<JobObjectiveAreaDTO>();

	/**
	 * 其他意向列表Title (工作类型， 期望月薪)
	 */
	private ArrayList<String> otherPurposeTitles = new ArrayList<String>();

	@ViewById(R.id.scrollView)
	PullToRefreshScrollView scrollView;

	@ViewById(R.id.emptyTipView)
	EmptyTipView emptyTipView;

	@ViewById(R.id.contentView)
	LinearLayout contentView;

	/**
	 * 意向行业列表
	 */
	@ViewById(R.id.industryListView)
	ListViewInScrollView industryListViiew;

	/**
	 * 意向职位列表
	 */
	@ViewById(R.id.postListView)
	ListViewInScrollView postListView;

	/**
	 * 意向地区列表
	 */
	@ViewById(R.id.areaListView)
	ListViewInScrollView areaListView;

	/**
	 * 其他意向（月薪，工作类型 。。.）
	 */
	@ViewById(R.id.metaDataListView)
	ListViewInScrollView metaDataListView;

	/**
	 * 意向行业
	 */
	private PurposeAdapter industryAdapter;

	/**
	 * 意向职位
	 */
	private PurposeAdapter postAdapter;

	/**
	 * 意向地区
	 */
	private PurposeAdapter areaAdapter;

	/**
	 * 其他意向
	 */
	private PurposeAdapter metaDataAdapter;

	/**
	 * 选择薪资范围
	 */
	private SalaryWheelDialog salaryWheelDialog;

	/**
	 * 是否编辑
	 */
	private boolean edit = false;

	private Menu menu;

	private ProcessDialog processDialog;

	private boolean is_edited;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (is_edited) {
			if (edit) {
				getMenuInflater().inflate(R.menu.action_ok, menu);
			} else {
				getMenuInflater().inflate(R.menu.action_edit, menu);
			}
		}
		this.menu = menu;

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (edit) {
				confirmSave();
			} else {
				finish();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (edit) {
			confirmSave();
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * 修改数据，提示是否保存
	 */
	private void confirmSave() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				PurposeActivity.this);
		builder.setTitle("是否保存数据?");
		builder.setCancelable(true);
		builder.setNegativeButton("不保存", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}

		});
		builder.setNeutralButton("保存", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				updateJobObjective();
			}

		});
		builder.create().show();
	}

	@AfterViews
	void bindViews() {

		jobUservice = RemoteServiceManager
				.getRemoteService(IRpcJobUservice.class);

		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), getString(R.string.title_purpose));

		salaryWheelDialog = new SalaryWheelDialog(this, this);

		otherPurposeTitles.add("工作类型");
		otherPurposeTitles.add("期望月薪");

		industryAdapter = new PurposeAdapter(PurposeAdapter.TYPE_INDUSTRY);
		postAdapter = new PurposeAdapter(PurposeAdapter.TYPE_POST);
		areaAdapter = new PurposeAdapter(PurposeAdapter.TYPE_AREA);
		metaDataAdapter = new PurposeAdapter(PurposeAdapter.TYPE_METADATA);

		industryListViiew.setAdapter(industryAdapter);
		postListView.setAdapter(postAdapter);
		areaListView.setAdapter(areaAdapter);
		metaDataListView.setAdapter(metaDataAdapter);

		processDialog = new ProcessDialog(this);

		// 加载意向职位
		loadData();

	}

	@Background
	public void loadData() {
		try {
			jobUservice
					.findUserObjectiveDTO(
							ZcdhApplication.getInstance().getZcdh_uid())
					.identify(
							kREQ_ID_findUserObjectiveDTO = RequestChannel
									.getChannelUniqueID(),
							this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Background
	void updateJobObjective() {
		try {
			if (jobObjectiveDTO == null) {
				jobObjectiveDTO = new JobObjectiveDTO();
			}
			jobObjectiveDTO.setIndustries(industryList);
			jobObjectiveDTO.setPosts(postList);
			jobObjectiveDTO.setAreas(areaList);
			jobUservice
					.updateObjectiveDTO(jobObjectiveDTO)
					.identify(
							kREQ_ID_updateObjectiveDTO = RequestChannel
									.getChannelUniqueID(),
							this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findUserObjectiveDTO)) {
			if (result != null) {
				jobObjectiveDTO = (JobObjectiveDTO) result;
				if (jobObjectiveDTO.getIndustries() != null)
					industryList = jobObjectiveDTO.getIndustries();
				if (jobObjectiveDTO.getPosts() != null)
					postList = jobObjectiveDTO.getPosts();
				if (jobObjectiveDTO.getAreas() != null)
					areaList = jobObjectiveDTO.getAreas();
				updateListView();
				emptyTipView.isEmpty(false);
				contentView.setVisibility(View.VISIBLE);
			} else {
				this.menu.findItem(R.id.action_edit).setVisible(false);
			}
		}

		if (reqId.equals(kREQ_ID_updateObjectiveDTO)) {
			if (result != null) {
				int updated = (Integer) result;
				if (updated == 0) {
					updateListView();
					ResumeActivity.FLAG_REFRESH = 1;
					this.edit = false;
					setMenuState();
				}
			}
		}
	}

	void updateListView() {
		is_edited = false;
		if (industryList.size() > 0
				|| postList.size() > 0
				|| areaList.size() > 0
				|| (jobObjectiveDTO.getMaxSalary() > 0 && jobObjectiveDTO
						.getMinSalary() >= 0)
				|| !TextUtils.isEmpty(jobObjectiveDTO.getWorkPropertyCode())) {
			is_edited = true;
		}
		supportInvalidateOptionsMenu();

		industryAdapter.notifyDataSetChanged();
		postAdapter.notifyDataSetChanged();
		areaAdapter.notifyDataSetChanged();
		metaDataAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRequestFinished(String reqId) {
		if (reqId.equals(kREQ_ID_updateObjectiveDTO)) {
			processDialog.dismiss();
		}
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyTipView.showException((ZcdhException) error, this);
	}

	/**
	 * 编辑
	 * 
	 * @param item
	 * 
	 */
	@OptionsItem(R.id.action_edit)
	void onMenuEdit(MenuItem item) {
		edit = true;

		setMenuState();
	}

	/**
	 * 保存
	 * 
	 * @param item
	 */
	@OptionsItem(R.id.action_OK)
	void onMenuOK(MenuItem item) {
		processDialog.show();
		updateJobObjective();
	}

	/**
	 * 改变菜单按钮
	 * 
	 */
	void setMenuState() {
		supportInvalidateOptionsMenu();
		industryAdapter.notifyDataSetChanged();
		postAdapter.notifyDataSetChanged();
		areaAdapter.notifyDataSetChanged();

	}

	/**
	 * 获取选择的意向行业
	 * 
	 * @param resultCode
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	@OnActivityResult(IndustryActivity.kREQUEST_INDUSTRY)
	void onResultIndustry(int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {

			HashMap<String, JobObjectiveIndustryDTO> industries = (HashMap<String, JobObjectiveIndustryDTO>) data
					.getExtras().getSerializable(
							IndustryActivity.kDATA_INDUSTRY_BUNDLE);
			if (industries != null) {
				industryList.clear();
				for (String key : industries.keySet()) {
					industryList.add(industries.get(key));
				}
				this.edit = true;
				setMenuState();
			}
		}

	}

	/**
	 * 获取选择的意向职位
	 * 
	 * @param resultCode
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	@OnActivityResult(PostsActivity.kREQUEST_POST)
	void onResultPost(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			HashMap<String, JobObjectivePostDTO> posts = (HashMap<String, JobObjectivePostDTO>) data
					.getExtras().getSerializable(
							PostsActivity.kDATA_POST_BUNLDE);
			if (posts != null) {
				postList.clear();
				for (String key : posts.keySet()) {
					postList.add(posts.get(key));
				}
				this.edit = true;
				setMenuState();
			}
		}
	}

	/**
	 * 获取选择的意向地区
	 * 
	 * @param resultCode
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	@OnActivityResult(AreaActivity.kREQUEST_AREA)
	void onResultArea(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			HashMap<String, JobObjectiveAreaDTO> areas = (HashMap<String, JobObjectiveAreaDTO>) data
					.getExtras()
					.getSerializable(AreaActivity.kDATA_AREA_BUNLDE);

			areaList.clear();
			for (String key : areas.keySet()) {
				areaList.add(areas.get(key));
			}
			this.edit = true;
			setMenuState();
		}
	}

	/**
	 * 获取选择的意向工作类型
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(ParamsActivity.kREQUEST_PARAM)
	void onResultPostWork(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String code = data.getExtras().getString(ParamsActivity.kDATA_CODE);
			String name = data.getExtras().getString(ParamsActivity.kDATA_NAME);

			jobObjectiveDTO.setWorkPropertyCode(code);
			jobObjectiveDTO.setWorkPropertyName(name);

			metaDataAdapter.notifyDataSetChanged();

			this.edit = true;
			setMenuState();
		}
	}

	@Override
	public void onSalary(boolean mianyi, int maxSalary, int minSalary) {
		if (jobObjectiveDTO == null) {
			jobObjectiveDTO = new JobObjectiveDTO();
		}
		jobObjectiveDTO.setNegotiableSalary(mianyi + "");
		if (!mianyi) {
			jobObjectiveDTO.setMaxSalary(maxSalary);
			jobObjectiveDTO.setMinSalary(minSalary);
		} else {
			jobObjectiveDTO.setNegotiableSalary("true");
		}
		this.edit = true;
		metaDataAdapter.notifyDataSetChanged();
		setMenuState();
	}

	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
	}

	@ItemClick(R.id.industryListView)
	void onItemIndustry(int position) {
		if (position == 0) {
			HashMap<String, JobObjectiveIndustryDTO> selected = new HashMap<String, JobObjectiveIndustryDTO>();

			for (int i = 0; i < industryList.size(); i++) {
				JobObjectiveIndustryDTO ind = industryList.get(i);
				selected.put(ind.getCode(), ind);
			}
			IndustryActivity_.intent(this).signle(false)
					.selectedIndustries(selected)
					.startForResult(IndustryActivity.kREQUEST_INDUSTRY);
		}
	}

	@ItemClick(R.id.postListView)
	void onItemPosts(int position) {
		if (position == 0) {
			HashMap<String, JobObjectivePostDTO> selected = new HashMap<String, JobObjectivePostDTO>();

			for (int i = 0; i < postList.size(); i++) {
				JobObjectivePostDTO post = postList.get(i);
				selected.put(post.getCode(), post);
			}
			CategoryPostActivity_.intent(this).single(false)
					.selectedPosts(selected)
					.startForResult(PostsActivity.kREQUEST_POST);
		}
	}

	/**
	 * 选择意向地区
	 * 
	 * @param position
	 */
	@ItemClick(R.id.areaListView)
	void onItemArea(int position) {
		if (position == 0) {
			HashMap<String, JobObjectiveAreaDTO> selected = new HashMap<String, JobObjectiveAreaDTO>();

			for (int i = 0; i < areaList.size(); i++) {
				JobObjectiveAreaDTO area = areaList.get(i);
				selected.put(area.getCode(), area);
			}
			AreaActivity_.intent(this).signle(false).selectedAreas(selected)
					.startForResult(AreaActivity.kREQUEST_AREA);
		}
	}

	/**
	 * 其他意向
	 * 
	 * @param position
	 */
	@ItemClick(R.id.metaDataListView)
	void onItemParam(int position) {
		String paramCodeCategory = null;
		// 1 )工作性质
		if (position == 0) {

			String paramCode = jobObjectiveDTO.getWorkPropertyCode();
			if (paramCode == null)
				paramCode = "";
			paramCodeCategory = ParamsActivity.kCODE_PARAM_JOB_NATUAL;
			ParamsActivity_.intent(this).paramCategoryCode(paramCodeCategory)
					.selectedParamCode(paramCode)
					.startForResult(ParamsActivity.kREQUEST_PARAM);
		}

		// 2 ) 薪资范围
		if (position == 1) {
			// paramCodeCategory = ParamsActivity.kCODE_PARAM_PAYMENT_SCOPE;
			salaryWheelDialog.show();
		}

	}

	/**
	 * 删除意向职位
	 */
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.delImgBtn) {
			if (v.getTag() != null) {
				String tag = (String) v.getTag();
				String[] args = tag.split("#");
				int type = Integer.valueOf(args[0]);
				int position = Integer.valueOf(args[1]);
				switch (type) {
				case PurposeAdapter.TYPE_INDUSTRY:
					industryList.remove(position);
					industryAdapter.notifyDataSetChanged();
					break;
				case PurposeAdapter.TYPE_POST:
					postList.remove(position);
					postAdapter.notifyDataSetChanged();
					break;
				case PurposeAdapter.TYPE_AREA:
					areaList.remove(position);
					areaAdapter.notifyDataSetChanged();
					break;

				}

			}
		}
	}

	class PurposeAdapter extends BaseAdapter {

		/**
		 * 当前为期望行业列表提供数据
		 */
		public static final int TYPE_INDUSTRY = 0;

		/**
		 * 当前为意向职位列表提供数据
		 * 
		 */
		public static final int TYPE_POST = 1;

		/**
		 * 当前为意向地区列表提供数据
		 */
		public static final int TYPE_AREA = 2;

		/**
		 * 当前为其他意向表提供数据
		 */
		public static final int TYPE_METADATA = 3;

		/**
		 * 记录当前提供数据的类型
		 */
		private int current_type = -1;

		public PurposeAdapter(int type) {
			this.current_type = type;
		}

		@Override
		public int getCount() {
			int count = 0;
			switch (current_type) {
			case 0:
				count = industryList.size() + 1;
				break;
			case 1:
				count = postList.size() + 1;
				break;
			case 2:
				count = areaList.size() + 1;
				break;
			case 3:
				count = 2;
				break;
			}
			return count;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder h = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.purpose_item, null);
				h = new Holder();
				h.itemNameText = (TextView) convertView
						.findViewById(R.id.itemNameText);
				h.itemValueText = (TextView) convertView
						.findViewById(R.id.itemValueText);
				h.delImgBtn = (LinearLayout) convertView
						.findViewById(R.id.delImgBtn);
				h.delImgBtn.setOnClickListener(PurposeActivity.this);
				h.accesoryImg = (ImageView) convertView
						.findViewById(R.id.accessoryImg);

				convertView.setTag(h);
			} else {
				h = (Holder) convertView.getTag();
			}

			// 如果不是其他意向，将第一行作为进入下一集选择列表
			// 如：意向行业，点击第一行进入选择行业列表
			if (current_type != TYPE_METADATA) {
				if (position == 0) {

					int count = -1;
					switch (current_type) {
					case 0:
						if (industryList != null)
							count = industryList.size();
						h.itemNameText.setText("期望行业" + "(" + count + "/" + MAX
								+ ")");
						break;
					case 1:
						if (industryList != null)
							count = postList.size();
						h.itemNameText.setText("期望职位(" + count + "/" + MAX
								+ ")");
						break;
					case 2:
						if (industryList != null)
							count = areaList.size();
						h.itemNameText.setText("期望地点(" + count + "/" + MAX
								+ ")");
						break;
					}

					h.accesoryImg.setVisibility(View.VISIBLE);
					h.delImgBtn.setVisibility(View.GONE);

				} else {

					switch (current_type) {
					case 0: // 取意向行业
						h.itemNameText.setText(industryList.get(position - 1)
								.getName());
						break;
					case 1: // 取意向职位
						h.itemNameText.setText(postList.get(position - 1)
								.getName());
						break;
					case 2: // 取意向地点
						h.itemNameText.setText(areaList.get(position - 1)
								.getName());
						break;
					}

					h.accesoryImg.setVisibility(View.GONE);
					if (edit) {
						h.delImgBtn.setVisibility(View.VISIBLE);
					} else {
						h.delImgBtn.setVisibility(View.GONE);
					}
					// 用于删除意向，标记是哪一行，相应的code
					String tag = current_type + "#" + (position - 1);
					h.delImgBtn.setTag(tag);
				}

				if (getCount() > 1 && position == 0) {
					convertView
							.setBackgroundResource(R.drawable.list_section_header_selector);
				} else {
					convertView
							.setBackgroundResource(R.drawable.list_item_grey_selector);

				}
			} else { // 显示的时其他意向的列表
				h.itemNameText.setText(otherPurposeTitles.get(position));
				if (jobObjectiveDTO != null) {
					if (position == 0) {
						h.itemValueText.setText(jobObjectiveDTO
								.getWorkPropertyName());
					}

					if (position == 1) {
						// 是否面议 “true” 面议
						String negotiable = jobObjectiveDTO
								.getNegotiableSalary();
						String valueSring = "";
						if ("true".equals(negotiable)) {
							valueSring = "面议";
						} else if (jobObjectiveDTO.getMaxSalary() != null
								&& jobObjectiveDTO.getMaxSalary() != 0
								&& jobObjectiveDTO.getMinSalary() != 0
								&& jobObjectiveDTO.getMinSalary() != null) {

							valueSring = jobObjectiveDTO.getMinSalary() + "至"
									+ jobObjectiveDTO.getMaxSalary();
						}
						h.itemValueText.setText(valueSring);
					}
				}

				h.itemValueText.setVisibility(View.VISIBLE);

				h.accesoryImg.setVisibility(View.VISIBLE);
				h.delImgBtn.setVisibility(View.GONE);

			}

			return convertView;
		}

		class Holder {
			TextView itemNameText;
			TextView itemValueText;
			LinearLayout delImgBtn; // 删除按钮
			ImageView accesoryImg; //
		}
	}
}
