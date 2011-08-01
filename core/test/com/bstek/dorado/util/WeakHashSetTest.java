package com.bstek.dorado.util;

import com.bstek.dorado.util.WeakHashSet;

import junit.framework.TestCase;

public class WeakHashSetTest extends TestCase {
	private WeakHashSet<Object> weakHashSet = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		weakHashSet = new WeakHashSet<Object>();
	}

	@Override
	protected void tearDown() throws Exception {
		weakHashSet = null;
		super.tearDown();
	}

	public void testAdd() {
		Object o = new Object();
		boolean actualReturn = weakHashSet.add(o);
		assertTrue(actualReturn);
		assertEquals(weakHashSet.size(), 1);
	}

}
