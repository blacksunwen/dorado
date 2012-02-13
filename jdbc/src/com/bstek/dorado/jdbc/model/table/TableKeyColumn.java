package com.bstek.dorado.jdbc.model.table;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.jdbc.key.KeyGenerator;

@XmlNode(nodeName="KeyColumn", 
		definitionType="com.bstek.dorado.jdbc.model.table.TableKeyColumnDefinition")
public class TableKeyColumn extends AbstractTableColumn {

	private KeyGenerator<?> keyGenerator;

	private Object keyParameter;
	
	private boolean updatable = false;
	
	@XmlProperty(parser="spring:dorado.jdbc.keyGeneratorParser")
	public KeyGenerator<?> getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyGenerator(KeyGenerator<?> keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	@XmlProperty
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
