package com.bstek.dorado.hibernate.criteria.criterion;

import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;

public class DoublePropertyCriterionOpParser extends PropertyParser {

	@Override
	protected boolean shouldEvaluateExpression() {
		return true; // 由于特殊解析逻辑的存在，此处已无法支持动态EL表达式
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String operator = (String) super.doParse(node, context);
		return DoublePropertyCriterion.OP.value(operator);
	}
}
