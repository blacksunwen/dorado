package com.bstek.dorado.jdbc.model.storedprogram;

import com.bstek.dorado.jdbc.type.JdbcType;

public class ProgramParameter {
	private String name;
	
	private JdbcType jdbcType;
	
	private Object value;

	private Type type = Type.IN;
	
	public enum Type {
		IN, OUT, INOUT
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}
