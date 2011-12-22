package com.bstek.dorado.idesupport.output;

import java.io.Writer;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.core.pkgs.PackageInfo;
import com.bstek.dorado.core.pkgs.PackageManager;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.model.CompositeType;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.ReferenceTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-19
 */
public class RuleSetOutputter {
	public void output(Writer writer, RuleTemplateManager ruleTemplateManager)
			throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(Constants.DEFAULT_CHARSET);

		XMLWriter xmlWriter = new XMLWriter(writer, format);
		xmlWriter.startDocument();

		Element rootElement = DocumentHelper.createElement("RuleSet");
		rootElement.addAttribute("version", ruleTemplateManager.getVersion());
		xmlWriter.writeOpen(rootElement);

		OutputContext context = new OutputContext();
		outputPackageInfos(xmlWriter, ruleTemplateManager, context);

		for (RuleTemplate ruleTemplate : ruleTemplateManager.getRuleTemplates()) {
			// 导致PropertyDataType不输出
			// if (ruleTemplate.isAbstract()
			// && ruleTemplate.getSubRuleTemplates().length == 0) {
			// continue;
			// }
			outputRuleTemplate(xmlWriter, ruleTemplate, context);
		}

		xmlWriter.writeClose(rootElement);
		xmlWriter.endDocument();
		xmlWriter.close();
	}

	protected void outputPackageInfos(XMLWriter xmlWriter,
			RuleTemplateManager ruleTemplateManager, OutputContext context)
			throws Exception {
		Map<String, PackageInfo> finalPackageInfos = new LinkedHashMap<String, PackageInfo>(
				PackageManager.getPackageInfoMap());

		Collection<PackageInfo> packageInfos = ruleTemplateManager
				.getPackageInfos();
		if (packageInfos != null) {
			for (PackageInfo packageInfo : packageInfos) {
				finalPackageInfos.put(packageInfo.getName(), packageInfo);
			}
		}

		Element packageInfosElement = DocumentHelper
				.createElement("PackageInfos");
		xmlWriter.writeOpen(packageInfosElement);
		for (PackageInfo packageInfo : finalPackageInfos.values()) {
			Element packageInfoElement = DocumentHelper
					.createElement("PackageInfo");
			setElementAttributes(packageInfoElement, packageInfo,
					"name,version");
			xmlWriter.write(packageInfoElement);
		}
		xmlWriter.writeClose(packageInfosElement);
	}

	protected void outputRuleTemplate(XMLWriter xmlWriter,
			RuleTemplate ruleTempalte, OutputContext context) throws Exception {
		Element element = DocumentHelper.createElement("Rule");
		setElementAttribute(element, ruleTempalte, "name");

		RuleTemplate[] parents = ruleTempalte.getParents();
		if (parents != null && parents.length > 0) {
			String parentsText = "";
			for (RuleTemplate parent : parents) {
				if (parentsText.length() > 0)
					parentsText += ',';
				parentsText += parent.getName();
			}
			element.addAttribute("parents", parentsText);
		}

		setElementAttributes(
				element,
				ruleTempalte,
				"label,abstract,nodeName,scope,sortFactor,category,robots,icon,labelProperty,autoGenerateId,reserve");

		xmlWriter.writeOpen(element);

		Collection<PropertyTemplate> primitiveProperties = ruleTempalte
				.getPrimitiveProperties().values();
		if (!primitiveProperties.isEmpty()) {
			Element subElement = DocumentHelper.createElement("PrimitiveProps");
			xmlWriter.writeOpen(subElement);
			for (PropertyTemplate property : primitiveProperties) {
				outputProperty(xmlWriter, property, context);
			}
			xmlWriter.writeClose(subElement);
		}

		Collection<PropertyTemplate> properties = ruleTempalte.getProperties()
				.values();
		if (!properties.isEmpty()) {
			Element subElement = DocumentHelper.createElement("Props");
			xmlWriter.writeOpen(subElement);
			for (PropertyTemplate property : properties) {
				outputProperty(xmlWriter, property, context);
			}
			xmlWriter.writeClose(subElement);
		}

		Collection<ClientEvent> clientEvents = ruleTempalte.getClientEvents()
				.values();
		if (!clientEvents.isEmpty()) {
			Element subElement = DocumentHelper.createElement("ClientEvents");
			xmlWriter.writeOpen(subElement);
			for (ClientEvent clientEvent : clientEvents) {
				outputClientEvent(xmlWriter, clientEvent, context);
			}
			xmlWriter.writeClose(subElement);
		}
		Collection<ChildTemplate> children = ruleTempalte.getChildren()
				.values();
		if (!children.isEmpty()) {
			Element subElement = DocumentHelper.createElement("Children");
			xmlWriter.writeOpen(subElement);
			for (ChildTemplate childTemplate : children) {
				outputChildTemplate(xmlWriter, childTemplate, context);
			}
			xmlWriter.writeClose(subElement);
		}

		xmlWriter.writeClose(element);
	}

	protected void outputProperty(XMLWriter xmlWriter,
			PropertyTemplate property, OutputContext context) throws Exception {
		Element element = DocumentHelper.createElement("Prop");
		setElementAttributes(element, property,
				"name,defaultValue,ignored,highlight,fixed,enumValues,editor,reserve");
		if (!property.isVisible()) {
			element.addAttribute("visible", "false");
		}

		if (StringUtils.isNotEmpty(property.getType())) {
			try {
				Class<?> type = ClassUtils.getClass(property.getType());
				if (EntityUtils.isSimpleType(type)
						&& !String.class.equals(type) && !type.isEnum()) {
					element.addAttribute("type", type.getName());
				}
			} catch (Exception e) {
				// do nothing
			}
		}

		CompositeType compositeType = property.getCompositeType();
		if (compositeType != CompositeType.Unsupport) {
			element.addAttribute("compositeType", compositeType.toString());
		}

		ReferenceTemplate reference = property.getReference();
		if (reference != null) {
			String referenceText = reference.getRuleTemplate().getName();
			if (StringUtils.isNotEmpty(reference.getProperty())) {
				referenceText += ':' + reference.getProperty();
			}
			element.addAttribute("reference", referenceText);
		}

		if (compositeType == CompositeType.Fixed
				|| compositeType == CompositeType.Open) {
			xmlWriter.writeOpen(element);
			for (PropertyTemplate subProperty : property.getProperties()
					.values()) {
				outputProperty(xmlWriter, subProperty, context);
			}
			xmlWriter.writeClose(element);
		} else {
			xmlWriter.write(element);
		}
	}

	protected void outputClientEvent(XMLWriter xmlWriter,
			ClientEvent clientEvent, OutputContext context) throws Exception {
		Element element = DocumentHelper.createElement("ClientEvent");
		setElementAttributes(element, clientEvent, "name,parameters,reserve");
		xmlWriter.write(element);
	}

	protected void outputChildTemplate(XMLWriter xmlWriter,
			ChildTemplate childTemplate, OutputContext context)
			throws Exception {
		Element element = DocumentHelper.createElement("Child");

		RuleTemplate ruleTemplate = childTemplate.getRuleTemplate();
		String ruleName = ruleTemplate.getName();
		if (ruleTemplate.isGlobal())
			element.addAttribute("rule", ruleName);

		setElementAttributes(element, childTemplate,
				"name,fixed,aggregated,ignored,reserve");
		xmlWriter.writeOpen(element);

		if (!ruleTemplate.isGlobal()) {
			outputRuleTemplate(xmlWriter, ruleTemplate, context);
		}
		xmlWriter.writeClose(element);
	}

	private void setElementAttributes(Element element, Object object,
			String propertyNames) throws Exception {
		for (String propertyName : StringUtils.split(propertyNames, ',')) {
			setElementAttribute(element, object, propertyName);
		}
	}

	private void setElementAttribute(Element element, Object object,
			String propertyName) throws Exception {
		Object value = PropertyUtils.getProperty(object, propertyName);
		if (value == null
				|| Boolean.FALSE.equals(value)
				|| (value instanceof Number && ((Number) value).intValue() == 0)) {
			return;
		}

		String text = null;
		if (value instanceof String[]) {
			text = StringUtils.join((String[]) value, ',');
		} else if (value instanceof Class<?>) {
			if (value != String.class)
				text = ((Class<?>) value).getName();
		} else {
			text = value.toString();
		}
		if (StringUtils.isEmpty(text)) {
			return;
		}

		element.addAttribute(propertyName, text);
	}
}
