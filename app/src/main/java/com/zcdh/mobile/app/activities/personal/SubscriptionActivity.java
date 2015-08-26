package com.zcdh.mobile.app.activities.personal;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobUserSubscriptionDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 订阅服务
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_subscription)
public class SubscriptionActivity extends BaseActivity implements RequestListener, OnClickListener, 
		OnRefreshListener2<ListView>, OnItemClickListener, DataLoadInterface{

	String kREQ_ID_findUserSubscriptionList;
	String kREQ_ID_removeUserSubscription;

	IRpcJobUservice jobUservice;

	/**
	 * 添加订阅按钮
	 */
	@ViewById(R.id.addSubscriptionRl)
	LinearLayout addSubscriptionRl;

	/**
	 * 显示所有订阅
	 */
	@ViewById(R.id.subScriptionListView)
	PullToRefreshListView subScriptionListView;

	SubscriptionAdapter subscriptionAdapter;

	// @ViewById(R.id.emptyView)
	// ScrollView emptyView;

	EmptyTipView emptyView;
	/**
	 * 所有订阅
	 */
	List<JobUserSubscriptionDTO> subscriptionList = new ArrayList<JobUserSubscriptionDTO>();

	/**
	 * 标识是否在编辑状态
	 */
	private boolean edit = true;
	private Integer currentPage = 1;
	private Integer pageSize = 20;
	Page<JobUserSubscriptionDTO> pages;
	/**
	 * 删除item ID
	 */
	private long delId;

	/**
	 * 添加订阅
	 */
	@Click(R.id.addSubscriptionRl)
	void onAddSubscription() {
		SubscriptionAddActivity_.intent(this).startForResult(SubscriptionAddActivity.kREQUEST_ADD);
	}

	@OnActivityResult(SubscriptionAddActivity.kREQUEST_ADD)
	void onActivityResult(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
	//		subScriptionListView.setRefreshing(true);
			currentPage = 1;
			pages = null;
			loadData();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		if (editable) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.action_edit, menu);
			MenuItem mi = menu.findItem(R.id.action_edit);

			if (edit) {
				mi.setTitle(R.string.edit);
			} else {
				mi.setTitle(R.string.cancel);
			}
		}
		return true;
	}

	/**
	 * 菜单右按钮
	 * 
	 * @param item
	 * 
	 */
	@OptionsItem(R.id.action_edit)
	void onMenuItem(MenuItem item) {
		if (!edit) {
			// updateWorkExp();
			this.edit = false;
			setMenuState();
		} else {
			setMenuState();
		}

	}

	/**
	 * 改变菜单按钮
	 * 
	 * @param edit
	 */
	void setMenuState() {

		edit = !edit;
		supportInvalidateOptionsMenu();
		subscriptionAdapter.notifyDataSetChanged();

	}

	@AfterViews
	void bindViews() {
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activity_title_subscription));
		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		
		emptyView = new EmptyTipView(this);
		subScriptionListView.setEmptyView(emptyView);
		subscriptionAdapter = new SubscriptionAdapter();
		subScriptionListView.setAdapter(subscriptionAdapter);
		subScriptionListView.setMode(Mode.PULL_FROM_END);
		subScriptionListView.setOnRefreshListener(this);
		subScriptionListView.setOnItemClickListener(this);

		if (getIntent().getExtras() != null) {
			boolean fromMessageCenter = (Boolean) getIntent().getExtras().get("fromMessageCenter");
			if (fromMessageCenter) {
				addSubscriptionRl.setVisibility(View.GONE);

			}
		}

		loadData();
	}

	public void loadData() {
		if (pages == null) {
			currentPage = 1;
		} else {
			if (pages.hasNextPage()) {
				currentPage = pages.getNextPage();
			} else {
				onComplete();
				Toast.makeText(this, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		doLoadData();
	}

	@Background
	void doLoadData() {
		jobUservice.findUserSubscriptionList(getUserId(), currentPage, pageSize).identify(kREQ_ID_findUserSubscriptionList = RequestChannel.getChannelUniqueID(), this);
	}

	@Background
	void delSubscription(long subId) {
		jobUservice.removeUserSubscription(subId).identify(kREQ_ID_removeUserSubscription = RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SubscriptionAddActivity_.intent(this).subscriptionDTO(subscriptionList.get(position - 1)).isEdit(true).startForResult(SubscriptionAddActivity.kREQUEST_ADD);
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			final long subId = (Long) v.getTag();
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle("删除");
			ab.setIcon(android.R.drawable.ic_dialog_alert);
			ab.setMessage("确认删除");
			ab.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					delId = subId;
					delSubscription(subId);
				}
			});
			ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					edit = false;
					setMenuState();
				}
			});
			ab.create().show();
		}
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	private boolean editable = false;

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findUserSubscriptionList)) {
			if (result != null) {

				pages = (Page<JobUserSubscriptionDTO>) result;
				if (pages.getCurrentPage() == 1) {
					subscriptionAdapter.updateItems(pages.getElements());
				} else {
					subscriptionAdapter.addToBottom(pages.getElements());
				}
			} else {
				subscriptionAdapter.clearItems();
			}
			// subscriptionAdapter.notifyDataSetChanged();

			if (subscriptionList != null && subscriptionList.size() == 0) {
				editable = false;
			} else {
				editable = true;
			}
			supportInvalidateOptionsMenu();
			
			emptyView.isEmpty(!(subscriptionAdapter.getCount()>0));
		}

		// 移除
		if (reqId.equals(kREQ_ID_removeUserSubscription)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
					this.edit = false;
					setMenuState();
			//		subScriptionListView.setRefreshing(true);
					JobUserSubscriptionDTO data;
					for(int i=0;i<subscriptionList.size();i++){
						data = subscriptionList.get(i);
						if(delId == data.getSubId()){
							subscriptionList.remove(i);
							subscriptionAdapter.notifyDataSetChanged();
							if (subscriptionList != null && subscriptionList.size() == 0) {
								editable = false;
								emptyView.isEmpty(true);
								supportInvalidateOptionsMenu();
							}
							
							break;
						}
					}
				} else {
					Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		onComplete();
	}

	@UiThread
	public void onComplete() {
		try {
			Thread.sleep(10L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		subScriptionListView.onRefreshComplete();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException(((ZcdhException)error).getErrCode(), this);
	}

	class SubscriptionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return subscriptionList.size();
		}

		/**
		 * @param elements
		 * @author jeason, 2014-7-11 下午3:18:45
		 */
		public void updateItems(List<JobUserSubscriptionDTO> elements) {
			subscriptionList.clear();
			subscriptionList.addAll(elements);
			notifyDataSetChanged();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void addToBottom(List<JobUserSubscriptionDTO> items) {
			subscriptionList.addAll(items);
			notifyDataSetChanged();
		}

		public void clearItems() {
			subscriptionList.clear();
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder v = null;
			if (convertView == null) {
				v = new ViewHolder();
				convertView = LayoutInflater.from(getApplication()).inflate(R.layout.subscription_item, null);
				v.industryText = (TextView) convertView.findViewById(R.id.industryText);
				v.postText = (TextView) convertView.findViewById(R.id.postText);
				v.areaText = (TextView) convertView.findViewById(R.id.areaTxt);
				v.accesoryImg = (ImageView) convertView.findViewById(R.id.accessoryImg);
				v.delBtn = (LinearLayout) convertView.findViewById(R.id.delImgBtn);
				v.delBtn.setOnClickListener(SubscriptionActivity.this);
				convertView.setTag(v);
			} else {
				v = (ViewHolder) convertView.getTag();
			}

			JobUserSubscriptionDTO jobUserSubscriptionDTO = subscriptionList.get(position);

			v.delBtn.setTag(jobUserSubscriptionDTO.getSubId());

			if (!StringUtils.isBlank(jobUserSubscriptionDTO.getIndustryName())) {
				v.industryText.setText("行业:" + jobUserSubscriptionDTO.getIndustryName());
			}
			if (!StringUtils.isBlank(jobUserSubscriptionDTO.getPostName())) {
				v.postText.setText("职位:" + jobUserSubscriptionDTO.getPostName());
			}
			if (!StringUtils.isBlank(jobUserSubscriptionDTO.getAreaName())) {
				v.areaText.setText("地区:" + jobUserSubscriptionDTO.getAreaName());
			}

			if (edit) {
				v.accesoryImg.setVisibility(View.VISIBLE);
				v.delBtn.setVisibility(View.GONE);
			} else {
				v.accesoryImg.setVisibility(View.GONE);
				v.delBtn.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		class ViewHolder {
			TextView industryText; // 行业
			TextView postText;// 职位
			TextView areaText;// 地区
			LinearLayout delBtn;
			ImageView accesoryImg;
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
		currentPage = 1;
		doLoadData();
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
		loadData();
	}

}
