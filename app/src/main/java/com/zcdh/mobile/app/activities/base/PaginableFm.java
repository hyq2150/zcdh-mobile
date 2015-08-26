/**
 * 
 * @author jeason, 2014-6-19 下午8:19:16
 */
package com.zcdh.mobile.app.activities.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.mobile.R;
import com.zcdh.mobile.app.activities.job_fair.CategoryDropDownFilter;
import com.zcdh.mobile.app.activities.job_fair.CategoryDropDownFilter.onFilterListener;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;

/**
 * @author jeason, 2014-6-19 下午8:19:16 包含PulltorefreshListview的可分页的Fragment
 *         用法参考com.zcdh.mobile.app.activities.job_fair。PostListFragment 只需重写联网请求
 *         与点击事件
 */
public abstract class PaginableFm<T> extends BaseFragment implements
		RequestListener, OnItemClickListener, IXListViewListener {

	protected RelativeLayout searchBar;

	protected XListView listview;

	protected LinearLayout rlSearch;

	protected EmptyTipView emptyView;

	protected ViewsAdapter adapter;

	protected int currentPage = 1;

	protected String K_REQ_GETPAGE;

	protected Handler handler = new Handler(Looper.getMainLooper());

	public boolean is_searchable = false;

	protected EditText et_keyword;

	protected ImageButton btnSearch;

	protected String keyword;

	protected boolean hasNextPage;

	Object mCallbacker;

	ProcessDialog processDialog;

	protected RelativeLayout toggleFilter;
	protected ImageView toggleImg;
	protected TextView toggleLable;
	protected CategoryDropDownFilter dropdownFilterView;
	protected long jobfair_id;
	
	protected RelativeLayout categoryTipsView;
	protected TextView categorySelected;
	protected ImageButton clearBtn;
	
	protected String code;//选择的行业编码
	protected String name;//选择的行业
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("PaginableFm");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("PaginableFm");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		processDialog = new ProcessDialog(getActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_jobfar_posts, null);
		
		categoryTipsView = (RelativeLayout) v.findViewById(R.id.categoryTipsView);
		categorySelected = (TextView) v.findViewById(R.id.categorySelectedvalueText);
		clearBtn = (ImageButton) v.findViewById(R.id.clearBtn);
		clearBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PaginableFm.this.code = null;
				PaginableFm.this.name = null;
				categoryTipsView.setVisibility(View.GONE);
				currentPage=1;
				adapter.clear();
				getData(currentPage,
						K_REQ_GETPAGE = RequestChannel
								.getChannelUniqueID(), keyword, code, name);
			}
		});
		
		dropdownFilterView = (CategoryDropDownFilter) v
				.findViewById(R.id.filterView);
		dropdownFilterView.setOnFilterListener(new onFilterListener() {
			
			@Override
			public void onFilter(String code, String name) {
				
				PaginableFm.this.code = code;
				PaginableFm.this.name = name;
				if(!TextUtils.isEmpty(code)){
					categoryTipsView.setVisibility(View.VISIBLE);
					categorySelected.setText(name);
				}else{
					categoryTipsView.setVisibility(View.GONE);
				}
				currentPage=1;
				adapter.clear();
				displayFilterView();
				getData(currentPage,
						K_REQ_GETPAGE = RequestChannel
								.getChannelUniqueID(), keyword, code, name);
				//processDialog.show();
				
			}

			
		});
		toggleFilter = (RelativeLayout) v.findViewById(R.id.filterBtn);
		toggleImg = (ImageView) toggleFilter.findViewById(R.id.toggleIcon);
		toggleLable = (TextView) toggleFilter.findViewById(R.id.toggleLable);
		toggleFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				displayFilterView();

			}
		});
		emptyView = (EmptyTipView) v.findViewById(R.id.emptyView);
		listview = (XListView) v.findViewById(R.id.listview);
		rlSearch = (LinearLayout) v.findViewById(R.id.searchPannel);

		if (!is_searchable) {
			rlSearch.setVisibility(View.GONE);
		} else {
			rlSearch.setVisibility(View.VISIBLE);
			et_keyword = (EditText) v.findViewById(R.id.keywordEditText);
			btnSearch = (ImageButton) v.findViewById(R.id.searchBtn);
			et_keyword.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					keyword = s.toString();
					if (TextUtils.isEmpty(keyword)) {
						currentPage = 1;
						getData(currentPage,
								K_REQ_GETPAGE = RequestChannel
										.getChannelUniqueID(), keyword, code, name);
					}
				}
			});

		}

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(keyword)){
					currentPage = 1;
					processDialog.show();
					getData(currentPage,
							K_REQ_GETPAGE = RequestChannel.getChannelUniqueID(),
							keyword, code, name);
				}
			}
		});
		bindView();
		return v;
	}
	

	protected void displayFilterView() {
		if (View.GONE == dropdownFilterView.getVisibility()) {
			dropdownFilterView.setVisibility(View.VISIBLE);
			toggleFilter.setBackgroundColor(getResources().getColor(R.color.row_grey));
			toggleImg.setImageResource(R.drawable.arrow_b);
			toggleLable.setTextColor(getResources().getColor(R.color.blue));
			loadFilterData();
		} else {
			dropdownFilterView.setVisibility(View.GONE);
			toggleFilter.setBackgroundColor(getResources().getColor(R.color.blue));
			toggleImg.setImageResource(R.drawable.arrow_w);
			toggleLable.setTextColor(getResources().getColor(R.color.white));
		}		
	}

	public void setCallbacker(Object callbacker) {
		mCallbacker = callbacker;
	}

	void bindView() {

		emptyView.setBackgroundColor(getResources().getColor(R.color.white));

		listview.setEmptyView(emptyView);
		listview.setPullRefreshEnable(false);
		listview.setPullLoadEnable(true);
		listview.setAutoLoadEnable(false);
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);

		adapter = new ViewsAdapter();
		listview.setAdapter(adapter);

		getData(currentPage,
				K_REQ_GETPAGE = RequestChannel.getChannelUniqueID(), "", code, name);

	}

	public void getData(final int current_page, final String reqId, final String keyword, final String code, final String name) {
		if(dropdownFilterView.getVisibility()==View.VISIBLE)displayFilterView();
		
	}

	public abstract void onItemClickWithItem(View view, T item, int position);

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		onItemClickWithItem(arg1, adapter.getItem(position - 1), position);
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:07
	 */
	private void getLatest() {
		currentPage = 1;
		if (RegisterUtil.isRegisterUser(getActivity())) {
			getData(currentPage,
					K_REQ_GETPAGE = RequestChannel.getChannelUniqueID(),
					keyword, code, name);
		}
		onComplete();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:05
	 */
	private void getMore() {
		if (!hasNextPage) {
			onComplete();
			return;
		}
		// currentPage++;
		getData(currentPage,
				K_REQ_GETPAGE = RequestChannel.getChannelUniqueID(), keyword, code, name);

	}

	protected void onComplete() {
		/*
		 * handler.postAtTime(new Runnable() {
		 * 
		 * @Override public void run() { listview.stopLoadMore(); } }, 100);
		 */
		listview.stopLoadMore();
	}

	public abstract View getView(View convertView, T item);

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
		/*
		 * if (reqId.equals(K_REQ_GETPAGE)) { if (result != null) { Page<T> page
		 * = (Page<T>) result; if (page.getCurrentPage() == 1) {
		 * adapter.updateItems(page.getElements()); } else {
		 * adapter.addToBottom(page.getElements()); } hasNextPage =
		 * page.hasNextPage(); } emptyView.isEmpty(adapter.getCount()==0); }
		 */
	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
		if(!TextUtils.isEmpty(code)){
			categoryTipsView.setVisibility(View.VISIBLE);
			categorySelected.setText(name+"  ("+adapter.getCount()+ "个)");
		}else{
			categoryTipsView.setVisibility(View.GONE);
			categorySelected.setText("");
		}
		if (processDialog.isShowing())
			processDialog.dismiss();
		
	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	@Override
	public void onLoadMore() {
		getMore();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	public void loadFilterData(){}
	public void clear(){}

	public abstract void onPageSelected(int page);
	public boolean onBack(){
		boolean display = dropdownFilterView.getVisibility()==View.VISIBLE;
		if(display)displayFilterView();
		return display;
	}
	
	protected class ViewsAdapter extends BaseAdapter {
		private List<T> mInfos;
	
		public ViewsAdapter() {
			mInfos = new ArrayList<T>();
		}
	
		public List<T> getItems() {
			return mInfos;
		}
	
		@Override
		public int getCount() {
			return mInfos.size();
		}
	
		@Override
		public T getItem(int position) {
			return mInfos.get(position);
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return PaginableFm.this.getView(convertView, getItem(position));
		}
	
		/**
		 * @param elements
		 * @author jeason, 2014-6-12 下午2:45:04
		 */
		public void updateItems(List<T> elements) {
			mInfos.clear();
			mInfos.addAll(elements);
			notifyDataSetChanged();
		}
	
		/**
		 * @param elements
		 * @author jeason, 2014-6-12 下午2:45:06
		 */
		public void addToBottom(List<T> elements) {
			mInfos.addAll(elements);
			notifyDataSetChanged();
		}
	
		public void clear(){
			mInfos.clear();
			notifyDataSetChanged();
		}
	}
}
