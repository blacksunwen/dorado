package com.bstek.dorado.data.config.xml;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.data.config.definition.ListenableObjectDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-25
 */
public abstract class DataObjectParser extends GenericObjectParser {

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		super.initDefinition(definition, element, context);

		ListenableObjectDefinition dof = (ListenableObjectDefinition) definition;
		String listener = (String) dof.getProperties().remove(
				XmlConstants.ATTRIBUTE_LISTENER);
		if (StringUtils.isNotEmpty(listener)) {
			dof.setListener(listener);
		}
	}

}