package com.bstek.dorado.jdbc.model;

import java.util.List;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.support.QueryOperation;
import com.bstek.dorado.jdbc.support.DataResolverContext;
import com.bstek.dorado.jdbc.support.RecordOperationProxy;

/**
 * JDBC表概念的接口
 * 
 * @author mark.li@bstek.com
 *
 */
public interface DbTable extends DbElement {

	/**
	 * 获取“表”的全部的“列”
	 * @return
	 */
	List<AbstractDbColumn> getAllColumns();
	
	/**
	 * 是否包含特定名称的“列”
	 * @param name
	 * @return
	 */
	boolean hasColumn(String name);
	
	/**
	 * 根据名称获取特定的“列”
	 * @param name
	 * @return
	 */
	AbstractDbColumn getColumn(String name);
	
	/**
	 * 获取“触发器”
	 * @return
	 */
	DbTableTrigger getTrigger();

	/**
	 * 计算查询语句
	 * @param operation
	 * @return
	 */
	SelectSql selectSql(QueryOperation operation);
	
	/**
	 * 是否支持“持久化表”
	 * @return
	 */
	boolean supportResolverTable();
	
	/**
	 * 获取“持久化表”
	 * @return
	 */
	Table getResolverTable();
	
	/**
	 * 创建记录操作代理对象
	 * @param record
	 * @param jdbcContext
	 * @return
	 */
	RecordOperationProxy createOperationProxy(Record record, DataResolverContext jdbcContext);
	
}
