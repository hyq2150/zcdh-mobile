/**
 * 
 * @author jeason, 2014-4-11 下午2:09:37
 */
package com.zcdh.mobile.app.activities.detail;

import com.umeng.analytics.MobclickAgent;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.api.model.CommentDTO;
import com.zcdh.mobile.api.model.ImgURLDTO;
import com.zcdh.mobile.api.model.JobEntShareDTO;
import com.zcdh.mobile.api.model.JobEnterpriseDetailDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.activities.ent.MainEntActivity;
import com.zcdh.mobile.app.activities.ent.MainEntActivity_;
import com.zcdh.mobile.app.activities.ent.MainEntLiuyanActivity_;
import com.zcdh.mobile.app.activities.ent.MainEntProductDetailActivity_;
import com.zcdh.mobile.app.views.AlbumPreview2;
import com.zcdh.mobile.app.views.CommentItemView;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.app.views.ProductPreviewItem;
import com.zcdh.mobile.app.views.TagsContainer;
import com.zcdh.mobile.framework.events.MyEvents;
import com.zcdh.mobile.framework.events.MyEvents.Subscriber;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.share.Share;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author jeason, 2014-4-11 下午2:09:37 公司详情Fragment
 */
@EFragment(R.layout.ent_detail)
public class EntDetailFragment extends BaseFragment implements
		RequestListener, Subscriber, DataLoadInterface {

	private static final String TAG = EntDetailFragment.class.getSimpleName();

	private long ent_id;

	private IRpcJobEnterpriseService entService;

	/** request keys */
	private String kREQ_ID_FINDJOBENTERPRISEDETAILDTO;

	private String kREQ_ID_isAttentionEnt;

	private String kREQ_ID_updateAttentionEnt;

	private String kREQ_ID_findEntShareDTO;

	private JobEnterpriseDetailDTO ent_detailDTO;

	@ViewById(R.id.scrollView)
	ScrollView scrollView;

	@ViewById(R.id.contentView)
	LinearLayout contentView;

	@ViewById(R.id.emptyView)
	EmptyTipView emptyView;

	// 是否关注企业
	private int isAttentionEnt;

	/**
	 * bind views
	 */

	@ViewById(R.id.ent_name)
	TextView ent_name;

	/**
	 * 企业是否认证
	 */
	@ViewById(R.id.renzhengImg)
	ImageView renzhengImg;

	@ViewById(R.id.ent_property)
	TextView ent_property;

	@ViewById(R.id.ent_scale)
	TextView ent_scale;

	@ViewById(R.id.ent_industry)
	TextView ent_industry;

	@ViewById(R.id.ent_address)
	TextView ent_address;
	// @ViewById(R.id.tags_container)
	// LinearLayout ll_tags;

	@ViewById(R.id.tags_container)
	TagsContainer ll_tags;

	@ViewById(R.id.ent_intro)
	TextView ent_intro;

	@ViewById(R.id.ll_intro)
	LinearLayout ll_intro;

	@ViewById(R.id.rlIntroTitle)
	RelativeLayout rlIntroTitle;

	@ViewById(R.id.dividerIntro)
	View dividerIntro;

	@ViewById(R.id.ent_albums)
	AlbumPreview2 preview;

	@ViewById(R.id.ll_album)
	LinearLayout ll_album;

	@ViewById(R.id.rlAlbumTitle)
	RelativeLayout rlAlbumTitle;

	@ViewById(R.id.tv_blockOrHideIntro)
	TextView tv_blockOrHideIntro;

	@ViewById(R.id.followBtn)
	Button followBtn;

	@ViewById(R.id.tv_contact)
	TextView tv_contact;

	@ViewById(R.id.rlProductTitle)
	RelativeLayout rlProductTitle;

	@ViewById(R.id.rlCommentTitle)
	RelativeLayout rlCommentTitle;

	@ViewById(R.id.ll_products)
	LinearLayout ll_products;

	@ViewById(R.id.ll_comments)
	LinearLayout ll_comments;

	// @ViewById(R.id.homepageBtn)
	// Button homepageBtn;

	@ViewById(R.id.interactions)
	LinearLayout interactions;

	/*
	 * @ViewById(R.id.divider_last) View divider_last;
	 */

	@ViewById(R.id.ll_comment_section)
	LinearLayout ll_comment_section;

	@ViewById(R.id.ll_products_section)
	LinearLayout ll_products_section;

	@ViewById(R.id.divider_products)
	View divider_products;

	@ViewById(R.id.comment_count)
	TextView comment_count;

	// private boolean showAllIntro = false;

	private boolean hide_home = false;

	@ViewById(R.id.tv_dial)
	TextView tv_dial;

	/**
	 * 企业分享的内容
	 */
	private List<JobEntShareDTO> shareContents;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		entService = RemoteServiceManager
				.getRemoteService(IRpcJobEnterpriseService.class);

		MyEvents.register(this);

		ent_id = getArguments().getLong(Constants.kENT_ID);

		// 隐藏底部按钮
		hide_home = getArguments().getBoolean("hide_home", false);

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	public void onDestroy() {
		super.onDestroy();
		RequestChannel.removeRequestListener(
				kREQ_ID_FINDJOBENTERPRISEDETAILDTO, kREQ_ID_isAttentionEnt,
				kREQ_ID_updateAttentionEnt);
		MyEvents.unregister(this);
	}

	// 唤醒的时候检查 entId 是否有变更，因为在职位详情页会去到不同的职位，entId 也由可能变更

	@AfterViews
	void bindView() {
		loadData();
	}

	/**
	 *
	 * @author jeason, 2014-4-12 上午9:25:53 绑定Views
	 */
	private void initViews() {

		if (ent_detailDTO == null) {
			return;
		}

		emptyView.isEmpty(false);
		contentView.setVisibility(View.VISIBLE);
		// Log.i(TAG
		// ,ent_detailDTO.getIsPublicPhone()+":"+ent_detailDTO.getIsPublicMobile());
		Log.i(TAG, ent_detailDTO + "");
		if ((ent_detailDTO.getIsPublicPhone() != null && ent_detailDTO
				.getIsPublicPhone() == 1)
				|| ent_detailDTO.getIsPublicMobile() != null
				&& ent_detailDTO.getIsPublicMobile() == 1) {
			if (StringUtils.isBlank(ent_detailDTO.getMobile())
					&& StringUtils.isBlank(ent_detailDTO.getPhone())) {
				tv_dial.setVisibility(View.GONE);
			}
		} else {
			tv_dial.setVisibility(View.GONE);
		}

		// if("-".equals(ent_detailDTO.getPhone())){
		// tv_dial.setVisibility(View.GONE);
		// }

		if (!StringUtils.isBlank(ent_detailDTO.getEntName())) {
			SystemServicesUtils.setActionBarCustomTitle(getActivity(),((AppCompatActivity)getActivity()).getSupportActionBar(),
					ent_detailDTO.getEntName());
		} else {
			SystemServicesUtils.setActionBarCustomTitle(getActivity(),((AppCompatActivity)getActivity()).getSupportActionBar(),
					ent_detailDTO.getFullEntName());

		}

		this.ent_address
				.setText(StringUtils.isBlank(ent_detailDTO.getAddress()) ? "无地址信息"
						: ent_detailDTO.getAddress());

		String industry = (StringUtils.isBlank(ent_detailDTO.getIndustry())) ? "未知"
				: ent_detailDTO.getIndustry();
		this.ent_industry.setText(String.format("行业:%s", industry));

		String entName = (StringUtils.isBlank(ent_detailDTO.getEntName())) ? "未知"
				: ent_detailDTO.getFullEntName();
		this.ent_name.setText(entName);

		if (ent_detailDTO.getIsLegalize() != null
				&& ent_detailDTO.getIsLegalize() == 1) {
			renzhengImg.setVisibility(View.VISIBLE);
		} else {
			renzhengImg.setVisibility(View.GONE);
		}
		//在企业主业隐藏认证图标
		Activity hostActivity = getActivity();
		if(hostActivity instanceof MainEntActivity){
			renzhengImg.setVisibility(View.GONE);
		}

		String propertyName = (StringUtils.isBlank(ent_detailDTO
				.getPropertyName())) ? "未知" : ent_detailDTO.getPropertyName();
		this.ent_property.setText(String.format("性质:%s", propertyName));

		String employNum = (StringUtils.isBlank(ent_detailDTO.getEmployNum())) ? "未知"
				: ent_detailDTO.getEmployNum();
		this.ent_scale.setText(String.format("规模:%s", employNum));

		String contactName = (StringUtils.isBlank(ent_detailDTO
				.getContactName())) ? "未知" : ent_detailDTO.getContactName();
		this.tv_contact.setText(String.format("联系人:%s", contactName));

		comment_count.setText(String.format("共有%d条",
				ent_detailDTO.getCommentTotals()));
		// 公司产品展示
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = 5;

		// 添加产品views 如果没有则隐藏相关view
		if (ent_detailDTO.getProduct() != null) {
			ll_products.removeAllViews();
			ProductPreviewItem product = new ProductPreviewItem(getActivity());
			product.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					MainEntProductDetailActivity_.intent(getActivity())
							.productUrl(ent_detailDTO.getProduct().getUrl())
							.start();
				}
			});
			product.initView(ent_detailDTO.getProduct());
			product.setBackgroundResource(R.drawable.message_item_selector);
			ll_products.addView(product, lp);
		} else {
			ll_products.setVisibility(View.GONE);
			rlProductTitle.setVisibility(View.GONE);
			divider_products.setVisibility(View.GONE);
		}

		// 添加评论views 如果没有则隐藏相关view
		if (ent_detailDTO.getCommentDTO() == null
				|| ent_detailDTO.getCommentDTO().isEmpty()) {
			// rlCommentTitle.setVisibility(View.GONE);
			// ll_comments.setVisibility(View.GONE);
		} else {
			if (ent_detailDTO.getCommentDTO() != null
					&& !ent_detailDTO.getCommentDTO().isEmpty()) {
				ll_comments.removeAllViews();
				for (CommentDTO comment : ent_detailDTO.getCommentDTO()) {
					CommentItemView commentView = new CommentItemView(
							getActivity());
					commentView.initData(getActivity(), comment);
					ll_comments.addView(commentView, lp);
				}
			}

		}

		// 获取公司环境图片
		final List<ImgURLDTO> imgs = ent_detailDTO.getPhotos();
		ArrayList<HashMap<String, String>> imgStrings = new ArrayList<HashMap<String, String>>();

		if (imgs != null) {
			for (ImgURLDTO imgModelDTO : imgs) {
				HashMap<String, String> hashImg = new HashMap<String, String>();
				hashImg.put("img_url", imgModelDTO.getBig());
				imgStrings.add(hashImg);
			}
		} else {
			ll_album.setVisibility(View.GONE);
			rlAlbumTitle.setVisibility(View.GONE);
			preview.setVisibility(View.GONE);
		}
		preview.initData(imgStrings, new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

//				ActivityDispatcher.toPhotoBrowser(getActivity(),
//						imgs.get(position).getOriginal(), new String[] { imgs
//								.get(position).getOriginal() });
				if(imgs!=null && imgs.size()>0){
					String[] urls = new String[imgs.size()];
					int i = 0;
					for (ImgURLDTO img : imgs) {
						urls[i] = img.getOriginal();
						i++;
					}
					ActivityDispatcher.toPhotoBrowser(EntDetailFragment.this.getActivity(), imgs.get(position).getOriginal(), urls);
				}
			}
		});

		Resources r = Resources.getSystem();
		float screen_width = r.getDisplayMetrics().widthPixels
				- (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 15,
				r.getDisplayMetrics()) * 4);
		int width = (int) (screen_width / 3);
		/*
		 * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		 * screen_width / 3, r.getDisplayMetrics());
		 */
		preview.getLayoutParams().height = width;
		preview.requestLayout();

		// 公司福利标签
		ll_tags.initData(ent_detailDTO.getTagNames(), null, Gravity.LEFT);
		setIntro();

		scrollView.smoothScrollBy(0, 0);

		// 隱藏底部按鈕
		if (hide_home) {
			hideHomeBtn();
		}
	}

	@Click(R.id.more_albums)
	void onMoreAlbums() {
		ActivityDispatcher.toEntAlbum(getActivity(), ent_id);
	}

	@Click(R.id.followBtn)
	void onFollowBtn() {
		if (ZcdhApplication.getInstance().getZcdh_uid() != -1) {
			if (SystemServicesUtils.isCompleteInfo(getActivity())) {
				updateAttentionEnt(ent_id);
			}
		} else {
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.login_first), Toast.LENGTH_SHORT).show();
			ActivityDispatcher.to_login(getActivity());
		}
	}

	@Click(R.id.shareBtn)
	void onShare() {
		SystemServicesUtils.initShareSDK(getActivity());
		if (shareContents == null) {
			findSharecontent();
		} else {
//			shareEnt();
			Share.showShare(getActivity(), shareContents, false, null);
		}
	}


	@Background
	void findSharecontent() {
		entService.findEntShareDTO(ent_id).identify(
				kREQ_ID_findEntShareDTO = RequestChannel.getChannelUniqueID(),
				this);
	}

	@Background
	void updateAttentionEnt(long entId) {
		entService
				.updateAttentionEnt(
						ZcdhApplication.getInstance().getZcdh_uid(), entId)
				.identify(
						kREQ_ID_updateAttentionEnt = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 分享企业记录
	 *
	 * @param hist
	 */
	@Background
	void addShareEntHistory() {
		entService.addEntShareHistory(getUserId(), ent_id).identify(
				RequestChannel.getChannelUniqueID(), this);
	}

	void setIntro() {
		String introString = ent_detailDTO.getIntroduction();
		if (!StringUtils.isBlank(introString)) {
			ent_intro.setText(introString);
		} else {
			ll_intro.setVisibility(View.GONE);
			rlIntroTitle.setVisibility(View.GONE);
			dividerIntro.setVisibility(View.GONE);
		}

	}

	@Click(R.id.tv_comment)
	void toComment() {
		if (getUserId() != -1) {
			if (SystemServicesUtils.isCompleteInfo(getActivity())) {
				ActivityDispatcher.toComment(getActivity(), ent_id);
			}
		} else {
			ActivityDispatcher.to_login(getActivity());
		}
	}

	/**
	 *
	 * @author jeason, 2014-4-12 上午9:01:59
	 */
	@Background
	public void loadData() {
		// 联网查找详细内容
		entService
				.findJobEnterpriseDetailDTO(ent_id)
				.identify(
						kREQ_ID_FINDJOBENTERPRISEDETAILDTO = RequestChannel
								.getChannelUniqueID(),
						this);

		entService.isAttentionEnt(ZcdhApplication.getInstance().getZcdh_uid(),
				ent_id).identify(
				kREQ_ID_isAttentionEnt = RequestChannel.getChannelUniqueID(),
				this);

	}

	public void loadData(long ent_id) {
		this.ent_id = ent_id;
		loadData();
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Click(R.id.ent_address)
	void toNavigator() {
		if (!TextUtils.isEmpty(ent_detailDTO.getAddress())) {
			ActivityDispatcher.toNavigate(getActivity(),
					ent_detailDTO.getLat(), ent_detailDTO.getLon());
		}
	}
	@Click(R.id.navBtn)
	void toNavigator1() {
		if (!TextUtils.isEmpty(ent_detailDTO.getAddress())) {
			ActivityDispatcher.toNavigate(getActivity(),
					ent_detailDTO.getLat(), ent_detailDTO.getLon());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_FINDJOBENTERPRISEDETAILDTO)) {

			this.ent_detailDTO = (JobEnterpriseDetailDTO) result;

			initViews();

		}
		if (reqId.equals(kREQ_ID_isAttentionEnt)) {
			if (result != null) {
				isAttentionEnt = (Integer) result;

				if (isAttentionEnt == 0) {

					followBtn.setText("关注");
				}
				if (isAttentionEnt == 1) {

					followBtn.setText("取消关注");
				}
			}
		}

		if (reqId.equals(kREQ_ID_updateAttentionEnt)) {
			if (result != null) {
				int i = (Integer) result;
				if (i == 0) {
					String msg = "";
					if (isAttentionEnt == 0) {
						followBtn.setText("取消关注");
						msg = "关注成功";
						isAttentionEnt = 1;
					} else if (isAttentionEnt == 1) {
						followBtn.setText("关注");
						msg = "取消关注成功";
						isAttentionEnt = 0;
					}
					Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT)
							.show();
				}
			}
		}

		if (reqId.equals(kREQ_ID_findEntShareDTO)) {
			if (result != null) {
				shareContents = (List<JobEntShareDTO>) result;
				Share.showShare(getActivity(), shareContents, false, null);
			}
		}

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		SystemServicesUtils.stopShareSDK(getActivity());
		super.onDestroyView();
	}

	@Click(R.id.more_products)
	void moreProducts() {
		// ActivityDispatcher.toMoreProduct(getActivity(), ent_id);
		MainEntActivity_.intent(this).entId(ent_id).default_index(1).start();
	}

	@Click(R.id.more_comments)
	void moreComments() {
		// ActivityDispatcher.toCompanyComments(getActivity(), ent_id);
		MainEntActivity_.intent(this).entId(ent_id).default_index(2).start();
	}

	@Click(R.id.tv_dial)
	void onDial() {
		if (getUserId() != -1) {
			String num = StringUtils.isBlank(ent_detailDTO.getMobile()) ? ent_detailDTO
					.getPhone() : ent_detailDTO.getMobile();
			if (!StringUtils.isBlank(num)) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ num));
				startActivity(intent);
			}
		} else {
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.login_first_then_call), Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 留言
	 */
	@Click(R.id.msgBtn)
	void onMsgBtn() {
		if (ZcdhApplication.getInstance().getZcdh_uid() == -1) {
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.login_first_then_message), Toast.LENGTH_SHORT).show();
			ActivityDispatcher.to_login(getActivity());
		} else {
			if (SystemServicesUtils.isCompleteInfo(getActivity())) {
				MainEntLiuyanActivity_.intent(getActivity()).entId(ent_id)
						.type("001").start();
			}
		}
	}

	@Click(R.id.homepageBtn)
	void goToHomePage() {
		// ActivityDispatcher.toEnterpriseHomepage(getActivity());
		MainEntActivity_.intent(getActivity()).entId(ent_id).start();
	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyView.showException((ZcdhException) error, this);
		contentView.setVisibility(View.GONE);
	}

	@Override
	public void receive(String key, Object msg) {
		if (Constants.kMSG_ENT_CHANGED.equals(key)) {
			long id = (Long) msg;
			if (id != ent_id) {
				ent_id = id;
				// loadData();
			}
		}
	}

	private void hideHomeBtn() {
		// divider_last.setVisibility(View.GONE);
		interactions.setVisibility(View.GONE);
		ll_comment_section.setVisibility(View.GONE);
		ll_products_section.setVisibility(View.GONE);
	}

}
