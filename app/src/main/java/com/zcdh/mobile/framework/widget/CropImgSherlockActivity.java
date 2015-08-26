/**
 * 
 * @author YJN, 2013-11-23 上午9:39:18
 */
package com.zcdh.mobile.framework.widget;

import com.zcdh.mobile.app.views.photopicker.CropImage;
import com.zcdh.mobile.app.views.photopicker.InternalStorageContentProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 取图片范围 1)实现CropImgActivity抽象类 2)调用 openGallery 或 takePicture 启动取图片 3）实现
 * CropedImg(Bitmap bitmap) 或 CropedImgFile(String filePath) 获取图片
 * 
 * @author YJN, 2013-11-23 上午9:39:18
 * 
 */
public abstract class CropImgSherlockActivity extends
	AppCompatActivity {

	protected static final String TAG = CropImgSherlockActivity.class
			.getSimpleName();

	protected static final String TEMP_PHOTO_FILE_NAME1 = "temp_photo1.jpg";

	protected static final int REQUEST_CODE_GALLERY = 0x1;
	protected static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	//
	protected static final int REQUEST_CODE_CROP_IMAGE = 0x3;
	// 临时文件
	protected File tempFile;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initDefaultCrop();
	}

	protected abstract void onCroped(String fullFileName);

	protected abstract void CropedImgFile(String filePath);

	/**
	 * 获得剪切后的图片
	 * 
	 * @return
	 * @author YJN, 2013-11-23 上午9:51:07
	 */
	protected abstract void CropedImg(Bitmap bitmap);

	/**
	 * 获得剪切后的图片，如果有多张
	 * 
	 * @return
	 * @author YJN, 2013-11-23 上午9:51:07
	 */
	/*
	 * abstract Bitmap getCropedImgList();
	 */
	// abstract void initTempFile();

	/**
	 * 初始化默认，在Activity 中只需一张时候；建立临时文件
	 * 
	 * @author YJN, 2013-11-23 上午9:48:01
	 */
	protected void initDefaultCrop() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			tempFile = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME1);
		} else {
			tempFile = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME1);
		}
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
		Log.i(TAG, tempFile + "");
		// String tempFielPath = getFilesDir() + File.separator +
		// "temp"+File.separator+
		// "databases"+File.separator+"temp_photo1.jpg";
		// //tempFile = new File("/mnt/sdcard/temp_photo1.jpg");
		// tempFile = new File(tempFielPath);
	}

	/* ========================== 图片处理 ========================= */
	public void takePicture() {

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
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Log.d(TAG, "cannot take picture", e);
		}
	}

	public void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	public void startCropImage() {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, tempFile.getPath());
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode :" + (resultCode == RESULT_OK));
		if (resultCode == RESULT_OK) {

			Bitmap bitmap;

			switch (requestCode) {

			case REQUEST_CODE_GALLERY:

				try {

					InputStream inputStream = getContentResolver()
							.openInputStream(data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(
							tempFile);
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

					return;
				}
				CropedImgFile(path);

				bitmap = BitmapFactory.decodeFile(tempFile.getPath());
				// tempFile.setImageBitmap(bitmap);
				CropedImg(bitmap);

				// 触发监听器
				onCroped(path);
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

}
