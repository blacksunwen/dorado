package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.DbTable;

/**
 * JDBC数据库操作
 * 
 * @author mark
 * 
 * @param <T>
 */
public abstract class AbstractJdbcOperation<T extends AbstractJdbcContext> {
	private T jdbcContext;

	public AbstractJdbcOperation(DbTable dbElement, T jdbcContext) {
		this.dbTable = dbElement;
		this.jdbcContext = jdbcContext;
	}

	private DbTable dbTable;

	public DbTable getDbTable() {
		return dbTable;
	}

	public void setDbTable(DbTable dbTable) {
		this.dbTable = dbTable;
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
