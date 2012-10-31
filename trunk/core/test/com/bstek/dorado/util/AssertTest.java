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
