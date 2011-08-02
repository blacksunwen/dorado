package com.bstek.dorado.idesupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.Rule;
import com.bstek.dorado.idesupport.model.RuleSet;
import com.bstek.dorado.idesupport.output.RuleSetOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-25
 */
public class StandaloneRuleSetBuilderTest extends IdeSupportContextTestCase {

	private RuleTemplateBuilder getRuleTemplateBuilder() throws Exception {
		Context context = Context.getCurrent();
		return (RuleTemplateBuilder) context
				.getServiceBean("idesupport.ruleTemplateBuilder");
	}

	private RuleSetOutputter getRuleSetOutputter() throws Exception {
		Context context = Context.getCurrent();
		return (RuleSetOutputter) context
				.getServiceBean("idesupport.ruleSetOutputter");
	}

	private String outputTemplateToFile() throws Exception {
		RuleTemplateManager ruleTemplateManager = getRuleTemplateBuilder()
				.getRuleTemplateManager();
		File file = File.createTempFile("rules", "xml");
		getRuleSetOutputter().output(new FileWriter(file), ruleTemplateManager);
		return file.getAbsolutePath();
	}

	public void test() throws Exception {
		String path = outputTemplateToFile();

		InputStream in = new FileInputStream(path);
		RuleSet ruleSet = StandaloneRuleSetBuilder.getRuleSet(in);
		assertNotNull(ruleSet);
		assertFalse(ruleSet.getRuleMap().isEmpty());

		Rule viewRule = ruleSet.getRule("View");
		assertNotNull(viewRule);

		assertNotNull(ruleSet.getRule("AnchorLayout"));
		assertNotNull(ruleSet.getRule("LayoutHolder"));
		assertNotNull(ruleSet.getRule("DataSet"));
		assertNotNull(ruleSet.getRule("UpdateAction"));

		Rule componentRule = ruleSet.getRule("Component");
		Rule controlRule = ruleSet.getRule("Control");
		Rule datasetRule = ruleSet.getRule("DataSet");
		Rule panelRule = ruleSet.getRule("Panel");
		Rule buttonRule = ruleSet.getRule("Button");
		Rule autoFormRule = ruleSet.getRule("AutoForm");

		assertTrue(datasetRule.isSubRuleOf(componentRule));
		assertTrue(controlRule.isSubRuleOf(componentRule));
		assertTrue(buttonRule.isSubRuleOf(componentRule));
		assertTrue(buttonRule.isSubRuleOf(controlRule));
		assertTrue(autoFormRule.isSubRuleOf(controlRule));

		assertFalse(datasetRule.isSubRuleOf(controlRule));
		assertFalse(componentRule.isSubRuleOf(controlRule));

		assertNotNull(viewRule.getPrimitiveProperty("interceptor"));
		assertNotNull(datasetRule.getPrimitiveProperty("id"));
		assertNotNull(panelRule.getPrimitiveProperty("id"));

		Child child = autoFormRule.getChild("AutoFormElement");
		assertNotNull(child);
	}
}
