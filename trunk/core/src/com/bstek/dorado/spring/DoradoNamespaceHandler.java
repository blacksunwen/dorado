package com.bstek.dorado.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author Frank.Zhang (mailto:frank.zhang@bstek.com), Benny Bao
 *         (mailto:benny.bao@bstek.com)
 * @since Jun 25, 2009
 */
public class DoradoNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionDecorator("property-parser",
				new MapEntryShortCutDecorator("propertyParsers"));
		registerBeanDefinitionDecorator("sub-parser",
				new MapEntryShortCutDecorator("subParsers"));
		registerBeanDefinitionDecorator("attribute-parser",
				new MapEntryShortCutDecorator("attributeParsers"));
		registerBeanDefinitionDecorator("property-outputter",
				new MapEntryShortCutDecorator("propertieConfigs"));
		registerBeanDefinitionDecorator("virtual-property",
				new VirtualPropertyDecorator());
		registerBeanDefinitionDecorator("virtual-event",
				new VirtualEventDecorator());

		registerBeanDefinitionParser("import-dorado",
				new ImportDoradoElementParser());
	}

}
