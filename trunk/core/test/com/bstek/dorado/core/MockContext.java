package com.bstek.dorado.core;

import java.io.InputStream;
import java.util.Properties;

import com.bstek.dorado.core.CommonContext;
import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.Context;

public class MockContext extends CommonContext {
	public static Context init() throws Exception {
		Properties properties = new Properties();
		InputStream in = MockContext.class.getClassLoader()
				.getResourceAsStream(
						"com/bstek/dorado/core/extension-configure.properties");
		try {
			properties.load(in);
			for (Object key : properties.keySet()) {
				Configure.getStore().set((String) key, properties.get(key));
			}
		}
		finally {
			in.close();
		}

		MockContext context = new MockContext();
		attachToThreadLocal(context);

		// Initialize Spring ApplicationContext
		context.initApplicationContext();

		EngineStartupListenerManager.notifyStartup();
		return context;
	}
}
