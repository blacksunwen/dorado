package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.jdbc.model.AbstractTable;
import com.bstek.dorado.jdbc.model.Column;
import com.bstek.dorado.jdbc.model.table.Table;

public class SqlTable extends AbstractTable {

	private String querySql;
	
	private Table table;

	public void addColumn(Column column) {
		if (column instanceof SqlTableColumn) {
			super.addColumn(column);
		} else {
			throw new IllegalArgumentException("unknow column class " + column.getClass());
		}
	}
	
	public String getQuerySql() {
		return querySql;
	}

	public Table getTable() {
		return table;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	@Override
	public String getType() {
		return "SqlTable";
	}
	
}