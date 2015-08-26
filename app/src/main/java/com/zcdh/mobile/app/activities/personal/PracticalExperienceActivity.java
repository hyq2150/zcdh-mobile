package com.zcdh.mobile.app.activities.personal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobPracticeDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

@EActivity(R.layout.activity_practical_experience)
public class PracticalExperienceActivity extends BaseActivity implements OnRefreshListener<ListView>,RequestListener,OnItemClickListener, OnClickListener, DataLoadInterface{

	private IRpcJobUservice jobUservice;
	private String kREQ_ID_findJobPracticeList;
	private String kREQ_ID_removeJobPractice;
	/**
	 * 实践经验列表
	 */
	@ViewById(R.id.practicalExpListView)
	PullToRefreshListView practicalExpListView;
	
	private PracticalExpAdapter practicalExpAdapter;
	
	/**
	 * 实践经历
	 */
	private List<JobPracticeDTO > practicalExpList = new ArrayList<JobPracticeDTO>();
	/**
	 * 标识是否编辑状态
	 */
	private boolean edit = true;

	/**
	 * 右菜单按钮
	 */
	private Menu menu;
	
	private EmptyTipView emptyView;
	
	/**
	 * 日期格式化
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
	
	
	@AfterViews
	void bindView() {

		jobUservice = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);

		SystemServicesUtils.setActionBarCustomTitle(this, getSupportActionBar(), getString(R.string.activity_title_practical_experience));

		practicalExpAdapter = new PracticalExpAdapter();
		emptyView = new EmptyTipView(this);
		practicalExpListView.setEmptyView(emptyView);
		practicalExpListView.setAdapter(practicalExpAdapter);
		practicalExpListView.setOnItemClickListener(this);
		practicalExpListView.setOnRefreshListener(this);
		// 加载实践经历
		loadData();
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
		practicalExpAdapter.notifyDataSetChanged();
	}
	
	@Click(R.id.addPracticalExpBtn)
	void onAddWorkExp() {
		PracticalExpAddActivity_.intent(this).startForResult(PracticalExpAddActivity.kREQUEST_PRACTICAL_EXP);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(practicalExpList!=null && practicalExpList.size()>0){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.action_edit, menu);
		}
		this.menu = menu;
		return true;
	}
	
	/**
	 * 加载实践经历列表
	 */
	@Background
	public void loadData() {
		jobUservice.findJobPracticeList(ZcdhApplication.getInstance().getZcdh_uid()).identify(kREQ_ID_findJobPracticeList = RequestChannel.getChannelUniqueID(), this);
	}
	
	/**
	 * 更新实践经历
	 */
	@Background
	void delPracticalExp(long wepPostId) {
		jobUservice.removeJobPractice(wepPostId).identify(kREQ_ID_removeJobPractice = RequestChannel.getChannelUniqueID(),this);
	}
	
	@Override
	public void onRequestStart(String reqId) {
		// TODO Auto-generated method stub
		
	}

	@OnActivityResult(PracticalExpAddActivity.kREQUEST_PRACTICAL_EXP)
	void onResultAddPracticalExp(int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			loadData();
			ResumeActivity.FLAG_REFRESH = 1;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		// TODO Auto-generated method stub
		// 获得实践经历列表
		if (reqId.equals(kREQ_ID_findJobPracticeList)) {
			if (result != null) {
				practicalExpList = (List<JobPracticeDTO>) result;
			}else {
				practicalExpList = new ArrayList<JobPracticeDTO>();
			}	
			emptyView.isEmpty(!(practicalExpList.size() > 0));
			practicalExpAdapter.notifyDataSetChanged();
			practicalExpListView.onRefreshComplete();
			supportInvalidateOptionsMenu();
		}
		// 删除实践经历
		if (reqId.equals(kREQ_ID_removeJobPractice)) {
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
		// TODO Auto-generated method stub
		emptyView.isEmpty(!(practicalExpList.size()>0));
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		// TODO Auto-generated method stub
		emptyView.showException((ZcdhException)error, this);
	}

	class PracticalExpAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return practicalExpList != null ? practicalExpList.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return practicalExpList != null ? practicalExpList.get(position) : position;
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
				h.practicalNameText = (TextView) convertView.findViewById(R.id.companyNameText);
				h.practicalTimeText = (TextView) convertView.findViewById(R.id.subTitleText);
				h.delBtn = (LinearLayout) convertView.findViewById(R.id.delImgBtn);
				h.delBtn.setOnClickListener(PracticalExperienceActivity.this);
				h.accesoryImg = (ImageView) convertView.findViewById(R.id.accessoryImg);
				convertView.setTag(h);
			} else {
				h = (ViewHolder) convertView.getTag();
			}

			JobPracticeDTO workExp = practicalExpList.get(position);

			h.practicalNameText.setText(workExp.getPractice_name());
			try {
				h.practicalTimeText.setText(sdf.format(workExp.getStart_time()) + " ~ " + sdf.format(workExp.getEnd_time()));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			h.delBtn.setTag(workExp.getPractice_id());

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
			TextView practicalNameText;
			TextView practicalTimeText;
			LinearLayout delBtn;
			ImageView accesoryImg;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.delImgBtn) {
			final long wpid = (Long) v.getTag();

			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle("删除");
			ab.setIcon(android.R.drawable.ic_dialog_alert);
			ab.setMessage("确认删除");
			ab.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					delPracticalExp(wpid);
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


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub practicalExperiencePostDTO
		PracticalExpAddActivity_.intent(this).practicalExperiencePostDTO(practicalExpList.get(position-1)).startForResult(PracticalExpAddActivity.kREQUEST_PRACTICAL_EXP);
	}


	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		loadData();
	}
}
