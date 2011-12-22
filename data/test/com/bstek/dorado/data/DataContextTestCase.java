package com.bstek.dorado.data;

import com.bstek.dorado.common.CommonContextTestCase;

public abstract class DataContextTestCase extends CommonContextTestCase {
	public DataContextTestCase() {
		addExtensionContextConfigLocation("com/bstek/dorado/data/context.xml");
		addExtensionContextConfigLocation("com/bstek/dorado/data/test-context.xml");
	}
}
