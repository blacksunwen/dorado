package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.DbTable;

/**
 * 抽象的对{@link DbTable} 数据库操作
 * 
 * @author mark.li@bstek.com
 * @param <T>
 */
public abstract class AbstractDbTableOperation<T extends AbstractJdbcContext> {
	private T jdbcContext;
	private DbTable dbTable;
	private boolean _processDefault = true;

	/**
	 * 是否触发默认的数据库操作
	 * @return
	 */
	public boolean isProcessDefault() {
		return _processDefault;
	}

	public void setProcessDefault(boolean _processDefault) {
		this._processDefault = _processDefault;
	}
	
	public AbstractDbTableOperation(DbTable dbElement, T jdbcContext) {
		this.dbTable = dbElement;
		this.jdbcContext = jdbcContext;
	}

	/**
	 * 获取操作的{@link DbTable}
	 * @return
	 */
	public DbTable getDbTable() {
		return dbTable;
	}

	public void setDbTable(DbTable dbTable) {
		this.dbTable = dbTable;
	}

	/**
	 * 获取数据库操作的上下文
	 * @return
	 */
	public T getJdbcContext() {
		return this.jdbcContext;
	}

	public JdbcEnviroment getJdbcEnviroment() {
		AbstractJdbcContext jdbcContext = getJdbcContext();
		JdbcEnviroment env = null;
		
		if (jdbcContext != null) {
			env = jdbcContext.getJdbcEnviroment();
		}
		if (env == null) {
			env = this.getDbTable().getJdbcEnviroment();
		}
		
		return env;
	}
	
	public boolean execute() {
		try {
			return doExecute();
		} finally {
			setProcessDefault(false);
		}
	}
	
	/**
	 * 执行操作动作
	 */
	protected abstract boolean doExecute();
}
