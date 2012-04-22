package com.bstek.dorado.jdbc.config;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.jdbc.model.DbElement;

/**
 * 全局共享的{@link DbElement}的配置对象
 * 
 * @author mark.li@bstek.com
 *
 */
public class JdbcConfigLoader implements InitializingBean{
	/**
	 * 配置文件的位置
	 */
	private String configLocation;

	private List<String> configLocations;
	
	private DbmManager dbmManager;
	
	public DbmManager getDbmManager() {
		return dbmManager;
	}

	public void setDbmManager(DbmManager dbmManager) {
		this.dbmManager = dbmManager;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public List<String> getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = configLocations;
	}

	public void afterPropertiesSet() throws Exception {
		dbmManager.register(this);
	}

}
