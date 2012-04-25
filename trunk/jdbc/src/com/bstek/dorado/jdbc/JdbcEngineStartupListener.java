package com.bstek.dorado.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.dorado.core.EngineStartupListener;
import com.bstek.dorado.data.config.DataConfigEngineStartupListener;
import com.bstek.dorado.jdbc.config.DbmDefinitionManager;

/**
 * JDBC模块的启动器
 * 
 * @author mark
 * 
 */
public class JdbcEngineStartupListener extends EngineStartupListener implements
		ApplicationContextAware {

	private static Log logger = LogFactory.getLog(JdbcEngineStartupListener.class);
	
	private ApplicationContext ctx;

	private DbmDefinitionManager dbmDefinitionManager;
	
	private KeyGeneratorManager keyGeneratorManager;
	
	private JdbcTypeManager jdbcTypeManager;
	
	public DbmDefinitionManager getDbmDefinitionManager() {
		return dbmDefinitionManager;
	}

	public void setDbmDefinitionManager(DbmDefinitionManager dbmDefinitionManager) {
		this.dbmDefinitionManager = dbmDefinitionManager;
	}

	public void setKeyGeneratorManager(KeyGeneratorManager keyGeneratorManager) {
		this.keyGeneratorManager = keyGeneratorManager;
	}

	public void setJdbcTypeManager(JdbcTypeManager jdbcTypeManager) {
		this.jdbcTypeManager = jdbcTypeManager;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
		/*
		 * JDBC模块一定是在DataType被加载之后启动（由于JdbcType的缘故）
		 */
		DataConfigEngineStartupListener l = ctx.getBean(DataConfigEngineStartupListener.class);
		this.setOrder(l.getOrder() + 10);
	}

	@Override
	public void onStartup() throws Exception {
		dbmDefinitionManager.refresh();
		
		logger.info(jdbcTypeManager.toString());
		logger.info(keyGeneratorManager.toString());
		logger.info(dbmDefinitionManager.toString());
	}
	
}
