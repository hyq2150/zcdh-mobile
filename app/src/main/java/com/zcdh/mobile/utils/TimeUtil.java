package com.zcdh.mobile.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String converTime(long time) {
		long currentSeconds = System.currentTimeMillis() / 1000;
		long timestamp = time / 1000;
		long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数
		System.out.println(timestamp + ":" + currentSeconds + ":" + timeGap);
		String timeStr = null;
		if (timeGap > 3 * 24 * 60 * 60) {// 1天以上
			timeStr = DateUtils.getDateByFormatYMD(new Date(timestamp * 1000));
		} else if (timeGap > 2 * 24 * 60 * 60 && timeGap <= 3 * 24 * 60 * 60) {// 1天以上
			// timeStr = timeGap/(24*60*60)+"天前";
			timeStr = "前天";
		} else if (timeGap > 24 * 60 * 60 && timeGap <= 2 * 24 * 60 * 60) {// 1天以上
			// timeStr = timeGap/(24*60*60)+"天前";
			timeStr = "昨天";
		} else if (timeGap > 60 * 60) {// 1小时-24小时
			timeStr = timeGap / (60 * 60) + "小时前";
		} else if (timeGap > 60) {// 1分钟-59分钟
			timeStr = timeGap / 60 + "分钟前";
		} else {// 1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}

	public static String getStandardTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		Date date = new Date(timestamp * 1000);
		sdf.format(date);
		return sdf.format(date);
	}
	// public static String getStandardTimeBYMDHS(long timestamp){
	// SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
	// Date date = new Date(timestamp*1000);
	// sdf.format(date);
	// return sdf.format(date);
	// }
	// public static String getStandardTimeBYMD(long timestamp){
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
	// Date date = new Date(timestamp*1000);
	// sdf.format(date);
	// return sdf.format(date);
	// }

}
