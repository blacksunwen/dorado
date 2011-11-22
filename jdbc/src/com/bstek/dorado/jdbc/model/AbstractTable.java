package com.bstek.dorado.jdbc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.util.Assert;

public abstract class AbstractTable extends AbstractDbElement implements DbTable {

	private Map<String,Column> columnMap = new LinkedHashMap<String,Column>();
	private TableTrigger trigger;
	
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

	public TableTrigger getTrigger() {
		return trigger;
	}
	
	public void setTrigger(TableTrigger trigger) {
		this.trigger = trigger;
	}
	
}
