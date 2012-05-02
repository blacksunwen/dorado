package com.bstek.dorado.jdbc.model;

import java.util.List;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.support.QueryOperation;
import com.bstek.dorado.jdbc.support.DataResolverContext;
import com.bstek.dorado.jdbc.support.RecordOperationProxy;

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
	
	RecordOperationProxy createOperationProxy(Record record, DataResolverContext jdbcContext);
	
	SelectSql selectSql(QueryOperation operation);
}
