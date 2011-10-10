package com.bstek.dorado.jdbc.key;

import com.bstek.dorado.jdbc.JdbcQueryContext;
import com.bstek.dorado.jdbc.model.table.TableKeyColumn;

public interface KeyGenerator<T> {
	
	String getName();

	T newKey(JdbcQueryContext context, TableKeyColumn keyColumn);
}
