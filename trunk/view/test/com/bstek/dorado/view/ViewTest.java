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
package com.bstek.dorado.view;

import junit.framework.TestCase;

import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.Container;

public class ViewTest extends TestCase {

	private class TestComponent extends Container {
		public TestComponent(String id) {
			this.setId(id);
		}
	}

	public void testGetSubComponent() {
		View view = new MockView(null);

		TestComponent com1 = new TestComponent("com1");
		TestComponent com2 = new TestComponent("com2");
		TestComponent com3 = new TestComponent("com3");
		TestComponent com4 = new TestComponent("com4");
		TestComponent com5 = new TestComponent("com5");
		com1.addChild(com2);
		com2.addChild(com3);
		view.addChild(com1);
		view.addChild(com4);
		com2.addChild(com5);

		Component com;

		for (int i = 1; i < 5; i++) {
			com = view.getComponent("com" + i);
			assertNotNull(com);
			assertEquals("com" + i, com.getId());
		}

		com2.removeChild(com3);
		com = view.getComponent("com3");
		assertNull(com);

		view.removeChild(com4);
		com = view.getComponent("com4");
		assertNull(com);
	}
}
