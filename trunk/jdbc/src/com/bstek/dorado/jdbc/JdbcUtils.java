package com.bstek.dorado.jdbc;

import java.util.Map;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.ParameterWrapper;
import com.bstek.dorado.data.config.definition.DataCreationContext;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.config.DbmDefinitionManager;
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
		DbElementDefinition definition = ((DbmDefinitionManager)getServiceBean("jdbc.dbmDefinitionManager")).getDefinition(tableName);
		Assert.notNull(definition, "no definition named [" + tableName + "]");
		
		DataCreationContext context = new DataCreationContext();
		
		try {
			@SuppressWarnings("unchecked")
			T table = (T)definition.create(context);
			return table;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 对记录加工，使其具有相应的状态
	 * @param record
	 * @param state
	 * @return
	 */
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
	
	/**
	 * 从前台参数中获取自动过滤条件
	 * @param parameter
	 * @return
	 */
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
	
	/**
	 * 从前台参数中获取查询参数
	 * @param parameter
	 * @return
	 */
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

}
