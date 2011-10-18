package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.model.DbElement;

public interface SqlGenerator {

	DbElement.Type getType();
	
	SelectSql selectSql(JdbcDataProviderOperation operation);
	
	InsertSql insertSql(JdbcRecordOperation operation);
	
	UpdateSql updateSql(JdbcRecordOperation operation);
	
	DeleteSql deleteSql(JdbcRecordOperation operation);
}
