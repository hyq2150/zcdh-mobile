package com.zcdh.mobile.app.activities.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobPostService;
import com.zcdh.mobile.api.model.ApplyPostInfoDTO;
import com.zcdh.mobile.api.model.JobEntPostDTO;
import com.zcdh.mobile.api.model.JobEntPostDetailDTO;
import com.zcdh.mobile.api.model.JobSpecialRequirementsMatchDTO;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * @author YJN, 2014-04-16 下午4:07:34 申请职位
 */
@EActivity(R.layout.activity_post_apply)
@OptionsMenu(R.menu.action_apply)
public class PostApplyActivity extends BaseActivity implements RequestListener {

	public static final String kMSG_APPLY_STATUS = "apply_status";

	String kREQ_ID_findSpecialRequirementMatchDTO;

	String kREQ_ID_userApplyPost;
	
	private String kREQ_ID_userApplyPostExt;

	IRpcJobPostService jobPostService;

	@ViewById(R.id.scrollView)
	ScrollView scrollView;

	// 企业图标
	@ViewById(R.id.entIconImg)
	ImageView entIconImg;

	@ViewById(R.id.entNameText)
	// 企业名称
	TextView entNameText;

	// 职位名称
	@ViewById(R.id.postNameText)
	TextView postNameText;

	// 招聘人数
	@ViewById(R.id.employCountText)
	TextView employCountText;

	// 已申请人数
	@ViewById(R.id.appliedText)
	TextView appliedText;

	// 企业特殊要求
	@ViewById(R.id.specialContainerLl)
	LinearLayout specialContainerLl;

	// 企业特殊要求列表
	@ViewById(R.id.specialListView)
	ListView specialListView;

	// 补充说明
	@ViewById(R.id.remarkContainer)
	LinearLayout remarkContainer;

	@ViewById(R.id.remarkEditTxt)
	EditText remarkEditTxt;

	// 职位详细
	@Extra
	JobEntPostDetailDTO detailDTO;

	// 用户选择求职意向的来源
	@Extra
	String applyType;
	
	@Extra
	boolean isFair;
	@Extra
	long fairID;

	// 改职位需要的特殊技能
	List<JobSpecialRequirementsMatchDTO> specialSkills;

	// 用户符合的特殊技能
	List<JobSpecialRequirementsMatchDTO> mySpecialSkills;

	ApplyPostInfoDTO applyPostInfoDto;

	// 选中的
	HashMap<Integer, JobSpecialRequirementsMatchDTO> selected = new HashMap<Integer, JobSpecialRequirementsMatchDTO>();

	ArrayList<String> skills = new ArrayList<String>();

	private SpecialAdapter specialAdapter;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jobPostService = RemoteServiceManager
				.getRemoteService(IRpcJobPostService.class);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.considerExifParams(true).build();
	}

	@AfterViews
	void init() {

		// ScrollView滚动至最顶端，以显示搜索历史的ListView
		scrollView.smoothScrollBy(0, 0);

		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), getString(R.string.post_apply));

		JobEntPostDTO post = detailDTO.getEntPostDTO();
		entNameText.setText(post.getEntName());
		postNameText.setText(post.getPostAliases());

		employCountText.setText(post.getEmployCount() + "");
		appliedText.setText(post.getApplyCount() + "");

		loadData();
		showData();
	}

	// 选择特殊要求
	void onItemclick(int position) {
		JobSpecialRequirementsMatchDTO sp = specialSkills.get(position);
		if (selected.get(position) != null) {
			selected.remove(position);
		} else {
			selected.put(position, sp);
		}
		if (mySpecialSkills.contains(sp)) {
			mySpecialSkills.remove(sp);
		} else {
			mySpecialSkills.add(sp);
		}
		showData();
	}

	// 申请职位
	@OptionsItem(R.id.action_apply)
	void onApply() {
		apply();
	}

	// 显示加载的数据
	void showData() {

		if (specialSkills != null && specialSkills.size() > 0) {
			if (specialAdapter == null) {
				specialAdapter = new SpecialAdapter();
			}
			specialListView.setAdapter(new SpecialAdapter());
			specialContainerLl.setVisibility(View.VISIBLE);
		} else {
			specialContainerLl.setVisibility(View.GONE);
		}

		// 显示企业Logo
		if (applyPostInfoDto != null) {
			if (applyPostInfoDto.getEntLogo() != null
					&& !TextUtils.isEmpty(applyPostInfoDto.getEntLogo()
							.getBig())) {
				ImageLoader.getInstance().displayImage(
						applyPostInfoDto.getEntLogo().getBig(), entIconImg,
						options);
			}
		}

	}

	// 加载该职位特殊技能
	@Background
	void loadData() {
		jobPostService
				.findSpecialRequirementMatchDTO(
						detailDTO.getEntPostDTO().getPostId())
				.identify(
						kREQ_ID_findSpecialRequirementMatchDTO = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	// 申请职位
	@Background
	void apply() {
		long entId = detailDTO.getEntPostDTO().getEntId();
		long postId = detailDTO.getEntPostDTO().getPostId();
		long userId = ZcdhApplication.getInstance().getZcdh_uid();
		String content = remarkEditTxt.getText().toString();

		if (isFair) {
			jobPostService.userApplyPostExt(fairID, entId, userId, postId, content, applyType,
					mySpecialSkills).identify(kREQ_ID_userApplyPostExt = RequestChannel.getChannelUniqueID(), this);
		}else {
			jobPostService.userApplyPost(entId, userId, postId, content, applyType,
					mySpecialSkills).identify(
					kREQ_ID_userApplyPost = RequestChannel.getChannelUniqueID(),
					this);
		}
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findSpecialRequirementMatchDTO)) {
			if (result != null) {
				applyPostInfoDto = (ApplyPostInfoDTO) result;
				showData();
			}
		}
		//招聘会用户申请职位
		// 用户申请职位
		if (reqId.equals(kREQ_ID_userApplyPostExt) || reqId.equals(kREQ_ID_userApplyPost)) {
			int apply = -1;

			if (result != null) {
				apply = (Integer) result;
			}
			String msg = "";
			if (apply == 0) { // 申请成功
				msg = "申请成功";
				MyEvents.post(kMSG_APPLY_STATUS, 1);
				finish();
			}
			if (apply == 1) {// 申请失败
				msg = "申请失败";
			}
			if (apply == 14) {// 职位已经被你申请了
				msg = "职位已经被你申请了";
			}
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	/**
	 * 
	 * @author yangjiannan
	 * 
	 */

	class SpecialAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return specialSkills.size();
		}

		@Override
		public Object getItem(int p) {
			return specialSkills.get(p);
		}

		@Override
		public long getItemId(int p) {
			return p;
		}

		@Override
		public View getView(int p, View contentView, ViewGroup arg2) {

			Holder h = null;
			if (contentView == null) {
				h = new Holder();
				contentView = LayoutInflater.from(getBaseContext()).inflate(
						R.layout.special_item, null);

				h.itemName = (TextView) contentView
						.findViewById(R.id.itemNameText);
				h.checkIcon = (ImageView) contentView
						.findViewById(R.id.checkImg);
				contentView.setTag(h);
			} else {
				h = (Holder) contentView.getTag();
			}
			JobSpecialRequirementsMatchDTO jp = specialSkills.get(p);
			h.itemName.setText(jp.getSpecailValue());
			if (selected.get(p) != null) {
				h.checkIcon.setVisibility(View.VISIBLE);
			} else {
				h.checkIcon.setVisibility(View.GONE);
			}

			return contentView;
		}

		class Holder {
			TextView itemName = null;
			ImageView checkIcon = null;
		}

	}

}
