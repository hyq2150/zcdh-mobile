/**
 * 
 */
package com.zcdh.mobile.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符窜工具类
 * 
 * @author caijianqing, 2012-10-18 上午10:34:19
 */
public class StringUtils {

	/**
	 * 返回一个字符窜的左边最多maxLength个字符窜，超出时在结尾添加后缀suffix
	 * 
	 * @param str 需要截取的字符窜，null时直接返回空字符串“”
	 * @param maxLength 最大截取字符数量，必须大于0
	 * @param suffix 后缀，可选
	 * @return
	 * @author caijianqing, 2012-10-18 上午10:39:09
	 */
	public static String left(String str, int maxLength, String suffix) {

		if (str == null) return "";

		int len = str.length();
		if (len <= maxLength) {
			return str;
		} else {
			return (suffix == null) ? str.substring(0, maxLength) : str.substring(0, maxLength) + suffix;
		}

	}

	/**
	 * 将第一个字母变为小写字母
	 * 
	 * @param s 待转变的字符窜
	 * @return 转换后的字符窜
	 */
	public static String toLowerCase(String s) {
		if (s == null) return null;
		int i = s.substring(0, 1).getBytes()[0];
		if (i >= 65 && i <= 90) {
			i += 32;
			StringBuffer sb = new StringBuffer();
			sb.append((char) i);
			sb.append(s.substring(1));
			return sb.toString();
		} else {
			return s;
		}
	}

	/**
	 * 通用正则表达式验证
	 * 
	 * @param allowNull 允许为空
	 * @param patterns 匹配的正则表达式集合
	 * @param value 待验证的值
	 * @return 验证结果,null为通过验证
	 */
	public static boolean checkPattern(boolean allowNull, String pattern, Object value) {
		if (null == value) {
			return allowNull;
		} else {
			return (Pattern.matches(pattern, value.toString()));
		}
	}

	/**
	 * 生成一些重复的字符的字符窜
	 * 
	 * @param s 被重复的字符
	 * @param n 重复次数
	 * @return 经过重复的字符窜
	 */
	public static String buildRepeatString(String s, int n) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 生成模糊搜索关键词
	 * 
	 * @param keyword
	 * @return
	 */
	public static String buildLikeWord(String keyword) {
		return new StringBuffer("%").append(keyword).append("%").toString();
	}

	/**
	 * 返回匹配正则表达式的所有匹配的结果
	 * 
	 * @param regex 正则表达式
	 * @param input 被查找的字符窜
	 * @return 所有找到的结果
	 */
	public static List<List<String>> matcherAll(String regex, String input) {
		List<List<String>> list = null;
		Matcher matcher;
		try {
			matcher = Pattern.compile(regex).matcher(input);
			List<String> l = null;
			list = new ArrayList<List<String>>();
			while (matcher.find()) {
				l = new ArrayList<String>();
				for (int i = 1; i <= matcher.groupCount(); i++) {
					l.add(new String(matcher.group(i)));
				}
				list.add(l);
			}
			return list;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			matcher = null;
			list = null;
		}
	}

	/**
	 * 返回匹配正则表达式的所有字符
	 * 
	 * @param regex 正则表达式
	 * @param input 被查找的字符窜
	 * @return 所有找到的结果
	 */
	public static List<String> matcher(String regex, String input) {
		List<String> list = null;
		Matcher matcher = null;
		try {
			matcher = Pattern.compile(regex).matcher(input);
			list = new ArrayList<String>();
			while (matcher.find()) {
				for (int i = 1; i <= matcher.groupCount(); i++) {
					list.add(new String(matcher.group(i)));
				}
			}
			return list.size() > 0 ? list : null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			matcher = null;
			list = null;
		}
	}

	/**
	 * 将iso8859-1编码的字符窜转为utf-8的字符窜, 用于表单中get方法传进来的参数
	 * 
	 * @param iso8859
	 * @return
	 */
	public static String iso88591toUTF8(String iso8859) {
		if (iso8859 == null) return null;
		try {
			return new String(iso8859.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将指定字符窜采用URL编码方式编码
	 * 
	 * @param url
	 * @return
	 */
	public static String urlEncodeUTF8(String url) {
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符窜中的特殊字符(正则表达式使用到的转义符:<b>()-.#%^*{}[]\|?/</b>)添加转义符
	 * 
	 * @param str 要转移的字符窜
	 * @return
	 */
	public static String escape(String str) {
		if (null == str || "".equals(str)) return str;
		return str.replaceAll("([\\(\\)\\-\\.\\#\\%\\^\\*\\{\\}\\[\\]\\\\\\\\|\\?\\/])", "\\\\$1");
	}

	/**
	 * String.trim的升级版, 去除前后英文空格的同时把中文空格也去除.
	 * 
	 * @param str 要trim的字符窜
	 * @return
	 */
	public static String trim2(String str) {
		if (null == str || "".equals(str)) return str;
		return str.trim().replaceAll("^　+|　+$", "");
	}

	/**
	 * 匹配字符窜中是否包含正在表达式的内容
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static boolean containsPattern(String str, String pattern) {
		return !str.matches("^(?!.*?" + pattern.toString() + ").*$");
	}

	/**
	 * 检查一个字符窜是否为空：<br>
	 * 如果为null或者trim()后是空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().equals("");
	}

	/**
	 * 是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		return Pattern.matches("\\d+", str);
	}

	/**
	 * 是否为浮点数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isFloat(String str) {
		return Pattern.matches("\\d+|\\d+\\.|\\d+\\.\\d+", str);
	}

	/**
	 * 是否为数字<br>
	 * 
	 * @param str 依次判断是否为以下字符窜之一：true，false，1，0，yes，no
	 * @return
	 */
	public static boolean isBoolean(String str) {
		if (str == null) return false;
		return "true".equals(str) || "false".equals(str) || "1".equals(str) || "0".equals(str) || "yes".equals(str) || "no".equals(str);
	}

	/**
	 * 讲字符窜转换为Boolean类型 true：true,yes,1 false：false,no,0
	 * 
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static boolean convertToBoolean(String str) throws ParseException {
		if (str == null) {
			throw new ParseException("参数数据类型错误：" + str, 0);
		}
		if ("true".equals(str) || "1".equals(str) || "yes".equals(str)) {
			return true;
		}
		if ("false".equals(str) || "0".equals(str) || "no".equals(str)) {
			return false;
		}
		throw new ParseException("参数数据类型错误：" + str, 0);
	}

	/**
	 * 判断两个字符窜的值是否相等，两个为null时返回true
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equals2(String s1, String s2) {
		if (s1 == null) {
			return s2 == null;
		} else {
			return s1.equals(s2);
		}
	}

	/** 共用正则表达式：URL */
	public static final String REG_URL = "((ftp|https?)://[-\\w]+(\\.\\w[-\\w]*)+|(?i:[a-z0-9](?:[-a-z0-9]*[a-z0-9])?\\.)+(?-i:com\\b|edu\\b|biz\\b|gov\\b|in(?:t|fo)\\b|mil\\b|net\\b|org\\b|[a-z][a-z]\\b))(:\\d+)?(/[^.!,?;\"'<>()\\[\\]{}\\s\\x7F-\\xFF]*(?:[.!,?]+[^.!,?;\"'<>()\\[\\]{}\\s\\x7F-\\xFF]+)*)?";

	/**
	 * 提取URL<Br>
	 * 所用正则表达式为：\b((ftp|https?)://[-\w]+(\.\w[-\w]*)+|(?i:[a-z0-9](?:[-a-z0-9]*[
	 * a-z0-9])?\.)+(?-i:com\b|edu\b|biz\b|gov\b|in(?:t|fo)\b|mil\b|net\b|org\b|
	 * [a-z][a-z]\b))(:\d+)?(/[^.!,?;"'<>()\[\]{}\s\x7F-\xFF]*(?:[.!,?]+[^.!,?;
	 * "'<>()\[\]{}\s\x7F-\xFF]+)*)?
	 * 
	 * @param srt
	 * @return
	 * @author test9, 2013-1-2 下午6:54:10
	 */
	public static String[] findUrls(String srt) {
		if (isBlank(srt)) return null;
		Matcher matcher = Pattern.compile(REG_URL).matcher(srt);
		if (!matcher.find()) return null;

		List<String> urls = new ArrayList<String>();
		do {
			String url = matcher.group();
			// 正则可匹配无协议开头的网址，依照习惯，一般为http://
			if (!url.startsWith("http://")) {
				url = "http://" + url;
			}
			urls.add(url);
		} while (matcher.find());
		return urls.toArray(new String[0]);
	}

	/**
	 * 转换HTML源码为可见字符
	 * 
	 * @param html
	 * @return
	 * @author test9, 2013-1-2 下午8:41:56
	 */
	public static String htmlSpecialChars(String html) {
		if (html == null || "".equals(html.trim())) return null;
		html = html.replaceAll("&", "&amp;");
		html = html.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		html = html.replaceAll("\"", "&quot;");
		return html;
	}

	/**
	 * 在内容中查找#话题#话题列表，并以List＜String＞形式返回
	 * 
	 * <br>
	 * <b>开发人员：</b> caijianqing, 2013-1-25 下午3:06:48
	 * 
	 * @param content
	 * @return 如果没有找到将返回NULL
	 */
	public static List<String> findTopics(String content) {
		Pattern pattern = Pattern.compile("#([^#]+)#");
		Matcher m = pattern.matcher(content);
		List<String> list = new ArrayList<String>();
		while (m.find()) {
			String name = m.group(1);
			if (list.contains(name)) continue;
			list.add(name);
		}
		return list.size() != 0 ? list : null;
	}

	/**
	 * 在内容中查找“@用户名”，并以List＜String＞形式返回
	 * 
	 * <br>
	 * <b>开发人员：</b> caijianqing, 2013-1-25 下午3:06:48
	 * 
	 * @param content
	 * @return 如果没有找到将返回NULL
	 */
	public static List<String> findAtUserAlias(String content) {
		Pattern pattern = Pattern.compile("@([^@ :/\\\\]+)");
		Matcher m = pattern.matcher(content);
		List<String> list = new ArrayList<String>();
		while (m.find()) {
			String name = m.group(1);
			if (list.contains(name)) continue;
			list.add(name);
		}
		return list.size() != 0 ? list : null;
	}

	/**
	 * 限制字符窜长度
	 * 
	 * @author caijianqing, 2013-4-26 上午11:01:06
	 * @param msg 字符窜，如果为null，直接返回null
	 * @param length，最大长度，必须大于0
	 * @param suffix 后缀，可选，一般为“...”
	 * @return
	 */
	public static String limit(String msg, Integer length, String suffix) {
		if (msg == null) return msg;
		if (msg.length() > length) if (suffix != null && length > suffix.length())
			msg = msg.substring(0, length - suffix.length()) + suffix;
		else
			msg = msg.substring(0, length);
		return msg;
	}

	/**
	 * 清除WORD冗余标志以适用于普通浏览器显示
	 * 
	 * @author caijianqing, 2013-5-16 上午9:03:49
	 * @param html
	 * @return
	 */
	public static String clearWord(String html) {
		// 去掉span
		html = html.replaceAll("<\\/?SPAN[^>]*>", "");
		// 去掉class
		html = html.replaceAll("<(\\w[^>]*) class=([^ |>]*)([^>]*)", "<$1$3");
		// 去掉style
		html = html.replaceAll("<(\\w[^>]*) style=\"([^\"]*)\"([^>]*)", "<$1$3");
		// 去掉lang
		html = html.replaceAll("<(\\w[^>]*) lang=([^ |>]*)([^>]*)", "<$1$3");
		// 去掉xml标示
		html = html.replaceAll("<\\\\?\\?xml[^>]*>", "");
		// 去掉像<Web:binding selector="#header"></Web:binding>的微软自定义标签
		html = html.replaceAll("<\\/?\\w+:[^>]*>", "");

		// 去掉<IMG height=97 alt="文本框: asdfa" src="image001.gif" width=87
		// v:dpi="96" v:shapes="_x0000_s1026">
		html = html.replaceAll("\\s\\w*:[^\\s/?>]*", "");
		html = html.replaceAll("<\\/?\\w+:[^>]*>", "");
		// 中的v:dpi="96" v:shapes="_x0000_s1026"
		return html;
	}

	/**
	 * 删除所有HTML标签
	 * 
	 * @author caijianqing, 2013-5-16 下午4:19:25
	 * @param html
	 * @return
	 */
	public static String deleteHTML(String html) {
		if (html == null) return null;
		return html.replaceAll("<[^<>]*>", "");
	}

	/**
	 * 构建SQL in语法的问号，例子：and id in(?,?,?)
	 * 
	 * @author caijianqing, 2013-5-23 下午3:52:21
	 * @param prefix 前缀，一般是“ and id”，自动前后补充空格
	 * @param count 构建的问号数量
	 * @return
	 */
	public static String sqlIN(String prefix, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append("?,");
		}
		if (sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
			sb.append(')');
			sb.insert(0, " in(");
			sb.insert(0, " " + prefix + " ");
		}
		return sb.toString();
	}

	/**
	 * 收集异常调用堆栈信息，生成字符窜
	 * 
	 * @author caijianqing, 2013-6-3 下午3:19:31
	 * @param t
	 * @return
	 */
	public static String printStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.close();
		return sw.toString();
	}

	/** 返回可以识别的异常信息 */
	public static String getCauseMessage(Throwable e) {
		Throwable cause = e.getCause() != null ? e.getCause() : e;
		if (cause.getMessage() == null)
			return cause.getClass().toString();
		else
			return cause.getMessage();
	}

	/**
	 * 获取指定对象的字符串描述
	 * 
	 * @param object：指定对象
	 * @param exIncludeFiled 排除字段
	 * @return
	 * @throws Exception
	 * @author peisonghai, 2013-5-22 下午2:36:42
	 */
	public static String getObjectString(Object object, String exIncludeFiled) throws Exception {
		String result = "";
		// 不需要的自己去掉即可
		if (object != null) {// if (object!=null ) ----begin
			// 拿到该类
			Class<?> clz = object.getClass();
			// 获取实体类的所有属性，返回Field数组
			Field[] fields = clz.getDeclaredFields();

			for (Field field : fields) {// --for() begin
				// System.out.println(field.getGenericType());//打印该类的所有属性类型
				String fieldname = field.getName() + ",";
				// System.out.println(fieldname);
				if (exIncludeFiled.contains(fieldname)) continue;
				// 如果类型是String
				if (field.getGenericType().toString().equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
					// 拿到该属性的gettet方法

					Method m = object.getClass().getMethod("get" + getMethodName(field.getName()));

					String val = (String) m.invoke(object);// 调用getter方法获取属性值
					if (val != null) {
						// System.out.println("String type:" + val);
						result = result + field.getName() + ":" + val + ",";
					}

				}

				// 如果类型是Integer
				if (field.getGenericType().toString().equals("class java.lang.Integer")) {
					Method m = object.getClass().getMethod("get" + getMethodName(field.getName()));
					Integer val = (Integer) m.invoke(object);
					if (val != null) {
						// System.out.println("Integer type:" + val);
						result = result + field.getName() + ":" + val + ",";
					}

				}

				// 如果类型是Double
				if (field.getGenericType().toString().equals("class java.lang.Double")) {
					Method m = object.getClass().getMethod("get" + getMethodName(field.getName()));
					Double val = (Double) m.invoke(object);
					if (val != null) {
						// System.out.println("Double type:" + val);
						result = result + field.getName() + ":" + val + ",";
					}

				}

				// 如果类型是Boolean 是封装类
				if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
					Method m = object.getClass().getMethod(field.getName());
					Boolean val = (Boolean) m.invoke(object);
					if (val != null) {
						// System.out.println("Boolean type:" + val);
						result = result + field.getName() + ":" + val + ",";
					}

				}

				// 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
				// 反射找不到getter的具体名
				if (field.getGenericType().toString().equals("boolean")) {
					Method m = object.getClass().getMethod(field.getName());
					Boolean val = (Boolean) m.invoke(object);
					if (val != null) {
						// System.out.println("boolean type:" + val);
						result = result + field.getName() + ":" + val + ",";
					}

				}
				// 如果类型是Date
				if (field.getGenericType().toString().equals("class java.util.Date")) {
					Method m = object.getClass().getMethod("get" + getMethodName(field.getName()));
					Date val = (Date) m.invoke(object);
					if (val != null) {
						// System.out.println("Date type:" + val);
						result = result + field.getName() + ":" + val + ",";
					}

				}
				// 如果类型是Short
				if (field.getGenericType().toString().equals("class java.lang.Short")) {
					Method m = object.getClass().getMethod("get" + getMethodName(field.getName()));
					Short val = (Short) m.invoke(object);
					if (val != null) {
						// System.out.println("Short type:" + val);
						result = result + field.getName() + ":" + val + ",";
					}

				}
				// 如果还需要其他的类型请自己做扩展

			}// for() --end

		}// if (object!=null ) ----end

		return result;
	}

	// 把一个字符串的第一个字母大写、效率是最高的、
	private static String getMethodName(String fildeName) throws Exception {
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}

	public static void main(String[] args) throws Exception {
		/*
		 * ProChkbillPosition p = new ProChkbillPosition(); String ex = "id,";
		 * p.setId(1); p.setProjectNo("001"); String reslut = getObjectString(p,
		 * ex); System.out.println(reslut);
		 */
	}

	public static String stringMD5(String input) {

		try {

			// 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）

			MessageDigest messageDigest = MessageDigest.getInstance("MD5");

			// 输入的字符串转换成字节数组

			byte[] inputByteArray = input.getBytes();

			// inputByteArray是输入字符串转换得到的字节数组

			messageDigest.update(inputByteArray);

			// 转换并返回结果，也是字节数组，包含16个元素

			byte[] resultByteArray = messageDigest.digest();

			// 字符数组转换成字符串返回

			return byteArrayToHex(resultByteArray);

		} catch (NoSuchAlgorithmException e) {

			return null;

		}

	}

	public static String byteArrayToHex(byte[] byteArray) {

		// 首先初始化一个字符数组，用来存放每个16进制字符

		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

		char[] resultCharArray = new char[byteArray.length * 2];

		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

		int index = 0;

		for (byte b : byteArray) {

			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];

			resultCharArray[index++] = hexDigits[b & 0xf];

		}

		// 字符数组组合成字符串返回

		return new String(resultCharArray);

	}

	public static String encrypt_pwd(String pwd) {
		String encrypt_salt1 = "Nq@*^b&";
		String encrypt_salt2 = "pgq8e!(";
		// 两次md5 加密
		return stringMD5(stringMD5(encrypt_salt1 + pwd + encrypt_salt2));
	}

	public static HashMap<String, String> getParams(String raw_params) {
		HashMap<String, String> params = new HashMap<String, String>();
		if(raw_params!=null){
			for (String key_value_pair : raw_params.split(",")) {
				if (key_value_pair.contains("=")) {
					String[] key_value = key_value_pair.split("=");
					params.put(key_value[0].trim(), key_value[1].trim());
				}
	
			}
		}
		return params;
	}
}
