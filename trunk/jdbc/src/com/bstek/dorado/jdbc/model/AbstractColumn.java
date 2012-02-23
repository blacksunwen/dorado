package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * 抽象的JDBC列对象
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractColumn {
	
	private String columnName;

	private JdbcType jdbcType;
	
	private boolean selectable = true;

	@XmlProperty(attributeOnly=true)
	@IdeProperty(highlight=1)
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	@XmlProperty(parser="spring:dorado.jdbc.jdbcTypeParser", attributeOnly=true)
	@IdeProperty(highlight=1, editor = "jdbc:list-jdbctype")
	public JdbcType getJdbcType() {
		return jdbcType;
	}
	
	public void setJdbcType(JdbcType jdbcType) {
		this.jdbcType = jdbcType;
	}
	
	@ClientProperty(escapeValue = "true")
	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	@IdeProperty(visible = false)
	public String getPropertyName() {
		return getColumnName();
	}
}
