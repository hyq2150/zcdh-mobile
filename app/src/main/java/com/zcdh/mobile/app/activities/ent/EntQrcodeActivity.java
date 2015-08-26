package com.zcdh.mobile.app.activities.ent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zcdh.mobile.R;
import com.zcdh.mobile.api.IRpcJobEnterpriseService;
import com.zcdh.mobile.app.qrcode.QRcodeGenerator;
import com.zcdh.mobile.framework.activities.BaseActivity;
import com.zcdh.mobile.framework.nio.RemoteServiceManager;
import com.zcdh.mobile.utils.SystemServicesUtils;

/**
 * 企业二维码
 * 
 * @author YJN
 *
 */
@EActivity(R.layout.activity_ent_qrcode)
public class EntQrcodeActivity extends BaseActivity {

	IRpcJobEnterpriseService enterpriseService;
	/**
	 * 企业logo
	 */
	@ViewById(R.id.entLogoImg)
	ImageView entLogoImg;

	/**
	 * 显示二维码
	 */
	@ViewById(R.id.qrcodeImg)
	ImageView qrcodeImg;

	@ViewById(R.id.entNameText)
	TextView entNameText;

	/**
	 * 企业id
	 */
	@Extra
	String qrcodecontent;

	@Extra
	String entLogo;

	@Extra
	String entName;

	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.considerExifParams(true).build();
	}

	@AfterViews
	void bindView() {
		SystemServicesUtils.setActionBarCustomTitle(EntQrcodeActivity.this,
				getSupportActionBar(), "企业二维码");
		enterpriseService = RemoteServiceManager
				.getRemoteService(IRpcJobEnterpriseService.class);

		if (!TextUtils.isEmpty(entName)) {
			entNameText.setText(entName);
		}

		if (!TextUtils.isEmpty(entLogo)) {
			ImageLoader.getInstance()
					.displayImage(entLogo, entLogoImg, options);
		}

		Bitmap qrBitmap = QRcodeGenerator.create(qrcodecontent); // QRcodeGenerator.Create2DCode(qrcodecontent,
																	// width
																	// ,height);
		qrcodeImg.setImageBitmap(qrBitmap);
	}

}
