package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.jdbc.model.table.TableColumn;

public class SqlTableColumn extends TableColumn {
	
	private String resolveColumnName;

	public String getResolveColumnName() {
		return resolveColumnName;
	}

	public void setResolveColumnName(String resolveColumnName) {
		this.resolveColumnName = resolveColumnName;
	}
	
}
