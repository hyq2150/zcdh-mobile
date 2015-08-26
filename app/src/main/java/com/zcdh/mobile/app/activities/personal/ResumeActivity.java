package com.zcdh.mobile.app.activities.personal;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.core.nio.except.ZcdhException;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.ImgAttachDTO;
import com.zcdh.mobile.api.model.ImgURLDTO;
import com.zcdh.mobile.api.model.JobUserResumeMiddleDTO;
import com.zcdh.mobile.api.model.JobUserResumeTitleDTO;
import com.zcdh.mobile.app.ActivityDispatcher;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.DataLoadInterface;
import com.zcdh.mobile.app.activities.auth.LoginHelper;
import com.zcdh.mobile.app.activities.newsmodel.NewsBrowserActivity_;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.app.views.EmptyTipView;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.framework.views.ListViewInScrollView;
import com.zcdh.mobile.utils.BitmapUtils;
import com.zcdh.mobile.utils.FileIoUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 个人简历
 * 
 * @author yangjiannan
 * 
 */
@EActivity(R.layout.activity_resume)
@OptionsMenu(R.menu.action_resume_preview)
public class ResumeActivity extends BaseActivity implements RequestListener,
		DataLoadInterface, OnRefreshListener<ScrollView> {

	private static final String TAG = ResumeActivity.class.getSimpleName();

	private String kREQ_ID_findJobUserResumeTitleDTO;
	private String kREQ_ID_findJobUserResumeMiddleDTO;
	private String kREQ_ID_updateImgInUserResume;
	private String kREQ_ID_getPreviewUrl;

	private IRpcJobUservice jobUservice;

	@ViewById(R.id.emptyTipView)
	EmptyTipView emptyTipView;

	@ViewById(R.id.content)
	LinearLayout contentView;
	@ViewById(R.id.scrollView)
	
	PullToRefreshScrollView scrollView;

	/**
	 * 简历列表
	 */
	@ViewById(R.id.resumeListView)
	ListViewInScrollView resumeListView;

	private ItemsAdapters itemsAdapter;

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
	 * 电邮
	 */
	@ViewById(R.id.emailText)
	TextView emailText;

	/**
	 * 人才类型
	 */
	@ViewById(R.id.talentsCategoryText)
	TextView talentsCategoryText;

	/**
	 * 联系电话
	 */
	@ViewById(R.id.contactText)
	TextView contactText;

	/**
	 * 提示信息
	 */
	@ViewById(R.id.tipsText)
	TextView tipsText;
	@ViewById(R.id.tipsContainer)
	RelativeLayout tipsContainer;
	@ViewById(R.id.jobStatus_content)
	TextView jobStatus_content;

	/**
	 * 简历列表标题
	 */
	private ArrayList<String> itemsTitles = new ArrayList<String>();

	/**
	 * 简历主页信息
	 */
	private JobUserResumeMiddleDTO middleDTO;
	private JobUserResumeTitleDTO titleDTO;

	/**
	 * 个人简历头像
	 */
	private ImgAttachDTO headImgAttachDTO;

	// private final String CATEGORY_CODE_JOBSTATUSES = "013";

	// private FinalDb dbTool;

	/**
	 * 标识是否刷新简历完整度, 设置为1， 标识需要刷新 。用于修改了基本信息，求职意向等等，回到我的简历主页后，需要刷新简历完整度。
	 */
	public static int FLAG_REFRESH = -1;

	private ProcessDialog processDialog;
	private DisplayImageOptions options;

	protected void onResume() {
		super.onResume();
		if (FLAG_REFRESH == 1) {
			loadData();
			FLAG_REFRESH = 0;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// dbTool = DbUtil.create(this);
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
	}

	@AfterViews
	void bindViews() {
		processDialog = new ProcessDialog(ResumeActivity.this);

		// scrollView.smoothScrollTo(0, 0);
		scrollView.getRefreshableView().smoothScrollTo(0, 0);
		scrollView.setOnRefreshListener(this);

		jobUservice = RemoteServiceManager
				.getRemoteService(IRpcJobUservice.class);
		SystemServicesUtils.setActionBarCustomTitle(this,
				getSupportActionBar(), getString(R.string.activity_resume));

		itemsTitles.add("基本信息");
		itemsTitles.add("求职意向");
		itemsTitles.add("工作经验");
		itemsTitles.add("教育经历");
		itemsTitles.add("职业技能");
		itemsTitles.add("实践经历");
		itemsTitles.add("获奖经历");
		itemsTitles.add("自我评价");
		itemsTitles.add("企业黑名单");

		itemsAdapter = new ItemsAdapters();

		resumeListView.setAdapter(itemsAdapter);

		loadData();
		// loadBodyInfo();
	}

	/**
	 * 将个人简历信息绑定到view显示
	 */
	void bindInfoToView() {

		if (titleDTO != null) {

			// 显示头像
			ImgURLDTO img = titleDTO.getImg();
			if (img != null && !StringUtils.isBlank(img.getBig())) {
				ImageLoader.getInstance().displayImage(img.getBig(),
						headPortraitImg, options);
			}

			// 名字
			if (!StringUtils.isBlank(titleDTO.getUserName())) {
				nameText.setText(titleDTO.getUserName());
				nameText.setTextColor(getResources().getColor(
						R.color.font_color));
			} else {
				nameText.setText("名字信息不完整");
				nameText.setTextColor(getResources().getColor(R.color.grey));
			}

			// email
			if (!StringUtils.isBlank(titleDTO.getEmail())) {
				emailText.setText(titleDTO.getEmail());
				emailText.setTextColor(getResources().getColor(
						R.color.font_color));
			} else {
				emailText.setText("电邮信息不完整");
				emailText.setTextColor(getResources().getColor(R.color.grey));
			}

			// 电话
			if (!StringUtils.isBlank(titleDTO.getMobile())) {
				contactText.setText(titleDTO.getMobile());
				contactText.setTextColor(getResources().getColor(
						R.color.font_color));
			} else {
				contactText.setText("电话信息不完整");
				contactText.setTextColor(getResources().getColor(R.color.grey));
			}
			// 人才类型
			if (!StringUtils.isBlank(titleDTO.getTalentTypeName())) {
				talentsCategoryText.setText(titleDTO.getTalentTypeName());
			}

			jobStatus_content.setText(String.format("%s",
					titleDTO.getJobStautsName()));
		}

		if (middleDTO != null) {

			// 提示信息
			String tips = middleDTO.getPromptContent();
			if (!StringUtils.isBlank(tips)) {
				tipsText.setText(tips);
				tipsContainer.setVisibility(View.VISIBLE);
			} else {
				tipsContainer.setVisibility(View.GONE);
			}
		}
		itemsAdapter = new ItemsAdapters();
		resumeListView.setAdapter(itemsAdapter);

	}

	/**
	 * 加载简历主页头部信息
	 */
	@Background
	public void loadData() {
		jobUservice
				.findJobUserResumeTitleDTO(getUserId())
				.identify(
						kREQ_ID_findJobUserResumeTitleDTO = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 加载简历主页主体信息,简历各项的完整度
	 */
	@Background
	void loadBodyInfo() {
		jobUservice
				.findJobUserResumeMiddleDTO(getUserId())
				.identify(
						kREQ_ID_findJobUserResumeMiddleDTO = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 获得简历预览的URL 地址
	 * 
	 */
	@Background
	void getPreviewUrl() {
		jobUservice.previewResumeByUserId(getUserId()).identify(
				kREQ_ID_getPreviewUrl = RequestChannel.getChannelUniqueID(),
				this);
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		// 加载头部信息，包括头像，名字。...
		if (reqId.equals(kREQ_ID_findJobUserResumeTitleDTO)) {
			if (result != null) {
				titleDTO = (JobUserResumeTitleDTO) result;
				bindInfoToView();
			}
			loadBodyInfo();
		}

		// 加载各项完整度
		if (reqId.equals(kREQ_ID_findJobUserResumeMiddleDTO)) {
			if (result != null) {
				middleDTO = (JobUserResumeMiddleDTO) result;
				Log.e(TAG, "加载各项完整度...." + middleDTO.getResumePercent());
				LoginHelper.getInstance(this).doCheck();
				bindInfoToView();
			}

			// 加载完所需数据，显示界面
			emptyTipView.isEmpty(false);
			contentView.setVisibility(View.VISIBLE);
		}

		// 修改头像
		if (reqId.equals(kREQ_ID_updateImgInUserResume)) {
			if (result != null) {
				int success = (Integer) result;
				if (success == 0) {
					headPortraitImg.setImageBitmap(BitmapUtils
							.compressImageFromFile(headImgAttachDTO
									.getFileName()));
				}
			}

		}

		// 简历预览url地址
		if (reqId.equals(kREQ_ID_getPreviewUrl)) {
			if (result != null) {
				String url = (String) result;
				NewsBrowserActivity_.intent(this).title("简历预览").url(url)
						.start();
			}
		}
	}

	@Override
	public void onRequestFinished(String reqId) {
		scrollView.onRefreshComplete();
		processDialog.dismiss();
	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		emptyTipView.showException((ZcdhException) error, this);
	}

	@OptionsItem(R.id.resume_preview)
	void onResumePreview() {
		getPreviewUrl();
	}

	@OptionsItem(android.R.id.home)
	void onBack() {
		// 设置Menu重新刷新
		finish();
	}

	@Click(R.id.headPortrait)
	void onPickPhoto() {
		showPickerPhotoDialog();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

		loadData();
	}

	/**
	 * 获取选择的相片
	 * 
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 拍照回调
		if (requestCode == Constants.REQUEST_CODE_TAKE_PHOTO
				|| requestCode == Constants.REQUEST_CODE_GALLERY) {
			if (resultCode == RESULT_OK) {
				Uri photoUri = null;
				if (data == null) {
					photoUri = Uri.fromFile(getTempFile(this, null));
				} else {
					photoUri = data.getData();
				}
				Log.i(TAG, "crop photo data :" + photoUri);
				cropPhoto(photoUri);
			}

		} else if (requestCode == Constants.ACTION_IMAGE_CROP) {

			if (resultCode == RESULT_OK) {

				// 拿到剪切数据
				Bitmap bitmap = data.getParcelableExtra("data");
				headPortraitImg.setImageBitmap(bitmap);
				// 图像保存到文件中
				FileOutputStream foutput = null;
				try {
					File temp = getTempFile(this, "crop_temp.PNG");
					foutput = new FileOutputStream(temp);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, foutput);
					processDialog.show("请稍后...");
					setHead2(temp.getPath());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (null != foutput) {
						try {
							foutput.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		scrollView.getRefreshableView().smoothScrollTo(0, 0);
	}

	void setHead2(final String fileName) {
		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
			};

			@Override
			protected Void doInBackground(Void... params) {
				File imgFile = new File(fileName);
				headImgAttachDTO = new ImgAttachDTO();
				try {
					headImgAttachDTO.setFileBytes(FileIoUtil.read(imgFile));
				} catch (IOException e) {
					e.printStackTrace();
				}
				headImgAttachDTO.setFileExtension(fileName.substring(
						fileName.lastIndexOf(".") + 1, fileName.length()));
				headImgAttachDTO.setFileName(fileName);
				headImgAttachDTO.setFileSize(imgFile.length());
				headImgAttachDTO.setUserId(getUserId());
				jobUservice
						.updateImgInUserResume(headImgAttachDTO)
						.identify(
								kREQ_ID_updateImgInUserResume = RequestChannel
										.getChannelUniqueID(),
								ResumeActivity.this);

				return null;
			}

		}.execute();

	}

	void showPickerPhotoDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("修改头像");
		ab.setNegativeButton("相册", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// SystemServicesUtils.userGallery(ResumeActivity.this,
				// REQUEST_CODE_CHOOSE_GALLERY);
				openGallery();
			}
		});
		ab.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// picUrl = SystemServicesUtils.useCamera(ResumeActivity.this,
				// REQUEST_CODE_TAKE_PIC);
				takePhoto();
			}
		});
		ab.create().show();
	}

	@Click(R.id.resumeHeader)
	void onHeadInfoClick() {
		ActivityDispatcher.toBasicInfo(this);
	}

	@ItemClick(R.id.resumeListView)
	void onItemClick(int position) {
		switch (position) {
		case 0://基本信息
			ActivityDispatcher.toBasicInfo(this);
			break;
		case 1://求职意向
			PurposeActivity_.intent(this).start();
			break;
		case 2://工作经验
			WorkExperienceActivity_.intent(this).start();
			break;
		case 3://教育经历
			EducationBackgroundActivity_.intent(this).start();
			break;
		case 4://职业技能
			if (middleDTO.getObjectiveFull() == 3
					|| middleDTO.getObjectiveFull() == 1) {
				ActivityDispatcher.toSkillsEdit(this);
			} else {
				Toast.makeText(this, "请设置求职意向", Toast.LENGTH_SHORT).show();
			}
			break;
		case 5://实践经历
			PracticalExperienceActivity_.intent(this).start();
			break;
		case 6://获奖经历
			WinningExperienceActivity_.intent(this).start();
			break;
		case 7://自我评价
			SelfEvaluationActivity_.intent(this).start();
			break;
		case 8://企业黑名单
			ActivityDispatcher.toBlackList(this);
			break;
		}
	}

	class ItemsAdapters extends BaseAdapter {

		@Override
		public int getCount() {
			return itemsTitles.size();
		}

		@Override
		public Object getItem(int position) {
			return itemsTitles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder h = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.resume_listview_item_accessory, null);
				h = new Holder();
				h.title = (TextView) convertView
						.findViewById(R.id.itemNameText);
				h.checkImg = (ImageView) convertView
						.findViewById(R.id.checkImg);
				h.descText = (TextView) convertView.findViewById(R.id.descText);
				convertView.setTag(h);
			} else {
				h = (Holder) convertView.getTag();
			}
			h.title.setText(itemsTitles.get(position));
			if (position == (itemsTitles.size() - 1)) {//企业黑名单
				h.descText.setVisibility(View.GONE);
				h.checkImg.setImageResource(R.drawable.greydot);
			} else if (middleDTO != null) {
				int full = -1;
				switch (position) {
				case 0://基本信息
					full = middleDTO.getBasicInfoFull();
					if (full == 0) { // 非必填不完整

						h.checkImg.setImageResource(R.drawable.reddot);

					} else if (full == 1) { // 非必填完整

						h.checkImg.setImageResource(R.drawable.reddot);

					} else if (full == 2) { // 必填项并且不完整

						h.checkImg.setImageResource(R.drawable.reddot);

					} else if (full == 3) { // 必填项并且完整

						h.checkImg.setImageResource(R.drawable.reddot);
					}
					break;
				case 1://求职意向
					full = middleDTO.getObjectiveFull();
					if (full == 0) { // 非必填不完整

						h.checkImg.setImageResource(R.drawable.greydot);

					} else if (full == 1) { // 非必填完整

						h.checkImg.setImageResource(R.drawable.greydot);

					} else if (full == 2) { // 必填项并且不完整

						h.checkImg.setImageResource(R.drawable.greydot);

					} else if (full == 3) { // 必填项并且完整

						h.checkImg.setImageResource(R.drawable.greydot);

					}
					break;
				case 2://工作经验
					full = middleDTO.getWorkExperienceFull();
					if (full == 0) { // 非必填不完整

						h.checkImg.setImageResource(R.drawable.yellowdot);

					} else if (full == 1) { // 非必填完整

						h.checkImg.setImageResource(R.drawable.yellowdot);

					} else if (full == 2) { // 必填项并且不完整

						h.checkImg.setImageResource(R.drawable.yellowdot);

					} else if (full == 3) { // 必填项并且完整

						h.checkImg.setImageResource(R.drawable.yellowdot);

					}
					break;
				case 3://教育经历
					full = middleDTO.getEduExperienceFull();
					if (full == 0) { // 非必填不完整
						h.checkImg.setImageResource(R.drawable.reddot);
					} else if (full == 1) { // 非必填完整
						h.checkImg.setImageResource(R.drawable.reddot);
					} else if (full == 2) { // 必填项并且不完整
						h.checkImg.setImageResource(R.drawable.reddot);
					} else if (full == 3) { // 必填项并且完整
						h.checkImg.setImageResource(R.drawable.reddot);
					}
					break;
				case 4://职业技能
					full = middleDTO.getTechnologyFull();
					if (full == 0) { // 非必填不完整
						h.checkImg.setImageResource(R.drawable.greendot);
					} else if (full == 1) { // 非必填完整
						h.checkImg.setImageResource(R.drawable.greendot);
					} else if (full == 2) { // 必填项并且不完整
						h.checkImg.setImageResource(R.drawable.greendot);
					} else if (full == 3) { // 必填项并且完整
						h.checkImg.setImageResource(R.drawable.greendot);
					}
					break;
				case 5://实践经历
					full = middleDTO.getPracticeFull();
					if (full == 0) {
						h.checkImg.setImageResource(R.drawable.yellowdot);
					}else if(full == 1){
						h.checkImg.setImageResource(R.drawable.yellowdot);
					}else if(full == 2){
						h.checkImg.setImageResource(R.drawable.yellowdot);
					}else if(full == 3){
						h.checkImg.setImageResource(R.drawable.yellowdot);
					}
					break;
				case 6://获奖经历
					full = middleDTO.getPrizesFull();
					if (full == 0) {
						h.checkImg.setImageResource(R.drawable.greydot);
					}else if(full == 1){
						h.checkImg.setImageResource(R.drawable.greydot);
					}else if(full == 2){
						h.checkImg.setImageResource(R.drawable.greydot);
					}else if(full == 3){
						h.checkImg.setImageResource(R.drawable.greydot);
					}
					break;
				case 7://自我评价
					full = middleDTO.getCommentMyselfFull();
					if (full == 0) { // 非必填不完整
						h.checkImg.setImageResource(R.drawable.greendot);
					} else if (full == 1) { // 非必填完整
						h.checkImg.setImageResource(R.drawable.greendot);
					} else if (full == 2) { // 必填项并且不完整
						h.checkImg.setImageResource(R.drawable.greendot);
					} else if (full == 3) { // 必填项并且完整
						h.checkImg.setImageResource(R.drawable.greendot);
					}
					break;
				}
				if (full == 2) {
					h.descText.setVisibility(View.VISIBLE);
				} else {
					h.descText.setVisibility(View.GONE);
				}
			}
			return convertView;
		}

		class Holder {
			TextView title;
			ImageView checkImg;
			TextView descText;
		}
	}

	private void takePhoto() {
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(getTempFile(this, null)));
		startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PHOTO);
	}

	private void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent,
				Constants.REQUEST_CODE_GALLERY);
	}

	private void cropPhoto(Uri data) {

		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);// 输出图片大小
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		ResumeActivity.this.startActivityForResult(intent,
				Constants.ACTION_IMAGE_CROP);
	}

	private File getTempFile(Context context, String fileName) {
		// it will return /sdcard/image.tmp
		File path = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			path = new File(Environment.getExternalStorageDirectory(),
					context.getPackageName());
		} else {
			path = new File(getFilesDir(), context.getPackageName());
		}
		if (!path.exists()) {
			path.mkdir();
		}
		if (!TextUtils.isEmpty(fileName)) {
			return new File(path, fileName);
		} else {
			Log.i(TAG, "temp ");
			return new File(path, "image.tmp");
		}
	}

}
