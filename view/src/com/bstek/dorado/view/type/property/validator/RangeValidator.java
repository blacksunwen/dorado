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

package com.bstek.dorado.view.type.property.validator;

import java.text.MessageFormat;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
@XmlNode(fixedProperties = "type=range")
@ClientObject(prototype = "dorado.validator.RangeValidator",
		shortTypeName = "Range")
public class RangeValidator extends BaseValidator {
	private float minValue;
	private RangeValidateMode minValueValidateMode = RangeValidateMode.ignore;
	private float maxValue;
	private RangeValidateMode maxValueValidateMode = RangeValidateMode.ignore;

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public RangeValidateMode getMinValueValidateMode() {
		return minValueValidateMode;
	}

	public void setMinValueValidateMode(RangeValidateMode minValueValidateMode) {
		this.minValueValidateMode = minValueValidateMode;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public RangeValidateMode getMaxValueValidateMode() {
		return maxValueValidateMode;
	}

	public void setMaxValueValidateMode(RangeValidateMode maxValueValidateMode) {
		this.maxValueValidateMode = maxValueValidateMode;
	}

	@Override
	protected Object doValidate(Object value) throws Exception {
		if (!(value instanceof Number)) {
			return null;
		}

		float f = ((Number) value).floatValue();
		boolean invalid = false;
		if (minValueValidateMode != RangeValidateMode.ignore) {
			String subMessage = "";
			if (f == minValue
					&& minValueValidateMode != RangeValidateMode.allowEquals) {
				invalid = true;
				subMessage = " or equals to";
			}
			if (f < minValue) {
				invalid = true;
			}
			if (invalid) {
				return MessageFormat.format(
						"Number too less, it should be greater than{0} {1}.",
						subMessage, minValue);
			}
		}
		if (maxValueValidateMode != RangeValidateMode.ignore) {
			String subMessage = "";
			if (f == maxValue
					&& maxValueValidateMode != RangeValidateMode.allowEquals) {
				invalid = true;
				subMessage = " or equals to";
			}
			if (f > maxValue) {
				invalid = true;
			}
			if (invalid) {
				return MessageFormat.format(
						"Number too great, it should be less than{0} {1}.",
						subMessage, maxValue);
			}
		}
		return null;
	}

}
