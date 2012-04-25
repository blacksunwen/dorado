package com.bstek.dorado.jdbc.support;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.jdbc.Dialect;
import com.bstek.dorado.jdbc.JdbcDao;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractJdbcEnviroment implements JdbcEnviroment, InitializingBean {

	private String name;
	private JdbcEnviromentManager manager;
	private boolean isdefault = false;
	private Dialect dialect;
	private JdbcDao jdbcDao;
	
	public JdbcEnviromentManager getManager() {
		return manager;
	}

	public void setManager(JdbcEnviromentManager manager) {
		this.manager = manager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefault() {
		return isdefault;
	}

	public void setDefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public void afterPropertiesSet() throws Exception {
		manager.register(this);
	}

	public JdbcDao getJdbcDao() {
		return jdbcDao;
	}

	public void setJdbcDao(JdbcDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}
}
