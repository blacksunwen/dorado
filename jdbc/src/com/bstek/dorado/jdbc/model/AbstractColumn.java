package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.type.JdbcType;

public abstract class AbstractColumn {
	
	private String columnName;

	private JdbcType jdbcType;
	
	private boolean selectable = true;

	@XmlProperty(attributeOnly=true)
	@IdeProperty(highlight=1)
	public String getColumnName() {
		return columnName;
	}
	
	@XmlProperty(parser="spring:dorado.jdbc.jdbcTypeParser", attributeOnly=true)
	@IdeProperty(highlight=1, editor = "jdbc:list-jdbctype")
	public JdbcType getJdbcType() {
		return jdbcType;
	}
	
	@ClientProperty(escapeValue = "true")
	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setJdbcType(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}

	@IdeProperty(visible = false)
	public String getPropertyName() {
		return getColumnName();
	}
}
