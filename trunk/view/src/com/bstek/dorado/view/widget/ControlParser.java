/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.widget;

import java.util.Map;

import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.view.config.definition.ControlDefinition;
import com.bstek.dorado.view.config.xml.ViewXmlConstants;

/**
 * 控件的解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 29, 2008
 */
public class ControlParser extends ComponentParser {

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		super.initDefinition(definition, element, context);

		ControlDefinition controlDefinition = (ControlDefinition) definition;
		Map<String, Object> properties = controlDefinition.getProperties();
		if (properties
				.containsKey(ViewXmlConstants.ATTRIBUTE_LAYOUT_CONSTRAINT)) {
			Object layoutConstraint = properties
					.remove(ViewXmlConstants.ATTRIBUTE_LAYOUT_CONSTRAINT);
			controlDefinition.setLayoutConstraint(layoutConstraint);
		}
	}

}
