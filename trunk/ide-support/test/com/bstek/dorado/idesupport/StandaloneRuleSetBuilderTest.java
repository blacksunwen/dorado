package com.bstek.dorado.idesupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Set;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.idesupport.model.Child;
import com.bstek.dorado.idesupport.model.Property;
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
		// File file = File.createTempFile("rules", "xml");
		File file = new File("e:/temp/rule.xml");
		getRuleSetOutputter().output(new FileWriter(file), ruleTemplateManager);
		return file.getAbsolutePath();
	}

	public void test() throws Exception {
		String path = outputTemplateToFile();

		InputStream in = new FileInputStream(path);
		RuleSet ruleSet = StandaloneRuleSetBuilder.getRuleSet(in);
		assertNotNull(ruleSet);
		assertFalse(ruleSet.getRuleMap().isEmpty());

		Rule modelRule = ruleSet.getRule("Model");
		assertNotNull(modelRule);

		Child dataTypeChild = modelRule.getChild("DataType");
		assertNotNull(dataTypeChild);

		Rule dataTypeRule = ruleSet.getRule("DataType");
		assertNotNull(dataTypeRule);
		assertEquals("DataType", dataTypeRule.getNodeName());
		assertFalse(dataTypeRule.isAbstract());

		Child propertyDef = dataTypeRule.getChild("PropertyDef");
		assertNotNull(propertyDef);

		Rule referenceRule = ruleSet.getRule("Reference");
		assertEquals("pojo", referenceRule.getProperty("parameter").getEditor());

		Set<Rule> concreteRules = propertyDef.getConcreteRules();
		assertNotNull(concreteRules);

		Property primitiveProperty = dataTypeRule.getPrimitiveProperty("impl");
		assertNotNull(primitiveProperty);

		Rule directDataProviderRule = ruleSet.getRule("DirectDataProvider");
		assertNotNull(directDataProviderRule);
		assertEquals("DataProvider", directDataProviderRule.getNodeName());
		assertEquals(null, directDataProviderRule.getLabel());
		assertFalse(directDataProviderRule.isAbstract());

		primitiveProperty = directDataProviderRule.getPrimitiveProperty("impl");
		assertNotNull(primitiveProperty);

		Rule[] subRules = dataTypeChild.getRule().getSubRules();
		assertNotNull(subRules);

		Rule viewRule = ruleSet.getRule("View");
		assertNotNull(viewRule);

		Rule defaultViewRule = ruleSet.getRule("DefaultView");
		assertNotNull(defaultViewRule);

		assertNotNull(ruleSet.getRule("AnchorLayout"));
		assertNotNull(ruleSet.getRule("LayoutHolder"));
		assertNotNull(ruleSet.getRule("DataSet"));
		assertNotNull(ruleSet.getRule("UpdateAction"));

		Rule componentRule = ruleSet.getRule("Component");
		Rule controlRule = ruleSet.getRule("Control_1");
		Rule containerRule = ruleSet.getRule("Container");
		Rule datasetRule = ruleSet.getRule("DataSet");
		Rule panelRule = ruleSet.getRule("Panel");
		Rule buttonRule = ruleSet.getRule("Button");
		Rule autoFormRule = ruleSet.getRule("AutoForm");

		assertTrue(datasetRule.isSubRuleOf(componentRule));
		assertTrue(controlRule.isSubRuleOf(componentRule));
		assertTrue(containerRule.isSubRuleOf(componentRule));
		assertTrue(buttonRule.isSubRuleOf(componentRule));
		assertTrue(buttonRule.isSubRuleOf(controlRule));
		assertTrue(autoFormRule.isSubRuleOf(controlRule));

		assertFalse(datasetRule.isSubRuleOf(controlRule));
		assertFalse(componentRule.isSubRuleOf(controlRule));
		
		assertEquals("Button", buttonRule.getNodeName());

		Set<Rule> componentRules = containerRule.getChild("Children")
				.getConcreteRules();
		assertFalse(componentRules.contains(defaultViewRule));

		Rule label1Rule = ruleSet.getRule("Label_1");
		assertNotNull(label1Rule);
		assertEquals(Integer.MAX_VALUE, label1Rule.getSortFactor());

		Rule toolBarButtonRule = ruleSet.getRule("Button_1");
		assertNotNull(toolBarButtonRule);
		assertEquals("ToolBar", toolBarButtonRule.getCategory());
		assertFalse(componentRules.contains(toolBarButtonRule));

		assertNotNull(viewRule.getPrimitiveProperty("listener"));
		assertNotNull(datasetRule.getPrimitiveProperty("id"));
		assertNotNull(panelRule.getPrimitiveProperty("id"));

		Child child = autoFormRule.getChild("AutoFormElement");
		assertNotNull(child);

	}
}
