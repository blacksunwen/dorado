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

import com.bstek.dorado.config.xml.XmlParser;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-26
 */
public class TypeAnnotationInfo {
	private String nodeName;
	private XmlParser parser;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public XmlParser getParser() {
		return parser;
	}

	public void setParser(XmlParser parser) {
		this.parser = parser;
	}
}
