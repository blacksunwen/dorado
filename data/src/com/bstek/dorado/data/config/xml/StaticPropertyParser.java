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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.util.xml.DomUtils;

/**
 * 静态属性节点的解析器。<br>
 * 静态属性是指不支持动态EL表达式的属性，如果该属性中定义了EL表达式，那么该表达式将在解析的过程中立即完成求值过程。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 18, 2007
 */
public class StaticPropertyParser extends GenericParser {

	@Override
	protected Object internalParse(Node node, DataParseContext context)
			throws Exception {
		Object value = null;
		if (node instanceof Element) {
			value = DomUtils.getTextContent((Element) node);
		} else {
			value = node.getNodeValue();
		}
		Expression expression = getExpressionHandler().compile((String) value);
		if (expression != null) {
			value = expression.evaluate();
		}
		return value;
	}
}
