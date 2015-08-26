package com.zcdh.mobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zcdh.core.nio.except.ZcdhException;

public class JsonUtil {
	/** jackson json 数据对象序列化工具 */
	static final ObjectMapper objectMapper = new ObjectMapper() //
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false) // 序列化空对象不警告
			.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false) // 控制不序列化
			.configure(MapperFeature.USE_ANNOTATIONS, true) // 不序列化注解
			.setSerializationInclusion(Include.NON_NULL)// 过滤null属性
			.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
			.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //反序列化成对象时忽略未知的属性
	
//	 .configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS , false)
	// .configure(MapperFeature.AUTO_DETECT_FIELDS, true)
	// .configure(MapperFeature.USE_GETTERS_AS_SETTERS , true)
	// .configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME , true)
	// .configure(MapperFeature.AUTO_DETECT_GETTERS, false)
	// .configure(MapperFeature.AUTO_DETECT_SETTERS, false)
	;
	

	/**
	 * 将对象转换为JSON字符串
	 * 
	 * @author caijianqing, 2013-4-3 下午1:27:13
	 * @param obj 被转换对象
	 * @return JSON字符串
	 * @throws NsmsException 转换时发生异常时以 NsmsException 异常抛出
	 */
	public static String toJson(Object obj) throws Exception {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new Exception("序列化JSON字符串时出错：" + e.getMessage());
		}
	}

	/**
	 * 将字符串转换为对象
	 * 
	 * @author caijianqing, 2013-4-3 下午1:26:40
	 * @param json JSON字符串
	 * @param type 类型
	 * @return 相应的转换结果
	 * @throws NsmsException 转换时发生异常时以 NsmsException 异常抛出
	 */
	public static Object toObject(String json, Type type) throws ZcdhException {
		try {
			//json = "{\"account_id\":\"32\",\"user_id\":\"33\",\"account\":\"15952682485\",\"pwd\":\"wwwwww\",\"status\":\"1\",\"resp_status\":\"0\"}";
			return objectMapper.readValue(json, objectMapper.getTypeFactory().constructType(type));

		} catch (JsonParseException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		} catch (JsonMappingException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		} catch (IOException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		}
	}

	/**
	 * 将字符串转换为对象
	 * 
	 * @author caijianqing, 2013-4-3 下午1:26:40
	 * @param json JSON字符串
	 * @param type 类型
	 * @return 相应的转换结果
	 * @throws NsmsException 转换时发生异常时以 NsmsException 异常抛出
	 */
	public static <T> T toObject(String json, Class<T> clazz) throws ZcdhException {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		} catch (JsonMappingException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		} catch (IOException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		}
	}

	public static <T> T toObject(String json, TypeReference<T> clazz) throws ZcdhException {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		} catch (JsonMappingException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		} catch (IOException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		}
	}

	/**
	 * 在数据流中反序列化
	 * 
	 * @author caijianqing, 2013-7-3 下午12:39:18
	 * @param stream
	 * @param type
	 * @return
	 * @throws ZcdhException
	 */
	public static Object toObject(InputStream stream, Type type) throws ZcdhException {
		try {
			return objectMapper.readValue(stream, objectMapper.getTypeFactory().constructType(type));
		} catch (JsonParseException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		} catch (JsonMappingException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		} catch (IOException e) {
			throw new ZcdhException("反序列化JSON字符串时出错：" + e.getMessage());
		}
	}

	/**
	 * 把结果保存在输出流中
	 * 
	 * @author caijianqing, 2013-7-3 下午12:39:18
	 * @param stream
	 * @param obj
	 * @return
	 * @throws ZcdhException
	 */
	public static void toStream(OutputStream stream, Object obj) throws ZcdhException {
		try {
			objectMapper.writeValue(stream, obj);
		} catch (JsonParseException e) {
			throw new ZcdhException("序列化JSON字符串时出错：" + e.getMessage());
		} catch (JsonMappingException e) {
			throw new ZcdhException("序列化JSON字符串时出错：" + e.getMessage());
		} catch (IOException e) {
			throw new ZcdhException("序列化JSON字符串时出错：" + e.getMessage());
		}
	}

//	public static void main(String[] args) {
//		/*
//		MsgRequest req = new MsgRequest(); // 发送请求
//		req.setServicename("Userservice");
//		req.setMethod("getUser");
//		HashMap<String,String> params=new HashMap<String,String>();
//		params.put("vesion", "1");
//		req.setParams(params);
//		String str="";
//		try {
//			 str= JsonUtil.toJson(req);
//			 System.out.println(str);
//		} catch(Exception e){
//			e.printStackTrace();
//		}
//		*/
////		HashMap<String,String> parmas=new HashMap<String,String>();
//		
//		
//		//parmas.put("id", "1123");
//		//Integer s= JsonUtil.toObject(parmas.get("id"), Integer.class);
//		//System.out.println(s);
////		parmas=null;
////		try {
////			String a=JsonUtil.toJson(parmas);
////			System.out.println(a);
////			//JsonUtil.toObject(a, parmas.);
////			HashMap<String,String> parmas1=(HashMap<String, String>) JsonUtil.toObject(a,new TypeReference<HashMap<String,String>>(){} );
////			System.out.println(parmas1);
////			File file=new File("");
////			if(file instanceof File) {
////				System.out.print(file);
////			}
////			
////			String b="null";
////			MsgAttach attach=JsonUtil.toObject(b,MsgAttach.class) ;
////			System.out.println(attach);
////			
////			MsgRequest request=new MsgRequest();
////			request.setService("accountService");
////			request.setMethod("login");
////			HashMap<String,Object> params=new HashMap<String,Object>();
////			params.put("account", "13392966895");
////			params.put("pwd", "123456");
////			request.setParams(params);
////			String josn=JsonUtil.toJson(request);
////			System.out.println(josn);
////			
////			
////			//////////////////
////			String testUnKnowPro = "{\"reqCode\":\"87ce378960854c618436392fbc6e797f\"}"; //josn.substring(0, josn.length()-1)+",\"name\":\"tom\"}";
////			System.out.println(testUnKnowPro);
////			JsonUtil.toObject(testUnKnowPro, MsgRequest.class);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//		
//		
//	}
	
}
