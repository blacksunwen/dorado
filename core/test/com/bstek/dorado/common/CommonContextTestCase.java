package com.bstek.dorado.common;

import com.bstek.dorado.core.ContextTestCase;

public abstract class CommonContextTestCase extends ContextTestCase {
	public CommonContextTestCase() {
		addExtensionContextConfigLocation("com/bstek/dorado/common/context.xml");
	}

}
