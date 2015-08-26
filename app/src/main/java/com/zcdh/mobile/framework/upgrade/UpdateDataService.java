package com.zcdh.mobile.framework.upgrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;

import com.zcdh.mobile.R;
import com.zcdh.mobile.framework.K;
import com.zcdh.mobile.utils.DbUtil;
import com.zcdh.mobile.utils.SharedPreferencesUtil;
import com.zcdh.mobile.utils.ZipUtils;

/**
 * 升级数据服务
 * 
 * @author yjn
 * 
 */
public class UpdateDataService {

	private Context context;

	private UpdateInfo updateInfo;
	
//	private CustomDialog dialog;

	public UpdateDataService(Activity context) {
		this.context = context;
	}

	/**
	 * 检查是否升级数据
	 * 
	 * @return
	 */
	public void checkUpgrade() {
		try {
			updateInfo = this.getUpdataInfo();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (updateInfo != null) {

			String currentVersion = SharedPreferencesUtil.getValue(context,
					K.DB.kBASIC_DB_VERSION_KEY,
					K.DB.kBASIC_DB_VERSION_KEY);
			if (!updateInfo.getVersion().equals(currentVersion)) { // 版本号不一致，进行升级数据
				globalHandler.sendEmptyMessage(UPGRADE_DIALOG);
			}else{
				globalHandler.sendEmptyMessage(DO_NOTHING);
			}
		}
	}

	/**
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
	 */
	private UpdateInfo getUpdataInfo() throws Exception {

		// 包装成url的对象
		URL url = new URL(K.Hosts.UPGRADE_DATA_URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		InputStream is = conn.getInputStream();

		if (is == null) {
			return null;
		}
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");// 设置解析的数据源
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();// 实体
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					info.setVersion(parser.nextText()); // 获取版本号
				} else if ("url".equals(parser.getName())) {
					info.setUrl(parser.nextText());
				}
				break;
			}
			type = parser.next();
		}

		return info;
	}

	/**
	 * 升级数据
	 * 
	 * @throws IOException
	 */
	public void upgradeData() throws IOException {
		String urlStr = updateInfo.getUrl();
		
		URL url = new URL(urlStr);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			DbUtil.prepareDatabase(context);
			String dbFile = DbUtil.getDbPath(context);
			String saveFolder = dbFile.substring(0, dbFile.lastIndexOf(File.separator));
			
			String downloadTmepFile = dbFile+ "temp.zip";

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(downloadTmepFile);

			int bytesRead = -1;
			byte[] buffer = new byte[512];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();
			//解压缩zip 文件
			ZipUtils.upZipFile(new File(downloadTmepFile), saveFolder);
			// 删除zip 文件
			new File(downloadTmepFile).delete();
			System.out.println("File downloaded");
		} else {
			System.out
					.println("No file to download. Server replied HTTP code: "
							+ responseCode);
		}
		httpConn.disconnect();
		
		globalHandler.sendEmptyMessage(DOWNLOADED_DATA);
	}

	private final static int UPGRADE_DIALOG = 1;
	private final static int DOWNLOADED_DATA = 2;

	protected static final int DO_NOTHING = 0;
	

	private Handler globalHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case DO_NOTHING:{
				
				//((WelcomeActivity)context).runBiz();
				
				break;
			}
			case DOWNLOADED_DATA:{
				// 保存最新的数据版本号到共享域
				SharedPreferencesUtil
						.putValue(
								context,
								K.DB.kBASIC_DB_VERSION_KEY,
								updateInfo.getVersion());
				dismissDialog();
				Toast.makeText(context, "数据更新完成", Toast.LENGTH_SHORT).show();
				//((WelcomeActivity)context).runBiz();
				break;
			}
			
			case UPGRADE_DIALOG:
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("发现有新的数据更新");
				builder.setMessage("是否更新？");
				builder.setCancelable(false);
				// Add the buttons
				builder.setPositiveButton(R.string.sure,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// User clicked OK button
								
								showDialog();
									new Thread(){
										@Override
										public void run(){
											try {
												upgradeData();
												
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}.start();
									
									
							}
						});
				builder.setNegativeButton("退出",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
								((Activity)context).finish();
							}
						});

				// Create the AlertDialog
				AlertDialog dialog = builder.create();
				
				dialog.show();
				break;

			}
		};
	};
	
	protected void showDialog() {
//		dialog = new CustomDialog(context,120, 80, R.layout.view_mydialog_layout, R.style.myDialog);
//		TextView msgTxt = (TextView) dialog.findViewById(R.id.tips);
//		msgTxt.setText("更新数据...");
//		dialog.show();
		
		Toast.makeText(context, "正在更新数据...", Toast.LENGTH_SHORT).show();
		
	}
	protected void dismissDialog() {
		///dialog.dismiss();
	}

}
