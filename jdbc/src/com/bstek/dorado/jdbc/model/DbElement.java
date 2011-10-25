package com.bstek.dorado.jdbc.model;

import java.util.List;

import com.bstek.dorado.jdbc.JdbcEnviroment;

public interface DbElement {

	public enum Type {
		Table, SqlTable, AutoTable
	}
	
	JdbcEnviroment getJdbcEnviroment();
	
	void setJdbcEnviroment(JdbcEnviroment env);
	
	String getName();
	
	Type getType();

	DbElementTrigger getTrigger();
	
	List<Column> getAllColumns();
	
	Column getColumn(String name);
	
}
