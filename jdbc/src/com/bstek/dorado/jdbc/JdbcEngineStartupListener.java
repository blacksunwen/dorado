package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.dorado.core.EngineStartupListener;
import com.bstek.dorado.data.config.DataConfigEngineStartupListener;
import com.bstek.dorado.jdbc.config.DbmManager;
import com.bstek.dorado.jdbc.config.GlobalDbModelConfig;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;

/**
 * JDBC模块的启动器
 * 
 * @author mark
 * 
 */
public class JdbcEngineStartupListener extends EngineStartupListener implements
		ApplicationContextAware {

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
		/*
		 * JDBC模块一定是在DataType被加载之后启动（由于JdbcType的缘故）
		 */
		DataConfigEngineStartupListener l = applicationContext.getBean(DataConfigEngineStartupListener.class);
		this.setOrder(l.getOrder() + 10);
	}

	@Override
	public void onStartup() throws Exception {
		this.registerEnviroments();
		this.registerDbModels();
	}

	/**
	 * 注册{@link JdbcEnviroment}
	 */
	protected void registerEnviroments() {
		JdbcEnviromentManager manager = JdbcUtils.getEnviromentManager();
		Map<String, JdbcEnviroment> envMap = ctx.getBeansOfType(JdbcEnviroment.class);
		for (JdbcEnviroment env: envMap.values()) {
			manager.register(env);
		}
	}
	
	/**
	 * 注册{@link JdbcEnviroment}
	 * @throws Exception
	 */
	protected void registerDbModels() throws Exception {
		Collection<GlobalDbModelConfig> configs = ctx.getBeansOfType(GlobalDbModelConfig.class).values();
		
		GlobalDbModelConfig[] configArray = configs.toArray(new GlobalDbModelConfig[0]);
		DbmManager configManager = JdbcUtils.getDbmManager();
		configManager.refresh(configArray);
	}
}
