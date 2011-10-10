package com.bstek.dorado.jdbc;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.jdbc.manager.JdbcEnviromentManager;
import com.bstek.dorado.jdbc.manager.JdbcModelManager;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.SqlGenerator;
import com.bstek.dorado.util.Assert;

public class JdbcUtils {

	public static JdbcEnviromentManager getEnviromentManager() {
		Context ctx = Context.getCurrent();
		try {
			JdbcEnviromentManager manager = (JdbcEnviromentManager)ctx.getServiceBean("jdbc.enviromentManager");
			return manager;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static JdbcModelManager getJdbcModelManager() {
		Context context = Context.getCurrent();
		try {
			JdbcModelManager parser = (JdbcModelManager)context.getServiceBean("jdbc.jdbcModelManager");
			return parser;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static SqlGenerator getSqlGenerator(DbElement dbElement) {
		SqlGenerator generator = getJdbcModelManager().getSqlGenerator(dbElement.getType());
		return generator;
	}
	
	public static DbElement getDbElement(String elementName) throws Exception {
		Assert.notEmpty(elementName, "DbElement name must not be null.");
		
		JdbcModelManager manager = JdbcUtils.getJdbcModelManager();
		DbElement dbElement = manager.getDbElement(elementName);
		
		return dbElement;
	}
	
//	public static Table getTable(String objRef) throws Exception {
//		DbElement dbElement = (DbElement) getDbElement(objRef);
//		if (dbElement instanceof Table){
//			return (Table)dbElement;
//		} else {
//			throw new IllegalArgumentException("[" + objRef + "] " + dbElement.getClass());
//		}
//	}
}
