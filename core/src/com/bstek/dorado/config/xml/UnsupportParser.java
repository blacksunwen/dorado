package com.bstek.dorado.config.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;

/**
 * 不做任何实际处理的空属性解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 25, 2008
 */
public class UnsupportParser implements XmlParser {

	public Object parse(Node node, ParseContext context) throws Exception {
		String propertyName;
		if (node instanceof Element) {
			propertyName = ((Element) node)
					.getAttribute(XmlConstants.ATTRIBUTE_NAME);
		} else {
			propertyName = node.getNodeName();
		}
		throw new XmlParseException("Property \"" + propertyName
				+ "\" Unsupported.", node, context);
	}
}
