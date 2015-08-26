/**
 * 
 * @author jeason, 2014-5-20 下午5:13:27
 */
package com.zcdh.mobile.app.activities.personal;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zcdh.comm.entity.Page;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.JobTechnicalDTO;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.personal.widget.SkillsDialog;
import com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.LoadingIndicator;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.adapters.PredicateAdapter;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author jeason, 2014-5-20 下午5:13:27
 * 技能编辑
 */
@EActivity(R.layout.skill_edit)
public class SkillEditActivity extends BaseActivity implements RequestListener, TagsDialogListener, DataLoadInterface {
	private static final String TAG = SkillEditActivity.class.getSimpleName();

	@ViewById(R.id.btn_add)
	Button btn_add;

	@ViewById(R.id.myTagContainer)
	TagsContainer container;

	@ViewById(R.id.skill_count)
	TextView skill_count;
	
	@ViewById(R.id.scrollView)
	PullToRefreshScrollView scrollView;
	
	@ViewById(R.id.emptyTipView)
	EmptyTipView emptyTipView;
	
	@ViewById(R.id.contentView)
	LinearLayout contentView;

	/**
	 * 技能弹出框
	 */
	SkillsDialog dialog;

	IRpcJobUservice uService;

	public String kREQ_ID_FINDTECHNICALBYSEARCHKEYWORD;

	public String kREQ_ID_FINDJOBTECHNICALSELECTEDBYUSERID;

	public String kREQ_ID_FINDJOBTECHNICALBYOBJECTIVE;

	Page<JobTechnicalDTO> mySkills;

	Page<JobTechnicalDTO> mAllTags;

	SelectedSkillsTagAdapter adapter;

	LayoutInflater inflater;

	int current_page = 1;

	private final int PAGE_SIZE = 20;
	
	boolean edit_mode = false;

	ProcessDialog loading;

	public String kREQ_ID_ADDJOBTECHNICAL;
	public String kREQ_ID_REMOVEJOBTECHNICAL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loading = new ProcessDialog(this);
		uService = RemoteServiceManager.getRemoteService(IRpcJobUservice.class);
		SystemServicesUtils.displayCustomedTitle(this, getSupportActionBar(), "添加技能");
	}

	@AfterViews
	void bindViews() {
		
		inflater = LayoutInflater.from(this);
		
		mySkills = new Page<JobTechnicalDTO>();
		mySkills.setElements(new ArrayList<JobTechnicalDTO>());
		adapter = new SelectedSkillsTagAdapter();
		container.setAdapter(this, adapter);

		dialog = new SkillsDialog(this,this);

		loadData();
	}
	
	public void loadData(){
		//联网获取我的技能标签
		getMySkills();
		//获取系统分配过来的标签
		getOrientedSkills();
		
		setCount(0);
	}

	private void setCount(int count) {
		skill_count.setText(String.format("我的技能：%d/20", count));
	}

	@Background
	void searchBasicSkills() {
		uService.findTechnicalBySearchKeyWord(keyword, current_page, PAGE_SIZE).identify(kREQ_ID_FINDTECHNICALBYSEARCHKEYWORD = RequestChannel.getChannelUniqueID(), this);
	}

	@Background
	void getMySkills() {
		uService.findJobTechnicalSelectedByUserId(getUserId(), 1, 100).identify(kREQ_ID_FINDJOBTECHNICALSELECTEDBYUSERID = RequestChannel.getChannelUniqueID(), this);
	}

	@Background
	void getOrientedSkills() {
		uService.findJobTechnicalByObjective(getUserId(), current_page, PAGE_SIZE).identify(kREQ_ID_FINDJOBTECHNICALBYOBJECTIVE = RequestChannel.getChannelUniqueID(), this);
	}

	@Click(R.id.addSkill)
	void onAdd() {
		dialog.show();
	}

	@Background
	public void onReInitialSkills(String keyword) {
		uService.findTechnicalBySearchKeyWord(keyword, 1, 10).identify(kREQ_ID_FINDTECHNICALBYSEARCHKEYWORD = RequestChannel.getChannelUniqueID(), this);
	}

	@Override
	public void onRequestStart(String reqId) {
		loading.show();
	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		loading.dismiss();
		if (result != null) {
			if (reqId.equals(kREQ_ID_FINDTECHNICALBYSEARCHKEYWORD)) {
				mAllTags = (Page<JobTechnicalDTO>) result;
				reInitialSkills(mAllTags, mySkills.getElements());
			}

			if (reqId.equals(kREQ_ID_FINDJOBTECHNICALSELECTEDBYUSERID)) {
				mySkills = (Page<JobTechnicalDTO>) result;
				reInitialSkills(mAllTags, mySkills.getElements());
			}

			if (reqId.equals(kREQ_ID_FINDJOBTECHNICALBYOBJECTIVE)) {
				mAllTags = (Page<JobTechnicalDTO>) result;
				reInitialSkills(mAllTags, mySkills.getElements());
			}
			
			if(reqId.equals(kREQ_ID_ADDJOBTECHNICAL)){
				loadData();
			}
			
		}
		contentView.setVisibility(View.VISIBLE);
		emptyTipView.isEmpty(mySkills==null);
		ResumeActivity.FLAG_REFRESH = 1;

	}

	void reInitialSkills(Page<JobTechnicalDTO> allTags, List<JobTechnicalDTO> selectedTags) {
		initialMySkills(selectedTags);
		supportInvalidateOptionsMenu();
		if (allTags != null) dialog.reInitialSkills(allTags.getElements(), selectedTags);
	}

	/**
	 * @param selectedTags
	 * @author jeason, 2014-5-21 下午2:55:52
	 */
	private void initialMySkills(List<JobTechnicalDTO> selectedTags) {
		if (selectedTags != null) {
			setCount(selectedTags.size());
			adapter.updateItems(selectedTags);
		}
	}

	private void add_skill() {
		dialog.show();
	}

	@Override
	public void onRequestFinished(String reqId) {
		loading.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		loading.dismiss();
		if(reqID.equals(kREQ_ID_REMOVEJOBTECHNICAL)){
			Toast.makeText(this, "删除技能失败", Toast.LENGTH_SHORT).show();
		}
		if(reqID.equals(kREQ_ID_FINDJOBTECHNICALSELECTEDBYUSERID)){
			emptyTipView.showException((ZcdhException)error, this);
		}
	}

	public void onSelect(JobTechnicalDTO item) {
		if (mySkills.getElements().size() >= 20) {
			Toast.makeText(this, "最多可选20个技能", Toast.LENGTH_SHORT).show();
			return;
		}
		if (item != null) {
			loading.show();
			doAddSkill(item);
		}
		mySkills.getElements().add(item);
		reInitialSkills(mAllTags, mySkills.getElements());
	}

	/**
	 * @param item
	 * @author jeason, 2014-5-23 下午12:11:06
	 */
	@Background
	void doAddSkill(JobTechnicalDTO item) {
		uService.addJobTechnical(item.getTechnicalCode(), item.getParamCode(), getUserId()).identify(kREQ_ID_ADDJOBTECHNICAL = RequestChannel.getChannelUniqueID(), this);
	}

	@Background
	void doRemoveSkill(JobTechnicalDTO item) {
		uService.removeJobTechnical(item.getId()).identify(kREQ_ID_REMOVEJOBTECHNICAL = RequestChannel.getChannelUniqueID(), this);
	}

	public void onRemoveSelect(JobTechnicalDTO item) {
		for (JobTechnicalDTO dto : mySkills.getElements()) {
			if (dto.getTechnicalCode().equals(item.getTechnicalCode())) {
				Log.i(TAG, "tech Id:" + dto.getId());
				doRemoveSkill(dto);
				mySkills.getElements().remove(dto);
				break;
			}
		}
		reInitialSkills(mAllTags, mySkills.getElements());
	}

	public void onShift() {
		if (mAllTags == null) {
			current_page = 1;
		} else if (!mAllTags.getOnMaxPage()) {
			current_page++;
		} else {
			current_page = 1;
		}
		// getAllBasicSkills();
		loading.show();

		if (StringUtils.isBlank(keyword)) {
			if (last_isSearchMode) {
				current_page = 1;
			}
			last_isSearchMode = !last_isSearchMode;
			getOrientedSkills();
		} else {
			last_isSearchMode = true;
			searchBasicSkills();
		}
	}

	private boolean last_isSearchMode = false;
	private String keyword = "";

	public void onSearchWordChanged(String content) {
		// if (keyword.equals(content)) {
		// return;
		// }

		keyword = content;

	}

	public void onSearch(String content) {
		// if (keyword.equals(content)) {
		// return;
		// }
		
		loading.show();
		current_page = 1;
		// keyword = content;
		if(TextUtils.isEmpty(content)){
			getOrientedSkills();
		}else{
			searchBasicSkills();
		}
		last_isSearchMode = true;
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		if(mySkills!=null && mySkills.getElements().size()>0){
			getMenuInflater().inflate(R.menu.action_edit, menu);
			MenuItem menu_item = menu.findItem(R.id.action_edit);
			if (edit_mode) {
				menu_item.setTitle(R.string.done);
			}
		}
		return true;
	}

	@OptionsItem(R.id.action_edit)
	void onEdit() {
		edit_mode = !edit_mode;
		supportInvalidateOptionsMenu();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener#onInitialTags()
	 */
	@Override
	public void onInitialTags() {
		onReInitialSkills("");
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener#onDialogDismiss()
	 */
	@Override
	public void onDialogDismiss() {
		
	}

	/* (non-Javadoc)
	 * @see com.zcdh.mobile.app.activities.personal.widget.TagsDialog.TagsDialogListener#onEditableConfirm(int, java.lang.String)
	 */
	@Override
	public void onEditableConfirm(int key, String content) {
		
	}

	/**
	 * 我的技能view数据源
	 * 
	 * @author jeason, 2014-5-22 下午3:09:21
	 */
	class SelectedSkillsTagAdapter extends PredicateAdapter {
	
		private List<JobTechnicalDTO> skills;
	
		public SelectedSkillsTagAdapter() {
			this.skills = new ArrayList<JobTechnicalDTO>();
			//setAddBtn();
		}
	
		public void updateItems(List<JobTechnicalDTO> items) {
			skills.clear();
			skills.addAll(items);
			//setAddBtn();
			notifyDataSetChanged();
		}
	
		public List<JobTechnicalDTO> getItems() {
			return skills;
		}
	
		/*private void setAddBtn() {
			this.skills.add(null);
			notifyDataSetChanged();
		}*/
	
		private View getAddButton() {
			View add_btn = inflater.inflate(R.layout.add_button, null);
			add_btn.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					SkillEditActivity.this.add_skill();
				}
			});
			return add_btn;
		}
	
		public void addItem(JobTechnicalDTO item) {
			skills.add(item);
			//setAddBtn();
		}
	
		@Override
		public View getView(int position, ViewGroup parentView) {
			final JobTechnicalDTO skill = skills.get(position);
			if (skill == null) {
				return getAddButton();
			}
			View view = inflater.inflate(R.layout.skill_tags_item, null);
			RelativeLayout ll_skill_tag_item = (RelativeLayout) view.findViewById(R.id.ll_skill_tag_item);
			ImageView iv_indicator = (ImageView) view.findViewById(R.id.iv_indicator);
			if (edit_mode) {
				iv_indicator.setVisibility(View.VISIBLE);
			} else {
				iv_indicator.setVisibility(View.GONE);
			}
			iv_indicator.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					if (edit_mode) {
						onRemoveSelect(skill);
					}
				}
			});
			TextView skillLevelText = (TextView) view.findViewById(R.id.skillLevelText);
			TextView skView = (TextView) view.findViewById(R.id.skillNameText);
	
			skView.setText(skill.getTechnicalName());
	
			if ("002.001".equals(skill.getParamCode())) {// 精通
				skillLevelText.setText("精");
				skillLevelText.setTextColor(getResources().getColor(R.color.tag_jingtong1));
				 skillLevelText.setBackgroundResource(R.drawable.skill_jingtong_background);
			}
			if ("002.002".equals(skill.getParamCode())) {// 熟悉
				skillLevelText.setText("熟");
				skillLevelText.setTextColor(getResources().getColor(R.color.tag_shu));
	
				 skillLevelText.setBackgroundResource(R.drawable.skill_shuxi_background);
			}
			if ("002.003".equals(skill.getParamCode())) {// 了解
				skillLevelText.setText("会");
				skillLevelText.setTextColor(getResources().getColor(R.color.tag_know));
	
				 skillLevelText.setBackgroundResource(R.drawable.skill_know_background);
			}
			if ("002.004".equals(skill.getParamCode())) { // /知道
				skillLevelText.setText("知");
				skillLevelText.setTextColor(getResources().getColor(R.color.tag_zhidao));
	
				 skillLevelText.setBackgroundResource(R.drawable.skill_zhidao_background);
			}
	
			ll_skill_tag_item.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					if (edit_mode) {
						onRemoveSelect(skill);
					}
				}
			});
	
			return view;
		}
	
		@Override
		public int getCount() {
			return skills.size();
		}
	
		@Override
		public void setLayout(ViewGroup parentView) {
			int unit = 40; // UnitTransfer.dip2px(getActivity(), 40);
			int height = skills.size() * unit;
			parentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
		}
	
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.zcdh.mobile.framework.adapters.PredicateAdapter#getMarginOffset()
		 */
		@Override
		public int getMarginOffset() {
			int margin = 20;
			return margin * 2 + 20 + 10;
		}
	
	}
}
