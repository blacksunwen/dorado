package com.bstek.dorado.view.config.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-8
 */
public class ContextParser extends ConfigurableDispatchableXmlParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Map<String, Object> viewContext = new HashMap<String, Object>();
		for (Element element : DomUtils.getChildrenByTagName((Element) node,
				ViewXmlConstants.ATTRIBUTE)) {
			String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
			Map<String, Object> properties = parseProperties(element, context);
			Object value = properties.get(XmlConstants.ATTRIBUTE_VALUE);
			viewContext.put(name, value);
		}
		return viewContext;
	}

}