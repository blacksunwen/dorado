package com.bstek.dorado.jdbc.test;

import org.springframework.jdbc.core.JdbcTemplate;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;

public abstract class AbstractDbElement {

	@SuppressWarnings("unchecked")
	private static <T> T getServiceBean(String serviceName) {
		Context ctx = Context.getCurrent();
		try {
			return (T)ctx.getServiceBean(serviceName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public JdbcEnviromentManager getEnviromentManager() {
		JdbcEnviromentManager manager = getServiceBean("jdbc.enviromentManager");
		return manager;
	}
	
	
	public void create() {
		JdbcEnviroment env = this.getEnviromentManager().getDefault();
		JdbcTemplate tpl = env.getSpringNamedDao().getJdbcTemplate();
		
		String sql = this.toCreateSQL();
		tpl.update(sql);
	}
	
	public void drop() {
		JdbcEnviroment env = this.getEnviromentManager().getDefault();
		JdbcTemplate tpl = env.getSpringNamedDao().getJdbcTemplate();
		
		String sql = this.toDropSQL();
		tpl.update(sql);
	}
	
	protected abstract String toCreateSQL();
	protected abstract String toDropSQL();
}
