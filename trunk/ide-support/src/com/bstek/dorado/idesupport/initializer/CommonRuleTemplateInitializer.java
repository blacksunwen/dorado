package com.bstek.dorado.idesupport.initializer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.common.event.ClientEventRegisterInfo;
import com.bstek.dorado.common.event.ClientEventRegistry;
import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.config.xml.CollectionToPropertyParser;
import com.bstek.dorado.config.xml.CompositePropertyParser;
import com.bstek.dorado.config.xml.DispatchableXmlParser;
import com.bstek.dorado.config.xml.IgnoreParser;
import com.bstek.dorado.config.xml.ObjectParser;
import com.bstek.dorado.config.xml.SubNodeToPropertyParser;
import com.bstek.dorado.config.xml.TypeAnnotationInfo;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserAnnotationHelper;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.config.xml.GenericObjectParser;
import com.bstek.dorado.idesupport.RuleTemplateManager;
import com.bstek.dorado.idesupport.model.ClientEvent;
import com.bstek.dorado.idesupport.model.CompositeType;
import com.bstek.dorado.idesupport.resolver.RuleSetResolver;
import com.bstek.dorado.idesupport.template.AutoChildTemplate;
import com.bstek.dorado.idesupport.template.AutoClientEvent;
import com.bstek.dorado.idesupport.template.AutoProperty;
import com.bstek.dorado.idesupport.template.AutoRuleTemplate;
import com.bstek.dorado.idesupport.template.ChildTemplate;
import com.bstek.dorado.idesupport.template.LazyReferenceTemplate;
import com.bstek.dorado.idesupport.template.PropertyTemplate;
import com.bstek.dorado.idesupport.template.ReferenceTemplate;
import com.bstek.dorado.idesupport.template.RuleTemplate;
import com.bstek.dorado.view.config.xml.ChildComponentParser;
import com.bstek.dorado.view.config.xml.ComponentParserDispatcher;
import com.bstek.dorado.view.output.ObjectOutputter;
import com.bstek.dorado.view.output.OutputUtils;
import com.bstek.dorado.view.output.Outputter;
import com.bstek.dorado.view.output.PropertyOutputter;
import com.bstek.dorado.view.widget.Component;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-20
 */
public class CommonRuleTemplateInitializer implements RuleTemplateInitializer {
	private static final String UNNAMED = "Unnamed";
	private static final String FAIL_SAFE_NODE_NAME = "FailSafeNodeName";

	private XmlParserAnnotationHelper xmlParserAnnotationHelper;

	public void setXmlParserAnnotationHelper(
			XmlParserAnnotationHelper xmlParserAnnotationHelper) {
		this.xmlParserAnnotationHelper = xmlParserAnnotationHelper;
	}

	@SuppressWarnings("unchecked")
	public void initRuleTemplate(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		Class<?> type = null;
		if (ruleTemplate.getType() != null) {
			type = ClassUtils.getClass(ruleTemplate.getType());
		}
		if (ruleTemplate.getParents() == null) {
			if (type != null) {
				RuleTemplateManager ruleTemplateManager = initializerContext
						.getRuleTemplateManager();
				Class<?> superType = type.getSuperclass();
				while (superType != null && !superType.equals(Object.class)) {
					RuleTemplate parentRuleTemplate = ruleTemplateManager
							.getRuleTemplate(superType);
					if (parentRuleTemplate != null
							&& parentRuleTemplate != ruleTemplate) {
						ruleTemplate
								.setParents(new RuleTemplate[] { parentRuleTemplate });
						break;
					}
					superType = superType.getSuperclass();
				}
			}
		}

		if (type != null && Component.class.isAssignableFrom(type)
				&& ruleTemplate.getCategory() == null) {
			Widget widget = type.getAnnotation(Widget.class);
			if (widget != null) {
				ruleTemplate.setCategory(widget.category());
			}
		}

		Context context = Context.getCurrent();
		Map<String, String> robotMap = (Map<String, String>) context
				.getAttribute(RuleSetResolver.ROBOT_MAP_ATTRIBUTE_KEY);
		if (robotMap != null) {
			String robots = robotMap.get(ruleTemplate.getName());
			if (StringUtils.isNotEmpty(robots)) {
				ruleTemplate.setRobots(StringUtils.split(robots, ","));
			}
		}

		XmlParser parser = ruleTemplate.getParser();
		if (parser instanceof ObjectParser) {
			((ObjectParser) parser).init();
		} else if (parser instanceof CollectionToPropertyParser) {
			((CollectionToPropertyParser) parser).init();
		}

		initProperties(ruleTemplate, initializerContext);
		initChildren(ruleTemplate, initializerContext);
		initClientEvent(ruleTemplate);

		for (PropertyTemplate property : ruleTemplate.getPrimitiveProperties()
				.values()) {
			initProperty(property);
		}
		for (PropertyTemplate property : ruleTemplate.getProperties().values()) {
			initProperty(property);
		}
	}

	protected void initProperty(PropertyTemplate property)
			throws ClassNotFoundException {
		Class<?> type = null;
		if (property.getType() != null) {
			type = ClassUtils.getClass(property.getType());
		}
		if (type != null && type.isEnum() && property.getEnumValues() == null) {
			Object[] ecs = type.getEnumConstants();
			String[] enumValues = new String[ecs.length];
			for (int i = 0; i < ecs.length; i++) {
				enumValues[i] = ecs[i].toString();
			}
			property.setEnumValues(enumValues);
		}
	}

	private DispatchableXmlParser getDispatchableXmlParser(
			RuleTemplate ruleTemplate) throws Exception {
		XmlParser parser = ruleTemplate.getParser();
		if (parser instanceof DispatchableXmlParser) {
			return (DispatchableXmlParser) parser;
		}
		return null;
	}

	private ObjectOutputter getObjectOutputter(RuleTemplate ruleTemplate) {
		ObjectOutputter outputter = null;
		if (ruleTemplate.getOutputter() instanceof ObjectOutputter) {
			outputter = (ObjectOutputter) ruleTemplate.getOutputter();
		}
		return outputter;
	}

	private Class<?> getType(RuleTemplate ruleTemplate) throws Exception {
		DispatchableXmlParser parser = getDispatchableXmlParser(ruleTemplate);
		Class<?> type = null;
		if (ruleTemplate.getType() != null) {
			type = ClassUtils.getClass(ruleTemplate.getType());
		}
		if (type == null && parser != null
				&& parser instanceof GenericObjectParser) {
			String impl = ((GenericObjectParser) parser).getImpl();
			if (StringUtils.isNotEmpty(impl))
				type = ClassUtils.getClass(impl);
		}
		return type;
	}

	private Collection<PropertyTemplate> getProperties(Class<?> type,
			DispatchableXmlParser parser, ObjectOutputter outputter,
			Map<String, PropertyTemplate> finalPrimitiveProperties,
			Map<String, PropertyTemplate> finalProperties,
			InitializerContext initializerContext) throws Exception {
		if (type == null) {
			return null;
		}

		Collection<PropertyTemplate> properties = new ArrayList<PropertyTemplate>();
		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(type);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String name = propertyDescriptor.getName();
			if (propertyDescriptor.getWriteMethod() != null
					&& (finalPrimitiveProperties == null || finalPrimitiveProperties
							.get(name) == null)) {
				PropertyTemplate originProperty = null;
				if (finalProperties != null) {
					originProperty = finalProperties.get(name);
				}

				Method readMethod = propertyDescriptor.getReadMethod();
				if (readMethod == null) {
					continue;
				}

				if (readMethod.getDeclaringClass() != type) {
					readMethod = type.getMethod(readMethod.getName(),
							readMethod.getParameterTypes());
				}

				XmlSubNode xmlSubNode = readMethod
						.getAnnotation(XmlSubNode.class);
				if (xmlSubNode != null) {
					continue;
				}

				ViewAttribute viewAttribute = readMethod
						.getAnnotation(ViewAttribute.class);

				XmlParser propertyParser = null;
				if (parser != null) {
					propertyParser = parser.getPropertyParsers().get(name);
					if (propertyParser instanceof IgnoreParser
							&& viewAttribute == null) {
						continue;
					}
				}

				if (originProperty != null) {
					if (originProperty instanceof AutoProperty) {
						AutoProperty autoProperty = (AutoProperty) originProperty;
						if (autoProperty.getAnnotationMethod() == readMethod
								&& (propertyParser == null || autoProperty
										.getPropertyParser() == propertyParser)) {
							continue;
						}
					}
				}

				Object defaultValue = null;
				String referenceComponentName = null, enumValues = null, editor = null;
				boolean ignored = false, visible = true, highlight = false;
				if (viewAttribute != null) {
					ignored = viewAttribute.ignored();
					visible = viewAttribute.visible();
					defaultValue = viewAttribute.defaultValue();
					referenceComponentName = viewAttribute
							.referenceComponentName();
					enumValues = viewAttribute.enumValues();
					editor = viewAttribute.editor();
					highlight = viewAttribute.highlight();
				}

				if (defaultValue == null && outputter != null) {
					defaultValue = outputter.getConfigProperties().get(name);
					if (defaultValue != null) {
						if (defaultValue instanceof PropertyOutputter) {
							try {
								defaultValue = PropertyUtils.getProperty(
										defaultValue, "escapeValue");
							} catch (Exception e) {
								// do nothing
							}
						}
					}
				}
				if (defaultValue != null) {
					if (defaultValue instanceof Outputter) {
						defaultValue = null;
					} else if (OutputUtils.IGNORE_VALUE.equals(defaultValue)) {
						defaultValue = null;
					} else if (OutputUtils.ESCAPE_VALUE.equals(defaultValue)) {
						defaultValue = null;
					}
				}

				PropertyTemplate property = new AutoProperty(readMethod,
						propertyParser);
				if (originProperty != null) {
					BeanUtils.copyProperties(originProperty, property);
				}
				property.setName(name);
				property.setIgnored(ignored);
				property.setVisible(visible);

				property.setType(propertyDescriptor.getPropertyType().getName());
				if (defaultValue != null) {
					property.setDefaultValue(defaultValue);
				}
				if (StringUtils.isNotEmpty(referenceComponentName)) {
					ReferenceTemplate referenceTemplate = new LazyReferenceTemplate(
							ruleTemplateManager, referenceComponentName, "id");
					property.setReference(referenceTemplate);
				}
				if (StringUtils.isNotEmpty(enumValues)) {
					property.setEnumValues(StringUtils.split(enumValues, ','));
				}
				property.setEditor(editor);
				property.setHighlight(highlight);

				if (propertyParser != null
						&& propertyParser instanceof CompositePropertyParser) {
					((ObjectParser) propertyParser).init();

					String impl = ((CompositePropertyParser) propertyParser)
							.getImpl();
					Class<?> compositeType = ClassUtils.getClass(impl);
					DispatchableXmlParser dispatchablePropertyParser = null;
					if (propertyParser instanceof DispatchableXmlParser) {
						dispatchablePropertyParser = (DispatchableXmlParser) propertyParser;
					}
					ObjectOutputter propertyOutputter = null;
					if (outputter != null) {
						Object obj = outputter.getConfigProperties().get(name);
						if (obj instanceof ObjectOutputter) {
							propertyOutputter = (ObjectOutputter) obj;
						}
					}

					Collection<PropertyTemplate> subProperties = getProperties(
							compositeType, dispatchablePropertyParser,
							propertyOutputter, null, null, initializerContext);
					if (subProperties != null) {
						if (((CompositePropertyParser) propertyParser).isOpen()) {
							property.setCompositeType(CompositeType.Open);
						} else {
							property.setCompositeType(CompositeType.Fixed);
						}
						for (PropertyTemplate subProperty : subProperties) {
							property.addProperty(subProperty);
							initProperty(subProperty);
						}
					}
				}

				properties.add(property);
			}
		}
		return properties;
	}

	protected void initProperties(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		Class<?> type = getType(ruleTemplate);
		if (type != null) {
			DispatchableXmlParser parser = getDispatchableXmlParser(ruleTemplate);
			ObjectOutputter outputter = getObjectOutputter(ruleTemplate);

			Map<String, PropertyTemplate> finalPrimitiveProperties = ruleTemplate
					.getFinalPrimitiveProperties();
			Map<String, PropertyTemplate> finalProperties = ruleTemplate
					.getFinalProperties();

			Collection<PropertyTemplate> properties = getProperties(type,
					parser, outputter, finalPrimitiveProperties,
					finalProperties, initializerContext);
			if (properties != null) {
				for (PropertyTemplate property : properties) {
					ruleTemplate.addProperty(property);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void initClientEvent(RuleTemplate ruleTemplate) throws Exception {
		Class<?> type = getType(ruleTemplate);
		if (type != null && ClientEventSupported.class.isAssignableFrom(type)) {
			Map<String, ClientEvent> finalClientEvents = ruleTemplate
					.getFinalClientEvents();
			Map<String, ClientEventRegisterInfo> clientEventRegisterInfos = ClientEventRegistry
					.getClientEventRegisterInfos((Class<ClientEventSupported>) type);
			if (clientEventRegisterInfos != null) {
				for (ClientEventRegisterInfo clientEventRegisterInfo : clientEventRegisterInfos
						.values()) {
					String name = clientEventRegisterInfo.getName();
					ClientEvent originClientEvent = finalClientEvents.get(name);
					if (originClientEvent != null) {
						if (((AutoClientEvent) originClientEvent)
								.getClientEventRegisterInfo() == clientEventRegisterInfo)
							continue;
					}

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

	protected void initChildren(RuleTemplate ruleTemplate,
			InitializerContext initializerContext) throws Exception {
		XmlParser parser = ruleTemplate.getParser();
		if (parser instanceof CollectionToPropertyParser) {
			initChildrenByCollectionParser(ruleTemplate,
					(CollectionToPropertyParser) parser, initializerContext);
		} else if (parser instanceof SubNodeToPropertyParser) {
			initChildrenBySubNodeParser(ruleTemplate,
					(SubNodeToPropertyParser) parser, initializerContext);
		} else if (parser instanceof DispatchableXmlParser) {
			initChildrenByDispatchableXmlParser(ruleTemplate,
					(DispatchableXmlParser) parser, initializerContext);
		}
	}

	protected void initChildrenByCollectionParser(RuleTemplate ruleTemplate,
			CollectionToPropertyParser parser,
			InitializerContext initializerContext) throws Exception {
		XmlSubNode xmlSubNode = null;
		Class<?> objectType = initializerContext.getCurrentType();
		String property = initializerContext.getCurrentProperty();
		Class<?> propertyType = parser.getElementType();
		if (StringUtils.isEmpty(property)) {
			property = parser.getProperty();
		}
		if (objectType != null && StringUtils.isNotEmpty(property)) {
			PropertyDescriptor propertyDescriptor = BeanUtils
					.getPropertyDescriptor(objectType, property);
			if (propertyDescriptor != null) {
				if (propertyType == null) {
					propertyType = propertyDescriptor.getPropertyType();
				}
				Method readMethod = propertyDescriptor.getReadMethod();
				if (readMethod != null) {
					if (readMethod.getDeclaringClass() != objectType) {
						readMethod = objectType.getMethod(readMethod.getName(),
								readMethod.getParameterTypes());
					}
					xmlSubNode = readMethod.getAnnotation(XmlSubNode.class);
				}
			}
		}
		addChildrenByCollectionParser(ruleTemplate, initializerContext, parser,
				xmlSubNode, propertyType);
	}

	protected void addChildrenByCollectionParser(RuleTemplate ruleTemplate,
			InitializerContext initializerContext,
			CollectionToPropertyParser parser, XmlSubNode xmlSubNode,
			Class<?> propertyType) throws Exception {
		Set<String> nodeNames = new HashSet<String>();
		for (Class<?> implType : parser.getImplTypes()) {
			TypeAnnotationInfo typeAnnotationInfo = getTypeAnnotationInfo(
					initializerContext, implType);
			if (typeAnnotationInfo != null) {
				String nodeName = typeAnnotationInfo.getNodeName();
				nodeNames.add(nodeName);
				doAddChildBySubParser(ruleTemplate, initializerContext,
						nodeName, nodeName, typeAnnotationInfo.getParser(),
						null, implType, true);
			}
		}

		for (Map.Entry<String, XmlParser> entry : parser.getSubParsers()
				.entrySet()) {
			String nodeName = entry.getKey();
			if (nodeNames.contains(nodeName)) {
				continue;
			}

			XmlParser subParser = entry.getValue();
			Class<?> subType = null;
			if (subParser instanceof ObjectParser) {
				String impl = ((ObjectParser) subParser).getImpl();
				if (StringUtils.isNotEmpty(impl))
					subType = ClassUtils.getClass(impl);
			}
			if (subType == null) {
				subType = propertyType;
			}
			addChildBySubParser(ruleTemplate, initializerContext, nodeName,
					subParser, xmlSubNode, subType, true);
		}
	}

	protected void initChildrenBySubNodeParser(RuleTemplate ruleTemplate,
			SubNodeToPropertyParser parser,
			InitializerContext initializerContext) throws Exception {
		XmlSubNode xmlSubNode = null;
		Class<?> objectType = initializerContext.getCurrentType();
		String property = initializerContext.getCurrentProperty();
		Class<?> propertyType = null;
		if (StringUtils.isEmpty(property)) {
			property = parser.getProperty();
		}
		if (objectType != null && StringUtils.isNotEmpty(property)) {
			PropertyDescriptor propertyDescriptor = BeanUtils
					.getPropertyDescriptor(objectType, property);
			if (propertyDescriptor != null) {
				propertyType = propertyDescriptor.getPropertyType();
				Method readMethod = propertyDescriptor.getReadMethod();
				if (readMethod != null) {
					if (readMethod.getDeclaringClass() != objectType) {
						readMethod = objectType.getMethod(readMethod.getName(),
								readMethod.getParameterTypes());
					}
					xmlSubNode = readMethod.getAnnotation(XmlSubNode.class);
				}
			}
		}
		addChildrenBySubNodeParser(ruleTemplate, initializerContext, parser,
				xmlSubNode, propertyType);
	}

	protected void addChildrenBySubNodeParser(RuleTemplate ruleTemplate,
			InitializerContext initializerContext,
			SubNodeToPropertyParser parser, XmlSubNode xmlSubNode,
			Class<?> propertyType) throws Exception {
		Set<String> nodeNames = new HashSet<String>();
		for (Map.Entry<String, XmlParser> entry : parser.getSubParsers()
				.entrySet()) {
			String nodeName = entry.getKey();
			if (nodeNames.contains(nodeName)) {
				continue;
			}

			XmlParser subParser = entry.getValue();
			Class<?> subType = null;
			if (subParser instanceof ObjectParser) {
				String impl = ((ObjectParser) subParser).getImpl();
				if (StringUtils.isNotEmpty(impl))
					subType = ClassUtils.getClass(impl);
			}
			if (subType == null) {
				subType = propertyType;
			}

			boolean aggregated = false;
			if (propertyType != null) {
				aggregated = Collection.class.isAssignableFrom(propertyType);
			}
			addChildBySubParser(ruleTemplate, initializerContext, nodeName,
					subParser, xmlSubNode, subType, aggregated);
		}
	}

	protected void initChildrenByDispatchableXmlParser(
			RuleTemplate ruleTemplate, DispatchableXmlParser parser,
			InitializerContext initializerContext) throws Exception {
		Class<?> type = getType(ruleTemplate);
		if (type != null) {
			initializerContext.pushType(type);
		}
		initializerContext.pushOutputter(ruleTemplate.getOutputter());
		try {
			addChildrenByDispatchableParser(ruleTemplate, initializerContext,
					parser);
		} finally {
			if (type != null) {
				initializerContext.popType();
			}
			initializerContext.popOutputter();
		}
	}

	protected void addChildrenByDispatchableParser(RuleTemplate ruleTemplate,
			InitializerContext initializerContext, DispatchableXmlParser parser)
			throws Exception {
		for (Map.Entry<String, XmlParser> entry : parser.getSubParsers()
				.entrySet()) {
			String nodeName = entry.getKey();
			XmlParser subParser = entry.getValue();
			if ("ClientEvent".equals(nodeName)) {
				continue;
			} else if (DispatchableXmlParser.SELF.equals(nodeName)
					&& !(subParser instanceof CollectionToPropertyParser)) {
				ruleTemplate.setParser(subParser);
			}

			XmlSubNode xmlSubNode = null;
			Class<?> propertyType = null;
			boolean aggregated = true;

			Class<?> objectType = initializerContext.getCurrentType();
			if (objectType != null) {
				String property = null;
				if (subParser instanceof CollectionToPropertyParser) {
					property = ((CollectionToPropertyParser) subParser)
							.getProperty();
				} else if (subParser instanceof SubNodeToPropertyParser) {
					SubNodeToPropertyParser subNodeParser = (SubNodeToPropertyParser) subParser;
					property = subNodeParser.getProperty();
					XmlParser tempParser = subNodeParser.getSubParsers().get(
							DispatchableXmlParser.SELF);
					if (tempParser != null) {
						subParser = tempParser;
					}
				} else if (!DispatchableXmlParser.WILDCARD.equals(nodeName)) {
					property = StringUtils.uncapitalize(nodeName);
					property = StringUtils.substringBefore(property, "/");
				}

				if (StringUtils.isNotEmpty(property)) {
					PropertyDescriptor propertyDescriptor = BeanUtils
							.getPropertyDescriptor(objectType, property);
					if (propertyDescriptor != null) {
						propertyType = propertyDescriptor.getPropertyType();
						Method readMethod = propertyDescriptor.getReadMethod();
						if (readMethod != null) {
							if (readMethod.getDeclaringClass() != objectType) {
								readMethod = objectType.getMethod(
										readMethod.getName(),
										readMethod.getParameterTypes());
							}
							xmlSubNode = readMethod
									.getAnnotation(XmlSubNode.class);
						}

						if (propertyType != null) {
							aggregated = Collection.class
									.isAssignableFrom(propertyType);
							if (aggregated && readMethod != null) {
								Type parameterType = readMethod
										.getGenericReturnType();
								if (parameterType instanceof ParameterizedType) {
									propertyType = (Class<?>) ((ParameterizedType) parameterType)
											.getActualTypeArguments()[0];
								} else {
									propertyType = null;
								}
							}
						}
					}
				}

				if (subParser instanceof CollectionToPropertyParser) {
					aggregated = false;
				}
			}

			addChildBySubParser(ruleTemplate, initializerContext, nodeName,
					subParser, xmlSubNode, propertyType, aggregated);
		}
	}

	protected void addChildBySubParser(RuleTemplate ruleTemplate,
			InitializerContext initializerContext, String nodeName,
			XmlParser subParser, XmlSubNode xmlSubNode, Class<?> propertyType,
			boolean aggregated) throws Exception {
		String childName = null;
		if (xmlSubNode != null) {
			childName = xmlSubNode.name();
		}
		if (nodeName.indexOf("/") > 0) {
			int index = nodeName.indexOf("/");
			String nodeName1 = nodeName.substring(0, index);
			String nodeName2 = nodeName.substring(index + 1);

			if (DispatchableXmlParser.SELF.equals(nodeName1)) {
				if (StringUtils.isEmpty(childName)) {
					childName = nodeName2;
				}
				doAddChildBySubParser(ruleTemplate, initializerContext,
						childName, nodeName2, subParser, xmlSubNode,
						propertyType, aggregated);
			} else {
				String name = ruleTemplate.getName() + '.' + nodeName1;
				RuleTemplate subRuleTemplate = new RuleTemplate(name);
				subRuleTemplate.setNodeName(nodeName1);

				if (StringUtils.isEmpty(childName)) {
					childName = nodeName1;
				}

				ChildTemplate childTemplate = new ChildTemplate(childName,
						subRuleTemplate);
				childTemplate.setAggregated(false);
				if (xmlSubNode != null) {
					childTemplate.setFixed(xmlSubNode.fixed());
				}
				// else {
				// childTemplate.setFixed(true);
				// }

				if (subParser instanceof SubNodeToPropertyParser) {
					subRuleTemplate.setParser(subParser);
				} else {
					initializerContext.pushProperty(StringUtils
							.uncapitalize(nodeName1));
					try {
						doAddChildBySubParser(subRuleTemplate,
								initializerContext, nodeName2, nodeName2,
								subParser, null, propertyType, aggregated);
					} finally {
						initializerContext.popProperty();
					}
				}
				ruleTemplate.addChild(childTemplate);
			}
		} else {
			if (StringUtils.isEmpty(childName)) {
				childName = nodeName;
			}
			doAddChildBySubParser(ruleTemplate, initializerContext, childName,
					nodeName, subParser, xmlSubNode, propertyType, aggregated);
		}
	}

	private String getNodeNameByType(Class<?> type) {
		String nodeName = FAIL_SAFE_NODE_NAME;
		XmlNode xmlNode = type.getAnnotation(XmlNode.class);
		if (xmlNode != null) {
			nodeName = xmlNode.nodeName();
		}
		if (StringUtils.isEmpty(nodeName)) {
			nodeName = type.getSimpleName();
		}
		return nodeName;
	}

	private void doAddChildBySubParser(RuleTemplate ruleTemplate,
			InitializerContext initializerContext, String childName,
			String nodeName, XmlParser subParser, XmlSubNode xmlSubNode,
			Class<?> propertyType, boolean aggregated) throws Exception {
		if (subParser instanceof IgnoreParser) {
			return;
		}

		if (DispatchableXmlParser.WILDCARD.equals(childName)) {
			if (propertyType != null) {
				childName = propertyType.getSimpleName();
			} else if (subParser instanceof ComponentParserDispatcher) {
				childName = "Component";
			} else if (subParser instanceof ChildComponentParser) {
				childName = "Children";
			} else {
				childName = UNNAMED;
			}
		}

		if (DispatchableXmlParser.WILDCARD.equals(nodeName)) {
			if (propertyType != null) {
				nodeName = getNodeNameByType(propertyType);
			} else {
				nodeName = FAIL_SAFE_NODE_NAME;
			}

			// if (subParser instanceof DispatchableXmlParser) {
			// XmlParser realSubParser = ((DispatchableXmlParser) subParser)
			// .getSubParsers().get(DispatchableXmlParser.SELF);
			// if (realSubParser != null)
			// subParser = realSubParser;
			// }
		}

		boolean fixed = false;
		if (!aggregated && xmlSubNode != null && xmlSubNode.fixed()) {
			fixed = true;
			aggregated = false;
		}

		RuleTemplate subRuleTemplate = null;
		RuleTemplateManager ruleTemplateManager = initializerContext
				.getRuleTemplateManager();

		Map<MultiKey, ChildTemplate> childTemplateMap = initializerContext
				.getChildTemplateMap();
		MultiKey childKey = new MultiKey(subParser, childName, propertyType);
		ChildTemplate childTemplate = childTemplateMap.get(childKey);
		if (childTemplate == null) {
			boolean processed = false;
			if (subParser instanceof ComponentParserDispatcher
					|| subParser instanceof ChildComponentParser) {
				if (propertyType == null) {
					if (subParser instanceof ComponentParserDispatcher) {
						propertyType = ((ComponentParserDispatcher) subParser)
								.getComponentType();
					}
				}
				subRuleTemplate = ruleTemplateManager
						.getRuleTemplate(propertyType);
				if (subRuleTemplate == null) {
					subRuleTemplate = ruleTemplateManager
							.getRuleTemplate("Component");
					processed = (propertyType == null);
				} else {
					processed = true;
				}
			}

			if (!processed) {
				if (propertyType != null
						&& FAIL_SAFE_NODE_NAME.equals(nodeName)
						&& xmlSubNode == null) {
					nodeName = getNodeNameByType(propertyType);
				}

				String name = ruleTemplate.getName() + '.' + childName;
				subRuleTemplate = new AutoRuleTemplate(name);
				subRuleTemplate.setNodeName(nodeName);
				subRuleTemplate.setParser(subParser);
				subRuleTemplate
						.setSortFactor(ruleTemplate.getChildren().size() + 1);

				Class<?> subType = propertyType;
				if (subType == null && subParser instanceof ObjectParser) {
					String impl = ((ObjectParser) subParser).getImpl();
					if (StringUtils.isNotEmpty(impl)) {
						subType = ClassUtils.getClass(impl);
					}
				}
				if (subType != null
						&& !(subParser instanceof CollectionToPropertyParser)) {
					subRuleTemplate.setType(subType.getName());
				}

				String property = initializerContext.getCurrentProperty();
				if (StringUtils.isNotEmpty(property)
						&& initializerContext.getCurrentOutputter() instanceof ObjectOutputter) {
					Object subOutputter = ((ObjectOutputter) initializerContext
							.getCurrentOutputter()).getConfigProperties().get(
							property);
					if (subOutputter != null
							&& subOutputter instanceof Outputter) {
						subRuleTemplate.setOutputter((Outputter) subOutputter);
					}
				}
			}

			childTemplate = new AutoChildTemplate(childName, subParser);
			childTemplate.setRuleTemplate(subRuleTemplate);
			childTemplate.setAggregated(aggregated);
			childTemplate.setFixed(fixed);
			childTemplateMap.put(childKey, childTemplate);
		} else {
			subRuleTemplate = childTemplate.getRuleTemplate();
			if (!subRuleTemplate.isGlobal()) {
				ruleTemplateManager.addRuleTemplate(subRuleTemplate);
			}

			childTemplate = new AutoChildTemplate(childName, subParser);
			childTemplate.setRuleTemplate(subRuleTemplate);
			childTemplate.setAggregated(aggregated);
			childTemplate.setFixed(fixed);
		}

		ChildTemplate originChildTemplate = ruleTemplate.getFinalChildren()
				.get(childName);
		if (originChildTemplate != null) {
			if (childTemplate instanceof AutoChildTemplate) {
				if (originChildTemplate instanceof AutoChildTemplate) {
					AutoChildTemplate act = (AutoChildTemplate) childTemplate;
					AutoChildTemplate oact = (AutoChildTemplate) originChildTemplate;
					if (act.getXmlParser() == oact.getXmlParser()) {
						return;
					}
				} else {
					return;
				}
			}
		}
		ruleTemplate.addChild(childTemplate);
	}

	private TypeAnnotationInfo getTypeAnnotationInfo(
			InitializerContext initializerContext, Class<?> type)
			throws Exception {
		TypeAnnotationInfo typeAnnotationInfo = initializerContext
				.getTypeAnnotationInfoMap().get(type);
		if (typeAnnotationInfo == null) {
			typeAnnotationInfo = xmlParserAnnotationHelper
					.getTypeAnnotationInfo(type);
			if (typeAnnotationInfo != null) {
				initializerContext.getTypeAnnotationInfoMap().put(type,
						typeAnnotationInfo);
			}
		}
		return typeAnnotationInfo;
	}
}
