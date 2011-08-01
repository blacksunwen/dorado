package com.bstek.dorado.web.loader;

import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.util.Assert;

/**
 * 资源样式的解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 24, 2008
 */
public class PackagesConfigPatternParser extends
		ConfigurableDispatchableXmlParser {

	@Override
	@SuppressWarnings("unchecked")
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element) node;
		String name = element.getAttribute("name");
		Assert.notEmpty(name);
		Pattern pattern = new Pattern(name);

		Map<String, Object> properties = parseProperties(element, context);
		((Map<String, Object>) new BeanMap(pattern)).putAll(properties);
		return pattern;
	}
}
