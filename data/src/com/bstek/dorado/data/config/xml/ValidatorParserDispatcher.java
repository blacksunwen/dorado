package com.bstek.dorado.data.config.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.CreationContext;
import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.config.definition.Operation;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.data.type.property.PropertyDef;
import com.bstek.dorado.data.type.validator.Validator;
import com.bstek.dorado.data.type.validator.ValidatorTypeRegisterInfo;
import com.bstek.dorado.data.type.validator.ValidatorTypeRegistry;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-27
 */
public class ValidatorParserDispatcher extends GenericParser {

	private static class AddValidatorOperation implements Operation {
		private Definition validator;

		public AddValidatorOperation(Definition validator) {
			this.validator = validator;
		}

		public void execute(Object object, CreationContext context)
				throws Exception {
			PropertyDef propertyDef = (PropertyDef) object;
			List<Validator> validators = propertyDef.getValidators();
			if (validators == null) {
				validators = new ArrayList<Validator>();
				propertyDef.setValidators(validators);
			}
			validators.add((Validator) validator.create(context));
		}
	}

	private ValidatorTypeRegistry validatorTypeRegistry;

	public void setValidatorTypeRegistry(
			ValidatorTypeRegistry validatorTypeRegistry) {
		this.validatorTypeRegistry = validatorTypeRegistry;
	}

	protected boolean shouldProcessImport() {
		return false;
	}

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		XmlParser parser = null;

		Element element = (Element) node;
		String type = element
				.getAttribute(DataXmlConstants.ATTRIBUTE_VALIDATOR_TYPE);
		ValidatorTypeRegisterInfo registryInfo = validatorTypeRegistry
				.getTypeRegisterInfo(type);
		if (registryInfo != null) {
			parser = (XmlParser) registryInfo.getParser();
		} else {
			throw new XmlParseException("Unrecognized Validator type[" + type
					+ "].", element, context);
		}
		if (parser == null) {
			throw new XmlParseException("Can not get Parser for Validator of ["
					+ type + "] type.", element, context);
		}

		Definition validator = (Definition) parser.parse(node, context);
		return new AddValidatorOperation(validator);
	}

}
