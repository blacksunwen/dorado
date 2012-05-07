package com.bstek.dorado.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

public class TestSequence {

	private String name;
	private int start = 1;
	private int increment = 1;
	
	public TestSequence(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public TestSequence setStart(int start) {
		this.start = start;
		return this;
	}
	
	public TestSequence setIncrement(int inc) {
		this.increment = inc;
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
		String sql = "CREATE SEQUENCE " + name + " START WITH " + start + " INCREMENT BY " + increment;
		return sql;
	}
	
	private String toDropSQL() {
		return "DROP SEQUENCE " + name;
	}
}
