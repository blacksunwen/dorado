package com.bstek.dorado.view.xml;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.view.ViewContextTestCase;
import com.bstek.dorado.view.manager.ViewConfig;
import com.bstek.dorado.view.manager.ViewConfigManager;

public class ViewConfigTest extends ViewContextTestCase {
	private ViewConfigManager getViewConfigManager() throws Exception {
		Context context = Context.getCurrent();
		ViewConfigManager viewConfigManager = (ViewConfigManager) context
				.getServiceBean("viewConfigManager");
		return viewConfigManager;
	}

	private ViewConfig getTestViewConfig() throws Exception {
		ViewConfigManager viewConfigManager = getViewConfigManager();
		ViewConfig viewConfig = viewConfigManager
				.getViewConfig("com/bstek/dorado/view/xml/TestView1");
		return viewConfig;
	}

	public void test1() throws Exception {
		ViewConfig viewConfig = getTestViewConfig();
		assertNotNull(viewConfig);
	}

}
