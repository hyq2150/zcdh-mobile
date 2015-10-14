package com.zcdh.mobile.app.views.iflytek;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.text.TextUtils;

//import com.iflytek.speech.ErrorCode;
//import com.iflytek.speech.SpeechError;
/**
 * 对云端返回的Json结果进行解析
 * @author iFlytek
 * @since 20131211
 */
public class JsonParser {
	
	/**
	 * 听写结果的Json格式解析
	 * @param json
	 * @return
	 */
	public static String parseIatResult(String json) {
		if(TextUtils.isEmpty(json))
			return "";
		
		StringBuilder ret = new StringBuilder();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 听写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret.toString();
	}
	
	/**
	 * 识别结果的Json格式解析
	 * @param json
	 * @return
	 */
	public static String parseGrammarResult(String json) {
		StringBuilder ret = new StringBuilder();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				for(int j = 0; j < items.length(); j++)
				{
					JSONObject obj = items.getJSONObject(j);
					if(obj.getString("w").contains("nomatch"))
					{
						ret.append("没有匹配结果.");
						return ret.toString();
					}
					ret.append("【结果】").append(obj.getString("w"));
					ret.append("【置信度】").append(obj.getInt("sc"));
					ret.append("\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.append("没有匹配结果.");
		} 
		return ret.toString();
	}
	
	/**
	 * 语义结果的Json格式解析
	 * @param json
	 * @return
	 */
	public static String parseUnderstandResult(String json) {
		StringBuilder ret = new StringBuilder();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			ret.append("【应答码】").append(joResult.getString("rc")).append("\n");
			ret.append("【转写结果】").append(joResult.getString("text")).append("\n");
			ret.append("【服务名称】").append(joResult.getString("service")).append("\n");
			ret.append("【操作名称】").append(joResult.getString("operation")).append("\n");
			ret.append("【完整结果】").append(json);
		} catch (Exception e) {
			e.printStackTrace();
			ret.append("没有匹配结果.");
		} 
		return ret.toString();
	}
}
