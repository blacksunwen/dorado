package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.Column;

public class TableKeyColumn extends Column {

	private KeyGenerator<?> keyGenerator;

	private Object keyParameter;
	
	private boolean updatable = false;
	
	public KeyGenerator<?> getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyGenerator(KeyGenerator<?> keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	public Object getKeyParameter() {
		return keyParameter;
	}

	public void setKeyParameter(Object keyParameter) {
		this.keyParameter = keyParameter;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	
}
