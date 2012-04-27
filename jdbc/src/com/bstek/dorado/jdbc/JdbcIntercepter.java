package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.support.DataResolverOperation;
import com.bstek.dorado.jdbc.support.DeleteAllOperation;
import com.bstek.dorado.jdbc.support.QueryOperation;
import com.bstek.dorado.jdbc.support.SaveOperation;
import com.bstek.dorado.jdbc.support.SaveRecordOperation;
import com.bstek.dorado.jdbc.support.TableRecordOperation;

/**
 * JDBC模块的拦截器
 * 
 * @author mark.li@bstek.com
 *
 */
public interface JdbcIntercepter {

	QueryOperation getOperation(QueryOperation operation);
	
	DataResolverOperation getOperation(DataResolverOperation operation);
	
	TableRecordOperation getOperation(TableRecordOperation operation);
	
	DeleteAllOperation getOperation(DeleteAllOperation operation);
	
	SaveOperation getOperation(SaveOperation operation);
	
	SaveRecordOperation getOperation(SaveRecordOperation operation);
	
	DbElementDefinition getDefinition(DbElementDefinition def);
	
}
