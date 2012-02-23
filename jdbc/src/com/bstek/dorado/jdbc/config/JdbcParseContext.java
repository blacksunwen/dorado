package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.data.config.xml.DataParseContext;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * JDBC模块解析XML时候的上下文对象
 * 
 * @author mark.li@bstek.com
 *
 */
public class JdbcParseContext extends /*ParseContext*/ DataParseContext {

	private JdbcEnviroment jdbcEnviroment;
	
	public JdbcParseContext(){
		super();
	}
	
	public JdbcParseContext(JdbcEnviroment jdbcEnviroment) {
		super();
		this.jdbcEnviroment = jdbcEnviroment;
	}
	
	public JdbcEnviroment getJdbcEnviroment() {
		return jdbcEnviroment;
	}

	public void setJdbcEnviroment(JdbcEnviroment jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}
	
}
