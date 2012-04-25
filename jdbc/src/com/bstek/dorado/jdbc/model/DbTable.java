package com.bstek.dorado.jdbc.model;

import java.util.List;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.DbTableTrigger;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.support.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.support.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.support.JdbcRecordOperationProxy;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public interface DbTable extends DbElement {

	List<AbstractDbColumn> getAllColumns();
	
	AbstractDbColumn getColumn(String name);
	
	DbTableTrigger getTrigger();

	boolean supportResolverTable();
	
	Table getResolverTable();
	
	JdbcRecordOperationProxy createOperationProxy(Record record, JdbcDataResolverContext jdbcContext);
	
	SelectSql selectSql(JdbcDataProviderOperation operation);
}
