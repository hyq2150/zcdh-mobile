/**
 * 
 * @author jeason, 2014-5-15 下午4:42:07
 */
package com.zcdh.mobile.app.activities.personal;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobUserBlackListDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.ent.MainEntActivity_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-5-15 下午4:42:07
 * 企业黑名单
 */
@EActivity(R.layout.enterprise_blacklist)
public class EnterpriseBlackList extends BaseActivity implements RequestListener, 
		OnRefreshListener2<ListView>, OnItemClickListener, DataLoadInterface {
	@ViewById(R.id.listview)
	PullToRefreshListView blackList;

	IRpcJobUservice userService;

	public String kREQ_ID_FINDUSERBLACKLISTDTOBYUSERID;

	public String kREQ_ID_REMOVEFROMLIST;

	List<JobUserBlackListDTO> entItems;
	EnterprisesAdapter adapter;
	EmptyTipView empty_view;
	boolean delete_mode = false;
	boolean hasData = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		entItems = new ArrayList<JobUserBlackListDTO>();
		empty_view = new EmptyTipView(this);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "企业黑名单");
		userService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
	}

	@AfterViews
	void initViews() {
		blackList.setMode(Mode.DISABLED);
		//blackList.setDividerDrawable(getResources().getDrawable(R.drawable.divider_horizontal_timeline));
		adapter = new EnterprisesAdapter();
		blackList.setOnRefreshListener(this);
		blackList.setAdapter(adapter);
		blackList.setEmptyView(empty_view);
		blackList.getRefreshableView().setDivider(null);
		blackList.getRefreshableView().setDividerHeight(5);
		blackList.getRefreshableView().setOnItemClickListener(this);
		loadData();
	}

	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
	}

	/**
	 * 
	 * @author jeason, 2014-5-15 下午5:44:07
	 */
	@Background
	public void loadData() {
		userService.findUserBlackListDTOByUserId(getUserId()).identify(kREQ_ID_FINDUSERBLACKLISTDTOBYUSERID = RequestChannel.getChannelUniqueID(), this);
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

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_FINDUSERBLACKLISTDTOBYUSERID)) {
			if (result != null) {
				entItems = (List<JobUserBlackListDTO>) result;
				adapter.notifyDataSetChanged();
			}
		}

		if (reqId.equals(kREQ_ID_REMOVEFROMLIST)) {
			Toast.makeText(this, "移除成功", Toast.LENGTH_SHORT).show();
		}

		if (!entItems.isEmpty()) {
			hasData = true;
			supportInvalidateOptionsMenu();
			empty_view.isEmpty(false);
		}else{
			empty_view.isEmpty(true);
		}

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
		empty_view.showException((ZcdhException)error, this);
	}

	private class EnterprisesAdapter extends BaseAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return entItems.size();
		}

		@Override
		public JobUserBlackListDTO getItem(int position) {
			return entItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return getItem(position).getEntId();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.simple_listview_item_accessory, null);
			TextView school = (TextView) convertView.findViewById(R.id.itemNameText);
			school.setText(getItem(position).getEntName());
			ImageView ivDelete = (ImageView) convertView.findViewById(R.id.btn_delete);
			if (delete_mode) {
				ivDelete.setVisibility(View.VISIBLE);
				ivDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						removeFromList(getItem(position));
						removeItem(position);
					}

				});
			} else {
				ivDelete.setVisibility(View.GONE);
			}
			return convertView;
		}

		public void removeItem(int position) {
			getItems().remove(position);
			notifyDataSetChanged();
			if(getCount()==0){
				supportInvalidateOptionsMenu();
			}
		}

		/**
		 * @return
		 * @author jeason, 2014-7-8 下午4:25:16
		 */
		public List<JobUserBlackListDTO> getItems() {
			// TODO Auto-generated method stub
			return entItems;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2
	 * #onPullDownToRefresh
	 * (com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2
	 * #onPullUpToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (hasData) {
			getMenuInflater().inflate(R.menu.action_edit, menu);
			MenuItem menu_item = menu.findItem(R.id.action_edit);
			if (delete_mode) {
				menu_item.setTitle(R.string.done);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@OptionsItem(R.id.action_edit)
	void setDeleteMode() {
		delete_mode = !delete_mode;
		supportInvalidateOptionsMenu();
		adapter.notifyDataSetChanged();
	}

	@Background
	public void removeFromList(JobUserBlackListDTO item) {
		userService.removeUserBlack(getUserId(), item.getEntId()).identify(kREQ_ID_REMOVEFROMLIST = RequestChannel.getChannelUniqueID(), this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MainEntActivity_.intent(this).entId(adapter.getItem(arg2 - 1).getEntId())
		.default_index(0)
		.start();
	}
}
