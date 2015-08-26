package com.zcdh.mobile.app.activities.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.api.model.JobObjectivePostDTO;
import com.zcdh.mobile.biz.entities.ZcdhCategoryPost;
import com.zcdh.mobile.biz.entities.ZcdhPost;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 职位选择
 * @author yangjiannan
 *
 */
@EActivity(R.layout.activity_post_chosen)
public class PostsActivity extends BaseActivity implements RemoveItemListner
{
	
	public final static String kMSG_POST_SELECTED = "post_selected";

	public static final String kDATA_CODE = "post_code";
	public static final String kDATA_NAME = "post_name";

	public static final int kREQUEST_POST = 2002;

	public static final String kDATA_POST_BUNLDE = "posts_bunlde";

	
	FinalDb finalDb;
	
	/**
	 * 该类别下得职位
	 */
	List<ZcdhPost> posts ; 
	
	
	/**
	 * 所属职位类别编码
	 */
	@Extra
	String categoryCode;
	ZcdhCategoryPost category;
	
	/**
	 * 标识是否单选
	 */
	@Extra
	boolean signle = true;
	
	
	/**
	 * 已选意向职位职位
	 */
	@Extra
	HashMap<String, JobObjectivePostDTO> selectedPosts = new HashMap<String, JobObjectivePostDTO>();
	

	/**
	 * 选择显示面板
	 */
	protected MultSelectionPannel multSelectionPannel;
	public void onCreate(Bundle b){
		super.onCreate(b);
	}
	
	/**
	 * 基础数据职位列表
	 */
	@ViewById(R.id.postsListView)
	ListView postsListView;

	private PostChosenAdapter postsAdapter;
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!signle){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.action_ok, menu);
		}
	    return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//多选确定
	    if(item.getItemId()==R.id.action_OK){
	    	
	    	Intent data = new Intent();
	    	data.putExtra(kDATA_POST_BUNLDE, selectedPosts);
	    	setResult(RESULT_OK, data);
	    }
	    finish();
	    return true;
	}
	
	@AfterViews
	void bindViews(){

		if(!signle){
			multSelectionPannel =new MultSelectionPannel(this, this);
		}
		loadData();
	}
	
	@Background
	void loadData(){
		if(finalDb==null){
			finalDb = DbUtil.create(this);
		}
		if(!StringUtils.isBlank(categoryCode)){
			posts = finalDb.findAllByWhere(ZcdhPost.class, String.format("post_category_code='%s'", categoryCode));
			category = finalDb.findAllByWhere(ZcdhCategoryPost.class, String.format("code='%s'", categoryCode)).get(0);
		}
		
		showData();
	}
	
	@UiThread
	void showData(){
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), category.getName()); 
		if(postsAdapter==null){
			postsAdapter = new PostChosenAdapter(this, this.posts);
		}
		postsListView.setAdapter(postsAdapter);
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
	
	
	@ItemClick(R.id.postsListView)
	void onItemClick(int position){
		ZcdhPost param = posts.get(position);
		String code = param.getPost_code();
		String name = param.getPost_name();
		
		
		if(signle){ //如果是单选， 没选中一个，关闭当前Activity
			Intent data = new Intent();
			data.putExtra(kDATA_CODE, code);
			data.putExtra(kDATA_NAME, name);
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
	public void onRemoveItem(Object key) {
		selectedPosts.remove(key);
	}
	
	
	
	
}