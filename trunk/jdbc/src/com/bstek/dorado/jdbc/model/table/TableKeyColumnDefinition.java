package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.model.ColumnDefinition;

public class TableKeyColumnDefinition extends ColumnDefinition {

	public TableKeyColumnDefinition() {
		super();
		this.setDefaultImpl(TableKeyColumn.class.getName());
	}
}
