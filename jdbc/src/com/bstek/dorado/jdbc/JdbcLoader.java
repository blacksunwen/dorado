package com.bstek.dorado.jdbc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.dorado.core.EngineStartupListener;
import com.bstek.dorado.data.config.DataConfigEngineStartupListener;
import com.bstek.dorado.jdbc.config.JdbcConfigManager;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的启动器
 * 
 * @author mark
 * 
 */
public class JdbcLoader extends EngineStartupListener implements
		ApplicationContextAware {

	private JdbcConfigManager configManager;

	public JdbcConfigManager getConfigManager() {
		return configManager;
	}

	public void setConfigManager(JdbcConfigManager configManager) {
		this.configManager = configManager;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		Assert.notNull(getConfigManager(), "ConfigManager must not be null.");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		/*
		 * JDBC模块一定是在DataType被加载之后启动（由于JdbcType的缘故）
		 */
		DataConfigEngineStartupListener l = applicationContext.getBean(DataConfigEngineStartupListener.class);
		this.setOrder(l.getOrder() + 10);
	}

	@Override
	public void onStartup() throws Exception {
		JdbcUtils.getEnviromentManager().initialize();
		this.getConfigManager().initialize();
	}

}
