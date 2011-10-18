package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.JdbcEnviroment;

public class JdbcDataResolverContext extends AbstractJdbcContext {

	public JdbcDataResolverContext(JdbcEnviroment enviroment, Object parameter) {
		super(enviroment, parameter);
	}

	public JdbcDataResolverContext(Object parameter) {
		this(null, parameter);
	}
}
