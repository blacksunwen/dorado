package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.AbstractJdbcContext;
import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * JDBC数据库操作
 * 
 * @author mark
 * 
 * @param <T>
 */
public abstract class DbElementJdbcOperation<T extends AbstractJdbcContext> {
	private T jdbcContext;

	public DbElementJdbcOperation(DbTable dbElement, T jdbcContext) {
		this.dbElement = dbElement;
		this.jdbcContext = jdbcContext;
	}

	private DbTable dbElement;

	public DbTable getDbTable() {
		return dbElement;
	}

	public void setDbTable(DbTable dbElement) {
		this.dbElement = dbElement;
	}

	public T getJdbcContext() {
		return this.jdbcContext;
	}

	public JdbcEnviroment getJdbcEnviroment() {
		AbstractJdbcContext jdbcContext = getJdbcContext();
		JdbcEnviroment env = jdbcContext.getJdbcEnviroment();
		if (env == null) {
			env = this.getDbTable().getJdbcEnviroment();
		}
		return env;
	}
	/**
	 * 执行操作动作
	 */
	public abstract void execute();
}
