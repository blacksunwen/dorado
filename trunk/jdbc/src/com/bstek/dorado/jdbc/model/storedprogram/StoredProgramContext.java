package com.bstek.dorado.jdbc.model.storedprogram;

import com.bstek.dorado.jdbc.AbstractJdbcContext;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.sql.SqlUtils;

public class StoredProgramContext extends AbstractJdbcContext {

	private JdbcParameterSource source;
	
	public StoredProgramContext(JdbcEnviroment enviroment, Object parameter) {
		super(enviroment, parameter);
		source = SqlUtils.createJdbcParameter(parameter);
	}

	public JdbcParameterSource getJdbcParameterSource() {
		return source;
	}
	
}
