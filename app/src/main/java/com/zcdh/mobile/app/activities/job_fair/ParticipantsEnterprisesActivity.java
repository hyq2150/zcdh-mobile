package com.zcdh.mobile.app.activities.job_fair;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.JobFairEnt;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.ent.MainEntActivity_;
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
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.participants_enterprises)
public class ParticipantsEnterprisesActivity extends BaseActivity implements
		RequestListener, YuyinInputListner, OnItemClickListener,
		OnRefreshListener2<ListView> {

	private static final String TAG = ParticipantsEnterprisesActivity.class
			.getSimpleName();

	@ViewById(R.id.clearIMG)
	ImageView clearIMG;

	@ViewById(R.id.select_title)
	TextView select_title;

	@ViewById(R.id.filterBtn)
	Button filterBtn;

	@ViewById(R.id.micBtn)
	ImageButton micBtn;

	@ViewById(R.id.searchBtn)
	Button searchBtn;

	@ViewById(R.id.keywordEditText)
	EditText keywordEditText;

	@Extra
	Long fairId;

	@ViewById(R.id.listview)
	PullToRefreshListView listview;

	private String K_REQ_ID_findJobFairEntBySearchType;
	private IRpcJobFairService jobfairService;
	private EmptyTipView emptyView;
	private Page<JobFairEnt> pageJobFairPost;
	private ParticipantsPositionAdapter adapter;
	private LayoutInflater inflater;
	private String keyWord = "";
	private String industryCode = "";
	private int isSearchOffline = 1;
	private int isSearchOnline = 1;
	private int currentPage = 1;
	private int pageSize = 10;
	private YuYinInputView yuyinView;
	private List<JobFairEnt> fairList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),
				getResources().getString(R.string.participants_enterprises));
		jobfairService = RemoteServiceManager
				.getRemoteService(IRpcJobFairService.class);
		inflater = LayoutInflater.from(this);
	}

	@AfterViews
	void bindView() {
		emptyView = new EmptyTipView(this);
		listview.setMode(Mode.BOTH);
		listview.setOnItemClickListener(this);
		listview.setOnRefreshListener(this);
		listview.setEmptyView(emptyView);
		adapter = new ParticipantsPositionAdapter();
		listview.setAdapter(adapter);
		getData();
		clearIMG.setVisibility(View.GONE);
		clearIMG.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				keyWord = "";
				industryCode = "";
				isSearchOffline = 1;
				isSearchOnline = 1;
				currentPage = 1;
				setSearchTitle(isSearchOnline, isSearchOffline, "");
				getData();
				clearIMG.setVisibility(View.GONE);
			}
		});
		keywordEditText.setHint("请输入企业名称搜索");
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
				// TODO Auto-generated method stub
				String keyword = s.toString();
				if (TextUtils.isEmpty(keyword)) {
					keyWord = keyword;
					currentPage = 1;
					getData();
				}
			}
		});
	}

	@Click(R.id.searchBtn)
	void onSearchBtn() {
		String keyword = keywordEditText.getText().toString();
		if (keyword != null) {
			if (!keyWord.equals(keyword)) {
				keyWord = keyword;
				getData();
			}
		} else {
			Toast.makeText(this, "请输入企业名称的关键词进行搜索", Toast.LENGTH_SHORT).show();
		}
	}

	@Click(R.id.filterBtn)
	void onFilter() {
		ActivityDispatcher.to_ParcitipantsIndustry(true, 1, fairId, this);
	}

	private void setSearchTitle(int isSearchOnline, int isSearchOffline,
			String all) {
		String tmp = "";
		if (isSearchOffline == 1) {
			if (isSearchOnline == 1) {
				tmp = "现场 , 线上";
			} else {
				tmp = "现场";
			}
		} else {
			if (isSearchOnline == 1) {
				tmp = "线上";
			} else {
				tmp = "现场 , 线上";
			}
		}
		if (all != null && !all.equals("")) {
			select_title.setText(tmp + " - " + all);
		} else {
			select_title.setText(tmp + " - 全部");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			industryCode = data.getStringExtra(Constants.kDATA_CODE);
			isSearchOnline = data.getIntExtra(Constants.kDATA_ONLINE, 1);
			isSearchOffline = data.getIntExtra(Constants.kDATA_OFFLINE, 1);
			setSearchTitle(isSearchOnline, isSearchOffline,
					data.getStringExtra(Constants.kDATA_NAME));
			clearIMG.setVisibility(View.VISIBLE);
			getData();
		}
	}

	@Click(R.id.micBtn)
	void onMic() {
		if (yuyinView == null) {
			yuyinView = new YuYinInputView(this, this);
		}
		yuyinView.showAtParent(findViewById(R.id.body));
	}

	@Background
	void getData() {
		// TODO Auto-generated method stub
		jobfairService
				.findJobFairEntBySearchType(fairId, keyWord, industryCode,
						isSearchOffline, isSearchOnline, currentPage, pageSize)
				.identify(
						K_REQ_ID_findJobFairEntBySearchType = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	private void getLatest() {
		currentPage = 1;
		if (RegisterUtil.isRegisterUser(this)) {
			getData();
		}
		onComplete();
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		// TODO Auto-generated method stub
		if (reqId.equals(K_REQ_ID_findJobFairEntBySearchType)) {
			if (result != null) {
				pageJobFairPost = (Page<JobFairEnt>) result;
				if (pageJobFairPost.getCurrentPage() == 1) {
					adapter.updateItems(pageJobFairPost.getElements());
				} else {
					adapter.addToBottom(pageJobFairPost.getElements());
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		if (reqId.equals(K_REQ_ID_findJobFairEntBySearchType)) {
			emptyView.isEmpty(pageJobFairPost.getElements().size() == 0);
		}
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		MainEntActivity_.intent(this).entId(fairList.get(arg2 - 1).getEntId())
				.jobfair_id(fairId).default_index(0).start();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		if (RegisterUtil.isRegisterUser(this)) {
			getLatest();
		}
		onComplete();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		getMore();
	}

	private void getMore() {
		if (pageJobFairPost == null) {
			currentPage = 1;
		} else {
			if (pageJobFairPost.hasNextPage()) {
				currentPage = pageJobFairPost.getNextPage();
			} else {
				onComplete();
				Toast.makeText(this,
						getResources().getString(R.string.no_more_data),
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		getData();
		onComplete();
	}

	@UiThread
	void onComplete() {
		listview.onRefreshComplete();
	}

	private class ParticipantsPositionAdapter extends BaseAdapter {
		private DisplayImageOptions options;

		public ParticipantsPositionAdapter() {
			super();
			// TODO Auto-generated constructor stub
			fairList = new ArrayList<JobFairEnt>();
			options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.showImageOnFail(R.drawable.ent_logo)
					.showImageForEmptyUri(R.drawable.ent_logo)
					.showImageOnLoading(R.drawable.ent_logo).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true).build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fairList.size();
		}

		@Override
		public JobFairEnt getItem(int position) {
			// TODO Auto-generated method stub
			return fairList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewholder;
			if (convertView == null) {
				viewholder = new ViewHolder();
				convertView = inflater.inflate(
						R.layout.participants_enterprises_item, parent, false);
				viewholder.booth = (TextView) convertView
						.findViewById(R.id.booth);
				viewholder.ent_name = (TextView) convertView
						.findViewById(R.id.ent_name);
				viewholder.enterprises_logo = (ImageView) convertView
						.findViewById(R.id.enterprises_logo);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}

			if (getItem(position) != null) {
				try {
					ImageLoader.getInstance().displayImage(
							getItem(position).getEntLogo().getMedium(),
							viewholder.enterprises_logo, options);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				if (getItem(position).getBoothNo() != null
						&& getItem(position).getBoothNo().length() > 0) {
					viewholder.booth.setText("展位号  : "
							+ getItem(position).getBoothNo());
				} else {
					viewholder.booth.setText("在线招聘");
				}

				viewholder.ent_name.setText(getItem(position).getEntNameTemp());
			}
			return convertView;
		}

		public void updateItems(List<JobFairEnt> elements) {
			fairList.clear();
			fairList.addAll(elements);
			notifyDataSetChanged();
		}

		public void addToBottom(List<JobFairEnt> elements) {
			fairList.addAll(elements);
			notifyDataSetChanged();
		}

		private class ViewHolder {
			TextView booth;
			TextView ent_name;
			ImageView enterprises_logo;
		}
	}

	@Override
	public void onComplete(String content) {
		// TODO Auto-generated method stub
		if (!keyWord.equals(content)) {
			keyWord = content;
			keywordEditText.setText(content);
			getData();
		}
	}
}
