package com.bstek.dorado.jdbc.config.xml;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.jdbc.JdbcEnviroment;

public class JdbcParseContext extends ParseContext {

	private JdbcEnviroment jdbcEnviroment;
	
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
