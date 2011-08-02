package com.bstek.dorado.idesupport.initializer;

import com.bstek.dorado.config.xml.DispatchableXmlParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.type.manager.DataTypeManager;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.view.output.ObjectOutputter;
import com.bstek.dorado.view.type.PropertyDefsOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-26
 */
public class DataTypeRuleTemplateInitializer extends
		CommonRuleTemplateInitializer {

	@Override
	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		DataTypeManager dataTypeManager = (DataTypeManager) Context
				.getCurrent().getServiceBean("dataTypeManager");
		ruleTemplate.setType(dataTypeManager.getDataType("Bean").getClass()
				.getName());

		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();
		DispatchableXmlParser parser = (DispatchableXmlParser) ruleTemplate
				.getParser();
		PropertyDefsOutputter outputter = null;
		if (ruleTemplate.getOutputter() instanceof ObjectOutputter) {
			outputter = (PropertyDefsOutputter) ((ObjectOutputter) ruleTemplate
					.getOutputter()).getConfigProperties().get("propertyDefs");
		}

		RuleTemplate subRuleTemplate;
		ChildTemplate childTemplate;

		subRuleTemplate = ruleTemplateManager.getRuleTemplate("PropertyDef");
		subRuleTemplate.setParser(parser.getSubParsers().get("PropertyDef"));
		if (outputter != null) {
			subRuleTemplate.setOutputter(outputter
					.getBasePropertyDefOutputter());
		}
		childTemplate = new ChildTemplate(subRuleTemplate.getName(),
				subRuleTemplate);
		childTemplate.setAggregated(true);
		ruleTemplate.addChild(childTemplate);

		subRuleTemplate = ruleTemplateManager.getRuleTemplate("Reference");
		subRuleTemplate.setParser(parser.getSubParsers().get("Reference"));
		if (outputter != null) {
			subRuleTemplate.setOutputter(outputter.getReferenceOutputter());
		}
		childTemplate = new ChildTemplate(subRuleTemplate.getName(),
				subRuleTemplate);
		childTemplate.setAggregated(true);
		ruleTemplate.addChild(childTemplate);

		subRuleTemplate = ruleTemplateManager.getRuleTemplate("Lookup");
		subRuleTemplate.setParser(parser.getSubParsers().get("Lookup"));
		if (outputter != null) {
			subRuleTemplate.setOutputter(outputter.getLookupOutputter());
		}
		childTemplate = new ChildTemplate(subRuleTemplate.getName(),
				subRuleTemplate);
		childTemplate.setAggregated(true);
		ruleTemplate.addChild(childTemplate);

		super.initRuleTemplate(ruleTemplate, initializerContext);
	}

	@Override
	protected void initChildren(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		// do nothing
	}
}
