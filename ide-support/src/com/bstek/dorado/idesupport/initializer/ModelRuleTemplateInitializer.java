/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.idesupport.initializer;

import com.bstek.dorado.data.provider.manager.DataProviderTypeRegisterInfo;
import com.bstek.dorado.data.provider.manager.DataProviderTypeRegistry;
import com.bstek.dorado.data.resolver.manager.DataResolverTypeRegisterInfo;
import com.bstek.dorado.data.resolver.manager.DataResolverTypeRegistry;
import com.bstek.dorado.data.type.manager.DataTypeTypeRegisterInfo;
import com.bstek.dorado.data.type.manager.DataTypeTypeRegistry;
import com.bstek.dorado.data.type.validator.Validator;
import com.bstek.dorado.data.type.validator.ValidatorTypeRegisterInfo;
import com.bstek.dorado.data.type.validator.ValidatorTypeRegistry;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.template.AutoRuleTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.view.type.property.validator.AbstractValidator;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-26
 */
public class ModelRuleTemplateInitializer implements RuleTemplateInitializer {
	private DataTypeTypeRegistry dataTypeTypeRegistry;
	private ValidatorTypeRegistry validatorTypeRegistry;
	private DataProviderTypeRegistry dataProviderTypeRegistry;
	private DataResolverTypeRegistry dataResolverTypeRegistry;

	public void setDataTypeTypeRegistry(
			DataTypeTypeRegistry dataTypeTypeRegistry) {
		this.dataTypeTypeRegistry = dataTypeTypeRegistry;
	}

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

		for (DataTypeTypeRegisterInfo dataTypeTypeInfo : dataTypeTypeRegistry
				.getTypes()) {
			String dataTypeName = dataTypeTypeInfo.getClassType()
					.getSimpleName();

			// 旧的EclipseIDE要求DataType的ruleName必须是DataType
			String ruleName;
			if ("default".equals(dataTypeTypeInfo.getType())) {
				ruleName = "DataType";
			} else {
				ruleName = dataTypeName;
			}

			boolean isNew = false;
			RuleTemplate newRuleTemplate = ruleTemplateManager
					.getRuleTemplate(ruleName);
			if (newRuleTemplate == null) {
				newRuleTemplate = new RuleTemplate(ruleName);
				newRuleTemplate.setGlobal(true);
				isNew = true;
			}
			newRuleTemplate.setSortFactor(++sortFactor);
			newRuleTemplate.setScope("public");
			newRuleTemplate.setType(dataTypeTypeInfo.getClassType().getName());
			newRuleTemplate.setReserve(dataTypeTypeInfo.getType());

			if (isNew) {
				ruleTemplateManager.addRuleTemplate(newRuleTemplate);
			}
		}

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
			newRuleTemplate.setReserve(dataProviderTypeInfo.getType());

			if (isNew) {
				ruleTemplateManager.addRuleTemplate(newRuleTemplate);
			}
		}

		for (DataResolverTypeRegisterInfo dataResolverTypeInfo : dataResolverTypeRegistry
				.getTypes()) {
			String resolverName = dataResolverTypeInfo.getClassType()
					.getSimpleName();

			boolean isNew = false;
			RuleTemplate newRuleTemplate = ruleTemplateManager
					.getRuleTemplate(resolverName);
			if (newRuleTemplate == null) {
				newRuleTemplate = new RuleTemplate(resolverName);
				newRuleTemplate.setGlobal(true);
				isNew = true;
			}
			newRuleTemplate.setSortFactor(++sortFactor);
			newRuleTemplate.setScope("public");
			newRuleTemplate.setType(dataResolverTypeInfo.getClassType()
					.getName());
			newRuleTemplate.setReserve(dataResolverTypeInfo.getType());

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
			newRuleTemplate.setLabel(validatorName);
			newRuleTemplate.setSortFactor(++sortFactor);
			newRuleTemplate.setScope("public");
			newRuleTemplate.setType(validatorTypeRegisterInfo.getClassType()
					.getName());
			newRuleTemplate.setReserve(validatorTypeRegisterInfo.getType());

			ruleTemplateManager.addRuleTemplate(newRuleTemplate);
		}
	}
}
