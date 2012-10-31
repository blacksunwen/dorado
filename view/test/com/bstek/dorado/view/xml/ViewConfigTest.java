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
