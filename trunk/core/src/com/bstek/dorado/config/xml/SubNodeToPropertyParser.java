package com.bstek.dorado.config.xml;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionInitOperation;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.util.Assert;
import com.bstek.dorado.util.clazz.ClassUtils;

public class SubNodeToPropertyParser extends ConfigurableDispatchableXmlParser {
	private String property;
	private Class<?> elementType;

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
		this.elementType = ClassUtils.forName(elementType);
	}

	public Class<?> getElementType() {
		return elementType;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		List<?> list = this.dispatchChildElements((Element) node, context);
		return (list != null && !list.isEmpty()) ? new SetElementOperation(
				property, list.get(0)) : null;
	}
}

class SetElementOperation implements DefinitionInitOperation {
	private static Set<String> NATIVE_DEFINITION_PROPERTIES = new HashSet<String>();

	static {
		for (PropertyDescriptor propertyDescriptor : PropertyUtils
				.getPropertyDescriptors(Definition.class)) {
			NATIVE_DEFINITION_PROPERTIES.add(propertyDescriptor.getName());
		}

		for (PropertyDescriptor propertyDescriptor : PropertyUtils
				.getPropertyDescriptors(ObjectDefinition.class)) {
			NATIVE_DEFINITION_PROPERTIES.add(propertyDescriptor.getName());
		}
	}

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
			PropertyDescriptor propertyDescriptor = PropertyUtils
					.getPropertyDescriptor(object, property);
			if (propertyDescriptor != null
					&& propertyDescriptor.getWriteMethod() != null
					&& !NATIVE_DEFINITION_PROPERTIES.contains(property)) {
				PropertyUtils.setSimpleProperty(object, property, element);
			} else {
				Definition parentDefinition = (Definition) object;
				parentDefinition.setProperty(property, element);
			}
		} else {
			PropertyUtils.setSimpleProperty(object, property, element);
		}
	}
}
