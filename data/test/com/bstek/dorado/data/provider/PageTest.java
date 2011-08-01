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
