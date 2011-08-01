package com.bstek.dorado.data.resolver;

import java.util.Map;

import com.bstek.dorado.data.resolver.AbstractDataResolver;
import com.bstek.dorado.data.resolver.DataItems;

public class TestDataResolver extends AbstractDataResolver {

	@Override
	protected Object internalResolve(DataItems dataItems, Object parameter)
			throws Exception {
		if ("Exception".equals(parameter)) {
			throw new RuntimeException("Test Exception");
		}

		String s = "testResolver1-";
		if (dataItems != null && !dataItems.isEmpty()) {
			for (Map.Entry<String, Object> entry : dataItems.entrySet()) {
				s += entry.getKey() + ":" + entry.getValue() + ";";
			}
		}
		s += "parameter:" + parameter;
		return s;
	}

}
