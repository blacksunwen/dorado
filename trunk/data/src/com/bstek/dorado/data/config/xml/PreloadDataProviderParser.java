/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.data.config.xml;

import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.ConfigurableDispatchableXmlParser;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParseException;

/**
 * DataProvider的预解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 21, 2007
 */
public class PreloadDataProviderParser extends
		ConfigurableDispatchableXmlParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		DataParseContext dataContext = (DataParseContext) context;
		Element element = ((Element) node);

		String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
		if (StringUtils.isEmpty(name)) {
			throw new XmlParseException("[" + XmlConstants.ATTRIBUTE_NAME
					+ "] attribute of [" + DataXmlConstants.DATA_PROVIDER
					+ "] can not be empty - [" + dataContext.getResource()
					+ "]", element, context);
		}

		Map<String, NodeWrapper> configuredDataProviders = dataContext
				.getConfiguredDataProviders();
		if (configuredDataProviders.containsKey(name)) {
			boolean overwrite = BooleanUtils.toBoolean(element
					.getAttribute(DataXmlConstants.ATTRIBUTE_OVERWRITE));
			if (!overwrite) {
				throw new XmlParseException(DataXmlConstants.DATA_PROVIDER
						+ " [" + name + "] is not unique!", element, context);
			}
		}

		configuredDataProviders.put(name,
				new NodeWrapper(node, context.getResource()));
		return null;
	}
}
