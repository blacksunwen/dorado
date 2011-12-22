package com.bstek.dorado.idesupport.template;

import com.bstek.dorado.annotation.XmlSubNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-24
 */
public class AutoChildTemplate extends ChildTemplate {
	private XmlSubNode xmlSubNode;

	public AutoChildTemplate(String name, RuleTemplate ruleTemplate,
			XmlSubNode xmlSubNode) {
		super(name, ruleTemplate);
		this.xmlSubNode = xmlSubNode;
	}

	public XmlSubNode getXmlSubNode() {
		return xmlSubNode;
	}
}
