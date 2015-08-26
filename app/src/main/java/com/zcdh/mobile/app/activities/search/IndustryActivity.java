package com.zcdh.mobile.app.activities.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobObjectiveAreaDTO;
import com.zcdh.mobile.api.model.JobObjectiveIndustryDTO;
import com.zcdh.mobile.api.model.JobObjectivePostDTO;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.biz.entities.ZcdhCategoryPost;
import com.zcdh.mobile.biz.entities.ZcdhIndustry;
import com.zcdh.mobile.biz.entities.ZcdhPost;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 行业类别
 * 
 * @author yangjiannan 1) 支持多选和单选， 传递参数
 */
@EActivity(R.layout.activity_industry)
public class IndustryActivity extends BaseActivity implements
		OnItemClickListener, RemoveItemListner , YuyinInputListner {

		

	/**
	 * 
	 * 注册接收结果时的 key （进一步了解详细使用，请见: com.zcdh.mobile.framework.events.MyEvents 类）
	 */
	public static final String kMSG_INDUSTRY_SELECTED = "industry_selected";

	public static final int kREQUEST_INDUSTRY = 2001;
	public static final String kDATA_CODE = "industry_code";
	public static final String kDATA_NAME = "industry_name";

	public static final String kDATA_INDUSTRY_BUNDLE = "industry_bundle";

	
	YuYinInputView yuyinInputView;
	/**
	 * 行业列表
	 */
	@ViewById(R.id.industryListView)
	StickyListHeadersListView industryListView;

	/* 搜索相关 */
	@ViewById(R.id.searchIndustryListView)
	ListView searchIndustryListView;
	
	IndustryStickAdapter industrySearchAdapter;
	
	@ViewById(R.id.resultPannel)
	View resultPannel;
	
	@ViewById(R.id.resultCountText)
	TextView resultCountText;
	
	
	/**
	 * 关键字输入框
	 */
	@ViewById(R.id.keywordEditText)
	EditText keywordEditText;
	
	@ViewById(R.id.clearBtn)
	ImageButton clearBtn;
	
	/**
	 * 保存的关键字
	 */
	String keyword;
	/**
	 * 列表区段
	 */
	protected String[] sections;

	/**
	 * 区段标识
	 */
	protected int[] sectionIndices;

	/**
	 * 行业列表适配器
	 */
	protected StickyListHeadersAdapter industryAdapter;

	/**
	 * 行业类别分类
	 */
	protected List<ZcdhIndustry> industriesParent = new ArrayList<ZcdhIndustry>();

	/**
	 * 行业
	 */
	protected List<ZcdhIndustry> industries = new ArrayList<ZcdhIndustry>();

	/**
	 * 搜索结果
	 */
	protected List<ZcdhIndustry> industriesSearchResult = new ArrayList<ZcdhIndustry>();
	
	

	/**
	 * 选择的行业
	 */
	@Extra
	HashMap<String, JobObjectiveIndustryDTO> selectedIndustries 
					= new HashMap<String, JobObjectiveIndustryDTO>();

	/**
	 * 标识是否单选，false 为多选
	 */
	@Extra
	boolean signle = true;
	
	/**
	 * 选择显示面板
	 */
	protected MultSelectionPannel multSelectionPannel;

	/**
	 * 本地数据库
	 */
	protected FinalDb finalDb;

	public void onCreate(Bundle b) {
		super.onCreate(b);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!signle) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.action_ok, menu);
		}
		return true;
	}

	/**
	 * 更具View 的id 绑定到变量，初始化一些UI
	 */
	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), getString(R.string.title_industry));
		if (!signle) {
			multSelectionPannel = new MultSelectionPannel(this, this);
		}
		industryListView.setOnItemClickListener(this);
		
		industrySearchAdapter = new IndustryStickAdapter(true);
		searchIndustryListView.setAdapter(industrySearchAdapter);

		// ///初始化
		// 2. 加载数据
		loadData();

		// 3. 列表开始显示数据
		showData();
	}

	/**
	 * 加载本地数据 
	 */
	void loadData() {
		if (finalDb == null) {
			finalDb = DbUtil.create(this);
		}
		industriesParent = finalDb.findAllByWhere(ZcdhIndustry.class,
				"parent_code=-1 and is_delete=1", "parent_code");
		industries = finalDb.findAllByWhere(ZcdhIndustry.class,
				"parent_code<>-1 and is_delete=1", "parent_code");

		sections = new String[industriesParent.size()];
		sectionIndices = new int[industriesParent.size()];
		int i = 0;
		String last = null;
		for (int j = 0; j < industries.size(); j++) {

			ZcdhIndustry p = industries.get(j);

			String parent_code = p.getParent_code();

			if (!StringUtils.isBlank(parent_code) && !parent_code.equals(last)) {

				sections[i] = parent_code;
				sectionIndices[i] = j;

				i++;
			}
			last = parent_code;
		}

	}

	/**
	 * 在列表显示数据
	 */
	void showData() {
		if (industryAdapter == null) {
			industryAdapter = new IndustryStickAdapter(false);
			industryListView.setAdapter(industryAdapter);
		}
		if(!signle){
			showSelectItems();
		}
	}
	
	/**
	 * 显示已选择的项
	 */
	void showSelectItems(){
		HashMap<String, String> _items = new HashMap<String, String>();
		for (String key : selectedIndustries.keySet()) {
			_items.put(key, selectedIndustries.get(key).getName());
		}
		if(_items.size()>0){
			multSelectionPannel.refresh(_items);
		}
	}
	
	/**
	 * 搜索
	 */
	void search(){
		industriesSearchResult = finalDb.findAllByWhere(ZcdhIndustry.class, "name like '%"+keyword+"%' and parent_code<>-1");
		if(industriesSearchResult!=null && industriesSearchResult.size()>0){
			resultCountText.setText("搜索结果有 " +industriesSearchResult.size() +"个");
		}else{
			resultCountText.setText("没有搜索到结果");
		}
		industrySearchAdapter.notifyDataSetChanged();
		resultPannel.setVisibility(View.VISIBLE);
		industryListView.setVisibility(View.GONE);
	}
	
	

	@TextChange(R.id.keywordEditText)
	void keywordChange(CharSequence text, TextView hello, int before, int start, int count){
		keyword = keywordEditText.getText().toString();
		if(!StringUtils.isBlank(keyword)){
			clearBtn.setVisibility(View.VISIBLE);
			search();
		}else{
			clearBtn.setVisibility(View.GONE);
			industriesSearchResult.clear();
			industrySearchAdapter.notifyDataSetChanged();
			resultPannel.setVisibility(View.GONE);
			industryListView.setVisibility(View.VISIBLE);
			
		}
	}
	
	@Click(R.id.clearBtn)
	void onClearBtn(){
		keywordEditText.setText("");
	}
	

	@Click(R.id.micBtn)
	void onMicBtn(){
		if(yuyinInputView==null){
			yuyinInputView = new YuYinInputView(this, this);
		}
		yuyinInputView.showAtParent(findViewById(R.id.body));
	}
	
	/**
	 *  选中一个搜索结果
	 * @param position
	 */
	@ItemClick(R.id.searchIndustryListView)
	void onSearchItemClick(int position){
//		ZcdhIndustry param = industriesSearchResult.get(position);
//		String code = param.getCode();
//		String name = param.getName();
//		Intent data = new Intent();
//		data.putExtra(IndustryActivity.kDATA_CODE, code);
//		data.putExtra(IndustryActivity.kDATA_NAME, name);
//		setResult(RESULT_OK, data);
//		finish();
		// 获取到选中的行业
				ZcdhIndustry param = industriesSearchResult.get(position);
				String code = param.getCode();
				String name = param.getName();

				if (signle) { // 如果是单选， 没选中一个，关闭当前Activity
					Bundle bundle = new Bundle();
					bundle.putSerializable(kMSG_INDUSTRY_SELECTED, param);
					Intent data = new Intent();
					data.putExtra(kDATA_CODE, code);
					data.putExtra(kDATA_NAME, name);

					setResult(RESULT_OK, data);
					finish();
				} else { // 如果是多选
					if(selectedIndustries.containsKey(code)){
						selectedIndustries.remove(code);
					}else{
						if (MultSelectionPannel.max > selectedIndustries.size()) {
							JobObjectiveIndustryDTO objInd = new JobObjectiveIndustryDTO();
							objInd.setCode(code);
							objInd.setName(name);
							selectedIndustries.put(code, objInd);
						}else{
							Toast.makeText(this, "最多选择" + MultSelectionPannel.max + "个行业", Toast.LENGTH_SHORT).show();
							return ;
						}
					}
					showSelectItems();
					industrySearchAdapter.notifyDataSetChanged();
				}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// 多选确定
		if (item.getItemId() == R.id.action_OK) {
			// TODO
			Intent data = new Intent();
			data.putExtra(kDATA_INDUSTRY_BUNDLE, selectedIndustries);
			setResult(RESULT_OK, data);
		}
		finish();
		return true;
	}

	/**
	 * 选中行业事件处理，分两种情况 1. 多选的时候 2. 单选的时候
	 */

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// 获取到选中的行业
		ZcdhIndustry param = industries.get(position);
		String code = param.getCode();
		String name = param.getName();

		if (signle) { // 如果是单选， 没选中一个，关闭当前Activity
			Bundle bundle = new Bundle();
			bundle.putSerializable(kMSG_INDUSTRY_SELECTED, param);
			Intent data = new Intent();
			data.putExtra(kDATA_CODE, code);
			data.putExtra(kDATA_NAME, name);

			setResult(RESULT_OK, data);
			finish();
		} else { // 如果是多选
			if(selectedIndustries.containsKey(code)){
				selectedIndustries.remove(code);
			}else{
				if (MultSelectionPannel.max > selectedIndustries.size()) {
					JobObjectiveIndustryDTO objInd = new JobObjectiveIndustryDTO();
					objInd.setCode(code);
					objInd.setName(name);
					selectedIndustries.put(code, objInd);
				}else{
					Toast.makeText(this, "最多选择" + MultSelectionPannel.max + "个行业", Toast.LENGTH_SHORT).show();
					return ;
				}
			}
			showSelectItems();
			industryAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 多选面板删除一个选项
	 */
	public void onRemoveItem(Object key) {
		selectedIndustries.remove(key);
	}
	
	@Override
	public void onComplete(String content) {
		this.keywordEditText.setText(content);
	}


	class IndustryStickAdapter extends BaseAdapter implements
			StickyListHeadersAdapter, SectionIndexer {
		
		boolean seach= false;
		public IndustryStickAdapter(boolean search){
			this.seach = search;
		}

		@Override
		public int getCount() {
			if(this.seach){
				return industriesSearchResult.size();
			}else{
				return industries.size();
			}
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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.simple_listview_item_checker,
								parent, false);
				holder.text = (TextView) convertView
						.findViewById(R.id.itemNameText);
				holder.checkerImg = (ImageView) convertView.findViewById(R.id.checkerImg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ZcdhIndustry ind ;

			if(this.seach){
				ind = industriesSearchResult.get(position);
				holder.text.setTextColor(getResources().getColor(R.color.result_hightlight));
			}else{
				ind= industries.get(position);
				holder.text.setTextColor(getResources().getColor(R.color.font_color));
			}
			holder.text.setText(ind.getName());

			String code = ind.getCode();
			if(selectedIndustries.containsKey(code)){
				holder.checkerImg.setVisibility(View.VISIBLE);
			}else{
				holder.checkerImg.setVisibility(View.GONE);
			}
			
			return convertView;
		}

		@Override
		public Object[] getSections() {
			return sections;
		}

		@Override
		public int getPositionForSection(int section) {
			if (section >= sectionIndices.length) {
				section = sectionIndices.length - 1;
			} else if (section < 0) {
				section = 0;
			}
			return sectionIndices[section];
		}

		@Override
		public int getSectionForPosition(int position) {
			for (int i = 0; i < sectionIndices.length; i++) {
				if (position < sectionIndices[i]) {
					return i - 1;
				}
			}
			return sectionIndices.length - 1;
		}

		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			HeaderViewHolder holder;

			if (convertView == null) {
				holder = new HeaderViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.section_header, parent, false);
				holder.text = (TextView) convertView
						.findViewById(R.id.headText);
				convertView.setTag(holder);
			} else {
				holder = (HeaderViewHolder) convertView.getTag();
			}

			// set header text as first char in name
			// CharSequence headerChar = mCountries[position].subSequence(0, 1);
			String parent_code = industries.get(position).getParent_code();
			List<ZcdhIndustry> parents = finalDb.findAllByWhere(
					ZcdhIndustry.class,
					String.format("code='%s'", parent_code));
			if (parents != null && parents.size() > 0) {
				holder.text.setText(parents.get(0).getName());
			}

			return convertView;
		}

		@Override
		public long getHeaderId(int position) {
			String parent_code = industries.get(position).getParent_code();

			return Long.valueOf(parent_code);
		}

		class HeaderViewHolder {
			TextView text;
		}

		class ViewHolder {
			TextView text;
			ImageView checkerImg;
		}
	}

}
