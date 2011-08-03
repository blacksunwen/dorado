package com.bstek.dorado.hibernate.criteria.criterion;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;

public class SubQueryNoValueCriterionOpParser extends PropertyParser {
	
	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		if (node instanceof Attr) {
			Attr attr = (Attr) node;
			String value = attr.getValue();
			if (StringUtils.isNotEmpty(value)) {
				return SubQueryNoValueCriterion.OP.value(value);
			}
		}
		return null;
	}
}
