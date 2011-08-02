package com.bstek.dorado.idesupport;

import com.bstek.dorado.idesupport.template.RuleTemplate;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-26
 */
public interface RuleTemplateManagerListener {
	void ruleTemplateAdded(RuleTemplateManager ruleTemplateManager,
			RuleTemplate ruleTemplate) throws Exception;
}
