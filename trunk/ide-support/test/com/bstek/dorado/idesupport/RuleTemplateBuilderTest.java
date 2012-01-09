package com.bstek.dorado.idesupport;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.idesupport.template.RuleTemplate;

public class RuleTemplateBuilderTest extends IdeSupportContextTestCase {

	protected RuleTemplateBuilder getRuleTemplateBuilder() throws Exception {
		Context context = Context.getCurrent();
		return (RuleTemplateBuilder) context
				.getServiceBean("idesupport.ruleTemplateBuilder");
	}

	public void test() throws Exception {
		RuleTemplateBuilder configRuleTemplateManager = getRuleTemplateBuilder();
		RuleTemplateManager ruleTemplateManager = configRuleTemplateManager
				.getRuleTemplateManager();
		assertNotNull(ruleTemplateManager);

		RuleTemplate dataTypeTemplate = ruleTemplateManager
				.getRuleTemplate("DataType");
		assertNotNull(dataTypeTemplate);
		assertEquals("com.bstek.dorado.data.type.DefaultEntityDataType",
				dataTypeTemplate.getType());

		RuleTemplate dataProviderTemplate = ruleTemplateManager
				.getRuleTemplate("AbstractDataProvider");
		assertNotNull(dataProviderTemplate);

		RuleTemplate layoutHolderTemplate = ruleTemplateManager
				.getRuleTemplate("LayoutHolder");
		assertNotNull(layoutHolderTemplate);

		RuleTemplate updateActionTemplate = ruleTemplateManager
				.getRuleTemplate("UpdateAction");
		assertNotNull(updateActionTemplate);

		RuleTemplate treeTemplate = ruleTemplateManager.getRuleTemplate("Tree");
		assertNotNull(treeTemplate);
	}
}
