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
package com.bstek.dorado.idesupport.parse;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.idesupport.model.ClientEvent;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-19
 */
public class ClientEventParser extends ConfigurableDispatchableXmlParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element) node;
		ClientEvent event = new ClientEvent();
		Map<String, Object> properties = this.parseProperties(element, context);
		BeanUtils.copyProperties(event, properties);
		return event;
	}

}
