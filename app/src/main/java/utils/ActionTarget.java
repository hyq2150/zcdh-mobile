/**
 * 
 * @author jeason, 2014-4-15 下午5:53:13
 */
package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jeason, 2014-4-15 下午5:53:13
 */
public class ActionTarget {

	public Object invokeMethod(Object owner, String methodName, Object... args) {

		Class ownerClass = owner.getClass();

		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Method method;
		try {
			method = getMethod(ownerClass, methodName, argsClass);
			if (args.length == 0) {
				return method.invoke(owner);
			}
			return method.invoke(owner, args);

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public Method getMethod(Class<?> clazz, String name, Class[] argsClass) throws NoSuchMethodException {
		try {
			return clazz.getDeclaredMethod(name, argsClass);
		} catch (NoSuchMethodException e) {
			if (clazz == Object.class) {
				throw e;
			}
			return getMethod(clazz.getSuperclass(), name, argsClass);
		}
	}
}
