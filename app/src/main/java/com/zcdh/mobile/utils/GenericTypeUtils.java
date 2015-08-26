package com.zcdh.mobile.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 处理泛型类型
 * @author yangjiannan
 *
 */
public class GenericTypeUtils {

	/**
	 * 获取泛型的参数类型
	 * @param parameterizedType
	 * @return
	 */
	public static Type getGenericTypeRawType(ParameterizedType  parameterizedType){
		Type[] typeParameter = ((Class<?>) parameterizedType
				.getRawType()).getTypeParameters();
		Type[] actualTypeArgument = parameterizedType
				.getActualTypeArguments();
		
		if(actualTypeArgument!=null && actualTypeArgument.length>0){
			return actualTypeArgument[0];
		}
		return null;
	}
}
