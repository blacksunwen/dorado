package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.type.JdbcType;

public abstract class Column {
	
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

	@IdeProperty(visible = false)
	public String getPropertyName() {
		return propertyName;
	}

	@XmlProperty(parser="spring:dorado.jdbc.jdbcTypeParser")
	@IdeProperty(highlight=1, editor = "jdbc:list-jdbctype")
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

	@IdeProperty(visible = false)
	public abstract String getKeyName();
}
