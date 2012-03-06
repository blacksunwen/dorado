package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.type.JdbcType;

public interface JdbcTypeManager {

	void register(JdbcType jdbcType);
	
	JdbcType[] list();
	
	JdbcType get(String name);
	
	boolean has(String name);
}
