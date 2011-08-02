package com.bstek.dorado.idesupport;

import com.bstek.dorado.view.ViewContextTestCase;

public abstract class IdeSupportContextTestCase extends ViewContextTestCase {
	public IdeSupportContextTestCase() {
		addExtensionContextConfigLocation("com/bstek/dorado/idesupport/ide-context.xml");
	}
}
