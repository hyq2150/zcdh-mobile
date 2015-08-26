package com.zcdh.mobile.app.activities.job_fair;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.JobFairPost;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.activities.detail.DetailsFrameActivity_;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.iflytek.YuYinInputView;
import com.zcdh.mobile.app.views.iflytek.YuyinInputListner;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

@EActivity(R.layout.participants_position)
public class ParticipantsPositionActivity extends BaseActivity implements
		RequestListener, OnItemClickListener, YuyinInputListner,
		OnRefreshListener2<ListView> {

	private static final String TAG = ParticipantsPositionActivity.class
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

	private String K_REQ_ID_findJobFairPostBySearchType;
	private IRpcJobFairService jobfairService;
	private EmptyTipView emptyView;
	private Page<JobFairPost> pageJobFairPost;
	private ParticipantsPositionAdapter adapter;
	private LayoutInflater inflater;
	private String keyWord = "";
	private String industryCode = "";
	private int isSearchOffline = 1;
	private int isSearchOnline = 1;
	private int currentPage = 1;
	private int pageSize = 10;
	private YuYinInputView yuyinView;
	private List<JobFairPost> fairList;
	private List<JobEntPostDTO> posts = new ArrayList<JobEntPostDTO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),
				getResources().getString(R.string.participants_position));
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
		keywordEditText.setHint("请输入职位名称搜索职位");
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
		}else {
			Toast.makeText(this, "请输入关键字进行搜索", Toast.LENGTH_SHORT).show();
		}
	}

	@Click(R.id.filterBtn)
	void onFilter() {
		ActivityDispatcher.to_ParcitipantsIndustry(true, 0, fairId, this);
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
				.findJobFairPostBySearchType(fairId, keyWord, industryCode,
						isSearchOffline, isSearchOnline, currentPage, pageSize)
				.identify(
						K_REQ_ID_findJobFairPostBySearchType = RequestChannel
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
		Log.e("fuck", "onRequestSuccess");
		if (reqId.equals(K_REQ_ID_findJobFairPostBySearchType)) {
			if (result != null) {
				pageJobFairPost = (Page<JobFairPost>) result;
				if (pageJobFairPost.getCurrentPage() == 1) {
					adapter.updateItems(pageJobFairPost.getElements());
				} else {
					adapter.addToBottom(pageJobFairPost.getElements());
				}
				for (int i = 0; i < pageJobFairPost.getElements().size(); i++) {
					JobFairPost post = (JobFairPost) (pageJobFairPost
							.getElements().get(i));
					JobEntPostDTO post2 = new JobEntPostDTO();
					post2.setPostId(post.getPostId());
					post2.setEntId(post.getEntId());
					post2.setPostAliases(post.getPostName());
					posts.add(post2);
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		if (reqId.equals(K_REQ_ID_findJobFairPostBySearchType)) {
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
		SharedPreferencesUtil.putValue(this, Constants.JOBFAIR_ID_KEY, fairId);
		DetailsFrameActivity_.intent(this).isFair(true).fairID(fairId)
				.postId(fairList.get(arg2 - 1).getPostId()).switchable(true)
				.currentIndex(arg2 - 1).posts(posts)// JobEntPostDTO
				.entId(fairList.get(arg2 - 1).getEntId()).start();
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
		public ParticipantsPositionAdapter() {
			super();
			// TODO Auto-generated constructor stub
			fairList = new ArrayList<JobFairPost>();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fairList.size();
		}

		@Override
		public JobFairPost getItem(int position) {
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
						R.layout.participants_position_item, parent, false);
				viewholder.booth = (TextView) convertView
						.findViewById(R.id.booth);
				viewholder.ent_name = (TextView) convertView
						.findViewById(R.id.ent_name);
				viewholder.salary = (TextView) convertView
						.findViewById(R.id.salary);
				viewholder.position_name = (TextView) convertView
						.findViewById(R.id.position_name);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}
			if (getItem(position) != null) {
				if (getItem(position).getBoothNo() != null
						&& getItem(position).getBoothNo().length() > 0) {
					viewholder.booth.setText("展位号  : "
							+ getItem(position).getBoothNo());
				} else {
					viewholder.booth.setText("在线招聘");
				}

				viewholder.ent_name.setText(getItem(position).getEntName());
				viewholder.position_name.setText(getItem(position)
						.getPostNameTemp());
				viewholder.salary.setText(getItem(position).getPsalary());
			}
			return convertView;
		}

		public void updateItems(List<JobFairPost> elements) {
			fairList.clear();
			fairList.addAll(elements);
			notifyDataSetChanged();
		}

		public void addToBottom(List<JobFairPost> elements) {
			fairList.addAll(elements);
			notifyDataSetChanged();
		}

		private class ViewHolder {
			TextView booth;
			TextView position_name;
			TextView salary;
			TextView ent_name;
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
