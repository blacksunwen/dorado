package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;

public class SqlTableSqlGenerator implements SqlGenerator<SqlTable> {

	public DbElement.Type getType() {
		return DbElement.Type.SqlTable;
	}
	
	@Override
	public SelectSql selectSql(SqlTable t, Object parameter,
			JdbcDataProviderContext jdbcContext) {
		SqlSelectSql selectSql = new SqlSelectSql();
		
		//querySql
		String querySql = t.getQuerySql();
		querySql = SqlUtils.build(querySql, parameter);
		selectSql.setDynamicToken(querySql);
		
		//SqlParameterSource
		JdbcParameterSource p = SqlUtils.createJdbcParameter(parameter);
		selectSql.setParameterSource(p);
		return selectSql;
	}

	@Override
	public InsertSql insertSql(SqlTable t, Record record,
			JdbcDataResolverContext jdbcContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateSql updateSql(SqlTable t, Record record,
			JdbcDataResolverContext jdbcContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteSql deleteSql(SqlTable t, Record record,
			JdbcDataResolverContext jdbcContext) {
		throw new UnsupportedOperationException();
	}

}
