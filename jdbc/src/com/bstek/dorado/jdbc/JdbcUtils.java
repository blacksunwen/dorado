package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.config.DbmManager;
import com.bstek.dorado.jdbc.config.JdbcCreationContext;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgram;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgramContext;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgramOperation;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的工具类
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class JdbcUtils {

	@SuppressWarnings("unchecked")
	private static <T> T getServiceBean(String serviceName) {
		Context ctx = Context.getCurrent();
		try {
			return (T)ctx.getServiceBean(serviceName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static JdbcEnviromentManager getEnviromentManager() {
		JdbcEnviromentManager manager = getServiceBean("jdbc.enviromentManager");
		return manager;
	}
	
	public static DbmManager getDbmManager() {
		DbmManager manager = (DbmManager)getServiceBean("jdbc.dbmManager");
		return manager;
	}
	
	public static ModelGeneratorSuit getModelGeneratorSuit() {
		ModelGeneratorSuit suit = getServiceBean("jdbc.modelGeneratorSuit");
		return suit;
	}
	
	public static ModelStrategy getModelStrategy() {
		ModelStrategy strategy = getServiceBean("jdbc.modelStrategy");
		return strategy;
	}
	
	public static JdbcIntercepter getGlobalIntercepter() {
		JdbcIntercepter intercepter = getServiceBean("jdbc.globalIntercepter");
		return intercepter;
	}
	
	public static DbTable getDbTable(String tableName) {
		Assert.notEmpty(tableName, "name of DbTable must not be null.");
		DbElementDefinition definition = JdbcUtils.getDbmManager().getDefinition(tableName);
		JdbcCreationContext context = new JdbcCreationContext();
		
		try {
			DbTable table = (DbTable)definition.create(context);
			return table;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static Collection<Record> query(String tableName, Object parameter) {
		JdbcDataProviderContext jCtx = new JdbcDataProviderContext(null, parameter);
		DbTable table = JdbcUtils.getDbTable(tableName);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jCtx);
		
		return query(operation);
	}
	
	public static Collection<Record> query(JdbcDataProviderOperation operation) {
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
	
	public static void insert(String tableName, Record record) {
		record = getRecordWithState(record, EntityState.NEW);
		DbTable dbTable = getDbTable(tableName);
		doSave(dbTable, record, null);
	}
	
	public static void update(String tableName, Record record) {
		record = getRecordWithState(record, EntityState.MODIFIED);
		DbTable dbTable = getDbTable(tableName);
		doSave(dbTable, record, null);
	}
	
	public static void delete(String tableName, Record record) {
		record = getRecordWithState(record, EntityState.DELETED);
		DbTable dbTable = getDbTable(tableName);
		doSave(dbTable, record, null);
	}
	
	public static Record getRecordWithState(Record record, EntityState state) {
		if (!EntityUtils.isEntity(record)) {
			try {
				record = EntityUtils.toEntity(record);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		EntityUtils.setState(record, state);
		return record;
	}
	
	public static void doSave(DbTable dbTable, Record record, JdbcDataResolverContext jdbcContext) {
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
	
	public static boolean doResolve(JdbcRecordOperation operation) {
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
	
	public static StoredProgram getStoredProgram(String spName) {
		Assert.notEmpty(spName, "name of StoredProgram must not be null.");
		DbElementDefinition definition = JdbcUtils.getDbmManager().getDefinition(spName);
		JdbcCreationContext context = new JdbcCreationContext();
		
		try {
			StoredProgram dbElement = (StoredProgram)definition.create(context);
			return dbElement;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
//	public static Object call(String spName, Object parameter) {
//		StoredProgram sp = null;
//		StoredProgramContext spContext = new StoredProgramContext(null, parameter);
//		StoredProgramOperation operation = new StoredProgramOperation(sp, spContext);
//		operation.execute();
//		
//		return spContext.getReturnValue();
//	}
//	
//	public static Object call(StoredProgram sp, Object parameter) {
//		StoredProgramContext spContext = new StoredProgramContext(null, parameter);
//		StoredProgramOperation operation = new StoredProgramOperation(sp, spContext);
//		operation.execute();
//		
//		return spContext.getReturnValue();
//	}
}
