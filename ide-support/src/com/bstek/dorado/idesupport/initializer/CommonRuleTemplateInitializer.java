package com.bstek.dorado.idesupport.initializer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.Modifier;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeObject;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.common.event.ClientEventRegisterInfo;
import com.bstek.dorado.common.event.ClientEventRegistry;
import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.common.event.ClientEventSupportedObject;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.data.config.definition.ListenableObjectDefinition;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.model.CompositeType;
import com.bstek.dorado.idesupport.robot.RobotInfo;
import com.bstek.dorado.idesupport.robot.RobotRegistry;
import com.bstek.dorado.idesupport.template.AutoChildTemplate;
import com.bstek.dorado.idesupport.template.AutoClientEvent;
import com.bstek.dorado.idesupport.template.AutoPropertyTemplate;
import com.bstek.dorado.idesupport.template.AutoRuleTemplate;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.LazyReferenceTemplate;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.ReferenceTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.util.clazz.ClassUtils;
import com.bstek.dorado.util.clazz.TypeInfo;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Component;
import com.bstek.dorado.view.widget.datacontrol.DataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-20
 */
public class CommonRuleTemplateInitializer implements RuleTemplateInitializer {
	private RobotRegistry robotRegistry;

	public void setRobotRegistry(RobotRegistry robotRegistry) {
		this.robotRegistry = robotRegistry;
	}

	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		TypeInfo typeInfo = TypeInfo.parse(ruleTemplate.getType());
		if (typeInfo == null) {
			return;
		}

		Class<?> type = typeInfo.getType();
		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();
		ruleTemplate.setAbstract(Modifier.isAbstract(type.getModifiers()));

		// 遍历所有SuperType并自动设定适合的ParentTemplate
		Class<?> superType = type.getSuperclass();
		List<Class<?>> superTypes = new ArrayList<Class<?>>();
		while (superType != null && !superType.equals(Object.class)
				&& !superType.equals(ClientEventSupportedObject.class)) {
			RuleTemplate parentRuleTemplate = ruleTemplateManager
					.getRuleTemplate(superType);
			if (parentRuleTemplate != null) {
				if (superTypes.isEmpty()) {
					ruleTemplate
							.setParents(new RuleTemplate[] { parentRuleTemplate });
				}
				break;
			}
			superTypes.add(superType);
			superType = superType.getSuperclass();
		}

		// 自动创建各级AbstractParentTemplate
		RuleTemplate parentRuleTemplate = null;
		if (!superTypes.isEmpty()) {
			for (int i = superTypes.size() - 1; i >= 0; i--) {
				superType = superTypes.get(i);
				RuleTemplate newRuleTemplate = createRuleTemplate(
						ruleTemplateManager, superType, parentRuleTemplate);
				parentRuleTemplate = newRuleTemplate;
			}
			if (parentRuleTemplate != null && ruleTemplate.getParents() == null) {
				ruleTemplate
						.setParents(new RuleTemplate[] { parentRuleTemplate });
			}
		}

		XmlNodeInfo xmlNodeInfo = getXmlNodeInfo(type);
		String nodeName = xmlNodeInfo.getNodeName();
		if (xmlNodeInfo != null && StringUtils.isNotEmpty(nodeName)) {
			ruleTemplate.setNodeName(nodeName);
			if (!ruleTemplate.isAbstract()
					&& StringUtils.isEmpty(ruleTemplate.getLabel())) {
				ruleTemplate.setLabel(nodeName);
			}
		}

		IdeObject ideObject = type.getAnnotation(IdeObject.class);
		if (ideObject != null
				&& ArrayUtils.indexOf(type.getDeclaredAnnotations(), ideObject) >= 0) {
			ruleTemplate.setLabelProperty(ideObject.labelProperty());
		}

		if (Component.class.isAssignableFrom(type)) {
			Widget widget = type.getAnnotation(Widget.class);
			if (widget != null) {
				ruleTemplate.setNodeName(widget.name());
				if (ArrayUtils.indexOf(type.getDeclaredAnnotations(), widget) >= 0) {
					if (StringUtils.isEmpty(ruleTemplate.getCategory())) {
						ruleTemplate.setCategory(widget.category());
					}
					ruleTemplate.setAutoGenerateId(widget.autoGenerateId());
				}
			}
		}

		List<String> robots = null;
		Map<String, RobotInfo> robotMap = robotRegistry.getRobotMap();
		for (Map.Entry<String, RobotInfo> entry : robotMap.entrySet()) {
			RobotInfo robotInfo = entry.getValue();
			if (robotInfo != null) {
				String pattern = robotInfo.getViewObject();
				if (PathUtils.match(pattern, ruleTemplate.getName())) {
					if (robots == null) {
						robots = new ArrayList<String>();
					}
					robots.add(robotInfo.getName() + '|' + robotInfo.getLabel());
				}
			}
		}

		if (robots != null) {
			ruleTemplate.setRobots(robots.toArray(new String[0]));
		}

		initProperties(ruleTemplate, typeInfo, xmlNodeInfo, initializerContext);
		initChildTemplates(ruleTemplate, typeInfo, xmlNodeInfo,
				initializerContext);
		initClientEvent(ruleTemplate, typeInfo, initializerContext);

		if (xmlNodeInfo != null && !xmlNodeInfo.getImplTypes().isEmpty()) {
			Set<Class<?>> implTypes = ClassUtils.foundClassTypes(xmlNodeInfo
					.getImplTypes().toArray(new String[0]), type);
			for (Class<?> implType : implTypes) {
				if (implType.equals(type)) {
					continue;
				}

				if (ruleTemplateManager.getRuleTemplate(implType) == null) {
					// 此处不传入parentRuleTemplate，后面由新RuleTemplate的init方法自动分配
					createRuleTemplate(ruleTemplateManager, implType, null);
				}
			}
		}
	}

	protected RuleTemplate createRuleTemplate(
			RuleTemplateManager ruleTemplateManager, Class<?> type,
			RuleTemplate parentRuleTemplate) throws Exception {
		String name = type.getSimpleName(), nodeName = null, label = null;
		String tempName = name;
		int tryCount = 0;
		while (ruleTemplateManager.getRuleTemplate(name) != null) {
			name = tempName + '_' + (++tryCount);
			nodeName = type.getSimpleName();
			label = type.getSimpleName();
		}

		String scope = null;
		if (Component.class.isAssignableFrom(type)) {
			Widget widget = type.getAnnotation(Widget.class);
			if (widget != null
					&& ArrayUtils
							.indexOf(type.getDeclaredAnnotations(), widget) >= 0
					&& !Modifier.isAbstract(type.getModifiers())) {
				label = widget.name();
			} else {
				scope = "protected";
			}
		}

		RuleTemplate newRuleTemplate = new AutoRuleTemplate(name,
				type.getName());
		if (parentRuleTemplate != null) {
			newRuleTemplate
					.setParents(new RuleTemplate[] { parentRuleTemplate });
		}
		if (label != null) {
			newRuleTemplate.setLabel(label);
		}
		if (scope != null) {
			newRuleTemplate.setScope(scope);
		}
		if (nodeName != null) {
			newRuleTemplate.setNodeName(nodeName);
		}
		ruleTemplateManager.addRuleTemplate(newRuleTemplate);
		return newRuleTemplate;
	}

	private XmlNodeInfo getXmlNodeInfo(Class<?> type) {
		XmlNodeInfo xmlNodeInfo = new XmlNodeInfo();

		for (Class<?> i : type.getInterfaces()) {
			collectXmlNodeInfo(xmlNodeInfo, i);
		}
		collectXmlNodeInfo(xmlNodeInfo, type);

		if ("#className".equals(xmlNodeInfo.getNodeName())) {
			xmlNodeInfo.setNodeName(type.getSimpleName());
		}
		// if (xmlNodeInfo.getSourceTypes() == null) {
		// xmlNodeInfo = null;
		// }
		return xmlNodeInfo;
	}

	private void collectXmlNodeInfo(XmlNodeInfo xmlNodeInfo, Class<?> type) {
		XmlNode xmlNode = type.getAnnotation(XmlNode.class);
		if (xmlNode == null
				|| ArrayUtils.indexOf(type.getDeclaredAnnotations(), xmlNode) < 0) {
			return;
		}
		xmlNodeInfo.addSourceType(type);

		if (StringUtils.isNotEmpty(xmlNode.nodeName())) {
			xmlNodeInfo.setNodeName(xmlNode.nodeName());
		}
		if (StringUtils.isNotEmpty(xmlNode.definitionType())) {
			xmlNodeInfo.setDefinitionType(xmlNode.definitionType());
		}
		for (String implType : xmlNode.implTypes()) {
			if (StringUtils.isNotEmpty(implType)) {
				xmlNodeInfo.getImplTypes().add(implType);
			}
		}
		if (!xmlNodeInfo.isScopable() && xmlNode.scopable()) {
			xmlNodeInfo.setScopable(true);
		}
		if (!xmlNodeInfo.isInheritable() && xmlNode.inheritable()) {
			xmlNodeInfo.setInheritable(true);
		}
		if (StringUtils.isNotEmpty(xmlNode.fixedProperties())) {
			Map<String, String> fixedProperties = xmlNodeInfo
					.getFixedProperties();
			for (String fixedProperty : StringUtils.split(
					xmlNode.fixedProperties(), ",;")) {
				int i = fixedProperty.indexOf('=');
				if (i > 0) {
					String property = fixedProperty.substring(0, i);
					fixedProperties.put(property,
							fixedProperty.substring(i + 1));
				}
			}
		}

		Map<String, XmlProperty> properties = xmlNodeInfo.getProperties();
		XmlProperty[] annotationProperties = xmlNode.properties();
		boolean hasPropertyAnnotation = false;
		if (annotationProperties.length == 1) {
			XmlProperty xmlProperty = annotationProperties[0];
			hasPropertyAnnotation = StringUtils.isNotEmpty(xmlProperty
					.propertyName())
					|| StringUtils.isNotEmpty(xmlProperty.propertyType())
					|| StringUtils.isNotEmpty(xmlProperty.parser());
		}
		if (hasPropertyAnnotation) {
			for (XmlProperty xmlProperty : annotationProperties) {
				if (xmlProperty.ignored()) {
					continue;
				}
				if (StringUtils.isEmpty(xmlProperty.propertyName())) {
					throw new IllegalArgumentException(
							"@XmlProperty.propertyName undefined. ["
									+ type.getName() + "]");
				}
				for (String property : StringUtils.split(xmlProperty
						.propertyName())) {
					properties.put(property, xmlProperty);
				}
			}
		}

		Set<XmlSubNode> subNodes = xmlNodeInfo.getSubNodes();
		XmlSubNode[] annotationSubNodes = xmlNode.subNodes();
		boolean hasSubNodeAnnotation = (annotationSubNodes.length > 0);
		if (annotationSubNodes.length == 1) {
			XmlSubNode xmlSubNode = annotationSubNodes[0];
			hasSubNodeAnnotation = StringUtils.isNotEmpty(xmlSubNode
					.propertyName())
					|| StringUtils.isNotEmpty(xmlSubNode.nodeName())
					|| StringUtils.isNotEmpty(xmlSubNode.propertyType());
		}
		if (hasSubNodeAnnotation) {
			for (XmlSubNode xmlSubNode : annotationSubNodes) {
				if (!(StringUtils.isNotEmpty(xmlSubNode.propertyType()) || (StringUtils
						.isNotEmpty(xmlSubNode.nodeName()) && StringUtils
						.isNotEmpty(xmlSubNode.parser())))) {
					throw new IllegalArgumentException(
							"Neither @XmlSubNode.propertyType nor @XmlSubNode.nodeName/@XmlSubNode.parser undefined. ["
									+ type.getName() + "]");
				}
				subNodes.add(xmlSubNode);
			}
		}
	}

	protected void initProperties(RuleTemplate ruleTemplate, TypeInfo typeInfo,
			XmlNodeInfo xmlNodeInfo, InitializerContext initializerContext)
			throws Exception {
		Collection<AutoPropertyTemplate> properties = getProperties(
				typeInfo.getType(), xmlNodeInfo, initializerContext);
		for (AutoPropertyTemplate propertyTemplate : properties) {
			if (propertyTemplate.isPrimitive()) {
				ruleTemplate.addPrimitiveProperty(propertyTemplate);
			} else {
				ruleTemplate.addProperty(propertyTemplate);
			}
		}
	}

	protected Collection<AutoPropertyTemplate> getProperties(Class<?> type,
			XmlNodeInfo xmlNodeInfo, InitializerContext initializerContext)
			throws Exception {
		Collection<AutoPropertyTemplate> properties = new ArrayList<AutoPropertyTemplate>();
		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();

		if (xmlNodeInfo != null) {
			if (xmlNodeInfo.isInheritable()) {
				AutoPropertyTemplate propertyTemplate = new AutoPropertyTemplate(
						"impl");
				propertyTemplate.setPrimitive(true);
				properties.add(propertyTemplate);

				propertyTemplate = new AutoPropertyTemplate("parent");
				propertyTemplate.setPrimitive(true);
				properties.add(propertyTemplate);
			}

			if (xmlNodeInfo.isScopable()) {
				AutoPropertyTemplate propertyTemplate = new AutoPropertyTemplate(
						"scope");
				propertyTemplate.setPrimitive(true);

				Object[] ecs = Scope.class.getEnumConstants();
				String[] enumValues = new String[ecs.length];
				for (int i = 0; i < ecs.length; i++) {
					enumValues[i] = ecs[i].toString();
				}
				propertyTemplate.setEnumValues(enumValues);

				properties.add(propertyTemplate);
			}

			if (StringUtils.isNotEmpty(xmlNodeInfo.getDefinitionType())) {
				Class<?> definitionType = ClassUtils.forName(xmlNodeInfo
						.getDefinitionType());
				if (ListenableObjectDefinition.class
						.isAssignableFrom(definitionType)) {
					AutoPropertyTemplate propertyTemplate = new AutoPropertyTemplate(
							"listener");
					propertyTemplate.setPrimitive(true);
					properties.add(propertyTemplate);
				}
			}

			for (Map.Entry<String, String> entry : xmlNodeInfo
					.getFixedProperties().entrySet()) {
				String propertyName = entry.getKey();
				String value = entry.getValue();

				AutoPropertyTemplate propertyTemplate = new AutoPropertyTemplate(
						propertyName);
				propertyTemplate.setDefaultValue(value);
				propertyTemplate.setFixed(true);
				propertyTemplate.setVisible(false);
				properties.add(propertyTemplate);
			}

			for (Map.Entry<String, XmlProperty> entry : xmlNodeInfo
					.getProperties().entrySet()) {
				String propertyName = entry.getKey();
				XmlProperty xmlProperty = entry.getValue();
				TypeInfo propertyTypeInfo = TypeInfo.parse(xmlProperty
						.propertyType());
				Class<?> propertyType = null;
				if (propertyTypeInfo != null) {
					propertyType = propertyTypeInfo.getType();
				}

				AutoPropertyTemplate propertyTemplate = new AutoPropertyTemplate(
						propertyName, xmlProperty);
				propertyTemplate.setPrimitive(xmlProperty.attributeOnly());
				if (propertyType != null && !propertyType.equals(String.class)) {
					propertyTemplate.setType(propertyType.getName());
				}

				if (xmlProperty.composite()) {
					initCompositeProperty(propertyTemplate, propertyType,
							initializerContext);
				}

				properties.add(propertyTemplate);
			}
		}

		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(type);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod != null
					&& propertyDescriptor.getWriteMethod() != null
					&& readMethod.getDeclaringClass() == type) {
				String propertyName = propertyDescriptor.getName();

				XmlSubNode xmlSubNode = readMethod
						.getAnnotation(XmlSubNode.class);
				if (xmlSubNode != null) {
					continue;
				}

				TypeInfo propertyTypeInfo;
				Class<?> propertyType = propertyDescriptor.getPropertyType();
				if (Collection.class.isAssignableFrom(propertyType)) {
					propertyTypeInfo = TypeInfo.parse(
							(ParameterizedType) readMethod
									.getGenericReturnType(), true);
					propertyType = propertyTypeInfo.getType();
				} else {
					propertyTypeInfo = new TypeInfo(propertyType, false);
				}

				AutoPropertyTemplate propertyTemplate = null;
				XmlProperty xmlProperty = readMethod
						.getAnnotation(XmlProperty.class);
				if (xmlProperty != null) {
					if (xmlProperty.unsupported()) {
						continue;
					}

					propertyTemplate = new AutoPropertyTemplate(propertyName,
							readMethod, xmlProperty);
					propertyTemplate.setPrimitive(xmlProperty.attributeOnly());

					if (("dataSet".equals(propertyName)
							|| "dataPath".equals(propertyName) || "property"
								.equals(propertyName))
							&& DataControl.class.isAssignableFrom(type)) {
						propertyTemplate.setHighlight(1);
					}

					if (xmlProperty.composite()) {
						initCompositeProperty(propertyTemplate, propertyType,
								initializerContext);
					}
				} else if (EntityUtils.isSimpleType(propertyType)
						|| propertyType.equals(Class.class)
						|| propertyType.isArray()
						&& propertyType.getComponentType().equals(String.class)) {
					propertyTemplate = new AutoPropertyTemplate(propertyName,
							readMethod, xmlProperty);
				}

				if (propertyTemplate != null) {
					propertyTemplate.setType(propertyDescriptor
							.getPropertyType().getName());

					if (propertyType.isEnum()) {
						Object[] ecs = propertyType.getEnumConstants();
						String[] enumValues = new String[ecs.length];
						for (int i = 0; i < ecs.length; i++) {
							enumValues[i] = ecs[i].toString();
						}
						propertyTemplate.setEnumValues(enumValues);
					}

					ComponentReference componentReference = readMethod
							.getAnnotation(ComponentReference.class);
					if (componentReference != null) {
						ReferenceTemplate referenceTemplate = new LazyReferenceTemplate(
								ruleTemplateManager,
								componentReference.value(), "id");
						propertyTemplate.setReference(referenceTemplate);
					}

					IdeProperty ideProperty = readMethod
							.getAnnotation(IdeProperty.class);
					if (ideProperty != null) {
						propertyTemplate.setVisible(ideProperty.visible());
						propertyTemplate.setEditor(ideProperty.editor());
						propertyTemplate.setHighlight(ideProperty.highlight());
						if (StringUtils.isNotEmpty(ideProperty.enumValues())) {
							propertyTemplate.setEnumValues(StringUtils.split(
									ideProperty.enumValues(), ','));
						}
					}

					ClientProperty clientProperty = readMethod
							.getAnnotation(ClientProperty.class);
					if (clientProperty != null) {
						propertyTemplate.setDefaultValue(clientProperty
								.escapeValue());
					}

					properties.add(propertyTemplate);
				}
			}
		}
		return properties;
	}

	protected void initCompositeProperty(AutoPropertyTemplate propertyTemplate,
			Class<?> propertyType, InitializerContext initializerContext)
			throws Exception {
		if (propertyType != null) {
			propertyTemplate.setCompositeType((Map.class
					.isAssignableFrom(propertyType)) ? CompositeType.Open
					: CompositeType.Fixed);

			Collection<AutoPropertyTemplate> subProperties = getProperties(
					propertyType, null, initializerContext);
			for (PropertyTemplate subProperty : subProperties) {
				propertyTemplate.addProperty(subProperty);
			}
		} else {
			propertyTemplate.setCompositeType(CompositeType.Open);
		}
	}

	protected void initChildTemplates(RuleTemplate ruleTemplate,
			TypeInfo typeInfo, XmlNodeInfo xmlNodeInfo,
			InitializerContext initializerContext) throws Exception {
		List<AutoChildTemplate> childTemplates = getChildTemplates(typeInfo,
				xmlNodeInfo, initializerContext);
		if (childTemplates.isEmpty()) {
			return;
		}

		for (ChildTemplate childTemplate : childTemplates) {
			ruleTemplate.addChild(childTemplate);
		}
	}

	protected List<AutoChildTemplate> getChildTemplates(TypeInfo typeInfo,
			XmlNodeInfo xmlNodeInfo, InitializerContext initializerContext)
			throws Exception {
		List<AutoChildTemplate> childTemplates = new ArrayList<AutoChildTemplate>();
		if (xmlNodeInfo != null) {
			for (XmlSubNode xmlSubNode : xmlNodeInfo.getSubNodes()) {
				TypeInfo propertyTypeInfo = TypeInfo.parse(xmlSubNode
						.propertyType());
				List<AutoChildTemplate> childRulesBySubNode = getChildTemplatesBySubNode(
						typeInfo, xmlSubNode.propertyName(), xmlSubNode,
						propertyTypeInfo, initializerContext);
				if (childRulesBySubNode != null) {
					childTemplates.addAll(childRulesBySubNode);
				}
			}
		}

		Class<?> type = typeInfo.getType();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(type);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod != null && readMethod.getDeclaringClass() == type) {
				XmlSubNode xmlSubNode = readMethod
						.getAnnotation(XmlSubNode.class);
				if (xmlSubNode != null) {
					TypeInfo propertyTypeInfo;
					Class<?> propertyType = propertyDescriptor
							.getPropertyType();
					if (Collection.class.isAssignableFrom(propertyType)) {
						propertyTypeInfo = TypeInfo.parse(
								(ParameterizedType) readMethod
										.getGenericReturnType(), true);
						propertyType = propertyTypeInfo.getType();
					} else {
						propertyTypeInfo = new TypeInfo(propertyType, false);
					}
					List<AutoChildTemplate> childTemplatesBySubNode = getChildTemplatesBySubNode(
							typeInfo, propertyDescriptor.getName(), xmlSubNode,
							propertyTypeInfo, initializerContext);
					if (childTemplatesBySubNode != null) {
						childTemplates.addAll(childTemplatesBySubNode);
					}
				}
			}
		}
		return childTemplates;
	}

	protected List<AutoChildTemplate> getChildTemplatesBySubNode(
			TypeInfo typeInfo, String propertyName, XmlSubNode xmlSubNode,
			TypeInfo propertyTypeInfo, InitializerContext initializerContext)
			throws Exception {
		List<AutoChildTemplate> childTemplates = new ArrayList<AutoChildTemplate>();

		boolean aggregated = xmlSubNode.aggregated();
		Class<?> propertyType = null;
		if (propertyTypeInfo != null) {
			propertyType = propertyTypeInfo.getType();
			aggregated = propertyTypeInfo.isAggregated();
		}

		Set<Class<?>> implTypes = ClassUtils.foundClassTypes(
				xmlSubNode.implTypes(), propertyType);
		for (Class<?> implType : implTypes) {
			if (implType.equals(typeInfo.getType())) {
				continue;
			}

			AutoChildTemplate childTemplate = getChildNodeByBeanType(null,
					xmlSubNode, aggregated, implType, "protected",
					initializerContext);
			if (childTemplate != null) {
				childTemplates.add(childTemplate);
			}
		}

		if (propertyType != null) {
			AutoChildTemplate childTemplate = getChildNodeByBeanType(
					StringUtils.capitalize(propertyName), xmlSubNode,
					aggregated, propertyType, null, initializerContext);
			if (childTemplate != null) {
				childTemplates.add(childTemplate);
			}
		}

		XmlNodeWrapper wrapper = xmlSubNode.wrapper();
		String wrapperName = wrapper.nodeName();
		if (StringUtils.isNotEmpty(wrapperName)) {
			List<AutoChildTemplate> wrapperTemplates = new ArrayList<AutoChildTemplate>();
			AutoRuleTemplate wrapperRuleTemplate = new AutoRuleTemplate(null);
			wrapperRuleTemplate.setNodeName(wrapper.nodeName());
			for (ChildTemplate childTemplate : childTemplates) {
				wrapperRuleTemplate.addChild(childTemplate);
			}

			AutoChildTemplate wrapperChildTemplate = new AutoChildTemplate(
					wrapperName, wrapperRuleTemplate, xmlSubNode);
			wrapperChildTemplate.setFixed(wrapper.fixed());
			wrapperTemplates.add(wrapperChildTemplate);
			return wrapperTemplates;
		} else {
			return childTemplates;
		}
	}

	protected AutoChildTemplate getChildNodeByBeanType(String name,
			XmlSubNode xmlSubNode, boolean aggregated, Class<?> beanType,
			String scope, InitializerContext initializerContext)
			throws Exception {
		XmlNodeInfo xmlNodeInfo = getXmlNodeInfo(beanType);
		if (xmlNodeInfo == null) {
			return null;
		}

		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();
		RuleTemplate ruleTemplate = ruleTemplateManager
				.getRuleTemplate(beanType);
		if (ruleTemplate == null) {
			ruleTemplate = createRuleTemplate(ruleTemplateManager, beanType,
					null);
			if (StringUtils.isNotEmpty(scope)) {
				ruleTemplate.setScope(scope);
			}
		}

		if (StringUtils.isEmpty(name)) {
			name = xmlNodeInfo.getNodeName();
			if (StringUtils.isEmpty(name)) {
				name = beanType.getSimpleName();
			}
			if (!xmlNodeInfo.getFixedProperties().isEmpty()) {
				StringBuffer buf = new StringBuffer();
				for (Map.Entry<String, String> entry : xmlNodeInfo
						.getFixedProperties().entrySet()) {
					if ((buf.length() > 0)) {
						buf.append(';');
					}
					buf.append(entry.getKey()).append('=')
							.append(entry.getValue());
				}
				name += '[' + buf.toString() + ']';
			}
		}

		AutoChildTemplate childTemplate = new AutoChildTemplate(name,
				ruleTemplate, xmlSubNode);
		childTemplate.setAggregated(aggregated);
		childTemplate.setFixed(xmlSubNode.fixed());
		return childTemplate;
	}

	protected void initClientEvent(RuleTemplate ruleTemplate,
			TypeInfo typeInfo, InitializerContext initializerContext)
			throws Exception {
		Class<?> type = typeInfo.getType();
		if (type != null && ClientEventSupported.class.isAssignableFrom(type)) {
			Map<String, ClientEventRegisterInfo> allClientEvents = new HashMap<String, ClientEventRegisterInfo>();
			Map<String, ClientEventRegisterInfo> clientEvents;

			for (Class<?> i : type.getInterfaces()) {
				clientEvents = ClientEventRegistry
						.getOwnClientEventRegisterInfos(i);
				if (clientEvents != null) {
					allClientEvents.putAll(clientEvents);
				}
			}

			clientEvents = ClientEventRegistry
					.getOwnClientEventRegisterInfos(type);
			if (clientEvents != null) {
				allClientEvents.putAll(clientEvents);
			}

			for (ClientEventRegisterInfo clientEventRegisterInfo : allClientEvents
					.values()) {
				String name = clientEventRegisterInfo.getName();

				ClientEvent clientEvent = new AutoClientEvent(
						clientEventRegisterInfo);
				clientEvent.setName(name);
				String[] signature = clientEventRegisterInfo.getSignature();
				clientEvent.setParameters(signature);
				ruleTemplate.addClientEvent(clientEvent);
			}
		}
	}
}
