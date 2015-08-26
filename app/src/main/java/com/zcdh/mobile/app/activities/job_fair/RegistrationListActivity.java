package com.zcdh.mobile.app.activities.job_fair;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.comm.entity.Page;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobFairService;
import com.zcdh.mobile.api.model.UserSignUpDTO;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.widget.CircleBitmapDisplayer;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.RegisterUtil;
import com.zcdh.mobile.utils.SystemServicesUtils;

@EActivity(R.layout.registration_list)
public class RegistrationListActivity extends BaseActivity implements
		RequestListener, OnRefreshListener2<ListView> {

	@ViewById(R.id.listview)
	PullToRefreshListView listview;

	@ViewById(R.id.register_num)
	TextView register_num;

	@Extra
	long fairId;
	private LayoutInflater inflater;
	private int currentPage = 1;
	private int pageSize = 10;
	private IRpcJobFairService jobfairService;
	private String kDATA_ID_findFairUserSignUpAndSchoolListByFairId;
	private String kDATA_ID_findFairUserSignUpAndSchoolCountByFairId;
	private Page<UserSignUpDTO> pageUserSignUpDTO;
	private RegistrationList adapter;
	private EmptyTipView emptyView;

	private SpannableStringBuilder getSpannableStringBuilder(String txt) {
		SpannableStringBuilder builder = new SpannableStringBuilder(txt);
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(this
				.getResources().getColor(R.color.participants));
		builder.setSpan(greenSpan, 5, txt.length() - 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(),"报名名单");
		jobfairService = RemoteServiceManager
				.getRemoteService(IRpcJobFairService.class);
		inflater = LayoutInflater.from(this);
	}

	@AfterViews
	void bindView() {
		adapter = new RegistrationList();
		emptyView = new EmptyTipView(this);
		listview.setMode(Mode.BOTH);
		listview.setOnRefreshListener(this);
		listview.setEmptyView(emptyView);
		listview.setAdapter(adapter);
		getData();
	}

	@UiThread
	void onComplete() {
		listview.onRefreshComplete();
	}

	private void getMore() {
		if (pageUserSignUpDTO == null) {
			currentPage = 1;
		} else {
			if (pageUserSignUpDTO.hasNextPage()) {
				currentPage = pageUserSignUpDTO.getNextPage();
			} else {
				onComplete();
				Toast.makeText(this, getResources().getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
				return;
			}
		}
		getData();
		onComplete();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		getMore();
	}

	@Background
	void getData() {
		// TODO Auto-generated method stub
		jobfairService.findFairUserSignUpAndSchoolCountByFairId(fairId).identify(kDATA_ID_findFairUserSignUpAndSchoolCountByFairId = RequestChannel.getChannelUniqueID(), this);
		jobfairService
				.findFairUserSignUpAndSchoolListByFairId(fairId, currentPage,
						pageSize)
				.identify(
						kDATA_ID_findFairUserSignUpAndSchoolListByFairId = RequestChannel
								.getChannelUniqueID(),
						this);
		
	}

	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		// TODO Auto-generated method stub
		if (reqId.equals(kDATA_ID_findFairUserSignUpAndSchoolCountByFairId)) {
			register_num.setText(getSpannableStringBuilder("已报名 ：" + result.toString() + " 人"));
		}
		if (reqId.equals(kDATA_ID_findFairUserSignUpAndSchoolListByFairId)) {
			if (result != null) {
				pageUserSignUpDTO = (Page<UserSignUpDTO>) result;
				if (pageUserSignUpDTO.getCurrentPage() == 1) {
					adapter.updateItems(pageUserSignUpDTO.getElements());
				} else {
					adapter.addToBottom(pageUserSignUpDTO.getElements());
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		// TODO Auto-generated method stub
		if (reqId.equals(kDATA_ID_findFairUserSignUpAndSchoolListByFairId)) {
			emptyView.isEmpty(pageUserSignUpDTO.getElements().size() == 0);
		}
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub

	}

	public class RegistrationList extends BaseAdapter {

		private List<UserSignUpDTO> userSignUpDTOList;
		private DisplayImageOptions options;

		public RegistrationList() {
			super();
			// TODO Auto-generated constructor stub
			userSignUpDTOList = new ArrayList<UserSignUpDTO>();
			options = new DisplayImageOptions.Builder().cacheInMemory(true).showImageOnFail(R.drawable.reg_icon).showImageForEmptyUri(R.drawable.reg_icon).showImageOnLoading(R.drawable.reg_icon)
					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
					.considerExifParams(true).displayer(new CircleBitmapDisplayer()).build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userSignUpDTOList.size();
		}

		@Override
		public UserSignUpDTO getItem(int position) {
			// TODO Auto-generated method stub
			return userSignUpDTOList.get(position);
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
						R.layout.registration_list_item, parent, false);
				viewholder.register_icon = (ImageView) convertView.findViewById(R.id.register_icon);
				viewholder.register_name = (TextView) convertView.findViewById(R.id.register_name);
				viewholder.register_time = (TextView) convertView.findViewById(R.id.register_time);
				convertView.setTag(viewholder);
			}else {
				viewholder = (ViewHolder) convertView.getTag();
			}
				viewholder.register_name.setText(userSignUpDTOList.get(position).getUser_name());
				viewholder.register_time.setText(userSignUpDTOList.get(position).getTimestr());
				ImageLoader.getInstance().displayImage(userSignUpDTOList.get(position).getImgURLDTO().getMedium(),
						viewholder.register_icon, options);
			return convertView;
		}

		public void updateItems(List<UserSignUpDTO> elements) {
			userSignUpDTOList.clear();
			userSignUpDTOList.addAll(elements);
			notifyDataSetChanged();
		}

		public void addToBottom(List<UserSignUpDTO> elements) {
			userSignUpDTOList.addAll(elements);
			notifyDataSetChanged();
		}

		private class ViewHolder {
			ImageView register_icon;
			TextView register_name;
			TextView register_time;
		}
	}

	private void getLatest() {
		currentPage = 1;
		if (RegisterUtil.isRegisterUser(this)) {
			getData();
		}
		onComplete();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		if (RegisterUtil.isRegisterUser(this)) {
			getLatest();
		}
		onComplete();
	}
}
