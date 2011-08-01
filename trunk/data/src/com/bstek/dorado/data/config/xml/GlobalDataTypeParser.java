package com.bstek.dorado.data.config.xml;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bstek.dorado.config.ParseContext;
import com.bstek.dorado.config.xml.XmlConstants;
import com.bstek.dorado.config.xml.XmlParseException;
import com.bstek.dorado.data.Constants;
import com.bstek.dorado.data.config.definition.DataTypeDefinition;

/**
 * 全局DataType的解析分派器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 8, 2008
 */
public class GlobalDataTypeParser extends DataTypeParserSupport {

	@Override
	protected Object doParse(Node node, ParseContext context) throws Exception {
		Element element = (Element) node;
		DataParseContext dataContext = (DataParseContext) context;
		Set<Node> parsingNodes = dataContext.getParsingNodes();
		Map<String, DataTypeDefinition> parsedDataTypes = dataContext
				.getParsedDataTypes();

		String name = element.getAttribute(XmlConstants.ATTRIBUTE_NAME);
		if (StringUtils.isEmpty(name)) {
			name = null;
		}

		DataTypeDefinition dataType = null;
		if (name != null) {
			// Comment 11/04/26 为了处理View中私有DataObject与Global DataObject重名的问题
			// DefinitionManager<DataTypeDefinition> dataTypeDefinitionManager =
			// dataContext
			// .getDataTypeDefinitionManager();
			// dataType = dataTypeDefinitionManager.getDefinition(name);
			if (dataType == null) {
				dataType = parsedDataTypes.get(name);
			}
		}

		if (dataType == null) {
			parsingNodes.add(element);
			dataContext
					.setPrivateObjectName(Constants.PRIVATE_DATA_OBJECT_PREFIX
							+ DataXmlConstants.PATH_DATE_TYPE_SHORT_NAME
							+ Constants.PRIVATE_DATA_OBJECT_SUBFIX + name);
			dataType = (DataTypeDefinition) internalParse(node, dataContext);

			dataType.setName(name);
			dataType.setId(dataContext.getDataObjectIdPrefix() + name);

			Class<?> matchType = (Class<?>) dataType.getProperties().remove(
					DataXmlConstants.ATTRIBUTE_MATCH_TYPE);
			dataType.setMatchType(matchType);

			Class<?> creationType = (Class<?>) dataType.getProperties().remove(
					DataXmlConstants.ATTRIBUTE_CREATION_TYPE);
			if (creationType != null) {
				if (matchType != null
						&& !matchType.isAssignableFrom(creationType)) {
					throw new XmlParseException("The CreationType ["
							+ creationType
							+ "] is not a sub type of the MatchType ["
							+ matchType + "].", element, context);
				}
				dataType.setCreationType(creationType);
			}

			dataType.setGlobal(true);

			dataContext.restorePrivateObjectName();
			parsingNodes.clear();

			if (name != null) {
				parsedDataTypes.put(name, dataType);
			}
		}
		return dataType;
	}
}
