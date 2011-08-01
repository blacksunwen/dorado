package com.bstek.dorado.data.resolver.manager;

import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.resolver.DataResolver;

public class TestDataResolverInterceptor {
	public String method1() {
		return "Return value of method1";
	}

	public DataItems method2(DataItems dataItems) {
		return dataItems;
	}

	public DataItems method3(DataItems dataItems, Object parameter) {
		return dataItems;
	}

	public String method4(DataResolver dataResolver, int parameter) {
		return String.valueOf(parameter);
	}

	public String method5(String value1, int value2, float value4,
			boolean parameter) {
		return "method5-" + value1 + value2 + value4 + parameter;
	}
}
