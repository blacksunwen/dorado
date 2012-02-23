package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperation;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public interface CurdSqlGenerator {

	SelectSql selectSql(JdbcDataProviderOperation operation);
	
	InsertSql insertSql(JdbcRecordOperation operation);
	
	UpdateSql updateSql(JdbcRecordOperation operation);
	
	DeleteSql deleteSql(JdbcRecordOperation operation);
}
