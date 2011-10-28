package com.bstek.dorado.jdbc.model.sqltable;

import com.bstek.dorado.jdbc.model.table.TableColumn;

public class SqlTableColumn extends TableColumn {
	
	private String nativeColumnName;

	public String getNativeColumnName() {
		return nativeColumnName;
	}

	public void setNativeColumnName(String nativeColumnName) {
		this.nativeColumnName = nativeColumnName;
	}
	
}
