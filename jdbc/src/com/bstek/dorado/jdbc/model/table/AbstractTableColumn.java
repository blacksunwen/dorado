package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.model.Column;

public abstract class AbstractTableColumn extends Column {

	@Override
	public String getKeyName() {
		return getColumnName();
	}

}
