package com.zcdh.mobile.app.activities.personal;

import com.baidu.mapapi.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobSearchService;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.JobUserSubscriptionDTO;
import com.zcdh.mobile.api.model.SearchConditionDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.search.AreaActivity;
import com.zcdh.mobile.app.activities.search.AreaActivity_;
import com.zcdh.mobile.app.activities.search.CategoryPostActivity_;
import com.zcdh.mobile.app.activities.search.IndustryActivity;
import com.zcdh.mobile.app.activities.search.IndustryActivity_;
import com.zcdh.mobile.app.activities.search.PostsActivity;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.DateUtils;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加订阅
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_add_subscription)
public class SubscriptionAddActivity extends BaseActivity implements RequestListener, OnRefreshListener<ListView>, OnItemClickListener, DataLoadInterface {

	public static final int kREQUEST_ADD = 2011;

	private String kREQ_ID_addUserSubscription;
	private String kREQ_ID_updateUserSubscription;
	private String kREQ_ID_findEntPostDTOByHighSearch;

	private IRpcJobUservice jobUservice;
	private IRpcJobSearchService searchService;

	/**
	 * 编辑表单
	 */
	@ViewById(R.id.formContainer)
	LinearLayout formContainer;

	/**
	 * 选择行业
	 */
	@ViewById(R.id.industryText)
	TextView industryText;

	/**
	 * 选择职位
	 */
	@ViewById(R.id.postText)
	TextView postText;

	/**
	 * 选择地区
	 */
	@ViewById(R.id.areaTxtz)
	TextView areaText;

	/**
	 * 显示订阅结果列表
	 */
	@ViewById(R.id.subScriptionListView)
	PullToRefreshListView subScriptionListView;

	private SubscriptionAdapter subscriptionAdapter;

	private EmptyTipView emptyView;

	private LoadingIndicator loading;

	private View headerView2;

	/**
	 * 订阅结果
	 */
	List<JobEntPostDTO> subscriptions = new ArrayList<JobEntPostDTO>();

	@Extra
	JobUserSubscriptionDTO subscriptionDTO = new JobUserSubscriptionDTO();

	private Page<JobEntPostDTO> postPage;

	/**
	 * 标识是否编辑
	 */
	@Extra
	boolean isEdit;

	/**
	 * 搜索订阅结果条件
	 */
	private SearchConditionDTO highSearchDTO = new SearchConditionDTO();

	private Integer currentPge = 0;

	private Integer pageSize = 20;

	private boolean editSuccess;

	private boolean addOnce = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loading = new LoadingIndicator(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		if (!isEdit) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.save, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (editSuccess) { // 如果当前
				setResult(RESULT_OK);
				finish();
			} else {
				if (isShowTip()) {
					showTip();
				} else {
					finish();
				}
			}

			return true;
		}

		if (item.getItemId() == R.id.action_save) {
			subscriptionDTO.setUserId(getUserId());
			boolean validate = true;
			if (StringUtils.isBlank(subscriptionDTO.getAreaCode())) {
				validate = false;
				Toast.makeText(this, "请选择地区", Toast.LENGTH_SHORT).show();
			}
			if (StringUtils.isBlank(subscriptionDTO.getPostCode())) {
				validate = false;
				Toast.makeText(this, "请选择职位", Toast.LENGTH_SHORT).show();
			}
			if (StringUtils.isBlank(subscriptionDTO.getIndustryCode())) {
				validate = false;
				Toast.makeText(this, "请选择行业", Toast.LENGTH_SHORT).show();
			}
			if (validate) {
				loading.show();
				saveSubscription();
			}
		}

		return super.onOptionsItemSelected(item);

	}

	@AfterViews
	void bindViews() {
		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		searchService = RemoteServiceManager.getRemoteService(IRpcJobSearchService.class);
		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), "");

		/*
		 * 如果是编辑或查看订阅
		 */
		if (isEdit) {
			SystemServicesUtils.setActionBarCustomTitle(getApplicationContext(), getSupportActionBar(), "订阅列表");
			/*
			 * if(subscriptionDTO!=null){
			 * industryText.setText(subscriptionDTO.getIndustryName());
			 * postText.setText(subscriptionDTO.getPostName());
			 * areaText.setText(subscriptionDTO.getAreaName()); }
			 */
			formContainer.setVisibility(View.GONE);

			highSearchDTO.setAreaCode(subscriptionDTO.getAreaCode());
			highSearchDTO.setIndustryCode(subscriptionDTO.getIndustryCode());
			highSearchDTO.setPostCode(subscriptionDTO.getPostCode());
			LatLng center = ZcdhApplication.getInstance().getMyLocation();
			if(center!=null){
				highSearchDTO.setLat(center.latitude);
				highSearchDTO.setLon(center.longitude);
			}
			subScriptionListView.getRefreshableView().addHeaderView(getHeaderView(), null, false);
			setCountText();
			subScriptionListView.getRefreshableView().addHeaderView(headerView2, null, false);

			// 加载订阅条件结果
			subscriptionAdapter = new SubscriptionAdapter();
			subScriptionListView.setAdapter(subscriptionAdapter);
			subScriptionListView.setEmptyView(emptyView);
			subScriptionListView.setMode(Mode.PULL_FROM_END);
			subScriptionListView.setOnRefreshListener(this);
			subScriptionListView.setOnItemClickListener(this);
			emptyView = new EmptyTipView(this);
			subScriptionListView.setEmptyView(emptyView);
			loadSubscriptions();
		} else {
			SystemServicesUtils.setActionBarCustomTitle(getApplicationContext(), getSupportActionBar(), "添加订阅");
			subScriptionListView.setVisibility(View.GONE);
		}
	}

	/**
	 * 选择行业
	 */
	@Click(R.id.industryRl)
	void onIndustryRl() {
		IndustryActivity_.intent(this).startForResult(IndustryActivity.kREQUEST_INDUSTRY);
	}

	/**
	 * 选择职位
	 */
	@Click(R.id.postRl)
	void onPostRl() {
		CategoryPostActivity_.intent(this).single(true).startForResult(PostsActivity.kREQUEST_POST);
	}


	/**
	 * 选择地区
	 */
	@Click(R.id.areaRl)
	void onAreaRl() {
		AreaActivity_.intent(this).startForResult(AreaActivity_.kREQUEST_AREA);
	}

	/**
	 * 接收选择的行业
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(IndustryActivity.kREQUEST_INDUSTRY)
	void onResultIndustry(int resultCode, Intent data) {
		if (data != null) {
			String name = data.getExtras().getString(IndustryActivity.kDATA_NAME);
			subscriptionDTO.setIndustryCode(data.getExtras().getString(IndustryActivity.kDATA_CODE));
			subscriptionDTO.setIndustryName(name);
			industryText.setText(name);
			industryText.setTextColor(getResources().getColor(R.color.font_color));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		long postid = subscriptions.get(position - 3).getPostId();
		ActivityDispatcher.toDetailsFrameActivity(this, postid, true, subscriptions, position - 3);
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		loadSubscriptions();
	}

	@UiThread
	void onComplete() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		subScriptionListView.onRefreshComplete();
	}

	/**
	 * 接收选择的职位
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(PostsActivity.kREQUEST_POST)
	void onResultPost(int resultCode, Intent data) {
		if (data != null) {
			String name = data.getExtras().getString(PostsActivity.kDATA_NAME);
			subscriptionDTO.setPostCode(data.getExtras().getString(PostsActivity.kDATA_CODE));
			subscriptionDTO.setPostName(name);
			postText.setText(name);
			postText.setTextColor(getResources().getColor(R.color.font_color));
		}
	}

	/**
	 * 接收选择的地区
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(AreaActivity.kREQUEST_AREA)
	void onResultArea(int resultCode, Intent data) {
		if (data != null) {
			String name = data.getExtras().getString(AreaActivity.kDATA_NAME);
			subscriptionDTO.setAreaCode(data.getExtras().getString(AreaActivity.kDATA_CODE));
			subscriptionDTO.setAreaName(name);
			areaText.setText(name);
			areaText.setTextColor(getResources().getColor(R.color.font_color));
		}
	}

	/**
	 * 添加和编辑订阅
	 */
	@Background
	void saveSubscription() {

		if (isEdit) {
			jobUservice.updateUserSubscription(subscriptionDTO).identify(kREQ_ID_updateUserSubscription = RequestChannel.getChannelUniqueID(), this);
		} else {
			jobUservice.addUserSubscription(subscriptionDTO).identify(kREQ_ID_addUserSubscription = RequestChannel.getChannelUniqueID(), this);
		}
	}

	/**
	 * 加载订阅结果
	 */
	void loadSubscriptions() {
//		hasNextPage = false;
		currentPge++;

		if (postPage == null) {
			currentPge = 1;
		} else {
			if (postPage.hasNextPage()) {
				currentPge = postPage.getNextPage();
			} else {
				onComplete();
				Toast.makeText(this, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		loadData();
	}

	@Background
	public void loadData() {
		searchService.findEntPostDTOByHighSearch(highSearchDTO, currentPge, pageSize).identify(kREQ_ID_findEntPostDTOByHighSearch = RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_addUserSubscription)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
					setResult(RESULT_OK);
					finish();
				} else {
					Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
				}
			}
		}

		if (reqId.equals(kREQ_ID_updateUserSubscription)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
					editSuccess = true;
				} else {
					Toast.makeText(this, "服务繁忙,保存失败", Toast.LENGTH_SHORT).show();
				}
			}
		}

		if (reqId.equals(kREQ_ID_findEntPostDTOByHighSearch)) {
			if (result != null) {
				postPage = (Page<JobEntPostDTO>) result;
				if (addOnce) {
					addOnce = !addOnce;
					setCountText();
				}
				if (postPage.getElements() != null && postPage.getElements().size() > 0) {
//					hasNextPage = postPage.hasNextPage();
					subscriptions.addAll(postPage.getElements());
					subscriptionAdapter.notifyDataSetChanged();
				}

				new Handler().postDelayed(new Runnable() {
					public void run() {
						onComplete();
					}

				}, 1000);

			}

			if (subscriptions != null && subscriptions.size() == 0) {
				emptyView.isEmpty(true);
			}else{
				emptyView.isEmpty(false);
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		loading.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException)error, this);
	}

	class SubscriptionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return subscriptions.size();
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
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.post_item, parent,false);
				holder = new ViewHolder();
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.distance = (TextView) convertView.findViewById(R.id.distance);
				holder.ll_tags_container = (TagsContainer) convertView.findViewById(R.id.ll_tags);
				holder.location_and_requirement = (TextView) convertView.findViewById(R.id.location_and_education_and_matchrate);
				holder.publish_time = (TextView) convertView.findViewById(R.id.publish_time);
				holder.salary = (TextView) convertView.findViewById(R.id.salary);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position % 2 == 0) {
				convertView.setBackgroundResource(R.drawable.list_item_grey_selector);
			} else {
				convertView.setBackgroundResource(R.drawable.list_item_silver_selector);
			}

			JobEntPostDTO post = subscriptions.get(position);
			holder.location_and_requirement.setText(post.getAreaName() + "/" + post.getDegree());
			holder.publish_time.setText(DateUtils.getDateByFormat(post.getPublishDate(), "yyyy-MM-dd"));// .getDateByFormatNUM(post.getPublishDate()));
			holder.salary.setText(post.getSalary());
			holder.title.setText(post.getPostAliases());
			holder.content.setText(subscriptions.get(position).getEntName());
			double distance = subscriptions.get(position).getDistance();
			if (distance == 0) { // 如果为0 不显示
				holder.distance.setVisibility(View.GONE);
			} else if (distance < 1000) { // 如果小于1千米，以米为单位
				String d = distance + "";
				holder.distance.setText(d.subSequence(0, d.indexOf(".")) + "米");
			} else if (distance > 1000) {
				double d = distance / 1000;
				Log.e("destance:", distance+"");
				BigDecimal b = new BigDecimal(d);
				double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				holder.distance.setText(String.valueOf(f1 + "千米"));
			}

			holder.ll_tags_container.removeAllViews();
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 0, 0, 0);
			if (subscriptions.get(position).getWelfares() != null) {
				holder.ll_tags_container.initData(subscriptions.get(position).getWelfares().subList(0, 3), null, Gravity.RIGHT);
			}

			return convertView;
		}
	}

	public class ViewHolder {
		private TextView title;
		private TextView publish_time;
		private TextView content;
		private TextView salary;
		private TextView location_and_requirement;
		private TextView distance;
		private TagsContainer ll_tags_container;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			if (isShowTip()) {
				showTip();
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	boolean isShowTip() {
		if (isEdit) {
			return false;
		}
		if (!StringUtils.isBlank(subscriptionDTO.getAreaCode())) {
			return true;
		}
		if (!StringUtils.isBlank(subscriptionDTO.getPostCode())) {
			return true;
		}
		if (!StringUtils.isBlank(subscriptionDTO.getIndustryCode())) {
			return true;
		}
		return false;
	}

	void showTip() {
		new AlertDialog.Builder(this).setCancelable(true).setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setTitle(R.string.friendly_hint).setNegativeButton(R.string.cancel, null).setMessage(R.string.edit_mode_exit).create().show();

	}

	private View getHeaderView() {

		View v = LayoutInflater.from(getApplication()).inflate(R.layout.subscription_item,null);
		TextView industryText = (TextView) v.findViewById(R.id.industryText);
		TextView postText = (TextView) v.findViewById(R.id.postText);
		TextView areaText = (TextView) v.findViewById(R.id.areaTxt);
		ImageView accesoryImg = (ImageView) v.findViewById(R.id.accessoryImg);
		View delBtn = (LinearLayout) v.findViewById(R.id.delImgBtn);

		delBtn.setTag(subscriptionDTO.getSubId());

		if (!StringUtils.isBlank(subscriptionDTO.getIndustryName())) {
			industryText.setText("行业:" + subscriptionDTO.getIndustryName());
		}
		if (!StringUtils.isBlank(subscriptionDTO.getPostName())) {
			postText.setText("职位:" + subscriptionDTO.getPostName());
		}
		if (!StringUtils.isBlank(subscriptionDTO.getAreaName())) {
			areaText.setText("地区:" + subscriptionDTO.getAreaName());
		}
		v.setBackgroundResource(android.R.color.transparent);
		accesoryImg.setVisibility(View.GONE);
		delBtn.setVisibility(View.GONE);
		return v;
	}

	private void setCountText() {
		if (headerView2 == null) {
			headerView2 = LayoutInflater.from(this).inflate(R.layout.current_post_total, null);
			TextView tv = (TextView) headerView2.findViewById(R.id.posts_total);
			tv.setText(String.format("为您找到职位 共%d个", 0));
		} else {
			TextView tv = (TextView) headerView2.findViewById(R.id.posts_total);
			tv.setText(String.format("为您找到职位 共%d个", postPage.getTotalRows()));
		}

	}

}
