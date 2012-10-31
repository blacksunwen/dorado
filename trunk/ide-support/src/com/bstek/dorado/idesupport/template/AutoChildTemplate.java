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
