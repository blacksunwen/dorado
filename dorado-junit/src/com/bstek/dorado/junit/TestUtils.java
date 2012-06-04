package com.bstek.dorado.junit;

public class TestUtils {
	private static final ThreadLocal<Class<?>> LOCAL_CLASS = new ThreadLocal<Class<?>>();
	public static Class<?> getCurrentClassClass() {
		return LOCAL_CLASS.get();
	}
	
	public static void setCurrentCaseClass(Class<?> clazz) {
		LOCAL_CLASS.set(clazz);
	}
	
}
