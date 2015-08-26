package com.zcdh.mobile.app.activities.parttimer;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobObjectiveAreaDTO;
import com.zcdh.mobile.api.model.SearchConditionDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.activities.search.AdvancedSearchActivity;
import com.zcdh.mobile.app.activities.search.AreaActivity;
import com.zcdh.mobile.app.activities.search.AreaActivity_;
import com.zcdh.mobile.app.activities.search.CategoryPostActivity_;
import com.zcdh.mobile.app.activities.search.MajorsActivity;
import com.zcdh.mobile.app.activities.search.PostsActivity;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.views.GridViewInScrollView;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_advace_search_parttimer)
public class PartTimerAdvacedSearchActivity extends BaseActivity {
	
	public static final String kDATA_SELECTED_WEEK = "kDATA_SELECTED_WEEK";

	/**
	 * 清除设置
	 */
	Menu action_clear;
	
	/**
	 * 
	 */
	@ViewById(R.id.scrollView)
	ScrollView scrollView;

	/**
	 * 搜索选项设置
	 */
	@ViewById(R.id.searchSettingListView)
	ListViewInScrollView searchSettingListView;
	
	/**
	 * 时间选择
	 */
	@ViewById(R.id.timeGrideview)
	GridViewInScrollView timeGridView;
	
	TimeAdapter timeAdapter;
	
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
	
	SearchSettoingAdapter searchSettoingAdapter;
	
	/**
	 * 是否显示清除
	 */
	boolean changed = true;
	
	/**
	 * 选择的星期
	 */
	@Extra
	List<String> selectedWeek = new ArrayList<String>();

	@AfterViews
	void bindViews(){
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), "筛选");
		searchSettoingAdapter = new SearchSettoingAdapter();
		searchSettingListView.setAdapter(searchSettoingAdapter);
		
		
		if(conditionDTO.getWeekdays()==null 
				|| conditionDTO.getWeekdays().size()==0){
			for (int i = 1; i <= 7; i++) {
				selectedWeek.add(i+"");
			}
			conditionDTO.setWeekdays(selectedWeek);
		}else{
			selectedWeek = conditionDTO.getWeekdays();
		}
		timeAdapter=  new TimeAdapter();
		timeGridView.setAdapter(timeAdapter);
		timeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				boolean ex = false;
				for (int i = 0; i < selectedWeek.size(); i++) {
					if(((position+1)+"").equals(selectedWeek.get(i))){
						ex = true;
						selectedWeek.remove(i);
					}
				}
				if(!ex)selectedWeek.add((position+1)+"");
				if(selectedWeek.size()>0){
					changed = true;
					supportInvalidateOptionsMenu();
				}
				conditionDTO.setWeekdays(selectedWeek);
				timeAdapter.notifyDataSetChanged();
			}
			
		});
		scrollView.smoothScrollBy(0, 0);
		
	}
	
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (changed) {
			getMenuInflater().inflate(R.menu.clear, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 多选确定
		if (item.getItemId() == R.id.action_clear) {
			conditionDTO = new SearchConditionDTO();
			conditionValuesName = new HashMap<Integer, String>();
			selectedAreas = new HashMap<String, JobObjectiveAreaDTO>();
			selectedWeek.clear();
			searchSettoingAdapter.update();
			timeAdapter.notifyDataSetChanged();
			changed = false;
			supportInvalidateOptionsMenu();
			return false;
		}

		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}

		return false;
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
		if (position == 2) {// 专业
//			CategoryMajorActivity_.intent(this).single(true).startForResult(MajorsActivity.kREQUEST_MAJOR);
			ActivityDispatcher.addMajor(this);
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
		}
	}
	
	@OnActivityResult(MajorsActivity.kREQUEST_MAJOR)
	 void onResultMajor(int resultCode, Intent data){
		if (data != null && data.getExtras() != null) {
			String code = data.getExtras().getString(MajorsActivity.kDATA_CODE);
			String name = data.getExtras().getString(MajorsActivity.kDATA_NAME);
			conditionDTO.setMajorCode(code);
			conditionValuesName.put(2, name);

			// 更新高级搜索设置表单列表
			searchSettoingAdapter.notifyDataSetChanged();
		}
	 }
	
	@Click(R.id.searchBtn)
	void onSearch(){
		Intent data = new Intent();
		data.putExtra(AdvancedSearchActivity.kDATA_CONDITIONS, conditionDTO);
		data.putExtra(AdvancedSearchActivity.kDATA_VALUES_NAME, conditionValuesName);
		data.putExtra(AdvancedSearchActivity.kDATA_IS_ADVANCESEARCH, true);
		data.putExtra(AdvancedSearchActivity.kDATA_SELECTED_AREAS, selectedAreas);
		setResult(RESULT_OK, data);
		finish();
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
		if (!StringUtils.isBlank(conditionDTO.getMajorCode()))
			show = true;
		changed = show;
		if(show)supportInvalidateOptionsMenu();
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
			searchConditionsNames.add(0, "专业");

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
						}
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
	
	class TimeAdapter extends BaseAdapter{
		
		int count = 7;

		@Override
		public int getCount() {
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
			convertView = LayoutInflater.from(PartTimerAdvacedSearchActivity.this)
						.inflate(R.layout.week_item, null);
			TextView itemTextView = (TextView) convertView.findViewById(R.id.itemNameText);
			ImageView checked = (ImageView) convertView.findViewById(R.id.checked);
			switch (position) {
			case 0:
				itemTextView.setText("星期一");
				break;

			case 1:
				itemTextView.setText("星期二");
				break;
			case 2:
				itemTextView.setText("星期三");
				break;
			case 3:
				itemTextView.setText("星期四");
				break;
			case 4:
				itemTextView.setText("星期五");
				break;
			case 5:
				itemTextView.setText("星期六");
				break;
			case 6:
				itemTextView.setText("星期日");
				break;
			}
			checked.setVisibility(View.GONE);
			itemTextView.setTextColor(getResources().getColor(R.color.font_color));
			convertView.setBackgroundColor(getResources().getColor(R.color.grey1));
			if(selectedWeek!=null){
				for (String w:selectedWeek) {
					if(((position+1)+"").equals(w)){
						checked.setVisibility(View.VISIBLE);
						itemTextView.setTextColor(getResources().getColor(R.color.white));
						convertView.setBackgroundColor(getResources().getColor(R.color.blue1));
						break;
					}
				}
			}
			return convertView;
		}
		
	}
}
