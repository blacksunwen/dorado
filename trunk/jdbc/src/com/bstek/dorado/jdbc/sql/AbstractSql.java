package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcParameterSource;

/**
 * 可执行SQL的抽象类
 * 
 * @author mark
 * 
 */
public abstract class AbstractSql {
	private JdbcParameterSource parameterSource;

	public JdbcParameterSource getParameterSource() {
		return parameterSource;
	}

	public void setParameterSource(JdbcParameterSource parameterSource) {
		this.parameterSource = parameterSource;
	}

	/**
	 * 输出SQL
	 * @param dialect
	 * @return
	 */
	public abstract String toSQL(Dialect dialect);
}
