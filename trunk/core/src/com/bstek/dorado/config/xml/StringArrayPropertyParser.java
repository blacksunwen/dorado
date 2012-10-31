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
package com.bstek.dorado.config.xml;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-19
 */
public class StringArrayPropertyParser extends StaticPropertyParser {

	private String seperator = ",";

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Object value = super.doParse(node, context);
		if (value == null)
			return null;

		if (value instanceof String) {
			String text = (String) value;
			if (!StringUtils.isEmpty(text)) {
				return StringUtils.split(text, seperator);
			} else {
				return null;
			}
		} else if (value instanceof String[]) {
			return value;
		} else {
			throw new XmlParseException("String array expected.", node, context);
		}
	}
}
