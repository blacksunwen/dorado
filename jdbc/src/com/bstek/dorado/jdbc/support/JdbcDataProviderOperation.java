package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.model.DbTable;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataProvider}对应的数据库操作
 * 
 * @author mark.li@bstek.com
 * 
 */
public class JdbcDataProviderOperation extends
		AbstractDbTableOperation<JdbcDataProviderContext, DbTable> {

	public JdbcDataProviderOperation(DbTable dbTable,
			JdbcDataProviderContext jdbcContext) {
		super(dbTable, jdbcContext);
	}

	protected boolean doExecute() throws Exception {
		return getJdbcEnviroment().getDialect().execute(this);
	}
	
	public Object getParameter() {
		JdbcDataProviderContext ctx = this.getJdbcContext();
		if (ctx != null) {
			return ctx.getParameter();
		} else {
			return null;
		}
	}
}
