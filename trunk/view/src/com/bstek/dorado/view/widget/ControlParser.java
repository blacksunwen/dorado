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
