package com.bstek.dorado.config.xml;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ConfigUtils;
import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.util.clazz.ClassUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-19
 */
public class ClassTypePropertyParser extends StaticPropertyParser {

	private boolean failSafe;

	public void setFailSafe(boolean failSafe) {
		this.failSafe = failSafe;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Object value = super.doParse(node, context);
		if (value == null)
			return null;

		if (value instanceof String) {
			try {
				return ClassUtils.forName((String) value);
			} catch (ClassNotFoundException e) {
				if (failSafe) {
					return ConfigUtils.IGNORE_VALUE;
				} else {
					throw e;
				}
			}
		} else if (value instanceof Class<?>) {
			return value;
		} else {
			throw new XmlParseException("Class type expected.", node, context);
		}
	}
}
