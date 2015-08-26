package com.zcdh.mobile.framework.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.zcdh.mobile.app.views.photopicker.CropImage;
import com.zcdh.mobile.app.views.photopicker.InternalStorageContentProvider;

/**
 * 
 * @author yangjiannan
 * 统一的获取照片的Activity 
 * 1）从照片库中选取
 * 2）camera拍照
 * 如果你要选取照片的地方都可以调用此Activity ，
 * 调用时传入参数openType: 1）REQUEST_CODE_GALLERY 到相册中选取
 * 2）REQUEST_CODE_TAKE_PICTURE 用相机拍照选取
 */
public class FWPhotoPickerActivity extends BaseActivity {
	
	protected static final String TAG = "ZCDH_CROPED";

	protected static final String TEMP_PHOTO_FILE_NAME1 = "temp_photo1.jpg";

	public static final int REQUEST_CODE_GALLERY = 2018;
	
	public static final int REQUEST_CODE_TAKE_PICTURE = 2017;
	//
	protected static final int REQUEST_CODE_CROP_IMAGE = 2019;
	//临时文件
	protected File tempFile;
	
	public static final String kPHOTO_PICKER_IMAGE_FILE = "kPHOTO_PICKER_IMAGE_FILE";
	public static final int kREQUEST_PHOTO_PICKER = 2016;
	public static final String kOPEN_TYPE_KEY = "kOPEN_TYPE_KEY";
	
	int openType; // 
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initDefaultCrop();
		if(getIntent().getExtras()!=null){
			openType = getIntent().getExtras().getInt(kOPEN_TYPE_KEY);
		}
		if(openType==REQUEST_CODE_GALLERY){
			openGallery();
		}
		if(openType==REQUEST_CODE_TAKE_PICTURE){
			takePicture();
		}
	}
	

	/**
	 * 初始化默认，在Activity 中只需一张时候；建立临时文件
	 * 
	 * @author YJN, 2013-11-23 上午9:48:01
	 */
	/**
	 * 初始化默认，在Activity 中只需一张时候；建立临时文件
	 * 
	 * @author YJN, 2013-11-23 上午9:48:01
	 */
	protected void initDefaultCrop(){
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			tempFile = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME1);
		} else {
			tempFile = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME1);
		}
	}
	
	/* ========================== 图片处理 ========================= */
	protected void takePicture() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(tempFile);
			} else {
				/*
				 * The solution is taken from here:
				 * http://stackoverflow.com/questions
				 * /10042695/how-to-get-camera-result-as-a-uri-in-data-folder
				 */
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			Log.d(TAG, "cannot take picture", e);
		}
	}

	public void openGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	private void startCropImage() {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, tempFile.getPath());
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK || data==null) {
			finish();
			return;
		}


		switch (requestCode) {

		case REQUEST_CODE_GALLERY:

			try {

				InputStream inputStream = getContentResolver().openInputStream(data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
				copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();

				startCropImage();

			} catch (Exception e) {

				Log.e(TAG, "Error while creating temp file", e);
			}

			break;
		case REQUEST_CODE_TAKE_PICTURE:

			startCropImage();
			break;
		case REQUEST_CODE_CROP_IMAGE:

			String path = data.getStringExtra(CropImage.IMAGE_PATH);
			if (path == null) {
				finish();
				return;
			}
			Intent filePathData = new Intent();
			filePathData.putExtra(kPHOTO_PICKER_IMAGE_FILE, path);
			setResult(RESULT_OK, filePathData);
			finish();

			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}
	
}
