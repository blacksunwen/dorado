package com.bstek.dorado.idesupport.template;

import com.bstek.dorado.config.xml.XmlParser;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-24
 */
public class AutoChildTemplate extends ChildTemplate {
	private XmlParser xmlParser;

	public AutoChildTemplate(String name, XmlParser xmlParser) {
		super(name);
		this.xmlParser = xmlParser;
	}

	public XmlParser getXmlParser() {
		return xmlParser;
	}
}
