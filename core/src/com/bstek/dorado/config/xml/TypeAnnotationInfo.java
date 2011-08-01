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
