/**
 * 
 * @author YJN, 2013-11-4 下午3:04:13
 */
package com.zcdh.mobile.utils;

/**
 * @author YJN, 2013-11-4 下午3:04:13
 */

import android.os.Environment;

/**
 * Checks the state of the external storage of the device.
 * 
 * @author kaolick
 */
public class StorageUtil {
	// Storage states
	private static boolean externalStorageAvailable, externalStorageWriteable;

	/**
	*  SDCard默认存储根位置
	*/
	private static String SAVE_PATH_SD   = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/com.zcdh.mobile/" ;
    private static String SAVE_PATH_INTERNE = "/data/data/com.zcdh.mobile";
	
	/**
	 * Checks the external storage's state and saves it in member attributes.
	 */
	private static void checkStorage() {
		// Get the external storage's state
		String state = Environment.getExternalStorageState();

		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// Storage is available and writeable
			externalStorageAvailable = externalStorageWriteable = true;
		} else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			// Storage is only readable
			externalStorageAvailable = true;
			externalStorageWriteable = false;
		} else {
			// Storage is neither readable nor writeable
			externalStorageAvailable = externalStorageWriteable = false;
		}
	}

	/**
	 * Checks the state of the external storage.
	 * 
	 * @return True if the external storage is available, false otherwise.
	 */
	public static boolean isExternalStorageAvailable() {
		checkStorage();

		return externalStorageAvailable;
	}

	/**
	 * Checks the state of the external storage.
	 * 
	 * @return True if the external storage is writeable, false otherwise.
	 */
	public static boolean isExternalStorageWriteable() {
		checkStorage();

		return externalStorageWriteable;
	}

	/**
	 * Checks the state of the external storage.
	 * 
	 * @return True if the external storage is available and writeable, false
	 *         otherwise.
	 */
	public static boolean isExternalStorageAvailableAndWriteable() {
		checkStorage();

		if (!externalStorageAvailable) {
			return false;
		} else if (!externalStorageWriteable) {
			return false;
		} else {
			return true;
		}
	}
	
	public static String getLocalSaveRootPath(){
		String result="";
		if(isExternalStorageAvailableAndWriteable()){ 
			result=SAVE_PATH_SD;
		}else {
			result=SAVE_PATH_INTERNE;
		}
		return result;
	}
}
