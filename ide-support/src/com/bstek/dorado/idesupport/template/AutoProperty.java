package com.bstek.dorado.idesupport.template;

import java.lang.reflect.Method;

import com.bstek.dorado.config.Parser;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-24
 */
public class AutoProperty extends PropertyTemplate {
	private Method annotationMethod;
	private Parser propertyParser;

	public AutoProperty(Method annotationMethod, Parser propertyParser) {
		this.annotationMethod = annotationMethod;
		this.propertyParser = propertyParser;
	}

	public Method getAnnotationMethod() {
		return annotationMethod;
	}

	public Parser getPropertyParser() {
		return propertyParser;
	}
}
