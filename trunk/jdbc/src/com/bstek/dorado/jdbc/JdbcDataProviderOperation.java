package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.DbTableJdbcOperation;
import com.bstek.dorado.jdbc.model.DbTable;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataProvider}对应的数据库操作
 * 
 * @author mark
 * 
 */
public class JdbcDataProviderOperation extends
		DbTableJdbcOperation<JdbcDataProviderContext> {

	public JdbcDataProviderOperation(DbTable dbTable,
			JdbcDataProviderContext jdbcContext) {
		super(dbTable, jdbcContext);
	}

	@Override
	public void execute() {
		getJdbcEnviroment().getDialect().execute(this);
	}

}
