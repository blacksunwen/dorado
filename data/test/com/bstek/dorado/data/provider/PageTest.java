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
package com.bstek.dorado.data.provider;

import com.bstek.dorado.data.provider.Page;

import junit.framework.TestCase;

public class PageTest extends TestCase {
	@SuppressWarnings("rawtypes")
	public void test() {
		Page page = new Page(10, 0);

		page.setEntityCount(0);
		assertEquals(1, page.getPageCount());

		page.setEntityCount(9);
		assertEquals(1, page.getPageCount());

		page.setEntityCount(10);
		assertEquals(1, page.getPageCount());

		page.setEntityCount(11);
		assertEquals(2, page.getPageCount());

		page.setEntityCount(20);
		assertEquals(2, page.getPageCount());
	}
}
