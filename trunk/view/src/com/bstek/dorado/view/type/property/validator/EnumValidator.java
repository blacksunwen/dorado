package com.bstek.dorado.view.type.property.validator;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
@XmlNode(fixedProperties = "type=enum")
@ClientObject(prototype = "dorado.validator.EnumValidator",
		shortTypeName = "Enum")
public class EnumValidator extends BaseValidator {

	private List<?> enumValues;

	@ClientProperty
	public List<?> getEnumValues() {
		return enumValues;
	}

	public void setEnumValues(List<?> enumValues) {
		this.enumValues = enumValues;
	}

	@Override
	protected Object doValidate(Object value) throws Exception {
		if (enumValues == null || enumValues.isEmpty() || value == null) {
			return null;
		}
		if (enumValues.indexOf(value) < 0) {
			return "Value out of enum range.";
		}
		return null;
	}
}
