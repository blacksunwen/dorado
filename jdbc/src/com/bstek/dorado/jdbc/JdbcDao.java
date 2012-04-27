package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.table.KeyObject;
import com.bstek.dorado.jdbc.support.DataProviderContext;
import com.bstek.dorado.jdbc.support.DefaultJdbcContext;
import com.bstek.dorado.jdbc.support.DeleteAllOperation;
import com.bstek.dorado.jdbc.support.QueryOperation;
import com.bstek.dorado.jdbc.support.SaveOperation;
import com.bstek.dorado.jdbc.support.SaveRecordOperation;

public class JdbcDao {

	/**
	 * 查询
	 * @param tableName 表名称
	 * @param parameter 查询参数
	 * @return 查询结果
	 * @throws Exception
	 */
	public Collection<Record> query(String tableName, Object parameter) throws Exception {
		DataProviderContext jCtx = new DataProviderContext(null, parameter);
		DbTable table = JdbcUtils.getDbTable(tableName);
		QueryOperation operation = new QueryOperation(table, jCtx);
		
		return this.query(operation);
	}
	
	/**
	 * 查询
	 * @param operation 查询操作对象
	 * @return 查询结果
	 * @throws Exception
	 */
	public Collection<Record> query(QueryOperation operation) throws Exception {
		DbTableTrigger trigger = operation.getDbTable().getTrigger();
		if (trigger != null) {
			trigger.doQuery(operation);
		}
		
		operation.run();
		
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
		DataProviderContext jCtx = new DataProviderContext();
		QueryOperation operation = new QueryOperation(table, jCtx);
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
		operation.run();
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

		SaveOperation operation = new SaveOperation(dbTable,record, null);
		this.doSave(operation);
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
		
		SaveOperation operation = new SaveOperation(dbTable,record, null);
		this.doSave(operation);
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
		
		SaveOperation operation = new SaveOperation(dbTable,record, null);
		this.doSave(operation);
	}
	
	public void doSave(SaveOperation operation) throws Exception {
		DbTableTrigger trigger = operation.getDbTable().getTrigger();
		if (trigger != null) {
			trigger.doSave(operation);
		}
		
		operation.run();
	}

	public void doSave(SaveRecordOperation operation) throws Exception {
		DbTableTrigger trigger = operation.getDbTable().getTrigger();
		if (trigger != null) {
			trigger.doSave(operation);
		}
		
		operation.run();
	}
}
