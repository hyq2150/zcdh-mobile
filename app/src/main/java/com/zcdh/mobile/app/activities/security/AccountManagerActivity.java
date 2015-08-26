package com.zcdh.mobile.app.activities.security;

import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobUservice;
import com.zcdh.mobile.api.model.ImgAttachDTO;
import com.zcdh.mobile.api.model.ThirdImgUrlDTO;
import com.zcdh.mobile.api.model.UserAccountManagerDTO;
import com.zcdh.mobile.app.Constants;
import com.zcdh.mobile.app.ZcdhApplication;
import com.zcdh.mobile.app.activities.register.VerificationCodeActivity_;
import com.zcdh.mobile.app.dialog.GenericDialog;
import com.zcdh.mobile.app.dialog.ProcessDialog;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.framework.nio.RequestChannel;
import com.zcdh.mobile.framework.nio.RequestListener;
import com.zcdh.mobile.utils.FileIoUtil;
import com.zcdh.mobile.utils.ImageUtils;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.StringUtils;
import com.zcdh.mobile.utils.SystemServicesUtils;

import net.tsz.afinal.bitmap.download.SimpleDownloader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.mob.tools.utils.R.getStringRes;

//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.openapi.models.User;

/**
 * 
 * @author yangjiannan 账号管理
 * 
 */
@EActivity(R.layout.activity_account_manage)
public class AccountManagerActivity extends BaseActivity implements
		RequestListener, Callback {

	private static final String TAG = AccountManagerActivity.class
			.getSimpleName();

	private List<UserAccountManagerDTO> userAccountManagerDTOs = new ArrayList<UserAccountManagerDTO>();

	/**
	 * 修改头像
	 */
	private String kREQ_ID_updateImgInUserHomePage;
	/**
	 * 用第三方修改头像
	 */
	private String kREQ_ID_bindThirdPortrait;

	/**
	 * 账号管理信息
	 */
	private String kREQ_ID_findUserAccountManagerDTOByUserId;

	/**
	 * 查找用户头像
	 */
	private String kREQ_ID_findThirdImgURLDTOByUserId;

	/**
	 * 绑定第三方账号
	 */
	private String kREQ_ID_bindQQAccount;

	private String kREQ_ID_bindWeiboAccount;

	private String kREQ_ID_bindWechatAccount;

	private String kREQ_ID_unbindQQAccount;

	private String kREQ_ID_unbindWeiboAccount;

	private String kREQ_ID_unbindWechatAccount;

	private IRpcJobUservice userService;

	// 正在处理...
	private ProcessDialog processDialog;

	/* ======================= 手机和电邮账号相关 =============== */
	/**
	 * 手机号码账号
	 */
	@ViewById(R.id.mobileAccountText)
	TextView mobileAccountText;

	/**
	 * 修改密码那一行
	 */
	@ViewById(R.id.passwdRowRl)
	RelativeLayout passwdRowRl;

	@ViewById(R.id.bindEmailText)
	TextView bindEmailText;

	/**
	 * 邮箱账号
	 */
	@ViewById(R.id.emailAccountText)
	TextView emailAccountText;

	/**
	 * 微博绑定状态
	 */
	@ViewById(R.id.weiboStatusText)
	TextView weiboStatusText;

	/**
	 * qq 绑定状态
	 */
	@ViewById(R.id.qqStatusText)
	TextView qqStatusText;
	/**
	 * 微信 绑定状态
	 */
	@ViewById(R.id.wechatStatusText)
	TextView wechatStatusText;

	private String mobile = null;
	private String email = null;

	private static final String BUND_TYPE_WECHAT = "weChat";
	private static final String BUND_TYPE_QQ = "QQ";
	private static final String BUND_TYPE_WEIBO = "weiBo";

	private String qqOpenId = null;
	private String weiboOpenId = null;
	private String wechatOpenId = null;

	/* ======================= 第三方账号相关 =============== */
	// 解除绑定对话框
	private GenericDialog unBindDialog;

	private ThirdPartAuthHelper authHelper;
	// 绑定第三方时返回的openId
	private String bindOpenId = null;

	// 正在解除绑定的第三方
	private String unbindType;

	/* ==================== 修改头像 ======================= */

	private GenericDialog modifyUserPhotoDialog;

	/**
	 * 用户头像
	 */
	private Bitmap modifiedUserPhoto;

	@Extra
	String userPhotoUrl;

	@Extra
	boolean gotoModifyPhoto;

	@ViewById(R.id.userPhotoImg)
	RoundedImageView userPhotoImg;

	/**
	 * 修改头像标注
	 */
	@ViewById(R.id.editHeadBtn)
	ImageView editHeadBtn;

	private List<ThirdImgUrlDTO> thirdImgUrlDTOs;

	/**
	 * 标识当前验证是为了 拿到第三方头像
	 */

	private boolean isModifyUserPhoto;

	private String qq_photo_url;
	private String weibo_photo_url;
	private String wechat_photo_url;

	private File tempFile;
	private DisplayImageOptions options;

	@AfterViews
	void bindViews() {
		userService = RemoteServiceManager
				.getRemoteService(IRpcJobUservice.class);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.considerExifParams(true).build();

		SystemServicesUtils.setActionBarCustomTitle(getApplicationContext(),
				getSupportActionBar(), "账号管理");

		processDialog = new ProcessDialog(this);
		unBindDialog = new GenericDialog(this, R.layout.unbind_account_dialog);
		unBindDialog.setCancelable(true);

		authHelper = new ThirdPartAuthHelper(this, new AuthResultCallback() {

			@Override
			public void onAuthResult(String openId, String authType) {
				if (isModifyUserPhoto) { // 为了拿到第三方头像而验证
					// modifyUserPhoto_BindValue = openId;

				} else { // 为了绑定第三方账号而验证

					bindOpenId = openId;
					// 绑定qq账号
					if (Constants.LOGIN_TYPE_QQ.equals(authType)) {
						bindQQAccount(openId);
					}

					// 绑定weibo账号
					if (Constants.LOGIN_TYPE_WEIBO.equals(authType)) {
						bindWeiboAccount(openId);
					}
					// getUserPhotoInThird(authType, openId);
				}

			}
		});

		// 显示头像
		if (userPhotoUrl != null) {
			editHeadBtn.setVisibility(View.VISIBLE);
			new AsyncTask<Void, Void, Bitmap>() {

				@Override
				protected Bitmap doInBackground(Void... params) {
					try {
						return ImageUtils.GetBitmapByUrl(userPhotoUrl);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				protected void onPostExecute(Bitmap result) {
					if (result != null)
						userPhotoImg.setImageBitmap(result);
				};

			}.execute();

		} else {
			editHeadBtn.setVisibility(View.GONE);
		}

		// 账号信息
		if (!gotoModifyPhoto) {
			processDialog.show();
		}
		findAccountsInfo();

		if (gotoModifyPhoto) {
			findUserHeadPhoto();
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
		AccountManagerActivity.this.startActivityForResult(intent,
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (processDialog != null && processDialog.isShowing()) {
			processDialog.dismiss();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (processDialog != null && processDialog.isShowing()) {
			processDialog.dismiss();
		}
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
				modifiedUserPhoto = data.getParcelableExtra("data");

				// 图像保存到文件中
				FileOutputStream foutput = null;
				try {
					File temp = getTempFile(this, "crop_temp.PNG");
					foutput = new FileOutputStream(temp);
					modifiedUserPhoto.compress(Bitmap.CompressFormat.PNG, 100,
							foutput);
					processDialog.show("请稍后...");
					modifyUserPhoto(temp.getPath());
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
		} else {
			// 第三方验证回调
			authHelper.setActivityResult(requestCode, resultCode, data);
		}

	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

	}

	/**
	 * 绑定手机回调
	 */
	@OnActivityResult(Constants.kREQUEST_BIND_MOBILE)
	void onResultBindMobile(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			UserAccountManagerDTO accountManagerDTO = (UserAccountManagerDTO) data
					.getSerializableExtra("account");
			userAccountManagerDTOs.add(accountManagerDTO);
			showAccountInfo();
		}
	}

	/**
	 * 绑定邮箱回调
	 */
	@OnActivityResult(Constants.kREQUEST_BIND_EMAIL)
	void onResultBindEmail(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			UserAccountManagerDTO accountManagerDTO = (UserAccountManagerDTO) data
					.getSerializableExtra("account");
			userAccountManagerDTOs.add(accountManagerDTO);
			showAccountInfo();
		}
	}

	/**
	 * 修改用户头像
	 */
	@Click(R.id.userPhotoImg)
	void onModifyUserPhoto() {
		// modifyUserPhotoDialog.show();
		// processDialog.show();
		findUserHeadPhoto();
	}

	/**
	 * 修改qq头像
	 */
	void onModifyUserPhotoInQQ() {
		modifyUserPhotoDialog.dismiss();
		processDialog.show();
		modifyUserPhotoInThird(qq_photo_url);
	}

	/**
	 * 修改weibo头像
	 */
	void onModifyUserPhotoInWeibo() {
		modifyUserPhotoDialog.dismiss();
		processDialog.show();
		modifyUserPhotoInThird(weibo_photo_url);
	}

	/**
	 * 修改wechat头像
	 */
	void onModifyUserPhotoInWechat() {
		modifyUserPhotoDialog.dismiss();
		processDialog.show();
		modifyUserPhotoInThird(wechat_photo_url);
	}

	/**
	 * 绑定手机
	 * 
	 */
	@Click(R.id.mobileRowRl)
	void onBandMobile() {
		VerificationCodeActivity_.intent(this).isBindMobile(mobile == null)
				.isModifyMobileNo(mobile != null)
				.startForResult(Constants.kREQUEST_BIND_MOBILE);

	}

	/**
	 * 绑定邮箱
	 * 
	 */
	@Click(R.id.emailRowRl)
	void onBandEmail() {
		BindEmailActivity_.intent(this).update(!StringUtils.isBlank(email))
				.updateEmail(email)
				.startForResult(Constants.kREQUEST_BIND_EMAIL);
	}

	/**
	 * 绑定qq
	 */
	@Click(R.id.qqRowRl)
	void onBindQQAccount() {
		if (qqOpenId != null) {
			unBindDialog.show();
			unbindType = BUND_TYPE_QQ;
		} else {
			processDialog.show();
			authHelper.requestTencentQQAuth();
		}
	}

	/**
	 * 绑定微信
	 */
	@Click(R.id.wechatRowRl)
	void onBindWechatAccount() {
		if (wechatOpenId != null) {
			unBindDialog.show();
			unbindType = BUND_TYPE_WECHAT;
		} else {
			processDialog.show();
			authHelper.requestWechatAuth(this);
		}
	}

	/**
	 * 绑定微博
	 */
	@Click(R.id.weiboRowRl)
	void onBindWeiboAccount() {
		if (weiboOpenId != null) {
			unBindDialog.show();
			unbindType = BUND_TYPE_WEIBO;
		} else {
			processDialog.show();
			authHelper.requestSinaWeiboAuth();
		}
	}

	/**
	 * 对话框按钮单击事件
	 * 
	 * @param view
	 */
	public void onButtonClick(View view) {
		Log.i(TAG, "buttonClick");
		switch (view.getId()) {
		case R.id.capturePhotoLL:
			modifyUserPhotoDialog.dismiss();
			takePhoto();
			break;

		case R.id.galleryPhotoLL:
			modifyUserPhotoDialog.dismiss();
			openGallery();
			break;
		case R.id.unBindBtn:
			unBindDialog.dismiss();

			boolean shouldUnbind = false;
			if (mobile != null) {
				shouldUnbind = true;
			} else {
				if (qqOpenId != null && weiboOpenId != null
						&& wechatOpenId != null) {
					shouldUnbind = true;
				}
			}
			if (shouldUnbind) {
				processDialog.show();
				if (BUND_TYPE_QQ.equals(unbindType)) {
					unbindQQAccount();
				}
				if (BUND_TYPE_WEIBO.equals(unbindType)) {
					unbindWeiBoAccount();
				}
				if (BUND_TYPE_WECHAT.equals(unbindType)) {
					unbindWechatAccount();
				}
			} else {
				Toast.makeText(this, "解除绑定失败，请先绑定手机号码并设置密码", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.cancelUnbindBtn:
			unBindDialog.dismiss();
			break;
		case R.id.qqPhotoLL:
			onModifyUserPhotoInQQ();
			break;
		case R.id.weiboPhotoLL:
			onModifyUserPhotoInWeibo();
			break;

		}
		// modifyUserPhotoDialog.dismiss();
	}

	/**
	 * 修改密码
	 */
	@Click(R.id.passwdRowRl)
	public void onUpdatePwd() {
//		UpdatePwdActivity_.intent(this).start();
	}

	/**
	 * 获取照片
	 *//*
	 @Override protected void onCroped(String fullFileName) {
	 processDialog.show("请稍后..."); modifyUserPhoto(fullFileName); }

	 @Override protected void CropedImgFile(String filePath) {

	 }

	 @Override protected void CropedImg(Bitmap bitmap) { modifiedUserPhoto =
	 bitmap; }
*/
	/**
	 * 账号管理信息
	 */
	@Background
	void findAccountsInfo() {
		userService
				.findUserAccountManagerDTOByUserId(getUserId())
				.identify(
						kREQ_ID_findUserAccountManagerDTOByUserId = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 查找用户头像
	 */
	@Background
	void findUserHeadPhoto() {
		userService
				.findThirdImgURLDTOByUserId(getUserId())
				.identify(
						kREQ_ID_findThirdImgURLDTOByUserId = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 修改头像
	 * 
	 * @param fileName
	 */
	@Background
	void modifyUserPhoto(String fileName) {
		Log.i(TAG, "modifyUserPhoto:" + fileName);
		File imgFile = new File(fileName);
		ImgAttachDTO imgAttachDTO = new ImgAttachDTO();
		try {
			imgAttachDTO.setFileBytes(FileIoUtil.read(imgFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		imgAttachDTO.setFileExtension("jpg");
		imgAttachDTO.setFileName(fileName);
		imgAttachDTO.setFileSize(imgFile.length());
		imgAttachDTO.setUserId(ZcdhApplication.getInstance().getZcdh_uid());
		userService
				.updateImgInUserHomePage(imgAttachDTO)
				.identify(
						kREQ_ID_updateImgInUserHomePage = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 用第三方修改头像
	 */
	@Background
	void modifyUserPhotoInThird(String url) {
		if (!TextUtils.isEmpty(url)) {
			byte[] bytes = new SimpleDownloader().download(url);
			if (bytes != null && bytes.length > 0) {
				modifiedUserPhoto = BitmapFactory.decodeByteArray(bytes, 0,
						bytes.length);

				ImgAttachDTO imgAttachDTO = new ImgAttachDTO();
				imgAttachDTO.setFileExtension("png");
				imgAttachDTO.setUserId(ZcdhApplication.getInstance()
						.getZcdh_uid());

				imgAttachDTO.setFileBytes(bytes);
				userService
						.updateImgInUserHomePage(imgAttachDTO)
						.identify(
								kREQ_ID_updateImgInUserHomePage = RequestChannel
										.getChannelUniqueID(),
								this);
			}
		}
	}

	/**
	 * 用第三方的头像更新
	 */
	@Background
	void bindThirdPhoto(String bindType, String bindValue, String imgUrl) {
		ImgAttachDTO headImg = null;
		if (!StringUtils.isBlank(imgUrl)) {
			byte[] fileBytes = new SimpleDownloader().download(imgUrl);
			if (fileBytes != null && fileBytes.length > 0) {
				headImg = new ImgAttachDTO();
				headImg.setFileBytes(fileBytes);
				headImg.setFileExtension("png");
				headImg.setUserId(getUserId());
			}
		}
		userService
				.bindThirdPortrait(bindType, bindValue, headImg)
				.identify(
						kREQ_ID_bindThirdPortrait = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 绑定第三方账号 绑定类型 mobile,email,weiBo,weChat,QQ
	 */
	@Background
	void bindQQAccount(String openId) {
		userService.bindAccount(getUserId(), BUND_TYPE_QQ, openId).identify(
				kREQ_ID_bindQQAccount = RequestChannel.getChannelUniqueID(),
				this);
	}

	@Background
	void bindWeiboAccount(String openId) {
		userService.bindAccount(getUserId(), BUND_TYPE_WEIBO, openId).identify(
				kREQ_ID_bindWeiboAccount = RequestChannel.getChannelUniqueID(),
				this);
	}

	@Background
	void bindWechatAccount(String openId) {
		// TODO Auto-generated method stub
		userService
				.bindAccount(getUserId(), BUND_TYPE_WECHAT, openId)
				.identify(
						kREQ_ID_bindWechatAccount = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Background
	void unbindWeiBoAccount() {
		userService
				.unBindAccount(getUserId(), BUND_TYPE_WEIBO, weiboOpenId)
				.identify(
						kREQ_ID_unbindWeiboAccount = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Background
	void unbindWechatAccount() {
		userService
				.unBindAccount(getUserId(), BUND_TYPE_WECHAT, wechatOpenId)
				.identify(
						kREQ_ID_unbindWechatAccount = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	@Background
	void unbindQQAccount() {
		userService
				.unBindAccount(getUserId(), BUND_TYPE_QQ, qqOpenId)
				.identify(
						kREQ_ID_unbindQQAccount = RequestChannel
								.getChannelUniqueID(),
						this);
	}

	/**
	 * 展示账号信息
	 */
	void showAccountInfo() {

		for (UserAccountManagerDTO accountManagerDTO : userAccountManagerDTOs) {
			// mobile,email,weiBo,weChat,QQ
			// 是否绑定手机
			String bindType = accountManagerDTO.getBindType();
			String bindValue = accountManagerDTO.getBindValue();
			if ("mobile".equals(bindType)) {
				if (!StringUtils.isBlank(bindValue)) {
					mobile = bindValue;
					mobileAccountText.setText(bindValue);
				} else {
					mobileAccountText.setText("未绑定");
				}
			}

			if ("email".equals(bindType)) {

				if (!StringUtils.isBlank(bindValue)) {
					email = bindValue;
					emailAccountText.setText(bindValue);
					bindEmailText.setText("修改邮箱");
					if (accountManagerDTO.getStatus() != null
							&& accountManagerDTO.getStatus() == 1) {

					}
				} else {
					emailAccountText.setText("未绑定");
					bindEmailText.setText("绑定邮箱");
				}

			}

			if (BUND_TYPE_WEIBO.equals(bindType)) {
				if (!StringUtils.isBlank(bindValue)) {
					weiboOpenId = bindValue;
					weiboStatusText.setText("已绑定");
				} else {
					weiboOpenId = null;
					weiboStatusText.setText("未绑定");
				}
			}

			if (BUND_TYPE_QQ.equals(bindType)) {
				if (!StringUtils.isBlank(bindValue)) {
					qqOpenId = bindValue;
					qqStatusText.setText("已绑定");
				} else {
					qqStatusText.setText("未绑定");
				}
			}

			if (BUND_TYPE_WECHAT.equals(bindType)) {
				if (!StringUtils.isBlank(bindValue)) {
					wechatOpenId = bindValue;
					wechatStatusText.setText("已绑定");
				} else {
					wechatStatusText.setText("未绑定");
				}
			}

		}

		if (email != null || mobile != null) {
			passwdRowRl.setVisibility(View.VISIBLE);
		} else {
			passwdRowRl.setVisibility(View.GONE);
		}
		// passwdRowRl.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示第三方的头像，供用户选择
	 */
	void showThirdImg() {

		modifyUserPhotoDialog = new GenericDialog(AccountManagerActivity.this,
				R.layout.modify_user_photo_dialog);
		modifyUserPhotoDialog.setCancelable(true);
		modifyUserPhotoDialog.findViewById(R.id.qqPhotoLL).setVisibility(
				View.GONE);
		modifyUserPhotoDialog.findViewById(R.id.weiboPhotoLL).setVisibility(
				View.GONE);
		modifyUserPhotoDialog.findViewById(R.id.wechatPhotoLL).setVisibility(
				View.GONE);
		if (thirdImgUrlDTOs != null) {

			for (int i = 0; i < thirdImgUrlDTOs.size(); i++) {
				ThirdImgUrlDTO imgUrlDTO = thirdImgUrlDTOs.get(i);
				String type = imgUrlDTO.getBindType();
				String imgUrl = imgUrlDTO.getBindPortrait().getBig();
				if (BUND_TYPE_QQ.equals(type)) { // qq 头像
					ImageLoader.getInstance().displayImage(
							imgUrl,
							(ImageView) modifyUserPhotoDialog
									.findViewById(R.id.qqPhoto), options);
					modifyUserPhotoDialog.findViewById(R.id.qqPhotoLL)
							.setVisibility(View.VISIBLE);
					qq_photo_url = imgUrl;
				}
				if (BUND_TYPE_WEIBO.equals(type)) { // 微博头像
					ImageLoader.getInstance().displayImage(
							imgUrl,
							(ImageView) modifyUserPhotoDialog
									.findViewById(R.id.weiboPhoto), options);
					modifyUserPhotoDialog.findViewById(R.id.weiboPhotoLL)
							.setVisibility(View.VISIBLE);
					weibo_photo_url = imgUrl;
				}
				if (BUND_TYPE_WECHAT.equals(type)) {
					ImageLoader.getInstance().displayImage(
							imgUrl,
							(ImageView) modifyUserPhotoDialog
									.findViewById(R.id.wechatPhoto), options);
					modifyUserPhotoDialog.findViewById(R.id.wechatPhotoLL)
							.setVisibility(View.VISIBLE);
					wechat_photo_url = imgUrl;
				}
			}
		}
		modifyUserPhotoDialog.show();

	}

	/**
	 * 在list中移除绑定
	 */
	private void removeBindAccountInList(String remove_open_id) {
		for (int i = 0; i < userAccountManagerDTOs.size(); i++) {
			UserAccountManagerDTO dto = userAccountManagerDTOs.get(i);
			if (remove_open_id.equals(dto.getBindValue())) {
				dto.setBindValue(null);
				// 删除根据open_id 自动登录
				SharedPreferencesUtil.putValue(this,
						Constants.KLOGIN_THIRD_ACCOUNT, null);

				String auth_type = SharedPreferencesUtil.getValue(this,
						Constants.kLOGIN_AUTH_TYPE, null);
				if (auth_type != null) {
					if (Constants.kAUTH_TYPE_QQ.equals(auth_type)
							|| Constants.kAUTH_TYPE_SINA_WEIBO
									.equals(auth_type)
							|| Constants.kAUTH_TYPE_WECHAT.equals(auth_type)) { // 如果是用第三方登录支付
						String openId = SharedPreferencesUtil.getValue(this,
								Constants.KLOGIN_THIRD_ACCOUNT, null);
						if (remove_open_id.equals(openId)) {
							SharedPreferencesUtil.putValue(this,
									Constants.KLOGIN_THIRD_ACCOUNT, null);
						}
					}
				}
			}
		}
	}

	/**
	 * 获取第三方的头像
	 * 
	 * @param login_type
	 */
	private void getUserPhotoInThird(String login_type, final String bindValue) {
		if (authHelper.isSessionValid(login_type)) {
			processDialog.show();
			if (Constants.LOGIN_TYPE_QQ.equals(login_type)) {
				authHelper.getUserInfoInQQ(new IUiListener() {

					@Override
					public void onError(UiError arg0) {
						if (processDialog != null && processDialog.isShowing()) {
							processDialog.dismiss();
						}
					}

					@Override
					public void onComplete(Object response) {
						JSONObject json = (JSONObject) response;
						Log.i(TAG, response + "");
						if (json.has("figureurl_qq_2")) {
							try {
								// 获取第三方账户头像
								String url = json.getString("figureurl_qq_2");

								// 同时用获取的头像，创建账号
								Log.i(TAG, url);
								bindThirdPhoto(BUND_TYPE_QQ, bindValue, url);

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onCancel() {
						if (processDialog != null && processDialog.isShowing()) {
							processDialog.dismiss();
						}
					}

				});

			}
			if (BUND_TYPE_WECHAT.equals(login_type)) {
				// 同时用获取的头像，创建账号
				bindThirdPhoto(BUND_TYPE_WECHAT, bindValue,
						SharedPreferencesUtil.getValue(this,
								Constants.HEAD_IMG_URL, ""));

			}
			if (Constants.LOGIN_TYPE_WEIBO.equals(login_type)) {
				authHelper
						.getUserInfoInWeibo(new com.sina.weibo.sdk.net.RequestListener() {

							@Override
							public void onWeiboException(WeiboException exc) {
								Log.e(TAG, exc.getMessage());
								if (processDialog != null && processDialog.isShowing()) {
									processDialog.dismiss();
								}
							}

							@Override
							public void onComplete(String response) {
								if (!TextUtils.isEmpty(response)) {
									User user = User
										.parse(response);
									Log.i(TAG, "微博头像 ："
											+ user.profile_image_url);
									bindThirdPhoto("weiBo", bindValue,
											user.profile_image_url);
									// 同时用获取的头像，创建账号

								}
							}
						});
			}
		}
	}

	@Override
	public void onRequestStart(String reqId) {

	}

	@Override
	public void onRequestSuccess(String reqId, Object result) {

		// 账号管理信息
		if (reqId.equals(kREQ_ID_findUserAccountManagerDTOByUserId)) {
			if (result != null) {
				userAccountManagerDTOs = (List<UserAccountManagerDTO>) result;
				showAccountInfo();
			}
		}

		if (reqId.equals(kREQ_ID_updateImgInUserHomePage)) {
			if (result != null) {
				int resultCode = (Integer) result;
				if (resultCode == 0) {
					userPhotoImg.setImageBitmap(modifiedUserPhoto);
					editHeadBtn.setVisibility(View.VISIBLE);
					Intent intent = new Intent(Constants.kACTION_MODIFIED_PHOTO);
					sendBroadcast(intent);
				} else {
					Toast.makeText(this, "修改头像失败", Toast.LENGTH_SHORT).show();
				}
				if (tempFile != null)
					tempFile.delete();
			}
		}

		if (reqId.equals(kREQ_ID_bindThirdPortrait)) {
			if (result != null) {
				int resultCode = (Integer) result;
				if (resultCode == 0) {

					// editHeadBtn.setVisibility(View.VISIBLE);
					// Intent intent = new Intent(kACTION_MODIFIED_PHOTO);
					// sendBroadcast(intent);
					Log.i(TAG, "上传绑定的第三方账号的头像成功");
				} else {
					// Toast.makeText(this, "改头像失败", Toast.LENGTH_SHORT).show();
					Log.e(TAG, "上传绑定的第三方账号的头像失败");
				}
			}
		}

		// 绑定qq 账号
		if (reqId.equals(kREQ_ID_bindQQAccount)) {
			if (result != null) {
				int resultCode = (Integer) result;
				// 0 成功,-1 绑定失败,2 手机号码已经存在,3 邮箱已经存在,9 qq 已经绑定， 10 微博已经绑定
				if (resultCode == 0) {
					UserAccountManagerDTO accountManagerDTO = new UserAccountManagerDTO();
					accountManagerDTO.setBindType(BUND_TYPE_QQ);
					accountManagerDTO.setBindValue(bindOpenId);
					userAccountManagerDTOs.add(accountManagerDTO);
					showAccountInfo();
					getUserPhotoInThird(BUND_TYPE_QQ, bindOpenId);
					Toast.makeText(this, "绑定QQ账号成功", Toast.LENGTH_SHORT).show();
				}
				if (resultCode == -1) {
					Toast.makeText(this, "绑定QQ账号失败", Toast.LENGTH_SHORT).show();
				}
				if (resultCode == 9) {
					Toast.makeText(this, "该QQ已被其他账号绑定", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
		// 綁定微信账号
		if (reqId.equals(kREQ_ID_bindWechatAccount)) {
			if (result != null) {
				int resultCode = (Integer) result;
				// 0 成功,-1失败， 44 微博已经绑定
				if (resultCode == 0) {
					UserAccountManagerDTO accountManagerDTO = new UserAccountManagerDTO();
					accountManagerDTO.setBindType(BUND_TYPE_WECHAT);
					accountManagerDTO.setBindValue(bindOpenId);
					userAccountManagerDTOs.add(accountManagerDTO);
					showAccountInfo();
					getUserPhotoInThird(BUND_TYPE_WECHAT, bindOpenId);
					Toast.makeText(this, "绑定微信账号成功", Toast.LENGTH_SHORT).show();
				}
				if (resultCode == -1) {
					Toast.makeText(this, "绑定微信账号失败", Toast.LENGTH_SHORT).show();
				}
				if (resultCode == 44) {
					Toast.makeText(this, "该微信已被其他账号绑定", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
		// 绑定微博账号
		if (reqId.equals(kREQ_ID_bindWeiboAccount)) {
			if (result != null) {
				int resultCode = (Integer) result;
				// 0 成功,-1 绑定失败,2 手机号码已经存在,3 邮箱已经存在,9 qq 已经绑定， 10 微博已经绑定
				if (resultCode == 0) {
					UserAccountManagerDTO accountManagerDTO = new UserAccountManagerDTO();
					accountManagerDTO.setBindType(BUND_TYPE_WEIBO);
					accountManagerDTO.setBindValue(bindOpenId);
					userAccountManagerDTOs.add(accountManagerDTO);
					showAccountInfo();
					getUserPhotoInThird(BUND_TYPE_WEIBO, bindOpenId);
					Toast.makeText(this, "绑定微博账号成功", Toast.LENGTH_SHORT).show();
				}
				if (resultCode == -1) {
					Toast.makeText(this, "绑定微博账号失败", Toast.LENGTH_SHORT).show();
				}
				if (resultCode == 10) {
					Toast.makeText(this, "该微博已被其他账号绑定", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}

		if (reqId.equals(kREQ_ID_unbindQQAccount)) {
			int resultCode = (Integer) result;
			if (resultCode == 0) {
				Toast.makeText(this, "解除QQ绑定成功", Toast.LENGTH_SHORT).show();
				removeBindAccountInList(qqOpenId);
				qqOpenId = null;
				showAccountInfo();
			} else {
				Toast.makeText(this, "解除QQ绑定失败", Toast.LENGTH_SHORT).show();
			}
		}
		if (reqId.equals(kREQ_ID_unbindWechatAccount)) {
			int resultCode = (Integer) result;
			if (resultCode == 0) {
				Toast.makeText(this, "解除微信绑定成功", Toast.LENGTH_SHORT).show();
				removeBindAccountInList(wechatOpenId);
				wechatOpenId = null;
				showAccountInfo();
			} else {
				Toast.makeText(this, "解除微信绑定失败", Toast.LENGTH_SHORT).show();
			}
		}
		if (reqId.equals(kREQ_ID_unbindWeiboAccount)) {
			int resultCode = (Integer) result;
			if (resultCode == 0) {
				Toast.makeText(this, "解除微博绑定成功", Toast.LENGTH_SHORT).show();
				removeBindAccountInList(weiboOpenId);
				weiboOpenId = null;
				showAccountInfo();
			} else {
				Toast.makeText(this, "解除微博绑定失败", Toast.LENGTH_SHORT).show();
			}
		}

		// 第三方头像列表
		if (reqId.equals(kREQ_ID_findThirdImgURLDTOByUserId)) {
			if (result != null) {
				thirdImgUrlDTOs = (List<ThirdImgUrlDTO>) result;
			}
			showThirdImg();
		}

	}

	@Override
	public void onRequestFinished(String reqId) {

		if (reqId.equals(kREQ_ID_findUserAccountManagerDTOByUserId))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}
		if (reqId.equals(kREQ_ID_updateImgInUserHomePage))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}
		if (reqId.equals(kREQ_ID_bindQQAccount))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}
		if (reqId.equals(kREQ_ID_bindWeiboAccount))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}
		if (reqId.equals(kREQ_ID_bindWechatAccount))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}

		if (reqId.equals(kREQ_ID_unbindQQAccount))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}
		if (reqId.equals(kREQ_ID_unbindWeiboAccount))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}
		if (reqId.equals(kREQ_ID_unbindWechatAccount))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}
		if (reqId.equals(kREQ_ID_bindThirdPortrait))
			if (processDialog != null && processDialog.isShowing()) {
				processDialog.dismiss();
			}
		// if (reqId.equals(kREQ_ID_findThirdImgURLDTOByUserId))
		// processDialog.dismiss();

	}

	@Override
	public void onRequestError(String reqID, Exception error) {
		if (processDialog != null && processDialog.isShowing()) {
			processDialog.dismiss();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case Constants.MSG_USERID_FOUND:
			Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constants.MSG_LOGIN:
			bindOpenId = (String) msg.obj;
			// 绑定wechat账号
			bindWechatAccount(bindOpenId);
			break;
		case Constants.MSG_AUTH_CANCEL:
			Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT)
					.show();
			break;
		case Constants.MSG_AUTH_ERROR:
			// 失败
			String expName = msg.obj.getClass().getSimpleName();
			if ("WechatClientNotExistException".equals(expName)
					|| "WechatTimelineNotSupportedException".equals(expName)
					|| "WechatFavoriteNotSupportedException".equals(expName)) {
				int resId = getStringRes(this, "wechat_client_inavailable");
				if (resId > 0) {
					showNotification(this.getString(resId));
				}
			}
			break;
		case Constants.MSG_AUTH_COMPLETE:
			SharedPreferencesUtil.putValue(this, Constants.HEAD_IMG_URL,
					(String) msg.obj);
			Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT)
					.show();
			break;
		}
		return false;
	}

	// 在状态栏提示分享操作
	private void showNotification(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
