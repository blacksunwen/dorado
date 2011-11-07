package com.bstek.dorado.config.xml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionInitOperation;
import com.bstek.dorado.util.Assert;

public class SubNodeToPropertyParser extends ConfigurableDispatchableXmlParser {
	private XmlParserAnnotationHelper xmlParserAnnotationHelper;
	private boolean initialized = false;
	private List<Class<?>> implTypes = new ArrayList<Class<?>>();
	private String property;
	private Class<?> elementType;

	public void setXmlParserAnnotationHelper(
			XmlParserAnnotationHelper xmlParserAnnotationHelper) {
		this.xmlParserAnnotationHelper = xmlParserAnnotationHelper;
	}

	public void registerImplType(Class<?> implType) {
		if (initialized) {
			throw new IllegalStateException(
					"Can not registerImpleType after CollectionParser used.");
		}
		implTypes.add(implType);
	}

	public void setImplTypes(List<Class<?>> implTypes) {
		for (Class<?> implType : implTypes) {
			registerImplType(implType);
		}
	}

	public List<Class<?>> getImplTypes() {
		return implTypes;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	public void setElementType(Class<?> elementType) {
		this.elementType = elementType;
	}

	public void setElementType(String elementType)
			throws ClassNotFoundException, LinkageError {
		this.elementType = ClassUtils.forName(elementType,
				ClassUtils.getDefaultClassLoader());
	}

	public Class<?> getElementType() {
		return elementType;
	}

	public void init() throws Exception {
		if (!initialized) {
			initialized = true;

			Map<String, XmlParser> subParsers = getSubParsers();
			Map<String, XmlParser> oldSubParsers = new LinkedHashMap<String, XmlParser>(
					subParsers);
			subParsers.clear();

			for (Class<?> implType : implTypes) {
				String nodeName = implType.getSimpleName();
				TypeAnnotationInfo typeAnnotationInfo = xmlParserAnnotationHelper
						.getTypeAnnotationInfo(implType);
				if (typeAnnotationInfo != null) {
					nodeName = typeAnnotationInfo.getNodeName();
				}
				if (!subParsers.containsKey(nodeName)) {
					registerSubParser(nodeName, typeAnnotationInfo.getParser());
				}
			}
			subParsers.putAll(oldSubParsers);
		}
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		init();

		List<?> list = this.dispatchChildElements((Element) node, context);
		return (list != null && !list.isEmpty()) ? new SetElementOperation(
				property, list.get(0)) : null;
	}
}

class SetElementOperation implements DefinitionInitOperation {
	private String property;
	private Object element;

	public SetElementOperation(String property, Object element) {
		Assert.notEmpty(property);
		this.property = property;
		this.element = element;
	}

	public void execute(Object object, CreationContext context)
			throws Exception {
		if (object instanceof Definition) {
			Definition parentDefinition = (Definition) object;
			parentDefinition.setProperty(property, element);
		} else {
			PropertyUtils.setProperty(object, property, element);
		}
	}
}
