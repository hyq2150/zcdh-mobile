package com.zcdh.mobile.app.activities.search;

import com.umeng.analytics.MobclickAgent;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobSearchService;
import com.zcdh.mobile.api.model.JobSearchTagDTO;
import com.zcdh.mobile.api.model.JobTagTypeDTO;
import com.zcdh.mobile.api.model.SearchConditionDTO;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.activities.search.FragmentTagsGroup.TagsSelectedListner;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.framework.views.PageIndicatorCircle;
import com.zcdh.mobile.utils.StringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 搜索主页
 * 
 * @author yangjiannan
 * 
 */
@EFragment(R.layout.fragment_main_search)
public class FragmentMainSearch extends BaseFragment implements
		RequestListener, OnPageChangeListener , TagsSelectedListner{

	private static final String TAG = FragmentMainSearch.class.getSimpleName();
	// 查找标签分类服务标识
	private String kREQ_ID_FINDJOBTAGTYPEDTO; // findJobTagTypeDTO

	// 分页查询标签
	private String kreq_id_findJobSearchTagDTOByPage;
	
	//extra ，将搜索条件带到搜索职位列表
	public final static String extraKeyword = "keyword";
	public final static String extraTagCode = "tagcode";

	@ViewById(R.id.scrollView)
	ScrollView scrollView;
	
	//显示加载动画
	@ViewById(R.id.loadingImg)
	ImageView loadingImg;

	// 显示顶部推广消息
	@ViewById(R.id.messageBoxTxt)
	TextView messageBoxTxt;
	
	@ViewById(R.id.advancedSearchBtn)
	Button advancedSearchBtn;

	// 搜索按钮
	@ViewById(R.id.searchBtn)
	Button searchBtn;

	// 搜索框
	@ViewById(R.id.searchEditText)
	EditText searchEditText;

	// 重置搜索框x按钮
	@ViewById(R.id.inputClearImgBtn)
	ImageButton inputClearImgBtn;

	// 显示标签的ViewPager
	@ViewById(R.id.tagsPager)
	ViewPager tagsPager;

	//
	@ViewById(R.id.indicator)
	PageIndicatorCircle indicator;

	// 标签切换栏
	@ViewById(R.id.tagsSwitchBar)
	RelativeLayout tagsSwitchBar;

	// 标签切换1
	@ViewById(R.id.changyonBtn)
	Button changyonBtn;

	// 标签切换2
	@ViewById(R.id.yingjieBtn)
	Button yingjieBtn;

	// 标签切换3
	@ViewById(R.id.renliBtn)
	Button renliBtn;
	
	@ViewById(R.id.preImgBtn)
	ImageButton preImgBtn;
	
	@ViewById(R.id.nextImgBtn)
	ImageButton nextImgBtn;

	// 标签组适配器
	private TagsPageAdapter tagsPageAdapter;

	// 标签组Fragment
	private ArrayList<FragmentTagsGroup_> tagsFragments = new ArrayList<FragmentTagsGroup_>();

	// 标识标签组共几页
	private int totalPages = 3;

	// 标识标签组当前第几页
	private int currentPage = 1;

	// 每组标签个数
	private static final int tagPageSize = 9;

	// 每个标签类别的标签总页数
	private static int tagsPages;

	// 搜索历史
	@ViewById(R.id.historyListview)
	ListViewInScrollView historyListview;

	private SearchHistoryAdapter historyAdapter;

	private ArrayList<String> historys = new ArrayList<String>();

	// 搜索服务
	private IRpcJobSearchService jobSearchService;

	// 标签类别
	private List<JobTagTypeDTO> jobTagTypes;

	// 保存不同标签类别的标签
	private HashMap<String, List<JobSearchTagDTO>> tags = new HashMap<String, List<JobSearchTagDTO>>();
	//保存各标签类别的最大页数
	private HashMap<String, Integer> maxPages = new HashMap<String, Integer>();

	//保存各标签类别的最后页数
	private HashMap<String, Integer> lastPage = new HashMap<String, Integer>();
	
	// 当前选中的标签类别编码
	private String selectedTagType;
	
	//保存搜索条件
	private SearchConditionDTO searchConditionDTO = new SearchConditionDTO();
	
	//企业ID
	public long entId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jobSearchService = RemoteServiceManager.getRemoteService(
				IRpcJobSearchService.class);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RemoteServiceManager.removeService(this);
	}

	@AfterViews
	void init() {
		
		// ScrollView滚动至最顶端，以显示搜索历史的ListView
		scrollView.smoothScrollBy(0, 0);
		
		indicator.setOnPageChangeListener(this);
		// 从后端加载标签分类数据 > 加载默认第一个类别的标签
		loadTagTypes();
		
		//显示加载动画
		loadingImg.setVisibility(View.VISIBLE);
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.loading);
		loadingImg.startAnimation(anim);
	}
	
	
	/* ============ 调用后端服务 ================== */
	/**
	 * 加载标签
	 */
	@Background
	void loadTagTypes() {
		jobSearchService.findJobTagTypeDTO()
		.identify(kREQ_ID_FINDJOBTAGTYPEDTO=RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 默认先加载第一个标签分类
	 */
	@Background
	void loadTags(String typeCode) {
		jobSearchService.findJobSearchTagDTOByPage(selectedTagType,currentPage, tagPageSize)
				.identify(kreq_id_findJobSearchTagDTOByPage=RequestChannel.getChannelUniqueID(), this);
		
	}
	
	
	/* ==================内部逻辑方法======================= */
	/**
	 * 跳转到搜索职位列表
	 */
	
	void search(){
		String keyWord = searchEditText.getText().toString();
		//boolean  correct = false;
		if(!StringUtils.isBlank(keyWord)){ 
			searchConditionDTO.setKeyWord(keyWord);
		}
		SearchResultsActivity_.intent(this).extraConditionDTO(searchConditionDTO).start();
		searchConditionDTO = new SearchConditionDTO();
	}
	/**
	 * 显示标签分类按钮
	 */
	void showTagSwitchBtns() {
		if (jobTagTypes.size() > 0) {

			// 动态添加切换不同tags类型的Button
			for (int i = 0; i < jobTagTypes.size(); i++) {
				JobTagTypeDTO tag = jobTagTypes.get(i);
				switch (i) {
				case 0:
					changyonBtn.setVisibility(View.VISIBLE);
					changyonBtn.setTag(0);
					changyonBtn.setText(tag.getTagTypeName());
					break;
				case 1:
					yingjieBtn.setVisibility(View.VISIBLE);
					yingjieBtn.setText(tag.getTagTypeName());
					yingjieBtn.setTag(1);
					break;
				case 2:
					renliBtn.setVisibility(View.VISIBLE);
					renliBtn.setText(tag.getTagTypeName());
					renliBtn.setTag(2);
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * 每个标签类别中显示的Fragments
	 */
	void showFragmentsForTags() {
		//如果已存有当前类别的标签，找出总页数
		if(maxPages.get(selectedTagType)!=null){
			totalPages = maxPages.get(selectedTagType);
		}else{
			totalPages = 0;
		}
		
//		Toast.makeText(getActivity(), "totalPages"+totalPages, Toast.LENGTH_SHORT).show();
		if (totalPages > 0) {
			if (tagsFragments.size() == 0) {// 根据size为零判断是否新切换到当前，如果不是，重新初始化FragmentAdapter

				for (int i = 0; i < totalPages; i++) {
					FragmentTagsGroup_ fragmentTagsGroup_ = new FragmentTagsGroup_();
					fragmentTagsGroup_.setTagsSelectedLisnter(this);
					tagsFragments.add(fragmentTagsGroup_);
				}
				// Toast.makeText(getActivity(),
				// "tagsFragments:"+tagsFragments.size(),
				// Toast.LENGTH_SHORT).show();
				// 初始化标签
				if (tagsPageAdapter == null) {
					tagsPageAdapter = new TagsPageAdapter(this, tagsFragments);
					tagsPager.setAdapter(tagsPageAdapter);

					indicator.setVisibility(View.VISIBLE);
					indicator.setViewPager(tagsPager);
				} else {
					tagsPageAdapter.setResetData(tagsFragments);
				}
				//回到最后一页
				if(lastPage.get(selectedTagType)!=null){
					int lastpage = lastPage.get(selectedTagType);
					tagsPager.setCurrentItem(lastpage-1);
				}
			}

		}

	}

	/**
	 * 显示标签
	 */
	void showTags() {
		if (jobTagTypes != null && jobTagTypes.size() > 0) {

			// 当前类别所有的标签
			List<JobSearchTagDTO> allTagsThisType = tags.get(selectedTagType);

			// 当前页显示的标签
			List<JobSearchTagDTO> tags_ = null;

			int startIndex = ((currentPage - 1) <= 0 ? 0 : (currentPage - 1))
					* tagPageSize;
			if(startIndex<0)startIndex=0;
			// 判断当前页是否已有可取的数据
			if (allTagsThisType != null && allTagsThisType.size() > startIndex) {
				tags_ = new ArrayList<JobSearchTagDTO>();
				Log.i("index:", startIndex+"");
				for (int i = 0; i < tagPageSize; i++) {
					if(allTagsThisType.size()-1>=startIndex+i){
						
						Log.i("index:", i+"");
						tags_.add(allTagsThisType.get(startIndex+i));
					}else{
						break;
					}
				}
				//将显示的数据交由GridView处理显示
				tagsFragments.get(currentPage-1).setTags(tags_);
			} else {
				// 如果没有，从后端服务加载
				
				loadTags(selectedTagType);
			}

		}
		
		//显示左右切换按钮
		preImgBtn.setVisibility(View.VISIBLE);
		nextImgBtn.setVisibility(View.VISIBLE);
	}
	
	// 重置相关数据
		void reset(int category) {
			
			lastPage.put(selectedTagType, currentPage);
//			Log.i(selectedTagType, currentPage);
			selectedTagType = jobTagTypes.get(category).getTagTypeCode();
			setSwitchBarSelectedStyle(category);
			
			currentPage = 1;
			tagsFragments.clear();
			tagsPageAdapter.setResetData(tagsFragments);
			
			//重新加载标签Fragments和相应的tags
			showFragmentsForTags();
			showTags();
		}

		void setSwitchBarSelectedStyle(int i) {
			if (0 == i) {
				changyonBtn.setBackgroundResource(R.drawable.tab_active);
				yingjieBtn.setBackgroundResource(R.drawable.tab_normal);
				renliBtn.setBackgroundResource(R.drawable.tab_normal);
			}
			if (1 == i) {
				changyonBtn.setBackgroundResource(R.drawable.tab_normal);
				yingjieBtn.setBackgroundResource(R.drawable.tab_active);
				renliBtn.setBackgroundResource(R.drawable.tab_normal);
			}
			if (2 == i) {
				changyonBtn.setBackgroundResource(R.drawable.tab_normal);
				yingjieBtn.setBackgroundResource(R.drawable.tab_normal);
				renliBtn.setBackgroundResource(R.drawable.tab_active);
			}
		}

	/*========================= UIS事件处理 =======================*/
	// ////////////// indicator 的 OnPageChangeListener事件监听
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int p) {
		currentPage = p+1;
		
		showTags();
	}

	/**
	 * 控制文本框右边的Button隐藏与现实，检测当文本框有内容时显示，否则隐藏
	 */
	@TextChange(R.id.searchEditText)
	void searchEditTextChanged(CharSequence text, TextView hello, int before,
			int start, int count) {
		String txt = hello.getText().toString();
		if (!StringUtils.isBlank(txt)) {
			inputClearImgBtn.setVisibility(View.VISIBLE);
		} else {
			inputClearImgBtn.setVisibility(View.GONE);
		}
	}

	/**
	 * 重置文本框内容
	 */
	@Click(R.id.inputClearImgBtn)
	void onInputClear() {

		searchEditText.setText("");
	}

	@Click(R.id.preImgBtn)
	void onPreGroup() {
		currentPage = currentPage - 1;
		currentPage = currentPage < 0 ? 0 : currentPage;
		tagsPager.setCurrentItem(currentPage);
	}

	@Click(R.id.nextImgBtn)
	void onNextGroup() {
		currentPage = currentPage + 1;
		currentPage = currentPage == totalPages ? totalPages - 1 : currentPage;
		tagsPager.setCurrentItem(currentPage);
	}

	/**
	 * 搜一搜，搜索职位
	 */
	@Click(R.id.searchBtn)
	void onSearch() {
//		ActivityDispatcher.toDemoActivity(getActivity());
		search();
	}

	// /切换标签分类
	@Click(R.id.changyonBtn)
	void onChangyonBtn() {
		
		reset(0);
	}

	// /切换标签分类
	@Click(R.id.yingjieBtn)
	void onYingjieBtn(View v) {
		
		reset(1);
	}

	// /切换标签分类
	@Click(R.id.renliBtn)
	void onRenliBtn(View v) {
		reset(2);
	}
	
	@Click(R.id.advancedSearchBtn)
	void OnAdvancedSearchBtn(){
		AdvancedSearchActivity_.intent(getActivity()).start();
	}
	
	
	/**
	 * 实现标签选择接口。设置标签搜索条件
	 */
	@Override
	public void onTagSelected(JobSearchTagDTO tagDto) {
		searchConditionDTO.setTagCode(tagDto.getTagCode());
		if(!StringUtils.isBlank(tagDto.getPropertyType())){
			searchConditionDTO.setPropertyCode(tagDto.getPropertyType());
		}
		search();
	}

	

	/* ============ 服务回调事件处理 =============== */

	@UiThread
	@Override
	public void onRequestStart(String reqId) {
		
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		// 标签分类
		if (reqId.equals(kREQ_ID_FINDJOBTAGTYPEDTO)) {
			// System.out.println("***********RESULT:"+result);
			if (result != null) {
				jobTagTypes = (List<JobTagTypeDTO>) result;
				showTagSwitchBtns();

				// 完成标签分类加载后，默认加载第一个标签分类的标签数据
				selectedTagType = jobTagTypes.get(0).getTagTypeCode();
				loadTags(selectedTagType);
			}
		}

		// 分页查询标签
		if (reqId.equals(kreq_id_findJobSearchTagDTOByPage)) {
			if (result != null) {
				Page<JobSearchTagDTO> page = (Page<JobSearchTagDTO>) result;
				// 获得标签类别的总页数
				totalPages = page.getMaxpage();
				maxPages.put(selectedTagType, totalPages);
				if (page.getElements() != null && page.getElements().size() > 0) {
					List<JobSearchTagDTO> tags_ = tags.get(selectedTagType);
					if (tags_ != null) {
						tags_.addAll(page.getElements());
					} else {
						tags.put(selectedTagType, page.getElements());
					}
					//为当前标签分类显示Fragments 分页
					showFragmentsForTags();
					//显示当前类别的标签
					showTags();
				}
			}
			
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		if(reqId.equals(kreq_id_findJobSearchTagDTOByPage)){
			loadingImg.clearAnimation();
			loadingImg.setVisibility(View.GONE);
			
		}
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	/**
	 * 搜索面板中的标签组适配器
	 * 
	 * @author yangjiannan
	 * 
	 */
	class TagsPageAdapter extends FragmentStatePagerAdapter {

	    ArrayList<FragmentTagsGroup_> fragments;


	    public TagsPageAdapter(android.support.v4.app.Fragment fragment, ArrayList<FragmentTagsGroup_> fragments) {
		super(getChildFragmentManager());
		this.fragments = fragments;
	    }

	    @Override
	    public android.support.v4.app.Fragment getItem(int position) {

		return fragments.get(position);
	    }

	    @Override
	    public int getCount() {
		return fragments.size();
	    }


	    @Override
	    public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	    }

	    @Override
	    public Object instantiateItem(View container, int position) {
		return fragments.get(position);
	    }

	    public void setResetData(ArrayList<FragmentTagsGroup_> fragments) {
		this.fragments = fragments;
		notifyDataSetChanged();
	    }

	}

	/**
	 * 搜索历史列表适配器
	 * 
	 * @author yangjiannan
	 * 
	 */
	class SearchHistoryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return historys.size();
		}

		@Override
		public Object getItem(int p) {
			return historys.get(p);
		}

		@Override
		public long getItemId(int p) {
			return p;
		}

		@Override
		public View getView(int p, View contentView, ViewGroup arg2) {
			TextView historyNameText = null;
			if (contentView == null) {
				contentView = LayoutInflater.from(getActivity())
						.inflate(R.layout.simple_listitem, null);
				historyNameText = (TextView) contentView
						.findViewById(R.id.itemNameText);
				contentView.setTag(historyNameText);
			} else {
				historyNameText = (TextView) contentView.getTag();
			}
			historyNameText.setText(historys.get(p));
			return contentView;
		}

	}
	
}
