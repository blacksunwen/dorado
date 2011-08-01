package com.bstek.dorado.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.bstek.dorado.util.Assert;

import junit.framework.TestCase;

public class AssertTest extends TestCase {

	public void testDoesNotContain() {
		Assert.doesNotContain("abcdefg", "#cd");
	}

	public void testIsAssignable() {
		Assert.isAssignable(List.class, Vector.class);
	}

	public void testIsInstanceOf() {
		Assert.isInstanceOf(List.class, new Vector<Object>());
	}

	public void testIsNull() {
		Assert.isNull(null);
	}

	public void testNotNull() {
		Assert.notNull(new Object());
	}

	public void testIsTrue() {
		Assert.isTrue(2 > 1);
	}

	public void testArrayNotEmpty() {
		Assert.notEmpty(new String[2]);
	}

	public void testCollectionNotEmpty() {
		List<Object> list = new Vector<Object>();
		list.add(new Object());
		Assert.notEmpty(list);
	}

	public void testMapNotEmpty() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("key1", "value1");
		Assert.notEmpty(map);
	}

}
