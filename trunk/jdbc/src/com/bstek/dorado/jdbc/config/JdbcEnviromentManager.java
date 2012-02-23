package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * {@link com.bstek.dorado.jdbc.JdbcEnviroment}的管理器
 * 
 * @author mark.li@bstek.com
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
	 * 列出所有的{@link JdbcEnviroment}
	 * @return
	 */
	JdbcEnviroment[] listAll();
	
	/**
	 * 注册{@link JdbcEnviroment}
	 * @param env
	 */
	void register(JdbcEnviroment env);
}
