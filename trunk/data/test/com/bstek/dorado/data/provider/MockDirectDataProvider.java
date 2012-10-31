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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.DirectDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;

public class MockDirectDataProvider extends DirectDataProvider {

	@Override
	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		return new Long(9158);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void internalGetResult(Object parameter, Page page,
			DataType resultDataType) throws Exception {
		int pageSize = page.getPageSize();
		int resultIndex = page.getFirstEntityIndex();

		List entitys = new ArrayList();
		for (int i = 0; i < pageSize; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("key", "key" + (++resultIndex));
			entitys.add(map);
		}
		page.setEntities(entitys);
		page.setEntityCount(pageSize * 10);
	}
}
