package com.bstek.dorado.jdbc;

import com.bstek.dorado.data.variant.Record;

public abstract class AbstractTestTable {
	
	private JdbcEnviroment jdbcEnviroment;
	
	public abstract String getCreateScript();
	public abstract String getDropScript();
	
	public AbstractTestTable(JdbcEnviroment jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}
	
	protected JdbcEnviroment getJdbcEnviroment() {
		return jdbcEnviroment;
	}
	
	public void doTest(Action action) throws Exception {
		String createScript = getCreateScript();
		jdbcEnviroment.getSpringNamedDao().getJdbcTemplate().update(createScript);
		try {
			action.doAction(jdbcEnviroment);
		} finally {
			String dropScript = getDropScript();
			jdbcEnviroment.getSpringNamedDao().getJdbcTemplate().update(dropScript);
		}
	}
	
	public Record get(Object key) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	public static interface Action {
		void doAction(JdbcEnviroment jdbcEnviroment) throws Exception;
	}
}
