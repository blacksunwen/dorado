package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.config.ColumnDefinition;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableColumnDefinition extends ColumnDefinition {

	public TableColumnDefinition() {
		super();
		this.setImpl(TableColumn.class.getName());
	}
}
