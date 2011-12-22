package com.bstek.dorado.view.type.property.validator;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-8-12
 */
@XmlNode(fixedProperties = "type=custom")
@ClientObject(prototype = "dorado.validator.CustomValidator",
		shortTypeName = "Custom")
@ClientEvents(@ClientEvent(name = "onValidate"))
public class CustomValidator extends AbstractValidator {

	@Override
	protected Object doValidate(Object value) throws Exception {
		return null;
	}

}
