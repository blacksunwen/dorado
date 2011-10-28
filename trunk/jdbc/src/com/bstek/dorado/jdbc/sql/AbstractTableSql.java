package com.bstek.dorado.jdbc.sql;

public abstract class AbstractTableSql extends AbstractSql {
	private String tableToken;
	private boolean retrieveAfterExecute = false;
	
	public String getTableToken() {
		return tableToken;
	}

	public void setTableToken(String tableToken) {
		this.tableToken = tableToken;
	}

	public boolean isRetrieveAfterExecute() {
		return retrieveAfterExecute;
	}

	public void setRetrieveAfterExecute(boolean retrieveAfterExecute) {
		this.retrieveAfterExecute = retrieveAfterExecute;
	}

}
