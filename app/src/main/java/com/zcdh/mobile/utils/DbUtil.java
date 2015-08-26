/**
 * 
 * @author YJN, 2013-11-3 下午11:54:15
 */
package com.zcdh.mobile.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.zcdh.mobile.framework.K;

/**
 * @author YJN, 2013-11-3 下午11:54:15
 * yangjiannan 2014-04-12
 */
public class DbUtil {

	/**
	 * 如果数据库文件较大，使用FileSplit分割为小于1M的小文件 此例中分割为 hello.db.101 hello.db.102
	 * hello.db.103
	 */
	// 第一个文件名后缀
	private static final int ASSETS_SUFFIX_BEGIN = 101;
	// 最后一个文件名后缀
	private static final int ASSETS_SUFFIX_END = 106;

	private static String dbDirectory = "";
	
	private static String dbFile = "";
	
	private static FinalDb finalDb;
	
	
	/**
	 * 创建数据库
	 * @param c
	 * @return
	 */
	public static FinalDb create(Context c) {
		try {
			prepareDatabase(c);
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(c, "copy db error", Toast.LENGTH_SHORT).show();
		}
		if(finalDb==null){
			finalDb = FinalDb.create(c, dbDirectory, K.DB.DbName, K.debug);
		}
		return finalDb;
	}

	/**
	 * 准备数据库
	 * 1）设置数据库路径
	 * 2) 检查数据库路径是否存在
	 * 3)如果不存在，copy 到设置好的数据库文件路径
	 */
	public static void prepareDatabase(Context c) throws IOException {
		//设置 数据库路径
		setDbPath(c);
		//检查数据是否存在
		if (!checkDataBase()) {
			DbUtil.copyBigDataBase(c);
		}

	}
	
	public static String getDbPath(Context c){
		if(StringUtils.isBlank(dbFile)){
			setDbPath(c);
		}
		return dbFile;
	}

	/**
	 *  设置当前数据库路径
	 * @param c
	 */
	private static void setDbPath(Context c) {
		String packageName = c.getPackageName();
		if (StorageUtil.isExternalStorageAvailableAndWriteable()) {
			dbDirectory = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + 
					packageName + File.separator+
					"databases"+File.separator+K.DB.version;
		} else {
			dbDirectory = c.getFilesDir() + File.separator + 
					packageName+File.separator+
					"databases"+File.separator+K.DB.version;
		}
		dbFile=dbDirectory+File.separator+K.DB.DbName;
	}

	// 复制assets下的大数据库文件时用这个
	private static void copyBigDataBase(Context c) throws IOException {

		InputStream myInput;

		File file = new File(dbDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}

		Log.i("dbDirectory", dbDirectory);
		Log.i("dbFile", dbFile);
		OutputStream myOutput = new FileOutputStream(dbFile);
		for (int i = ASSETS_SUFFIX_BEGIN; i < ASSETS_SUFFIX_END + 1; i++) {
			myInput = c.getAssets().open(K.DB.DbName + "." + i);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myInput.close();
		}
		myOutput.close();
	}

	// 检查数据库是否有效
	private static boolean checkDataBase() {
		// String myPath = path + Constants.DATABASE_NAME;
		File file = new File(dbFile);
		if (file.exists()) {
			SQLiteDatabase checkDB = null;
			try {
				checkDB = SQLiteDatabase.openDatabase(dbFile, null,
						SQLiteDatabase.OPEN_READONLY);

			} catch (SQLiteException e) { // database does't exist yet.
				e.printStackTrace();
			}
			if (checkDB != null) {
				try {
					checkDB.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return checkDB != null ? true : false;
		} else {
			return false;
		}

	}
	

}
