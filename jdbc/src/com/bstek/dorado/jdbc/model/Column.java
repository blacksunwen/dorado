package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.type.JdbcType;

public class Column {
	
	private String columnName;

	private String propertyName;

	private JdbcType jdbcType;
	
	private boolean selectable = true;

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setJdbcType(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}

}
