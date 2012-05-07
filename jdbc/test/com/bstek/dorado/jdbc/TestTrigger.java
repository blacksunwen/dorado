package com.bstek.dorado.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

public class TestTrigger {

	private String name;
	private String position;
	private String tableName;
	private Class<? extends org.h2.api.Trigger> triggerClazz;
	
	public TestTrigger(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public TestTrigger setPosition(String position) {
		this.position = position;
		return this;
	}
	
	public TestTrigger setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public TestTrigger setClazz(Class<? extends org.h2.api.Trigger> triggerClazz) {
		this.triggerClazz = triggerClazz;
		return this;
	}
	
	public void create() {
		JdbcEnviroment env = TestJdbcUtils.getEnviromentManager().getDefault();
		JdbcTemplate tpl = env.getSpringNamedDao().getJdbcTemplate();
		
		String sql = this.toCreateSQL();
		tpl.update(sql);
	}
	
	public void drop() {
		JdbcEnviroment env = TestJdbcUtils.getEnviromentManager().getDefault();
		JdbcTemplate tpl = env.getSpringNamedDao().getJdbcTemplate();
		
		String sql = this.toDropSQL();
		tpl.update(sql);
	}
	
	private String toCreateSQL() {
		String sql = "CREATE TRIGGER " + name + " " + position + " ON " + tableName + 
				" FOR EACH ROW" + " CALL \"" + triggerClazz.getName() + "\"";
		return sql;
	}
	
	private String toDropSQL() {
		return "DROP TRIGGER " + name;
	}
}
