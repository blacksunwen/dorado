package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.table.KeyGenerator;

public interface KeyGeneratorManager {
	
	@SuppressWarnings("rawtypes")
	void register(KeyGenerator keyGenerator);
	
	KeyGenerator<Object>[] list();
	
	<T>KeyGenerator<T> get(String name);
	
	boolean has(String name);
}
