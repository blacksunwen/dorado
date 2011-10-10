package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.jdbc.JdbcParameterSource;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.jdbc.sql.SqlUtils;

public class SqlTableSqlGenerator implements SqlGenerator<SqlTable> {

	public DbElement.Type getType() {
		return DbElement.Type.SqlTable;
	}
	
	@Override
	public SelectSql selectSql(SqlTable t, Object parameter) {
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

}
