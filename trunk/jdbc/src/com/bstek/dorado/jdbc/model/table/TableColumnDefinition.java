package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.model.ColumnDefinition;

public class TableColumnDefinition extends ColumnDefinition {

	public TableColumnDefinition() {
		super();
		this.setDefaultImpl(TableColumn.class.getName());
	}
}
