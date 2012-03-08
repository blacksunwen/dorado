package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.data.variant.VariantUtils;

/**
 * {@link com.bstek.dorado.jdbc.model.DbElement}的定义对象
 * 
 * @author mark.li@bstek.com
 *
 */
public class DbElementDefinition extends ObjectDefinition {

	public String getName() {
		return (String)this.getProperties().get("name");
	}
	
	public boolean getVirtualPropertyBoolean(String propertyName, boolean defult) {
		Object value = this.getProperties().get(propertyName);
		this.getProperties().remove(propertyName);
		
		try {
			value = this.getFinalValueOrExpression(value, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (value != null) {
			if (value instanceof Expression) {
				throw new IllegalArgumentException("["+propertyName+"] not support EL expression.");
			}
			return VariantUtils.toBoolean(value);
		} else {
			return defult;
		}
	}
	
}
