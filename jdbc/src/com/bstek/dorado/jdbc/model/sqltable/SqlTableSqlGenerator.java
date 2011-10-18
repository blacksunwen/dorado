package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;
import com.bstek.dorado.jdbc.sql.UpdateSql;

public class SqlTableSqlGenerator implements SqlGenerator {

	public DbElement.Type getType() {
		return DbElement.Type.SqlTable;
	}
	
	@Override
	public SelectSql selectSql(JdbcDataProviderOperation operation) {
		SqlTable t = (SqlTable)operation.getDbElement();
		Object parameter = operation.getJdbcContext().getParameter();
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
	public InsertSql insertSql(JdbcRecordOperation operation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateSql updateSql(JdbcRecordOperation operation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteSql deleteSql(JdbcRecordOperation operation) {
		throw new UnsupportedOperationException();
	}

}
