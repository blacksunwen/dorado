package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperation;

public interface SqlGenerator {

	String getType();
	
	SelectSql selectSql(JdbcDataProviderOperation operation);
	
	InsertSql insertSql(JdbcRecordOperation operation);
	
	UpdateSql updateSql(JdbcRecordOperation operation);
	
	DeleteSql deleteSql(JdbcRecordOperation operation);
}
