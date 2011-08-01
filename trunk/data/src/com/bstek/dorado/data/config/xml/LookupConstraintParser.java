package com.bstek.dorado.data.config.xml;

import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.DefinitionUtils;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.data.type.property.Lookup;
import com.bstek.dorado.data.type.property.LookupConstraint;
import com.bstek.dorado.util.Assert;

/**
 * 数据参照属性中约束条件的解析器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 18, 2008
 */
public class LookupConstraintParser extends GenericObjectParser {

	private static class AddLookupConstraintOperation implements Operation {
		private ObjectDefinition lookupConstraint;

		public AddLookupConstraintOperation(ObjectDefinition lookupConstraint) {
			this.lookupConstraint = lookupConstraint;
		}

		public void execute(Object object, CreationContext context)
				throws Exception {
			Lookup lookupProperty = (Lookup) object;
			lookupProperty.addConstraint((LookupConstraint) DefinitionUtils
					.getRealValue(lookupConstraint, context));
		}
	}

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		definition.setImpl(getImpl());

		Map<String, Object> properties = parseProperties(element, context);
		String keyLookupProperty = (String) properties
				.get(DataXmlConstants.ATTRIBUTE_LOOKUP_KEY_PROPERTY);
		Assert.notEmpty(keyLookupProperty);

		definition.getProperties().putAll(properties);
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		ObjectDefinition lookupConstraint = (ObjectDefinition) super.doParse(
				node, context);
		return new AddLookupConstraintOperation(lookupConstraint);
	}

}
