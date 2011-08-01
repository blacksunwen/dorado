package com.bstek.dorado.view.config.xml;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-5
 */
public class ComponentPropertyParser extends PropertyParser {
	private XmlParser componentParser;

	public void setComponentParser(XmlParser componentParser) {
		this.componentParser = componentParser;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		if (node instanceof Element) {
			Element element = (Element) node;
			List<Element> childElements = DomUtils.getChildElements(element);
			if (childElements.size() == 1) {
				return componentParser.parse(childElements.get(0), context);
			}
		}
		return super.doParse(node, context);
	}
}
