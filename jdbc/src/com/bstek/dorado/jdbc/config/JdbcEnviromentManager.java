package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * JdbcEnviroment的管理器
 * @author mark
 *
 */
public interface JdbcEnviromentManager {

	/**
	 * 获得指定名称的{@link JdbcEnviroment}
	 * @param name
	 * @return
	 */
	JdbcEnviroment getEnviroment(String name);
	
	/**
	 * 获得默认的{@link JdbcEnviroment}
	 * @return
	 */
	JdbcEnviroment getDefault();
	
	/**
	 * 注册{@link JdbcEnviroment}
	 * @param env
	 */
	void register(JdbcEnviroment env);
	
	/**
	 * 初始化，根据默认规则读取并注册{@link JdbcEnviroment}
	 */
	void initialize();
}
