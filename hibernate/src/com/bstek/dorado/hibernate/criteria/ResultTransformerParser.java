package com.bstek.dorado.hibernate.criteria;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.springframework.core.Constants;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.core.bean.BeanFactoryUtils;

public class ResultTransformerParser extends PropertyParser {

	private static Constants constants = new Constants(Criteria.class);
	
	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String value = (String) super.doParse(node, context);
		if (StringUtils.isNotEmpty(value)) {
			try {
				return constants.asObject(value);
			} catch (Exception e) {
				return BeanFactoryUtils.getBean(value);
			}
		}
		return null;
	}
}
