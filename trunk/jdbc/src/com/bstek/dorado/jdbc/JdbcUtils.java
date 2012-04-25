package com.bstek.dorado.jdbc;

import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.config.DbmManager;
import com.bstek.dorado.jdbc.config.JdbcCreationContext;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的工具类
 * 
 * @author mark.li@bstek.com
 *
 */
public final class JdbcUtils {

	private JdbcUtils(){}
	
	/**
	 * 获取一个DbTable对象
	 * 
	 * @param tableName
	 * @return
	 */
	public static <T extends DbTable> T getDbTable(String tableName) {
		Assert.notEmpty(tableName, "name of DbTable must not be null.");
		DbElementDefinition definition = getDbmManager().getDefinition(tableName);
		JdbcCreationContext context = new JdbcCreationContext();
		
		try {
			@SuppressWarnings("unchecked")
			T table = (T)definition.create(context);
			return table;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
	
	public static com.bstek.dorado.data.provider.Criteria getFilterCriteria(Object parameter) {
		if (parameter instanceof ParameterWrapper) {
			ParameterWrapper pw = (ParameterWrapper)parameter;
			Map<String, Object> sysParameter = pw.getSysParameter();
			
			if (sysParameter instanceof Record) {
				Record paraRecord = (Record)sysParameter;
				return (Criteria)paraRecord.get("criteria");
			}
		}
		
		return null;
	}
	
	public static Object getRealParameter(Object parameter) {
		if (parameter != null) {
			if (parameter instanceof ParameterWrapper) {
				return ((ParameterWrapper)parameter).getParameter();
			}
		}
		
		return parameter;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getServiceBean(String serviceName) {
		Context ctx = Context.getCurrent();
		try {
			return (T)ctx.getServiceBean(serviceName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static DbmManager dbmManager = null;
	private static DbmManager getDbmManager() {
		if (dbmManager == null) {
			dbmManager = (DbmManager)getServiceBean("jdbc.dbmManager");
		}
		return dbmManager;
	}
	
//	public static StoredProgram getStoredProgram(String spName) {
//		Assert.notEmpty(spName, "name of StoredProgram must not be null.");
//		DbElementDefinition definition = JdbcUtils.getDbmManager().getDefinition(spName);
//		JdbcCreationContext context = new JdbcCreationContext();
//		
//		try {
//			StoredProgram dbElement = (StoredProgram)definition.create(context);
//			return dbElement;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
	
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
