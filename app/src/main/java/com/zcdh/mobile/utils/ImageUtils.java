package com.zcdh.mobile.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.exceptions.FileException;
import com.zcdh.mobile.utils.FileSplit.FileSplitInputStream;
import com.zcdh.mobile.utils.FileSplit.FileSplitOutputStream;

public class ImageUtils {

	public static final String sdcard_save_path = Environment
			.getExternalStorageDirectory() + "/Zcdh";
	public static String sdcard_save_img_path = sdcard_save_path + "/image";
	public static String sdcard_save_temp_img_path = sdcard_save_path
			+ "/image/temp";

	public static final int send_server_img_max_size = 1024 * 100;// 发送到服务器图片最大的大小
	public static final int memory_show_img_max_size = 1024 * 60;// 在内存中显示的图片最大的大小

	public static final int bitmap_max_thread_size = 3;// 加载图片最大线程数
	public static final String bitmap_cache_dir = "zcdhImgCache";// 缓存路径名

	public static Bitmap zoomImage(Bitmap image, int widthNew, int heightNew) {
		int width = image.getWidth();
		int height = image.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) widthNew) / width;
		float scaleHeight = ((float) heightNew) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(image, 0, 0, width, height, matrix,
				true);
		return bitmap;
	}

	/**
	 * 获取图片流
	 * 
	 * @param uri
	 *            图片地址
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public static InputStream GetImageByUrl(String uri)
			throws MalformedURLException {
		URL url = new URL(uri);
		Log.e("ImageUtils", "url : " + url.toString());
		URLConnection conn;
		InputStream is;
		try {
			conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();

			// 或者用如下方法

			// is=(InputStream)url.getContent();
			return is;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static InputStream GetImageByUrl2(String uri, String path)
			throws IOException {

		File dir = new File(path);
		dir.mkdirs();

		// 文件分割样例

		try {
			// 打开一个分割流：尝试从本地，如果本地无响应的文件，则会抛出IOException
			FileSplitInputStream fsin = FileSplit.getInputStream(dir,
					"username", "password", uri);
			return fsin;
		} catch (IOException e) {
			// 分割数据流到文件片段，即从服务器下载图片并分割保存在本地
			FileSplitOutputStream fsout = FileSplit.getOutputStream(dir,
					"username", "password", uri);
			InputStream is = GetImageByUrl(uri);// 获取服务器文件输出流
			fsout.split(is);// 分割该输出流并保存在分割文件片段中
			return fsout.getInputStream();// 快捷返回一个读取流
		}

	}

	/**
	 * 获取Bitmap
	 * 
	 * @param uri
	 *            图片地址
	 * @return
	 */
	public static Bitmap GetBitmapByUrl(String url) {

		Bitmap bitmap;
		InputStream is;
		try {

			is = GetImageByUrl(url);

			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			return bitmap;

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 获取Drawable
	 * 
	 * @param uri
	 *            图片地址
	 * @return
	 */
	public static File GetImageFileByUrl(String uri, String savePath) {

		File file = null;
		InputStream is;
		try {

			is = GetImageByUrl(uri);
			file = FileIoUtil.writeExt(is, savePath);
			is.close();

			return file;

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (FileException e) {

			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取Drawable
	 * 
	 * @param uri
	 *            图片地址
	 * @return
	 */
	public static Drawable GetDrawableByUrl(String uri) {

		Drawable drawable;
		InputStream is;
		try {

			is = GetImageByUrl(uri);

			drawable = Drawable.createFromStream(is, "src");

			is.close();

			return drawable;

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 计算图像尺寸
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 图像原始高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static Bitmap getBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {

		// 首先设置 inJustDecodeBounds=true 来检查尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// 计算压缩比例
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// 设置inJustDecodeBounds为false
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap getBitmapFromResource(Resources res, int resId) {
		// 首先设置 inJustDecodeBounds=true 来检查尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 计算压缩比例
		options.inSampleSize = 1;

		// 设置inJustDecodeBounds为false
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static Bitmap getBitmapFromUrl(String url, String savePath,
			int reqWidth, int reqHeight) {
		Bitmap result = null;
		File imgFile = null;
		// 文件参数
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			imgFile = GetImageFileByUrl(url, savePath);

			if (imgFile != null) {
				BitmapFactory.decodeStream(new FileInputStream(imgFile), null,
						options);

				// 计算压缩比例
				options.inSampleSize = calculateInSampleSize(options, reqWidth,
						reqHeight);
				// options.inSampleSize = 33;

				// 设置inJustDecodeBounds为false
				options.inJustDecodeBounds = false;

				result = BitmapFactory.decodeStream(
						new FileInputStream(imgFile), null, options);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Bitmap getBitmapFromUrl(String url, String savePath) {
		Bitmap result = null;
		// 文件参数
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			// imgFile=GetImageFileByUrl(url, foldername, fileName);
			// 计算压缩比例
			options.inSampleSize = 1;
			// options.inSampleSize = 33;

			// 设置inJustDecodeBounds为false
			options.inJustDecodeBounds = false;

			InputStream in = GetImageByUrl2(url, savePath);
			result = BitmapFactory.decodeStream(in, null, options);

			// if(imgFile!=null){
			// result= BitmapFactory.decodeStream( new FileInputStream(imgFile),
			// null, options);
			// } else {
			// return null;
			// }

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Bitmap getBitmapFromSdcard(final String ImageFileName,
			int reqWidth, int reqHeight) {
		Bitmap result = null;
		// 文件参数
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		if (FileIoUtil.isFileExist(ImageFileName)) {
			result = BitmapFactory.decodeFile(ImageFileName, options);
		} else {
			return null;
		}
		// 计算压缩比例
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// 设置inJustDecodeBounds为false
		options.inJustDecodeBounds = false;

		if (FileIoUtil.isFileExist(ImageFileName)) {
			result = BitmapFactory.decodeFile(ImageFileName, options);
		}
		return result;
	}

	public static Bitmap getBitmapFromSdcard(final String ImageFileName) {
		Bitmap result = null;
		// 文件参数
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 计算压缩比例
		options.inSampleSize = 1;
		// 设置inJustDecodeBounds为false
		options.inJustDecodeBounds = false;

		if (FileIoUtil.isFileExist(ImageFileName)) {
			result = BitmapFactory.decodeFile(ImageFileName, options);
		} else {
			return null;
		}
		return result;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
								: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	public static int getResourceByPicName(String picName, Context context) {
		int id = context.getResources().getIdentifier(
				picName == null ? "failure" : picName, "drawable",
				context.getPackageName());
		if (id <= 0) {
			id = R.drawable.failure;
		}
		return id;
	}

	public static void main() {
		// decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100,
		// 100));

	}

	/**
	 * 图片按照给定的质量大小进行压缩
	 * 
	 * @param image
	 *            图像对象
	 * @param maxImageSize
	 *            压缩后最大图像
	 * @return
	 * @author test7, 2013-3-20 上午11:06:00
	 */
	public static InputStream compressImageByQualityToStream(Bitmap image,
			int maxImageSize) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		System.out.println("按质量缩小的图片原始大小：" + baos.toByteArray().length);
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 5;// 每次都减少10
		}
		System.out.println("按质量缩小后的图片大小1：" + baos.toByteArray().length);
		byte[] buff = baos.toByteArray();
		ByteArrayInputStream isBm = new ByteArrayInputStream(buff);// 把压缩后的数据baos存放到ByteArrayInputStream中

		return isBm;
	}

	/**
	 * 图片按照给定的质量大小进行压缩
	 * 
	 * @param image
	 *            图像对象
	 * @param maxImageSize
	 *            压缩后最大图像
	 * @return
	 * @author test7, 2013-3-20 上午11:06:00
	 */
	public static Bitmap compressImageByQuality(Bitmap image, int maxImageSize) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		System.out.println("按质量缩小的图片原始大小：" + baos.toByteArray().length);
		int options = 100;
		while (baos.toByteArray().length / 1024 > maxImageSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		System.out.println("按质量缩小后的图片大小1：" + baos.toByteArray().length);
		byte[] buff = baos.toByteArray();
		// ByteArrayInputStream isBm = new
		// ByteArrayInputStream(buff);//把压缩后的数据baos存放到ByteArrayInputStream中
		// System.out.println("按质量缩小后的图片大小2："+baos.toByteArray().length);
		// BufferedOutputStream bos=new BufferedOutputStream(baos);

		// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,
		// null);//把ByteArrayInputStream数据生成图片
		Bitmap bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
		try {
			FileIoUtil.write(sdcard_save_img_path + File.separator + "001.jpg",
					new ByteArrayInputStream(buff));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 依据指定文件名对图片按比例大小压缩(先进行比例压缩，再进行图像质量压缩)
	 * 
	 * @param srcPath
	 *            图片文件路径
	 * @param w
	 *            图片宽度
	 * @param h
	 *            图片高度
	 * @return
	 * @author test7, 2013-3-20 上午10:59:35
	 */
	public static Bitmap compressImage(String srcPath, float width, float height) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		/*
		 * float height = 800f;//这里设置高度为800f float width = 480f;//这里设置宽度为480f
		 */
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / width);
		} else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		bitmap = compressImageByQuality(bitmap, send_server_img_max_size);

		return bitmap;// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 依据指定文件名对图片按比例大小压缩(先进行比例压缩，再进行图像质量压缩)
	 * 
	 * @param srcPath
	 *            图片文件路径
	 * @param w
	 *            图片宽度
	 * @param h
	 *            图片高度
	 * @return
	 * @author test7, 2013-3-20 上午10:59:35
	 */
	public static File compressImageToFile(String srcPath, float width,
			float height) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		/*
		 * float height = 800f;//这里设置高度为800f float width = 480f;//这里设置宽度为480f
		 */
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / width);
		} else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		InputStream is = compressImageByQualityToStream(bitmap,
				send_server_img_max_size);
		File imgeFile = null;
		try {
			imgeFile = FileIoUtil.writeExt(is, srcPath);
		} catch (FileException e) {
			e.printStackTrace();
			return null;
		}
		return imgeFile;// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 依据Bitmap图片，对图片按比例大小压缩方法
	 * 
	 * @param image
	 *            图片对象(先进行比例压缩，再进行图像质量压缩)
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @return
	 * @author test7, 2013-3-20 上午11:10:56
	 */
	public static Bitmap compressImage(Bitmap image, float width, float height) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		/*
		 * //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为 float hh = 800f;//这里设置高度为800f float
		 * ww = 480f;//这里设置宽度为480f
		 */
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / width);
		} else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / height);
		}

		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

		return compressImageByQuality(bitmap, send_server_img_max_size);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 依据Bitmap图片，对图片按比例大小压缩方法
	 * 
	 * @param image
	 *            图片对象(先进行比例压缩，再进行图像质量压缩)
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @return
	 * @author test7, 2013-3-20 上午11:10:56
	 */
	public static File compressImageToFile(Bitmap image, float width,
			float height) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		/*
		 * //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为 float hh = 800f;//这里设置高度为800f float
		 * ww = 480f;//这里设置宽度为480f
		 */
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / width);
		} else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / height);
		}

		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

		InputStream is = compressImageByQualityToStream(bitmap,
				send_server_img_max_size);
		File imgeFile = null;
		try {
			String filepath = sdcard_save_temp_img_path;
			String filename = new Date().getTime() + ".jpg";
			imgeFile = FileIoUtil.writeExt(is, filepath + filename);
		} catch (FileException e) {
			e.printStackTrace();
			return null;
		}

		return imgeFile;// 压缩好比例大小后再进行质量压缩
	}

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		// return _bmp;
		return output;
	}

}
