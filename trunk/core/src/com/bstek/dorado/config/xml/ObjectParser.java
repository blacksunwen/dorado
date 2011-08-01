package com.bstek.dorado.config.xml;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionInitOperation;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.core.bean.Scope;
import com.bstek.dorado.util.clazz.BeanPropertyUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-30
 */
public class ObjectParser extends ConfigurableDispatchableXmlParser implements
		BeanFactoryAware {
	private static final String SELF_PREFIX = DispatchableXmlParser.SELF + '/';
	private static final String COMPOSITE_PROPERTY_PARSER = "dorado.prototype.compositePropertyParser";
	private static final String COLLECTION_PARSER = "dorado.prototype.collectionToPropertyParser";
	private static final String SUBNODE_PARSER = "dorado.prototype.subNodeToPropertyParser";

	private BeanFactory beanFactory;
	private XmlParserAnnotationHelper xmlParserAnnotationHelper;
	private Class<? extends ObjectDefinition> definitionType = ObjectDefinition.class;
	private String impl;
	private boolean scopable;
	private boolean inheritable;
	private String defaultImpl;
	private boolean annotationProcessed;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void setXmlParserAnnotationHelper(
			XmlParserAnnotationHelper xmlParserAnnotationHelper) {
		this.xmlParserAnnotationHelper = xmlParserAnnotationHelper;
	}

	/**
	 * 设置要生成配置声明对象的实现类类型。例如:com.bstek.dorado.data.config.definition.
	 * DataTypeDefinition 。
	 * 
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public void setDefinitionType(String definitionType)
			throws ClassNotFoundException {
		setDefinitionType(ClassUtils.getClass(definitionType));
	}

	public void setDefinitionType(
			Class<? extends ObjectDefinition> definitionType)
			throws ClassNotFoundException {
		this.definitionType = definitionType;
	}

	public Class<? extends ObjectDefinition> getDefinitionType() {
		return definitionType;
	}

	/**
	 * 设置配置声明对象生成的最终对象的实例化方式。<br>
	 * 注意，有些配置声明对象并不支持复杂的实例化方式。如果将{@link #setSupportsExpressionImpl(boolean)}
	 * 属性设置为false， 那么此解析器将仅支持直接以Class名描述的实例化方式。
	 * 
	 * @see com.bstek.dorado.data.config.definition.ObjectDefinition#setImpl(String)
	 */
	public void setImpl(String impl) {
		this.impl = impl;
	}

	public String getImpl() {
		return impl;
	}

	/**
	 * 设置是否允许用户通过XML节点中的scope属性来指定最终对象的作用范围。
	 */
	public void setScopable(boolean scopable) {
		this.scopable = scopable;
	}

	public boolean isScopable() {
		return scopable;
	}

	/**
	 * 设置是否允许用户通过XML节点中的parent属性来指定配置声明对象的继承关系。
	 */
	public void setInheritable(boolean inheritable) {
		this.inheritable = inheritable;
	}

	public boolean isInheritable() {
		return inheritable;
	}

	/**
	 * 设置默认的实现类。
	 */
	public void setDefaultImpl(String defaultImpl) {
		this.defaultImpl = defaultImpl;
	}

	public String getDefaultImpl() {
		return defaultImpl;
	}

	/**
	 * 返回默认的最终对象作用范围。
	 */
	protected Scope getDefaultScope() {
		return null;
	}

	/**
	 * 将parent属性中的配置信息转换成一组指向具体配置声明对象的引用。<br>
	 * 例如根据parent="hr.Employee,hr.Manager"，可得到两个分别指向hr.Employee和hr.
	 * Manager这两个配置声明对象的引用。
	 * 
	 * @param parentNameText
	 *            parent属性的配置信息
	 * @param context
	 *            解析上下文
	 * @return 一组指向具体配置声明对象的引用。
	 * @throws Exception
	 */
	protected DefinitionReference<? extends Definition>[] getParentDefinitionReferences(
			String parentNameText, ParseContext context) throws Exception {
		return null;
	}

	/**
	 * 创建一个新的配置声明对象。
	 * 
	 * @param element
	 *            当前节点
	 * @param context
	 *            解析上下文
	 * @return 新的配置声明对象
	 * @throws Exception
	 */
	protected ObjectDefinition createDefinition(Element element,
			ParseContext context) throws Exception {
		return definitionType.newInstance();
	}

	/**
	 * 初始化一个配置声明对象。
	 * 
	 * @param definition
	 *            配置声明对象
	 * @param element
	 *            当前节点
	 * @param context
	 *            解析上下文
	 * @throws Exception
	 */
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		definition.setResource(context.getResource());
		definition.setDefaultImpl(defaultImpl);

		String realImpl = element.getAttribute(XmlConstants.ATTRIBUTE_IMPL);
		if (StringUtils.isEmpty(realImpl))
			realImpl = this.impl;

		String parent = element.getAttribute(XmlConstants.ATTRIBUTE_PARENT);
		if (StringUtils.isNotEmpty(realImpl)) {
			/*
			 * if (StringUtils.isNotEmpty(parent)) { throw new
			 * XmlParseException("[" + XmlConstants.ATTRIBUTE_PARENT +
			 * "] attribute should be empty", element, context); }
			 */
			ClassUtils.getClass(realImpl);
			definition.setImpl(realImpl);
		} else if (StringUtils.isNotEmpty(parent)) {
			if (!inheritable) {
				throw new XmlParseException("[" + XmlConstants.ATTRIBUTE_PARENT
						+ "] attribute not supported.", element, context);
			}

			DefinitionReference<? extends Definition>[] parentReferences = getParentDefinitionReferences(
					parent, context);
			if (parentReferences != null) {
				definition.setParentReferences(parentReferences);
			}
		}

		if (scopable) {
			String scope = element.getAttribute(XmlConstants.ATTRIBUTE_SCOPE);
			if (StringUtils.isEmpty(scope)) {
				definition.setScope(getDefaultScope());
			} else {
				definition.setScope(Scope.valueOf(scope));
			}
		}

		Map<String, Object> properties = parseProperties(element, context);
		definition.getProperties().putAll(properties);

		List<?> results = dispatchChildElements(element, context);
		for (Object result : results) {
			if (result instanceof DefinitionInitOperation) {
				((DefinitionInitOperation) result).execute(definition, null);
			} else if (result instanceof Operation) {
				definition.addInitOperation((Operation) result);
			}
		}
	}

	protected Object internalParse(Node node, ParseContext context)
			throws Exception {
		init();

		Element element = (Element) node;
		ObjectDefinition definition = createDefinition(element, context);
		initDefinition(definition, element, context);
		return definition;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		return internalParse(node, context);
	}

	public void init() throws Exception {
		if (!annotationProcessed) {
			annotationProcessed = true;
			collectAnnotationConfig();
		}
	}

	protected void collectAnnotationConfig() throws Exception {
		String impl = StringUtils.defaultIfEmpty(this.impl, defaultImpl);
		if (StringUtils.isEmpty(impl)) {
			return;
		}

		Class<?> classType = ClassUtils.getClass(impl);
		Map<String, XmlParser> propertyParsers = getPropertyParsers();
		Map<String, XmlParser> subParsers = getSubParsers();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils
				.getPropertyDescriptors(classType);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod == null) {
				continue;
			}
			if (readMethod.getDeclaringClass() != classType) {
				readMethod = classType.getMethod(readMethod.getName(),
						readMethod.getParameterTypes());
			}

			String propertyName = propertyDescriptor.getName();
			if (!BeanPropertyUtils.isValidProperty(classType, propertyName)) {
				continue;
			}

			Class<?> propertyType = propertyDescriptor.getPropertyType();
			boolean isCollection = Collection.class
					.isAssignableFrom(propertyType);

			XmlSubNode xmlSubNode = readMethod.getAnnotation(XmlSubNode.class);
			if (xmlSubNode != null) {
				String path = StringUtils.defaultIfEmpty(xmlSubNode.path(),
						StringUtils.capitalize(propertyName));
				String parserId = xmlSubNode.parser();
				XmlParser elementParser = null;

				if (StringUtils.isEmpty(parserId)) {
					if (isCollection) {
						Type parameterType = readMethod.getGenericReturnType();
						if (parameterType instanceof ParameterizedType) {
							propertyType = (Class<?>) ((ParameterizedType) parameterType)
									.getActualTypeArguments()[0];
						} else {
							propertyType = null;
						}
					}

					TypeAnnotationInfo typeAnnotationInfo = null;
					if (propertyType != null) {
						typeAnnotationInfo = xmlParserAnnotationHelper
								.getTypeAnnotationInfo(propertyType);
						if (typeAnnotationInfo != null) {
							elementParser = typeAnnotationInfo.getParser();
						}
						if (elementParser == null) {
							elementParser = xmlParserAnnotationHelper
									.getDefaultSubParser(propertyType);
						}
					}

					if (elementParser != null) {
						if (isCollection) {
							String subNodeName = null;
							int lastDelim = path.lastIndexOf("/");
							if (lastDelim > 0) {
								subNodeName = path.substring(lastDelim + 1);
								path = path.substring(0, lastDelim);
							}
							if (subParsers.containsKey(path)) {
								continue;
							}

							// if (typeAnnotationInfo != null
							// && (StringUtils.isEmpty(subNodeName) ||
							// DispatchableXmlParser.WILDCARD
							// .equals(subNodeName))) {
							// subNodeName = typeAnnotationInfo.getNodeName();
							// }

							CollectionToPropertyParser collectionToPropertyParser = (CollectionToPropertyParser) beanFactory
									.getBean(COLLECTION_PARSER);
							collectionToPropertyParser
									.setProperty(propertyName);
							collectionToPropertyParser.registerSubParser(
									StringUtils.defaultIfEmpty(subNodeName,
											DispatchableXmlParser.WILDCARD),
									elementParser);
							subParsers.put(path, collectionToPropertyParser);
						} else {
							if (path.startsWith(SELF_PREFIX)) {
								path = path.substring(SELF_PREFIX.length());
							}
							if (subParsers.containsKey(path)) {
								continue;
							}

							SubNodeToPropertyParser subNodeToPropertyParser = (SubNodeToPropertyParser) beanFactory
									.getBean(SUBNODE_PARSER);
							subNodeToPropertyParser.setProperty(propertyName);
							subNodeToPropertyParser.registerSubParser(
									DispatchableXmlParser.SELF, elementParser);
							subParsers.put(path, subNodeToPropertyParser);
						}
					}
				} else {
					if (subParsers.containsKey(path)) {
						continue;
					}

					XmlParser subParser = (XmlParser) beanFactory
							.getBean(parserId);
					subParsers.put(path, subParser);
				}
				continue;
			}

			if (propertyParsers.containsKey(propertyName)) {
				continue;
			}
			XmlProperty xmlProperty = readMethod
					.getAnnotation(XmlProperty.class);
			XmlParser parser = null;
			if (xmlProperty != null) {
				if (StringUtils.isNotEmpty(xmlProperty.parser())) {
					parser = (XmlParser) beanFactory.getBean(xmlProperty
							.parser());
				} else if (xmlProperty.composite()) {
					CompositePropertyParser compositePropertyParser = (CompositePropertyParser) beanFactory
							.getBean(COMPOSITE_PROPERTY_PARSER);
					parser = compositePropertyParser;
					compositePropertyParser.setImpl(propertyType.getName());
					compositePropertyParser.setOpen(Map.class
							.isAssignableFrom(propertyType));
				}
			}
			if (parser == null) {
				parser = xmlParserAnnotationHelper
						.getDefaultPropertyParser(propertyType);
			}
			propertyParsers.put(propertyName, parser);
		}
	}
}