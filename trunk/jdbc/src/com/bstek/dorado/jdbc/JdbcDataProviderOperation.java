package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.AbstractJdbcOperation;
import com.bstek.dorado.jdbc.model.DbElement;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataProvider}对应的数据库操作
 * 
 * @author mark
 * 
 */
public class JdbcDataProviderOperation extends
		AbstractJdbcOperation<JdbcDataProviderContext> {

	public JdbcDataProviderOperation(DbElement dbElement,
			JdbcDataProviderContext jdbcContext) {
		super(dbElement, jdbcContext);
	}

	@Override
	public void execute() {
		getJdbcEnviroment().getDialect().execute(this);
	}

}
