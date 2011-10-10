package com.bstek.dorado.jdbc.config.xml;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.PropertyParser;
import com.bstek.dorado.core.bean.BeanFactoryUtils;
import com.bstek.dorado.jdbc.model.DbElementTrigger;

public class TriggerParser extends PropertyParser {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		String triggerName = (String) super.doParse(node, context);
		if (StringUtils.isNotEmpty(triggerName)) {
			return (DbElementTrigger)BeanFactoryUtils.getBean(triggerName);
		} else {
			return null;
		}
	}

}
