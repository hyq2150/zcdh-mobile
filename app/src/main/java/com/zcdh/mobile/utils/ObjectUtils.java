package com.zcdh.mobile.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.zcdh.core.annotation.Param;
import com.zcdh.core.annotation.RpcMethod;
import com.zcdh.mobile.api.model.JobEducationDTO;
import com.zcdh.mobile.api.model.JobTrainDTO;
import com.zcdh.mobile.api.model.JobUserInfoDTO;

public class ObjectUtils {

	/**
	 * 从文本转换至指定的类型的对象
	 * 
	 * @param clazz
	 *            目标类型
	 * @param _value
	 *            文本值
	 * @param style
	 *            样式，Date时起作用
	 * @return
	 * @throws ParseException
	 */
	public static Object convertToBaseType(Class<?> clazz, String _value,
			String style) throws ParseException {
		Object value = null;
		if (String.class.isAssignableFrom(clazz)) {
			// String
			value = _value;
		} else if (StringUtils.isBlank(_value)) {
			value = null;
		} else if (Integer.class.isAssignableFrom(clazz)
				|| int.class.isAssignableFrom(clazz)) {
			// Integer
			if (!StringUtils.isNumber(_value)) {
				throw new ParseException("参数数据类型错误：" + _value, 0);
			}
			value = Integer.valueOf(_value);
		} else if (Float.class.isAssignableFrom(clazz)
				|| float.class.isAssignableFrom(clazz)) {
			// Double
			if (!StringUtils.isFloat(_value)) {
				throw new ParseException("参数数据类型错误：" + _value, 0);
			}
			value = Float.valueOf(_value);
		} else if (Double.class.isAssignableFrom(clazz)
				|| double.class.isAssignableFrom(clazz)) {
			// Double
			if (!StringUtils.isFloat(_value)) {
				throw new ParseException("参数数据类型错误：" + _value, 0);
			}
			value = Double.valueOf(_value);
		} else if (Long.class.isAssignableFrom(clazz)
				|| long.class.isAssignableFrom(clazz)) {
			// Long
			if (!StringUtils.isNumber(_value)) {
				throw new ParseException("参数数据类型错误：" + _value, 0);
			}
			value = Long.valueOf(_value);
		} else if (Boolean.class.isAssignableFrom(clazz)
				|| boolean.class.isAssignableFrom(clazz)) {
			// Boolean
			if ("on".equals(_value))
				_value = "true";
			if (!StringUtils.isBoolean(_value)) {
				throw new ParseException("参数数据类型错误：" + _value, 0);
			}
			value = StringUtils.convertToBoolean(_value);
		} else if (Date.class.isAssignableFrom(clazz)) {
			// Date
			if (!StringUtils.isBlank(_value)) {
				if (style == null) {
					throw new ParseException("Date类型数据的style为null", 0);
				}
				SimpleDateFormat sdf = new SimpleDateFormat(style);
				value = sdf.parse(_value);
				sdf = null;
			}
		} else {
			// Unkown
			throw new ParseException(clazz.getName(), 0);
		}
		return value;
	}

	/**
	 * 验证是否为基本类型（八大基本类型）
	 * 
	 * @param clazz
	 * @return
	 * @author caijianqing, 2013-1-5 下午7:30:03
	 */
	public static boolean isBasicType(Class<?> clazz) {
		if (String.class.isAssignableFrom(clazz)) {
		} else if (Integer.class.isAssignableFrom(clazz)
				|| int.class.isAssignableFrom(clazz)) {
		} else if (Long.class.isAssignableFrom(clazz)
				|| long.class.isAssignableFrom(clazz)) {
		} else if (Boolean.class.isAssignableFrom(clazz)
				|| boolean.class.isAssignableFrom(clazz)) {
		} else if (Character.class.isAssignableFrom(clazz)
				|| char.class.isAssignableFrom(clazz)) {
		} else if (Short.class.isAssignableFrom(clazz)
				|| short.class.isAssignableFrom(clazz)) {
		} else if (Float.class.isAssignableFrom(clazz)
				|| float.class.isAssignableFrom(clazz)) {
		} else if (Double.class.isAssignableFrom(clazz)
				|| double.class.isAssignableFrom(clazz)) {
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 是否为基本对象、日期或枚举
	 * 
	 * @param clazz
	 * @return
	 */
	// private static boolean isBasicData(Class<?> clazz) {
	// if (isBasicType(clazz)) {
	// } else if (Date.class.isAssignableFrom(clazz)) {
	// } else if (java.sql.Date.class.isAssignableFrom(clazz)) {
	// } else if (clazz.isEnum()) {
	// } else {
	// return false;
	// }
	// return true;
	// }

	/**
	 * 根据注解获取方法体
	 * 
	 * @param clazz
	 * @param methodName
	 * @return
	 * @author danny, 2013-9-21 下午5:18:53
	 */
	public static Method getMethod(Class<?> clazz, String methodName) {

		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			// System.out.println("获取全部方法的方法(含父类):"+m.toString());
			RpcMethod rpcMehod = method.getAnnotation(RpcMethod.class);
			if (rpcMehod != null
					&& rpcMehod.value().equalsIgnoreCase(methodName)) {
				return method;
			}
		}

		return null;
	}

	public static String getParamAnnotationName(Annotation[] annotations) {
		String result = null;
		if (annotations == null)
			return null;
		if (annotations.length <= 0)
			return null;
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == Param.class) {
				Param param = (Param) annotation;
				result = param.value();
				break;
			}
		}
		return result;
	}

	public static Param getParamAnnotation(Annotation[] annotations) {
		Param result = null;
		if (annotations == null)
			return null;
		if (annotations.length <= 0)
			return null;
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == Param.class) {
				Param param = (Param) annotation;
				result = param;
				break;
			}
		}
		return result;
	}

	/***
	 * 判断对象是否为空 <blockquote> 1.对象为空<br>
	 * 2.当这个为数组的时候为空<br>
	 * 3.对象为集合的时候为空<br>
	 * 4.对象为map<br>
	 * 5.对象为字符串 当字符串只是为""和“null”的时候也表示这个对象为空<br>
	 * </blockquote>
	 * 
	 * @param obj
	 *            判断的对象
	 * @return 判断为空返回true 否则为false
	 * @author focus, 2013-10-23 下午2:26:36
	 */
	public static boolean isEmpty(Object obj) {

		if (obj == null) // 对象为空
			return true;
		if (obj instanceof String) // 对象为字符串
			return ((String) obj).trim().length() == 0 ? true : ((String) obj)
					.trim().equalsIgnoreCase("null");
		if (obj instanceof Object[]) // 对象时数组
			return (((Object[]) obj).length) == 0;
		if (obj instanceof Collection) // 对象时集合
			return ((Collection) obj).isEmpty();
		if (obj instanceof Map) // 对象时散列表Map
			return ((Map) obj).isEmpty();
		return false;
	}

	/**
	 * 把desObj目标对象的get方法里面的值，放进源对象srcObj中的set方法里面去 该方法仅仅针对于get set 的实体类方法<br>
	 * 
	 * <blockquote> 1.当前只是针对于 srcObj对象中有值的属性，赋值到desObj对象中去<br>
	 * 2.而当srcObj对象中没有值的属性，这直接使用desObj中的属性不变<br>
	 * </blockquote>
	 * 
	 * @param srcObj
	 *            源对象
	 * @param desObj
	 *            目标对象
	 * @return 返回已经更新过的对象的值
	 * @throws Exception
	 * @author focus, 2013-11-15 下午2:51:45
	 */
	public static Object updateObject(Object srcObj, Object desObj) {
		// 1.检查参数
		if (srcObj == null)
			return desObj;
		if (desObj == null)
			return srcObj;

		// 2.取得源对象的字节码对象
		Class<?> srcClazz = srcObj.getClass();
		Method[] getMehods = srcClazz.getDeclaredMethods();

		// 3.取得目标对象的字节码对象
		Class<?> desClazz = desObj.getClass();
		Method[] setMehods = desClazz.getDeclaredMethods();

		for (Method g : getMehods) {

			try {

				// 只取方法名称为get的方法
				if (g.getName().startsWith("get")) {
					// 取得当前方法的返回值
//					Object gValue = g.invoke(srcObj, null);
					Object gValue = g.invoke(srcObj, new Object[]{});
					// 方法名称
					String gname = g.getName().substring(2);

					if (isEmpty(gValue))
						continue; // 如果值为空则直接跳过

					for (Method s : setMehods) {

						// 只是取得方法名称为set的方法
						if (s.getName().startsWith("set"))
							// 去掉方法的前三个字母、 如果先等的话则取值
							if (s.getName().substring(2).equals(gname)) {
								s.invoke(desObj, gValue);
							}
					}
				}
			} catch (Exception e) {

			}
		}
		return desObj;
	}

	public static void main(String[] args) throws Exception {
		// CopyDesc<Blog> desc = CopyDesc.create(Blog.class);
		// desc.add(".id");
		// desc.add(".content");
		// desc.add(".client.*");
		// desc.add(".commentList.blog.client.name");
		// desc.add(".commentList.content");
		// Blog blog = new Blog();
		// Client client = new Client();
		// client.setName("ios");
		// blog.setBlogType(BlogType.MUSIC);
		// blog.setContent("hellobaby");
		// blog.setClient(client);
		// List<Comment> list = new ArrayList<Comment>();
		// Comment comment = new Comment();
		// comment.setContent("huifu");
		// list.add(comment);
		// blog.setCommentList(list);
		//
		// Blog blog2 = copyEntity(blog, desc);
		// System.out.println(blog2.getContent());
		// System.out.println(blog2.getClient().getName());
		// System.out.println(blog2.getCommentList().get(0).getContent());
		System.out.println(isEmpty(null));

	}

	public static boolean compareJobUserInfo(JobUserInfoDTO compareDto,
			JobUserInfoDTO comparedDto) {
		if (compareDto == comparedDto)
			return true;
		if (comparedDto == null)
			return false;
		if (compareDto.getClass() != comparedDto.getClass())
			return false;
		JobUserInfoDTO other = (JobUserInfoDTO) comparedDto;
		if (compareDto.getAddressCode() == null) {
			if (other.getAddressCode() != null)
				return false;
		} else if (!compareDto.getAddressCode().equals(other.getAddressCode()))
			return false;
		if (compareDto.getAddressName() == null) {
			if (other.getAddressName() != null)
				return false;
		} else if (!compareDto.getAddressName().equals(other.getAddressName()))
			return false;
		if (compareDto.getBirth() == null) {
			if (other.getBirth() != null)
				return false;
		} else if (!compareDto.getBirth().equals(other.getBirth()))
			return false;
		if (compareDto.getCredentialTypeCode() == null) {
			if (other.getCredentialTypeCode() != null)
				return false;
		} else if (!compareDto.getCredentialTypeCode().equals(
				other.getCredentialTypeCode()))
			return false;
		if (compareDto.getCredentialTypeName() == null) {
			if (other.getCredentialTypeName() != null)
				return false;
		} else if (!compareDto.getCredentialTypeName().equals(
				other.getCredentialTypeName()))
			return false;
		if (compareDto.getCredentials() == null) {
			if (other.getCredentials() != null)
				return false;
		} else if (!compareDto.getCredentials().equals(other.getCredentials()))
			return false;
		if (compareDto.getDegreeCode() == null) {
			if (other.getDegreeCode() != null)
				return false;
		} else if (!compareDto.getDegreeCode().equals(other.getDegreeCode()))
			return false;
		if (compareDto.getDegreeName() == null) {
			if (other.getDegreeName() != null)
				return false;
		} else if (!compareDto.getDegreeName().equals(other.getDegreeName()))
			return false;
		if (compareDto.getEmail() == null) {
			if (other.getEmail() != null)
				return false;
		} else if (!compareDto.getEmail().equals(other.getEmail()))
			return false;
		if (compareDto.getFileCode() == null) {
			if (other.getFileCode() != null)
				return false;
		} else if (!compareDto.getFileCode().equals(other.getFileCode()))
			return false;
		if (compareDto.getGerder() == null) {
			if (other.getGerder() != null)
				return false;
		} else if (!compareDto.getGerder().equals(other.getGerder()))
			return false;
		if (compareDto.getGerderCode() == null) {
			if (other.getGerderCode() != null)
				return false;
		} else if (!compareDto.getGerderCode().equals(other.getGerderCode()))
			return false;
		if (compareDto.getIsMarried() == null) {
			if (other.getIsMarried() != null)
				return false;
		} else if (!compareDto.getIsMarried().equals(other.getIsMarried()))
			return false;
		if (compareDto.getIsMarriedCode() == null) {
			if (other.getIsMarriedCode() != null)
				return false;
		} else if (!compareDto.getIsMarriedCode().equals(
				other.getIsMarriedCode()))
			return false;
		if (compareDto.getJobStatusCode() == null) {
			if (other.getJobStatusCode() != null)
				return false;
		} else if (!compareDto.getJobStatusCode().equals(
				other.getJobStatusCode()))
			return false;
		if (compareDto.getJobStautsName() == null) {
			if (other.getJobStautsName() != null)
				return false;
		} else if (!compareDto.getJobStautsName().equals(
				other.getJobStautsName()))
			return false;
		if (compareDto.getLaguageTypeCount() == null) {
			if (other.getLaguageTypeCount() != null)
				return false;
		} else if (!compareDto.getLaguageTypeCount().equals(
				other.getLaguageTypeCount()))
			return false;
		if (compareDto.getMobile() == null) {
			if (other.getMobile() != null)
				return false;
		} else if (!compareDto.getMobile().equals(other.getMobile()))
			return false;
		if (compareDto.getNation() == null) {
			if (other.getNation() != null)
				return false;
		} else if (!compareDto.getNation().equals(other.getNation()))
			return false;
		if (compareDto.getNativePlaceCode() == null) {
			if (other.getNativePlaceCode() != null)
				return false;
		} else if (!compareDto.getNativePlaceCode().equals(
				other.getNativePlaceCode()))
			return false;
		if (compareDto.getNativePlaceName() == null) {
			if (other.getNativePlaceName() != null)
				return false;
		} else if (!compareDto.getNativePlaceName().equals(
				other.getNativePlaceName()))
			return false;
		if (compareDto.getPanmeldenCode() == null) {
			if (other.getPanmeldenCode() != null)
				return false;
		} else if (!compareDto.getPanmeldenCode().equals(
				other.getPanmeldenCode()))
			return false;
		if (compareDto.getPanmeldenName() == null) {
			if (other.getPanmeldenName() != null)
				return false;
		} else if (!compareDto.getPanmeldenName().equals(
				other.getPanmeldenName()))
			return false;
		if (compareDto.getServiceYearCode() == null) {
			if (other.getServiceYearCode() != null)
				return false;
		} else if (!compareDto.getServiceYearCode().equals(
				other.getServiceYearCode()))
			return false;
		if (compareDto.getServiceYearName() == null) {
			if (other.getServiceYearName() != null)
				return false;
		} else if (!compareDto.getServiceYearName().equals(
				other.getServiceYearName()))
			return false;
		if (compareDto.getTalentTypeCode() == null) {
			if (other.getTalentTypeCode() != null)
				return false;
		} else if (!compareDto.getTalentTypeCode().equals(
				other.getTalentTypeCode()))
			return false;
		if (compareDto.getTalentTypeName() == null) {
			if (other.getTalentTypeName() != null)
				return false;
		} else if (!compareDto.getTalentTypeName().equals(
				other.getTalentTypeName()))
			return false;
		if (compareDto.getUserId() == null) {
			if (other.getUserId() != null)
				return false;
		} else if (!compareDto.getUserId().equals(other.getUserId()))
			return false;
		if (compareDto.getUserName() == null) {
			if (other.getUserName() != null)
				return false;
		} else if (!compareDto.getUserName().equals(other.getUserName()))
			return false;
		return true;
	}

	public static boolean compareJobEduDTO(JobEducationDTO compareDto,
			JobEducationDTO comparedDto) {

		if (compareDto == comparedDto)
			return true;
		if (comparedDto == null)
			return false;
		if (compareDto.getClass() != comparedDto.getClass())
			return false;
		JobEducationDTO other = (JobEducationDTO) comparedDto;
		if (compareDto.getContent() == null) {
			if (other.getContent() != null)
				return false;
		} else if (!compareDto.getContent().equals(other.getContent()))
			return false;
		if (compareDto.getDegreeCode() == null) {
			if (other.getDegreeCode() != null)
				return false;
		} else if (!compareDto.getDegreeCode().equals(other.getDegreeCode()))
			return false;
		if (compareDto.getDegreeName() == null) {
			if (other.getDegreeName() != null)
				return false;
		} else if (!compareDto.getDegreeName().equals(other.getDegreeName()))
			return false;
		if (compareDto.getEduId() == null) {
			if (other.getEduId() != null)
				return false;
		} else if (!compareDto.getEduId().equals(other.getEduId()))
			return false;
		if (compareDto.getEndTime() == null) {
			if (other.getEndTime() != null)
				return false;
		} else if (!compareDto.getEndTime().equals(other.getEndTime()))
			return false;
		if (compareDto.getMajorCode() == null) {
			if (other.getMajorCode() != null)
				return false;
		} else if (!compareDto.getMajorCode().equals(other.getMajorCode()))
			return false;
		if (compareDto.getMajorName() == null) {
			if (other.getMajorName() != null)
				return false;
		} else if (!compareDto.getMajorName().equals(other.getMajorName()))
			return false;
		if (compareDto.getSchoolCode() == null) {
			if (other.getSchoolCode() != null)
				return false;
		} else if (!compareDto.getSchoolCode().equals(other.getSchoolCode()))
			return false;
		if (compareDto.getSchoolName() == null) {
			if (other.getSchoolName() != null)
				return false;
		} else if (!compareDto.getSchoolName().equals(other.getSchoolName()))
			return false;
		if (compareDto.getStartTime() == null) {
			if (other.getStartTime() != null)
				return false;
		} else if (!compareDto.getStartTime().equals(other.getStartTime()))
			return false;
		if (compareDto.getUserId() == null) {
			if (other.getUserId() != null)
				return false;
		} else if (!compareDto.getUserId().equals(other.getUserId()))
			return false;
		return true;
	}

	public static boolean compareTrainningDTO(JobTrainDTO compareDto,
			JobTrainDTO comparedDto) {

		if (compareDto == comparedDto)
			return true;
		if (comparedDto == null)
			return false;
		if (compareDto.getClass() != comparedDto.getClass())
			return false;
		JobTrainDTO other = (JobTrainDTO) comparedDto;
		if (compareDto.getContent() == null) {
			if (other.getContent() != null)
				return false;
		} else if (!compareDto.getContent().equals(other.getContent()))
			return false;
		if (compareDto.getCourseCode() == null) {
			if (other.getCourseCode() != null)
				return false;
		} else if (!compareDto.getCourseCode().equals(other.getCourseCode()))
			return false;
		if (compareDto.getCourseName() == null) {
			if (other.getCourseName() != null)
				return false;
		} else if (!compareDto.getCourseName().equals(other.getCourseName()))
			return false;
		if (compareDto.getCredentialCode() == null) {
			if (other.getCredentialCode() != null)
				return false;
		} else if (!compareDto.getCredentialCode().equals(
				other.getCredentialCode()))
			return false;
		if (compareDto.getCredentialName() == null) {
			if (other.getCredentialName() != null)
				return false;
		} else if (!compareDto.getCredentialName().equals(
				other.getCredentialName()))
			return false;
		if (compareDto.getEndTime() == null) {
			if (other.getEndTime() != null)
				return false;
		} else if (!compareDto.getEndTime().equals(other.getEndTime()))
			return false;
		if (compareDto.getStartTime() == null) {
			if (other.getStartTime() != null)
				return false;
		} else if (!compareDto.getStartTime().equals(other.getStartTime()))
			return false;
		if (compareDto.getTrainId() == null) {
			if (other.getTrainId() != null)
				return false;
		} else if (!compareDto.getTrainId().equals(other.getTrainId()))
			return false;
		if (compareDto.getTrainInstitution() == null) {
			if (other.getTrainInstitution() != null)
				return false;
		} else if (!compareDto.getTrainInstitution().equals(
				other.getTrainInstitution()))
			return false;
		if (compareDto.getUserId() == null) {
			if (other.getUserId() != null)
				return false;
		} else if (!compareDto.getUserId().equals(other.getUserId()))
			return false;
		return true;
	}

	/**
	 * compare two object
	 * 
	 * @param actual
	 * @param expected
	 * @return <ul>
	 *         <li>if both are null, return true</li>
	 *         <li>return actual.{@link Object#equals(Object)}</li>
	 *         </ul>
	 */
	public static boolean isEquals(Object actual, Object expected) {
		return actual == expected
				|| (actual == null ? expected == null : actual.equals(expected));
	}

	/**
	 * convert long array to Long array
	 * 
	 * @param source
	 * @return
	 */
	public static Long[] transformLongArray(long[] source) {
		Long[] destin = new Long[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * convert int array to Integer array
	 * 
	 * @param source
	 * @return
	 */
	public static Integer[] transformIntArray(int[] source) {
		Integer[] destin = new Integer[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}
}
