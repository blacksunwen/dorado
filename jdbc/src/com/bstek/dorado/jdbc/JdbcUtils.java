package com.bstek.dorado.jdbc;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.jdbc.config.JdbcConfigManager;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;
import com.bstek.dorado.jdbc.model.DbElement;
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
}
