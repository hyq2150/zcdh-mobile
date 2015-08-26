/**
 * 
 * @author jeason, 2014-6-18 下午4:24:31
 */
package com.zcdh.mobile.app.activities.job_fair;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.JobFairExtListDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.search.AreaActivity;
import com.zcdh.mobile.app.activities.search.AreaActivity_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeason, 2014-6-18 下午4:24:31 招聘会列表
 */
@EActivity(R.layout.job_fairs)
public class JobFairsActivity extends BaseActivity implements
		OnItemClickListener, OnRefreshListener2<ListView>, RequestListener,
		YuyinInputListner {

	private static final String TAG = JobFairsActivity.class.getSimpleName();
	@ViewById(R.id.all_fairs)
	Button all_fairs;
	@ViewById(R.id.my_fairs)
	Button my_fairs;
	@ViewById(R.id.listview)
	PullToRefreshListView listview;

	@ViewById(R.id.keywordEditText)
	EditText keywordEditText;

	@ViewById(R.id.searchBtn)
	Button searchBtn;

	@ViewById(R.id.micBtn)
	ImageButton micBtn;

	private int currentPage = 1;
	private int currentMyPage = 1;
	private final int PageSize = 10;
	private EmptyTipView emptyView;
	private LayoutInflater inflater;
	private IRpcJobFairService service;
	private Page<JobFairExtListDTO> pageFair, myPageFair;
	private String K_REQ_ID_FINDJOBFAIRLIST;
	private String K_REQ_ID_FINDMYJOBFAIRLTST;
	private JobfairsAdapter adapter;

	private String key_word = "";

	private String areaCode;

	private String areaName;

	private YuYinInputView yuyinView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),
				"招聘会");
		inflater = LayoutInflater.from(this);
		service = RemoteServiceManager
				.getRemoteService(IRpcJobFairService.class);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (yuyinView != null && yuyinView.isShowing()) {
			yuyinView.dismiss();
		}
	}

	@AfterViews
	void bindView() {

		searchBtn.setVisibility(View.VISIBLE);
		keywordEditText.setHint("搜索全部");
		keywordEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String keyword = s.toString();
				if (TextUtils.isEmpty(keyword)) {
					key_word = keyword;
					if (all_fairs.isSelected()) {
						currentPage = 1;
						getData();
					} else {
						currentMyPage = 1;
						getMyFairsData();
					}
				}
			}
		});

		emptyView = new EmptyTipView(this);
		listview.setMode(Mode.BOTH);
		listview.setOnItemClickListener(this);
		listview.setOnRefreshListener(this);
		listview.setEmptyView(emptyView);
		adapter = new JobfairsAdapter();
		listview.setAdapter(adapter);

		all_fairs.setSelected(true);
		my_fairs.setSelected(false);
		all_fairs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fairsSelect(v);
				getData();
			}
		});
		my_fairs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fairsSelect(v);
				signUp();
			}
		});
		getData();

	}

	void signUp() {
		if (!RegisterUtil.isRegisterUser(this)) {
			Toast.makeText(JobFairsActivity.this, "尚未登录！", Toast.LENGTH_SHORT)
					.show();
			ActivityDispatcher.to_login(this);
			return;
		}
		getMyFairsData();
	}

	private void fairsSelect(View v) {
		switch (v.getId()) {
		case R.id.my_fairs:
			if (RegisterUtil.isRegisterUser(this)) {
				my_fairs.setSelected(true);
				all_fairs.setSelected(false);
			}
			break;
		case R.id.all_fairs:
			my_fairs.setSelected(false);
			all_fairs.setSelected(true);
			break;
		default:
			break;
		}
	}

	@Background
	void getMyFairsData() {
		service.findMyJobFairExtList(key_word,
				ZcdhApplication.getInstance().getZcdh_uid(), currentMyPage,
				PageSize)
				.identify(
						K_REQ_ID_FINDMYJOBFAIRLTST = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Background
	void getData() {
		service.findJobFairExtList(key_word, areaCode, currentPage, PageSize)
				.identify(
						K_REQ_ID_FINDJOBFAIRLIST = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	private class JobfairsAdapter extends BaseAdapter {
		private List<JobFairExtListDTO> fairList;

		public JobfairsAdapter() {
			fairList = new ArrayList<JobFairExtListDTO>();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fairList.size();
		}

		@Override
		public JobFairExtListDTO getItem(int position) {
			return fairList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewholder;
			if (convertView == null) {
				viewholder = new ViewHolder();
				convertView = inflater.inflate(R.layout.job_fair_list_item_new,
						parent, false);
				convertView.setTag(viewholder);
				viewholder.tv_note = (TextView) convertView
						.findViewById(R.id.tv_note);
				viewholder.dateText = (TextView) convertView
						.findViewById(R.id.dateText);
				viewholder.tv_title = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewholder.stautText = (ImageView) convertView
						.findViewById(R.id.stautText);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}
			viewholder.tv_note.setText(getItem(position).getDecription());
			viewholder.tv_title.setText(getItem(position).getTitle());

			if (getItem(position).getStartTime() != null
					&& getItem(position).getEndTime() != null) {
				// setFairTime(viewholder.dateText, startTime, endTime);
				viewholder.dateText.setText(getItem(position).getTimeRange());
			}
			// 招聘会状态 0: 不通过 ，1：生效，上线，-1：审核中,2:草稿中 3：失效，暂停，4：已关闭 5：已结束
			int state = getItem(position).getStatus();
			// viewholder.stautText.setTag(position);
			Log.e(TAG, "state : " + state);
			switch (state) {
			case 0:
				viewholder.stautText.setVisibility(View.GONE);
				break;
			case 1:
				viewholder.stautText.setVisibility(View.VISIBLE);
				viewholder.stautText
						.setImageResource(R.drawable.fairs_status_ing);
				break;
			case 2:
				viewholder.stautText.setVisibility(View.VISIBLE);
				viewholder.stautText
						.setImageResource(R.drawable.fairs_status_stop);
				break;
			case 3:
				viewholder.stautText.setVisibility(View.VISIBLE);
				viewholder.stautText
						.setImageResource(R.drawable.fairs_status_stop);
				break;
			case 4:
				viewholder.stautText.setVisibility(View.VISIBLE);
				viewholder.stautText
						.setImageResource(R.drawable.fairs_status_close);
				break;
			case 5:
				viewholder.stautText.setVisibility(View.VISIBLE);
				viewholder.stautText
						.setImageResource(R.drawable.fairs_status_end);
				break;
			default:
				break;
			}
			return convertView;
		}

		/**
		 * @param elements
		 * @author jeason, 2014-6-12 下午2:45:04
		 */
		public void updateItems(List<JobFairExtListDTO> elements) {
			fairList.clear();
			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i).getStatus() == 0) {
					elements.remove(i);
				}
			}
			fairList.addAll(elements);
			notifyDataSetChanged();
		}

		/**
		 * @param elements
		 * @author jeason, 2014-6-12 下午2:45:06
		 */
		public void addToBottom(List<JobFairExtListDTO> elements) {
			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i).getStatus() == 0) {
					elements.remove(i);
				}
			}
			fairList.addAll(elements);
			notifyDataSetChanged();
		}
	}

	private class ViewHolder {
		TextView dateText;
		// TextView weekText;
		// TextView timeText;
		ImageView stautText;
		TextView tv_title;
		TextView tv_note;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ActivityDispatcher.toJobFairDetailActivityNew(this,
				adapter.getItem(arg2 - 1).getBannerId(),
				adapter.getItem(arg2 - 1).getStatus());
	}

	@Click(R.id.micBtn)
	void onMic() {
		if (yuyinView == null) {
			yuyinView = new YuYinInputView(this, this);
		}
		yuyinView.showAtParent(findViewById(R.id.body));
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (RegisterUtil.isRegisterUser(this)) {
			getLatest();
		}
		onComplete();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:07
	 */
	private void getLatest() {
		currentPage = 1;
		currentMyPage = 1;
		if (RegisterUtil.isRegisterUser(this)) {
			if (all_fairs.isSelected()) {
				getData();
			} else {
				getMyFairsData();
			}
		}
		onComplete();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		getMore();
	}

	/**
	 * 
	 * @author jeason, 2014-6-6 下午4:47:05
	 */
	private void getMore() {
		if (pageFair == null) {
			currentPage = 1;
			currentMyPage = 1;
		} else {
			if (all_fairs.isSelected()) {
				if (pageFair.hasNextPage()) {
					currentPage = pageFair.getNextPage();
				} else {
					onComplete();
					Toast.makeText(this,
							getResources().getString(R.string.no_more_data),
							Toast.LENGTH_SHORT).show();
					return;
				}
			} else {
				if (myPageFair.hasNextPage()) {
					currentMyPage = pageFair.getNextPage();
				} else {
					onComplete();
					Toast.makeText(this,
							getResources().getString(R.string.no_more_data),
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		if (all_fairs.isSelected()) {
			getData();
		} else {
			getMyFairsData();
		}
		onComplete();
	}

	@UiThread
	void onComplete() {
		listview.onRefreshComplete();
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(K_REQ_ID_FINDMYJOBFAIRLTST)) {
			if (result != null) {
				myPageFair = (Page<JobFairExtListDTO>) result;
				if (myPageFair.getCurrentPage() == 1) {
					adapter.updateItems(myPageFair.getElements());
				} else {
					adapter.addToBottom(myPageFair.getElements());
				}
				Log.e(TAG, "我的招聘会 : " + myPageFair.getElements().size() + "");
			}
		}
		if (reqId.equals(K_REQ_ID_FINDJOBFAIRLIST)) {
			if (result != null) {
				pageFair = (Page<JobFairExtListDTO>) result;
				if (pageFair.getCurrentPage() == 1) {
					adapter.updateItems(pageFair.getElements());
				} else {
					adapter.addToBottom(pageFair.getElements());
				}
				Log.e(TAG, "全部招聘会 : " + pageFair.getElements().size() + "");
			}
		}
		if (all_fairs.isSelected()) {
			if (pageFair != null && pageFair.getElements().size() > 0) {
				emptyView.isEmpty(false);
			} else {
				emptyView.isEmpty(true);
			}
		} else {
			if (myPageFair != null && myPageFair.getElements().size() > 0) {
				emptyView.isEmpty(false);
			} else {
				emptyView.isEmpty(true);
			}
		}

	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub

	}

	@Click(R.id.searchBtn)
	void onSearch() {
		String keyword = keywordEditText.getText().toString();
		if (!key_word.equals(keyword)) {
			key_word = keyword;
			if (all_fairs.isSelected()) {
				getData();
			} else {
				getMyFairsData();
			}

		}
	}

	/**
	 * 地区列表返回值
	 * 
	 * @param resultCode
	 * @param data
	 */
	@OnActivityResult(AreaActivity.kREQUEST_AREA)
	void onResultArea(int resultCode, Intent data) {
		if (data != null && data.getExtras() != null) {
			areaCode = data.getExtras().getString(AreaActivity.kDATA_CODE);
			areaName = data.getExtras().getString(AreaActivity.kDATA_NAME);
			supportInvalidateOptionsMenu();
			if ("0".equals(areaCode)) {
				areaCode = null;
			}
			if (all_fairs.isSelected()) {
				currentPage = 1;
				getData();
			} else {
				currentMyPage = 1;
				getMyFairsData();
			}
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.action_area_select, menu);
		if (areaCode != null) {
			menu.findItem(R.id.action_area_select).setTitle(areaName);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@OptionsItem(R.id.action_area_select)
	void onAreaSelect() {
		AreaActivity_.intent(this).showQuanguo(true)
				.startForResult(AreaActivity.kREQUEST_AREA);
	}

	@Override
	public void onComplete(String content) {
		if (!key_word.equals(content)) {
			key_word = content;
			keywordEditText.setText(content);
			if (all_fairs.isSelected()) {
				getData();
			} else {
				getMyFairsData();
			}
		}
	}
}
