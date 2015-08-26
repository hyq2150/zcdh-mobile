package com.zcdh.mobile.app.activities.ent;

import com.baidu.mapapi.model.LatLng;
import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcNearByService;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.app.activities.nearby.SearchResultAdapter;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 根据企业搜索职位
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_search_by_ent)
public class SearchPostsByEntActivity extends BaseActivity implements
		IXListViewListener, OnItemClickListener, RequestListener, YuyinInputListner {

	private static final String TAG = SearchPostsByEntActivity.class.getSimpleName();

	String kREQ_ID_findSearchPostDTOByEntName;

	IRpcNearByService nearByService;

	/**
	 * 搜索关键字
	 */
	@ViewById(R.id.keywordEditText)
	EditText keywordEditText;
	
	@ViewById(R.id.clearBtn)
	ImageButton clearBtn;
	/**
	 *  显示结果数量
	 */
	@ViewById(R.id.postCountText)
	TextView postCountText;

	/**
	 * 搜索结果列表
	 */
	@ViewById(R.id.searchResultListView)
	XListView searchResultListView;

	SearchResultAdapter searchResultAdapter;
	
	/**
	 *  语音
	 */
	YuYinInputView yuyinInputView;

	private int currentPage;

	private boolean hasNextPage;

	ArrayList<JobEntPostDTO> searchResults = new ArrayList<JobEntPostDTO>();
	
	String keyword;
	
	ProcessDialog processDialog;
	
	@ViewById(R.id.emptyView)
	EmptyTipView emptyView;

	@AfterViews
	void bindView() {

		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), "企业搜索");

		nearByService = RemoteServiceManager
				.getRemoteService(IRpcNearByService.class);

		searchResultAdapter = new SearchResultAdapter(this);
		
		searchResultListView.setPullRefreshEnable(false);
		searchResultListView.setPullLoadEnable(true);
		searchResultListView.setAutoLoadEnable(false);
		searchResultListView.setXListViewListener(this);
		searchResultListView.setOnItemClickListener(this);
		searchResultListView.setAdapter(searchResultAdapter);
		
		processDialog = new ProcessDialog(this);
		
		keywordEditText.setHint("输入企业名称");
	
		emptyView.setVisibility(View.GONE);
	
	}

	@Click(R.id.searchBtn)
	void onSearch() {
		
		String keyword = keywordEditText.getText().toString();
		if(!keyword.equals(this.keyword)){
			
			this.keyword = keyword;
			currentPage = 0;
			searchResults.clear();
			searchResultAdapter.updateItems(searchResults);
			processDialog.show();
			searchPostByEntName(keyword);
		}
//		if (!TextUtils.isEmpty(keyword)) {
//		}
		
	}
	
	@TextChange(R.id.keywordEditText)
	void keywordChange(CharSequence text, TextView hello, int before, int start, int count){
		String keyword = hello.getText().toString();
		if(!StringUtils.isBlank(keyword)){
			clearBtn.setVisibility(View.VISIBLE);
		}
	}
	

	@Click(R.id.clearBtn)
	void onClearBtn(){
		keywordEditText.setText("");
		this.keyword = "";
		currentPage = 0;
		searchResults.clear(); 
		postCountText.setText("");
		hasNextPage = false;
		searchResultAdapter.updateItems(searchResults);
		searchResultListView.setPullLoadEnable(false);
		clearBtn.setVisibility(View.GONE);
	}
	
	
	/**
	 *  语音
	 */
	@Click(R.id.micBtn)
	void onMic(){
		if(yuyinInputView==null){
			yuyinInputView = new YuYinInputView(this, this);
		}
		yuyinInputView.showAtParent(findViewById(R.id.body));
	}

	/**
	 * 更具企业名称搜索相关职位
	 * 
	 * @param keyword
	 */
	@Background
	void searchPostByEntName(String keyword) {
		LatLng location = ZcdhApplication.getInstance().getMyLocation();
		nearByService.findSearchPostDTOByEntName(getUserId(), keyword,
				location.longitude, location.latitude, currentPage,
				Constants.Page_SIZE).identify(
				kREQ_ID_findSearchPostDTOByEntName=RequestChannel.getChannelUniqueID(), this);
		currentPage++;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		long postId = searchResults.get(position - 1).getPostId();
		DetailsFrameActivity_.intent(this).postId(postId)
				.switchable(true).currentIndex(position - 1)
				.entId(searchResults.get(position - 1).getEntId())
				.posts(searchResults).start();
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		if (hasNextPage) {
			searchPostByEntName(keyword);
		}
	}
	@UiThread
	void onComplete(boolean hasNextPage) {
		Log.i(TAG, "加载更多 Oncomplete");
		
		searchResultListView.stopLoadMore();
		searchResultListView.setPullLoadEnable(hasNextPage);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findSearchPostDTOByEntName)) {
			Page<JobEntPostDTO> page = null;
			if (result != null) {
				page = (Page<JobEntPostDTO>) result;
				hasNextPage = page.hasNextPage();
				searchResults.addAll(page.getElements());
				searchResultAdapter.updateItems(searchResults);
			}
			if(page!=null && page.getTotalRows()>0){
				postCountText.setText("企业相关职位 "+page.getTotalRows()+" 个岗位");
				searchResultListView.setVisibility(View.VISIBLE);
				emptyView.isEmpty(false);
				emptyView.setVisibility(View.GONE);
			}else{
				postCountText.setText("");
				searchResultListView.setVisibility(View.GONE);
				emptyView.isEmpty(true);
				emptyView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete(hasNextPage);
		processDialog.dismiss();
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	/**
	 * 语音输入返回的结果
	 */
	@Override
	public void onComplete(String content) {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(content) 
				&& !content.equals(keyword)){
			keywordEditText.setText(content);
			this.keyword = content;
			processDialog.show();
			searchPostByEntName(keyword);
		}
	}

}
