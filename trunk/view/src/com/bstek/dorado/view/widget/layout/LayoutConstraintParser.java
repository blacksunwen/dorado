package com.bstek.dorado.view.widget.layout;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.text.ConfigurableDispatchableTextParser;
import com.bstek.dorado.config.text.DispatchableTextParser;
import com.bstek.dorado.config.text.TextParseContext;

/**
 * 视图中布局条件的解析器的抽象类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 30, 2008
 */
public class LayoutConstraintParser extends
		ConfigurableDispatchableTextParser {
	@Override
	public boolean supportsHeader() {
		return true;
	}

	@Override
	public Object parse(char[] charArray, TextParseContext context)
			throws Exception {
		CommonLayoutConstraint constraint = new CommonLayoutConstraint();
		Map<String, Object> attributes = parseToAttributes(charArray, context);

		String type = (String) attributes
				.remove(DispatchableTextParser.HEADER_ATTRIBUTE);
		if (StringUtils.isNotEmpty(type)) {
			attributes.put("type", type);
		}
		constraint.putAll(attributes);
		return constraint;
	}
}
