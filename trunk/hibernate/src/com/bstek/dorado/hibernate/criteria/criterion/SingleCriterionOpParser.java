package com.bstek.dorado.hibernate.criteria.criterion;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;

public class SingleCriterionOpParser extends PropertyParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String value = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(value)) {
			return SingleCriterion.OP.value(value);
		}
		
		return null;
	}
}
