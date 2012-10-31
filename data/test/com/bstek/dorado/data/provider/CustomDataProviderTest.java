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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.Page;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CustomDataProviderTest extends ConfigManagerTestSupport {

	public void test() throws Exception {
		DataProvider dataProvider = getDataProviderManager().getDataProvider(
				"providerCustom");
		assertNotNull(dataProvider);
		assertEquals(new Long(9158), dataProvider.getResult());

		final int pageSize = 10;

		Page page = new Page(pageSize, 3);
		dataProvider.getResult(page);
		assertNotNull(page);
		assertEquals(pageSize * 10, page.getEntityCount());

		Collection<?> entities = page.getEntities();
		assertEquals(pageSize, entities.size());

		int i = 20;
		for (Iterator<?> it = entities.iterator(); it.hasNext();) {
			Map<String, Object> map = (Map<String, Object>) it.next();
			assertEquals("key" + (++i), map.get("key"));
		}
	}
}
