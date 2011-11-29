package com.bstek.dorado.jdbc;

import java.util.Collection;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.config.JdbcConfigManager;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.model.TableTrigger;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgram;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgramContext;
import com.bstek.dorado.jdbc.model.storedprogram.StoredProgramOperation;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的工具类
 * 
 * @author mark
 *
 */
public abstract class JdbcUtils {

	public static JdbcEnviromentManager getEnviromentManager() {
		Context ctx = Context.getCurrent();
		try {
			JdbcEnviromentManager manager = (JdbcEnviromentManager)ctx.getServiceBean("jdbc.enviromentManager");
			return manager;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static JdbcConfigManager getJdbcConfigManager() {
		Context context = Context.getCurrent();
		try {
			JdbcConfigManager parser = (JdbcConfigManager)context.getServiceBean("jdbc.jdbcConfigManager");
			return parser;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static SqlGenerator getSqlGenerator(DbElement dbElement) {
		SqlGenerator generator = getJdbcConfigManager().getSqlGenerator(dbElement.getType());
		return generator;
	}
	
	public static DbElement getDbElement(String elementName) {
		Assert.notEmpty(elementName, "DbElement name must not be null.");
		DbElement dbElement = JdbcUtils.getJdbcConfigManager().getDbElement(elementName);
		
		return dbElement;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Collection<Record> query(String tableName, Object parameter) {
		Page page = new Page(0, 0);
		query(tableName, parameter, page);
		return (Collection<Record>)page.getEntities();
	}
	
	@SuppressWarnings("rawtypes") 
	public static void query(String tableName, Object parameter, Page page) {
		Assert.notEmpty(tableName, "tableName must not be null.");

		DbElement dbElement = JdbcUtils.getDbElement(tableName);
		Assert.isTrue(dbElement instanceof DbTable, "[" + tableName + "] is not a table.");
		
		DbTable table = (DbTable)dbElement;
		JdbcDataProviderContext rCtx = new JdbcDataProviderContext(table.getJdbcEnviroment(),parameter, page);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, rCtx);
		TableTrigger trigger = table.getTrigger();
		
		if (trigger != null) {
			trigger.doQuery(operation);
		} else {
			operation.execute();
		}
	}
	
	public static void insert(String tableName, Record record) {
		doSave(tableName, record, EntityState.NEW);
	}
	
	public static void update(String tableName, Record record) {
		doSave(tableName, record, EntityState.MODIFIED);
	}
	
	public static void delete(String tableName, Record record) {
		doSave(tableName, record, EntityState.DELETED);
	}
	
	public static void doSave(String tableName, Record record, EntityState state) {
		DbElement dbe = getDbElement(tableName);
		Assert.isTrue(dbe instanceof DbTable, "[" + tableName + "] is not a table.");
		DbTable table = (DbTable)dbe;
		
		Record enRecord = record;
		if (!EntityUtils.isEntity(enRecord)) {
			try {
				enRecord = EntityUtils.toEntity(enRecord);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		EntityUtils.setState(enRecord, state);
		
		JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(table.getJdbcEnviroment(), null, null, null);
		JdbcRecordOperation operation = new JdbcRecordOperation(table, enRecord, jdbcContext);
		
		TableTrigger trigger = table.getTrigger();
		if (trigger == null) {
			operation.execute();
		} else {
			trigger.doSave(operation);
		}
	}
	
	public static Object call(String spName, Object parameter) {
		StoredProgram sp = null;
		StoredProgramContext spContext = new StoredProgramContext(null, parameter);
		StoredProgramOperation operation = new StoredProgramOperation(sp, spContext);
		operation.execute();
		
		return spContext.getReturnValue();
	}
	
	public static Object call(StoredProgram sp, Object parameter) {
		StoredProgramContext spContext = new StoredProgramContext(null, parameter);
		StoredProgramOperation operation = new StoredProgramOperation(sp, spContext);
		operation.execute();
		
		return spContext.getReturnValue();
	}
	
}
