package com.bstek.dorado.jdbc.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.bstek.dorado.core.CommonContext;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.EngineStartupListenerManager;

public class JdbcTestContext extends CommonContext {

	private ApplicationContext applicationContext;
	
	public JdbcTestContext() {
		super();
		
		try {
			this.initApplicationContext();
			Context.attachToThreadLocal(this);
			EngineStartupListenerManager.notifyStartup();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ApplicationContext getApplicationContext() throws Exception {
		if (applicationContext == null) {
			applicationContext = createApplicationContext();
		}
		return applicationContext;
	}
	
	public void close() throws Exception{
		Context.dettachFromThreadLocal();
		ConfigurableApplicationContext configContext = (ConfigurableApplicationContext)this.getApplicationContext();
		configContext.close();
	}
}
