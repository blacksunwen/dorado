package com.bstek.dorado.jdbc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.util.Assert;

public abstract class AbstractDbElement implements DbElement {

	private String name;
	
	private JdbcEnviroment env;
	
	private DbElementTrigger trigger;
	
	private Map<String,Column> columnMap = new LinkedHashMap<String,Column>();
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@ViewAttribute(visible=false)
	@Override
	public JdbcEnviroment getJdbcEnviroment() {
		if (env != null) {
			return env;
		} else {
			return JdbcUtils.getEnviromentManager().getDefault();
		}
	}

	@Override
	public void setJdbcEnviroment(JdbcEnviroment env) {
		this.env = env;
	}

	public DbElementTrigger getTrigger() {
		return trigger;
	}
	
	public void setTrigger(DbElementTrigger trigger) {
		this.trigger = trigger;
	}
	
	public List<Column> getAllColumns() {
		return new ArrayList<Column>(columnMap.values());
	}
	
	public Column getColumn(String name) {
		Column c = columnMap.get(name);
		Assert.notNull(c, getType() + "named [" + getName() + "]" + " has not column named [" + name + "]");
		return c;
	}
	
	public void addColumn(Column column) {
		String key = this.getColumnKey(column);
		if (columnMap.containsKey(key)) {
			throw new IllegalArgumentException("Duplicate column named [" + key + "]");
		}
		columnMap.put(key, column);
	}
	
	protected String getColumnKey(Column column) {
		return column.getColumnName();
	}
}
