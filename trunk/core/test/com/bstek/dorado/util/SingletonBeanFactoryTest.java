package com.bstek.dorado.util;

import com.bstek.dorado.util.SingletonBeanFactory;

import junit.framework.TestCase;

public class SingletonBeanFactoryTest extends TestCase {

	public void testGetInstance() throws IllegalAccessException,
			InstantiationException {
		Object instance1 = SingletonBeanFactory
				.getInstance(java.util.HashMap.class);
		Object instance2 = SingletonBeanFactory
				.getInstance(java.util.HashMap.class);
		Object instance3 = SingletonBeanFactory
				.getInstance(java.util.HashMap.class);
		assertTrue(instance1 == instance2);
		assertTrue(instance2 == instance3);
	}
}
