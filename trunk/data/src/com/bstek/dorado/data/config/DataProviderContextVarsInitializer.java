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