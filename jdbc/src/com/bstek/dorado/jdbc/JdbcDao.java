package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.model.table.KeyObject;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.support.DefaultJdbcContext;
import com.bstek.dorado.jdbc.support.DeleteAllOperation;
import com.bstek.dorado.jdbc.support.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.support.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.support.JdbcDataResolverContext;
import com.bstek.dorado.jdbc.support.JdbcRecordOperation;
import com.bstek.dorado.jdbc.support.JdbcRecordOperationProxy;
import com.bstek.dorado.util.Assert;

public class JdbcDao {

	/**
	 * 查询
	 * @param tableName 表名称
	 * @param parameter 查询参数
	 * @return 查询结果
	 * @throws Exception
	 */
	public Collection<Record> query(String tableName, Object parameter) throws Exception {
		JdbcDataProviderContext jCtx = new JdbcDataProviderContext(null, parameter);
		DbTable table = JdbcUtils.getDbTable(tableName);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jCtx);
		
		return query(operation);
	}
	
	/**
	 * 查询
	 * @param operation 查询操作对象
	 * @return 查询结果
	 * @throws Exception
	 */
	public Collection<Record> query(JdbcDataProviderOperation operation) throws Exception{
		if (operation.isProcessDefault()) {
			DbTable table = operation.getDbTable();
			Assert.notNull(table, "DbTable must not be null.");
			
			DbTableTrigger trigger = table.getTrigger();
			if (trigger != null) {
				trigger.doQuery(operation);
			} 
			
			if (operation.isProcessDefault()) {
				operation.execute();
			}
		}
		
		Page<Record> page = operation.getJdbcContext().getPage();
		return page.getEntities();
	}
	
	/**
	 * 根据Table的主键值获取记录
	 * 
	 * @param table 表对象
	 * @param keys  主键值
	 * @return
	 */
	public Record getByKey(Table table, Object... keys) throws Exception {
		JdbcDataProviderContext jCtx = new JdbcDataProviderContext();
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jCtx);
		KeyObject keyObject = table.createKeyObject(keys);
		jCtx.setParameter(keyObject);
		
		Collection<Record> records = query(operation);
		if (records == null || records.size() == 0){
			return null;
		} else if (records.size() == 1) {
			return records.iterator().next();
		} else if (records.size() > 1) {
			throw new IllegalArgumentException("too many records be retrieved [" + records.size() + "], only one excepted.");
		} else {
			return null;
		}
	}
	
	
	public void deleteAll(Table table, String columnName, Object columnValue) throws Exception {
		deleteAll(table, Collections.singletonMap(columnName, columnValue));
	}
	
	public void deleteAll(Table table, Map<String, Object> columnValueMap) throws Exception {
		DefaultJdbcContext jCtx = new DefaultJdbcContext(table.getJdbcEnviroment(), null);
		DeleteAllOperation operation = new DeleteAllOperation(table, jCtx, columnValueMap);
		operation.execute();
	}
	
	/**
	 * 插入数据
	 * @param tableName 表名称
	 * @param record 被插入的记录
	 * @throws Exception
	 */
	public void insert(String tableName, Record record) throws Exception {
		record = JdbcUtils.getRecordWithState(record, EntityState.NEW);
		DbTable dbTable = JdbcUtils.getDbTable(tableName);
		doSave(dbTable, record, null);
	}
	
	/**
	 * 更新数据
	 * @param tableName 表名称
	 * @param record 被更新的记录
	 * @throws Exception
	 */
	public void update(String tableName, Record record) throws Exception {
		record = JdbcUtils.getRecordWithState(record, EntityState.MODIFIED);
		DbTable dbTable = JdbcUtils.getDbTable(tableName);
		doSave(dbTable, record, null);
	}
	
	/**
	 * 删除数据
	 * @param tableName 表名称
	 * @param record 被更新的记录
	 * @throws Exception
	 */
	public void delete(String tableName, Record record) throws Exception{
		record = JdbcUtils.getRecordWithState(record, EntityState.DELETED);
		DbTable dbTable = JdbcUtils.getDbTable(tableName);
		doSave(dbTable, record, null);
	}
	
	public void doSave(DbTable dbTable, Record record, JdbcDataResolverContext jdbcContext) throws Exception{
		if (dbTable.supportResolverTable()) {
			Table table = dbTable.getResolverTable();
			if (table != null) {
				JdbcRecordOperationProxy proxy = dbTable.createOperationProxy(record, jdbcContext);
				if (proxy != null) {
					JdbcRecordOperation proxyOperation = proxy.getProxyOperation();
					if (doResolve(proxyOperation)) {
						Map<String, String> propertyMap = proxy.getProxyPropertyMap();
						if (propertyMap != null) {
							Record proxyRecord = proxyOperation.getRecord();
							
							Iterator<String> keyItr = propertyMap.keySet().iterator();
							while (keyItr.hasNext()) {
								String propertyName = keyItr.next();
								String proxyPropertyName = propertyMap.get(propertyName);
								Object proxyValue = proxyRecord.get(proxyPropertyName);
								record.put(propertyName, proxyValue);
							}
						}
					}
				}
			}
		} else {
			JdbcRecordOperation operation = new JdbcRecordOperation((Table)dbTable, record, jdbcContext);
			doResolve(operation);
		}
	}
	
	public boolean doResolve(JdbcRecordOperation operation) throws Exception{
		DbTable table = operation.getDbTable();
		DbTableTrigger trigger = table.getTrigger();
		if (trigger == null) {
			return operation.execute();
		} else {
			trigger.doSave(operation);
			if (operation.isProcessDefault()) {
				return operation.execute();
			}
			return true;
		}
	}
}
