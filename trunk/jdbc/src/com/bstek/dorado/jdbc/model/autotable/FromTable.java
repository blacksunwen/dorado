package com.bstek.dorado.jdbc.model.autotable;

import com.bstek.dorado.jdbc.model.table.Table;

public class FromTable {

	private String tableAlias;
	
	private Table table;
	
	public String getTableAlias() {
		return this.tableAlias;
	}
	
	public Table getTable() {
		return table;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public void setTable(Table table) {
		this.table = table;
	}
	
}
