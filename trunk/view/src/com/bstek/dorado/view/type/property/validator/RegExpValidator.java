package com.bstek.dorado.view.type.property.validator;

import java.text.MessageFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.ViewObject;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
@ViewObject(prototype = "dorado.validator.RegExpValidator", shortTypeName = "RegExp")
public class RegExpValidator extends BaseValidator {
	private String whiteRegExp;
	private String blackRegExp;
	private RegExpValidatorMode validateMode = RegExpValidatorMode.whiteBlack;

	public String getWhiteRegExp() {
		return whiteRegExp;
	}

	public void setWhiteRegExp(String whiteRegExp) {
		this.whiteRegExp = whiteRegExp;
	}

	public String getBlackRegExp() {
		return blackRegExp;
	}

	public void setBlackRegExp(String blackRegExp) {
		this.blackRegExp = blackRegExp;
	}

	public RegExpValidatorMode getValidateMode() {
		return validateMode;
	}

	public void setValidateMode(RegExpValidatorMode validateMode) {
		this.validateMode = validateMode;
	}

	protected boolean match(String regExp, String s) {
		return Pattern.matches(regExp, s);
	}

	@Override
	protected Object doValidate(Object value) throws Exception {
		if (!(value instanceof String)) {
			return null;
		}

		String s = (String) value;
		if (StringUtils.isEmpty(s)) {
			return null;
		}

		boolean valid = false;
		if (validateMode == RegExpValidatorMode.whiteBlack) {
			valid = StringUtils.isNotEmpty(blackRegExp)
					&& match(whiteRegExp, s)
					|| StringUtils.isNotEmpty(whiteRegExp)
					&& !match(whiteRegExp, s);
		} else {
			valid = StringUtils.isNotEmpty(whiteRegExp)
					&& !match(whiteRegExp, s)
					|| StringUtils.isNotEmpty(blackRegExp)
					&& match(whiteRegExp, s);
		}
		if (valid) {
			return MessageFormat.format("Bad format string \"{0}\".", s);
		}
		return null;
	}

}
