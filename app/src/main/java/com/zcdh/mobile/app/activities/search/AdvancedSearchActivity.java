package com.zcdh.mobile.app.activities.search;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobObjectiveAreaDTO;
import com.zcdh.mobile.api.model.SearchConditionDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.zcdh.mobile.app.ActivityDispatcher;

/**
 * 高级搜索
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_advanced_search)
public class AdvancedSearchActivity extends BaseActivity {

	public static final int kREQUEST_ADVANCE_SEARCH = 2019;

	public static final String kDATA_CONDITIONS = "kDATA_CONDITIONS";
	public static final String kDATA_VALUES_NAME = "kDATA_VALUES_NAME";
	public static final String kDATA_IS_ADVANCESEARCH = "kDATA_IS_ADVANCESEARCH";
	public static final String kDATA_SELECTED_AREAS = "kDATA_SELECTED_AREAS";
	
	@ViewById(R.id.scrollView)
	ScrollView scrollView;

	@ViewById(R.id.searchSettingListView)
	ListViewInScrollView searchSettingListView;

	private SearchSettoingAdapter searchSettoingAdapter;

	@ViewById(R.id.searchBtn)
	Button searchBtn;

	Menu action_clear;

	/**
	 * 保存每一项选择的值名称
	 */
	@Extra
	HashMap<Integer, String> conditionValuesName = new HashMap<Integer, String>();

	/**
	 * 选择的地区
	 * 
	 */
	@Extra
	HashMap<String, JobObjectiveAreaDTO> selectedAreas = new HashMap<String, JobObjectiveAreaDTO>();
	
	/**
	 * 搜索条件
	 */
	@Extra
	SearchConditionDTO conditionDTO = new SearchConditionDTO();
	
	@Extra
	boolean isAdvanceSearch=true;

	private boolean hasConditons;

	@AfterViews
	void bindViews() {

		// 设置标题
		if(isAdvanceSearch){
			SystemServicesUtils.setActionBarCustomTitle(this,
					getSupportActionBar(), "高级查询");
		}else{
			SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), getString(R.string.title_filter));
		}

		searchSettoingAdapter = new SearchSettoingAdapter();
		searchSettingListView.setAdapter(searchSettoingAdapter);
		scrollView.smoothScrollBy(0, 0);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(hasConditons){
			getMenuInflater().inflate(R.menu.clear, menu);
		}
		action_clear = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 多选确定
		if (item.getItemId() == R.id.action_clear) {
			conditionDTO = new SearchConditionDTO();
			conditionValuesName = new HashMap<Integer, String>();
			selectedAreas = new HashMap<String, JobObjectiveAreaDTO>();
			searchSettoingAdapter.update();
			shouldClear();
			return false;
		}

		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return false;
	}

	@Click(R.id.searchBtn)
	void onSearch() {
		// SearchResultsActivity_.intent(this).extraConditionDTO(conditionDTO)
		// .flags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT).start();
		Intent data = new Intent();
		data.putExtra(kDATA_CONDITIONS, conditionDTO);
		data.putExtra(kDATA_VALUES_NAME, conditionValuesName);
		data.putExtra(kDATA_IS_ADVANCESEARCH, isAdvanceSearch);
		data.putExtra(kDATA_SELECTED_AREAS, selectedAreas);
		
		data.putExtra(Constants.kRESULT_CODE, RESULT_OK);
		data.putExtra(Constants.kREQUEST_CODE, kREQUEST_ADVANCE_SEARCH);

		data.setAction(Constants.kACTION_CONDITION);
		
		sendBroadcast(data);
		
		finish();

	}

	/**
	 * 选择参数
	 * 
	 * @param position
	 */
	@ItemClick(R.id.searchSettingListView)
	void onItemClick(int position) {

		if (position == 0) { // 意向地区
			AreaActivity_.intent(this).signle(false)
					.selectedAreas(selectedAreas)
					.startForResult(AreaActivity.kREQUEST_AREA);
		}
		if (position == 1) {// 职位类别
			CategoryPostActivity_.intent(this)

			.startForResult(PostsActivity.kREQUEST_POST);
		}
		if (position == 2) {// 行业
			ActivityDispatcher.to_Industry(true, this);
		}
		if (position == 3) {// 发布时间
			ParamsActivity_.intent(this)
					.paramCategoryCode(ParamsActivity.kCODE_PARAM_PUBLISH_TIME)
					.startForResult(ParamsActivity.kREQUEST_PARAM);
		}
		if (position == 4) {// 薪酬范围
			ParamsActivity_
					.intent(this)
					.paramCategoryCode(ParamsActivity.kCODE_PARAM_PAYMENT_SCOPE)
					.selectedParamCode(conditionDTO.getSalaryCode())
					.startForResult(ParamsActivity.kREQUEST_PARAM);
		}
		if (position == 5) {// 公司性质
			ParamsActivity_
					.intent(this)
					.paramCategoryCode(
							ParamsActivity.kCODE_PARAM_COMPANY_NATURE)
					.selectedParamCode(conditionDTO.getPropertyCode())
					.startForResult(ParamsActivity.kREQUEST_PARAM);
		}
		if (position == 6) {// 公司规模
			ParamsActivity_
					.intent(this)
					.paramCategoryCode(ParamsActivity.kCODE_PARAM_COMPANY_SCALA)
					.selectedParamCode(conditionDTO.getEmployNum())
					.startForResult(ParamsActivity.kREQUEST_PARAM);
		}
	}

	/**
	 * 地区列表返回值
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(AreaActivity.kREQUEST_AREA)
	void onResultArea(int resultCode, Intent data) {
		if (data != null && data.getExtras() != null) {
			// String code =
			// data.getExtras().getString(AreaActivity.kDATA_CODE);
			// String name =
			// data.getExtras().getString(AreaActivity.kDATA_NAME);
			//
			// conditionDTO.setAreaCode(code);
			// conditionValuesName.put(0, name);
			selectedAreas = (HashMap<String, JobObjectiveAreaDTO>) data
					.getExtras()
					.getSerializable(AreaActivity.kDATA_AREA_BUNLDE);
			List<String> areaCodes = new ArrayList<String>();
			for (String key : selectedAreas.keySet()) {
				JobObjectiveAreaDTO jobAreaDto = selectedAreas.get(key);
				areaCodes.add(jobAreaDto.getCode());
			}
			conditionDTO.setAreaCodes(areaCodes);
			// 更新高级搜索设置表单列表
			searchSettoingAdapter.notifyDataSetChanged();
			shouldClear();
		}
	}

	/**
	 * 职位列表返回值
	 * 
	 * @param resultCode
	 * @param data

	 */
	@OnActivityResult(PostsActivity.kREQUEST_POST)
	void onResultPost(int resultCode, Intent data) {
		if (data != null && data.getExtras() != null) {
			String code = data.getExtras().getString(PostsActivity.kDATA_CODE);
			String name = data.getExtras().getString(PostsActivity.kDATA_NAME);

			conditionDTO.setPostCode(code);
			conditionValuesName.put(1, name);

			// 更新高级搜索设置表单列表
			searchSettoingAdapter.notifyDataSetChanged();
			shouldClear();
		}
	}
	/**
	 * 行业列表返回值
	 * 
	 * @param resultCode
	 * @param data
*/
	@OnActivityResult(IndustryActivity.kREQUEST_INDUSTRY)
	void onResultIndustry(int resultCode, Intent data) {
		if (data != null && data.getExtras() != null) {
			String code = data.getExtras().getString(
					IndustryActivity.kDATA_CODE);
			String name = data.getExtras().getString(
					IndustryActivity.kDATA_NAME);

			conditionDTO.setIndustryCode(code);
			conditionValuesName.put(2, name);

			// 更新高级搜索设置表单列表
			searchSettoingAdapter.notifyDataSetChanged();
			shouldClear();
		}
	}

	/**
	 * 其他参数列表返回值
	 * 
	 * @param resultCode
	 * @param data
*/
	@OnActivityResult(ParamsActivity.kREQUEST_PARAM)
	void onResultParam(int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {

			String paramCategory = data.getExtras().getString(
					ParamsActivity.kPARAM_CATEGORY_CODE);
			String code = data.getExtras().getString(ParamsActivity.kDATA_CODE);
			String name = data.getExtras().getString(ParamsActivity.kDATA_NAME);

			Log.i("advance search onresult :", code + "#" + name);
			// 获得选择的发布时间
			if (ParamsActivity.kCODE_PARAM_PUBLISH_TIME.equals(paramCategory)) {
				conditionDTO.setTimeBucket(code);
				conditionValuesName.put(3, name);
			}
			// 获得选择的薪资范围
			if (ParamsActivity.kCODE_PARAM_PAYMENT_SCOPE.equals(paramCategory)) {
				conditionDTO.setSalaryCode(code);
				conditionValuesName.put(4, name);
			}
			// 获得选择的公司性质
			if (ParamsActivity.kCODE_PARAM_COMPANY_NATURE.equals(paramCategory)) {
				conditionDTO.setPropertyCode(code);
				conditionValuesName.put(5, name);
			}
			// 获得选择的公司规模
			if (ParamsActivity.kCODE_PARAM_COMPANY_SCALA.equals(paramCategory)) {
				conditionDTO.setEmployNum(code);
				conditionValuesName.put(6, name);
			}

			// 更新高级搜索设置表单
			searchSettoingAdapter.notifyDataSetChanged();
			shouldClear();
		}
	}

	/**
	 * 高级搜索设置表单列表适配
	 * 
	 * @author yangjiannan
	 * 
	 */
	class SearchSettoingAdapter extends BaseAdapter {

		private ArrayList<String> searchConditionsNames;

		public SearchSettoingAdapter() {
			update();

		}

		public void update() {
			searchConditionsNames = new ArrayList<String>();
			searchConditionsNames.add(0, getString(R.string.search_area));
			searchConditionsNames.add(0, getString(R.string.search_post));
			searchConditionsNames.add(0, getString(R.string.search_industry));
			searchConditionsNames.add(0,
					getString(R.string.search_publish_date));
			searchConditionsNames.add(0, getString(R.string.search_payment));
			searchConditionsNames.add(0,
					getString(R.string.search_company_nature));
			searchConditionsNames.add(0,
					getString(R.string.search_company_scale));

			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return searchConditionsNames.size();
		}

		@Override
		public Object getItem(int p) {
			return searchConditionsNames.get(p);
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
				contentView = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.simple_listview_item_clearable, null);

				h.itemName = (TextView) contentView
						.findViewById(R.id.itemNameText);
				h.itemValue = (TextView) contentView
						.findViewById(R.id.secondItemNameText);
				h.clearBtn = (ImageButton) contentView
						.findViewById(R.id.clearBtn);
				h.clearBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int index = (Integer) v.getTag();
						switch (index) {
						case 0:
							conditionDTO.getAreaCodes().clear();
							selectedAreas.clear();
							break;

						case 1:
							conditionDTO.setPostCode(null);
							conditionValuesName.put(1, null);
							break;
						case 2:
							conditionDTO.setIndustryCode(null);
							conditionValuesName.put(2, null);
							break;
						case 3:
							conditionDTO.setTimeBucket(null);
							conditionValuesName.put(3, null);
							break;
						case 4:
							// 获得选择的薪资范围
							conditionDTO.setSalaryCode(null);
							conditionValuesName.put(4, null);
							break;
						case 5:
							// 获得选择的公司性质
							conditionDTO.setPropertyCode(null);
							conditionValuesName.put(5, null);
							break;
						case 6:
							// 获得选择的公司规模
							conditionDTO.setEmployNum(null);
							conditionValuesName.put(6, null);
							break;
						}
						shouldClear();
						searchSettoingAdapter.notifyDataSetChanged();
					}
				});
				h.accsesory = (ImageView) contentView
						.findViewById(R.id.accessoryImg);
				contentView.setTag(h);
			} else {
				h = (Holder) contentView.getTag();
			}
			h.clearBtn.setTag(p);

			String value = "";
			if (p == 0 && selectedAreas != null) {
				for (String key : selectedAreas.keySet()) {
					value += "/ " + selectedAreas.get(key).getName();
				}
				if (value.length() > 1) {
					value = value.substring(1);
				}
			} else {
				value = conditionValuesName.get(p);
			}
			h.itemName.setText(searchConditionsNames.get(searchConditionsNames
					.size() - 1 - p));

			if (!StringUtils.isBlank(value)) {
				h.itemValue.setText(value);
				h.clearBtn.setVisibility(View.VISIBLE);
				h.accsesory.setVisibility(View.GONE);
			} else {
				h.itemValue.setText("");
				h.itemValue.setHint(getString(R.string.chosen));
				h.clearBtn.setVisibility(View.GONE);
				h.accsesory.setVisibility(View.VISIBLE);
			}

			return contentView;
		}

		/**
		 * 更具下标获取 conditionDTO
		 */
		String getconditionByPosition(int p) {
			String value = "";
			switch (p) {
			case 0:
				value = conditionDTO.getAreaCode();
				break;
			case 1:
				value = conditionDTO.getPostCode();
				break;
			case 2:
				value = conditionDTO.getIndustryCode();
				break;
			case 3:
				value = conditionDTO.getTimeBucket();
				break;
			case 4:
				value = conditionDTO.getSalaryCode();
				break;
			// case 0:
			// // value = conditionDTO.get
			// break;
			// case 0:
			// value = conditionDTO.get
			// break;

			}
			return "";
		}

		class Holder {
			TextView itemName = null;
			TextView itemValue = null;
			ImageButton clearBtn = null;
			ImageView accsesory = null;
		}

	}

	/**
	 * 
	 */
	void shouldClear() {

		boolean show = false;
		if (conditionDTO.getAreaCodes() != null
				&& conditionDTO.getAreaCodes().size() > 0)
			show = true;
		if (!StringUtils.isBlank(conditionDTO.getPostCode()))
			show = true;
		if (!StringUtils.isBlank(conditionDTO.getIndustryCode()))
			show = true;
		if (!StringUtils.isBlank(conditionDTO.getTimeBucket()))
			show = true;
		if (!StringUtils.isBlank(conditionDTO.getSalaryCode()))
			show = true;
		if (!StringUtils.isBlank(conditionDTO.getPropertyCode()))
			show = true;
		if (!StringUtils.isBlank(conditionDTO.getEmployNum()))
			show = true;
		hasConditons = show;
		supportInvalidateOptionsMenu();
	}

}
