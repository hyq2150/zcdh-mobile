/**
 * 
 * @author jeason, 2014-4-21 上午11:41:43
 */
package com.zcdh.mobile.app.activities.vacation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.androidannotations.api.BackgroundExecutor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcHolidayService;
import com.zcdh.mobile.api.model.EntPostSearchDTO;
import com.zcdh.mobile.api.model.SearchAreaDTO;
import com.zcdh.mobile.api.model.SearchHolidayConditionDTO;
import com.zcdh.mobile.api.model.SearchIndustryDTO;
import com.zcdh.mobile.api.model.SearchMajorDTO;
import com.zcdh.mobile.api.model.SearchTitleDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.filter.FilterTabView;
import com.zcdh.mobile.app.views.filter.FilterView;
import com.zcdh.mobile.app.views.filter.FilterView.OnSelectListener;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 职位搜索
 * @author jeason, 2014-4-21 上午11:41:43
 */

public class SearchActivity extends BaseActivity implements OnRefreshListener2<ListView>, OnSelectListener, RequestListener {

	public String kREQ_ID_findSearchHolidayTitleDTO;
	public String kREQ_ID_findEntPostSearchDTO;

	private PullToRefreshListView listview;
	private PostsAdapter adapter;
	private IRpcHolidayService service;

	/** 搜索按钮 */
	Button searchBtn;

	private FilterTabView tabView;
	private EditText et_search;
	private FilterView fiterCities;
	private FilterView fiterMajors;
	private FilterView fiterIndustries;
	private FilterView filterTimes;
	public final static int FILTERTYPE_CITIES = 0x01;
	public final static int FILTERTYPE_MAJORS = 0x02;
	public final static int FILTERTYPE_INDUSTRIES = 0x03;
	public final static int FILTERTYPE_TIME = 0x04;

	private final int page_size = 10;
	Page<EntPostSearchDTO> current_posts;
	SearchHolidayConditionDTO condition;
	private String keyword = "";
	InputMethodManager imm;

	private AsyncTask<Void, Void, Page<EntPostSearchDTO>> getMore;
	private AsyncTask<Void, Void, Page<EntPostSearchDTO>> searchTask;
	private AsyncTask<Void, Void, SearchTitleDTO> filterTask;
	private EmptyTipView empty_view;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "职位搜索");

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		service = RemoteServiceManager.getRemoteService(IRpcHolidayService.class, this);
		empty_view = new EmptyTipView(this);

		setContentView(R.layout.search_list);
		adapter = new PostsAdapter(this);
		adapter.setIs_vacation(true);
		bindviews();
		condition = new SearchHolidayConditionDTO();
		// getUserId(), null, null, null, null, null, "007.005");
		condition.setUser_id(getUserId());
		condition.setWorkPropertyCode("007.005");
		getFilters();
		getMorePosts();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(com.
	 * actionbarsherlock.view.Menu)
	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		menu.clear();
//		getSupportMenuInflater().inflate(R.menu.action_orders, menu);
//		return super.onCreateOptionsMenu(menu);
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_orders:
			ActivityDispatcher.toOrders(this);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * @author jeason, 2014-4-25 下午3:13:50
	 */
	private void bindviews() {
		// initUI();

		setTitle("暑假工");

		listview = (PullToRefreshListView) findViewById(R.id.listview);

		// rightBtnText.setText(R.string.search);
		// rightBtnText.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// onKeywords();
		// }
		// });

		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if(!StringUtils.isBlank(et_search.getText().toString()))
				onKeywords();
			}
		});

		tabView = (FilterTabView) findViewById(R.id.filter);
		et_search = (EditText) findViewById(R.id.search_bar);
		et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					onKeywords();
					imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					return true;
				}
				return false;
			}
		});
		listview.setAdapter(adapter);
		listview.setMode(Mode.PULL_FROM_END);
		listview.setOnRefreshListener(this);
		listview.setEmptyView(empty_view);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ActivityDispatcher.toDetailsFrameActivity(SearchActivity.this, adapter.getItem(arg2 - 1).getPostId(), false, null, 0);
			}

		});

	}

	/**
	 * 
	 * @author jeason, 2014-4-25 下午3:04:33
	 */
	private void getFilters() {

		BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {

			@Override
			public void execute() {
				try {
					service.findSearchHolidayTitleDTO().identify(kREQ_ID_findSearchHolidayTitleDTO = RequestChannel.getChannelUniqueID(), SearchActivity.this);
				} catch (Throwable e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
				}
			}

		});
	}

	/**
	 * 填充过滤头部
	 * 
	 * @param result
	 * @author jeason, 2014-4-25 下午3:05:27
	 */
	protected void initFilters(SearchTitleDTO result) {
		if (result == null) {
			Toast.makeText(this, "没有过滤条件", Toast.LENGTH_SHORT).show();
			return;
		}
		ArrayList<HashMap<String, String>> provinces = new ArrayList<HashMap<String, String>>();
		SparseArray<LinkedList<HashMap<String, String>>> cities = new SparseArray<LinkedList<HashMap<String, String>>>();

		HashMap<String, String> whole_nation = new HashMap<String, String>();
		whole_nation.put("name", "全国");
		whole_nation.put("value", "");
		provinces.add(whole_nation);
		cities.put(0, new LinkedList<HashMap<String, String>>());

		int i = 1;
		for (SearchAreaDTO area : result.getAreas()) {
			HashMap<String, String> province = new HashMap<String, String>();
			province.put("name", area.getName() + "(" + area.getPostCount() + ")");
			province.put("value", area.getCode());
			provinces.add(province);
			LinkedList<HashMap<String, String>> cities_of_prov = new LinkedList<HashMap<String, String>>();
			for (SearchAreaDTO subArea : area.getSubAreas()) {
				HashMap<String, String> city = new HashMap<String, String>();
				city.put("name", subArea.getName() + "(" + subArea.getPostCount() + ")");
				city.put("value", subArea.getCode());
				cities_of_prov.add(city);
			}
			cities.put(i, cities_of_prov);
			i++;
		}

		this.fiterCities = new FilterView(this);
		fiterCities.setOnSelectListener(this);
		fiterCities.init(this, FILTERTYPE_CITIES, provinces, cities);

		ArrayList<HashMap<String, String>> majors = new ArrayList<HashMap<String, String>>();
		SparseArray<LinkedList<HashMap<String, String>>> subMajors = new SparseArray<LinkedList<HashMap<String, String>>>();

		HashMap<String, String> non_specific = new HashMap<String, String>();
		non_specific.put("name", "不限");
		non_specific.put("value", "");
		majors.add(non_specific);
		subMajors.put(0, new LinkedList<HashMap<String, String>>());

		i = 1;
		for (SearchMajorDTO major : result.getMajors()) {

			HashMap<String, String> major_hash = new HashMap<String, String>();
			major_hash.put("name", major.getName() + "(" + major.getPostCount() + ")");
			major_hash.put("value", major.getCode());
			majors.add(major_hash);

			LinkedList<HashMap<String, String>> subMajors_of_major = new LinkedList<HashMap<String, String>>();
			for (SearchMajorDTO subMajor : major.getSubMajors()) {
				HashMap<String, String> subMajor_hash = new HashMap<String, String>();
				subMajor_hash.put("name", subMajor.getName() + "(" + subMajor.getPostCount() + ")");
				subMajor_hash.put("value", subMajor.getCode());
				subMajors_of_major.add(subMajor_hash);
			}
			subMajors.put(i, subMajors_of_major);
			i++;
		}

		this.fiterMajors = new FilterView(this);
		fiterMajors.setOnSelectListener(this);
		fiterMajors.init(this, FILTERTYPE_MAJORS, majors, subMajors);

		ArrayList<HashMap<String, String>> industries = new ArrayList<HashMap<String, String>>();
		SparseArray<LinkedList<HashMap<String, String>>> subIndustries = new SparseArray<LinkedList<HashMap<String, String>>>();

		non_specific = new HashMap<String, String>();
		non_specific.put("name", "不限");
		non_specific.put("value", "");
		industries.add(non_specific);
		subIndustries.put(0, new LinkedList<HashMap<String, String>>());

		i = 1;
		for (SearchIndustryDTO industry : result.getIndusties()) {

			HashMap<String, String> industry_hash = new HashMap<String, String>();
			industry_hash.put("name", industry.getName() + "(" + industry.getPostCount() + ")");
			industry_hash.put("value", industry.getCode());
			industries.add(industry_hash);

			LinkedList<HashMap<String, String>> subIndustries_of_industry = new LinkedList<HashMap<String, String>>();
			for (SearchIndustryDTO subIndustry : industry.getSubIndustries()) {
				HashMap<String, String> subIndustry_hash = new HashMap<String, String>();
				subIndustry_hash.put("name", subIndustry.getName() + "(" + subIndustry.getPostCount() + ")");
				subIndustry_hash.put("value", subIndustry.getCode());
				subIndustries_of_industry.add(subIndustry_hash);
			}
			subIndustries.put(i, subIndustries_of_industry);
			i++;
		}

		this.fiterIndustries = new FilterView(this);
		fiterIndustries.setOnSelectListener(this);
		fiterIndustries.init(this, FILTERTYPE_INDUSTRIES, industries, subIndustries);

		filterTimes = new FilterView(this);
		filterTimes.initTimeFiter(this, i);
		filterTimes.setOnSelectListener(this);

		ArrayList<String> strs = new ArrayList<String>();
		strs.add("地区");
		strs.add("专业");
		strs.add("行业");
		strs.add("时间");
		ArrayList<View> views = new ArrayList<View>();
		views.add(fiterCities);
		views.add(fiterMajors);
		views.add(fiterIndustries);
		views.add(filterTimes);
		tabView.setValue(strs, views);
	}

	/**
	 * 
	 * 
	 * @author jeason, 2014-4-25 下午3:04:00
	 */
	protected void onKeywords() {
		// TODO 这里处理搜索
		keyword = this.et_search.getText().toString().trim();
		doSearch();

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO 没有下拉操作
		listview.onRefreshComplete();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 获取更多
		getMorePosts();

	}

	/**
	 * 当查询条件改编 重新查数据
	 * 
	 * @author jeason, 2014-4-25 下午8:54:45
	 */
	void doSearch() {
		BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {

			@Override
			public void execute() {
				try {
					service.findEntPostSearchDTO(keyword, condition, 1, page_size).identify(kREQ_ID_findEntPostSearchDTO = RequestChannel.getChannelUniqueID(), SearchActivity.this);
				} catch (Throwable e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
				}
			}

		});
	}

	/**
	 * 
	 * @author jeason, 2014-4-25 下午2:44:08
	 */
	private void getMorePosts() {
		getMore = new AsyncTask<Void, Void, Page<EntPostSearchDTO>>() {

			@Override
			protected Page<EntPostSearchDTO> doInBackground(Void... params) {
				int current_page = 1;
				if (current_posts == null) {
					current_page = 1;
				} else if (current_posts.hasNextPage()) {
					current_page++;
				} else {
					return null;
				}
				try {
					service.findEntPostSearchDTO(keyword, condition, current_page, page_size).identify(kREQ_ID_findEntPostSearchDTO = RequestChannel.getChannelUniqueID(), SearchActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				return null;

			}

			protected void onPostExecute(Page<EntPostSearchDTO> result) {
					onComplete();
			};

		}.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.view.filter.FilterView.OnSelectListener#onTimeSelector
	 * (int, java.util.Date, java.util.Date)
	 */
	@Override
	public void onTimeSelector(int filter_type, Date start_date, Date end_date) {
		// TODO Auto-generated method stub
		tabView.onPressBack();
		condition.setStartTime(start_date);
		condition.setEndTime(end_date);
		onKeywords();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zcdh.mobile.view.filter.FilterView.OnSelectListener#getValue(int,
	 * java.lang.String, int, int, java.lang.String)
	 */
	@Override
	public void getValue(int filter_type, String value, int level1_selected_position, int level2_selected_position, String showText) {
		tabView.onPressBack();
		switch (filter_type) {
		case FILTERTYPE_CITIES:
			List<String> areas = condition.getAreaCodes();
			if (areas == null) {
				areas = new ArrayList<String>();
			} else {
				areas.clear();
			}
			areas.add(value);
			condition.setAreaCodes(areas);
			break;
		case FILTERTYPE_INDUSTRIES:
			List<String> industries = condition.getIndustryCodes();
			if (industries == null) {
				industries = new ArrayList<String>();

			} else {
				industries.clear();
			}
			industries.add(value);
			condition.setIndustryCodes(industries);
			break;
		case FILTERTYPE_MAJORS:
			List<String> majors = condition.getMajorCodes();
			if (majors == null) {
				majors = new ArrayList<String>();
			} else {
				majors.clear();
			}
			majors.add(value);
			condition.setMajorCodes(majors);
			break;

		default:
			break;
		}
		onKeywords();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zcdh.mobile.activity.framework.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (searchTask != null) {
			searchTask.cancel(true);
		}
		if (getMore != null) {
			getMore.cancel(true);
		}
		if (filterTask != null) {
			filterTask.cancel(true);
		}
		super.onDestroy();
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
		// TODO Auto-generated method stub

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
		if (reqId.equals(kREQ_ID_findSearchHolidayTitleDTO)) {
			SearchTitleDTO filters = (SearchTitleDTO) result;
			initFilters(filters);
		}

		if (reqId.equals(kREQ_ID_findEntPostSearchDTO)) {
			if (result != null) {
				current_posts = (Page<EntPostSearchDTO>) result;
				adapter.updateItems(current_posts.getElements());
			} else {
				adapter.clearItems();
				Toast.makeText(SearchActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
			}

			onComplete();
		}
	}

	void onComplete() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listview.onRefreshComplete();
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}
}
