package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.TextPropertyParser;
import com.bstek.dorado.view.config.xml.ViewXmlConstants;

/**
 * 默认的布局条件的解析分派器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 17, 2008
 */
public class DefaultLayoutConstraintParserDispatcher extends TextPropertyParser {
	@Override
	protected Object parseText(String text, ParseContext context)
			throws Exception {
		if (ViewXmlConstants.NON_LAYOUT_CONSTRAINT.equals(text)) {
			return Layout.NON_LAYOUT_CONSTRAINT;
		}
		else {
			return super.parseText(text, context);
		}
	}
}
