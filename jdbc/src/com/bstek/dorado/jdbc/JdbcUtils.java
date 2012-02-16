package com.bstek.dorado.jdbc;

import java.util.Collection;

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
	
	public static DbmManager getDbmManager() {
		Context context = Context.getCurrent();
		try {
			DbmManager parser = (DbmManager)context.getServiceBean("jdbc.dbmManager");
			return parser;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ModelGeneratorSuit getModelGeneratorSuit() {
		Context context = Context.getCurrent();
		try {
			ModelGeneratorSuit strategy = (ModelGeneratorSuit)context.getServiceBean("jdbc.modelGeneratorSuit");
			return strategy;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ModelStrategy getJdbcModelStrategy() {
		Context context = Context.getCurrent();
		try {
			ModelStrategy strategy = (ModelStrategy)context.getServiceBean("jdbc.modelStrategy");
			return strategy;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static DbTable getDbTable(String tableName) {
		Assert.notEmpty(tableName, "name of DbTable must not be null.");
		DbElementDefinition definition = JdbcUtils.getDbmManager().getDefinition(tableName);
		JdbcCreationContext context = new JdbcCreationContext();
		
		try {
			DbTable dbElement = (DbTable)definition.create(context);
			return dbElement;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	
	public static Collection<Record> query(String tableName, QueryArg arg) {
		if (arg == null) {
			arg = new QueryArg();
		}
		
		JdbcEnviroment jdbcEnviroment = arg.getJdbcEnviroment();
		Page<Record> page = arg.getPage();
		Object parameter = arg.getParameter();
		
		DbTable table = JdbcUtils.getDbTable(tableName);
		
		JdbcDataProviderContext rCtx = new JdbcDataProviderContext(jdbcEnviroment, parameter, page);
		JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, rCtx);
		
		TableTrigger trigger = table.getTrigger();
		if (trigger != null) {
			trigger.doQuery(operation);
		} else {
			operation.execute();
		}
		return page.getEntities();
	}
	
	public static Collection<Record> query(String tableName, Object parameter) {
		QueryArg arg = new QueryArg();
		arg.setParameter(parameter);
		
		return query(tableName, arg);
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
		DbTable table = getDbTable(tableName);
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
