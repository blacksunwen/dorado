package com.bstek.dorado.jdbc.model.table;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractKeyGenerator<T> implements KeyGenerator<T> {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isIdentity() {
		return false;
	}

}
