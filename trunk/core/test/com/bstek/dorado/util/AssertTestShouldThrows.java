package com.bstek.dorado.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import junit.framework.TestCase;

public class AssertTestShouldThrows extends TestCase {
	public void testDoesNotContain() {
		try {
			Assert.doesNotContain("abcdefg", "cd");

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testIsAssignable() {
		try {
			Assert.isAssignable(Vector.class, List.class);

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testIsInstanceOf() {
		try {
			Assert.isInstanceOf(List.class, new HashMap<Object, Object>());

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testIsNull() {
		try {
			Assert.isNull(new Object());

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testNotNull() {
		try {
			Assert.notNull(null);

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testIsTrue() {
		try {
			Assert.isTrue(2 < 1);

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testArrayNotEmpty() {
		try {
			Assert.notEmpty(new String[0]);

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testCollectionNotEmpty() {
		try {
			List<Object> list = new Vector<Object>();
			Assert.notEmpty(list);

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

	public void testMapNotEmpty() {
		try {
			Map<Object, Object> map = new HashMap<Object, Object>();
			Assert.notEmpty(map);

			fail();
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail();
		}
	}

}
