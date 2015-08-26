package com.zcdh.mobile.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class SMSUtils {
	private static final String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	/**
     * 匹配短信中间的4个数字（验证码等）
     *
     * @param hyq
     * @return
     */
    public static String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(SMSUtils.patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
