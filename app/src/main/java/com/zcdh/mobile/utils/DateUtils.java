package com.zcdh.mobile.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author peisonghai
 * @version 1.0.0
 * @create 2011-06-13 10:10
 * @see 处理时间（包括时间格式的类）
 */
public class DateUtils {
	/**
	 * 取时间年的格式代码
	 */
	public static String YEAR = "yyyy";

	/**
	 * 取时间月的格式代码
	 */
	public static String MONTH = "MM";

	/**
	 * 取时间日的格式代码
	 */
	public static String DAY = "dd";

	/**
	 * 取时间时的格式代码
	 */
	public static String HOUR = "hh";

	/**
	 * 取时间24小时制的格式代码
	 */
	public static String HOUR_24 = "HH";

	/**
	 * 取时间分的格式代码
	 */
	public static String MIMUTE = "mm";

	/**
	 * 取时间秒的格式代码
	 */
	public static String SECOND = "ss";

	/**
	 * 取时间毫秒的格式代码
	 */
	public static String MILLISECOND = "SS";

	/**
	 * 格式为yyyy-MM-dd的时间
	 */
	public static String YMD_FORMAT = YEAR + "-" + MONTH + "-" + DAY;

	public static String YM_FORMAT = YEAR + "-" + MONTH;

	public static String HM_FORMAT = HOUR_24 + ":" + MIMUTE;

	/**
	 * 格式为yyyy年 MM月dd日的时间
	 */
	public static String YMD_FORMAT_CH = YEAR + "年 " + MONTH + "月" + DAY + "日 ";

	/**
	 * 格式为MM-dd的时间
	 */
	public static String MD_FORMAT = MONTH + "-" + DAY;

	/**
	 * 
	 */
	public static String NUM_FORMAT = YEAR + MONTH + DAY;

	/**
	 * 格式为yyyy-MM-dd HH:mm:ss的时间
	 */
	public static String YMDHMS_FORMAT = YEAR + "-" + MONTH + "-" + DAY + " "
			+ HOUR_24 + ":" + MIMUTE + ":" + SECOND;

	/**
	 * 格式为yyyy-MM-dd HH:mm的时间
	 */
	public static String YMDHM_FORMAT = YEAR + "-" + MONTH + "-" + DAY + " "
			+ HOUR_24 + ":" + MIMUTE;

	/**
	 * 格式为yyyy-MM-dd HH:mm:ss:SS的时间
	 */
	public static String UTILTIME_FORMAT = YEAR + "-" + MONTH + "-" + DAY + " "
			+ HOUR_24 + ":" + MIMUTE + ":" + SECOND + ":" + MILLISECOND;

	/**
	 * 格式为yyyyMMddHHmmssSS的时间
	 */
	public static String CRITERIONTIME_FORMAT = YEAR + MONTH + DAY + HOUR_24
			+ MIMUTE + SECOND + MILLISECOND;

	/**
	 * 格式为yyyyMMddHHmmssSS的时间
	 */
	public static String CRITERIONTIME_FORMAT_DHMS = DAY + HOUR_24 + MIMUTE
			+ SECOND + MILLISECOND;

	/**
	 * @author peisonghai
	 * @see 2011-06-13 获得yy-mm-dd格式的时间
	 * @return String date
	 */
	public static String getYearMonthDay() {
		return getDateByFormat(YEAR + "-" + MONTH + "-" + DAY);
	}

	/**
	 * @see 返回格式为yyyy-MM-dd HH:mm:ss:SS的时间字符串
	 * @return String date
	 */
	public static String getNOWTime_0() {
		return getDateByFormat(UTILTIME_FORMAT);
	}

	/**
	 * @see 返回格式为yyyyMMddHHmmssSS的时间字符串
	 * @return String date
	 */
	public static String getNOWTime_1() {
		return getDateByFormat(CRITERIONTIME_FORMAT);
	}

	/**
	 * @see 获得指定时间格式
	 * @param String
	 *            format 时间格式
	 * @return String dateStr 返回获得相应格式时间的字符串
	 */
	public static String getDateByFormat(String format) {
		String dateStr = "";
		try {
			if (format != null) {
				Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat simFormat = new SimpleDateFormat(format,
						Locale.CHINA);
				dateStr = simFormat.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * @see 获得指定时间格式
	 * @param Date
	 *            date 时间
	 * @param String
	 *            format 时间格式
	 * @return String dateStr 返回获得相应格式时间的字符串
	 */
	public static String getDateByFormatYMD(Date date) {
		String dateStr = "";
		try {
			SimpleDateFormat simFormat = new SimpleDateFormat(YMD_FORMAT,
					Locale.CHINA);
			dateStr = simFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * @see 获得指定时间格式
	 * @param Date
	 *            date 时间
	 * @param String
	 *            format 时间格式
	 * @return String dateStr 返回获得相应格式时间的字符串
	 */
	public static String getDateByFormatYM(Date date) {
		String dateStr = "";
		try {
			SimpleDateFormat simFormat = new SimpleDateFormat(YM_FORMAT,
					Locale.CHINA);
			dateStr = simFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	public static String getHourMimute(Date date) {
		String dateStr = "";
		try {
			SimpleDateFormat simFormat = new SimpleDateFormat(HM_FORMAT,
					Locale.CHINA);
			dateStr = simFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * @see 获得指定时间格式中午形式
	 * @param Date
	 *            date 时间
	 * @param String
	 *            format 时间格式
	 * @return String dateStr 返回获得相应格式时间的字符串
	 */
	public static String getDateByFormatYMDC(Date date) {
		String dateStr = "";
		try {
			SimpleDateFormat simFormat = new SimpleDateFormat(YMD_FORMAT_CH,
					Locale.CHINA);
			dateStr = simFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * @see 获得指定时间格式
	 * @param Date
	 *            date 时间
	 * @param String
	 *            format 时间格式
	 * @return String dateStr 返回获得相应格式时间的字符串
	 */
	public static String getDateByFormatNUM(Date date) {
		String dateStr = "";
		try {
			SimpleDateFormat simFormat = new SimpleDateFormat(NUM_FORMAT,
					Locale.CHINA);
			dateStr = simFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * @see 获得指定时间格式
	 * @param Date
	 *            date 时间
	 * @param String
	 *            format 时间格式
	 * @return String dateStr 返回获得相应格式时间的字符串
	 */
	public static String getDateByFormat(Date date, String format) {
		String dateStr = "";
		try {
			if (format != null) {
				SimpleDateFormat simFormat = new SimpleDateFormat(format,
						Locale.CHINA);
				dateStr = simFormat.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	public static String getDateByFormatYMDHMS(Date date) {
		String dateStr = "";
		String format = DateUtils.YMDHMS_FORMAT;
		try {
			if (format != null) {
				SimpleDateFormat simFormat = new SimpleDateFormat(format,
						Locale.CHINA);
				dateStr = simFormat.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	public static String getDateByFormatYMDHM(Date date) {
		String dateStr = "";
		String format = DateUtils.YMDHM_FORMAT;
		try {
			if (format != null) {
				SimpleDateFormat simFormat = new SimpleDateFormat(format,
						Locale.CHINA);
				dateStr = simFormat.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * @see 获得当前时间
	 * @return Date date
	 */
	public static Date getNOWTime() {
		return new Date(System.currentTimeMillis());
	}

	/**
	 * @see 把字符串类型的时间转换为yyyy-MM-dd的时间格式
	 */
	public static Date getDateByStrToYMDHMS(String str) {
		Date date = null;
		if (str != null && str.trim().length() > 0) {
			DateFormat dFormat = new SimpleDateFormat(YMDHMS_FORMAT,
					Locale.getDefault());
			try {
				date = dFormat.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * @see 把字符串类型的时间转换为yyyy-MM-dd的时间格式
	 */
	public static Date getDateByStrToYMD(String str) {
		Date date = null;
		if (str != null && str.trim().length() > 0) {
			DateFormat dFormat = new SimpleDateFormat(YMD_FORMAT,
					Locale.getDefault());
			try {
				date = dFormat.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * @see 把字符串类型的时间转换为自定义格式的时间
	 */
	public static Date getDateByStrToFormat(String format, String str) {
		DateFormat dFormat = new SimpleDateFormat(format, Locale.getDefault());
		Date date = null;
		try {
			if (str != null) {
				date = dFormat.parse(str);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 功能：判断输入年份是否为闰年<br>
	 * 
	 * @param year
	 * @return 是：true 否：false
	 * @author pure
	 */
	public static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0) {
					leap = true;
				} else {
					leap = false;
				}
			} else {
				leap = true;
			}
		} else
			leap = false;
		return leap;
	}

	/**
	 * 功能：得到指定月份的月底 格式为：xxxx-yy-zz (eg: 2007-12-31)<br>
	 * 
	 * @param String
	 * @return String
	 */
	public static String getEndOfMonth(String str) {
		int tyear = Integer.parseInt(getDateByFormat(getDateByStrToYMD(str),
				YEAR));
		int tmonth = Integer.parseInt(getDateByFormat(getDateByStrToYMD(str),
				MONTH));
		String strtmonth = null;
		String strZ = null;
		if (tmonth == 1 || tmonth == 3 || tmonth == 5 || tmonth == 7
				|| tmonth == 8 || tmonth == 10 || tmonth == 12) {
			strZ = "31";
		}
		if (tmonth == 4 || tmonth == 6 || tmonth == 9 || tmonth == 11) {
			strZ = "30";
		}
		if (tmonth == 2) {
			if (leapYear(tyear)) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		strtmonth = tmonth >= 10 ? String.valueOf(tmonth) : ("0" + tmonth);
		return tyear + "-" + strtmonth + "-" + strZ;
	}

	public static String getEndOfMonth(int tyear, int tmonth) {
		String strtmonth = null;
		String strZ = null;
		if (tmonth == 1 || tmonth == 3 || tmonth == 5 || tmonth == 7
				|| tmonth == 8 || tmonth == 10 || tmonth == 12) {
			strZ = "31";
		}
		if (tmonth == 4 || tmonth == 6 || tmonth == 9 || tmonth == 11) {
			strZ = "30";
		}
		if (tmonth == 2) {
			if (leapYear(tyear)) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}
		strtmonth = tmonth >= 10 ? String.valueOf(tmonth) : ("0" + tmonth);
		return tyear + "-" + strtmonth + "-" + strZ;
	}

	/**
	 * 功能：得到指定月份的月初 格式为：xxxx-yy-zz (eg: 2007-12-01)<br>
	 * 
	 * @param String
	 * @return String
	 */
	public static String getStartOfMonth(int tyear, int tmonth) {
		String strtmonth = tmonth >= 10 ? String.valueOf(tmonth)
				: ("0" + tmonth);
		return tyear + "-" + strtmonth + "-" + "01";
	}

	public static String getStartOfMonth(String str) {
		int tyear = Integer.parseInt(getDateByFormat(getDateByStrToYMD(str),
				YEAR));
		int tmonth = Integer.parseInt(getDateByFormat(getDateByStrToYMD(str),
				MONTH));
		String strtmonth = tmonth >= 10 ? String.valueOf(tmonth)
				: ("0" + tmonth);
		return tyear + "-" + strtmonth + "-" + "01";
	}

	/**
	 * 功能：得到指定月份的月初 格式为：xxxx-yy-zz (eg: 2007-12-01)<br>
	 * 
	 * @param String
	 * @return String
	 */
	public static int getMonthCountBySQU(String start, String end) {
		int syear = Integer.parseInt(getDateByFormat(getDateByStrToYMD(start),
				YEAR));
		int smonth = Integer.parseInt(getDateByFormat(getDateByStrToYMD(start),
				MONTH));
		int eyear = Integer.parseInt(getDateByFormat(getDateByStrToYMD(start),
				YEAR));
		int emonth = Integer.parseInt(getDateByFormat(getDateByStrToYMD(start),
				MONTH));
		return (eyear - syear) * 12 + (emonth - smonth) + 1;
	}

	/**
	 * 获得时间序列 EG:2008-01-01~2008-01-31,2008-02-01~2008-02-29
	 */
	public static List<String> getMonthSqu(String fromDate, String toDate) {
		List<String> list = new ArrayList<String>();
		int count = getMonthCountBySQU(fromDate, toDate);
		int syear = Integer.parseInt(getDateByFormat(
				getDateByStrToYMD(fromDate), YEAR));
		int smonth = Integer.parseInt(getDateByFormat(
				getDateByStrToYMD(fromDate), MONTH));
		int eyear = Integer.parseInt(getDateByFormat(getDateByStrToYMD(toDate),
				YEAR));
		String startDate = fromDate;
		String endDate = "";
		for (int i = 1; i <= count; i++) {
			if (syear <= eyear) {
				startDate = getStartOfMonth(syear, smonth);
				endDate = getEndOfMonth(syear, smonth);
				list.add(startDate + "~" + endDate);
				System.out.println(startDate + "~" + endDate);
				if (smonth == 13) {
					smonth = 1;
					syear++;
				}
				smonth++;
			}
		}
		return list;
	}

	/**
	 * 通过传入的时间来获得所属周内的时间
	 * 
	 * @param start
	 * @param num
	 * @return
	 */
	public static String getDateOFWeekByDate(String start, int num) {
		Date dd = getDateByStrToYMD(start);
		Calendar c = Calendar.getInstance();
		c.setTime(dd);
		if (num == 1) // 返回星期一所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num == 2) // 返回星期二所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num == 3) // 返回星期三所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num == 4) // 返回星期四所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num == 5) // 返回星期五所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num == 6) // 返回星期六所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num == 0) // 返回星期日所在的日期
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return getDateByFormatYMD(c.getTime());
	}

	/**
	 * 获取当前日期为星期几
	 * 
	 * @param dt
	 * @return
	 * @author test7, 2013-3-8 下午2:38:59
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public static String getTimeBetween(Date begin, Date end) {
		String result = "";
		try {
			/*
			 * String sbegin=getDateByFormatYMDHMS(begin); String
			 * send=getDateByFormatYMDHMS(end); //SimpleDateFormat dfs = new
			 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 * begin=getDateByStrToYMDHMS(sbegin); end =
			 * getDateByStrToYMDHMS(send);
			 */
			Long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			Long minute = between / (60 * 60);
			Long second = between - minute * 60 * 60;

			result = second + "''";
			if (minute.intValue() > 0) {
				result = minute + "'" + second;
			}
		} catch (Exception e) {
			return "";
		}
		return result;
	}

	public static Long getTimeBetweenByMinute(Date begin, Date end) {
		Long between = 0L;
		try {
			/*
			 * String sbegin=getDateByFormatYMDHMS(begin); String
			 * send=getDateByFormatYMDHMS(end); //SimpleDateFormat dfs = new
			 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 * begin=getDateByStrToYMDHMS(sbegin); end =
			 * getDateByStrToYMDHMS(send);
			 */
			between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

		} catch (Exception e) {
			return between;
		}
		return between;
	}

}
