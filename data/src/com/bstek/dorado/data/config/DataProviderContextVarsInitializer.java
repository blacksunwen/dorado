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
package com.bstek.dorado.data.config;

import java.lang.reflect.Method;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.el.ContextVarsInitializer;
import com.bstek.dorado.data.provider.DataProvider;
import com.bstek.dorado.data.provider.manager.DataProviderManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jun 23, 2009
 */
public class DataProviderContextVarsInitializer implements
		ContextVarsInitializer {
	private static DataProviderManager dataProviderManager;

	private static DataProviderManager getDataProviderManager()
			throws Exception {
		if (dataProviderManager == null) {
			dataProviderManager = (DataProviderManager) Context.getCurrent()
					.getServiceBean("dataProviderManager");
		}
		return dataProviderManager;
	}

	public void initializeContext(Map<String, Object> vars) throws Exception {
		Method method = getClass().getDeclaredMethod("getDataProvider",
				new Class[] { String.class });
		vars.put("getDataProvider", method);
	}

	public static DataProvider getDataProvider(String name) throws Exception {
		return getDataProviderManager().getDataProvider(name);
	}

}
