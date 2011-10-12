package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.AbstractJdbcContext;

/**
 * JDBC数据库操作
 * 
 * @author mark
 * 
 * @param <T>
 */
public abstract class AbstractJdbcOperation<T extends AbstractJdbcContext> {
	private T jdbcContext;

	public AbstractJdbcOperation(DbElement dbElement, T jdbcContext) {
		this.dbElement = dbElement;
		this.jdbcContext = jdbcContext;
	}

	private DbElement dbElement;

	public DbElement getDbElement() {
		return dbElement;
	}

	public void setDbElement(DbElement dbElement) {
		this.dbElement = dbElement;
	}

	public T getJdbcContext() {
		return this.jdbcContext;
	}

	/**
	 * 执行操作动作
	 */
	public abstract void execute();
}
