/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
