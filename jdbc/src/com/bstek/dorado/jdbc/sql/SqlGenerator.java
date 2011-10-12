package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.model.DbElement;

public interface SqlGenerator<T> {

	DbElement.Type getType();
	
	SelectSql selectSql(T t, Object parameter, JdbcDataProviderContext jdbcContext);
	
	InsertSql insertSql(T t, Record record, JdbcDataResolverContext jdbcContext);
	
	UpdateSql updateSql(T t, Record record, JdbcDataResolverContext jdbcContext);
	
	DeleteSql deleteSql(T t, Record record, JdbcDataResolverContext jdbcContext);
}
