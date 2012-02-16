package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.config.ColumnDefinition;

public class TableKeyColumnDefinition extends ColumnDefinition {

	public TableKeyColumnDefinition() {
		super();
		this.setImpl(TableKeyColumn.class.getName());
	}
}
