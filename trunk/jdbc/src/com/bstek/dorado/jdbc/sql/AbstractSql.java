package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcParameterSource;

public abstract class AbstractSql {
	private JdbcParameterSource parameterSource;

	public JdbcParameterSource getParameterSource() {
		return parameterSource;
	}

	public void setParameterSource(JdbcParameterSource parameterSource) {
		this.parameterSource = parameterSource;
	}
	
	public abstract String toSQL(Dialect dialect);
}
