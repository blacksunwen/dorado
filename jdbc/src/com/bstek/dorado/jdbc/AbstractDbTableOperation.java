package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.DbTable;

/**
 * JDBC数据库操作
 * 
 * @author mark
 * 
 * @param <T>
 */
public abstract class AbstractDbTableOperation<T extends AbstractJdbcContext> {
	private T jdbcContext;

	private boolean _processDefault = true;

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
	
	public void execute() {
		try {
			doExecute();
		} finally {
			setProcessDefault(false);
		}
	}
	
	/**
	 * 执行操作动作
	 */
	protected abstract void doExecute();
}
