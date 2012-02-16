package com.bstek.dorado.core;


public class MockContext extends CommonContext {
	public static Context init() throws Exception {
		MockContext context = new MockContext();
		attachToThreadLocal(context);

		// Initialize Spring ApplicationContext
		context.initApplicationContext();

		EngineStartupListenerManager.notifyStartup();
		return context;
	}
}
