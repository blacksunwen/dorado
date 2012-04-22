package com.bstek.dorado.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.dorado.core.EngineStartupListener;
import com.bstek.dorado.data.config.DataConfigEngineStartupListener;
import com.bstek.dorado.jdbc.config.DbmManager;
import com.bstek.dorado.jdbc.support.DefaultKeyGeneratorManager;

/**
 * JDBC模块的启动器
 * 
 * @author mark
 * 
 */
public class JdbcEngineStartupListener extends EngineStartupListener implements
		ApplicationContextAware {

	private static Log logger = LogFactory.getLog(DefaultKeyGeneratorManager.class);
	
	private ApplicationContext ctx;

	private DbmManager dbmManager;
	
	private KeyGeneratorManager keyGeneratorManager;
	
	private JdbcTypeManager jdbcTypeManager;
	
	public DbmManager getDbmManager() {
		return dbmManager;
	}

	public void setDbmManager(DbmManager dbmManager) {
		this.dbmManager = dbmManager;
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
		dbmManager.refresh();
		
		logger.info(jdbcTypeManager.toString());
		logger.info(keyGeneratorManager.toString());
		logger.info(dbmManager.toString());
	}
	
}
