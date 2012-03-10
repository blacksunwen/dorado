package com.bstek.dorado.jdbc;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.jdbc.config.DbmManager;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;

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
	
	public static JdbcIntercepter getGlobalIntercepter() {
		JdbcIntercepter intercepter = getServiceBean("jdbc.globalIntercepter");
		return intercepter;
	}

}
