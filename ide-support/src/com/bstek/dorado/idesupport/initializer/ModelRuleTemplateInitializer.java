package com.bstek.dorado.idesupport.initializer;

import com.bstek.dorado.data.provider.manager.DataProviderTypeRegisterInfo;
import com.bstek.dorado.data.provider.manager.DataProviderTypeRegistry;
import com.bstek.dorado.data.resolver.manager.DataResolverTypeRegisterInfo;
import com.bstek.dorado.data.resolver.manager.DataResolverTypeRegistry;
import com.bstek.dorado.data.type.DefaultEntityDataType;
import com.bstek.dorado.data.type.validator.Validator;
import com.bstek.dorado.data.type.validator.ValidatorTypeRegisterInfo;
import com.bstek.dorado.data.type.validator.ValidatorTypeRegistry;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.template.AutoPropertyTemplate;
import com.bstek.dorado.idesupport.template.AutoRuleTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.view.type.property.validator.AbstractValidator;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-26
 */
public class ModelRuleTemplateInitializer implements RuleTemplateInitializer {
	private ValidatorTypeRegistry validatorTypeRegistry;
	private DataProviderTypeRegistry dataProviderTypeRegistry;
	private DataResolverTypeRegistry dataResolverTypeRegistry;

	public void setValidatorTypeRegistry(
			ValidatorTypeRegistry validatorTypeRegistry) {
		this.validatorTypeRegistry = validatorTypeRegistry;
	}

	public void setDataProviderTypeRegistry(
			DataProviderTypeRegistry dataProviderTypeRegistry) {
		this.dataProviderTypeRegistry = dataProviderTypeRegistry;
	}

	public void setDataResolverTypeRegistry(
			DataResolverTypeRegistry dataResolverTypeRegistry) {
		this.dataResolverTypeRegistry = dataResolverTypeRegistry;
	}

	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		int sortFactor = 0;
		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();

		RuleTemplate dataTypeRuleTemplate = new RuleTemplate("DataType");
		dataTypeRuleTemplate.setScope("public");
		dataTypeRuleTemplate.setType(DefaultEntityDataType.class.getName());
		ruleTemplateManager.addRuleTemplate(dataTypeRuleTemplate);

		for (DataProviderTypeRegisterInfo dataProviderTypeInfo : dataProviderTypeRegistry
				.getTypes()) {
			String providerName = dataProviderTypeInfo.getClassType()
					.getSimpleName();

			boolean isNew = false;
			RuleTemplate newRuleTemplate = ruleTemplateManager
					.getRuleTemplate(providerName);
			if (newRuleTemplate == null) {
				newRuleTemplate = new RuleTemplate(providerName);
				newRuleTemplate.setGlobal(true);
				isNew = true;
			}
			newRuleTemplate.setSortFactor(++sortFactor);
			newRuleTemplate.setScope("public");
			newRuleTemplate.setType(dataProviderTypeInfo.getClassType()
					.getName());

			if (isNew) {
				ruleTemplateManager.addRuleTemplate(newRuleTemplate);
			}
		}

		for (DataResolverTypeRegisterInfo dataResolverTypeInfo : dataResolverTypeRegistry
				.getTypes()) {
			String ResolverName = dataResolverTypeInfo.getClassType()
					.getSimpleName();

			boolean isNew = false;
			RuleTemplate newRuleTemplate = ruleTemplateManager
					.getRuleTemplate(ResolverName);
			if (newRuleTemplate == null) {
				newRuleTemplate = new RuleTemplate(ResolverName);
				newRuleTemplate.setGlobal(true);
				isNew = true;
			}
			newRuleTemplate.setSortFactor(++sortFactor);
			newRuleTemplate.setScope("public");
			newRuleTemplate.setType(dataResolverTypeInfo.getClassType()
					.getName());

			if (isNew) {
				ruleTemplateManager.addRuleTemplate(newRuleTemplate);
			}
		}

		RuleTemplate validatorRuleTemplate = ruleTemplateManager
				.getRuleTemplate("Validator");
		if (validatorRuleTemplate == null) {
			validatorRuleTemplate = new AutoRuleTemplate("Validator",
					Validator.class.getName());
			validatorRuleTemplate.setGlobal(true);
			ruleTemplateManager.addRuleTemplate(validatorRuleTemplate);
		}

		RuleTemplate abstractValidatorRuleTemplate = ruleTemplateManager
				.getRuleTemplate("AbstractValidator");
		if (abstractValidatorRuleTemplate == null) {
			abstractValidatorRuleTemplate = new AutoRuleTemplate(
					"AbstractValidator", AbstractValidator.class.getName());
			ruleTemplateManager.addRuleTemplate(abstractValidatorRuleTemplate);
		}
		abstractValidatorRuleTemplate
				.setParents(new RuleTemplate[] { validatorRuleTemplate });

		for (ValidatorTypeRegisterInfo validatorTypeRegisterInfo : validatorTypeRegistry
				.getTypes()) {
			String validatorName = validatorTypeRegisterInfo.getClassType()
					.getSimpleName();
			RuleTemplate newRuleTemplate = ruleTemplateManager
					.getRuleTemplate(validatorName);
			if (newRuleTemplate == null) {
				newRuleTemplate = new RuleTemplate(validatorName);
			}
			newRuleTemplate.setSortFactor(++sortFactor);
			newRuleTemplate.setScope("public");
			newRuleTemplate.setType(validatorTypeRegisterInfo.getClassType()
					.getName());

			AutoPropertyTemplate propertyTemplate = new AutoPropertyTemplate(
					"type");
			propertyTemplate.setDefaultValue(validatorTypeRegisterInfo
					.getType());
			propertyTemplate.setFixed(true);
			propertyTemplate.setVisible(false);
			newRuleTemplate.addPrimitiveProperty(propertyTemplate);

			ruleTemplateManager.addRuleTemplate(newRuleTemplate);
		}
	}
}
