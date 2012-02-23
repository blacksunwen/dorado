package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.config.ColumnDefinition;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableKeyColumnDefinition extends ColumnDefinition {

	public TableKeyColumnDefinition() {
		super();
		this.setImpl(TableKeyColumn.class.getName());
	}
}
