package com.bstek.dorado.idesupport.template;

import java.lang.reflect.Method;

import com.bstek.dorado.annotation.XmlProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-24
 */
public class AutoPropertyTemplate extends PropertyTemplate {
	private Method method;
	private XmlProperty xmlProperty;
	private boolean isPrimitive;

	public AutoPropertyTemplate(String name) {
		super(name);
	}

	public AutoPropertyTemplate(String name, XmlProperty xmlProperty) {
		super(name);
		this.xmlProperty = xmlProperty;
	}

	public AutoPropertyTemplate(String name, Method method,
			XmlProperty xmlProperty) {
		super(name);
		this.method = method;
		this.xmlProperty = xmlProperty;
	}

	public XmlProperty getXmlProperty() {
		return xmlProperty;
	}

	public Method getMethod() {
		return method;
	}

	public boolean isPrimitive() {
		return isPrimitive;
	}

	public void setPrimitive(boolean isPrimitive) {
		this.isPrimitive = isPrimitive;
	}
}
