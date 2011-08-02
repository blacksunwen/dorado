package com.bstek.dorado.idesupport.initializer;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.manager.DataProviderTypeRegisterInfo;
import com.bstek.dorado.data.provider.manager.DataProviderTypeRegistry;
import com.bstek.dorado.data.type.validator.ValidatorTypeRegisterInfo;
import com.bstek.dorado.data.type.validator.ValidatorTypeRegistry;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-26
 */
public class ModelRuleTemplateInitializer implements RuleTemplateInitializer {

	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		Context context = Context.getCurrent();
		DataProviderTypeRegistry dataProviderTypeRegistry = (DataProviderTypeRegistry) context
				.getServiceBean("dataProviderTypeRegistry");
		ValidatorTypeRegistry validatorTypeRegistry = (ValidatorTypeRegistry) context
				.getServiceBean("validatorTypeRegistry");

		int sortFactor = 100;
		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();
		RuleTemplate dataProvider = ruleTemplateManager
				.getRuleTemplate("DataProvider");
		for (DataProviderTypeRegisterInfo dataProviderTypeInfo : dataProviderTypeRegistry
				.getTypes()) {
			String providerName = dataProviderTypeInfo.getClassType()
					.getSimpleName();
			RuleTemplate newRuleTemplate = ruleTemplateManager
					.getRuleTemplate(providerName);
			if (newRuleTemplate == null) {
				newRuleTemplate = new RuleTemplate(providerName);
			}
			else {
				newRuleTemplate.setInitialized(false);
			}
			newRuleTemplate.setSortFactor(++sortFactor);
			newRuleTemplate.setScope("public");
			newRuleTemplate.setParents(new RuleTemplate[] { dataProvider });
			newRuleTemplate.setType(dataProviderTypeInfo.getClassType().getName());
			newRuleTemplate.setParser((XmlParser) dataProviderTypeInfo
					.getParser());

			PropertyTemplate property = new PropertyTemplate("type");
			property.setDefaultValue(dataProviderTypeInfo.getType());
			property.setFixed(true);
			property.setVisible(false);
			newRuleTemplate.addPrimitiveProperty(property);

			ruleTemplateManager.addRuleTemplate(newRuleTemplate);
		}

		sortFactor = 200;
		RuleTemplate validator = ruleTemplateManager
				.getRuleTemplate("Validator");
		for (ValidatorTypeRegisterInfo validatorTypeRegisterInfo : validatorTypeRegistry
				.getTypes()) {
			String validatorName = validatorTypeRegisterInfo.getClassType()
					.getSimpleName();
			RuleTemplate newRuleTemplate = ruleTemplateManager
					.getRuleTemplate(validatorName);
			if (newRuleTemplate == null) {
				newRuleTemplate = new RuleTemplate(validatorName);
			}
			else {
				newRuleTemplate.setInitialized(false);
			}
			newRuleTemplate.setSortFactor(++sortFactor);
			newRuleTemplate.setScope("public");
			newRuleTemplate.setParents(new RuleTemplate[] { validator });
			newRuleTemplate.setType(validatorTypeRegisterInfo.getClassType().getName());
			newRuleTemplate.setParser((XmlParser) validatorTypeRegisterInfo
					.getParser());

			PropertyTemplate property = new PropertyTemplate("type");
			property.setDefaultValue(validatorTypeRegisterInfo.getType());
			property.setFixed(true);
			property.setVisible(false);
			newRuleTemplate.addPrimitiveProperty(property);

			ruleTemplateManager.addRuleTemplate(newRuleTemplate);
		}

		// RuleTemplate dataResolver = ruleTemplateManager
		// .getRuleTemplate("DataResolver");
		// ruleTemplateManager.addRuleTemplate(dataResolver);
	}
}
