package com.zcdh.mobile.app.activities.personal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobWorkExperienceDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 工作经验
 * 
 * @author yangjiannan 2015-05-17 14:12
 */
@SuppressLint("SimpleDateFormat")
@EActivity(R.layout.activity_work_experience)
public class WorkExperienceActivity extends BaseActivity implements RequestListener, 
	OnItemClickListener, OnClickListener, DataLoadInterface, OnRefreshListener<ListView> {

	private String kREQ_ID_findJobWorkExperienceDTOByUserId;
	private String kREQ_ID_removeJobWorkExperiencePost;

	private IRpcJobUservice jobUservice;
	
	/**
	 * 工作经验列表
	 */
	@ViewById(R.id.workExpListView)
	PullToRefreshListView workExpListView;

	private BaseAdapter workExpAdapter;

	/**
	 * 工作经验
	 */
	private List<JobWorkExperienceDTO> workExpList = new ArrayList<JobWorkExperienceDTO>();
	
	/**
	 * 标识是否编辑状态
	 */
	private boolean edit = true;

	/**
	 * 右菜单按钮
	 */
	private Menu menu;

	/**
	 * 日期格式化
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");

	private EmptyTipView emptyView;

	@AfterViews
	void bindView() {

		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activity_title_work_experience));

		workExpAdapter = new WorkExpAdapter();
		emptyView = new EmptyTipView(this);
		workExpListView.setEmptyView(emptyView);
		workExpListView.setAdapter(workExpAdapter);
		workExpListView.setOnItemClickListener(this);
		workExpListView.setOnRefreshListener(this);
		// 加载工作经验
		loadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(workExpList!=null && workExpList.size()>0){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.action_edit, menu);
		}
		this.menu = menu;
		return true;
	}

	/**
	 * 加载工作经验列表
	 */
	@Background
	public void loadData() {
		jobUservice.findJobWorkExperienceDTOByUserId(ZcdhApplication.getInstance().getZcdh_uid()).identify(kREQ_ID_findJobWorkExperienceDTOByUserId = RequestChannel.getChannelUniqueID(), this);
	}

	/**
	 * 更新工作经验
	 */
	@Background
	void delWorkExp(long wepPostId) {
		jobUservice.removeJobWorkExperiencePost(wepPostId).identify(kREQ_ID_removeJobWorkExperiencePost = RequestChannel.getChannelUniqueID(), this);
	}

	@Click(R.id.addWorkExpBtn)
	void onAddWorkExp() {
		WorkExpAddActivity_.intent(this).startForResult(WorkExpAddActivity.kREQUEST_WORK_EXP);
	}

	@OnActivityResult(WorkExpAddActivity.kREQUEST_WORK_EXP)
	void onResultAddWorkExp(int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			loadData();
			ResumeActivity.FLAG_REFRESH = 1;
		}
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i("TAG","workExpListView onItemClick :"+ workExpList.size() + "---" + position + "-->" + workExpList.get(position-1).getWepPostId());
		WorkExpAddActivity_.intent(this).jobWorkExperienceId(workExpList.get(position-1).getWepPostId()).startForResult(WorkExpAddActivity.kREQUEST_WORK_EXP);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.delImgBtn) {
			final long wpid = (Long) v.getTag();

			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle("删除");
			ab.setIcon(android.R.drawable.ic_dialog_alert);
			ab.setMessage("确认删除");
			ab.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					delWorkExp(wpid);
				}
			});
			ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ab.create().show();
		}
	}

	@OptionsItem(android.R.id.home)
	void onBack() {
		finish();
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
			this.edit = false;
		}
		setMenuState();
	}

	/**
	 * 改变菜单按钮
	 * 
	 * @param edit
	 */
	void setMenuState() {
		MenuItem rightMenuItem = menu.getItem(0);
		if (edit) {
			rightMenuItem.setTitle(getString(R.string.cancel));
			this.edit = false;
		} else {
			rightMenuItem.setTitle(getString(R.string.edit));
			this.edit = true;
		}
		workExpAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		// 获得工作列表
		if (reqId.equals(kREQ_ID_findJobWorkExperienceDTOByUserId)) {
			if (result != null) {
				workExpList = (List<JobWorkExperienceDTO>) result;
			} else {
				workExpList = new ArrayList<JobWorkExperienceDTO>();
			}
			
			emptyView.isEmpty(!(workExpList.size()>0));
			workExpAdapter.notifyDataSetChanged();
			workExpListView.onRefreshComplete();
			supportInvalidateOptionsMenu();
		}
		// 删除工作经验
		if (reqId.equals(kREQ_ID_removeJobWorkExperiencePost)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					this.edit = false;
					setMenuState();
					loadData();
					ResumeActivity.FLAG_REFRESH = 1;
				}
			}
		}

	}

	@Override
	public void onRequestFinished(String reqId) {
		emptyView.isEmpty(!(workExpList.size()>0));
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException)error, this);
	}

	class WorkExpAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return workExpList != null ? workExpList.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return workExpList != null ? workExpList.get(position) : position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder h = null;
			if (convertView == null) {
				h = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.work_exp_item, null);
				h.companyNameText = (TextView) convertView.findViewById(R.id.companyNameText);
				h.subTitelText = (TextView) convertView.findViewById(R.id.subTitleText);
				h.delBtn = (LinearLayout) convertView.findViewById(R.id.delImgBtn);
				h.delBtn.setOnClickListener(WorkExperienceActivity.this);
				h.accesoryImg = (ImageView) convertView.findViewById(R.id.accessoryImg);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}

			JobWorkExperienceDTO workExp = workExpList.get(position);

			String commpanyName = workExp.getCorName();

			String postName = workExp.getPostName();

			String startTime = workExp.getStartTime() != null ? sdf.format(workExp.getStartTime()) : "";

			String endTime = workExp.getEndTime() != null ? sdf.format(workExp.getEndTime()) : "";

			String subTitle = String.format("%s / %s~%s", postName, startTime, endTime);

			h.companyNameText.setText(commpanyName);
			h.subTitelText.setText(subTitle);
			h.delBtn.setTag(workExp.getWepPostId());
			Log.i("delBtn tag", workExp.getWepPostId() + "");

			if (edit) {
				h.accesoryImg.setVisibility(View.VISIBLE);
				h.delBtn.setVisibility(View.GONE);
			} else {
				h.accesoryImg.setVisibility(View.GONE);
				h.delBtn.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		class ViewHolder {
			TextView companyNameText;
			TextView subTitelText;
			LinearLayout delBtn;
			ImageView accesoryImg;
		}
	}
}
