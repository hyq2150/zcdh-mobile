package com.zcdh.mobile.app.activities.search;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobObjectivePostDTO;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.biz.entities.ZcdhCategoryPost;
import com.zcdh.mobile.biz.entities.ZcdhPost;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import net.tsz.afinal.FinalDb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

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
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 职位类别
 * @author yangjiannan
 *
 */
@EActivity(R.layout.activity_category_post)
public class CategoryPostActivity extends BaseActivity implements OnItemClickListener, 
RemoveItemListner, YuyinInputListner {

	YuYinInputView yuyinInputView;
	
	/**
	 * 显示搜索的职位
	 */
	@ViewById(R.id.postsListView)
	ListView postsListView;
	
	PostChosenAdapter postsAdapter;
	
	/**
	 * 显示所有职位类别
	 */
	@ViewById(R.id.categoryListView)
	StickyListHeadersListView categoryListView;
	
	StickyListHeadersAdapter categoryAdapter;
	
	//职位类别分类
	List<ZcdhCategoryPost> categorys = new ArrayList<ZcdhCategoryPost>();
	//职位类别
	List<ZcdhCategoryPost> categorysPost = new ArrayList<ZcdhCategoryPost>();
	/**
	 *  搜索的职位
	 */
	List<ZcdhPost> posts =  new ArrayList<ZcdhPost>();
	
	String[] sections ;
	int[] sectionIndices;
	
	/**
	 * 多选面板
	 */
	MultSelectionPannel multSelectionPannel;
	
	/* 搜索相关 */
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
	 * 标识是否单选
	 */
	@Extra
	boolean single = true;
	
	/**
	 * 已选择的职位
	 */
	@Extra
	HashMap<String ,JobObjectivePostDTO> selectedPosts = new HashMap<String, JobObjectivePostDTO>();
	
	FinalDb finalDb;

	
	public void onCreate(Bundle b){
		super.onCreate(b);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!single){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.action_ok, menu);
		}
	    return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//多选确定
	    if(item.getItemId()==R.id.action_OK){
	    	if(selectedPosts.size()<0){
	    		Toast.makeText(this, "请选择意向职位", Toast.LENGTH_SHORT).show();
	    		return false;
	    	}
	    	Intent data = new Intent();
	    	data.putExtra(PostsActivity.kDATA_POST_BUNLDE, selectedPosts);
	    	setResult(RESULT_OK, data);
	    }
	    finish();
	    return true;
	}
	
	
	@AfterViews
	void bindViews(){
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.title_category_posts));
		categoryListView.setOnItemClickListener(this);
		postsAdapter = new PostChosenAdapter(this, posts);
		postsAdapter.setHightlinght(true);
		postsListView.setAdapter(postsAdapter);
		if(!single){
			multSelectionPannel = new MultSelectionPannel(this, this);
		}
		
		loadData();
	}
	
	@Background
	void loadData(){
		if(finalDb==null){
			finalDb = DbUtil.create(this);
		}
		
		categorys = finalDb.findAllByWhere(ZcdhCategoryPost.class, "parent_code=-1 and is_delete==1");
		categorysPost =finalDb.findAllByWhere(ZcdhCategoryPost.class, "parent_code<>-1 and is_delete==1");
			
		sections = new String[categorys.size()];
		sectionIndices = new int[categorys.size()];
		int i =0;
		String last = null; 
		for (int j=0 ;j<categorysPost.size();j++) {
			
			ZcdhCategoryPost p = categorysPost.get(j);
			
			String parent_code = p.getParent_code();
			
			if(!StringUtils.isBlank(parent_code) 
					&& !parent_code.equals(last)){
				
				sections[i] = parent_code;
				sectionIndices[i] = j;
				
				i++;
			}
			last = parent_code; 
		}
	
		showData();
	}

	
	
	@UiThread
	void showData(){
		if(categoryAdapter==null){
			categoryAdapter = new CateogryStickAdapter();
			categoryListView.setAdapter(categoryAdapter);
		}
		showSelectItems();
	}
	
	/**
	 * 显示已选择的项
	 */
	void showSelectItems(){
		HashMap<String, String> _items = new HashMap<String, String>();
		for (String key : selectedPosts.keySet()) {
			_items.put(key, selectedPosts.get(key).getName());
		}
		if(_items.size()>0){
			multSelectionPannel.refresh(_items);
		}
	}
	
	/**
	 * 搜索
	 */
	void search(){
		posts = finalDb.findAllByWhere(ZcdhPost.class, "post_name like '%"+keyword+"%'");
		if(posts!=null && posts.size()>0){
			resultCountText.setText("搜索结果有 " +posts.size() +"个");
		}else{
			resultCountText.setText("没有搜索到结果");
		}
		postsAdapter.updateItems(posts);
		resultPannel.setVisibility(View.VISIBLE);
		categoryListView.setVisibility(View.GONE);
	}
	
	
	@OnActivityResult(PostsActivity.kREQUEST_POST)
	void onResultPost(int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			if(single){//如果是单选
				setResult(RESULT_OK, data);
				finish();
			}else{ //如果是多选
				HashMap<String, JobObjectivePostDTO> posts_bunlde = 
						(HashMap<String, JobObjectivePostDTO>) data.getExtras().getSerializable(PostsActivity.kDATA_POST_BUNLDE);

				//将选择的职位加入到选择面板中显示
				selectedPosts.clear();
				for (String key : posts_bunlde.keySet()) {
					if (!selectedPosts.containsKey(key)) {
						selectedPosts.put(key, posts_bunlde.get(key));
					}
				}
			}
			
			showSelectItems();
		}
	}

	@TextChange(R.id.keywordEditText)
	void keywordChange(CharSequence text, TextView hello, int before, int start, int count){
		keyword = keywordEditText.getText().toString();
		if(!StringUtils.isBlank(keyword)){
			clearBtn.setVisibility(View.VISIBLE);
			search();
		}else{
			clearBtn.setVisibility(View.GONE);
			posts.clear();
			postsAdapter.updateItems(posts);
			resultPannel.setVisibility(View.GONE);
			categoryListView.setVisibility(View.VISIBLE);
			
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
	@ItemClick(R.id.postsListView)
	void onSearchItemClick(int position){
//		ZcdhPost param = posts.get(position);
//		String code = param.getPost_code();
//		String name = param.getPost_name();
//		Intent data = new Intent();
//		data.putExtra(PostsActivity.kDATA_CODE, code);
//		data.putExtra(PostsActivity.kDATA_NAME, name);
//		setResult(RESULT_OK, data);
//		finish();
		
		ZcdhPost param = posts.get(position);
		String code = param.getPost_code();
		String name = param.getPost_name();
		
		
		if(single){ //如果是单选， 没选中一个，关闭当前Activity
			Intent data = new Intent();
			data.putExtra(PostsActivity.kDATA_CODE, code);
			data.putExtra(PostsActivity.kDATA_NAME, name);
			setResult(RESULT_OK, data);
			finish();
		}else{ // 如果是多选
			if(selectedPosts.containsKey(code)){
				selectedPosts.remove(code);
			}else{
				if(MultSelectionPannel.max>selectedPosts.size()){
					JobObjectivePostDTO objPost = new JobObjectivePostDTO();
					objPost.setCode(code);
					objPost.setName(name);
					selectedPosts.put(code, objPost);
				}else{
					Toast.makeText(this, "最多选择" + MultSelectionPannel.max + "个职位", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			showSelectItems();
			postsAdapter.updateSelected(selectedPosts);
		}
		
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		String categoryCode = categorysPost.get(position).getCode();
		PostsActivity_.intent(this)
		.categoryCode(categoryCode)		// 职位类别
		.signle(single)					// 是否单选
		.selectedPosts(selectedPosts) 	//已选职位
		.startForResult(PostsActivity.kREQUEST_POST);

		//如果是单选,将当前activity finish
	}
	
	/**
	 * 选移除一个选项 
	 */
	@Override
	public void onRemoveItem(Object key) {
		selectedPosts.remove(key);
	}
	
	class CateogryStickAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer{

		@Override
		public int getCount() {
			return categorysPost.size();
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
						.inflate(R.layout.simple_listview_item_accessory, parent, false);
				holder.text = (TextView) convertView
						.findViewById(R.id.itemNameText);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ZcdhCategoryPost post = categorysPost.get(position);
			
			holder.text.setText(post.getName());
			// // 设置选中颜色
//			if (position == areaSelected) {
//				convertView.setBackgroundResource(R.drawable.selected_area);
//			} else {
//				convertView.setBackgroundResource(R.drawable.area_selector);
//			}

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
			String parent_code = categorysPost.get(position).getParent_code();
			List<ZcdhCategoryPost> parents = finalDb.findAllByWhere(ZcdhCategoryPost.class, String.format("code='%s'", parent_code));
			if(parents!=null && parents.size()>0){
				holder.text.setText(parents.get(0).getName());
			}

			return convertView;
		}

		@Override
		public long getHeaderId(int position) {
			String parent_code = categorysPost.get(position).getParent_code();
		
			return Long.valueOf(parent_code);
		}
		
		
		class HeaderViewHolder {
			TextView text;
		}

		class ViewHolder {
			TextView text;
		}
	}

	@Override
	public void onComplete(String content) {
		this.keywordEditText.setText(content);
	}

	
}
