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
package com.bstek.dorado.vidorsupport.vidor;

import com.bstek.dorado.config.text.TextParseContext;
import com.bstek.dorado.config.text.TextParser;

class StringTextParser implements TextParser {

	//com.bstek.dorado.config.text.TextAttributeParser#parse
	@Override
	public Object parse(char[] charArray, TextParseContext context)
			throws Exception {
		final int BEFORE_ATTRIBUTE_VALUE = 1;
		final int IN_ATTRIBUTE_VALUE = 2;
		final int AFTER_ATTRIBUTE_VALUE = 3;
		int status = BEFORE_ATTRIBUTE_VALUE;

		StringBuffer valueStack = new StringBuffer();
		for (int currentIndex = context.getCurrentIndex(); currentIndex < charArray.length
				&& status != AFTER_ATTRIBUTE_VALUE; currentIndex++) {
			char c = charArray[currentIndex];
			context.setCurrentIndex(currentIndex);

			switch (status) {
			case BEFORE_ATTRIBUTE_VALUE:
				if (isIgnoredChar(c)) {
					continue;
				} else if (c == ';') {
					status = AFTER_ATTRIBUTE_VALUE;
				} else {
					valueStack.append(c);
					status = IN_ATTRIBUTE_VALUE;
				}
				break;
			case IN_ATTRIBUTE_VALUE:
				if (c == ';') {
					status = AFTER_ATTRIBUTE_VALUE;
				} else {
					valueStack.append(c);
				}
				break;
			}
		}

		if (status != AFTER_ATTRIBUTE_VALUE) {
			context.increaseCurrentIndex();
		}

		String valueText = valueStack.toString();
		return valueText;
	}

	private boolean isIgnoredChar(char c) {
		return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
	}
}
