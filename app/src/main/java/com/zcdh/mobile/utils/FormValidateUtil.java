/**
 * 
 * @author YJN, 2013-10-29 下午10:33:15
 */
package com.zcdh.mobile.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YJN, 2013-10-29 下午10:33:15
 * 数据验证
 */
public class FormValidateUtil {
	//验证邮箱格式
    public static boolean isEmail(String strEmail) {	
    	String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
    	Pattern p = Pattern.compile(strPattern);
    	Matcher m = p.matcher(strEmail);
    	return m.matches();
    	
    }
    //手机号码验证
    public static boolean isMobileNO(String mobiles) {
    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9,5-9]))\\d{8}$");     
        Matcher m = p.matcher(mobiles);     
        System.out.println(m.matches()+"---");     
        return m.matches();   
    }
}
