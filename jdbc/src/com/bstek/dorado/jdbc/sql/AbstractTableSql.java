package com.bstek.dorado.jdbc.sql;

public abstract class AbstractTableSql extends AbstractSql {
	private String tableToken;
	
	public String getTableToken() {
		return tableToken;
	}

	public void setTableToken(String tableToken) {
		this.tableToken = tableToken;
	}
}
