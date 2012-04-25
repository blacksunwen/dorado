package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.model.table.KeyGenerator;
/**
 * 主键生成器的管理者
 * @author mark.li@bstek.com
 *
 */
public interface KeyGeneratorManager {
	
	@SuppressWarnings("rawtypes")
	void register(KeyGenerator keyGenerator);
	
	KeyGenerator<Object>[] list();
	
	<T>KeyGenerator<T> get(String name);
	
	boolean has(String name);
}
