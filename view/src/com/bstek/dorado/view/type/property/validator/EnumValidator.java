package com.bstek.dorado.view.type.property.validator;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
@ViewObject(prototype = "dorado.validator.EnumValidator", shortTypeName = "Enum")
public class EnumValidator extends BaseValidator {

	private List<?> enumValues;

	@ViewAttribute(outputter = "dorado.dataPropertyOutputter")
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
