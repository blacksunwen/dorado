package com.bstek.dorado.data.type.property;

/**
 * 基本属性。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 17, 2007
 */
public class BasePropertyDef extends PropertyDefSupport {
	private String propertyPath;

	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}
}