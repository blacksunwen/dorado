package com.bstek.dorado.idesupport.initializer;


import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.idesupport.template.RuleTemplate;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-28
 */
public class PropertyDefRuleTemplateInitializer extends
		CommonRuleTemplateInitializer {

	@Override
	protected void addChildBySubParser(RuleTemplate ruleTemplate,
			InitializerContext initializerContext, String nodeName,
			XmlParser subParser, XmlSubNode xmlSubNode, Class<?> propertyType,
			boolean aggregated) throws Exception {
		if ("Validator".equals(nodeName)) return;
		super.addChildBySubParser(ruleTemplate, initializerContext, nodeName,
				subParser, xmlSubNode, propertyType, aggregated);
	}

}
