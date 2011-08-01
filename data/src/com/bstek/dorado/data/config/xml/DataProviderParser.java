package com.bstek.dorado.data.config.xml;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.definition.DefinitionReference;
import com.bstek.dorado.config.definition.ObjectDefinition;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParser;
import com.bstek.dorado.config.xml.XmlParserUtils;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;

/**
 * DataProvider解析器的抽象类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 2, 2007
 */
public class DataProviderParser extends DataObjectParser {

	/**
	 * 用于解析各种数据节点的解析器。
	 */
	protected XmlParser dataParser;

	/**
	 * 设置用于解析各种数据节点的解析器。
	 */
	public void setDataParser(XmlParser dataParser) {
		this.dataParser = dataParser;
	}

	public DataProviderParser() {
		setScopable(true);
		setInheritable(true);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected DefinitionReference<DataProviderDefinition>[] getParentDefinitionReferences(
			String parentNameText, ParseContext context) throws Exception {
		DefinitionReference<DataProviderDefinition> parentReference = ((DataParseContext) context)
				.getDataProviderReference(parentNameText);
		if (parentReference != null) {
			return new DefinitionReference[] { parentReference };
		} else {
			return null;
		}
	}

	@Override
	protected ObjectDefinition createDefinition(Element element,
			ParseContext context) {
		return new DataProviderDefinition();
	}

	@Override
	protected void initDefinition(ObjectDefinition definition, Element element,
			ParseContext context) throws Exception {
		super.initDefinition(definition, element, context);

		DataProviderDefinition dataProvider = (DataProviderDefinition) definition;

		String interceptor = (String) dataProvider.getProperties().remove(
				XmlConstants.ATTRIBUTE_INTERCEPTOR);
		if (StringUtils.isNotEmpty(interceptor)) {
			dataProvider.setInterceptor(interceptor);
		}

		Node parameterNode = XmlParserUtils.getPropertyNode(element,
				DataXmlConstants.ATTRIBUTE_PARAMETER,
				DataXmlConstants.PARAMETER);
		if (parameterNode != null) {
			Object parameter = dataParser.parse(parameterNode, context);
			definition.getProperties().put(
					DataXmlConstants.ATTRIBUTE_PARAMETER, parameter);
		}
	}
}
