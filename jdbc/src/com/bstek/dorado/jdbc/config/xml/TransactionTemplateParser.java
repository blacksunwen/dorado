package com.bstek.dorado.jdbc.config.xml;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.support.TransactionTemplate;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.core.bean.BeanFactoryUtils;

public class TransactionTemplateParser extends PropertyParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String beanName = (String)super.doParse(node, context);
		if (StringUtils.isNotEmpty(beanName)) {
			return (TransactionTemplate)BeanFactoryUtils.getBean(beanName);
		} else {
			return null;	
		}
	}

}
