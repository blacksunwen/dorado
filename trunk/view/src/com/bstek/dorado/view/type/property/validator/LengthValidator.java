package com.bstek.dorado.view.type.property.validator;

import java.text.MessageFormat;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
@XmlNode(fixedProperties = "type=length")
@ClientObject(prototype = "dorado.validator.LengthValidator",
		shortTypeName = "Length")
public class LengthValidator extends BaseValidator {

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	private int minLength = -1;
	private int maxLength = -1;

	@Override
	protected Object doValidate(Object value) throws Exception {
		if (value instanceof String) {
			int len = ((String) value).length();
			if (minLength > 0 && len < minLength) {
				return MessageFormat.format(
						"Text too short, it should be longer than {0}.",
						minLength);
			}
			if (maxLength > 0 && len > maxLength) {
				return MessageFormat.format(
						"Text too long, it should be shorter than {0}.",
						maxLength);
			}
		}
		return null;
	}
}
