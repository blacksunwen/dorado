package com.bstek.dorado.view.widget;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.view.config.definition.ComponentDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-1
 */
public class ChildComponentParser extends ConfigurableDispatchableXmlParser {

	private XmlParser componentParser;

	public void setComponentParser(XmlParser componentParser) {
		this.componentParser = componentParser;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		ComponentDefinition component = (ComponentDefinition) componentParser
				.parse(node, context);
		return component;
	}
}
