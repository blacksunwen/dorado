package com.bstek.dorado.jdbc.model.storedprogram;

import com.bstek.dorado.jdbc.type.JdbcType;

public class AbstractParameter {

	private String name;
	
	private JdbcType jdbcType;
	
	private Object value;

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

}
