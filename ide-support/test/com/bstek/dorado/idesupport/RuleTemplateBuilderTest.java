package com.bstek.dorado.idesupport;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.idesupport.RuleTemplateBuilder;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.template.ChildTemplate;
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
				.getRuleTemplate("DataProvider");
		assertNotNull(dataProviderTemplate);

		assertEquals(2, dataProviderTemplate.getChildren().size());

		ChildTemplate[] children = dataProviderTemplate.getChildren().values()
				.toArray(new ChildTemplate[0]);
		ChildTemplate childTemplate = children[0];
		assertNotNull(childTemplate);

		RuleTemplate ruleTemplate = childTemplate.getRuleTemplate();
		assertNotNull(ruleTemplate);
		assertEquals("ParameterDataType", ruleTemplate.getNodeName());

		RuleTemplate[] parents = ruleTemplate.getParents();
		assertEquals(1, parents.length);
		assertSame(ruleTemplateManager.getRuleTemplate("DataType"), parents[0]);

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
