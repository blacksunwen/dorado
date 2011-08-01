package com.bstek.dorado.config.xml;

import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.DefinitionInitOperation;
import com.bstek.dorado.util.Assert;

public class SubNodeToPropertyParser extends ConfigurableDispatchableXmlParser {
	private String property;

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
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
		}
		else {
			PropertyUtils.setProperty(object, property, element);
		}
	}
}
