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

import com.bstek.dorado.util.CloneUtils;

import junit.framework.TestCase;

public class CloneUtilsTest extends TestCase {

	private static class MockObject implements Cloneable {
		private String text;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	public void test() throws CloneNotSupportedException {
		final String text = "Yaha!";

		MockObject obj, clonedObj;
		obj = new MockObject();
		obj.setText(text);
		clonedObj = (MockObject) CloneUtils.clone(obj);

		assertEquals(text, clonedObj.getText());
	}
}
