package com.bstek.dorado.jdbc.model.table;

import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.jdbc.KeyGeneratorManager;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractKeyGenerator<T> implements KeyGenerator<T>, InitializingBean {

	private String name;

	private KeyGeneratorManager manager;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public KeyGeneratorManager getManager() {
		return manager;
	}

	public void setManager(KeyGeneratorManager manager) {
		this.manager = manager;
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.manager.register(this);
	}

	@Override
	public boolean isIdentity() {
		return false;
	}

}
