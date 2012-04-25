package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.DbTable;

/**
 * 抽象的对{@link DbTable} 数据库操作
 * 
 * @author mark.li@bstek.com
 * @param <CTX>
 */
public abstract class AbstractDbTableOperation<CTX extends AbstractJdbcContext, TAB extends DbTable> {
	private CTX jdbcContext;
	private TAB dbTable;
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
	
	public AbstractDbTableOperation(TAB dbTable, CTX jdbcContext) {
		this.dbTable = dbTable;
		this.jdbcContext = jdbcContext;
	}

	/**
	 * 获取操作的{@link DbTable}
	 * @return
	 */
	public TAB getDbTable() {
		return dbTable;
	}

	public void setDbTable(TAB dbTable) {
		this.dbTable = dbTable;
	}

	/**
	 * 获取数据库操作的上下文
	 * @return
	 */
	public CTX getJdbcContext() {
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
	
	public boolean execute() throws Exception {
		if (this.isProcessDefault()) {
			try {
				return doExecute();
			} finally {
				setProcessDefault(false);
			}
		}
		return false;
	}
	
	/**
	 * 执行操作动作
	 */
	protected abstract boolean doExecute() throws Exception;
}
