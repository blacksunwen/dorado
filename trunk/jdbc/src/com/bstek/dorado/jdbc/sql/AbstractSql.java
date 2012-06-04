package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.Dialect;

/**
 * 可执行SQL的抽象类
 * 
 * @author mark.li@bstek.com
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
	
	private String SQL = null;

	public String getSQL(Dialect dialect) {
		if (SQL == null) {
			SQL = this.toSQL(dialect);
		}
		
		return SQL;
	}
	
	/**
	 * 输出SQL
	 * @param dialect
	 * @return
	 */
	protected abstract String toSQL(Dialect dialect);
}
