package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.jdbc.model.table.TableColumn;

public class SqlTableColumn extends TableColumn {
	
	private String resolveColumn;

	public String getResolveColumn() {
		return resolveColumn;
	}

	public void setResolveColumn(String resolveColumn) {
		this.resolveColumn = resolveColumn;
	}
	
}
