package com.bstek.dorado.idesupport.initializer;

import com.bstek.dorado.idesupport.template.RuleTemplate;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-20
 */
public interface RuleTemplateInitializer {
	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception;
}
