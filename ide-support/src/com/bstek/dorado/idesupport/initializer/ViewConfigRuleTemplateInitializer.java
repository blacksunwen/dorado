package com.bstek.dorado.idesupport.initializer;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.LazyReferenceTemplate;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.view.registry.AssembledComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.ComponentTypeRegistry;
import com.bstek.dorado.view.registry.DefaultComponentTypeRegisterInfo;
import com.bstek.dorado.view.registry.LayoutTypeRegisterInfo;
import com.bstek.dorado.view.registry.LayoutTypeRegistry;
import com.bstek.dorado.view.registry.VirtualPropertyDescriptor;
import com.bstek.dorado.view.widget.Component;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-26
 */
public class ViewConfigRuleTemplateInitializer implements
		RuleTemplateInitializer {

	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {		
		Context context = Context.getCurrent();
		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();

		int sortFactor = 100;
		RuleTemplate layoutHolderTemplate = ruleTemplateManager
				.getRuleTemplate("LayoutHolder");
		RuleTemplate[] layoutRuleTemplates = new RuleTemplate[] { ruleTemplateManager
				.getRuleTemplate("Layout") };
		RuleTemplate[] layoutConstraintRuleTemplates = new RuleTemplate[] { ruleTemplateManager
				.getRuleTemplate("LayoutConstraint") };
		LayoutTypeRegistry layoutTypeRegistry = (LayoutTypeRegistry) context
				.getServiceBean("layoutTypeRegistry");
		for (LayoutTypeRegisterInfo registerInfo : layoutTypeRegistry
				.getRegisterInfos()) {
			RuleTemplate layoutRuleTemplate = new RuleTemplate(
					StringUtils.capitalize(registerInfo.getType()) + "Layout");
			layoutRuleTemplate.setNodeName(registerInfo.getType());
			layoutRuleTemplate.setParents(layoutRuleTemplates);
			if (registerInfo.getClassType() != null) {
				layoutRuleTemplate.setType(registerInfo.getClassType()
						.getName());
			}
			layoutRuleTemplate.setSortFactor(++sortFactor);
			ruleTemplateManager.addRuleTemplate(layoutRuleTemplate);
			layoutHolderTemplate.addChild(new ChildTemplate(layoutRuleTemplate
					.getNodeName(), layoutRuleTemplate));

			RuleTemplate layoutConstraintRuleTemplate = new RuleTemplate(
					StringUtils.capitalize(registerInfo.getType())
							+ "LayoutConstraint");
			layoutConstraintRuleTemplate.setGlobal(true);
			layoutConstraintRuleTemplate
					.setParents(layoutConstraintRuleTemplates);
			if (registerInfo.getConstraintClassType() != null) {
				layoutConstraintRuleTemplate.setType(registerInfo
						.getConstraintClassType().getName());
			}
			ruleTemplateManager.addRuleTemplate(layoutConstraintRuleTemplate);
		}

		sortFactor = 200;
		ComponentTypeRegistry componentTypeRegistry = (ComponentTypeRegistry) context
				.getServiceBean("componentTypeRegistry");
		for (ComponentTypeRegisterInfo registerInfo : componentTypeRegistry
				.getRegisterInfos()) {
			String name = registerInfo.getName();
			RuleTemplate componentRuleTemplate = ruleTemplateManager
					.getRuleTemplate(name);
			if (componentRuleTemplate == null) {
				componentRuleTemplate = new RuleTemplate(name);
				componentRuleTemplate.setScope("public");
			} else {
				componentRuleTemplate.setInitialized(false);
				if ("protected".equals(componentRuleTemplate.getScope())) {
					componentRuleTemplate.setScope("public");
				}
			}
			componentRuleTemplate.setSortFactor(++sortFactor);
			componentRuleTemplate.setCategory(registerInfo.getCategory());

			Class<? extends Component> classType = registerInfo.getClassType();
			if (registerInfo instanceof AssembledComponentTypeRegisterInfo) {
				AssembledComponentTypeRegisterInfo assembledComponentTypeRegisterInfo = (AssembledComponentTypeRegisterInfo) registerInfo;
				ComponentTypeRegisterInfo superComponentTypeInfo = assembledComponentTypeRegisterInfo
						.getSuperComponentTypeInfo();
				if (superComponentTypeInfo != null) {
					String superRuleName = superComponentTypeInfo.getName();
					componentRuleTemplate
							.setParents(new RuleTemplate[] { ruleTemplateManager
									.getRuleTemplate(superRuleName) });

					for (VirtualPropertyDescriptor propertyDescriptor : assembledComponentTypeRegisterInfo
							.getVirtualProperties().values()) {
						PropertyTemplate propertyTemplate = new PropertyTemplate();
						propertyTemplate.setName(propertyDescriptor.getName());
						if (propertyDescriptor.getType() != null) {
							propertyTemplate.setType(propertyDescriptor
									.getType().getName());
						}
						propertyTemplate.setDefaultValue(propertyDescriptor
								.getDefaultValue());
						if (StringUtils.isNotEmpty(propertyDescriptor
								.getReferenceComponentType())) {
							propertyTemplate
									.setReference(new LazyReferenceTemplate(
											ruleTemplateManager,
											propertyDescriptor
													.getReferenceComponentType(),
											"id"));
						}
						componentRuleTemplate.addProperty(propertyTemplate);
					}
				}
			} else if (classType != null) {
				componentRuleTemplate.setType(classType.getName());
			}

			componentRuleTemplate.setParser((XmlParser) registerInfo
					.getParser());
			if (registerInfo instanceof DefaultComponentTypeRegisterInfo) {
				componentRuleTemplate
						.setOutputter(((DefaultComponentTypeRegisterInfo) registerInfo)
								.getOutputter());
			}
			ruleTemplateManager.addRuleTemplate(componentRuleTemplate);
		}
	}
}
