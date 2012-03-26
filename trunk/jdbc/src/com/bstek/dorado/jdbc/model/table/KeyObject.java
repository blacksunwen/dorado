package com.bstek.dorado.jdbc.model.table;

import java.util.List;

import com.bstek.dorado.util.Assert;

public class KeyObject {
	private Table table;
	private Object[] keys;
	
	public static final String PARAMETER_KEY = KeyObject.class.getName();
	
	public KeyObject(Table table, Object[] keyValues) {
		List<TableKeyColumn> keyColumnList = table.getKeyColumns();
		Assert.isTrue(keyColumnList.size() == keyValues.length, "the count of keyColumns is [" + keyColumnList.size() + "], " +
				"but the count of values is [" + keyValues.length + "] [" + table.getName() + "]");
		
		this.table = table;
		this.keys = keyValues;
	}

	public Table getTable() {
		return table;
	}

	public Object[] getKeyValues() {
		return keys;
	}
}
