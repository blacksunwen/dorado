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
