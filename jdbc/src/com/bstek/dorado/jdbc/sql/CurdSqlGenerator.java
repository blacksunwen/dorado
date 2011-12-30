package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperation;

public interface CurdSqlGenerator {

	SelectSql selectSql(JdbcDataProviderOperation operation);
	
	InsertSql insertSql(JdbcRecordOperation operation);
	
	UpdateSql updateSql(JdbcRecordOperation operation);
	
	DeleteSql deleteSql(JdbcRecordOperation operation);
}
