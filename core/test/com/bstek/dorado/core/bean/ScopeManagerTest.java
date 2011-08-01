package com.bstek.dorado.core.bean;

import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.core.bean.ScopeManager;

import junit.framework.TestCase;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 26, 2008
 */
public class ScopeManagerTest extends TestCase {
	public void testSingletonPutAndGet() {
		final String KEY = Object.class.getName();
		ScopeManager sm = new ScopeManager();
		Object o = new Object();
		sm.putBean(Scope.singleton, KEY, o);
		assertSame(o, sm.getBean(Scope.singleton, KEY));
	}

	public void testInstantPutAndGet() {
		final String KEY = Object.class.getName();
		ScopeManager sm = new ScopeManager();
		Object o = new Object();
		sm.putBean(Scope.instant, KEY, o);
		assertNull(sm.getBean(Scope.instant, KEY));
	}
}
