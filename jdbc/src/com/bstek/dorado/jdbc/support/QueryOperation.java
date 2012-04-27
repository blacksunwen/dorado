package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.jdbc.model.DbTable;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataProvider}对应的数据库操作
 * 
 * @author mark.li@bstek.com
 * 
 */
public class QueryOperation extends
		AbstractDbTableOperation<DataProviderContext, DbTable> {

	public QueryOperation(DbTable dbTable,
			DataProviderContext jdbcContext) {
		super(dbTable, jdbcContext);
	}

	public Object getParameter() {
		DataProviderContext ctx = this.getJdbcContext();
		if (ctx != null) {
			return ctx.getParameter();
		} else {
			return null;
		}
	}
	
	public Criteria getCriteria() {
		DataProviderContext ctx = this.getJdbcContext();
		if (ctx != null) {
			return ctx.getCriteria();
		} else {
			return null;
		}
	}
	
	protected boolean doRun() throws Exception {
		return getDialect().execute(this);
	}
}
