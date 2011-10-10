package com.bstek.dorado.jdbc.sql;

import com.bstek.dorado.jdbc.model.DbElement;

public interface SqlGenerator<T> {

	DbElement.Type getType();
	
	SelectSql selectSql(T t, Object parameter);
}
