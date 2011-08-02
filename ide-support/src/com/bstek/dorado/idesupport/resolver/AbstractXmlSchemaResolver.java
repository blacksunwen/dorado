/**
 * 
 */
package com.bstek.dorado.idesupport.resolver;

import com.bstek.dorado.idesupport.RuleSetBuilder;
import com.bstek.dorado.idesupport.RuleTemplateBuilder;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.resolver.AbstractTextualResolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-3-31
 */
public abstract class AbstractXmlSchemaResolver extends AbstractTextualResolver {

	protected RuleSet getRuleSet(DoradoContext context) throws Exception {
		RuleTemplateBuilder ruleTemplateBuilder = (RuleTemplateBuilder) context
				.getServiceBean("idesupport.ruleTemplateBuilder");
		RuleSetBuilder ruleSetBuilder = (RuleSetBuilder) context
				.getServiceBean("idesupport.ruleSetBuilder");

		RuleTemplateManager ruleTemplateManager = ruleTemplateBuilder
				.getRuleTemplateManager();
		RuleSet ruleSet = ruleSetBuilder.buildRuleSet(ruleTemplateManager);
		return ruleSet;
	}
}
