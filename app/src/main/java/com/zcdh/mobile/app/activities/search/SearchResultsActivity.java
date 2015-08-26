/**
 * 
a * @author jeason, 2014-4-8 下午5:18:54
 */
package com.zcdh.mobile.app.activities.search;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobSearchService;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.SearchConditionDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.app.views.filter.FilterTabView;
import com.zcdh.mobile.app.views.filter.FilterView;
import com.zcdh.mobile.app.views.filter.FilterView.OnSelectListener;
import com.zcdh.mobile.biz.entities.ZcdhArea;
import com.zcdh.mobile.biz.entities.ZcdhParam;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;

import net.tsz.afinal.FinalDb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jeason, 2014-4-8 下午5:18:54
 */
@EActivity(R.layout.activity_post_list)
@OptionsMenu(R.menu.view_on_map)
public class SearchResultsActivity extends BaseActivity implements RequestListener, OnRefreshListener2<ListView>, OnItemClickListener, OnSelectListener {

	@ViewById(R.id.filter)
	FilterTabView filterTab;

	@ViewById(R.id.listview)
	PullToRefreshListView listView;

	LayoutInflater inflater;
	private PostsAdapter adapter;

	private FilterView filter_region;
	private FilterView filter_salary_range;
	private FilterView filter_publish_time;

	private ArrayList<HashMap<String, String>> groupProvinces;
	private SparseArray<LinkedList<HashMap<String, String>>> subGroupsCities;

	List<ZcdhArea> provinces;

	private ArrayList<HashMap<String, String>> groupSalaryRanges;
	private ArrayList<HashMap<String, String>> groupPublishtimes;

	/** request recognition keys */
	public String kREQ_ID_FINDENTPOSTDTOBYHIGHSEARCH;

	private Page<JobEntPostDTO> mPages;

	/** Pagination params */
	private int current_page = 0;
	private final int pageSize = 10;

	@Extra
	SearchConditionDTO extraConditionDTO;

	/** == 搜索接口 == */
	IRpcJobSearchService jobSearchService;

	/** filter types constants */
	public static final int FILTERTYPE_REGION = 0x01;
	public static final int FILTERTYPE_SALARY_RANGE = 0x02;
	public static final int FILTERTYPE_PUBLISH_TIME = 0x03;

	/** 薪酬类型param_category_code */
	private final String CATEGORY_CODE_SALARY_RANGE = "006";
	/** 发布时间类型param_category_code */
	private final String CATEGORY_CODE_PUBLISH_TIME = "020";
	/** 区域类型parent_code */
	private final String PARENT_CODE_REGION = "-1";
	private FinalDb dbTool;

	private EmptyTipView emptyTipView;

	private LoadingIndicator loading;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		loading = new LoadingIndicator(this);
		jobSearchService = RemoteServiceManager.getRemoteService(IRpcJobSearchService.class);
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.search_result));

		dbTool = DbUtil.create(this);
	}

	@SuppressLint("InlinedApi")
	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
	}

	@OptionsItem(R.id.action_view_on_map)
	void onMap(){
		ActivityDispatcher.toVacationSearch(this);
	}
	/**
	 * 
	 * @author jeason, 2014-4-8 下午5:21:38
	 */
	@AfterViews
	void init() {

		inflater = LayoutInflater.from(this);
		adapter = new PostsAdapter();
		listView.setAdapter(adapter);
		listView.setMode(Mode.PULL_FROM_END);
		listView.setOnRefreshListener(this);
		listView.setOnItemClickListener(this);

		stuffFilter();

		// search for the first time
		onSearch(extraConditionDTO, true);
	}

	/**
	 * 
	 * @author jeason, 2014-4-10 上午9:29:38
	 */
	void stuffFilter() {

		System.out.println(System.currentTimeMillis());

		// region data start loading
		groupProvinces = new ArrayList<HashMap<String, String>>();
		subGroupsCities = new SparseArray<LinkedList<HashMap<String, String>>>();
		provinces = dbTool.findAllByWhere(ZcdhArea.class, String.format("parent_code = '%s'", PARENT_CODE_REGION));

		HashMap<String, String> whole_nation = new HashMap<String, String>();
		whole_nation.put("name", "全国");
		whole_nation.put("value", "");
		groupProvinces.add(whole_nation);
		subGroupsCities.put(0, new LinkedList<HashMap<String, String>>());

		int i = 1;
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
		// region data end loading

		// salary range data loading begin
		groupSalaryRanges = new ArrayList<HashMap<String, String>>();
		List<ZcdhParam> salary_ranges = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_SALARY_RANGE));

		HashMap<String, String> non_specific = new HashMap<String, String>();
		non_specific.put("name", "不限");
		non_specific.put("value", "");
		groupSalaryRanges.add(non_specific);

		for (ZcdhParam condition : salary_ranges) {
			HashMap<String, String> condition_map = new HashMap<String, String>();
			condition_map.put("name", condition.getParam_name());
			condition_map.put("value", condition.getParam_code());
			groupSalaryRanges.add(condition_map);
		}
		// salary range data loading end

		groupPublishtimes = new ArrayList<HashMap<String, String>>();
		List<ZcdhParam> publish_times = dbTool.findAllByWhere(ZcdhParam.class, String.format("param_category_code = '%s'", CATEGORY_CODE_PUBLISH_TIME));

		non_specific.put("name", "不限");
		non_specific.put("value", "");
		groupPublishtimes.add(non_specific);

		for (ZcdhParam condition : publish_times) {
			HashMap<String, String> condition_map = new HashMap<String, String>();
			condition_map.put("name", condition.getParam_name());
			condition_map.put("value", condition.getParam_code());
			groupPublishtimes.add(condition_map);
		}

		System.out.println(System.currentTimeMillis());

		// initial region filter
		filter_region = new FilterView(this);
		filter_region.setOnSelectListener(this);
		filter_region.init(this, FILTERTYPE_REGION, groupProvinces, subGroupsCities);

		// initial salary range filter
		filter_salary_range = new FilterView(this);
		filter_salary_range.setOnSelectListener(this);
		filter_salary_range.init(this, FILTERTYPE_SALARY_RANGE, groupSalaryRanges, null);

		// inintial publishtime filter
		filter_publish_time = new FilterView(this);
		filter_publish_time.setOnSelectListener(this);
		filter_publish_time.init(this, FILTERTYPE_PUBLISH_TIME, groupPublishtimes, null);

		// initial filterTab's views source
		ArrayList<View> mViewArray = new ArrayList<View>();
		mViewArray.add(filter_region);
		mViewArray.add(filter_salary_range);
		mViewArray.add(filter_publish_time);
		mViewArray.add(null);

		// initial filtertab's titles source
		ArrayList<String> mTextArray = new ArrayList<String>();
		mTextArray.add("地区");
		mTextArray.add("薪酬");
		mTextArray.add("时间");
		mTextArray.add("筛选");

		// set datasource to FilterTab
		filterTab.setValue(mTextArray, mViewArray);

	}

	/**
	 * 这里实现联网搜索
	 */

	void onSearch(SearchConditionDTO searchParams, boolean update) {
		loading.show();

		// 全部更新
		if (update) {
			current_page = 0;
			// 翻页查看更多
		} else {
			if (mPages == null) return;

			current_page = mPages.getNextPage();

			// 如果没有下一页
			if (mPages.getCurrentPage() == mPages.getMaxpage()) {
				Toast.makeText(this, "没有更多数据", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		doSearch(searchParams, current_page, pageSize);
	}

	@Background
	void doSearch(SearchConditionDTO searchParams, int current_page, int pageSize) {
		jobSearchService.findEntPostDTOByHighSearch(searchParams, current_page, pageSize)
		.identify(kREQ_ID_FINDENTPOSTDTOBYHIGHSEARCH=RequestChannel.getChannelUniqueID(), this);
	}

	public class PostsAdapter extends BaseAdapter {

		private ArrayList<JobEntPostDTO> mPosts = new ArrayList<JobEntPostDTO>();

		public void updateItems(ArrayList<JobEntPostDTO> posts) {
			mPosts = posts;
			notifyDataSetChanged();
		}

		public ArrayList<JobEntPostDTO> getItems() {
			return mPosts;
		}

		public void clearItems() {
			mPosts.clear();
			notifyDataSetChanged();
		}

		public void addItemsToTop(List<JobEntPostDTO> posts) {
			mPosts.addAll(0, posts);
			notifyDataSetChanged();
		}

		public void addItemsToBottom(List<JobEntPostDTO> posts) {
			mPosts.addAll(posts);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mPosts.size();
		}

		@Override
		public Object getItem(int position) {
			return mPosts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.post_item, parent,false);
				holder = new ViewHolder();
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.distance = (TextView) convertView.findViewById(R.id.distance);
				holder.ll_tags_container = (TagsContainer) convertView.findViewById(R.id.ll_tags);
				holder.location_and_requirement = (TextView) convertView.findViewById(R.id.location_and_education);
				holder.publish_time = (TextView) convertView.findViewById(R.id.publish_time);
				holder.salary = (TextView) convertView.findViewById(R.id.salary);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// setBackground
			// if (position % 2 == 0) {
			// convertView.setBackgroundResource(R.drawable.post_item_background_even);
			// } else {
			// convertView.setBackgroundResource(R.drawable.post_item_background_odd);
			// }
			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.drawable.list_item_grey_selector);
			} else {
				convertView.setBackgroundResource(R.drawable.list_item_silver_selector);
			}

			// TODO stuff data
			JobEntPostDTO post = mPosts.get(position);
			holder.location_and_requirement.setText(post.getPostAddress() + "/" + post.getDegree());
			holder.publish_time.setText(DateUtils.getDateByFormat(post.getPublishDate(), "yyyy-MM-hh"));// .getDateByFormatNUM(post.getPublishDate()));
			holder.salary.setText(post.getSalary());
			holder.title.setText(post.getPostAliases());
			holder.content.setText(mPosts.get(position).getEntName());
			double distance = mPosts.get(position).getDistance();
			if (distance == 0) { // 如果为0 不显示
				holder.distance.setVisibility(View.GONE);
			} else if (distance < 1000) { // 如果小于1千米，以米为单位
				holder.distance.setText(String.valueOf(distance) + "米");
			} else if (distance > 1000) {
				holder.distance.setText(String.valueOf(distance / 1000) + "米");
			}

			holder.ll_tags_container.removeAllViews();
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 0, 0, 0);
			if (mPosts.get(position).getWelfares() != null) {
				holder.ll_tags_container.initData(mPosts.get(position).getWelfares().subList(0, 3), null, Gravity.RIGHT);
			}

			return convertView;
		}
	}

	public class ViewHolder {
		TextView title;
		TextView publish_time;
		TextView content;
		TextView salary;
		TextView location_and_requirement;
		TextView distance;
		TagsContainer ll_tags_container;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 下拉刷新
		onGetLatestData();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				onComplete();
			}

		}, 1000);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 上拉获取更多
		onGetMoreData();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				onComplete();
			}

		}, 1000);
	}

	@UiThread
	void onComplete() {
		listView.onRefreshComplete();
	}

	/**
	 * 
	 * @author jeason, 2014-4-9 上午8:59:18
	 */
	private void onGetLatestData() {
		onSearch(extraConditionDTO, true);
	}

	/**
	 * 
	 * @author jeason, 2014-4-9 上午9:00:03
	 */
	private void onGetMoreData() {
		onSearch(extraConditionDTO, false);

	}

	@Override
	public void onRequestStart(String reqId) {
		loading.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_FINDENTPOSTDTOBYHIGHSEARCH)) {
			setList((Page<JobEntPostDTO>) result);
		}
	}

	/**
	 * 
	 * @author jeason, 2014-4-14 下午5:41:04
	 * @param page
	 */
	private void setList(Page<JobEntPostDTO> page) {
		if (page == null) {
			if (mPages != null && mPages.getCurrentPage() == 1) {
				adapter.clearItems();
			}
			setEmptyView();
			return;
		}

		mPages = page;

		// 如果为第一页
		if (mPages.getCurrentPage() == 1) {
			adapter.updateItems((ArrayList<JobEntPostDTO>) mPages.getElements());
		} else {
			int lastPosition = adapter.getCount();
			adapter.addItemsToBottom(mPages.getElements());
			listView.getRefreshableView().setSelection(lastPosition - 1);
		}
	}

	/**
	 * 
	 * @author jeason, 2014-4-16 下午10:42:06
	 */
	private void setEmptyView() {
		if (adapter.getItems().isEmpty()) {

			if (emptyTipView == null) {
				emptyTipView = new EmptyTipView(this);
			}
			listView.setEmptyView(emptyTipView);
		}
	}

	@Override
	public void onRequestError(String reqId, Exception error) {

	}

	@Override
	public void onRequestFinished(String reqId) {
		setEmptyView();
		loading.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		JobEntPostDTO dto = ((JobEntPostDTO) adapter.getItem(position - 1));
		long postId = dto.getPostId();
		long entId = dto.getEntId();
		DetailsFrameActivity_.intent(this)
		.posts(adapter.mPosts)
		.postId(postId)
		.entId(entId)
		.switchable(true)
		.currentIndex(position-1)
		.start();
	}

	@Override
	public void getValue(int filter_type, String value, int level1_selected_position, int level2_selected_position, String showText) {
		switch (filter_type) {
		// 区域过滤
		case FILTERTYPE_REGION:
			extraConditionDTO.setAreaCode(value);
			break;
		// 薪酬过滤
		case FILTERTYPE_SALARY_RANGE:
			extraConditionDTO.setSalaryCode(value);
			break;
		// 发布时间
		case FILTERTYPE_PUBLISH_TIME:
			extraConditionDTO.setTimeBucket(value);
			break;
		default:
			break;
		}
		onSearch(extraConditionDTO, true);

		filterTab.onPressBack();
	}

	/**
	 * 筛选按钮点击
	 */
	public void onFilterTabExtraButtonClick() {
		AdvancedSearchActivity_.intent(this).start();
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.app.views.filter.FilterView.OnSelectListener#onTimeSelector(int, java.util.Date, java.util.Date)
	 */
	@Override
	public void onTimeSelector(int filter_type, Date start_date, Date end_date) {
		// TODO Auto-generated method stub
		
	}
	
	
}
