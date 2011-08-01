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
