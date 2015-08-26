package com.zcdh.mobile.app.activities.personal;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.ImgAttachDTO;
import com.zcdh.mobile.api.model.JobUserHomePageDTO;
import com.zcdh.mobile.api.model.JobUserHomePageMiddleDTO;
import com.zcdh.mobile.api.model.JobUserHomePageTitleDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.base.BaseFragment;
import com.zcdh.mobile.app.activities.settings.SettingsHomeActivity_;
import com.zcdh.mobile.framework.activities.FWPhotoPickerActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.FileIoUtil;
import com.zcdh.mobile.utils.StringUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * 个人中心
 * 
 * @author yangjiannan
 * 
 */
@EFragment(R.layout.fragment_my_home)
public class FragmentMyHome extends BaseFragment implements
		RequestListener {

	private static final String TAG = FragmentMyHome.class.getSimpleName();
	private String kREQ_ID_findJobUserHomePageDTO;
	private String kREQ_ID_updateImgInUserHomePage;
	private String kREQ_ID_signIn;

	private IRpcJobUservice jobUservice;

	/**
	 * 头像框
	 */
	@ViewById(R.id.headPortrait)
	RelativeLayout headPortrait;

	/**
	 * 头像图片
	 */
	@ViewById(R.id.headPortraitImg)
	ImageView headPortraitImg;

	/**
	 * 名字
	 */
	@ViewById(R.id.nameText)
	TextView nameText;

	/**
	 * 签到按钮
	 */
	@ViewById(R.id.btn_signin)
	Button signinBtn;

	@ViewById(R.id.view_count)
	TextView view_count;

	@ViewById(R.id.favorites_count)
	TextView favorites_count;

	@ViewById(R.id.comments_count)
	TextView comments_count;

	/**
	 * 个人简历信息
	 */
	@ViewById(R.id.resume_info)
	TextView resume_info;

	/**
	 * 信息中心信息
	 */
	@ViewById(R.id.infocenter_info)
	ImageView infocenter_info;

	/**
	 * 系统设置
	 */
	@ViewById(R.id.syssetting_info)
	ImageView syssetting_info;

	/**
	 * 个人主页信息
	 */
	private JobUserHomePageDTO homePageDTO;
	private JobUserHomePageMiddleDTO middleDTO;
	private JobUserHomePageTitleDTO titleDTO;
	private ImgAttachDTO imgAttachDTO;
	private DisplayImageOptions options;

	/**
	 * 获取得到相片
	 * 
	 * @param resultCode
	 * @param data
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK
				&& requestCode == FWPhotoPickerActivity.kREQUEST_PHOTO_PICKER) {
			String filePath = data.getExtras().getString(
					FWPhotoPickerActivity.kPHOTO_PICKER_IMAGE_FILE);
			File imgFile = new File(filePath);
			if (imgAttachDTO == null) {
				imgAttachDTO = new ImgAttachDTO();
			}
			try {
				imgAttachDTO.setFileBytes(FileIoUtil.read(imgFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
			imgAttachDTO.setFileExtension(filePath.substring(
					filePath.lastIndexOf(".") + 1, filePath.length()));
			imgAttachDTO.setFileName(filePath);
			imgAttachDTO.setFileSize(imgFile.length());
			imgAttachDTO.setUserId(ZcdhApplication.getInstance().getZcdh_uid());
			updatePhoto();
		}
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
	
	@AfterViews
	void bindViews() {
		jobUservice = RemoteServiceManager
				.getRemoteService(IRpcJobUservice.class);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.considerExifParams(true).build();
		loadHomeInfos();
	}

	/**
	 * 绑定数据到Views
	 */
	void bindInfoToViews() {
		if (titleDTO != null) {

			// 头像
			if (titleDTO.getImg() != null) {
				String url_string = titleDTO.getImg().getBig();
				if (!StringUtils.isBlank(url_string)) {
					ImageLoader.getInstance().displayImage(url_string,
							headPortraitImg, options);
				}
			}

			// 名字
			String name = titleDTO.getUserName();
			if (!StringUtils.isBlank(name)) {
				nameText.setText(name);
				nameText.setTextColor(getResources().getColor(
						R.color.font_color));
			} else {
				nameText.setText("名字信息不完整");
				nameText.setTextColor(getResources().getColor(R.color.grey));
			}

			// 签到
			if (titleDTO.getSignIn() != null && titleDTO.getSignIn() == 0) {
				signinBtn.setText(getActivity().getResources().getString(R.string.sign));
			} else {
				signinBtn.setText(getActivity().getResources().getString(R.string.signed));
				signinBtn.setEnabled(false);
			}

		}

		if (middleDTO != null) {
			if (middleDTO.getResumePercent() != null) {
				resume_info
						.setText("完整度 " + middleDTO.getResumePercent() + "%");
			}

			if (middleDTO.getNewInfocenter() == 0) {
				infocenter_info.setVisibility(View.GONE);
			} else {
				infocenter_info.setVisibility(View.VISIBLE);
			}

		}

	}

	/**
	 * 加载主页信息
	 */
	@Background
	void loadHomeInfos() {
		jobUservice
				.findJobUserHomePageDTO(
						ZcdhApplication.getInstance().getZcdh_uid())
				.identify(
						kREQ_ID_findJobUserHomePageDTO = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 更新头像
	 */
	@Background
	void updatePhoto() {
		jobUservice
				.updateImgInUserHomePage(imgAttachDTO)
				.identify(
						kREQ_ID_updateImgInUserHomePage = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 签到
	 */
	@Background
	void signin() {
		jobUservice.signIn(ZcdhApplication.getInstance().getZcdh_uid())
				.identify(kREQ_ID_signIn = RequestChannel.getChannelUniqueID(),
						this);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {
		if (reqId.equals(kREQ_ID_findJobUserHomePageDTO)) {
			if (result != null) {
				homePageDTO = (JobUserHomePageDTO) result;
				titleDTO = homePageDTO.getTitle();
				middleDTO = homePageDTO.getMiddle();
				bindInfoToViews();
			}
		}

		if (reqId.equals(kREQ_ID_updateImgInUserHomePage)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					// ImageUtils.getBitmapTool(getActivity()).display(head,
					// uri);
					headPortraitImg.setImageBitmap(BitmapFactory
							.decodeFile(imgAttachDTO.getFileName()));

				}
			}
		}

		if (reqId.equals(kREQ_ID_signIn)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.sign_success), Toast.LENGTH_SHORT)
							.show();
					signinBtn.setText(getActivity().getResources().getString(R.string.signed));
				}
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {

	}

	@Override
	public void onRequestError(String reqID, Exception error) {

	}

	@Click(R.id.rl_shares)
	void onClickShares() {
	}

	@Click(R.id.info_center)
	void onClickinfo_center() {
		// InfoCenter_.intent(this).start();
	}

	@Click(R.id.rl_favorites)
	void onClickFavorites() {
		FavoritePostActivity_.intent(this).start();
	}

	@Click(R.id.rl_feeds)
	void onClickFeeds() {
		SubscriptionActivity_.intent(this).start();
	}

	@Click(R.id.rl_resume)
	void onClickResume() {
		ActivityDispatcher.toMyResumeActivity(getActivity());
	}

	@Click(R.id.headPortrait)
	void onClickHead() {

		AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
		ab.setTitle("修改头像");
		ab.setNegativeButton("相册", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getActivity(),
						FWPhotoPickerActivity.class);
				intent.putExtra(FWPhotoPickerActivity.kOPEN_TYPE_KEY,
						FWPhotoPickerActivity.REQUEST_CODE_GALLERY);
				startActivityForResult(intent,
						FWPhotoPickerActivity.kREQUEST_PHOTO_PICKER);
			}
		});
		ab.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getActivity(),
						FWPhotoPickerActivity.class);
				intent.putExtra(FWPhotoPickerActivity.kOPEN_TYPE_KEY,
						FWPhotoPickerActivity.REQUEST_CODE_TAKE_PICTURE);
				startActivityForResult(intent,
						FWPhotoPickerActivity.kREQUEST_PHOTO_PICKER);
			}
		});
		ab.create().show();

	}

	@Click(R.id.btn_signin)
	void onSignin() {
		signin();
	}

	@Click(R.id.rl_setting)
	void onSetting() {
		SettingsHomeActivity_.intent(this).start();
	}

}
