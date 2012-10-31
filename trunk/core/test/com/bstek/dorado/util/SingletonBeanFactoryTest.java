/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
