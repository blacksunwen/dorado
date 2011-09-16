package com.bstek.dorado.util.method;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.NumberUtils;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.util.proxy.ProxyBeanUtils;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

/**
 * 用于辅助类方法自动匹配功能的工具类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 3, 2008
 */
public abstract class MethodAutoMatchingUtils {
	private static final Class<?>[] EMPTY_TYPES = new Class<?>[0];
	private static final Object[] EMPTY_ARGS = new Object[0];
	private static final String[] EMPTY_NAMES = new String[0];

	private static Map<Object, Method[]> methodCache = new Hashtable<Object, Method[]>();
	private static Paranamer paranamer = new CachingParanamer(
			new BytecodeReadingParanamer());
	private static Collection<ParameterFactory> systemOptionalParameters;

	private static class IgnoreType {
	};

	private static Collection<ParameterFactory> getSystemOptionalParameters()
			throws Exception {
		if (systemOptionalParameters == null) {
			Context context = Context.getCurrent();
			SystemOptionalParametersFactory systemOptionalParametersFactory = (SystemOptionalParametersFactory) context
					.getServiceBean("systemOptionalParametersFactory");
			if (systemOptionalParametersFactory != null) {
				systemOptionalParameters = systemOptionalParametersFactory
						.getOptionalParameters();
			}
			if (systemOptionalParameters == null) {
				systemOptionalParameters = new ArrayList<ParameterFactory>();
			}
		}
		return systemOptionalParameters;
	}

	private static Object getCacheKey(Class<?> cl, String methodName) {
		return new MultiKey(cl, methodName);
	}

	/**
	 * 查找给定的Class类型在Class类型数组中的下标位置，如果未找到相容的类型则返回-1。<br>
	 * 此处所指的相容是指Class类型数组中的某类型与要查找的Class类型相同，或是要查找的Class类型的父类型。
	 * 
	 * @param types
	 *            Class类型数组。
	 * @param type
	 *            要查找的Class类型。
	 * @return 下标位置。
	 */
	public static int indexOfTypes(Class<?>[] types, Class<?> type) {
		int index = -1;
		for (int i = 0; i < types.length; i++) {
			Class<?> t = types[i];
			if (isTypeAssignableFrom(type, t) > 0) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 返回给定的类中匹配某一方法名的所有方法。
	 * 
	 * @param cl
	 *            被查找的类。
	 * @param methodName
	 *            方法名。
	 * @return 找到的方法。
	 */
	public static Method[] getMethodsByName(Class<?> cl, String methodName) {
		cl = ProxyBeanUtils.getProxyTargetType(cl);
		Object cacheKey = getCacheKey(cl, methodName);
		Method[] methods = methodCache.get(cacheKey);
		if (methods == null) {
			List<Method> methodList = new ArrayList<Method>();
			Method[] allMethods = cl.getMethods();
			for (Method method : allMethods) {
				if (method.getName().equals(methodName)) {
					methodList.add(method);
				}
			}

			methods = new Method[methodList.size()];
			methodList.toArray(methods);
			methodCache.put(cacheKey, methods);
		}
		return methods;
	}

	/**
	 * 在给定的一组方法中根据方法名、调用参数和返回值类型查找一个匹配的方法。
	 * 
	 * @param methods
	 *            方法数组。
	 * @param args
	 *            调用参数。
	 * @param returnType
	 *            返回值类型，如果为null则表示忽略对返回值类型的判断。
	 * @return 找到的方法。
	 * @throws MethodAutoMatchingException
	 *             如果找到了一个以上的匹配方法或没有找到任何匹配的方法将抛出此异常。
	 */
	public static Method findMatchingMethod(Method[] methods, Object[] args,
			Class<?> returnType) throws MethodAutoMatchingException {
		Class<?>[] argTypes = args2ArgTypes(args);
		return findMatchingMethod(methods, argTypes, returnType);
	}

	private static void trimClassTypes(Class<?>[] cls) {
		for (int i = 0; i < cls.length; i++) {
			Class<?> type = cls[i];
			if (type == null) {
				type = Object.class;
			} else {
				type = ProxyBeanUtils.getProxyTargetType(type);
			}
			cls[i] = type;
		}
	}

	/**
	 * 在给定的一组方法中根据方法名、方法参数类型和返回值类型查找一个匹配的方法。<br>
	 * 注意，此方法将忽略方法参数的顺序。
	 * 
	 * @param methods
	 *            方法数组。
	 * @param requiredTypes
	 *            必须提供的方法参数类型。
	 * @param exactTypes
	 *            类型必须严格匹配的方法参数类型。
	 * @param optionalTypes
	 *            可选的方法参数类型。
	 * @param returnType
	 *            返回值类型，如果为null则表示忽略对返回值类型的判断。
	 * @return 找到的方法的描述对象。
	 * @throws MethodAutoMatchingException
	 *             如果找到了一个以上的匹配方法或没有找到任何匹配的方法将抛出此异常。
	 */
	private static MethodDescriptor findMatchingMethod(Method[] methods,
			Class<?>[] requiredTypes, Class<?>[] exactTypes,
			Class<?>[] optionalTypes, Class<?> returnType)
			throws MethodAutoMatchingException {
		if (requiredTypes == null) {
			requiredTypes = EMPTY_TYPES;
		} else {
			trimClassTypes(requiredTypes);
		}

		if (exactTypes == null) {
			exactTypes = EMPTY_TYPES;
		} else {
			trimClassTypes(exactTypes);
		}

		if (optionalTypes == null) {
			optionalTypes = EMPTY_TYPES;
		} else {
			trimClassTypes(optionalTypes);
		}

		MethodDescriptor methodDescriptor = null;
		for (Method method : methods) {
			MethodDescriptor tmpMethodDescriptor = describMethodIfMatching(
					method, requiredTypes, exactTypes, optionalTypes,
					returnType);
			if (tmpMethodDescriptor != null) {
				if (methodDescriptor != null) {
					int matchingRate = methodDescriptor.getMatchingRate();
					int tmpMatchingRate = tmpMethodDescriptor.getMatchingRate();
					if (matchingRate == tmpMatchingRate) {
						String message = getExceptionMessage(
								"More than one methods matching the following condition",
								methods, requiredTypes, exactTypes,
								optionalTypes, returnType);
						throw new MethodAutoMatchingException(message);
					} else if (tmpMatchingRate < matchingRate) {
						continue;
					}
				}
				methodDescriptor = tmpMethodDescriptor;
			}
		}

		if (methodDescriptor == null) {
			String message = getExceptionMessage(
					"No method could be found which matching the following condition",
					methods, requiredTypes, exactTypes, optionalTypes,
					returnType);
			throw new MethodAutoMatchingException(message);
		}
		return methodDescriptor;
	}

	private static MethodDescriptor describMethodIfMatching(Method method,
			Class<?>[] requiredTypes, Class<?>[] exactTypes,
			Class<?>[] optionalTypes, Class<?> returnType) {
		Class<?>[] argTypes = method.getParameterTypes();
		if (argTypes.length > (requiredTypes.length + exactTypes.length + optionalTypes.length)) {
			return null;
		}

		int[] argIndexs = new int[argTypes.length];
		int[] argMatchingRates = new int[argTypes.length];
		for (int i = 0; i < argIndexs.length; i++) {
			argIndexs[i] = -1;
			argMatchingRates[i] = 0;
		}

		Class<?>[] requiredTypeArray = new Class<?>[requiredTypes.length];
		Class<?>[] exactTypeArray = new Class<?>[exactTypes.length];
		Class<?>[] optionalTypeArray = new Class<?>[optionalTypes.length];
		System.arraycopy(requiredTypes, 0, requiredTypeArray, 0,
				requiredTypes.length);
		System.arraycopy(exactTypes, 0, exactTypeArray, 0, exactTypes.length);
		System.arraycopy(optionalTypes, 0, optionalTypeArray, 0,
				optionalTypes.length);

		// 判断返回类型
		if (returnType != null && !returnType.equals(IgnoreType.class)) {
			Class<?> methodReturnType = method.getReturnType();
			if (isTypeAssignableFrom(returnType, methodReturnType) == 0) {
				methodReturnType = MethodUtils
						.toNonPrimitiveClass(methodReturnType);
				if (isTypeAssignableFrom(returnType, methodReturnType) == 0) {
					return null;
				}
			}
		}

		// 映射所有必须的参数
		for (int i = 0; i < requiredTypeArray.length; i++) {
			Class<?> requiredType = requiredTypeArray[i];
			int matchingArg = -1;
			for (int j = 0; j < argTypes.length; j++) {
				Class<?> argType = argTypes[j];
				if (isTypeAssignableFrom(argType, requiredType) > 0) {
					if (matchingArg == -1) {
						matchingArg = j;
					} else {
						Class<?> conflictType = argTypes[matchingArg];
						if (argType.equals(conflictType)) {
							// 一个以上的参数可匹配该必须的参数
							return null;
						} else if (isTypeAssignableFrom(conflictType, argType) > 0) {
							matchingArg = j;
						}
					}
				}
			}

			// 该必须的参数已找到映射
			if (matchingArg != -1) {
				argIndexs[matchingArg] = i;
				argMatchingRates[matchingArg] = 10;
			} else {
				return null;
			}
		}

		// 映射必须严格匹配的参数
		for (int i = 0; i < exactTypeArray.length; i++) {
			Class<?> exactType = exactTypeArray[i];
			int matchingArg = -1;
			for (int j = 0; j < argTypes.length; j++) {
				Class<?> argType = argTypes[j];
				if (isTypeAssignableFrom(exactType, argType) > 0) {
					if (matchingArg == -1) {
						matchingArg = j;
					} else {
						// 一个以上的参数可匹配该严格匹配的参数
						return null;
					}
				}
			}

			// 该严格匹配的参数已找到映射
			if (matchingArg != -1) {
				if (argMatchingRates[matchingArg] == 0) {
					argIndexs[matchingArg] = requiredTypeArray.length + i;
					argMatchingRates[matchingArg] = 9;
				} else {
					// 与某个以匹配成功的必须的参数冲突
					return null;
				}
			}
		}

		// 处理可选参数
		int conflictArg = -1, matchingRate = 1000;
		for (int i = 0; i < argTypes.length; i++) {
			if (argMatchingRates[i] == 0) {
				Class<?> argType = argTypes[i];
				for (int j = 0; j < optionalTypeArray.length; j++) {
					Class<?> optionalType = optionalTypeArray[j];
					if (optionalType != null) {
						int rate = isTypeAssignableFrom(argType, optionalType);
						if (rate == 0) {
							rate = isTypesCompatible(argType, optionalType);
						}
						if (rate > 0) {
							int originMatchingRate = argMatchingRates[i];
							if (originMatchingRate == 0) {
								argIndexs[i] = requiredTypeArray.length
										+ exactTypeArray.length + j;
								argMatchingRates[i] = rate;
								matchingRate += (rate * 2);
							} else if (conflictArg != -1) {
								// 发现一个以上的可选参数冲突
								return null;
							} else if (originMatchingRate > rate) {
								matchingRate -= (5 - (originMatchingRate - rate)); // 越相似扣分越多
							} else if (rate > originMatchingRate) {
								argIndexs[i] = requiredTypeArray.length
										+ exactTypeArray.length + j;
								argMatchingRates[i] = rate;
								matchingRate -= (5 - (rate - originMatchingRate)); // 越相似扣分越多
							} else {
								// 发现一个可选参数冲突
								argIndexs[i] = -1;
								conflictArg = i;
								matchingRate -= (argTypes.length * 10);
							}
						}
					}
				}

				if (argIndexs[i] != -1) {
					optionalTypeArray[argIndexs[i] - exactTypeArray.length
							- requiredTypeArray.length] = null;
				}
			}
		}

		// 处理冲突的可选参数
		if (conflictArg != -1) {
			Class<?> argType = argTypes[conflictArg];
			for (int i = 0; i < optionalTypeArray.length; i++) {
				Class<?> optionalType = optionalTypeArray[i];
				if (optionalType != null
						&& isTypeAssignableFrom(argType, optionalType) > 0) {
					if (argIndexs[conflictArg] == -1) {
						argIndexs[conflictArg] = requiredTypeArray.length + i;
					} else {
						return null;
					}
				}
			}
		}

		int undetermine = 0, undetermineIndex = -1;
		for (int i = 0; i < argIndexs.length; i++) {
			if (argIndexs[i] == -1) {
				undetermine++;
				undetermineIndex = i;
			}
		}

		// 如果只有一个可选参数，那么尽可能转换类型并匹配。
		if (undetermine == 1 && optionalTypes.length == 1) {
			Class<?> argType = argTypes[undetermineIndex];
			if (isSimpleType(argType) && isSimpleType(optionalTypes[0])) {
				argIndexs[undetermineIndex] = requiredTypeArray.length
						+ exactTypeArray.length;
				undetermine = 0;
				matchingRate -= 200;
			}
		}

		if (undetermine > 0)
			return null;
		return new MethodDescriptor(method, argIndexs, matchingRate);
	}

	public static boolean isSimpleType(Class<?> cl) {
		return (String.class.equals(cl) || cl.isPrimitive()
				|| Boolean.class.equals(cl)
				|| Number.class.isAssignableFrom(cl) || cl.isEnum());
	}

	public static String[] getParameterNames(Method method) {
		return paranamer.lookupParameterNames(method);
	}

	private static MethodDescriptor describMethodIfMatching(Method method,
			String[] requiredParameterNames, String[] optionalParameterNames)
			throws SecurityException, NoSuchMethodException {
		Method lookupMethod = method;
		Class<?> cl = lookupMethod.getDeclaringClass();
		if (ProxyBeanUtils.isProxy(cl)) {
			lookupMethod = ProxyBeanUtils.getProxyTargetType(cl).getMethod(
					method.getName(), method.getParameterTypes());
		}

		String[] methodParameterNames = getParameterNames(lookupMethod);
		if (methodParameterNames.length > requiredParameterNames.length
				+ optionalParameterNames.length) {
			return null;
		}

		int[] argIndexs = new int[methodParameterNames.length];
		for (int i = 0; i < argIndexs.length; i++) {
			argIndexs[i] = -1;
		}

		for (int i = 0; i < requiredParameterNames.length; i++) {
			int index = ArrayUtils.indexOf(methodParameterNames,
					requiredParameterNames[i]);
			if (index < 0) {
				return null;
			}
			argIndexs[index] = i;
		}

		for (int i = 0; i < argIndexs.length; i++) {
			if (argIndexs[i] == -1) {
				int index = ArrayUtils.indexOf(optionalParameterNames,
						methodParameterNames[i]);
				if (index < 0) {
					return null;
				}
				argIndexs[i] = requiredParameterNames.length + index;
			}
		}
		return new MethodDescriptor(method, argIndexs, 0);
	}

	/**
	 * 在给定的一组方法中根据参数名查找一个匹配的方法。<br>
	 * 注意，此方法将忽略方法参数的顺序。
	 * 
	 * @param methods
	 *            方法数组。
	 * @param requiredParameterNames
	 *            必须提供的方法参数名数组。
	 * @param optionalParameterNames
	 *            可选的方法参数名数组。
	 * @return 找到的方法的描述对象。
	 * @throws MethodAutoMatchingException
	 *             如果找到了一个以上的匹配方法或没有找到任何匹配的方法将抛出此异常。
	 */
	private static MethodDescriptor findMatchingMethodByParameterNames(
			Method[] methods, String[] requiredParameterNames,
			String[] optionalParameterNames)
			throws MethodAutoMatchingException, SecurityException,
			NoSuchMethodException {
		if (requiredParameterNames == null)
			requiredParameterNames = EMPTY_NAMES;
		if (optionalParameterNames == null)
			optionalParameterNames = EMPTY_NAMES;

		MethodDescriptor methodDescriptor = null;
		for (Method method : methods) {
			MethodDescriptor tmpMethodDescriptor = describMethodIfMatching(
					method, requiredParameterNames, optionalParameterNames);
			if (tmpMethodDescriptor != null) {
				if (methodDescriptor != null) {
					String message = getExceptionMessage(
							"More than one methods matching the following condition",
							methods, requiredParameterNames,
							optionalParameterNames);
					throw new MethodAutoMatchingException(message);
				} else {
					methodDescriptor = tmpMethodDescriptor;
				}
			}
		}

		if (methodDescriptor == null) {
			String message = getExceptionMessage(
					"No method could be found which matching the following condition",
					methods, requiredParameterNames, optionalParameterNames);
			throw new MethodAutoMatchingException(message);
		}
		return methodDescriptor;
	}

	/**
	 * 根据方法描述对象调用方法。
	 * 
	 * @param methodDescriptor
	 *            方法的描述对象
	 * @param object
	 *            反射的对象。
	 * @param parameters
	 *            可选的参数数组。
	 * @return 方法执行后的返回值。
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Object invokeMethod(MethodDescriptor methodDescriptor,
			Object object, Object[] parameters) throws Exception {
		Method method = methodDescriptor.getMethod();
		Class<?>[] parameterTypes = method.getParameterTypes();
		int[] argIndexs = methodDescriptor.getArgIndexs();
		Object[] realArgs = new Object[argIndexs.length];
		for (int i = 0; i < argIndexs.length; i++) {
			Object arg = parameters[argIndexs[i]];
			if (arg != null) {
				Class<?> defType = parameterTypes[i];
				Class<?> argType = ProxyBeanUtils.getProxyTargetType(arg
						.getClass());
				if (!defType.isAssignableFrom(argType)) {
					if (Number.class.isAssignableFrom(defType)
							&& Number.class.isAssignableFrom(argType)) {
						arg = NumberUtils
								.convertNumberToTargetClass((Number) arg,
										(Class<? extends Number>) defType);
					} else if (isSimpleType(defType) || isSimpleType(argType)) {
						arg = ConvertUtils.convert(arg, defType);
					}
				}
			}
			realArgs[i] = arg;
		}
		return method.invoke(object, realArgs);
	}

	/**
	 * 在给定的一组方法中根据方法名、方法参数类型和返回值类型查找一个匹配的方法，并调用该方法。
	 * 
	 * @param methods
	 *            方法数组。
	 * @param object
	 *            方法的宿主对象。
	 * @param requiredParameterTypes
	 *            必须提供的方法参数类型。
	 * @param requiredParameters
	 *            必须的方法参数。
	 * @param exactParameterTypes
	 *            类型必须严格匹配的方法参数类型。
	 * @param exactParameters
	 *            类型必须严格匹配的方法参数。
	 * @param optionalParameterTypes
	 *            可选的方法参数类型。
	 * @param optionalParameters
	 *            可选的方法参数。
	 * @param returnType
	 *            返回值类型，如果为null则表示忽略对返回值类型的判断。
	 * @return 方法执行后的返回值。
	 * @throws MethodAutoMatchingException
	 * @throws Exception
	 */
	public static Object invokeMethod(Method[] methods, Object object,
			Class<?>[] requiredParameterTypes, Object[] requiredParameters,
			Class<?>[] exactParameterTypes, Object[] exactParameters,
			Class<?>[] optionalParameterTypes, Object[] optionalParameters,
			Class<?> returnType) throws MethodAutoMatchingException, Exception {
		Collection<ParameterFactory> systemOptionalParameters = getSystemOptionalParameters();
		if (!systemOptionalParameters.isEmpty()) {
			if (exactParameterTypes != null) {
				Class<?>[] newExactParameterTypes = new Class<?>[exactParameterTypes.length
						+ systemOptionalParameters.size()];
				System.arraycopy(exactParameterTypes, 0,
						newExactParameterTypes, 0, exactParameterTypes.length);

				Object[] newExactParameters = new Object[exactParameters.length
						+ systemOptionalParameters.size()];
				System.arraycopy(exactParameters, 0, newExactParameters, 0,
						exactParameters.length);

				int i = optionalParameterTypes.length;
				for (ParameterFactory parameterFactory : systemOptionalParameters) {
					newExactParameterTypes[i] = parameterFactory
							.getParameterType();
					newExactParameters[i] = parameterFactory.getParameter();
					i++;
				}

				exactParameterTypes = newExactParameterTypes;
				exactParameters = newExactParameters;
			} else {
				exactParameterTypes = new Class<?>[systemOptionalParameters
						.size()];
				exactParameters = new Object[systemOptionalParameters.size()];

				int i = 0;
				for (ParameterFactory parameterFactory : systemOptionalParameters) {
					exactParameterTypes[i] = parameterFactory
							.getParameterType();
					exactParameters[i] = parameterFactory.getParameter();
					i++;
				}
			}
		}

		MethodDescriptor methodDescriptor = findMatchingMethod(methods,
				requiredParameterTypes, exactParameterTypes,
				optionalParameterTypes, returnType);

		if (requiredParameters == null) {
			requiredParameters = EMPTY_ARGS;
		}
		if (exactParameters == null) {
			exactParameters = EMPTY_ARGS;
		}
		if (optionalParameters == null) {
			optionalParameters = EMPTY_ARGS;
		}

		Object[] args = new Object[requiredParameters.length
				+ exactParameters.length + optionalParameters.length];
		System.arraycopy(requiredParameters, 0, args, 0,
				requiredParameters.length);
		System.arraycopy(exactParameters, 0, args, requiredParameters.length,
				exactParameters.length);
		System.arraycopy(optionalParameters, 0, args, requiredParameters.length
				+ exactParameters.length, optionalParameters.length);
		return invokeMethod(methodDescriptor, object, args);
	}

	/**
	 * 在给定的一组方法中根据参数名查找一个匹配的方法，并调用该方法。<br>
	 * 注意，此方法将忽略方法参数的顺序。
	 * 
	 * @param methods
	 *            方法数组。
	 * @param object
	 *            方法的宿主对象。
	 * @param requiredParameterNames
	 *            必选参数名的数组。
	 * @param requiredParameters
	 *            必选参数的数组。
	 * @param optionalParameterNames
	 *            可选参数名的数组。
	 * @param optionalParameters
	 *            可选参数的数组。
	 * @return 方法执行后的返回值。
	 * @throws optionalParameterNames
	 * @throws Exception
	 */
	public static Object invokeMethod(Method[] methods, Object object,
			String[] requiredParameterNames, Object[] requiredParameters,
			String[] optionalParameterNames, Object[] optionalParameters)
			throws MethodAutoMatchingException, Exception {
		Collection<ParameterFactory> systemOptionalParameters = getSystemOptionalParameters();
		if (!systemOptionalParameters.isEmpty()) {
			if (optionalParameterNames != null) {
				String[] newOptionalParameterNames = new String[optionalParameterNames.length
						+ systemOptionalParameters.size()];
				System.arraycopy(optionalParameterNames, 0,
						newOptionalParameterNames, 0,
						optionalParameterNames.length);

				Object[] newOptionalParameters = new Object[optionalParameters.length
						+ systemOptionalParameters.size()];
				System.arraycopy(optionalParameters, 0, newOptionalParameters,
						0, optionalParameters.length);

				int i = optionalParameterNames.length;
				for (ParameterFactory parameterFactory : systemOptionalParameters) {
					newOptionalParameterNames[i] = parameterFactory
							.getParameterName();
					newOptionalParameters[i] = parameterFactory.getParameter();
					i++;
				}

				optionalParameterNames = newOptionalParameterNames;
				optionalParameters = newOptionalParameters;
			} else {
				optionalParameterNames = new String[systemOptionalParameters
						.size()];
				optionalParameters = new Object[systemOptionalParameters.size()];

				int i = 0;
				for (ParameterFactory parameterFactory : systemOptionalParameters) {
					optionalParameterNames[i] = parameterFactory
							.getParameterName();
					optionalParameters[i] = parameterFactory.getParameter();
					i++;
				}
			}
		}

		MethodDescriptor methodDescriptor = findMatchingMethodByParameterNames(
				methods, requiredParameterNames, optionalParameterNames);

		if (requiredParameters == null)
			requiredParameters = EMPTY_ARGS;
		if (optionalParameters == null)
			optionalParameters = EMPTY_ARGS;

		Object[] args = new Object[requiredParameters.length
				+ optionalParameters.length];
		System.arraycopy(requiredParameters, 0, args, 0,
				requiredParameters.length);
		System.arraycopy(optionalParameters, 0, args,
				requiredParameters.length, optionalParameters.length);
		return invokeMethod(methodDescriptor, object, args);
	}

	private static int isTypeAssignableFrom(Class<?> targetType,
			Class<?> sourceType) {
		int rate = 5;
		if (!targetType.equals(sourceType)) {
			rate = 4;
			boolean b = targetType.isAssignableFrom(sourceType);
			if (!b && targetType.isPrimitive()) {
				targetType = MethodUtils.toNonPrimitiveClass(targetType);
				b = targetType.isAssignableFrom(sourceType);
			}
			if (!b) {
				b = Number.class.isAssignableFrom(targetType)
						&& Number.class.isAssignableFrom(sourceType);
			}
			if (!b)
				rate = 0;
		}
		return rate;
	}

	private static int isTypesCompatible(Class<?> targetType,
			Class<?> sourceType) {
		boolean b = targetType.isAssignableFrom(sourceType)
				|| sourceType.isAssignableFrom(targetType);
		if (!b && (targetType.isPrimitive() || sourceType.isPrimitive())) {
			targetType = MethodUtils.toNonPrimitiveClass(targetType);
			sourceType = MethodUtils.toNonPrimitiveClass(sourceType);
			b = targetType.isAssignableFrom(sourceType)
					|| sourceType.isAssignableFrom(targetType);
		}
		if (!b) {
			b = Number.class.isAssignableFrom(targetType)
					&& Number.class.isAssignableFrom(sourceType);
		}
		return (b) ? 1 : 0;
	}

	private static Class<?>[] args2ArgTypes(Object[] args) {
		Class<?>[] argTypes = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			if (arg != null) {
				Class<?> cl = arg.getClass();
				argTypes[i] = ProxyBeanUtils.getProxyTargetType(cl);
			} else {
				argTypes[i] = IgnoreType.class;
			}
		}
		return argTypes;
	}

	private static String getClassName(Class<?> type) {
		if (net.sf.cglib.proxy.Factory.class.isAssignableFrom(type)) {
			return type.getSuperclass().getName();
		} else {
			return type.getName();
		}
	}

	private static String getExceptionMessage(String header, Method[] methods,
			Class<?>[] requiredTypes, Class<?>[] exactTypes,
			Class<?>[] optionalTypes, Class<?> returnType) {
		StringBuffer message = new StringBuffer();
		message.append(getExceptionMessageHeader(methods, header));
		message.append(" requiredTypes=[");
		for (int i = 0; i < requiredTypes.length; i++) {
			if (i > 0)
				message.append(",");
			Class<?> argType = requiredTypes[i];
			if (argType != null) {
				message.append(getClassName(argType));
			} else {
				message.append("*");
			}
		}
		message.append("],");

		message.append(" exactTypes=[");
		for (int i = 0; i < exactTypes.length; i++) {
			if (i > 0)
				message.append(",");
			Class<?> argType = exactTypes[i];
			if (argType != null) {
				message.append(getClassName(argType));
			} else {
				message.append("*");
			}
		}
		message.append("],");

		message.append(" optionalTypes=[");
		for (int i = 0; i < optionalTypes.length; i++) {
			if (i > 0)
				message.append(",");
			Class<?> argType = optionalTypes[i];
			if (argType != null) {
				message.append(getClassName(argType));
			} else {
				message.append("*");
			}
		}
		message.append("],");
		message.append(getExceptionMesasgeFooter(returnType));
		return message.toString();
	}

	private static String getExceptionMessage(String header, Method[] methods,
			String[] requiredParameterNames, String[] optionalParameterNames) {
		StringBuffer message = new StringBuffer();
		message.append(getExceptionMessageHeader(methods, header));
		message.append(" requiredParameters=[");
		for (int i = 0; i < requiredParameterNames.length; i++) {
			if (i > 0)
				message.append(",");
			message.append(requiredParameterNames[i]);
		}
		message.append("],");
		message.append(" optionalParameters=[");
		for (int i = 0; i < optionalParameterNames.length; i++) {
			if (i > 0)
				message.append(",");
			message.append(optionalParameterNames[i]);
		}
		message.append("]");
		return message.toString();
	}

	private static String getExceptionMessageHeader(Method[] methods,
			String header) {
		StringBuffer message = new StringBuffer();
		message.append(header).append(": ");

		boolean classIsSame = true;
		boolean methodNameIsSame = true;
		Class<?> cl = null;
		String methodName = null;
		for (Method method : methods) {
			if (classIsSame) {
				Class<?> declaringClass = method.getDeclaringClass();
				if (cl == null) {
					cl = declaringClass;
				} else if (!declaringClass.equals(cl)) {
					if (declaringClass.isAssignableFrom(cl)) {
						cl = declaringClass;
					} else if (cl.isAssignableFrom(declaringClass)) {
						// do nothing
					} else {
						classIsSame = false;
					}
				}
			}

			if (methodNameIsSame) {
				if (methodName == null) {
					methodName = method.getName();
				} else if (!method.getName().equals(methodName)) {
					methodNameIsSame = false;
				}
			}
		}

		if (classIsSame) {
			message.append(" class=").append(cl.getName()).append(",");
		}

		if (methodNameIsSame) {
			message.append(" methodName=").append(methodName).append(",");
		}
		return message.toString();
	}

	private static String getExceptionMesasgeFooter(Class<?> returnType) {
		StringBuffer message = new StringBuffer();
		message.append(" returnType=");
		if (returnType != null) {
			message.append(getClassName(returnType));
		} else {
			message.append("*");
		}
		return message.toString();
	}
}
